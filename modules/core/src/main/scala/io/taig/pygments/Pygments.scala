package io.taig.pygments

abstract class Pygments[F[_]] {
  def highlight(lexer: String, code: String): F[List[Fragment]]
}
