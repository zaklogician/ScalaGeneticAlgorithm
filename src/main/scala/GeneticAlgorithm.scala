package scalaga;

trait RNG extends scala.util.Random

trait GeneticAlgorithm {
  //////////////////////////////////////////////////////////////////////////////
  // REQUIRES (CONFIG):
  val rng: RNG
  
  type Gene
  def randomGene: Gene
  val individualSize: Int
  
  def fitnessFn(individual: Individual): Double
  
  val populationSize: Int
  val eliteRatio: Double
  val tournamentSize: Int
  val iterations: Int
  
  
  //////////////////////////////////////////////////////////////////////////////
  // PROVIDES:
  
  // Individuals
  
  type Individual = Seq[Gene]
  
  def crossover(a: Individual, b: Individual): Individual = {
    require(a.length == b.length)
    val crosspoint = rng.nextInt(a.length)
    val aPrefix = a.splitAt(crosspoint)._1
    val bSuffix = b.splitAt(crosspoint)._2
    aPrefix ++ bSuffix
  }

  def mutate(a: Individual): Individual = {
    val mpoint = rng.nextInt(a.length)
    a.updated(mpoint, randomGene)
  }
  
  def randomIndividual: Individual = 
    for(i <- 1 to individualSize) yield randomGene
  
  // Populations
  
  type Population = Seq[Individual]
  
  def randomPopulation: Population = 
    for(i <- 1 to populationSize) yield randomIndividual
  
  // Fitness cache (avoids re-evaluating fitness)
  
  protected var fitness_ = scala.collection.mutable.Map.empty[Individual,Double]
  def fitness(x: Individual): Double = fitness_.getOrElseUpdate(x,fitnessFn(x))
  
  // Selection
  
  def tournamentSelect(population: Population): Individual = {
    require(tournamentSize >= 2)
    val tournament = rng.shuffle(population).take(tournamentSize)
    tournament.maxBy(fitness(_))
  }

  // Genetic Algorithm

  def performIteration(population: Population): Population = {
	  // see "Essentials of Metaheuristics" Algorithm 33
    val numberOfElites = (population.length * eliteRatio).toInt
    val eliteSurvivors = population.sortBy(-fitness(_)).take(numberOfElites)
    val childIterations = (population.length - numberOfElites)/2
    val children = (1 to childIterations) flatMap { i =>
      val father = tournamentSelect(population)
      val mother = tournamentSelect(population)
      Seq( mutate(crossover(father,mother)) , mutate(crossover(mother,father)) )
    }
    eliteSurvivors ++ children
  }

  /**
   * Main GA loop. Returns a map containing all individuals evaluated during
   * the course of the algorithm. To get only the fittest solution,
   * you can use e.g. result.toList.maxBy(_._2)
   */
  def performAlgorithm: Map[Individual,Double] = {
    fitness_ = scala.collection.mutable.Map.empty[Individual,Double]
    var population: Population = randomPopulation
    var iter = 0
    while(iter < iterations) {
      population = performIteration(population)
      iter = iter + 1
    }
    fitness_.toMap
  }
  
}
