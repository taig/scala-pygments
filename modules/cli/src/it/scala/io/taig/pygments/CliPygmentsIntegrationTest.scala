package io.taig.pygments

import cats.effect.{IO, Resource}

import java.nio.file.{Path, Paths}

final class CliPygmentsIntegrationTest extends PygmentsTest {
  override val pygments: Fixture[Pygments[IO]] = new Fixture[Pygments[IO]]("pygments") {
    var instance: Pygments[IO] = _

    override def apply(): Pygments[IO] = instance

    override def beforeAll(): Unit =
      this.instance = CliPygments[IO].unsafeRunSync()

    override def afterAll(): Unit =
      this.instance = null
  }
}
