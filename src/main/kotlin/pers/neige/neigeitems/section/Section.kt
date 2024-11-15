package pers.neige.neigeitems.section

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import org.slf4j.LoggerFactory
import pers.neige.neigeitems.manager.SectionManager

/**
 * 用于对节点的ConfigurationSection进行包装, 方便地获取或缓存解析值
 * configSection.name只能获得末级ID, 难以解决形似a.b.c的多级调用
 * 因此需要特别指定ID
 *
 * @param configSection 节点配置
 * @property id 节点ID
 * @constructor 编译js脚本并进行包装
 */
class Section(configSection: ConfigurationSection, val id: String = configSection.name) {
    private companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(Section::class.java.simpleName)
    }

    /**
     * 获取节点类型
     */
    val type = configSection.getString("type")

    /**
     * 获取节点内容
     */
    val data: ConfigurationSection = configSection

    /**
     * 获取节点解析值
     *
     * @param cache 解析值缓存
     * @param player 待解析玩家
     * @param sections 节点池
     * @return 解析值
     */
    fun get(
        cache: MutableMap<String, String>? = null,
        player: OfflinePlayer? = null,
        sections: ConfigurationSection? = null
    ): String? {
        // 空类型(可能是多层节点)
        type?.let {
            // 空解析器
            SectionManager.sectionParsers[type]?.let { parser ->
                // 空返回值(只有解析出错才允许返回空值)
                parser.onRequest(data, cache, player, sections)?.let {
                    return it
                }
                logger.info("{} 节点 {} 无法获取解析值", type, id)
                return null
            }
            logger.info("{} 拥有未知节点类型 {}", id, type)
        }
        return null
    }

    /**
     * 获取节点解析值并存入缓存
     *
     * @param cache 解析值缓存
     * @param player 待解析玩家
     * @param sections 节点池
     * @return 解析值
     */
    fun load(
        cache: MutableMap<String, String>? = null,
        player: OfflinePlayer? = null,
        sections: ConfigurationSection? = null
    ): String? {
        val result = get(cache, player, sections)
        result?.let { cache?.put(id, it) }
        return result
    }
}