# Scala Pygments

[![CI & CD](https://github.com/taig/scala-pygments/actions/workflows/main.yml/badge.svg)](https://github.com/taig/scala-pygments/actions/workflows/main.yml)
[![scala-pygments-core Scala version support](https://index.scala-lang.org/taig/scala-pygments/scala-pygments-core/latest-by-scala-version.svg)](https://index.scala-lang.org/taig/scala-pygments/scala-pygments-core)
[![Scala Steward badge](https://img.shields.io/badge/Scala_Steward-helping-blue.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=)](https://scala-steward.org)


> A Scala wrapper around [Pygments](https://github.com/pygments/pygments) based on [GraalVM's Python Runtime](https://www.graalvm.org/reference-manual/python/)

## Prerequisites

GraalVM with support for Python must be used as the Java runtime  

```
gu install python
```

Create a Python environment for scala-pygments

```
graalpython -m venv ${JAVA_HOME}/languages/python/scala-pygments/
```

Install Pygments in the new environment  

```
${JAVA_HOME}/languages/python/scala-pygments/bin/pip install Pygments
```

## Installation

**sbt**

```scala
libraryDependencies ++=
  "io.taig" %%% "scala-pygments-core" % "x.y.z" :: 
  "io.taig" %% "scala-pygments-graalvm-python" % "x.y.z" ::
  "io.taig" %% "scala-pygments-cli" % "x.y.z" ::
  Nil
```

## Usage

Currently, this library only exposes a single method, allowing to tokenize source code with a specific lexer.

```scala
import cats.effect.{IO, IOApp}
import io.taig.pygments.GraalVmPythonPygments

object App extends IOApp.Simple {
  val python = s"${System.getenv("JAVA_HOME")}/languages/python/scala-pygments/bin/python"

  override def run: IO[Unit] =
    GraalVmPythonPygments
      .default[IO](python)
      .use(_.tokenize("Scala", """println("Hello world!")"""))
      .flatMap(IO.println)
}
```

```
sbt> run
List(Fragment(Name(None),'println'), Fragment(Punctuation,'('), Fragment(Literal(String(None)),'"Hello world!"'), Fragment(Punctuation,')'), Fragment(Text(None),'\n'))
```

## Benchmarks

```shell
> benchmarks/Jmh/run -wi 10 -i 5 -f1 -t4
[info] Benchmark                                              Mode  Cnt     Score    Error  Units
[info] CliTokenizeBenchmark.tokenizeLong                     thrpt    5    12,419 ±  0,469  ops/s
[info] CliTokenizeBenchmark.tokenizeMedium                   thrpt    5    16,245 ±  0,643  ops/s
[info] CliTokenizeBenchmark.tokenizeShort                    thrpt    5    16,699 ±  0,391  ops/s
[info] GraalVmPythonDefaultTokenizeBenchmark.tokenizeLong    thrpt    5     0,958 ±  0,063  ops/s
[info] GraalVmPythonDefaultTokenizeBenchmark.tokenizeMedium  thrpt    5    24,724 ±  0,239  ops/s
[info] GraalVmPythonDefaultTokenizeBenchmark.tokenizeShort   thrpt    5   625,756 ± 24,527  ops/s
[info] GraalVmPythonPooledTokenizeBenchmark.tokenizeLong     thrpt    5     1,475 ±  0,375  ops/s
[info] GraalVmPythonPooledTokenizeBenchmark.tokenizeMedium   thrpt    5    87,482 ±  1,239  ops/s
[info] GraalVmPythonPooledTokenizeBenchmark.tokenizeShort    thrpt    5  2374,671 ± 32,192  ops/s
```