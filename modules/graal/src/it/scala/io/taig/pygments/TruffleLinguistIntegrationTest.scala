package io.taig.pygments

import cats.effect.{IO, Resource}

final class TruffleLinguistIntegrationTest extends LinguistTest {
  override val linguist: Resource[IO, Pygments[IO]] = GraalPythonPygments.default[IO]
}
