package com.txu.utils

import java.nio.charset.Charset

import org.apache.commons.lang3.StringUtils


/**
  * @version v1.0
  * @date 2021/5/20
  * @desc
  * @Since 2021/5/20 15:09
  **/
object Encode  extends App {
  while(true){

    var input =readLine()
    var rs = input
      if(!StringUtils.isAsciiPrintable(input)) {
      rs = input.getBytes("utf-8").mkString("utf","__","").replace("-","_")
      println(rs)
      var arr = scala.collection.mutable.ArrayBuffer[Byte]()
       rs.substring(3).split("__").toStream.foreach(x => arr += x.replace("_","-").toByte)
      println(new String(arr.toArray,"UTF-8"))
    }

  }

}
