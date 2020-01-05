package com.penta.network

val Double.f: Float get() = this.toFloat()
val Float.d: Double get() = this.toDouble()

fun exp(x: Float) = Math.exp(x.d).f

fun log(x: Float) = Math.log(x.d).f

fun sqrt(x: Float) = Math.sqrt(x.d).f

fun pow(x: Float, p: Float) = Math.pow(x.d, p.d).f

fun rand() = Math.random().f

fun rand(min: Float, max: Float) = (max - min) * rand() + min

fun tanh(x: Float) = Math.tanh(x.d).f