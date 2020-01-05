package com.penta.qlerning

import com.penta.network.Activation.SIGMOID
import com.penta.network.Graph
import com.penta.network.Loss
import com.penta.network.network

class Q251 {

    private val network by lazy {
        network(Loss.MSE) {
            layer { neurons(2) }
            layer { neurons(5) using SIGMOID }
            layer { neurons(1) using SIGMOID }
        }
    }
    val graph by lazy { Graph() }
    var counter = 0

    private fun output(state: Int, action: Int) = network.outputs(listOf(state.toFloat(), action.toFloat()))[0]

    private fun random(min: Int, max: Int) = ((max - min + 1) * Math.random()).toInt() + min

    private fun nextState(state: Int, action: Int) = when {
        action == 0 && state == 0 -> 0
        action == 1 && state == 9 -> 9
        else -> state + if (action == 0) -1 else 1
    }

    private fun getReward(state: Int, action: Int) = if (state == 8 && action == 1) -1f else if (state == 1 && action == 0) 1f else -0.001f

    fun train() {
        val delta = 0.75f
        var threshold = 0.9f
        var state = 5


        while (state !in listOf(0, 9)) {
            val epsilon = Math.random()
            val action = if (epsilon > threshold) (0..1).maxBy { output(state, it) }!! else random(0, 1)
            val reward = getReward(state, action)
            val statePrime = nextState(state, action)
            val argMaxQ = if (state in listOf(0, 9)) 0f else (0..1).map { output(statePrime, it) }.max()!!
            val q = reward + delta * argMaxQ
            val inputs = listOf(listOf(state.toFloat(), action.toFloat()))
            val outputs = listOf(listOf(q))

            threshold -= 0.001f
            state = statePrime
            graph.update(counter++, network.train(inputs, outputs))
        }
    }

    fun test() {
        val x = "0=========P"
        var state = 5

        while (true) {
            val index = (0 .. 1).maxBy { output(state, it) }
            println((0..1).map { output(state, it) })

            if (index == 0) state-- else state++
            print("\r${x.substring(0, state)}X${x.substring(state + 1, 11)}: $state")
            readLine()
        }
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val q = Q251()

            repeat(1000) {
                q.train()
            }
            q.graph.showChart()
            q.test()
        }
    }

}