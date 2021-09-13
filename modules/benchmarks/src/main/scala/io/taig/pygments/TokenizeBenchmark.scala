package io.taig.pygments

import java.util.concurrent.TimeUnit

import cats.effect.unsafe.implicits.global
import cats.effect.{IO, Resource}
import org.openjdk.jmh.annotations._

@OutputTimeUnit(TimeUnit.SECONDS)
@BenchmarkMode(Array(Mode.Throughput))
@State(Scope.Benchmark)
abstract class TokenizeBenchmark {
  def pygments: Resource[IO, Pygments[IO]]

  private var instance: Pygments[IO] = _

  private var release: IO[Unit] = _

  @Setup(Level.Trial)
  def setup(): Unit = {
    val (instance, release) = pygments.allocated.unsafeRunSync()
    this.instance = instance
    this.release = release
  }

  @TearDown(Level.Trial)
  def teardown(): Unit = {
    release.unsafeRunSync()
    instance = null
    release = null
  }

  @Benchmark
  def tokenizeShort(): Unit = {
    instance.tokenize("Scala", Samples.short).unsafeRunSync()
    ()
  }

  @Benchmark
  def tokenizeMedium(): Unit = {
    instance.tokenize("Scala", Samples.medium).unsafeRunSync()
    ()
  }

  @Benchmark
  def tokenizeLong(): Unit = {
    instance.tokenize("Scala", Samples.long).unsafeRunSync()
    ()
  }
}