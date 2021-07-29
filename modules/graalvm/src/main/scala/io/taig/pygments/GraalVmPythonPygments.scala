package io.taig.pygments;

import cats.effect.{Async, Resource}
import cats.syntax.all._
import org.graalvm.polyglot.{Context, PolyglotAccess}

import java.util.concurrent.{Executors, TimeUnit}
import scala.concurrent.ExecutionContext

final class GraalVmPythonPygments[F[_]](context: Context, execution: ExecutionContext)(implicit F: Async[F])
    extends Pygments[F] {
  override def highlight(language: String, code: String): F[List[Fragment]] = {
    val fa = F.defer {
      context.getPolyglotBindings.putMember("code", code)

      val script =
        s"""from pygments import highlight
           |from pygments.lexers import ${language}Lexer
           |from pygments.formatters import RawTokenFormatter
           |import polyglot
           |
           |highlight(polyglot.import_value('code'), ${language}Lexer(), RawTokenFormatter())""".stripMargin

      val result = context.eval("python", script)

      val size = result.getArraySize
      val bytes = new Array[Byte](size.toInt)
      var index = 0

      while (index < result.getArraySize) {
        bytes(index) = result.getArrayElement(index.toLong).asByte()
        index += 1
      }

      new String(bytes).split('\n').toList.traverse { value =>
        value.indexOf('\t') match {
          case -1 => F.raiseError[Fragment](new IllegalStateException("Unexpected pygments format"))
          case index =>
            val token = value.substring(0, index)
            val code = value.substring(index + 1)
            Token
              .parse(token)
              .liftTo[F](new IllegalStateException(s"Unknown token '$value'"))
              .map(Fragment(_, code))
        }
      }
    }

    F.evalOn(fa, execution)
  }
}

object GraalVmPythonPygments {
  def apply[F[_]](context: Context)(implicit F: Async[F]): Resource[F, Pygments[F]] =
    Resource
      .make(F.delay(Executors.newSingleThreadExecutor())) { executor =>
        F.delay {
          executor.shutdown()
          executor.awaitTermination(10, TimeUnit.SECONDS)
          ()
        }
      }
      .map(ExecutionContext.fromExecutorService)
      .map(new GraalVmPythonPygments[F](context, _))

  def default[F[_]](executable: String)(implicit F: Async[F]): Resource[F, Pygments[F]] = {
    val context = F.delay {
      Context
        .newBuilder("python")
        .allowExperimentalOptions(true)
        .allowIO(true)
        .option("python.ForceImportSite", "true")
        .option("python.Executable", executable)
        .allowPolyglotAccess(PolyglotAccess.ALL)
        .build()
    }

    Resource.fromAutoCloseable(context).flatMap(GraalVmPythonPygments[F])
  }
}
