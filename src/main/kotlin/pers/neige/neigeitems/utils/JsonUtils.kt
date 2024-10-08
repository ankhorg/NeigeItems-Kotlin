package pers.neige.neigeitems.utils

import com.alibaba.fastjson2.parseObject
import com.alibaba.fastjson2.toJSONString

/**
 * JSON相关工具类
 */
object JsonUtils {
    /**
     * String 转 HashMap<String, String>
     *
     * @return 转换结果
     */
    @JvmStatic
    fun toMap(string: String): HashMap<String, String> {
        return string.parseObject<HashMap<String, String>>()
    }

    /**
     * String 转 HashMap<String, Int>
     *
     * @return 转换结果
     */
    @JvmStatic
    fun toStringIntMap(string: String): HashMap<String, Int> {
        return string.parseObject<HashMap<String, Int>>()
    }

    /**
     * Map<*, *> 转 String
     *
     * @return 转换结果
     */
    @JvmStatic
    fun toString(map: Map<*, *>): String {
        return map.toJSONString()
    }
}