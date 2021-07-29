package com.txu.scala.crux

import org.json.{JSONArray, JSONObject}

import scala.collection.immutable.TreeMap
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.util.Try

/**
  * @version v1.0
  * @date 2021/5/17
  * @desc
  * @Since 2021/5/17 18:05
  **/
/*
(b:B {id int index:DEFAULT,name string})
      {
      "compress.policy": "global",
      "field.schemas": [
      {
      "field.extra.attr": {"field.type": "STRING"},
      "field.name": "__tags",
      "field.type": "ARRAY"
      },
      {
      "field.name": "name",
      "field.type": "STRING"
      },
      {
      "field.name": "id",
      "field.type": "INT"
      }
      ],+



      "index.schemas": ["id"],
      "label.index": 1,
      "label.value": "B"
      }
*/
object DescCypherMarker extends App{
  var json = "{                                                       \n  \"__VERSION\": 10,                                      \n  \"edge.tables\": [{                                     \n    \"compress.policy\": \"global\",                        \n    \"field.schemas\": [                                  \n      {                                                 \n        \"field.extra.attr\": {\"field.type\": \"STRING\"},   \n        \"field.name\": \"__tags\",                         \n        \"field.type\": \"ARRAY\"                           \n      },                                                \n      {                                                 \n        \"field.name\": \"name\",                           \n        \"field.type\": \"DOUBLE\"                          \n      }                                                 \n    ],                                                  \n    \"index.schemas\": [],                                \n    \"label.index\": 2,                                   \n    \"label.value\": \"F\"                                  \n  }],                                                   \n  \"graph.encryption.type\": \"NO_ENCRYPTION\",             \n  \"graph.index.separated\": false,                       \n  \"graph.index.type\": \"NATIVE\",                         \n  \"graph.name\": \"itc1\",                                 \n  \"graph.replication.number\": 3,                        \n  \"graph.shard.number\": 3,                              \n  \"vertex.tables\": [                                    \n    {                                                   \n      \"compress.policy\": \"global\",                      \n      \"field.schemas\": [                                \n        {                                               \n          \"field.extra.attr\": {\"field.type\": \"STRING\"}, \n          \"field.name\": \"__tags\",                       \n          \"field.type\": \"ARRAY\"                         \n        },                                              \n        {                                               \n          \"field.name\": \"name\",                         \n          \"field.type\": \"STRING\"                        \n        }                                               \n      ],                                                \n      \"index.schemas\": [],                              \n      \"label.index\": 0,                                 \n      \"label.value\": \"A\"                                \n    },                                                  \n    {                                                   \n      \"compress.policy\": \"global\",                      \n      \"field.schemas\": [                                \n        {                                               \n          \"field.extra.attr\": {\"field.type\": \"STRING\"}, \n          \"field.name\": \"__tags\",                       \n          \"field.type\": \"ARRAY\"                         \n        },                                              \n        {                                               \n          \"field.name\": \"name\",                         \n          \"field.type\": \"INT\"                           \n        }                                               \n      ],                                                \n      \"index.schemas\": [],                              \n      \"label.index\": 1,                                 \n      \"label.value\": \"b\"                                \n    },                                                  \n    {                                                   \n      \"compress.policy\": \"global\",                      \n      \"field.schemas\": [                                \n        {                                               \n          \"field.extra.attr\": {\"field.type\": \"STRING\"}, \n          \"field.name\": \"__tags\",                       \n          \"field.type\": \"ARRAY\"                         \n        },                                              \n        {                                               \n          \"field.name\": \"id\",                           \n          \"field.type\": \"INT\"                           \n        }                                               \n      ],                                                \n      \"index.schemas\": [],                              \n      \"label.index\": 3,                                 \n      \"label.value\": \"C\"                                \n    },                                                  \n    {                                                   \n      \"compress.policy\": \"global\",                      \n      \"field.schemas\": [                                \n        {                                               \n          \"field.extra.attr\": {\"field.type\": \"STRING\"}, \n          \"field.name\": \"__tags\",                       \n          \"field.type\": \"ARRAY\"                         \n        },                                              \n        {                                               \n          \"field.name\": \"time\",                         \n          \"field.type\": \"LOCAL_DATETIME\"                \n        }                                               \n      ],                                                \n      \"index.schemas\": [],                              \n      \"label.index\": 4,                                 \n      \"label.value\": \"d1\"                               \n    },                                                  \n    {                                                   \n      \"compress.policy\": \"global\",                      \n      \"field.schemas\": [                                \n        {                                               \n          \"field.extra.attr\": {\"field.type\": \"STRING\"}, \n          \"field.name\": \"__tags\",                       \n          \"field.type\": \"ARRAY\"                         \n        },                                              \n        {                                               \n          \"field.name\": \"id\",                           \n          \"field.type\": \"INT\"                           \n        }                                               \n      ],                                                \n      \"index.schemas\": [],                              \n      \"label.index\": 5,                                 \n      \"label.value\": \"C1\"                               \n    }                                                   \n  ]                                                     \n}                                                       "
  var ob = new JSONObject(json)

//(b:B {id int index:DEFAULT,name string})
var vetexArr = Try(ob.getJSONArray(GraphSchemaConst.VERTEX_TABLES)).getOrElse(null)
var edgeArr = Try(ob.getJSONArray(GraphSchemaConst.EDGE_TABLES)).getOrElse(null)
var isVertex =true
  println(getSortedEntity(ob, true))
  println(getVertexs(edgeArr, false,null).mkString(" "))


