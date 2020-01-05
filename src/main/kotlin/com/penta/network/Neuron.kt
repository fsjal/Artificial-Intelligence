package com.penta.network

class Neuron(private val w: FloatArray,
             var b: Float = rand(-1f, 1f),
             var z: Float = 0f,
             var a: Float = 1f,
             var delta: Float = 0f
): Iterable<Float> {

    override fun iterator() = w.iterator()

    override fun toString() = "\ta: $a, z: $z, delta: $delta - ${w.toList()} $b"

    operator fun get(index: Int) = w[index]

    operator fun set(index: Int, value: Float) { w[index] = value }
}