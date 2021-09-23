package io.taig.pygments

import cats.effect.std.{Queue, Semaphore}
import cats.effect.{Async, Resource, Sync}
import cats.syntax.all._
import org.graalvm.polyglot.{Context, Engine, HostAccess, PolyglotAccess, Source}

import java.nio.file.Path

final class GraalVmPythonPygments[F[_]](contexts: Resource[F, Either[Throwable, Context]])(implicit F: Sync[F])
    extends Pygments[F] {
  override def tokenize(lexer: String, code: String): F[List[Fragment]] = contexts.rethrow.use { context =>
    F.blocking {
      context.getPolyglotBindings.putMember("code", code)

      val script = s"highlight(polyglot.import_value('code'), ${lexer}Lexer(), formatter)"
      val source = Source.newBuilder("python", script, "Unnamed").cached(false).buildLiteral()
      val result = context.eval(source)

      val size = result.getArraySize
      val bytes = new Array[Byte](size.toInt)
      var index = 0

      while (index < result.getArraySize) {
        bytes(index) = result.getArrayElement(index.toLong).asByte()
        index += 1
      }

      bytes
    }.map(Pygments.parseFragmentResult(code, _))
  }
}

object GraalVmPythonPygments {
  def apply[F[_]: Async](context: Context): F[Pygments[F]] =
    Semaphore[F](1).map(lock => new GraalVmPythonPygments[F](lock.permit.as(context.asRight)))

  def default[F[_]: Async](executable: Path): Resource[F, Pygments[F]] = pooled(executable, size = 1)

  def pooled[F[_]](executable: Path, size: Int)(implicit F: Async[F]): Resource[F, Pygments[F]] =
    Resource.eval(Queue.unbounded[F, Either[Throwable, Context]]).flatMap { queue =>
      val contexts = Resource.make(queue.take)(queue.offer)
      val engine = Resource.fromAutoCloseable(F.delay(Engine.create()))

      engine
        .flatMap { engine =>
          List
            .fill(size)(context[F](executable, Some(engine)).attempt)
            .parTraverse_(_.evalMap(queue.offer))
        }
        .start
        .as(new GraalVmPythonPygments[F](contexts))
    }

  def context[F[_]](executable: Path, engine: Option[Engine])(implicit F: Sync[F]): Resource[F, Context] =
    Resource.fromAutoCloseable {
      F.delay {
        val builder = Context
          .newBuilder("python")
          .allowExperimentalOptions(true)
          .allowHostAccess(HostAccess.ALL)
          .allowIO(true)
          .allowNativeAccess(true)
          .allowPolyglotAccess(PolyglotAccess.ALL)
          .option("python.ForceImportSite", "true")
          .option("python.Executable", executable.toString)

        engine.foreach(builder.engine)

        val context = builder.build()

        context.eval(
          "python",
          """from pygments import highlight
            |from pygments.formatters import RawTokenFormatter
            |from pygments.lexers import *
            |import polyglot
            |
            |formatter = RawTokenFormatter()""".stripMargin
        )

        context
      }
    }
}
