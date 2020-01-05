package com.penta.qlerning

import com.penta.network.Activation.TANH
import com.penta.network.Graph
import com.penta.network.Loss
import com.penta.network.network

class Q3 {

    private val map = listOf(
            listOf('*','*','*','*','*','*','*','*', '*', '*'),
            listOf('*','*','*','*','*','*','*','*', '*', '*'),
            listOf('*','*','*','*','*','*','*','*', '*', '*'),
            listOf('*','*','*','*','*','*','*','*', '*', '*'),
            listOf('*','*','*','*','*','*','*','*', '*', '*'),
            listOf('*','*','*','O','O','O','*','O', '*', '*'),
            listOf('*','*','*','*','*','*','*','*', '*', '*'),
            listOf('*','*','*','*','*','*','*','*', '*', '*'),
            listOf('*','*','*','*','*','*','*','*', '*', '*'),
            listOf('*','*','*','*','*','*','*','*', '*', 'G')
    )
    private val network = network(Loss.MSE) {
        layer { neurons(3) }
        layer { neurons(7) using TANH }
        layer { neurons(1) using TANH }
    }

    enum class Action { Up, Right, Down, Left }

    private fun output(state: Pair<Int, Int>, action: Action) =
            network.outputs(listOf(state.first.toFloat(), state.second.toFloat(), action.ordinal.toFloat()))[0]

    private fun random(min: Int, max: Int) = ((max - min + 1) * Math.random()).toInt() + min

    private fun makeMove(state: Pair<Int, Int>, action: Action): Pair<Int, Int> {
        return when (action) {
            Action.Up -> state.first to (if (state.second != 0) state.second - 1 else ROWS)
            Action.Right -> (if (state.first != COLUMNS) state.first + 1 else 0) to state.second
            Action.Down -> state.first to (if (state.second != ROWS) state.second + 1 else 0)
            Action.Left -> (if (state.first != 0) state.first - 1 else COLUMNS) to state.second
        }
    }

    private fun getReward(state: Pair<Int, Int>): Float {
        return when {
            map[state.first][state.second] == 'O' -> -1f
            map[state.first][state.second] == 'G' -> 1f
            else -> -0.01f
        }
    }

    private fun train() {
        val delta = 0.6f
        val threshold = 0.2f
        val batchMax = 400
        val batchTrain = 0.6f
        val actions = Action.values()
        val graph = Graph()
        val epochs = 100
        network.learningRate = 0.1f

        (0..epochs).forEach { epoch ->
            var state = 3 to 4
            val batch = mutableListOf<Q>()
            val inputs = mutableListOf<List<Float>>()
            val outputs = mutableListOf<List<Float>>()

            while (batch.size < batchMax) {
                val epsilon = Math.random()
                val action = if (epsilon < (threshold + 0.7f * epoch/epochs)) actions.maxBy { output(state, it) }!! else actions[random(0, 3)]
                val statePrime = makeMove(state, action)
                val reward = getReward(statePrime)

                batch.add(Q(state, action, reward, statePrime))
                state = statePrime
            }

            batch.distinct().forEach { q ->
                inputs.add(listOf(q.state.first.toFloat(), q.state.second.toFloat(), q.action.ordinal.toFloat()))
                outputs.add(listOf(q.reward + delta * actions.map { output(q.statePrime, it) }.max()!!))
            }

            val error = (0..1000).map { network.train(inputs, outputs) }.average().toFloat()
            graph.update(epoch, error)

        }
        graph.showChart()
        show()

    }

    private fun show() {
        println()
        for (i in 0..ROWS) {
            for (j in 0..COLUMNS) {
                print("(${Action.values().maxBy { output(i to j, it) }}) ")
            }
            println()
        }
    }

    private fun test() {
        var state = 3 to 4

        while (true) {
            val action = Action.values().maxBy { output(state, it) }!!

            for (i in 0..ROWS) {
                for (j in 0..COLUMNS) {
                    if (state.first == i && state.second == j) print("X ") else print(map[i][j] + " ")
                }
                println()
            }
            readLine()
            state = makeMove(state, action)
        }
    }

    companion object {
        const val ROWS = 9
        const val COLUMNS = 9

        @JvmStatic
        fun main(args: Array<String>) {
            val q = Q3()

            q.train()
            q.test()
        }
    }

    data class Q(val state: Pair<Int, Int>, val action: Action, val reward: Float, val statePrime: Pair<Int, Int>) {

        override fun equals(other: Any?) = if (other is Q) state == other.state && action == other.action else false

        override fun hashCode() = state.first * state.second + action.ordinal
    }
}
