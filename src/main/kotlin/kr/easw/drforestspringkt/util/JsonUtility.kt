package kr.easw.drforestspringkt.util

import org.json.simple.JSONArray
import org.json.simple.JSONObject


fun constructJson(vararg data: Pair<String, Any>): String {
    val json = JSONObject()
    for ((x, y) in data) {
        json[x] = y
    }
    return json.toJSONString().replace("\\/", "/")
}

fun constructJsonObject(vararg data: Pair<String, Any>): JSONObject {
    val json = JSONObject()
    for ((x, y) in data) {
        json[x] = y
    }
    return json
}

fun constructJsonArray(vararg data: Any): String {
    val json = JSONArray()
    for (x in data) {
        json.add(x)
    }
    return json.toJSONString()
}