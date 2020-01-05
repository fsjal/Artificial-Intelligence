package com.penta.markov

fun main() {

    val mark = markov {
        state { data = "A"; nextStates = mapOf("A" to 0.3f, "B" to 0.7f) }
        state { data = "B"; nextStates = mapOf("B" to 1f) }
    }
    mark.init()

    (0 .. 10).forEach { _ ->
        mark.chain()
        print(mark.state?.data)
        readLine()
    }
}