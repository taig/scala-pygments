package io.taig.pygments

abstract class Pygments[F[_]] {
  def tokenize(lexer: String, code: String): F[List[Fragment]]
}
