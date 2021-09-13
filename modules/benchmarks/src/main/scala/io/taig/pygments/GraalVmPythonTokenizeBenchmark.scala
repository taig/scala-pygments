package io.taig.pygments

import java.nio.file.Paths

import cats.effect.{IO, Resource}

class GraalVmPythonTokenizeBenchmark extends TokenizeBenchmark {
  override val pygments: Resource[IO, Pygments[IO]] = {
    val executable = Paths.get(System.getenv("JAVA_HOME") + "/languages/python/scala-pygments/bin/python")
    GraalVmPythonPygments.default(executable)
  }
}
