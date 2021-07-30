package io.taig.pygments

object HelloWord {
  val Brainfuck = """Hello World in Brainfuck
                    |
                    |++++++++++[>+++++++>++++++++++>+++<<<-]>++.>+.+++++++
                    |..+++.>++.<<+++++++++++++++.>.+++.------.--------.>+.""".stripMargin

  val C = """/* Hello World in C, Ansi-style */
            |
            |#include <stdio.h>
            |#include <stdlib.h>
            |
            |int main(void)
            |{
            |  puts("Hello World!");
            |  return EXIT_SUCCESS;
            |}""".stripMargin

  val Cobol = """       * Hello World in COBOL
                |
                |*****************************
                |IDENTIFICATION DIVISION.
                |PROGRAM-ID. HELLO.
                |ENVIRONMENT DIVISION.
                |DATA DIVISION.
                |PROCEDURE DIVISION.
                |MAIN SECTION.
                |DISPLAY "Hello World!"
                |STOP RUN.
                |****************************""".stripMargin

  val CoffeeSCript = """// Hello world in CoffeeScript
                       |
                       |alert "Hello, World!"""".stripMargin

  val CPlusPlus = """// Hello World in C++ (pre-ISO)
                    |
                    |#include <iostream.h>
                    |
                    |main()
                    |{
                    |    cout << "Hello World!" << endl;
                    |    return 0;
                    |}""".stripMargin

  val Crystal = """# Hello world in Crystal
                  |
                  |puts "Hello World"""".stripMargin

  val Css = """/* Hello World in CSS */
              |body:before {
              |    content: "Hello World";
              |}""".stripMargin

  val CSharp = """//Hello World in C#
                 |class HelloWorld
                 |{
                 |    static void Main()
                 |    {
                 |        System.Console.WriteLine("Hello, World!");
                 |    }
                 |}""".stripMargin

  val Dart = """// Hello world in Dart
               |
               |main() {
               |   print('Hello world!');
               |}""".stripMargin

  val Elm = """-- Hello world in Elm
              |
              |import Text
              |
              |main = Text.plainText "Hello, world!"""".stripMargin

  val Fortran = """C     Hello World in Fortran
                  |
                  |      PROGRAM HELLO
                  |      WRITE (*,100)
                  |      STOP
                  |  100 FORMAT (' Hello World! ' /)
                  |      END""".stripMargin

  val FSharp =
    """(* Hello World in F# *)
      |
      |printf "Hello World!\n"""".stripMargin

  val Go = """// Hello world in Go
             |
             |package main
             |import "fmt"
             |func main() {
             | fmt.Printf("Hello World\n")
             |}""".stripMargin

  val Groovy = """// Hello World in Groovy
                 |
                 |println "Hello World"""".stripMargin

  val Haskell = """-- Hello World in Haskell
                  |
                  |main = putStrLn "Hello World"""".stripMargin

  val Html = """<HTML>
               |<!-- Hello World in HTML -->
               |<HEAD>
               |<TITLE>Hello World!</TITLE>
               |</HEAD>
               |<BODY>
               |Hello World!
               |</BODY>
               |</HTML>""".stripMargin

  val Java = """class HelloWorld {
               |  static public void main(String args[]) {
               |    System.out.println("Hello World!");
               |  }
               |}""".stripMargin

  val JavaScript = """console.log("Hello World");"""

  val Julia = """# Hello world in Julia
                |
                |println("Hello, World!")""".stripMargin

  val Lua = """# Hello World in Lua
              |
              |print "Hello world"""".stripMargin

  val Matlab = """% Hello World in MATLAB.
                 |
                 |disp('Hello World');""".stripMargin

  val Php =
    """<?php
      |  // Hello world in PHP
      |  echo 'Hello World!';
      |?>""".stripMargin

  val Python = """# Hello world in Python 3 (aka Python 3000)
                 |
                 |print("Hello World")""".stripMargin

  val R = """# Hello World in R
            |cat("Hello world\n")""".stripMargin

  val Ruby = """# Hello World in Ruby
               |puts "Hello World!"""".stripMargin

  val Scala = """object HelloWorld extends App {
                |  println("Hello world!")
                |}""".stripMargin

  val Sql = """# Hello World in SQL
              |
              |SELECT 'Hello World';""".stripMargin

  val Swift = """// Hello world in Swift
                |
                |println("Hello, world!")""".stripMargin
}
