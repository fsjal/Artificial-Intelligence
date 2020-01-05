package com.penta.qlerning

class Q35 {

    private val map = listOf(
            listOf('*','*','*','*','*','*','*','*','*','*'),
            listOf('*','*','*','*','*','*','*','*','*','*'),
            listOf('*','*','*','*','*','*','*','*','*','*'),
            listOf('*','*','*','*','*','*','*','*','*','*'),
            listOf('*','*','*','*','*','*','*','*','*','*'),
            listOf('*','*','*','O','*','*','*','*','*','*'),
            listOf('*','*','*','*','*','*','*','*','P','*'),
            listOf('*','*','*','*','*','*','*','*','*','*'),
            listOf('*','*','*','*','*','*','*','*','*','*'),
            listOf('*','*','*','*','*','*','*','*','*','*')
    )
    private val q = Array(100) { Array(4) { 0.0 } }
    enum class Action { Up, Left, Down, Right }

    fun train() {
        val actions = listOf(Action.Up, Action.Left, Action.Down, Action.Right)
        var state = 24
        val delta = 0.5
        (0 .. 10000).forEach {
            val action = actions[(Math.random() * 4).toInt()]
            var statePrime = when(action) {
                Action.Up -> state - 10
                Action.Left -> state - 1
                Action.Down -> state + 10
                Action.Right -> state + 1
            }
            val r = when {
                statePrime == 68 -> 10.0
                statePrime == 35 -> -10.0
                else -> -0.1
            }

            if (statePrime >= 100) statePrime = 24
            if (statePrime <= -1) statePrime = 24
            q[state][action.ordinal] = r + delta * q[statePrime].max()!!
            state = statePrime
        }
        println(q.joinToString("\n") { it.joinToString() })
    }

    fun test() {
        var state = 24

        while (true) {
            println(q[state].max())
            val action = Action.values()[q[state].indexOf(q[state].max())]

            state = when(action) {
                Action.Up -> state - 10
                Action.Left -> state - 1
                Action.Down -> state + 10
                Action.Right -> state + 1
            }

            for (i in 0 until 10) {
                for (j in 0 until 10) {
                    if (state / 10 == i && state % 10 == j) print("X ") else print(map[i][j] + " ")
                }
                println()
            }
            readLine()
        }
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val q = Q35()

            q.train()
            q.test()
        }
    }
}