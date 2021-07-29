package com.txu.scala

object AbstractTest {
  def main(args: Array[String]): Unit = {

    val s2 = new Student2
    s2.speak
    //println(s2.age) 不用不报错
  }
}

abstract class Person2{
  def speak
  def name:String
  def age:Int
}

class Student2 extends Person2 {
  override def speak: Unit = println(name)

  override def name: String = "xt"

  override def age= ???
}
