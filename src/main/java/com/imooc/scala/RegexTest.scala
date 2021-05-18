package com.imooc.scala

import java.util
import java.util.ArrayList
import java.util.regex.Pattern
object RegexTest extends App {
//  getList.foreach(x => print(x))

  println(Pattern.matches("*", "das"))
//  println(m1("asd", "as*"))

  def m1(str: String,regex: String) = str match {
    case regex => true
    case _ => false
  }

//  println("a".matches("a.*"))
  private def getList():Array[String] = {
    var list = Array("my_graph4", "v", "v", "a")

    list
  }

}



