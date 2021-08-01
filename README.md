# Scala Pygments

[![CI & CD](https://github.com/taig/scala-pygments/actions/workflows/main.yml/badge.svg)](https://github.com/taig/scala-pygments/actions/workflows/main.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.taig/scala-pygments-core_3)](https://index.scala-lang.org/taig/scala-pygments/)


> A Scala wrapper around [Pygments](https://github.com/pygments/pygments) based on [GraalVM's Python Runtime](https://www.graalvm.org/reference-manual/python/)

## Prerequisites

GraalVM with support for Python must be used as the Java runtime  

```
gu install python
```

Pygments must be installed in a GraalVM Python environment  

```
pip install Pygments
```

To run the integration tests, a python environment must first be created

```
graalpython -m venv ${JAVA_HOME}/languages/python/scala-pygments/
${JAVA_HOME}/languages/python/scala-pygments/bin/pip install Pygments
```

## Installation

**sbt**

```scala
libraryDependencies ++=
  "io.taig" %% "scala-pygments-core" % "x.y.z" :: 
  "io.taig" %% "scala-pygments-graalvm" % "x.y.z" ::
  Nil
```

## Usage

Currently, this library only exposes a single method, allowing to tokenize source code with a specific lexer.

```scala
import cats.effect.{IO, IOApp}
import io.taig.pygments.GraalVmPythonPygments

object Playground extends IOApp.Simple {
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