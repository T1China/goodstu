package com.txu.job.excel


import org.apache.commons.lang3.ArrayUtils
import org.apache.commons.lang3.math.NumberUtils
import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.util.FileCopyUtils

import java.io.{File, FileInputStream}
import java.time.LocalDate
import java.util
import java.util.Collections
import scala.collection.mutable.ArrayBuffer


/**
 * @version v1.0
 * @date 2022/9/14
 * @desc
 * @Since 2022/9/14 14:38
 * */
object TransExcel {
  val CONSTANT_NAME_SEPARATOR = "_"
  val CONSTANT_FILE_EXCEL = ".xls"
  val CONSTANT_FILE_PDF = ".pdf"
//  val dedup = Array(0, 1, 3) //去除无用的列
  val dedup = Array[Integer]() //去除无用的列
  var mergeFirstCol = 0
  var mergeLastCol = 1
  var bus_idx = 1

  def main(args: Array[String]): Unit = {

    //    var nowdate = LocalDate.now()
    //    var curTime = System.currentTimeMillis()
    //    val dayStr = nowdate.getYear + CONSTANT_NAME_SEPARATOR + nowdate.getMonthValue + CONSTANT_NAME_SEPARATOR + nowdate.getDayOfYear
    //    val prefixStr = dayStr + curTime
    //    val execlPath = "C:\\Users\\tian.xu\\Desktop\\excel\\2022年9月15日砂糖送货明细.xls"
    //    val sles = "3.8.11.23.27.28.32.36".split("\\.", -1).map(_.toInt)
    //    val workDir = "C:\\tmp\\excelTranser\\"
    //    paserExcel("周四", prefixStr, execlPath, workDir)
    //    ExcelObj.setSels(sles)
    transExcelToPdf()
//    transExcelToPdf(true)
    //    PdfUtil.excel2pdf("C:\\Users\\tian.xu\\Desktop\\excel\\7月8月统计.xlsx")
    //    PdfUtil.cutWilteSpace("C:\\Users\\tian.xu\\Desktop\\excel\\7月8月统计.pdf")

  }

  //指定sheet名或者返回明天是周几
  def getSheetNameOrTomorrowWeekStr(week: String): String = {
    if (week == null || week.isEmpty) {
      var nowdate = LocalDate.now()
      val w = nowdate.getDayOfWeek.getValue + 1
      val weekStr = w match {
        case 7 => "周一"
        case 1 => "周一"
        case 2 => "周二"
        case 3 => "周三"
        case 4 => "周四"
        case 5 => "周五"
        case 6 => "周一"
        case 8 => "周一"
      }
      weekStr
    } else {
      week
    }
  }


  def getTotalMap(execlFile: File, sheetName: String): util.HashMap[String, String] = {

    val newExcelIn = new FileInputStream(execlFile)

    val sheetNameStr = getSheetNameOrTomorrowWeekStr(sheetName)
    //获取excel每行内容
    val excelMaps = ExcelUtil.readeExcelData(newExcelIn, sheetNameStr, 0, 0, null)

    var lastIdx = -1
    for (i <- 0 until excelMaps.size()) {
      if (excelMaps.get(i).get(2) == "合计") {
        lastIdx = i
      }
    }
    new util.HashMap[String, String]()
  }


  def paserExcel(sheetName: String = "", prefixStr: String, excelFile: String, workSpace: String = "/tmp/excelTranser/") = {

    val newExcelStr = workSpace + File.separator + prefixStr + CONSTANT_FILE_EXCEL
    val newExcelFile = new File(newExcelStr)
    val execlFile = new File(excelFile)
    val workDir = new File(workSpace)
    workDir.deleteOnExit()
    workDir.mkdir()
    FileCopyUtils.copy(execlFile, newExcelFile)
    val newExcelIn = new FileInputStream(newExcelFile)

    val sheetNameStr = getSheetNameOrTomorrowWeekStr(sheetName)
    //获取excel每行内容
    val excelMaps = ExcelUtil.readeExcelData(newExcelIn, sheetNameStr, 0, 0, null)

    var lastIdx = -1
    for (i <- 0 until excelMaps.size()) {
      if (excelMaps.get(i).get(2) == "合计") {
        lastIdx = i
      }
     }
    if (lastIdx == -1) throw new Exception("Could not confirm last line, last line should contains ‘合计’")


    var secondArr = excelMaps.get(1).values().toArray
    val title = excelMaps.get(0).get(0).toString
    val cols = new util.TreeMap[Int, Array[Object]]()
    var colLen = -1
    for (i <- 2 until lastIdx) {
      val tmpMap = excelMaps.get(i)
      val values = tmpMap.values().toArray
      if (i == 2) {
        colLen = values.size
      } else {
        assert(colLen == values.size, "Unusual number of cols")
      }
      cols.put(values.head.toString.toInt, values)
    }
    ExcelObj.setAttrs(title, secondArr, cols, colLen)
  }


