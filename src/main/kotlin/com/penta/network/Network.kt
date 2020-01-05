package com.penta.network

import com.penta.network.Activation.ActivationFunction
import com.penta.network.Loss.LossFunction

/**
 * Multi-layer network
 */
@NetworkDSL
class Network(val loss: LossFunction = Loss.MSE) {

    val layers = mutableListOf<Layer>() // layer of the network, inputs included
    val lambda = 0.0001f // regularisation, for better output and avoid over-fitting
    val outputLayer get() = layers.last()
    val inputLayer get() = layers.first()
    var learningRate = 0.1f // learning rate of the network, should be 0.9 < x < 0.000001

    override fun toString(): String {
        return layers.joinToString("\n")
    }
    /**
     * Add layer to the network
     */
    fun addLayer(neuronsCount: Int, activation: ActivationFunction = Activation.SIGMOID) {
        val previousNeuronsSize = if (layers.size == 0) 0 else outputLayer.neuronsSize
        val neurons = (0 until neuronsCount).map { Neuron(FloatArray(previousNeuronsSize) { rand(-1f, 1f) }) }

        layers.add(Layer(neurons, activation))
    }

    /**
     * feeding the network
     */
    fun feed(inputs: List<Float>) {
        inputLayer.zip(inputs).forEach { (neuron, input) -> neuron.a = input } // assigning input layer
        (1 until layers.size).forEach { layerIndex ->
            val layer = layers[layerIndex]
            layer.forEach { neuron ->
                neuron.z = neuron.zip(layers[layerIndex - 1]).fold(neuron.b) { acc, (w, n) -> acc + w * n.a } // ∑ (w*i)+b
                neuron.a = layer.activation.fn(neuron.z) // avtivation function
            }
        }
    }

    /**
     * training the network
     */
    fun train(inputs: List<List<Float>>, targets: List<List<Float>>): Float {
        val batchSize = inputs.size
        var error = 0f

        (0 until batchSize).forEach { epoch ->
            val target = targets[epoch]
            val input = inputs[epoch]
            val e = FloatArray(target.size) { 0f }

            feed(input)
            // output layer
            outputLayer.forEachIndexed { index, neuron ->
                val cost = loss.fn(target[index], neuron.a) + 0.5f * lambda * neuron.map { pow(it, 2f) }.sum()
                val dcost = loss.fnPrime(target[index], neuron.a)
                val dz = outputLayer.activation.fnPrime(neuron.z)

                e[index] = cost
                neuron.delta = dcost * dz
                neuron.zip(layers[layers.size - 2]).forEachIndexed { i, (w, previousNeuron) ->
                    neuron[i] -= learningRate * dcost * dz * previousNeuron.a + lambda * w
                }
                neuron.b -= learningRate * neuron.delta + lambda
            }
            // hidden layers
            (layers.size - 2 downTo 1).forEach { layerIndex ->
                val layer = layers[layerIndex]

                layer.forEachIndexed { index, neuron ->
                    val nextLayer = layers[layerIndex + 1]
                    val previousLayer = layers[layerIndex - 1]
                    val dz = layer.activation.fnPrime(neuron.z)
                    val cost = nextLayer.fold(0f) { acc, nextNeuron -> acc + nextNeuron.delta * nextNeuron[index] } // ∑ (w * delta)

                    neuron.delta = cost * dz
                    neuron.zip(previousLayer).forEachIndexed { i, (_, previousNeuron) ->
                        neuron[i] -= learningRate * neuron.delta * previousNeuron.a
                    }
                    neuron.b -= learningRate * neuron.delta
                }
            }
            error += e.sum() / e.size
        }
        return error / batchSize
    }

    /**
     *  get the outputs of the network according to passed inputs
     */
    fun outputs(inputs: List<Float>): List<Float> {
        feed(inputs)
        return outputLayer.map { it.a }
    }

    /**
     *  get the predicted value of the network, using the softmax method
     */
    fun predict(inputs: List<Float>): Int {
        feed(inputs)
        val sum = outputLayer.map { exp(it.a) }.sum() // classifier using softmax function
        val ex  = outputLayer.map { exp(it.a) / sum } // e / ∑e(counter)

        return ex.indexOf(ex.max())
    }

    /* DSL */

    fun layer(init: Layer.LayerBuilder.() -> Unit) {
        val previousNeuronsSize = if (layers.size == 0) 0 else outputLayer.neuronsSize
        val layer = Layer.LayerBuilder().apply(init)
        val neurons = (0 until layer.neuronsSize).map { Neuron(FloatArray(previousNeuronsSize) { rand(-1f, 1f) }) }

        layers.add(Layer(neurons, layer.activation))
    }
}

fun network(loss: LossFunction = Loss.MSE, init: Network.() -> Unit): Network = Network(loss).apply(init)

@DslMarker
annotation class NetworkDSL