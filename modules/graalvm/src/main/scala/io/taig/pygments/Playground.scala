package io.taig.pygments

import cats.effect.{IO, IOApp}

object Playground extends IOApp.Simple {
  val python = "/Users/niklas/.sdkman/candidates/java/current/languages/python/scala-pygments/bin/python"
  override def run: IO[Unit] = {
    GraalVmPythonPygments
      .default[IO](python)
      .use { pygments =>
        pygments.tokenize(
          "Scala",
          """// Hello world in Scala
            |
            |object HelloWorld extends App {
            |  println("Hello world!")
            |}""".stripMargin
        )
      }
      .map(println)
  }
}
