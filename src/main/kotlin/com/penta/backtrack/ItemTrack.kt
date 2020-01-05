package com.penta.backtrack

interface ItemTrack<T> {

    var item: T

    fun isComplete(index: Int): Boolean

    fun play(index: Int): Boolean

    fun reroll(index: Int)
}