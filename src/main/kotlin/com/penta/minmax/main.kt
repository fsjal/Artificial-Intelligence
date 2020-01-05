package com.test.ai

fun main() {
    val tree = Tree.build {
        tree {
            node(3)
            node(5)
        }
        tree {
            tree {
                tree {
                    node(0)
                    node(7)
                    node(10)
                    node(3)
                }
                tree {
                    node(5)
                    node(6)
                }

            }
            tree {
                tree {
                    node(7)
                    node(8)
                }
                tree {
                    node(8)
                    node(10)
                }
                tree {
                    node(-3)
                    node(-12)
                }
                tree {
                    node(4)
                    node(2)
                    node(6)
                }
            }
            tree {
                tree {
                    node(4)
                    node(5)
                }
                tree {
                    node(2)
                    node(8)
                }
            }
        }
    }

    println(tree.solve(Methods.ALPHA_BETA))

}