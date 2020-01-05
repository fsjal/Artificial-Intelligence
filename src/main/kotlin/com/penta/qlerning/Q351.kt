package com.penta.qlerning

import com.penta.network.Activation.SIGMOID
import com.penta.network.network
import com.penta.qlerning.Q351.Action.*

class Q351 {

    private enum class Action { Up, Left, Down, Right }
    private val map by lazy { listOf(
        listOf('*', '*', '*', '*', '*', '*', '*', '*', '*', '*'),
        listOf('*', '*', '*', '*', '*', '*', '*', '*', '*', '*'),
        listOf('*', '*', '*', '*', '*', '*', '*', '*', '*', '*'),
        listOf('*', '*', '*', '*', '*', '*', '*', '*', '*', '*'),
        listOf('*', '*', '*', '*', '*', '*', '*', '*', '*', '*'),
        listOf('*', '*', '*', 'O', '*', '*', '*', '*', '*', '*'),
        listOf('*', '*', '*', '*', '*', '*', '*', '*', 'G', '*'),
        listOf('*', '*', '*', '*', '*', '*', '*', '*', '*', '*'),
        listOf('*', '*', '*', '*', '*', '*', '*', '*', '*', '*'),
        listOf('*', '*', '*', '*', '*', '*', '*', '*', '*', '*')
    )}
    private val network by lazy { network {
        layer { neurons(3) }
        layer { neurons(8) using SIGMOID }
        layer { neurons(1) using SIGMOID }
    }}

    private fun output(state: Pair<Int, Int>, action: Action) = network.outputs(listOf(state.first.toFloat(), state.second.toFloat(), action.ordinal.toFloat()))[0]

    private fun random(min: Int, max: Int) = ((max - min + 1) * Math.random()).toInt() + min

    private fun nextState(state: Pair<Int, Int>, action: Action) = with(state) {
        when (action) {
            Up -> if (first == 0) this else first - 1 to second
            Left -> if (second == 0) this else first to second - 1
            Down -> if (first == 9) this else first + 1 to second
            Right -> if (second == 9) this else first to second + 1
        }
    }

    private fun getReward(state: Pair<Int, Int>) = with(state) {
        when {
            map[first][second] == 'O' -> -1f
            map[first][second] == 'G' -> 1f
            else -> -0.01f
        }
    }

    fun train() {
        val delta = 0.75f
        var threshold = 0.9f
        var state = 3 to 4
        var isFinished = false
        val actions = listOf(Up, Left, Down, Right)
        var counter = 0
        val replay = mutableMapOf<Pair<Int, Int>, Pair<Action, Float>>()

        while (!isFinished) {
            val epsilon = Math.random()
            val action = if (epsilon > threshold) actions.maxBy { output(state, it) }!! else values()[random(0, actions.size - 1)]
            val statePrime = nextState(state, action)
            val reward = getReward(statePrime)
            val argMaxQ = if (map[state.first][state.second] in listOf('O', 'G')) { isFinished = true; 0f } else actions.map { output(statePrime, it) }.max()!!
            val q = reward + delta * argMaxQ

            if (counter > 100 && threshold > 0.3f) { threshold -= 0.01f; counter = 0 }
            counter++
//            println("$state $action $epsilon $threshold ${values().maxBy { output(state, it) }}")
            replay[state] = action to q
            state = statePrime
            replay.forEach { (first, second), (action, q) ->
                val inputs = listOf(listOf(first.toFloat(), second.toFloat(), action.ordinal.toFloat()))
                val outputs = listOf(listOf(q))
                network.train(inputs, outputs)
            }
            //println(replay.joinToString { (action, q) -> "$action:$q" })
        }
    }

    private fun test() {
        var state = 3 to 4

        while (true) {
            val action = values().maxBy { output(state, it) }!!

            for (i in 0..9) {
                for (j in 0..9) {
                    if (state.first == i && state.second == j) print("X ") else print(map[i][j] + " ")
                }
                println()
            }
            readLine()
            state = nextState(state, action)
        }
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val q = Q351()
            repeat(100) {
                q.train()
            }
            q.test()
        }
    }

}