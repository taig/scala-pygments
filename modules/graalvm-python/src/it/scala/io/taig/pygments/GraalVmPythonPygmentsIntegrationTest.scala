package io.taig.pygments

import cats.effect.{IO, Resource}

import java.nio.file.{Path, Paths}

object GraalVmPythonPygmentsIntegrationTest {
  val Executable: Path = Paths.get(System.getenv("JAVA_HOME") + "/languages/python/scala-pygments/bin/python")
}

final class DefaultGraalVmPythonPygmentsIntegrationTest extends PygmentsTest {
  override val pygments: Resource[IO, Pygments[IO]] =
    GraalVmPythonPygments.default[IO](GraalVmPythonPygmentsIntegrationTest.Executable)

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

final class PooledGraalVmPythonPygmentsIntegrationTest extends PygmentsTest {
  override val pygments: Resource[IO, Pygments[IO]] =
    GraalVmPythonPygments.pooled[IO](GraalVmPythonPygmentsIntegrationTest.Executable, size = 2)
}
