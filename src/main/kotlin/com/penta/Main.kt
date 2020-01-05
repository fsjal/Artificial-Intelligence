package com.penta

import kotlin.reflect.KClass

fun main2(args: Array<String>) {
//    val x = (-50 .. 50).map { it/10.0 }
//    val y = x.map { Math.sin(it) }

    // create your PlotPanel (you can use it as a JPanel)
    // Create Chart
    //val chart = QuickChart.getChart("Sample Chart", "X", "Y", "y(x)", x, y)
    // Show it
    //SwingWrapper(chart).displayChart()

//    (0 until 10).map { (it to it+1) }.forEach { println(it) }

}
@Target(AnnotationTarget.FIELD)
annotation class Test2(val name: String = "")

class Person {

    fun init(h: Human) {
        val x = Human::class.java.declaredFields.filter { it.isAnnotationPresent(Test2::class.java) }
        x.forEach {
            val a = it.getAnnotation(Test2::class.java)
            it.isAccessible = true
            it.set(h, a.name)
            it.isAccessible = false
        }
    }

    fun fetch(p: KClass<*>) {
        val x = p.java.declaredFields.filter { it.isAnnotationPresent(Test2::class.java) }
        p::class.java.declaredFields.forEach { println(it) }
        println(x.size)



        x.forEach {
            val a = it.getAnnotation(Test2::class.java)
            it.isAccessible = true
            println(it.get(null))
            it.isAccessible = false
        }
    }
}

object Lol {
    @Test2
    var aha = "ok lol"

}

class Human {
    @Test2(name = "Nader")
    val namae =""

    init {
        Person().init(this)
    }
}

fun main(args: Array<String>) {
    Person().fetch(Lol::class)
}

fun random(min: Int, max: Int) = ((max - min + 1) * Math.random() + min).toInt()