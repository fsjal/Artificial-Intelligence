package com.penta.qlerning

import com.penta.network.Network

class Q15 {

    val network = Network()
    val r = arrayOf(
            arrayOf(-1, -1, -1, -1, 0, -1),
            arrayOf(-1, -1, -1, 0, -1, 100),
            arrayOf(-1, -1, -1, 0, -1, -1),
            arrayOf(-1, 0, 0, -1, 0, -1),
            arrayOf(0, -1, -1, 0, -1, 100),
            arrayOf(-1, 0, -1, -1, 0, 100)
    )
    val q = Array(6) { Array(6) { 0f } }

    init {
        network.addLayer(2)
        network.addLayer(3)
        network.addLayer(1)
    }

    fun output(state: Int, action: Int) = network.outputs(listOf(state.toFloat(), action.toFloat()))[0]

    fun random(min: Int, max: Int) = ((max - min + 1) * Math.random()).toInt() + min

    fun train() {
        var state = 2
        val delta = 0.5f
        val alpha = 1f
        val replay = mutableMapOf<Pair<Int, Int>, Float>()

        (1 .. 10000).forEach {
            val actions = r[state].mapIndexed { index, value -> index to value }
            val a = (Math.random() * actions.size).toInt()
            val action = actions[a].first
            val statePrime = action
            val reward = actions[a].second.toFloat()
            val argMaxQ = r[statePrime].mapIndexed { action, reward -> output(statePrime, action) }.max()!!
            val qn = output(state, action)
//            val qq = qn + alpha * (reward + delta * argMaxQ - qn)
            val qq = reward + delta * argMaxQ

            replay.put(state to action, qq)
            //q[state][action] = qq
//            network.train(listOf(listOf(state.toFloat(), action.toFloat())), listOf(listOf(qq)))
            state = statePrime
        }
        val inputs = replay.keys.map { listOf(it.first.toFloat(), it.second.toFloat()) }
        val output = replay.values.map { listOf(it) }

        (1 .. 10000).forEach { network.train(inputs, output) }
    }

    fun test() {
        var state = 2
        println(q.joinToString("\n") { it.toList().toString() })
        while (true) {
//            val action = r[state].mapIndexed { a, reward -> a to output(state, a) }.maxBy { it.second }?.first!!
            val action = (0 .. 5).mapIndexed{ index, it -> index to output(state, it) }.maxBy { it.second }!!.first

            println(action)
            state = action
            readLine()
        }
    }

/*
    fun train () {
        var state = 2
        val replay = mutableMapOf<Int, List<Double>>()

        (1 .. 1000).forEach {
            val actions = r[state].mapIndexed { index, value -> index to value }.filter { it.second != -1 } // valid action only
            val action = (Math.random() * actions.size).toInt() // choose action
            val statePrime = actions[action].first
            val reward = actions[action].second // get reward
//            val argmaxQ = r[statePrime].mapIndexed { action, reward ->
//                if (reward != -1) netOut(statePrime, action) else -10.0
//            }.max()!!
//            val q = reward + 0.8 * argmaxQ
            val input = mutableListOf<Array<Double>>()
            val output = mutableListOf<Array<Double>>()

//            replay.put(state, listOf(action.toDouble(), q, reward.toDouble()))
            replay.forEach { (state, value) ->
                input.add(arrayOf(state.toDouble(), value[0]))
                output.add(arrayOf(value[1]))
            }

//            network.train(input.toTypedArray(), output.toTypedArray())

            if (it % 10 == 0) {
//                println("$it) State: $state, action: $action -> state': $statePrime, argmaxQ: $argmaxQ, reward: $reward, q: $q")
//                println("$i) replays: ${replay}")
            }

            state = statePrime
        }
    }

    fun test() {
        var state = 2

        while (true) {
            val actions = r[state].mapIndexed { index, value -> index to value }.filter { it.second != -1 }
//            state = actions.mapIndexed { index, value -> index to netOut(state, value.first) }.maxBy { it.second }?.first!!
//            println(actions.map { println("state: $state, action: ${it.first}"); netOut(state, it.first) })
            println(state)
            readLine()
        }
    }
*/

//    fun netOut(state: Int, action: Int) = network.outputs(arrayOf(state.toDouble(), action.toDouble()))[0]


    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val q = Q15()

            q.train()
            q.test()
        }
    }


}