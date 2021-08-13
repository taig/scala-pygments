package io.taig.pygments

import java.nio.file.Path

import cats.effect.std.Semaphore
import cats.effect.{Async, Resource, Sync}
import cats.syntax.all._
import org.graalvm.polyglot.{Context, HostAccess, PolyglotAccess}

final class GraalVmPythonPygments[F[_]](lock: Semaphore[F])(context: Context)(implicit F: Sync[F]) extends Pygments[F] {
  override def tokenize(lexer: String, code: String): F[List[Fragment]] = lock.permit.surround {
    F.blocking {
      context.getPolyglotBindings.putMember("code", code)

      val script =
        s"""from pygments import highlight
           |from pygments.lexers import ${lexer}Lexer
           |from pygments.formatters import RawTokenFormatter
           |import polyglot
           |
           |highlight(polyglot.import_value('code'), ${lexer}Lexer(), RawTokenFormatter())""".stripMargin

      val result = context.eval("python", script)

      val size = result.getArraySize
      val bytes = new Array[Byte](size.toInt)
      var index = 0

      while (index < result.getArraySize) {
        bytes(index) = result.getArrayElement(index.toLong).asByte()
        index += 1
      }

      bytes
    }.flatMap { bytes =>
      new String(bytes).split('\n').toList.traverse { value =>
        value.indexOf('\t') match {
          case -1 => F.raiseError[Fragment](new IllegalStateException("Unexpected pygments format"))
          case index =>
            val token = value.substring(0, index)
            val code = value.substring(index + 1)
            Token.parse(token).liftTo[F](new IllegalStateException(s"Unknown token '$value'")).map(Fragment(_, code))
        }
      }
    }
  }
}

object GraalVmPythonPygments {
  def apply[F[_]](context: Context)(implicit F: Async[F]): F[Pygments[F]] =
    Semaphore[F](1).map(new GraalVmPythonPygments[F](_)(context))

  def default[F[_]](python: Path)(implicit F: Async[F]): Resource[F, Pygments[F]] = {
    val context = F.delay {
      Context
        .newBuilder("python")
        .allowExperimentalOptions(true)
        .allowHostAccess(HostAccess.ALL)
        .allowIO(true)
        .allowNativeAccess(true)
        .allowPolyglotAccess(PolyglotAccess.ALL)
        .option("python.ForceImportSite", "true")
        .option("python.Executable", python.toString)
        .build()
    }

    Resource.fromAutoCloseable(context).evalMap(GraalVmPythonPygments[F])
  }
}
