package com.txu.lambda.mode.test

import java.util
import java.util.concurrent.ForkJoinPool
import scala.collection.mutable

/**
 * @version v1.0
 * @date 2022/10/14
 * @desc
 * @Since 2022/10/14 18:01
 * */
object StreamDemo extends App {



  def giveRangeOfLongs_whenSummedInParallel_shouldBeEqualToExpectedTotal(): Unit = {
    val firstNum = 1
    val lastNum = 1000000
    val aList = firstNum until lastNum
    val customThreadPool = new ForkJoinPool(4)
//    val actualTotal = customThreadPool.

  }
}
