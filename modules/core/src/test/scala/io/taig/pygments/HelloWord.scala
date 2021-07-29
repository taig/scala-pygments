package io.taig.pygments

object HelloWord {
  val Java = """class HelloWorld {
               |  static public void main( String args[] ) {
               |    System.out.println( "Hello World!" );
               |  }
               |}""".stripMargin

  val JavaScript = """console.log("Hello World");"""

  val Scala = """object HelloWorld extends App {
                |  println("Hello world!")
                |}""".stripMargin
}
