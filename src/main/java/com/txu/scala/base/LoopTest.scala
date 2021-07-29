package com.txu.scala.base

object LoopTest {

  def whileLoop(arr:Array[Int]): Unit = {
    var idx = 0
    var n = arr.length
    val tStart = System.currentTimeMillis()
    while (idx < n) {
      arr(idx) = 1
      idx += 1
    }
    val tEnd = System.currentTimeMillis()
    println("while loop took " + (tEnd - tStart) + "ms")
  }

  def forLoop(arr:Array[Int]): Unit = {
    var idx = 0
    var n = arr.length
    val tStart = System.currentTimeMillis()
    for(idx <- 0 until n) {
      arr(idx) = 1
    }
    val tEnd = System.currentTimeMillis()
    println("for loop took " + (tEnd - tStart) + "ms")
  }

  def foreachLoop(arr:Array[Int]): Unit = {
    var n = arr.length
    val tStart = System.currentTimeMillis()
    (0 until n).foreach{idx => arr(idx) = 1}
    val tEnd = System.currentTimeMillis()
    println("foreach range took " + (tEnd - tStart) + "ms")
  }

  def foreachFuncLoop(arr:Array[Int]): Unit = {
    val tStart = System.currentTimeMillis()
    arr.foreach{ idx => arr(idx) = 1}
    val tEnd = System.currentTimeMillis()
    println("foreach function took " + (tEnd - tStart) + "ms")
  }

  def profileRun(n: Int) {
    val arr = new Array[Int](n)

    whileLoop(arr)
    foreachLoop(arr)
    forLoop(arr)
    foreachFuncLoop(arr)
  }

  def main(args:Array[String]) {
    profileRun(500000000)
  }

}
