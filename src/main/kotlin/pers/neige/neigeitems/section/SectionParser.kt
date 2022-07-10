package pers.neige.neigeitems.section

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import pers.neige.neigeitems.section.impl.StringsParser
import pers.neige.neigeitems.utils.SectionUtils.parseSection

abstract class SectionParser {
    /**
     * 用于私有节点解析
     * @param data 节点内容
     * @param cache 解析值缓存
     * @param player 待解析玩家
     * @param sections 节点池
     * @return 解析值
     */
    open fun onRequest(data: HashMap<String, *>, cache: HashMap<String, String>?, player: OfflinePlayer?, sections: ConfigurationSection?): String? {
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
    open fun onRequest(args: List<String>, cache: HashMap<String, String>?, player: OfflinePlayer?, sections: ConfigurationSection?): String {
        return ""
    }
}