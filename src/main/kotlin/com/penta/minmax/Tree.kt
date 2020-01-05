package com.test.ai

class Tree : Node() {

    internal val children = mutableListOf<Node>()

    fun solve(method: Method) = method.solve(this)

    fun tree(block: Tree.() -> Unit = { }) =  children.add(Tree().apply(block))

    fun node(value: Int) = children.add(Node(value))

    override fun toString() = "$value : ${children.toList()}"

    companion object {

        fun build(block: Tree.() -> Unit) = Tree().apply(block)
    }
}