  def appendString(str: String, arrBuf: ArrayBuffer[String]): String = {
    var iter = arrBuf.iterator
    var rs = ""
    while(iter.hasNext){
      rs = rs + str + iter.next() + ";\n"
    }
    rs
  }

  def getVertexs(jsonArr: JSONArray, isVertex: Boolean, labelMark: String): ArrayBuffer[String] = {

    val v = ArrayBuffer[String]()
    if (jsonArr != null && jsonArr.length() != 0) {
      var len = jsonArr.length()
      while (len > 0) {
        len -= 1

        var indexs = Try(jsonArr.getJSONObject(len).getJSONArray(GraphSchemaConst.INDEX_SCHEMAS)).getOrElse(null)
        var fields = Try(jsonArr.getJSONObject(len).getJSONArray(GraphSchemaConst.FIELD_SCHEMAS)).getOrElse(null)
        var tmp = if (isVertex) "(" else "["
        //if you need alias
        //tmp = if (isVertex) tmp + GraphSchemaConst.VERTEX + len else tmp + GraphSchemaConst.EDGE + len
        var label = Try(jsonArr.getJSONObject(len).getString(GraphSchemaConst.LABEL_VALUE)).getOrElse(null)
        if (label != null && label.length > 0) {
          tmp = tmp + ":" + label + " "
        }

        var indexArr = Array[String]()
        if (indexs != null && indexs.length() > 0) {
          var len2 = indexs.length()
          var arr = new Array[String](len2)
          while (len2 > 0) {
            len2 -= 1
            arr(len2) = indexs.getString(len2)
          }
          indexArr = arr
        }
        if (fields != null && fields.length() > 0) {
          tmp = tmp + "{"
          var len1 = fields.length()
          var fieldOb = new JSONObject()
          var fieldName = ""
          var fieldType = ""
          var fieldExtraAttr = new JSONObject()
          while (len1 > 0) {
            len1 -= 1
            fieldName = fields.getJSONObject(len1).getString(GraphSchemaConst.FIELD_NAME)
            fieldType = fields.getJSONObject(len1).getString(GraphSchemaConst.FIELD_TYPE)
            //todo no support inner properties
            if (!fieldName.startsWith(GraphSchemaConst.CONF_AFFIX)) {
              if (indexArr.contains(fieldName)) {
                // todo no support index type
                tmp = tmp + fieldName + " " + fieldType.toLowerCase + " index:DEFAULT,"
              } else {
                tmp = tmp + fieldName + " " + fieldType.toLowerCase
                // array type not support index
                if (GraphSchemaConst.CONF_ARRAY.equals(fieldType)){
                  fieldExtraAttr = Try(fields.getJSONObject(len1).getJSONObject(GraphSchemaConst.FIELD_EXTRA_ATTR)).getOrElse(null)
                  tmp = tmp + " <" + Try(fieldExtraAttr.getString(GraphSchemaConst.FIELD_TYPE)).getOrElse("").toLowerCase + ">"
                }
                tmp = tmp + ","
              }
            }
          }

          tmp = if (tmp.endsWith(",")) tmp.substring(0, tmp.size - 1) + "}" else tmp.substring(0, tmp.size - 1)
        }
        tmp = if (isVertex) tmp + ")" else tmp + "]"
        if (labelMark == null) {
          v += tmp
        } else {
          if (labelMark.substring(1, labelMark.length - 1).equals(label)) v += tmp
        }
      }

    }
    v
  }


