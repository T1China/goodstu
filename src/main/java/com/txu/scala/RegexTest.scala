package com.txu.scala

import java.util
import java.util.{ArrayList, HashSet}
import java.util.regex.Pattern
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
object RegexTest extends App {
//  getList.foreach(x => print(x))

  val dateRegex = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$"
  println("2020-02-01T00:00:00".matches(dateRegex))
  println("2020-02-01 00:00:00".matches(dateRegex))

  def sureType(name:String,labelsTypeMap:mutable.HashMap[String,mutable.HashMap[String,String]],labels:Array[String]): String ={
    var rs:String = null;
    var tmpMap = new mutable.HashMap[String,String]()
    for (i <- 0 until labels.length){
      tmpMap = labelsTypeMap.get(labels(i)).getOrElse(null)
      if (tmpMap!=null){
        var tmp = tmpMap.get(name).getOrElse(null)
        if (rs == null){
          rs = tmp
        }else{
          if(tmp!=null && tmp != rs){
            return "Dynamic"
          }
        }
      }
    }
    rs
  }

//  println(Pattern.matches("*", "das"))
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




