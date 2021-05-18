package com.imooc.scala

object HelloWorld {
  def main(args: Array[String]): Unit = {
//    println("Hello World")
//    println(add(1, 3))
//    println(add1)
//    sayHello
//    sayAny()
//    println(speed(time = 10.0, dist = 100.0))

//    Range(1,10)//[1,10)
//    Range(1,10,3)//(1,4,7)
//    1 to 10 //[1,10]
//    1.to(10)
//    1 until 10 //[1,10) 底层都是range
//    1.until(10)
//
//    for(i <- 1 to 10 if i % 2 == 0) println(i)
//    val courses = Array("a","b","c","d")
//    courses.foreach(c => println(c))

//    var (a, b) = (100, 0)
//    while(a>0){
//      b+=a
//      a=a-1
//    }
//    println(b)

    val a = new EntityTest //class得用val声明？
    a.prtv1()


  }

  private def add(x: Int, y: Int): Int = {
    x +  y
  }
  def add1() = 1 + 3
  def sayHello:Unit={
    println("Hello")
  }

  def sayAny(string: String = "cool") = {
    println(string)
  }


  def speed(dist:Double,time:Double):Double=dist/time

  def sum(a:Int,b:Int*):Int={
    var rs = a
    for (elem <- b) {
      rs+=elem
    }
    rs
  }
}

class EntityTest{
  var x:Int =_ //使用占位符要定义类型
  var d = 1.1
  private  val a = 1l //只能在类里访问 private [this] val a = 1
  def prtv1()= println(a)
}
