package com.test.ai

import kotlin.math.max
import kotlin.math.min

internal class AlphaBeta : Method {

    override fun solve(tree: Tree) = maxTree(tree)

    private fun maxTree(currentTree: Node, alpha: Int = Int.MIN_VALUE, beta: Int = Int.MAX_VALUE): Node {
        if (currentTree !is Tree) return currentTree
        currentTree.value = Int.MIN_VALUE
        var a = alpha

        for (child in currentTree.children) {
            val value = max(currentTree.value, minTree(child, a, beta).value)
            currentTree.value = value
            a = max(a, value)
            if (beta <= a) break
        }
        return currentTree
    }

    private fun minTree(currentTree: Node, alpha: Int = Int.MIN_VALUE, beta: Int = Int.MAX_VALUE): Node {
        if (currentTree !is Tree) return currentTree
        currentTree.value = Int.MAX_VALUE
        var b = beta

        for (child in currentTree.children) {
            val value = min(currentTree.value, maxTree(child, alpha, b).value)
            currentTree.value = value
            b = min(b, value)
            if (b <= alpha) break
        }

        return currentTree
    }
}