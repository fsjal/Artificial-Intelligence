package com.penta.qlerning

import com.penta.network.Activation.TANH
import com.penta.network.Graph
import com.penta.network.Loss
import com.penta.network.network
class Q25 {

    private val network = network(Loss.MSE) {
        layer { neurons(2) }
        layer { neurons(5) using TANH }
        layer { neurons(1) using TANH }
    }

    private fun output(state: Int, action: Int) = network.outputs(listOf(state.toFloat(), action.toFloat()))[0]

    private fun random(min: Int, max: Int) = ((max - min + 1) * Math.random()).toInt() + min

    private fun makeMove(state: Int, action: Int) = when {
        action == 0 && state == 0 -> 0
        action == 1 && state == 9 -> 9
        else -> if (action == 0) state - 1 else state + 1
    }

    private fun getReward(state: Int, action: Int) = if (state == 8 && action == 1) -1f else if (state == 1 && action == 0) 1f else -0.001f

    fun train() {
        val gamma = 0.8f
        val threshold = 0.2f
        val batchMax = 50
        val batchTrain = 0.7f
        val graph = Graph()
        var state = 5
        val epochs = 1000

        (0..epochs).forEach { epoch ->
            val batch = mutableListOf<Q>()
            val inputs = mutableListOf<List<Float>>()
            val outputs = mutableListOf<List<Float>>()

            while (state != 9) {
                val epsilon = Math.random()
                val action = /*if (epsilon < (threshold + 0.7f * epoch/epochs)) (0..1).maxBy { output(state, it) }!! else*/ random(0, 1)
                val statePrime = makeMove(state, action)
                val reward = getReward(state, action)

                batch.add(Q(state, action, reward, statePrime))
                state = statePrime
            }

            batch.distinct().forEach { qValue ->
                inputs.add(listOf(qValue.state.toFloat(), qValue.action.toFloat()))
                outputs.add(listOf(qValue.reward + gamma * (0..1).map { output(qValue.statePrime, qValue.action) }.max()!!))
            }
            val error = (0..100).map { network.train(inputs, outputs) }.average().toFloat()
            graph.update(epoch, error)
        }
        //graph.showChart()
        //show()
    }


    fun train2() {
        val delta = 0.5f
        val threshold = 0.5f
        val graph = Graph()

        (0..1000).forEach { epoch ->
            var state = 5
            val batch = mutableMapOf<Pair<Int, Int>, Float>()
            val inputs = mutableListOf<List<Float>>()
            val outputs = mutableListOf<List<Float>>()

            (0..100).forEach {
                val epsilon = Math.random()
                val action = /*if (epsilon < (threshold)) (0..1).maxBy { output(state, it) }!! else*/ random(0, 1)
                val statePrime = makeMove(state, action)
                val reward = getReward(state, action)
                val q = reward + delta * (0..1).map { output(state, it) }.max()!!

                if (!batch.containsKey(state to action)) batch[state to action] = q
                state = statePrime
            }

            batch.forEach { (value, q) ->
                inputs.add(listOf(value.first.toFloat(), value.second.toFloat()))
                outputs.add(listOf(q))
            }
            val error = network.train(inputs, outputs)
            graph.update(epoch, error)
        }
        //graph.showChart()
    }

    fun train3() {
        val delta = 0.5f
        val graph = Graph()

        (0..10).forEach { epoch ->
            var state = 5

            (0..100).forEach {
                val epsilon = Math.random()
                val action = /*if (epsilon < (threshold)) (0..1).maxBy { output(state, it) }!! else*/ random(0, 1)
                val statePrime = makeMove(state, action)
                val reward = output(state, action)
                val q = reward + delta * (0..1).map { output(state, it) }.max()!!
                //println("[$state:$action -> $statePrime] :(${output(state, action)}) -> $q = $reward + ($delta * ${(0..1).map { output(state, it) }.max()})")
                val inputs = listOf(listOf(state.toFloat(), action.toFloat()))
                val outputs = listOf(listOf(q))
                val cq = output(state, action)
                val error = network.train(inputs, outputs)
                val nq = output(state, action)
               // println("[$state:$action] Q = $cq - New Q = $nq ; error = $error")
                graph.update(epoch+it, error)
                state = statePrime
                //Thread.sleep(5000)
            }
        }
        graph.showChart()
    }

    fun show() {
        println((0 until 10).joinToString { state -> (0..1).maxBy { action -> output(state, action) }.toString() })
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
            val q = Q25()

            q.train3()
            q.test()
        }
    }

    data class Q(val state: Int, val action: Int, val reward: Float, val statePrime: Int) {

        override fun equals(other: Any?) = if (other is Q) state == other.state && action == other.action else false

        override fun hashCode() = state * action + action
    }

}