  def transExcelToPdf(str:String = "C:\\Users\\tian.xu\\Desktop\\excel\\", isSumPer:Boolean = false) = {

    //分单子
    val choicesStr = "".replaceAll(" ", "")
    val choicesArr = choicesStr.split("\\.")
    val execlFiles = new File(str)
//    println("目录：" + str)
//    new File("测试execlTrans.txt").createNewFile()
    val execlFile = execlFiles.listFiles().filter(!_.isDirectory)
      .filter(t => t.toString.contains("明细") || t.toString.contains("砂糖")).last
    val in = new FileInputStream(execlFile)

    val s = ExcelUtil.readeExcelData(in, getSheetNameOrTomorrowWeekStr(""), 0, 0, null)
    var lastIdx = -1
    for (i <- 0 until s.size()) {
      if (s.get(i).get(2) == "合计" || s.get(i).get(1) == "合计" || s.get(i).get(0) == "合计") {
        lastIdx = i
      }
    }
    if (lastIdx == -1) throw new Exception("Could not confirm last line, last line should contains ‘合计’")

    var secondArr = s.get(1).values().toArray
    val CONSTANT_TITLE2_STR1 = "门店（"
    val CONSTANT_TITLE2_STR2 = "家）"
    val title = s.get(0).get(0).toString
    val colLen = s.get(0).size()
    val a1 = new ArrayBuffer[String]()
    val a2 = new ArrayBuffer[String]()
    val sum1 = new Array[Double](colLen)
    val sum2 = new Array[Double](colLen)
    for (i <- 2 until lastIdx) {
      val tmpMap = s.get(i)
      if (choicesArr.head.nonEmpty && choicesArr.contains(tmpMap.get(0))) {
        val values = tmpMap.values().toArray
        a1.append(values.mkString(","))
        values.zipWithIndex.map { kv =>
          val i = kv._2
          val data = kv._1.toString
          if (i > 1 && NumberUtils.isNumber(data)) {
            val oldNum = sum1(i)
            if (oldNum == null) {
              sum1(i) = data.toDouble
            } else {
              sum1(i) = oldNum + data.toDouble
            }
          }
        }
      } else {
        val values = tmpMap.values().toArray
        a2.append(values.mkString(","))
        values.zipWithIndex.map { kv =>
          val i = kv._2
          val data = kv._1.toString
          if (i > 1 && NumberUtils.isNumber(data)) {
            val oldNum = sum2(i)
            if (oldNum == null) {
              sum2(i) = data.toDouble
            } else {
              sum2(i) = oldNum + data.toDouble
            }
          }
        }
      }
    }
    var t1 = 0.0
    var sumStr1 = sum1.map { a =>
      if (a != null) {
        if (ExcelUtil.isInt(a.asInstanceOf[Double])) {
          val s1 = a.asInstanceOf[Number].intValue()
          t1 += s1
          s1.toString
        } else {
          t1 += a
          a.toString
        }
      } else {
        ""
      }
    }

    sumStr1(0) = "总计"
    sumStr1(1) = t1.toString
    t1 = 0
    var sumStr2 = sum2.map { a =>
      if (a != null) {
        if (ExcelUtil.isInt(a.asInstanceOf[Double])) {
          val s1 = a.asInstanceOf[Number].intValue()
          t1 += s1
          s1.toString
        } else {
          t1 += a
          a.toString
        }
      } else {
        null
      }
    }
    sumStr2(0) = "总计"
    sumStr2(1) = t1.toString
    val time = LocalDate.now().toString + "_"   + System.currentTimeMillis().toString

    val newExcelStr = str.split("\\.xls").head + time + ".xls"
    val newpdfStr = str.split("\\.xls").head + time + ".pdf"
    val writer = new ExcelWriter(newExcelStr)

    val wb = new XSSFWorkbook
    writer.createSheet(wb, 0, "1")
    var cellStyle: CellStyle = if (isSumPer) {
      writer.getCellStyle("宋体", 16, true, 1, 0)
    } else {
      writer.getCellStyle("宋体", 20, true, 0, 0)
    }
    val otherCellStyle = writer.getCellStyle("宋体", 16, true, 1, 1)
    otherCellStyle.setWrapText(true);
    cellStyle.setWrapText(true);
    val arr2: Array[Integer] = Array[Integer](0, 6)
    val styleCellCols = new util.HashSet[Integer]()
    0.until(colLen).map(styleCellCols.add(_))
    val styleCellCols1 = new util.HashSet[Integer]()
    styleCellCols1.addAll(styleCellCols)
    styleCellCols1.remove(4)
    dedup.map(styleCellCols1.remove(_))
    if (a2.nonEmpty) {
      if (!isSumPer) {
        writer.merge(0, 0, 0, colLen - 1, title, cellStyle, 50, 30 * 110, null, styleCellCols1)
        if (secondArr(1) == "门店") {
          bus_idx = 1
          mergeLastCol = 1
          secondArr(1) = CONSTANT_TITLE2_STR1 + a2.size + CONSTANT_TITLE2_STR2
        }
        if (secondArr(2) == "门店") secondArr(2) = {
          bus_idx = 2
          mergeLastCol = 2
          CONSTANT_TITLE2_STR1 + a2.size + CONSTANT_TITLE2_STR2
        }
        if (secondArr(3) == "门店") secondArr(3) = {
          bus_idx = 3
          mergeLastCol = 3
          CONSTANT_TITLE2_STR1 + a2.size + CONSTANT_TITLE2_STR2
        }
        if (secondArr(4) == "门店") secondArr(4) = {
          bus_idx = 4
          mergeLastCol = 4
          CONSTANT_TITLE2_STR1 + a2.size + CONSTANT_TITLE2_STR2
        }
        val titleList = secondArr.zipWithIndex.filter(kv => !dedup.contains(kv._2)).map(_._1)
        writer.write(titleList.mkString(","), cellStyle, otherCellStyle, 100, 30 * 110, styleCellCols, styleCellCols1)
        var i = 1
        a2.map { str =>
          i += 1
          val strList = str.split(",", -1).zipWithIndex.filter(kv => !dedup.contains(kv._2)).map(a => if (a._1 == "0") "" else a._1)
          writer.write(strList.mkString(","), cellStyle, otherCellStyle, 40, 30 * 100, styleCellCols, styleCellCols1)
          writer.merge(i, i, mergeFirstCol, mergeLastCol, strList(bus_idx), cellStyle, 25, 30 * 110, null, styleCellCols1)
          }

        val strList1 = sumStr2.zipWithIndex.filter(kv => !dedup.contains(kv._2)).map(_._1)
        writer.write(strList1.mkString(","), cellStyle, otherCellStyle, 40, 30 * 110, styleCellCols, styleCellCols1)
      } else {
        writer.merge(0, 0, 0, colLen - 1, title, cellStyle, 50, 30 * 110, null, styleCellCols1)
        if (secondArr(1) == "门店") secondArr(1) = CONSTANT_TITLE2_STR1 + a2.size + CONSTANT_TITLE2_STR2
        if (secondArr(2) == "门店") secondArr(2) = CONSTANT_TITLE2_STR1 + a2.size + CONSTANT_TITLE2_STR2
        if (secondArr(3) == "门店") secondArr(3) = CONSTANT_TITLE2_STR1 + a2.size + CONSTANT_TITLE2_STR2
        if (secondArr(4) == "门店") secondArr(4) = CONSTANT_TITLE2_STR1 + a2.size + CONSTANT_TITLE2_STR2
        var i = 1
        a2.map { str =>
          i += 1
          val strList = str.split(",", -1).zipWithIndex.map(a => if (dedup.contains(a._2) || a._1 == "0" || a._1.trim.isEmpty) (a._2, "") else if (a._2 == 2) (a._2, a._1) else (a._2, a._1 + secondArr(a._2))).filter(kv => !dedup.contains(kv._2) && kv._2.nonEmpty).map(_._2)
          val content = strList.mkString(",")
          writer.merge(i, i, 0, colLen / 2, content, cellStyle, 60, 30 * 110, null, styleCellCols1)
          if (i == 20) {
            writer.createSheet(wb, 1, "2")
          }
        }
      }
    }

    if (a1.nonEmpty) {
      writer.createSheet(wb, 1, "2")
      if (!isSumPer) {

        writer.merge(0, 0, 0, colLen - 1, title, cellStyle, 50, 30 * 110, null, styleCellCols1)
        val secondList = secondArr.map { elem =>
          if (elem.toString.startsWith("门店")) {
            bus_idx = 2
            mergeLastCol = 2
            CONSTANT_TITLE2_STR1 + a1.size + CONSTANT_TITLE2_STR2
          } else {
            elem.toString
          }
        }
        val titleList = secondArr.zipWithIndex.filter(kv => !dedup.contains(kv._2)).map(_._1)
        writer.write(secondList.mkString(","), cellStyle, otherCellStyle, 100, 30 * 110, styleCellCols, styleCellCols1)
        var i = 1
        a1.map { str =>
          i += 1
          val strList = str.split(",", -1).zipWithIndex.filter(kv => !dedup.contains(kv._2)).map(a => if (a._1 == "0") "" else a._1)
          writer.write(strList.mkString(","), cellStyle, otherCellStyle, 40, 30 * 100, styleCellCols, styleCellCols1)
          writer.merge(i, i, mergeFirstCol, mergeLastCol, strList(bus_idx), cellStyle, 25, 30 * 110, null, styleCellCols1)
        }

        val strList1 = sumStr1.zipWithIndex.filter(kv => !dedup.contains(kv._2)).map(_._1)
        writer.write(strList1.mkString(","), cellStyle, otherCellStyle, 40, 30 * 110, styleCellCols, styleCellCols1)
      } else {
        writer.merge(0, 0, 0, colLen - 1, title, cellStyle, 50, 30 * 110, null, styleCellCols1)
        if (secondArr(1) == "门店") secondArr(1) = CONSTANT_TITLE2_STR1 + a2.size + CONSTANT_TITLE2_STR2
        if (secondArr(2) == "门店") secondArr(2) = CONSTANT_TITLE2_STR1 + a2.size + CONSTANT_TITLE2_STR2
        if (secondArr(3) == "门店") secondArr(3) = CONSTANT_TITLE2_STR1 + a2.size + CONSTANT_TITLE2_STR2
        if (secondArr(4) == "门店") secondArr(4) = CONSTANT_TITLE2_STR1 + a2.size + CONSTANT_TITLE2_STR2
        var i = 1
        a2.map { str =>
          i += 1
          val strList = str.split(",", -1).zipWithIndex.map(a => if (dedup.contains(a._2) || a._1 == "0" || a._1.trim.isEmpty) (a._2, "") else if (a._2 == 2) (a._2, a._1) else (a._2, a._1 + secondArr(a._2))).filter(kv => !dedup.contains(kv._2) && kv._2.nonEmpty).map(_._2)
          val content = strList.mkString(",")
          writer.merge(i, i, 0, colLen / 2, content, cellStyle, 60, 30 * 110, null, styleCellCols1)
          if (i == 20) {
            writer.createSheet(wb, 1, "2")
          }
        }
      }
    }
//
//    if (a1.nonEmpty) {
//      writer.createSheet(wb, 1, "2")
//      writer.merge(0, 0, 0, colLen - 1, title, cellStyle, 50, 30 * 110, null, styleCellCols1)
//      if (secondArr(1) == "门店") secondArr(1) = CONSTANT_TITLE2_STR1 + a2.size + CONSTANT_TITLE2_STR2
//      if (secondArr(2) == "门店") secondArr(2) = CONSTANT_TITLE2_STR1 + a2.size + CONSTANT_TITLE2_STR2
//      if (secondArr(3) == "门店") secondArr(3) = CONSTANT_TITLE2_STR1 + a2.size + CONSTANT_TITLE2_STR2
//      if (secondArr(4) == "门店") secondArr(4) = CONSTANT_TITLE2_STR1 + a2.size + CONSTANT_TITLE2_STR2
//      writer.write(secondArr.mkString(","), cellStyle, otherCellStyle, 40, 30 * 110, styleCellCols, styleCellCols1)
//      a1.map(writer.write(_, cellStyle, otherCellStyle, 40, 30 * 86, styleCellCols, styleCellCols1))
//      writer.write(sumStr1.mkString(","), cellStyle, otherCellStyle, 40, 30 * 110, styleCellCols, styleCellCols1)
//    }


    writer.flush()
    writer.close()
    println(s.toString)
    PdfUtil.excel2pdf(newExcelStr)
    PdfUtil.cutWilteSpace(newpdfStr)
  }
}


