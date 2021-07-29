package io.taig.pygments

import cats.effect.{IO, Resource}
import cats.syntax.parallel.catsSyntaxParallelTraverse1
import munit.CatsEffectSuite

abstract class PygmentsTest extends CatsEffectSuite {
  def pygments: Resource[IO, Pygments[IO]]

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

  test("Scala") {
    pygments
      .use(_.highlight("Scala", HelloWord.Scala))
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
