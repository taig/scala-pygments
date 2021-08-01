package io.taig.pygments

import cats.effect.{IO, IOApp}

object Playground extends IOApp.Simple {
  val python = s"${System.getenv("JAVA_HOME")}/languages/python/scala-pygments/bin/python"

  override def run: IO[Unit] =
    GraalVmPythonPygments
      .default[IO](python)
      .use(_.tokenize("Scala", """println("Hello world!")"""))
      .flatMap(IO.println)
}
