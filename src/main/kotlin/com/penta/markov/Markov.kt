package com.penta.markov

class Markov {

    private var currentState: State? = null
    private val states = mutableListOf<State>()

    val state get() = currentState

    fun state(init: State.() -> Unit) {
        states.add(State().apply(init))
    }

    fun add(state: State) {
        states.add(state)
    }

    fun init() {
        currentState = states[0]
    }

    fun chain() {
        var rand = Math.random()
        val next = currentState?.nextStates?.toList()!!
        var index = 0

        while (rand >= 0) rand -= next[index++].second
        currentState = states.find { it.data == next[index - 1].first }
    }

    override fun toString() = states.toList().joinToString("\n")
}

fun markov(init: Markov.() -> Unit) = Markov().apply(init)