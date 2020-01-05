package com.penta.genetics

class Genetic<T>(private val targetFitness: Int, private val populationCount: Int = 100) {

    val population = mutableListOf<Gene<T>>()
    private var generations: Int = 1
    private val graded = (populationCount * CHANCE_GRADED_RETAIN).toInt()

    fun grade() {
        population.forEach { it.grade() }
    }

    fun evolve(): T? {
        // sorting population by their fitness
        population.sortByDescending { it.fitness }

        // find if max fitness has been reached
        val solutions = population.filter { it.fitness >= targetFitness }.size
        if (solutions != 0) return population[0].value

        /* mating */
        val parents = population.take(graded).toMutableList()
        population.takeLast(populationCount - graded).filter { Math.random() < CHANCE_NONGRATED_RETAIN }.forEach { parents.add(it) }

        (0 until parents.size).forEach { population[it] = parents[it] }
        (parents.size until populationCount).forEach {
            var father = 0
            var mother = 0

            while (father == mother) {
                father = (Math.random() * parents.size).toInt()
                mother = (Math.random() * parents.size).toInt()
            }
            parents.add(population[it].mate(parents[father].value, parents[mother].value))
        }

        /* mutation */
        parents.filter { Math.random() < CHANCE_TO_MUTATE }.forEach { it.mutate() }

        population.clear()
        population.addAll(parents)
        generations++

        return null
    }

    companion object {
        var CHANCE_TO_MUTATE = 0.10f            // chance of mutation
        var CHANCE_GRADED_RETAIN = 0.25f        // will be retained and create the next generation
        var CHANCE_NONGRATED_RETAIN = 0.05f     // will be added randomly, ignored
    }
}