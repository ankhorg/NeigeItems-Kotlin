package pers.neige.neigeitems.section

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection

abstract class SectionParser {
    abstract val id: String

    /**
     * 用于私有节点解析
     * @param data 节点内容
     * @param cache 解析值缓存
     * @param player 待解析玩家
     * @param sections 节点池
     * @return 解析值
     */
    open fun onRequest(data: ConfigurationSection, cache: HashMap<String, String>? = null, player: OfflinePlayer? = null, sections: ConfigurationSection? = null): String? {
        return null
    }

    /**
     * 用于即时节点解析
     * @param args 节点参数
     * @param cache 解析值缓存
     * @param player 待解析玩家
     * @param sections 节点池
     * @return 解析值
     */
    open fun onRequest(args: List<String>, cache: HashMap<String, String>? = null, player: OfflinePlayer? = null, sections: ConfigurationSection? = null): String {
        return ""
    }
}