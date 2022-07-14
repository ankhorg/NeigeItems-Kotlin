package pers.neige.neigeitems.section

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import pers.neige.neigeitems.manager.SectionManager
import pers.neige.neigeitems.utils.ConfigUtils.toMap
import taboolib.common.platform.function.info

class Section(configSection: ConfigurationSection) {
    val id = configSection.name
    val type = configSection.getString("type")
    val data: HashMap<String, Any> = configSection.toMap()

    /**
     * 获取节点解析值
     * @param cache 解析值缓存
     * @param player 待解析玩家
     * @param sections 节点池
     * @return 解析值
     */
    fun get(cache: HashMap<String, String>? = null, player: OfflinePlayer? = null, sections: ConfigurationSection? = null): String? {
        // 空类型(可能是多层节点)
        type?.let {
            // 空解析器
            SectionManager.sectionParsers[type]?.let { parser ->
                // 空返回值(只有解析出错才允许返回空值)
                parser.onRequest(data, cache, player, sections)?.let {
                    return it
                }
                info("$type 节点 $id 无法获取解析值")
            }
            info("$id 拥有未知节点类型 $type")
        }
        return null
    }

    /**
     * 获取节点解析值并存入缓存
     * @param cache 解析值缓存
     * @param player 待解析玩家
     * @param sections 节点池
     * @return 解析值
     */
    fun load(cache: HashMap<String, String>? = null, player: OfflinePlayer? = null, sections: ConfigurationSection? = null): String? {
        val result = get(cache, player, sections)
        result?.let { cache?.put(id, it) }
        return result
    }
}