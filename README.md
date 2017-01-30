Scala Genetic Algorithm Example README
======================================

This is an example implementation of a Genetic Algorithm with Elitism in the
Scala programming language. 

The intended audience is students learning various tricks of 
metaheuristics implementation. The goal is to quickly explore various choices
regarding elitism, providing a template for implementing selection methods, 
choosing an appropriate random number generator, etc.

It also provides examples of property-based testing to ensure correctness of
metaheuristics implementations.

Usage
-----

You'll need [sbt](http://www.scala-sbt.org/) to run the examples.

Go to the main directory, and execute `sbt test` to run the tests. Similarly,
execute `sbt run` to build the program and execute the main class.
