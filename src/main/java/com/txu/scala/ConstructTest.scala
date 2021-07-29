package com.txu.scala

object ConstructTest {
  def main(args: Array[String]): Unit = {

    var p =new Person("xt",27)
    p = new Person("xt",30,"male")
    p = new Student("xt",30,"male")
    println(p.gender)
    p()
    p()
  }
}

//主构造器
class Person(var name:String,var age:Int){
  def apply(): Unit = println("parents apply")

  val school:String="ahsu"
  var gender:String=_
  println("Master Contructor enter")
  //附属构造器
  def this(name:String,age:Int,gender:String){
    //先加载主构造器
    this(name,age)
    println("Follower Contructor enter")
    this.gender=gender
    println("Follower Contructor leave")
  }
  println("Master Contructor leave")
}

//先加载父类的，再加载子类的构造器，父类未赋值的不会报错了 null

//override var name:String 不能overwrite入参可变
class Student(name:String,age:Int,var major:String) extends Person(name,age){
  println("Child class Constructor enter")
  // var school = "" 父类有的属性会报错，需要overwrite
  //需要把父类的var改成val，可变参不用overwrite
  override val school = "ahnu"
  override def toString: String = "ggfly"
  println("Child class Constructor leave")

   def apply(name: String, age: Int, major: String): Unit = println("child apply")
}