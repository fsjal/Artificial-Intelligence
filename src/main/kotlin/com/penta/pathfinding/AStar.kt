package com.penta.pathfinding

import java.util.*

class AStar<T>(private val map: Array<Node<T>>, private val start: Node<T>, private val goal: Node<T>,
               private val heuristicFn: (Node<T>, Node<T>) -> Float) {

    private val open: PriorityQueue<Node<T>> // new discoverd nodes that their neighbors are not known and f is not calculated
    private val closed: ArrayList<Node<T>> // nodes done

    init {
        // A* : f = heuristic + weight, need a error heuristic function though
        open = PriorityQueue({ nodeA, nodeB ->
            when {
                nodeA.f > nodeB.f -> 1
                nodeA.f < nodeB.f -> -1
                else -> 0
            }
        })

        // Dijkstra : no heuristic function
        /*open = PriorityQueue({ nodeA, nodeB ->
            when {
                nodeA.g > nodeB.g -> 1
                nodeA.g < nodeB.g -> -1
                else -> 0
            }
        })*/

        // Greedy : no weights
        /*open = PriorityQueue({ nodeA, nodeB ->
            when {
                nodeA.g > nodeB.g -> 1
                nodeA.g < nodeB.g -> -1
                else -> 0
            }
        })*/
        closed = ArrayList()
    }

    // It just takes the best nodes from current neighbors by their weights and heuristics
    fun solve() {
        open.add(start)
        start.h = heuristicFn(start, goal)
        while (!open.isEmpty()) {
            val current = open.poll()

            closed.add(current)
            if (current == goal) {
                reconstructPath()
                return
            }

            for (neighbor in neighbors(current)) {
                if (closed.contains(neighbor)) continue
                neighbor.g = current.g + 1
                neighbor.h = heuristicFn(neighbor, goal)
                neighbor.parent = current
                open.add(neighbor)
            }
            println(open.joinToString(", "))
        }
    }

    private fun neighbors(node: Node<T>): Array<Node<T>> {
        val positions = arrayOf(Pair(-1, 0), Pair(0, -1), Pair(1, 0), Pair(0, 1))

        return positions.map { (x, y) ->
            map.firstOrNull { it.x == node.x + x && it.y == node.y + y }
        }
                .filterNotNull()
                .filter { it.type != AType.Wall }
                .toTypedArray()
    }

    fun reconstructPath(): Array<Node<T>> {
        val x = goal

        return generateSequence(x) {
            if (it.parent != null) {
                it.isPath = true
                it.parent
            } else {
                null
            }
        }.toList().toTypedArray()
    }

}