package io.taig.pygments

abstract class Pygments[F[_]] {
  def tokenize(lexer: String, code: String): F[List[Fragment]]
}

object Pygments {
  private val Linebreak = "\\n"

  def parseFragmentResult(code: String, result: Array[Byte]): List[Fragment] = {
    val endsWithLinebreak = code.endsWith("\n")
    val builder = List.newBuilder[Fragment]
    val lines = new String(result).split('\n')
    val length = lines.length
    var index = 0

    while (index < length) {
      val line = lines(index)

      if (index == length - 1 && !endsWithLinebreak) {
        val fragment = Fragment.unsafeParse(line)
        if (fragment.code == Linebreak) ()
        else if (fragment.code.endsWith(Linebreak))
          builder += fragment.copy(code = fragment.code.dropRight(Linebreak.length))
        else builder += fragment
      } else builder += Fragment.unsafeParse(line)

      index += 1
    }

    builder.result()
  }
}
