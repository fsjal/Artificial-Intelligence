package com.penta.qlerning

class Q2 {

/*
States	Action: Left	Action: Right
0	    0.2	            0.6
1	    -0.6	        0.6
2	    -0.4	        0.5
3	    -0.2	        0.3
4	    -0.1	        0.3
5	    0	            0.2
6	    0	            0.5
7	    0.1	            0.6
8	    0.3	            0.8
9	    0.2	            1
10	    1	            0.5
11	    0.6	            0.2
 */

    val q = Array(10){ Array(2){ 0.0 } }
    val q2 = Array(12){ Array(2){ Math.random() * if (Math.random() < 0.5) -1 else 1 } }
    val r2 = arrayOf(
            arrayOf(0.2, 0.4),
            arrayOf(-0.6, 0.6),
            arrayOf(-0.4, 0.5),
            arrayOf(-0.2, 0.3),
            arrayOf(-0.1, 0.3),
            arrayOf(0.0, 0.2),
            arrayOf(0.0, 0.5),
            arrayOf(0.1, 0.6),
            arrayOf(0.3, 0.8),
            arrayOf(0.2, 1.0),
            arrayOf(1.0, 0.5),
            arrayOf(0.6, 0.2)
    )

    val delta = 0.2f

    fun random(min: Int, max: Int) = ((max - min + 1) * Math.random()).toInt() + min

    fun train() {
        var state = 2
        var statePrime:Int
        var action:Int
        var r: Double
        var i = 0

        while (i++ < 2000) {
            action = random(0, 1)
            statePrime = if (action == 0) state - 1 else state + 1
            r = if (statePrime == 10) 10.0 else if (statePrime == -1) -10.0 else -0.1

            if (statePrime == 10) statePrime = 5
            if (statePrime == -1) statePrime = 5
            q[state][action] = r + delta * q[statePrime].max()!!
            state = statePrime
        }
    }

    fun show() {
        println(q.joinToString("\n") { it.joinToString() })
    }

    fun test() {
        val x = "0=========P"
        var state = 0

        while (true) {
            println(q[state].max())
            val index = q[state].indexOf(q[state].max())

            if (index == 0) state-- else state++
            print("\r${x.substring(0, state)}X${x.substring(state + 1, 11)}: $state")
            readLine()
        }
    }

    fun train2() {
        val alpha = 0.1
        (1 .. 1000000).forEach { epoch ->
            var state = 2
            while (state != 12 && state != -1) {
                val action = r2[state].indexOf(r2[state][random(0, 1)])

                print("\r${r2[state][action] + (delta * (q2[action].max() ?: 0.0))}")
                q2[state][action] += alpha * (r2[state][action] + (delta * (q2[action].max() ?: 0.0)) - q2[state][action])
                if (action == 0) state-- else state++

            }
        }
    }

    fun show2() {
        println(q2.joinToString("\n") { it.joinToString() })
    }

    fun test2() {
        val x = "0=========P"
        var state = 2

        while (true) {
            val index = q2[state].indexOf(q2[state].max())

            if (index == 0) state-- else state++
            print("\r${x.substring(0, state)}X${x.substring(state + 1, 11)}: $state")
            readLine()
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val q = Q2()

            q.train()
            q.show()
            q.test()
        }
    }
}