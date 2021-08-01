package io.taig.pygments

import cats.effect.{IO, Resource}
import cats.syntax.all._
import munit.CatsEffectSuite

import scala.io.Source

abstract class PygmentsTest extends CatsEffectSuite {
  def pygments: Resource[IO, Pygments[IO]]

  test("Brainfuck") {
    pygments
      .use(_.tokenize("Brainfuck", HelloWord.Brainfuck))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("C") {
    pygments
      .use(_.tokenize("C", HelloWord.C))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("Cobol") {
    pygments
      .use(_.tokenize("Cobol", HelloWord.Cobol))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("CoffeeScript") {
    pygments
      .use(_.tokenize("CoffeeScript", HelloWord.CoffeeSCript))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("Crystal") {
    pygments
      .use(_.tokenize("Crystal", HelloWord.Crystal))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("CSS") {
    pygments
      .use(_.tokenize("Css", HelloWord.Css))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("C++") {
    pygments
      .use(_.tokenize("Cpp", HelloWord.CPlusPlus))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("C#") {
    pygments
      .use(_.tokenize("CSharp", HelloWord.CSharp))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("Dart") {
    pygments
      .use(_.tokenize("Dart", HelloWord.Dart))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("Elm") {
    pygments
      .use(_.tokenize("Elm", HelloWord.Elm))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("Fortran") {
    pygments
      .use(_.tokenize("Fortran", HelloWord.Fortran))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("F#") {
    pygments
      .use(_.tokenize("FSharp", HelloWord.FSharp))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("Go") {
    pygments
      .use(_.tokenize("Go", HelloWord.Go))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("Groovy") {
    pygments
      .use(_.tokenize("Groovy", HelloWord.Groovy))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("Haskell") {
    pygments
      .use(_.tokenize("Haskell", HelloWord.Haskell))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("HTML") {
    pygments
      .use(_.tokenize("Html", HelloWord.Html))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("Java") {
    pygments
      .use(_.tokenize("Java", HelloWord.Java))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("JavaScript") {
    pygments
      .use(_.tokenize("Javascript", HelloWord.JavaScript))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("Julia") {
    pygments
      .use(_.tokenize("Julia", HelloWord.Julia))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("Lua") {
    pygments
      .use(_.tokenize("Lua", HelloWord.Lua))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("MATLAB") {
    pygments
      .use(_.tokenize("Matlab", HelloWord.Matlab))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("PHP") {
    pygments
      .use(_.tokenize("Php", HelloWord.Php))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("Python") {
    pygments
      .use(_.tokenize("Python", HelloWord.Python))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("R") {
    pygments
      .use(_.tokenize("S", HelloWord.R))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("Ruby") {
    pygments
      .use(_.tokenize("Ruby", HelloWord.Ruby))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("Scala") {
    pygments
      .use(_.tokenize("Scala", HelloWord.Scala))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("SQL") {
    pygments
      .use(_.tokenize("Sql", HelloWord.Sql))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("Swift") {
    pygments
      .use(_.tokenize("Swift", HelloWord.Swift))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("Build.hs") {
    IO.blocking(Source.fromInputStream(getClass.getResourceAsStream("/Build.hs")).mkString)
      .flatMap(code => pygments.use(_.tokenize("Haskell", code)))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("core.rb") {
    IO.blocking(Source.fromInputStream(getClass.getResourceAsStream("/core.rb")).mkString)
      .flatMap(code => pygments.use(_.tokenize("Ruby", code)))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("Expr.java") {
    IO.blocking(Source.fromInputStream(getClass.getResourceAsStream("/Expr.java")).mkString)
      .flatMap(code => pygments.use(_.tokenize("Java", code)))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("module.py") {
    IO.blocking(Source.fromInputStream(getClass.getResourceAsStream("/module.py")).mkString)
      .flatMap(code => pygments.use(_.tokenize("Python", code)))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("MonadCancel.scala") {
    IO.blocking(Source.fromInputStream(getClass.getResourceAsStream("/MonadCancel.scala")).mkString)
      .flatMap(code => pygments.use(_.tokenize("Scala", code)))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("ReactDOM.js") {
    IO.blocking(Source.fromInputStream(getClass.getResourceAsStream("/ReactDOM.js")).mkString)
      .flatMap(code => pygments.use(_.tokenize("Javascript", code)))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("Resource.scala") {
    IO.blocking(Source.fromInputStream(getClass.getResourceAsStream("/Resource.scala")).mkString)
      .flatMap(code => pygments.use(_.tokenize("Scala", code)))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("concurrent access") {
    val java = List.fill(100)(("Java", HelloWord.Java))
    val javascript = List.fill(100)(("Javascript", HelloWord.JavaScript))
    val scala = List.fill(100)(("Scala", HelloWord.Scala))
    val all = java ++ javascript ++ scala

    pygments.use { linguist =>
      for {
        java <- linguist.tokenize("Java", HelloWord.Java)
        javascript <- linguist.tokenize("Javascript", HelloWord.JavaScript)
        scala <- linguist.tokenize("Scala", HelloWord.Scala)
        obtained <- all.parTraverse { case (language, code) => linguist.tokenize(language, code) }
      } yield {
        assertEquals(obtained, expected = List.fill(100)(java) ++ List.fill(100)(javascript) ++ List.fill(100)(scala))
      }
    }
  }
}
