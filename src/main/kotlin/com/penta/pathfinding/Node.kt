package com.penta.pathfinding

class Node<T>(val x: Int, val y: Int, val value: T, val type: AType = AType.Path) {

    var h: Float = 0f      // heuristic
    var g: Int = 0      // movement cost
    val f: Float          // f=g+h
        get() = h + g
    var parent: Node<T>? = null
    var isPath = false

    override fun equals(other: Any?): Boolean {
        if (other is Node<*>) return other.x == x && other.y == y

        return false
    }

    override fun hashCode(): Int = x + y

    override fun toString() = "($x, $y): $value - h: $h, g: $g, f: $f"
}