package io.taig.pygments

import java.nio.file.Paths

import cats.effect.{IO, Resource}

final class GraalVmPythonPygmentsIntegrationTest extends PygmentsTest {
  override val pygments: Resource[IO, Pygments[IO]] = {
    val executable = Paths.get(System.getenv("JAVA_HOME") + "/languages/python/scala-pygments/bin/python")
    GraalVmPythonPygments.default[IO](executable)
  }

  test("don't wrap values in apostrophes") {
    pygments.use { pygments =>
      assertIO(
        obtained = pygments.tokenize("Java", "foobar").map(_.headOption.map(_.code)),
        returns = Some("foobar")
      )
    }
  }

  test("don't add a trailing linebreak") {
    pygments.use { pygments =>
      for {
        withLinebreak <- pygments.tokenize("Java", "foobar\n")
        withoutLinebreak <- pygments.tokenize("Java", "foobar")
      } yield {
        assertEquals(
          obtained = withLinebreak,
          expected = List(Fragment(Token.Name(None), "foobar"), Fragment(Token.Text(None), "\\n"))
        )
        assertEquals(obtained = withoutLinebreak, expected = List(Fragment(Token.Name(None), "foobar")))
      }
    }
  }
}
