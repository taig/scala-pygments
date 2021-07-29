package io.taig.pygments

import cats.effect.{IO, Resource}

final class GraalPythonPygmentsIntegrationTest extends PygmentsTest {
  override val pygments: Resource[IO, Pygments[IO]] = {
    val executable = System.getenv("JAVA_HOME") + "/languages/python/scala-pygments/bin/python"
    GraalPythonPygments.default[IO](executable)
  }
}
