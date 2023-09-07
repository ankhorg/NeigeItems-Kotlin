package pers.neige.neigeitems.section.impl

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import pers.neige.neigeitems.manager.HookerManager.papi
import pers.neige.neigeitems.manager.ScriptManager
import pers.neige.neigeitems.section.SectionParser
import pers.neige.neigeitems.utils.SectionUtils.parseSection

/**
 * js节点解析器
 */
object JavascriptParser : SectionParser() {
    override val id: String = "js"

    override fun onRequest(
        data: ConfigurationSection,
        cache: MutableMap<String, String>?,
        player: OfflinePlayer?,
        sections: ConfigurationSection?
    ): String? {
        return handler(
            cache,
            player,
            sections,
            true,
            data.getString("path"),
            data.getStringList("args")
        )
    }

    override fun onRequest(
        args: List<String>,
        cache: MutableMap<String, String>?,
        player: OfflinePlayer?,
        sections: ConfigurationSection?
    ): String {
        return handler(
            cache,
            player,
            sections,
            false,
            args.getOrNull(0),
            if (args.isNotEmpty()) args.drop(1) else mutableListOf()
        ) ?: "<$id::${args.joinToString("_")}>"
    }


    /**
     * @param cache 解析值缓存
     * @param player 待解析玩家
     * @param sections 节点池
     * @param parse 是否对参数进行节点解析
     * @param info 脚本文件名::函数名
     * @param args 函数参数
     * @return 解析值
     */
    private fun handler(
        cache: MutableMap<String, String>?,
        player: OfflinePlayer?,
        sections: ConfigurationSection?,
        parse: Boolean,
        info: String?,
        args: List<String>
    ): String? {
        info?.let {
            val array = info.split("::")
            // 脚本文件名
            val path = array[0]
            // 函数名
            val func = array[1]
            val map = HashMap<String, Any>()
            player?.let {
                map["player"] = player
                map["papi"] = java.util.function.Function<String, String> { string -> papi(player, string) }
            }
            map["vars"] = java.util.function.Function<String, String> { string -> string.parseSection(cache, player, sections) }
            return ScriptManager.compiledScripts[path]?.invoke(func, map, *args.toTypedArray())?.toString()?.parseSection(parse, cache, player, sections)
        }
        return null
    }
}