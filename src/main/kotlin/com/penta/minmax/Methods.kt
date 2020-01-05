package com.test.ai

object Methods {

    val MIN_MAX get() = object : Method by MinMax() { }
    val ALPHA_BETA get() = object : Method by AlphaBeta() { }
}