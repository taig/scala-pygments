package io.taig.pygments

import cats.effect.{IO, Resource}
import cats.syntax.all._
import munit.CatsEffectSuite

import scala.io.Source

abstract class PygmentsTest extends CatsEffectSuite {
  def pygments: Resource[IO, Pygments[IO]]

  test("Brainfuck") {
    pygments
      .use(_.highlight("Brainfuck", HelloWord.Brainfuck))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("C") {
    pygments
      .use(_.highlight("C", HelloWord.C))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("Cobol") {
    pygments
      .use(_.highlight("Cobol", HelloWord.Cobol))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("CoffeeScript") {
    pygments
      .use(_.highlight("CoffeeScript", HelloWord.CoffeeSCript))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("Crystal") {
    pygments
      .use(_.highlight("Crystal", HelloWord.Crystal))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("CSS") {
    pygments
      .use(_.highlight("Css", HelloWord.Css))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("C++") {
    pygments
      .use(_.highlight("Cpp", HelloWord.CPlusPlus))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("C#") {
    pygments
      .use(_.highlight("CSharp", HelloWord.CSharp))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("Dart") {
    pygments
      .use(_.highlight("Dart", HelloWord.Dart))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("Elm") {
    pygments
      .use(_.highlight("Elm", HelloWord.Elm))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("Fortran") {
    pygments
      .use(_.highlight("Fortran", HelloWord.Fortran))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("F#") {
    pygments
      .use(_.highlight("FSharp", HelloWord.FSharp))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("Go") {
    pygments
      .use(_.highlight("Go", HelloWord.Go))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("Groovy") {
    pygments
      .use(_.highlight("Groovy", HelloWord.Groovy))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("Haskell") {
    pygments
      .use(_.highlight("Haskell", HelloWord.Haskell))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("HTML") {
    pygments
      .use(_.highlight("Html", HelloWord.Html))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("Java") {
    pygments
      .use(_.highlight("Java", HelloWord.Java))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("JavaScript") {
    pygments
      .use(_.highlight("Javascript", HelloWord.JavaScript))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("Julia") {
    pygments
      .use(_.highlight("Julia", HelloWord.Julia))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("Lua") {
    pygments
      .use(_.highlight("Lua", HelloWord.Lua))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("MATLAB") {
    pygments
      .use(_.highlight("Matlab", HelloWord.Matlab))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("PHP") {
    pygments
      .use(_.highlight("Php", HelloWord.Php))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("Python") {
    pygments
      .use(_.highlight("Python", HelloWord.Python))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("R") {
    pygments
      .use(_.highlight("S", HelloWord.R))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("Ruby") {
    pygments
      .use(_.highlight("Ruby", HelloWord.Ruby))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("Scala") {
    pygments
      .use(_.highlight("Scala", HelloWord.Scala))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("SQL") {
    pygments
      .use(_.highlight("Sql", HelloWord.Sql))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("Swift") {
    pygments
      .use(_.highlight("Swift", HelloWord.Swift))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("Build.hs") {
    IO.blocking(Source.fromInputStream(getClass.getResourceAsStream("/Build.hs")).mkString)
      .flatMap(code => pygments.use(_.highlight("Haskell", code)))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("core.rb") {
    IO.blocking(Source.fromInputStream(getClass.getResourceAsStream("/core.rb")).mkString)
      .flatMap(code => pygments.use(_.highlight("Ruby", code)))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("Expr.java") {
    IO.blocking(Source.fromInputStream(getClass.getResourceAsStream("/Expr.java")).mkString)
      .flatMap(code => pygments.use(_.highlight("Java", code)))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("module.py") {
    IO.blocking(Source.fromInputStream(getClass.getResourceAsStream("/module.py")).mkString)
      .flatMap(code => pygments.use(_.highlight("Python", code)))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("MonadCancel.scala") {
    IO.blocking(Source.fromInputStream(getClass.getResourceAsStream("/MonadCancel.scala")).mkString)
      .flatMap(code => pygments.use(_.highlight("Scala", code)))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("ReactDOM.js") {
    IO.blocking(Source.fromInputStream(getClass.getResourceAsStream("/ReactDOM.js")).mkString)
      .flatMap(code => pygments.use(_.highlight("Javascript", code)))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("Resource.scala") {
    IO.blocking(Source.fromInputStream(getClass.getResourceAsStream("/Resource.scala")).mkString)
      .flatMap(code => pygments.use(_.highlight("Scala", code)))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("concurrent access") {
    val java = List.fill(100)(("Java", HelloWord.Java))
    val javascript = List.fill(100)(("Javascript", HelloWord.JavaScript))
    val scala = List.fill(100)(("Scala", HelloWord.Scala))
    val all = java ++ javascript ++ scala

    pygments.use { linguist =>
      for {
        java <- linguist.highlight("Java", HelloWord.Java)
        javascript <- linguist.highlight("Javascript", HelloWord.JavaScript)
        scala <- linguist.highlight("Scala", HelloWord.Scala)
        obtained <- all.parTraverse { case (language, code) => linguist.highlight(language, code) }
      } yield {
        assertEquals(obtained, expected = List.fill(100)(java) ++ List.fill(100)(javascript) ++ List.fill(100)(scala))
      }
    }
  }
}
