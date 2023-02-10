package pers.neige.neigeitems.utils

import com.alibaba.fastjson2.parseObject
import com.alibaba.fastjson2.toJSONString
import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency

@RuntimeDependencies(
    RuntimeDependency(
        "!com.alibaba.fastjson2:fastjson2-kotlin:2.0.9",
        test = "!com.alibaba.fastjson2.filter.Filter"
    )
)
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