  def getSortedEntity(schema: JSONObject, isVertex: Boolean): String = {
    var jsonArr = new JSONArray()
      if (isVertex) {
        jsonArr = Try(schema.getJSONArray(GraphSchemaConst.VERTEX_TABLES)).getOrElse(new JSONArray())
      } else {
        jsonArr = Try(schema.getJSONArray(GraphSchemaConst.EDGE_TABLES)).getOrElse(new JSONArray())
      }

    val labelTypeMap = new mutable.HashMap[String, mutable.HashMap[String,String]]()
    val fieldTypeMap = new mutable.HashMap[ String,String]()
    val fieldsMap = new mutable.HashMap[String, String]()
    var tmpMap = new mutable.HashMap[String, String]()


    if (jsonArr != null && jsonArr.length() != 0) {
      var len = jsonArr.length()
      //label level has much indexSet and fieldSet
      while (len > 0) {
        len -= 1

        var fields = Try(jsonArr.getJSONObject(len).getJSONArray(GraphSchemaConst.FIELD_SCHEMAS)).getOrElse(null)
        var label = Try(jsonArr.getJSONObject(len).getString(GraphSchemaConst.LABEL_VALUE)).getOrElse(null)

        if (fields != null && fields.length() > 0) {
          var len1 = fields.length()
          var fieldOb = new JSONObject()
          var fieldName = ""
          var fieldType = ""
          fieldsMap.clear()
          //field level has much fields
          while (len1 > 0) {
            len1 -= 1
            fieldName = fields.getJSONObject(len1).getString(GraphSchemaConst.FIELD_NAME)
            fieldType = fields.getJSONObject(len1).getString(GraphSchemaConst.FIELD_TYPE)
            //todo no support inner properties
//            if (!fieldName.startsWith(GraphSchemaConst.CONF_AFFIX)) {
              //add privimte
                if (!GraphSchemaConst.CONF_ARRAY.equals(fieldType)){
                  fieldsMap.put(fieldName, fieldType.toLowerCase)
                }
//            }
          }
            fieldsMap.foreach(x => {
              var tmp = fieldTypeMap.get(x._1)
              if (!tmp.isEmpty){
                if (tmp.get.equals(x._2) || tmp.get.equals("Dynamic")){
                  //do nothing
                }else{
                  fieldTypeMap.put(x._1,"Dynamic")
                }
              }else{
                fieldTypeMap.put(x._1,x._2)
              }
            })
            labelTypeMap.put(label, new mutable.HashMap[String, String]()++fieldsMap)
        }
      }
    }
    labelTypeMap.mkString(",")


  }


  def getConfs(ob:JSONObject): String ={
    def addSign(s:String)= "'" + s + "'"
    var tabVersion = Try(ob.getInt(GraphSchemaConst.CONF_VERSION)).getOrElse(10):Int
    val c = scala.collection.mutable.ArrayBuffer[String]()
    var value = ""

    {
      value = Try(ob.getString(GraphSchemaConst.CONF_SHARD_NUMBER)).getOrElse(null)
      if (value != null) c += (addSign(GraphSchemaConst.CONF_SHARD_NUMBER) + ":" + value)

      value = Try(ob.getString(GraphSchemaConst.CONF_GRAPH_NAME)).getOrElse(null)
      if (value != null) c += (addSign(GraphSchemaConst.CONF_GRAPH_NAME) + ":" + addSign(value))
    }

    if (tabVersion >= 5) {
      value = Try(ob.getString(GraphSchemaConst.CONF_REPLICATION_NUMBER)).getOrElse(null)
      if (value != null) c += (addSign(GraphSchemaConst.CONF_REPLICATION_NUMBER) + ":" + value)

      value = Try(ob.getString(GraphSchemaConst.CONF_INDEX_TYPE)).getOrElse(null)
      if (value != null) c += (addSign(GraphSchemaConst.CONF_INDEX_TYPE) + ":" + addSign(value))
    }

    if (tabVersion >= 8) {
      value = Try(ob.getString(GraphSchemaConst.CONF_ENCRYPTION_CIPHER)).getOrElse(null)
      if (value != null) c += (addSign(GraphSchemaConst.CONF_ENCRYPTION_CIPHER) + ":" + addSign(value))

      value = Try(ob.getString(GraphSchemaConst.CONF_INDEX_TYPE)).getOrElse(null)
      if (value != null) c += (addSign(GraphSchemaConst.CONF_INDEX_TYPE) + ":" + addSign(value))
    }

    c.mkString("{",",","}")
  }

  object GraphSchemaConst {
    val FIELD_NAME = "field.name"
    val FIELD_TYPE = "field.type"
    val FIELD_EXTRA_ATTR = "field.extra.attr"
    val FIELD_IS_INDEX = "field.isIndex"
    val LABEL_VALUE = "label.value"
    val FIELD_SCHEMAS = "field.schemas"
    val INDEX_SCHEMAS = "index.schemas"
    val VERTEX_TABLES = "vertex.tables"
    val EDGE_TABLES = "edge.tables"
    val GRAPH_NAME = "graph.name"
    val SCHEMA = "schema"
    val GRAPH = "graph"
    val VERTEX = "vertex"
    val EDGE = "edge"
    val DETELE = "delete"
    val DROP = "drop"
    val ALTER = "alter"
    val WITH = "with"
    val ALTER_GRAPH_SCHEMA = "alter_graph_schema"
    val DELETE_LABEL = "delete_label"
    val FROM_GRAPH = "from.graph"
    val PROP_INDEX = "prop_index"
    val NODE_PROP_RESERVE = "node_prop_reserve"
    val REL_PROP_RESERVE = "rel_prop_reserve"
    val SPACE = " "
    val CONF_AFFIX = "__"
    val CONF_TAGS = "__tags"
    val CONF_VERSION = "__VERSION"
    val CONF_GRAPH_NAME = "graph.name"
    val CONF_SHARD_NUMBER = "graph.shard.number"
    //support after graphschemav5
    val CONF_REPLICATION_NUMBER = "graph.replication.number"
    val CONF_INDEX_TYPE =  "graph.index.type"
    //support after graphschemav8
    val CONF_ENCRYPTION_CIPHER = "graph.encryption.type"
    val CONF_INDEX_SEPARATED = "graph.index.separated"
    val CONF_ARRAY = "ARRAY"


  }

}
