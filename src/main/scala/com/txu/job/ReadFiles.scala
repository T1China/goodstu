package com.txu.job

import java.io.File
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
 * @version v1.0
 * @date 2022/9/9
 * @desc 解析chpher文件 to json
 * @Since 2022/9/9 11:16
 * */
object ReadFiles extends App{
  val cypherDir = new File("C:\\goodjob\\ldbc\\cypher")
val t1 = System.nanoTime();
val t3 = System.currentTimeMillis()
  val files = getFile(cypherDir)


  files.map { file =>
    val iter = scala.io.Source.fromFile(file).getLines()
    if (!iter.isEmpty) {
      var inputNames = new ArrayBuffer[String]()
      var inputTypes = new ArrayBuffer[String]()
      var outputNames = new ArrayBuffer[String]()
      var outputTypes = new ArrayBuffer[String]()
      val caseNames = file.getName.split("-",3)
      val caseName = caseNames(0).substring(0,1) + caseNames(1).substring(0,1) + "-" + caseNames(2).split("\\.")(0).toInt.toString
      var flag = true
      val queryBuf = new StringBuffer()
      while (iter.hasNext && flag) {
        val tmp = iter.next()
        if (tmp.startsWith("/*") && iter.hasNext) {
          val params = iter.next()
          if (params.startsWith("params:") && iter.hasNext) {
            var flag1 = true
            while (flag1 && iter.hasNext) {
              val param = iter.next()
              if (param.startsWith("result:") && iter.hasNext) {
                var flag2 = true
                while (flag2 && iter.hasNext) {
                  val result = iter.next()
                  if (result.startsWith("*/")) {
                    flag2 = false
                  } else {
                    val kv = result.split(" ", 2)
                    if (kv.size > 1 && kv(0).nonEmpty && kv(1).nonEmpty) {
                      outputNames.append(kv(0))
                      outputTypes.append(kv(1))
                    }
                  }
                }
                flag1 = false
              } else {
                val kv = param.split(" ", 2)
                if (kv.size > 1 && kv(0).nonEmpty && kv(1).nonEmpty) {
                  inputNames.append(kv(0))
                  inputTypes.append(kv(1))
                }
              }
            }

          }

        }

          if (tmp.toLowerCase.startsWith("match")) {
            queryBuf.append(tmp+"\n")
            var flag3 = true
            while (flag3 && iter.hasNext) {
              var tmpQueryStr = iter.next()
              if (tmpQueryStr.endsWith(";")) {
                queryBuf.append(tmpQueryStr.substring(0,tmpQueryStr.size - 1)+"\n")
                flag3 = false
                flag = false
              } else {
                queryBuf.append(tmpQueryStr+"\n")
              }
          }
        }
      }
      println(caseName)
      println(inputNames.mkString(",") + "  " + inputTypes.mkString(","))
      println(outputNames.mkString(",") + "  " + outputTypes.mkString(","))
      println(queryBuf.toString.trim.replaceAll("\\s+", " "))
    }
  }

  val cypherDir1 = new File("C:\\goodjob\\ldbc\\cypher\\1")
  cypherDir1.delete()
  cypherDir1.createNewFile()
  val t2 = System.nanoTime();
  val t4 = System.currentTimeMillis();
  println(t2-t1)
  println(t4-t3)
  def getFile(file: File):Array[File] = {
    val files = file.listFiles().filter(! _.isDirectory)
      .filter(t => t.toString.endsWith(".cypher"))
    files ++ file.listFiles().filter(_.isDirectory).flatMap(getFile(_))
  }
}
