package io.taig.pygments

import cats.effect.Sync
import cats.syntax.all._

import java.nio.charset.StandardCharsets

final class CliPygments[F[_]](runtime: Runtime)(implicit F: Sync[F]) extends Pygments[F] {
  override def tokenize(lexer: String, code: String): F[List[Fragment]] = {
    F.blocking {
      val process = runtime.exec(Array("pygmentize", "-f", "tokens", "-l", lexer))
      val output = process.getOutputStream

      try output.write(code.getBytes)
      finally output.close()

      val input = process.getInputStream

      try input.readAllBytes()
      finally input.close()

    }.map(Pygments.parseFragmentResult(code, _))
  }
}

object CliPygments {
  def apply[F[_]](implicit F: Sync[F]): F[Pygments[F]] = F.blocking {
    val runtime = Runtime.getRuntime
    val process = runtime.exec(Array("pygmentize", "-V"))
    val input = process.getInputStream

    val bytes =
      try input.readAllBytes()
      finally input.close()

    val version = new String(bytes, StandardCharsets.UTF_8)
    if (version.startsWith("Pygments version")) new CliPygments[F](Runtime.getRuntime)
    else throw new IllegalStateException("pygments cli not available")
  }
}
