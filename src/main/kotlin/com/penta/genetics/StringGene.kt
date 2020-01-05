package com.penta.genetics

class StringGene: Gene<String> {

    override var fitness = 0
    override var value = ""

    init {
         value = TARGET.indices.map { ALPHA[(Math.random() * ALPHA.length).toInt()] }.fold("", { acc, c -> acc + c })
    }

    override fun grade() {
        fitness = TARGET.indices.filter { value[it] == TARGET[it] }.size
    }

    override fun mate(father: String, mother: String): StringGene {
        val gene = StringGene()
        gene.value = TARGET.indices.foldIndexed("", { index, acc, i -> acc + if (index % 2 == 0) father[i] else mother[i] })
        return gene
    }

    override fun mutate() {
        val index = (Math.random() * TARGET.length).toInt()
        val sb = StringBuilder(value)

        sb[index] = ALPHA[(Math.random() * ALPHA.length).toInt()]
        value = sb.toString()
    }

    override fun toString() = value

    companion object {
        const val TARGET = "Hello world !"
        //const val TARGET = "Hello world"
        const val ALPHA = "abcdefghijklmnopqrstuvwxyz AZERTYUIOPQSDFGHJKLMWXCVBN?./§!'"

        @JvmStatic
        fun main(args: Array<String>) {
            val gen = Genetic<String>(TARGET.length, 100)

            (0 until 100).forEach { gen.population.add(StringGene()) }
            var x = gen.evolve()
            while (x == null) {
                println(gen.population[0].value)
                gen.grade()
                x = gen.evolve()
            }
            println(x)
        }
    }

}