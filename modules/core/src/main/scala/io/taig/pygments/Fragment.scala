package io.taig.pygments

final case class Fragment(token: Token, code: String)

object Fragment {
  def unsafeParse(line: String): Fragment = line.indexOf('\t') match {
    case -1 => throw new IllegalStateException("Unexpected pygments format")
    case index =>
      val token = line.substring(0, index)
      val code = line.substring(index + 2, line.length - 1)
      Token.parse(token) match {
        case Some(token) => Fragment(token, code)
        case None        => throw new IllegalStateException(s"Unknown token '$token'")
      }
  }
}
