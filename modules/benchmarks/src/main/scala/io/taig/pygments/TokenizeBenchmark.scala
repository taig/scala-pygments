package io.taig.pygments

import java.util.concurrent.TimeUnit
import cats.effect.unsafe.implicits.global
import cats.effect.{IO, Resource}
import org.openjdk.jmh.annotations._

import java.nio.file.{Path, Paths}

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

object TokenizeBenchmark {
  val Executable: Path = Paths.get(System.getenv("JAVA_HOME") + "/languages/python/scala-pygments/bin/python")
}

class GraalVmPythonDefaultTokenizeBenchmark extends TokenizeBenchmark {
  override val pygments: Resource[IO, Pygments[IO]] = GraalVmPythonPygments.default(TokenizeBenchmark.Executable)
}

class GraalVmPythonPooledTokenizeBenchmark extends TokenizeBenchmark {
  override val pygments: Resource[IO, Pygments[IO]] =
    GraalVmPythonPygments.pooled(TokenizeBenchmark.Executable, size = 4)
}
