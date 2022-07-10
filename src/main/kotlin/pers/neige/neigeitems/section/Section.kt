package pers.neige.neigeitems.section

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import pers.neige.neigeitems.manager.SectionManager
import pers.neige.neigeitems.utils.ConfigUtils.toMap

class Section(configSection: ConfigurationSection) {
    val id = configSection.name
    val type = configSection.getString("type")
    val data: HashMap<String, Any> = configSection.toMap()

    /**
     * 将节点解析值存入缓存
     * @param cache 解析值缓存
     * @param player 待解析玩家
     * @param sections 节点池
     * @return 解析值
     */
    fun load(cache: HashMap<String, String>?, player: OfflinePlayer?, sections: ConfigurationSection?): String? {
        SectionManager.sectionParsers[type]?.onRequest(data, cache, player, sections)?.let {
            cache?.put(id, it)
            return it
        }
        return null
    }

    /**
     * 获取节点解析值
     * @param cache 解析值缓存
     * @param player 待解析玩家
     * @param sections 节点池
     * @return 解析值
     */
    fun get(cache: HashMap<String, String>?, player: OfflinePlayer?, sections: ConfigurationSection?): String? {
        return SectionManager.sectionParsers[type]?.onRequest(data, cache, player, sections)
    }
}