package com.penta.qlerning

import org.knowm.xchart.QuickChart
import org.knowm.xchart.SwingWrapper

class Q {

    val q = Array(6){ Array(6){ 0 } }
    val r = arrayOf(
            arrayOf(-1, -1, -1, -1, 0, -1),
            arrayOf(-1, -1, -1, 0, -1, 100),
            arrayOf(-1, -1, -1, 0, -1, -1),
            arrayOf(-1, 0, 0, -1, 0, -1),
            arrayOf(0, -1, -1, 0, -1, 100),
            arrayOf(-1, 0, -1, -1, 0, 100)
    )

    var action = 0
    var state = 0
    val delta = 0.8f

    fun train() {
        var counter = 0
        var i = 0
        var c = 0
        val xx = mutableListOf<Int>()
        val yy = mutableListOf<Int>()
        while (i < 100) {
            val converge = ArrayList<Int>()
            for (n in 0 .. 5) {
                state = n
                println("State started: $state")
                do {
                    val actions = r[state].mapIndexed { index, it -> Pair(index, it) }.filter { it.second != -1 }

                    action = actions[random(0, actions.size - 1)].first // policy is randomness
                    val x = Q(state, action)
                    converge.add(Math.abs(q[state][action] - x))

                    q[state][action] = x
                    //println("state: $state, action: $action, q: ${q[state][action]}")
                    state = action // the action is the next state actually
                    counter++
                } while (state != 5)
            }
            xx.add(i)
            yy.add(converge.average().toInt())
            if (converge.max() == 0) c++
            if (c == 100) break
            println(converge.joinToString(", "))
            i++
            println("episode: $i")
            for (i in 0 until 6) {
                for (j in 0 until 6) {
                    print("${q[i][j]}, ")
                }
                println()
            }
            println()
        }

        val chart = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", xx, yy)
        SwingWrapper(chart).displayChart()
        for (i in 0 until 6) {
            for (j in 0 until 6) {
                print("${q[i][j]*100/496f}, ")
            }
            println()
        }

        test(2)
    }

    fun test(state: Int) {
        var current = state
        generateSequence(state) {
            if (current != 5) {
                current = q[current].mapIndexed { index, i -> Pair(index, i) }.maxBy { it.second }?.first ?: 0
                current
            } else {
                null
            }
        }.forEach { println(it) }
    }

    fun random(min: Int, max: Int) = ((max - min + 1) * Math.random()).toInt() + min

    fun Q(state: Int, action: Int) : Int {
        return  r[state][action] + (delta * (q[action].filter { it != -1 }.max() ?: 0)).toInt()
    }
}