object ExcelObj {
  var title: String = null
  var secondArr: Array[Object] = null
  var cols: util.TreeMap[Int, Array[Object]] = null
  var colSize: Int = -1
  var sels: Array[Int] = Array()
  val CONSTANT_TITLE2_STR1 = "门店（"
  val CONSTANT_TITLE2_STR2 = "家）"


  def setAttrs(title: String, secondArr: Array[Object], cols: util.TreeMap[Int, Array[Object]], colSize: Int) = {
    this.title = title
    this.secondArr = secondArr
    this.cols = cols
    this.colSize = colSize
  }

  def setSels(sels: Array[Int]) = {
    this.sels = sels
  }

  def getSelsCols = sels.map(cols.get(_))

  def getNonSelsCols = cols.keySet().toArray.filter(!sels.contains(_)).map(cols.get(_))

  def getTotals(cols: Array[Array[Object]]) = {
    if (cols == null || cols.isEmpty) {
      null
    } else {
      val sum = new Array[Double](colSize)
      for (i <- cols) {
        val tmpArr = i
        tmpArr.zipWithIndex.map { kv =>
          val i = kv._2
          val data = kv._1.toString
          if (i > 3 && NumberUtils.isNumber(data)) {
            val oldNum = sum(i)
            if (oldNum == null) {
              sum(i) = data.toDouble
            } else {
              sum(i) = oldNum + data.toDouble
            }
          }
        }
      }
      val totals = sum.sum
      val rs = sum.map { num =>
        if (ExcelUtil.isInt(num)) {
          num.asInstanceOf[Number].intValue().toString
        } else {
          num.toString
        }
      }
      rs(0) = "总计"
      rs(1) = totals.toString
      rs
    }
  }

