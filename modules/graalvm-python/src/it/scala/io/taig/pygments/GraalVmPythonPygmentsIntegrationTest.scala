package io.taig.pygments

import java.nio.file.Paths

import cats.effect.{IO, Resource}

final class GraalVmPythonPygmentsIntegrationTest extends PygmentsTest {
  override val pygments: Resource[IO, Pygments[IO]] = {
    val executable = Paths.get(System.getenv("JAVA_HOME") + "/languages/python/scala-pygments/bin/python")
    GraalVmPythonPygments.default[IO](executable)
  }
}
