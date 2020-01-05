package com.penta.pathfinding

fun main(args: Array<String>) {
    val rawMap2 = arrayOf(
            charArrayOf('_', '_', '_', '#', '_', '_', '_'),
            charArrayOf('_', '#', '_', '#', '_', '#', '_'),
            charArrayOf('_', '#', '_', '#', '_', '#', '_'),
            charArrayOf('_', '#', '_', '#', '_', '#', '_'),
            charArrayOf('_', '#', '_', '#', '_', '#', '_'),
            charArrayOf('S', '#', '_', '_', '_', '#', 'G')
    )

    val rawMap1 = arrayOf(
            charArrayOf('_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_'),
            charArrayOf('_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_', 'G', '_'),
            charArrayOf('_', '_', '_', '_', '_', '#', '#', '#', '#', '#', '#', '#', '#', '_', '_'),
            charArrayOf('_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '#', '_', '_'),
            charArrayOf('_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '#', '_', '_'),
            charArrayOf('_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '#', '_', '_'),
            charArrayOf('_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '#', '_', '_'),
            charArrayOf('_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '#', ' ', ' '),
            charArrayOf('#', '#', '#', '#', '#', '#', '#', '#', '_', '_', '_', '_', '#', '_', '_'),
            charArrayOf('_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '#', '_', '_'),
            charArrayOf('_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '#', '_', '_'),
            charArrayOf('_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '#', '_', '_'),
            charArrayOf('S', '_', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '_', '_'),
            charArrayOf('_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_'),
            charArrayOf('_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_', '_')
    )

    var i = 0
    val map = rawMap2.flatMap {
                val x = it.mapIndexed({ j, c ->  Node(i, j, c, when(c) {
                    'S' -> AType.Start
                    'G' -> AType.Goal
                    '#' -> AType.Wall
                    else -> AType.Path
                    })
                })
                i++
                x
            }.toTypedArray()
    val goal = map.first { it.type == AType.Goal }
    val start = map.first { it.type == AType.Start }
    val astar = AStar(map, start, goal, { a: Node<Char>, b: Node<Char> ->
        Math.sqrt(Math.pow((a.x - b.x).toDouble(), 2.0) + Math.pow((b.x - b.x).toDouble(), 2.0)).toFloat()
    })

    astar.solve()
    astar.reconstructPath()

    val solutions = map.filter { it.isPath }.toTypedArray()

    rawMap2.forEachIndexed { i, row ->
        row.forEachIndexed { j, value ->

            if (solutions.firstOrNull { it.x == i && it.y == j && it != goal} != null) {
                print("*")
            } else {
                print(value)
            }
        }
        println()
    }
    println("walking: ${solutions.size}")
}