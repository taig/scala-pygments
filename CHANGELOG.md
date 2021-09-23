# Changelog

## 0.0.13

_2021-09-23_

 * Add cli module
 * Propagate context initialization errors
 * Upgrade to sbt-houserules 0.3.14

## 0.0.11

_2021-09-22_

 * Change default initialization to pooled(1) to leverage asynchronous start
 * Set test fixture fields to null in afterAll

## 0.0.10

_2021-09-21_

 * Reuse context engine in pooled instances
 * Fix pooled shared engine not being closed
 * Add benchmarks module
 * Upgrade to cats-effect 3.2.9

## 0.0.8

_2021-09-12_

 * Initialize pooled instance in background

## 0.0.7

_2021-09-12_

 * Load pygments dependencies as part of context initialization
 * Upgrade to cats-effect 3.2.8
 * Upgrade to scala 3.0.2
 * Upgrade to munit 0.7.29

## 0.0.6

_2021-08-29_

 * [#2] Pooled instance (#3)
 * Upgrade to cats-effect 3.2.5

## 0.0.5

_2021-08-14_

 * Add `Token.All` which provides an exhaustive `Set` of all possible values

## 0.0.4

_2021-08-13_

 * Suppress pygments auto appended linebreak
 * Fix code values wrapped in apostrophes

## 0.0.3

_2021-08-13_

 * Rename module scala-pygments-graalvm to scala-pygments-graalvm-python
 * Chane `GraalVmPythonPygments.default(executable: String)` to `(python: Path)`
 * Upgrade to sbt-scalajs 1.7.0
 * Upgrade to cats-effect 3.2.2

## 0.0.2

_2021-08-13_

 * Release process failed, please use 0.0.3 instead

## 0.0.1

_2021-08-01_

 * Initial release