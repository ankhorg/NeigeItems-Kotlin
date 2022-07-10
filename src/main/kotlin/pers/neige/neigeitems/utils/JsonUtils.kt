package pers.neige.neigeitems.utils

import com.alibaba.fastjson2.JSONObject

object JsonUtils {
    // String 转 JSONObject
    @JvmStatic
    fun String.toJson(): JSONObject {
        return JSONObject.parseObject(this)
    }

    // JSONObject 转 HashMap
    @JvmStatic
    fun JSONObject.toMap(): HashMap<String, String> {
        val map = HashMap<String, String>()
        this.keys.forEach {
            map[it] = this.getString(it)
        }
        return map
    }

    // String 转 HashMap
    @JvmStatic
    fun String.toMap(): HashMap<String, String> {
        return this.toJson().toMap()
    }

    // HashMap 转 JSONObject
    @JvmStatic
    fun HashMap<String, String>.toJson(): JSONObject {
        val jsonObject = JSONObject()
        for ((key, value) in this) {
            jsonObject.put(key, value)
        }
        return jsonObject
    }

    // HashMap 转 String
    @JvmStatic
    fun HashMap<String, String>.asString(): String {
        return this.toJson().toString()
    }
}