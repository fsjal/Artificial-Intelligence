package com.penta.network

/**
 * Activation function for the neural network
 */
object Activation {

    val SIGMOID: ActivationFunction get() = Sigmoid()

    val TANH: ActivationFunction get() = Tanh()

    val RELU: ActivationFunction get() = ReLu()

    val ELU: ActivationFunction get() = ELu()

    val ISRLU: ActivationFunction get() = IsRLu()

    val SOFTPLUS: ActivationFunction get() = SoftPlus()

    val LINEAR: ActivationFunction get() = Linear()

    val SWISH: ActivationFunction get() = Swish()

    class Sigmoid : ActivationFunction {

        override fun fn(x: Float) = 1 / (1 + exp(-x))

        override fun fnPrime(x: Float) = fn(x) * (1 - fn(x))
    }

    class Tanh : ActivationFunction {

        override fun fn(x: Float) = tanh(x)

        override fun fnPrime(x: Float) = 1 - pow(tanh(x), 2f)
    }

    class ReLu(private val alpha: Float = 0.1f) : ActivationFunction {

        override fun fn(x: Float) = if (x < 0) alpha * x else x

        override fun fnPrime(x: Float) = if (x < 0) alpha else 1f
    }

    class ELu(private val alpha: Float = 0.01f) : ActivationFunction {

        override fun fn(x: Float) = if (x < 0) alpha * (exp(x) - 1) else x

        override fun fnPrime(x: Float) = if (x < 0) fn(x) - alpha else 1f
    }

    class IsRLu(private val alpha: Float = 0.01f) : ActivationFunction {

        override fun fn(x: Float) = if (x < 0) x / (sqrt(1 + alpha * pow(x, 2f))) else x

        override fun fnPrime(x: Float) = if (x < 0) pow(1 / (sqrt(1 + alpha * pow(x, 2f))), 3f) else 1f
    }

    class SoftPlus : ActivationFunction {

        override fun fn(x: Float) = log(1 + exp(x))

        override fun fnPrime(x: Float) = 1 / (1 + exp(-x))
    }

    class Linear : ActivationFunction {

        override fun fn(x: Float) = x

        override fun fnPrime(x: Float) = 1f
    }

    class Swish : ActivationFunction {

        override fun fn(x: Float) = x / (1 + exp(-x))

        override fun fnPrime(x: Float) = (1 - x) / (1 + exp(-x)) + x / pow(1 + exp(-x), 2f)
    }

    interface ActivationFunction {

        fun fn(x: Float): Float

        fun fnPrime(x: Float): Float
    }
}

