package com.imooc.scala.ReferenceTest

import scala.collection.mutable

object WeakMapForCacheTest {
  //todo limit map.size , if not will cost much gct
  // and control timeliness  lru?

  val graphSchemaCache = new mutable.WeakHashMap[String,String]
  
  def getGraphSchema(graphName:String) = {
    graphName.hashCode.toString
  }

  def cacheGraph(graphName:String)={
      var schema = getGraphSchema(graphName)
      if (schema.isEmpty) throw new  Exception("can not getGraph from db")
      //new String to keep single reference
      graphSchemaCache.put(new String(graphName),schema)
      schema
  }

  def getGraph(graphName:String)={
    var graphSchemaOp = graphSchemaCache.get(graphName)

    if (graphSchemaOp.isEmpty){
      println("cache " + graphName)
      cacheGraph(graphName)
    } else {
      graphSchemaOp.get
    }
  }

  def main(args: Array[String]): Unit = {

    println(WeakMapForCacheTest.getGraph("key-1"))
    println(WeakMapForCacheTest.getGraph("key-2"))
    println(WeakMapForCacheTest.getGraph("key-3"))
    println(WeakMapForCacheTest.getGraph(new String("key-1")))
    println(WeakMapForCacheTest.getGraph(new String("key-1")))
    System.gc()
    println(WeakMapForCacheTest.graphSchemaCache.size)
    println(WeakMapForCacheTest.getGraph("key-1"))
    println(WeakMapForCacheTest.graphSchemaCache.size)
    println(WeakMapForCacheTest.getGraph(new String("key-1")))
    Thread.sleep(3000)
    println(WeakMapForCacheTest.graphSchemaCache.size)




val weakHashMap =  new mutable.WeakHashMap[String,String]

    //向weakHashMap中添加4个元素
    var i: Int = 0
    while ( {
      i < 3
    }) {
      weakHashMap.put("key-" + i, "value-" + i)

      {
        i += 1; i - 1
      }
    }
    //输出添加的元素
    System.out.println("数组长度：" + weakHashMap.size + "，输出结果：" + weakHashMap)
    val s = "key-1"
    println(s)
    System.out.println("数组长度：" + weakHashMap.size + "，输出结果：" + weakHashMap.get("key-1"))
    //主动触发一次GC
    System.gc()
    System.out.println("数组长度：" + weakHashMap.size + "，输出结果：" + weakHashMap.get("key-1"))
    //再输出添加的元素
    System.out.println("数组长度：" + weakHashMap.size + "，输出结果：" + weakHashMap)
  }
}
