package com.txu.scala

/**
 * @version v1.0
 * @date 2021/10/11
 * @desc
 * @Since 2021/10/11 11:57
 * */
class ArrayTest {
  def main(args: Array[String]): Unit = {
    val arr = Array(1,2,3)
        for (i <- arr.length - 1 until 0){
          println(i)
        }
  }
}
