package com.test.ai

class MinMax : Method {

    override fun solve(tree: Tree) = minMax(tree)

    private fun minMax(currentTree: Node, isMaximizing: Boolean = true): Node {
        if (currentTree !is Tree) return currentTree

        val node = if (isMaximizing) {
            currentTree.children.map { minMax(it, false) }.maxBy { it.value }!!
        } else {
            currentTree.children.map { minMax(it) }.minBy { it.value }!!
        }

        currentTree.value = node.value
        return node
    }
}