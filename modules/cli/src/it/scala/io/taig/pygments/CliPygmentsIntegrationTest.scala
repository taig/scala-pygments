package io.taig.pygments

import cats.effect.IO

import scala.concurrent.duration._

final class CliPygmentsIntegrationTest extends PygmentsTest {
  override val munitTimeout: Duration = 60.seconds

  override val pygments: Fixture[Pygments[IO]] = new Fixture[Pygments[IO]]("pygments") {
    var instance: Pygments[IO] = _

    override def apply(): Pygments[IO] = instance

    override def beforeAll(): Unit =
      this.instance = CliPygments[IO].unsafeRunSync()

    override def afterAll(): Unit =
      this.instance = null
  }
}