  def transToPdf(dirStr: String, prefixStr: String) = {
    val time = System.currentTimeMillis().toString
    val newExcelStr = dirStr + prefixStr + TransExcel.CONSTANT_NAME_SEPARATOR + 1 + TransExcel.CONSTANT_FILE_EXCEL
    val newpdfStr = dirStr + prefixStr + TransExcel.CONSTANT_FILE_PDF
    val writer = new ExcelWriter(newExcelStr)
    val a2Arr = ExcelObj.getNonSelsCols
    val a1Arr = ExcelObj.getSelsCols
    val a2 = a2Arr.map(_.mkString(","))
    val a1 = a1Arr.map(_.mkString(","))
    val wb = new XSSFWorkbook
    writer.createSheet(wb, 0, "1")
    val cellStyle: CellStyle = writer.getCellStyle("宋体", 16, true, 0, 0)

    val styleCellCols = new util.HashSet[Integer]()
    0.until(colSize).map(styleCellCols.add(_))
    val styleCellCols1 = new util.HashSet[Integer]()
    styleCellCols1.addAll(styleCellCols)
    styleCellCols1.remove(4)

    writer.merge(0, 0, 0, colSize - 1, title, cellStyle, 50, 30 * 110, null, styleCellCols1)
    secondArr(1) = CONSTANT_TITLE2_STR1 + a2.size + CONSTANT_TITLE2_STR2
    writer.write(secondArr.mkString(","), cellStyle, cellStyle, 40, 30 * 110, styleCellCols, styleCellCols1)
    a2.map(writer.write(_, cellStyle, cellStyle, 40, 30 * 100, styleCellCols, styleCellCols1))
    writer.write(getTotals(a2Arr).mkString(","), cellStyle, cellStyle, 40, 30 * 110, styleCellCols, styleCellCols1)


    if (a1.nonEmpty) {
      writer.createSheet(wb, 1, "2")
      writer.merge(0, 0, 0, colSize - 1, title, cellStyle, 50, 30 * 110, null, styleCellCols1)
      secondArr(1) = CONSTANT_TITLE2_STR1 + a1.size + CONSTANT_TITLE2_STR2
      writer.write(secondArr.mkString(","), cellStyle, cellStyle, 40, 30 * 110, styleCellCols, styleCellCols1)
      a1.map(writer.write(_, cellStyle, cellStyle, 40, 30 * 86, styleCellCols, styleCellCols1))
      writer.write(getTotals(a1Arr).mkString(","), cellStyle, cellStyle, 40, 30 * 110, styleCellCols, styleCellCols1)
    }


    writer.flush()
    writer.close()
    PdfUtil.excel2pdf(newExcelStr)
    PdfUtil.cutWilteSpace(newpdfStr)
  }
}