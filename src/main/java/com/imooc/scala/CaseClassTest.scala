package com.imooc.scala

//不用new 通常用于模式匹配
object CaseClassTest {
  def main(args: Array[String]): Unit = {
    println(CaseClassTest1("xt").name)
    println(CaseClassTest1("xt"))
  }
}

case class CaseClassTest1(name:String)
