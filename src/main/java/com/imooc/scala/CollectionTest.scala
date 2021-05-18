package com.imooc.scala


//App里有main，不需要main直接do
object CollectionTest extends App {
  println(singleStarExp("dasd*"))
  println(singleStarExp("*dasd"))
  println(singleStarExp("dasd"))
  println(singleStarExp("da*sd*"))
  println(singleStarExp("*dasd*"))
  println(singleStarExp("dasd*ads"))
  def singleStarExp(regex:String)={
    if (regex.substring(1,regex.length-1).contains('*')){
      false
    } else if(regex.charAt(0).equals('*') ^ regex.charAt(regex.length-1).equals('*')){
      true
    } else{
      false
    }
  }
}
