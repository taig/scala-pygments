package io.taig.pygments

import cats.effect.IO
import cats.syntax.all._
import munit.CatsEffectSuite

import scala.io.Source

abstract class PygmentsTest extends CatsEffectSuite {
  def pygments: Fixture[Pygments[IO]]

  override def munitFixtures: Seq[Fixture[_]] = List(pygments)

  test("Brainfuck") {
    pygments().tokenize("Brainfuck", HelloWord.Brainfuck).map(obtained => assert(obtained.nonEmpty))
  }

  test("C") {
    pygments().tokenize("C", HelloWord.C).map(obtained => assert(obtained.nonEmpty))
  }

  test("Cobol") {
    pygments().tokenize("Cobol", HelloWord.Cobol).map(obtained => assert(obtained.nonEmpty))
  }

  test("CoffeeScript") {
    pygments().tokenize("CoffeeScript", HelloWord.CoffeeSCript).map(obtained => assert(obtained.nonEmpty))
  }

  test("Crystal") {
    pygments().tokenize("Crystal", HelloWord.Crystal).map(obtained => assert(obtained.nonEmpty))
  }

  test("CSS") {
    pygments().tokenize("Css", HelloWord.Css).map(obtained => assert(obtained.nonEmpty))
  }

  test("C++") {
    pygments().tokenize("Cpp", HelloWord.CPlusPlus).map(obtained => assert(obtained.nonEmpty))
  }

  test("C#") {
    pygments().tokenize("CSharp", HelloWord.CSharp).map(obtained => assert(obtained.nonEmpty))
  }

  test("Dart") {
    pygments().tokenize("Dart", HelloWord.Dart).map(obtained => assert(obtained.nonEmpty))
  }

  test("Elm") {
    pygments().tokenize("Elm", HelloWord.Elm).map(obtained => assert(obtained.nonEmpty))
  }

  test("Fortran") {
    pygments().tokenize("Fortran", HelloWord.Fortran).map(obtained => assert(obtained.nonEmpty))
  }

  test("F#") {
    pygments().tokenize("FSharp", HelloWord.FSharp).map(obtained => assert(obtained.nonEmpty))
  }

  test("Go") {
    pygments().tokenize("Go", HelloWord.Go).map(obtained => assert(obtained.nonEmpty))
  }

  test("Groovy") {
    pygments().tokenize("Groovy", HelloWord.Groovy).map(obtained => assert(obtained.nonEmpty))
  }

  test("Haskell") {
    pygments().tokenize("Haskell", HelloWord.Haskell).map(obtained => assert(obtained.nonEmpty))
  }

  test("HTML") {
    pygments().tokenize("Html", HelloWord.Html).map(obtained => assert(obtained.nonEmpty))
  }

  test("Java") {
    pygments().tokenize("Java", HelloWord.Java).map(obtained => assert(obtained.nonEmpty))
  }

  test("JavaScript") {
    pygments().tokenize("Javascript", HelloWord.JavaScript).map(obtained => assert(obtained.nonEmpty))
  }

  test("Julia") {
    pygments().tokenize("Julia", HelloWord.Julia).map(obtained => assert(obtained.nonEmpty))
  }

  test("Lua") {
    pygments().tokenize("Lua", HelloWord.Lua).map(obtained => assert(obtained.nonEmpty))
  }

  test("MATLAB") {
    pygments().tokenize("Matlab", HelloWord.Matlab).map(obtained => assert(obtained.nonEmpty))
  }

  test("PHP") {
    pygments().tokenize("Php", HelloWord.Php).map(obtained => assert(obtained.nonEmpty))
  }

  test("Python") {
    pygments().tokenize("Python", HelloWord.Python).map(obtained => assert(obtained.nonEmpty))
  }

  test("R") {
    pygments().tokenize("S", HelloWord.R).map(obtained => assert(obtained.nonEmpty))
  }

  test("Ruby") {
    pygments().tokenize("Ruby", HelloWord.Ruby).map(obtained => assert(obtained.nonEmpty))
  }

