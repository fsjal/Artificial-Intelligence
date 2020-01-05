package com.penta.markov

data class State(var data: String = "", var nextStates: Map<String, Float> = emptyMap()) {

    override fun equals(other: Any?): Boolean {
        return when(other) {
            is State -> other.data == data
            else -> false
        }
    }

    override fun hashCode() = data.hashCode() + 31 * nextStates.hashCode()
}