package com.penta.network

object Loss {

    val MSE: LossFunction get() = Mse()

    val CROSS_ENTROPY: LossFunction get() = CrossEntropy()

    class Mse: LossFunction {

        override fun fn(target: Float, prediction: Float) = 0.5f * pow(target - prediction, 2f)

        override fun fnPrime(target: Float, prediction: Float) = prediction - target
    }

    class CrossEntropy: LossFunction {
        override fun fn(target: Float, prediction: Float) = -(target * log(prediction) + (1 - target) * log(1 - prediction))

        override fun fnPrime(target: Float, prediction: Float) = -(target / prediction - (1 - target) / (1 - prediction))
    }

    interface LossFunction {

        fun fn(target: Float, prediction: Float): Float

        fun fnPrime(target: Float, prediction: Float): Float
    }
}