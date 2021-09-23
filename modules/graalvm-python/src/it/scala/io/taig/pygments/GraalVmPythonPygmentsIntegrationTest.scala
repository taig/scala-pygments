package io.taig.pygments

import cats.effect.{IO, Resource}

import java.nio.file.{Path, Paths}

abstract class GraalVmPythonPygmentsIntegrationTest extends PygmentsTest {
  def resource: Resource[IO, Pygments[IO]]

  override val pygments: Fixture[Pygments[IO]] = new Fixture[Pygments[IO]]("pygments") {
    var instance: Pygments[IO] = _

    var release: IO[Unit] = IO.unit

    override def apply(): Pygments[IO] = instance

    override def beforeAll(): Unit = {
      val (instance, release) = resource.allocated.unsafeRunSync()
      this.instance = instance
      this.release = release
    }

    override def afterAll(): Unit = {
      release.unsafeRunSync()
      this.instance = null
      this.release = null
    }
  }
}

object GraalVmPythonPygmentsIntegrationTest {
  val Executable: Path = Paths.get(System.getenv("JAVA_HOME") + "/languages/python/scala-pygments/bin/python")
}

final class DefaultGraalVmPythonPygmentsIntegrationTest extends GraalVmPythonPygmentsIntegrationTest {
  override val resource: Resource[IO, Pygments[IO]] =
    GraalVmPythonPygments.default[IO](GraalVmPythonPygmentsIntegrationTest.Executable)
}

final class PooledGraalVmPythonPygmentsIntegrationTest extends GraalVmPythonPygmentsIntegrationTest {
  override val resource: Resource[IO, Pygments[IO]] =
    GraalVmPythonPygments.pooled[IO](GraalVmPythonPygmentsIntegrationTest.Executable, size = 2)
}
