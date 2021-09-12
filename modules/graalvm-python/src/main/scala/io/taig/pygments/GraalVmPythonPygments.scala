package io.taig.pygments

import cats.effect.std.{Queue, Semaphore}
import cats.effect.{Async, Resource, Sync}
import cats.syntax.all._
import org.graalvm.polyglot.{Context, HostAccess, PolyglotAccess}

import java.nio.file.Path

final class GraalVmPythonPygments[F[_]](contexts: Resource[F, Context])(implicit F: Sync[F]) extends Pygments[F] {
  override def tokenize(lexer: String, code: String): F[List[Fragment]] = contexts.use { context =>
    F.blocking {
      context.getPolyglotBindings.putMember("code", code)

      val script =
        s"""from pygments.lexers import ${lexer}Lexer
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
      F.delay {
        val builder = List.newBuilder[Fragment]
        val lines = new String(bytes).split('\n')
        val length = lines.length
        var index = 0

        while (index < length) {
          if (index == length - 1 && !code.endsWith("\n")) index += 1
          else {
            val line = lines(index)

            line.indexOf('\t') match {
              case -1 => throw new IllegalStateException("Unexpected pygments format")
              case index =>
                val token = line.substring(0, index)
                val code = line.substring(index + 2, line.length - 1)
                Token.parse(token) match {
                  case Some(token) => builder += Fragment(token, code)
                  case None        => throw new IllegalStateException(s"Unknown token '$token'")
                }
            }

            index += 1
          }
        }

        builder.result()
      }
    }
  }
}

object GraalVmPythonPygments {
  def apply[F[_]](context: Context)(implicit F: Async[F]): F[Pygments[F]] =
    Semaphore[F](1).map(lock => new GraalVmPythonPygments[F](lock.permit.as(context)))

  def default[F[_]](executable: Path)(implicit F: Async[F]): Resource[F, Pygments[F]] =
    context[F](executable).evalMap(GraalVmPythonPygments[F])

  def pooled[F[_]: Async](executable: Path, size: Int): Resource[F, Pygments[F]] =
    Resource.eval(Queue.unbounded[F, Context]).flatMap { queue =>
      val contexts = Resource.make(queue.take)(queue.offer)

      List
        .fill(size)(context[F](executable))
        .parTraverse(_.evalTap(queue.offer))
        .as(new GraalVmPythonPygments[F](contexts))
    }

  def context[F[_]](executable: Path)(implicit F: Sync[F]): Resource[F, Context] = Resource.fromAutoCloseable {
    F.delay {
      val context = Context
        .newBuilder("python")
        .allowExperimentalOptions(true)
        .allowHostAccess(HostAccess.ALL)
        .allowIO(true)
        .allowNativeAccess(true)
        .allowPolyglotAccess(PolyglotAccess.ALL)
        .option("python.ForceImportSite", "true")
        .option("python.Executable", executable.toString)
        .build()

      context.eval(
        "python",
        """from pygments import highlight
          |from pygments.formatters import RawTokenFormatter
          |import polyglot""".stripMargin
      )

      context
    }
  }
}
