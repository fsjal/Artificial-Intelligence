package com.penta.network

import com.penta.network.Activation.ActivationFunction

class Layer(private val neurons: List<Neuron>, val activation: ActivationFunction) : Iterable<Neuron> {

    val neuronsSize by lazy { neurons.size }

    override fun iterator() = neurons.iterator()

    override fun toString(): String {
        return "Layer: $neuronsSize neurons: ${activation.javaClass.simpleName}\n${neurons.joinToString("\n")}"
    }

    operator fun get(index: Int) = neurons[index]

    /* DSL */

    class LayerBuilder(var neuronsSize: Int = 0, var activation: ActivationFunction = Activation.SIGMOID) {

        fun neurons(size: Int) = apply { neuronsSize = size }

        infix fun using(activation: ActivationFunction) { this.activation = activation }
    }
}