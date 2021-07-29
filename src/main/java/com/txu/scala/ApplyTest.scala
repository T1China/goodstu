package com.txu.scala

object ApplyTest {
  def main(args: Array[String]): Unit = {

    val a1 = new ApplyTest1
    println("~~~~~~~~~~")
    a1() // class apply
    println("~~~~~~~~~~")
    ApplyTest1.sayHi//其实object是单例
    println("~~~~~~~~~~")
    ApplyTest1()//()都是调的apply,()不是构造器，且调的是父类apply?
  }
}

//object是class的伴生对象，class是object的伴生类
//object中的内容都是class的静态内容
//抽象类是不可以直接实例化的，在apply中可以实例化抽象类的子类
//scala中可以在object中构造很多apply方法
//Array使用了
object ApplyTest1{
  println("object entry")
  def sayHi = println("hi")
  //最佳实践在object调class
//  def apply(): ApplyTest1 = new ApplyTest1()
  def apply(): Unit = println("object apply")
  println("object leave")

}

class ApplyTest1{
  println("class entry")
  def apply(): Unit = println("class apply") //
  println("class leave")
}