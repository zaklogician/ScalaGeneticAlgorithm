package scalaga.test;

import org.scalacheck.Properties
import org.scalacheck.Prop
import org.scalacheck.Prop.BooleanOperators
import org.scalacheck.Gen
import org.scalacheck.Gen.{oneOf, listOf, alphaStr, numChar}

import scalaga.RNG;
import scalaga.GeneticAlgorithm;

object TestGeneticAlgorithm extends Properties("GeneticAlgorithm") {
  
  //////////////////////////////////////////////////////////////////////////////
  // MOCK OBJECTS:
  
  object MockRNG extends scala.util.Random(0x1234) with RNG
  
  object MockGA extends GeneticAlgorithm {
    val rng: RNG = MockRNG
    
    type Gene = Int
    def randomGene: Int = rng.nextInt(2)
    val individualSize: Int = 6
    
    def fitnessFn(individual: Individual): Double = {
      individual.count(_ != 0)
    }
        
    val populationSize: Int = 100
    val eliteRatio: Double = 0.1
    val tournamentSize: Int = 5
    val iterations: Int = 100
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // GENERATORS (for randomized testing):
  
  val gene = Gen.oneOf(0,1)
  val individual = Gen.listOf(gene).suchThat(_.length > 1)
  
  //////////////////////////////////////////////////////////////////////////////
  // TESTS:
  
  property("crossover idempotent") = Prop.forAll(individual) { i =>
    MockGA.crossover(i,i) == i
  }
  
  property("mutate preserves length") = Prop.forAll(individual) { i =>
    MockGA.mutate(i).length == i.length
  }
  
  property("mutate changes at most one index") = Prop.forAll(individual) { i =>
    val original = i
    val modified = MockGA.mutate(i)
    (original,modified).zipped.count(p => p._1 != p._2) <= 1
  }
  
  property("performIteration maintains size") = Prop.forAll { (_: Unit) =>
    val original = MockGA.randomPopulation
    val modified = MockGA.performIteration(original)
    original.length == modified.length
  }
  
  property("performIteration increases fitness") = Prop.forAll { (_: Unit) =>
    val original = MockGA.randomPopulation
    val modified = MockGA.performIteration(original)
    val originalBest = original.map(MockGA.fitnessFn).max
    val modifiedBest = modified.map(MockGA.fitnessFn).max
    modifiedBest >= originalBest
  }
  
  /*property("performAlgorithm converges") = Prop.forAll { (_: Unit) =>
    val result = MockGA.performAlgorithm
    result.toList.maxBy(_._2)._2 == MockGA.individualSize.toDouble
  }*/
  
}
