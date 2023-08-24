package com.txu.job

import java.net.URLEncoder

/**
 * @version v1.0
 * @date 2023/3/16
 * @desc
 * @Since 2023/3/16 22:37
 * */
object Test1 {
  def main(args: Array[String]): Unit = {
    val cypherStr = "use wiki;match (a) return a limit 1;"
    val cypherBody = "?cypher=" + URLEncoder.encode(cypherStr, "UTF-8")
    val rowLimit = "&limit=-1"
    val username = "&username=hive"
    val password = "&password=123456"
    val getBody =  cypherBody + rowLimit + username + password
    println(getBody)
  }

}
