package scalaga;

/**
 * Usage example of the GeneticAlgorithm trait.
 */
object Main {
  
  object Random extends scala.util.Random(0x1234) with RNG { }
  
  object GA extends GeneticAlgorithm {
    val rng: RNG = Random
    
    type Gene = Char
    
    val alphabet = "abcdefghijklmnopqrstuvwxyz"
    def randomGene: Char = alphabet.charAt(rng.nextInt(alphabet.length))
    val individualSize: Int = 5
    
    // the fitness is the number of characters we got right
    def fitnessFn(individual: Individual): Double = {
      (individual,"hello").zipped.count(c => c._1 == c._2).toDouble
    }
        
    val populationSize: Int = 100
    val eliteRatio: Double = 0.1
    val tournamentSize: Int = 5
    val iterations: Int = 100
  }
  
  
  def main(args: Array[String]): Unit = {
    println("If you're reading this, the build worked!")
    println("As an example, we evolve the string 'hello':")
    val result = GA.performAlgorithm
    println(result.maxBy(_._2)._1.mkString)
  }
  
}
