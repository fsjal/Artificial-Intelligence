package com.penta.genetics

interface Gene<T> {

    var fitness: Int
    var value: T

    fun grade()

    fun mate(father: T, mother: T): Gene<T>

    fun mutate()
}