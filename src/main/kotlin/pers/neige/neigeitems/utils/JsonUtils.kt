package pers.neige.neigeitems.utils

import com.alibaba.fastjson2.parseObject
import com.alibaba.fastjson2.toJSONString

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
     * Map<String, String> 转 String
     *
     * @return 转换结果
     */
    @JvmStatic
    fun toString(map: Map<String, String>): String {
        return map.toJSONString()
    }
}