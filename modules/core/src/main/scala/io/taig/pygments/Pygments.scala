package io.taig.pygments

abstract class Pygments[F[_]] {
  def highlight(language: String, code: String): F[List[Fragment]]
}
