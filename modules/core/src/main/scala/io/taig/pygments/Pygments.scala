package io.taig.pygments

abstract class Pygments[F[_]] {
  def tokenize(lexer: String, code: String): F[List[Fragment]]
}

object Pygments {
  def parseFragmentResult(code: String, result: Array[Byte]): List[Fragment] = {
    val builder = List.newBuilder[Fragment]
    val lines = new String(result).split('\n')
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