  test("Scala") {
    pygments().tokenize("Scala", HelloWord.Scala).map(obtained => assert(obtained.nonEmpty))
  }

  test("SQL") {
    pygments().tokenize("Sql", HelloWord.Sql).map(obtained => assert(obtained.nonEmpty))
  }

  test("Swift") {
    pygments().tokenize("Swift", HelloWord.Swift).map(obtained => assert(obtained.nonEmpty))
  }

  test("Build.hs") {
    IO.blocking(Source.fromInputStream(getClass.getResourceAsStream("/Build.hs")).mkString)
      .flatMap(code => pygments().tokenize("Haskell", code))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("core.rb") {
    IO.blocking(Source.fromInputStream(getClass.getResourceAsStream("/core.rb")).mkString)
      .flatMap(code => pygments().tokenize("Ruby", code))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("Expr.java") {
    IO.blocking(Source.fromInputStream(getClass.getResourceAsStream("/Expr.java")).mkString)
      .flatMap(code => pygments().tokenize("Java", code))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("module.py") {
    IO.blocking(Source.fromInputStream(getClass.getResourceAsStream("/module.py")).mkString)
      .flatMap(code => pygments().tokenize("Python", code))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("MonadCancel.scala") {
    IO.blocking(Source.fromInputStream(getClass.getResourceAsStream("/MonadCancel.scala")).mkString)
      .flatMap(code => pygments().tokenize("Scala", code))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("ReactDOM.js") {
    IO.blocking(Source.fromInputStream(getClass.getResourceAsStream("/ReactDOM.js")).mkString)
      .flatMap(code => pygments().tokenize("Javascript", code))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("Resource.scala") {
    IO.blocking(Source.fromInputStream(getClass.getResourceAsStream("/Resource.scala")).mkString)
      .flatMap(code => pygments().tokenize("Scala", code))
      .map(obtained => assert(obtained.nonEmpty))
  }

  test("don't wrap values in apostrophes") {
    assertIO(
      obtained = pygments().tokenize("Java", "foobar").map(_.headOption.map(_.code)),
      returns = Some("foobar")
    )
  }

  test("don't add a trailing linebreak (Java)") {
    for {
      withLinebreak <- pygments().tokenize("Java", "foobar\n")
      withoutLinebreak <- pygments().tokenize("Java", "foobar")
    } yield {
      assertEquals(
        obtained = withLinebreak,
        expected = List(Fragment(Token.Name(None), "foobar"), Fragment(Token.Text(None), "\\n"))
      )
      assertEquals(obtained = withoutLinebreak, expected = List(Fragment(Token.Name(None), "foobar")))
    }
  }

  test("don't add a trailing linebreak (Html)") {
    for {
      withLinebreak <- pygments().tokenize("Html", "foobar\n")
      withoutLinebreak <- pygments().tokenize("Html", "foobar")
    } yield {
      assertEquals(obtained = withLinebreak, expected = List(Fragment(Token.Text(None), "foobar\\n")))
      assertEquals(obtained = withoutLinebreak, expected = List(Fragment(Token.Text(None), "foobar")))
    }
  }

  test("concurrent access") {
    val java = List.fill(100)(("Java", HelloWord.Java))
    val javascript = List.fill(100)(("Javascript", HelloWord.JavaScript))
    val scala = List.fill(100)(("Scala", HelloWord.Scala))
    val all = java ++ javascript ++ scala

    for {
      java <- pygments().tokenize("Java", HelloWord.Java)
      javascript <- pygments().tokenize("Javascript", HelloWord.JavaScript)
      scala <- pygments().tokenize("Scala", HelloWord.Scala)
      obtained <- all.parTraverse { case (language, code) => pygments().tokenize(language, code) }
    } yield {
      assertEquals(obtained, expected = List.fill(100)(java) ++ List.fill(100)(javascript) ++ List.fill(100)(scala))
    }
  }
}
