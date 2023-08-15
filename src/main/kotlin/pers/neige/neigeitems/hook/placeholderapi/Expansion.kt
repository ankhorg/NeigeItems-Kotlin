package pers.neige.neigeitems.hook.placeholderapi

import com.alibaba.fastjson2.parseObject
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import pers.neige.neigeitems.NeigeItems
import pers.neige.neigeitems.manager.HookerManager
import pers.neige.neigeitems.utils.ItemUtils.getDeepOrNull
import pers.neige.neigeitems.utils.ItemUtils.getNbt
import pers.neige.neigeitems.utils.ItemUtils.isNiItem
import pers.neige.neigeitems.utils.ItemUtils.toValue
import pers.neige.neigeitems.utils.SectionUtils.parseSection
import taboolib.module.nms.getItemTag
import taboolib.platform.compat.PlaceholderExpansion
import java.io.InputStreamReader
import java.util.concurrent.ConcurrentHashMap
import javax.script.CompiledScript
import javax.script.SimpleBindings

/**
 * PlaceholderAPI扩展
 */
object Expansion : PlaceholderExpansion {
    val engine = HookerManager.nashornHooker.getGlobalEngine().also { engine ->
        NeigeItems.plugin.getResource("JavaScriptLib/lib.js")?.use { input ->
            InputStreamReader(input, "UTF-8").use { reader ->
                engine.eval(reader)
            }
        }
    }

    val scripts = ConcurrentHashMap<String, CompiledScript>()

    override val identifier = "ni"

    override fun onPlaceholderRequest(player: OfflinePlayer?, args: String): String {
        val params = args.split("_", limit = 2)
        when (val key = params[0].lowercase()) {
            "parse" -> return params.getOrNull(1)?.parseSection(player) ?: ""
            "data", "nbt" -> {
                // 玩家在线且当前PAPI变量输入了参数
                if (player != null && player is Player && params.size == 2) {
                    // %ni_data/nbt_type_slot_content%
                    params[1].split("_", limit = 3).let {
                        if (it.size == 3) {
                            val type = it[0].lowercase()
                            val slot = it[1].lowercase()
                            val content = it[2]
                            val inventory = player.inventory
                            // 获取待检测物品
                            val itemStack = when (slot) {
                                "hand" -> inventory.itemInMainHand
                                "offhand" -> inventory.getItem(40)
                                "head" -> inventory.getItem(39)
                                "chest" -> inventory.getItem(38)
                                "legs" -> inventory.getItem(37)
                                "feet" -> inventory.getItem(36)
                                else -> slot.toIntOrNull()?.let { index -> inventory.getItem(index) }
                            } ?: return ""
                            if (itemStack.type == Material.AIR) return ""
                            if (key == "data") {
                                val itemInfo = itemStack.isNiItem(true) ?: return ""
                                val data: HashMap<String, String> = itemInfo.data!!
                                when (type) {
                                    "get" -> return data[content] ?: ""
                                    "has" -> return data.containsKey(content).toString()
                                    "check" -> {
                                        content.parseObject<java.util.HashMap<String, String?>>().forEach { (key, value) ->
                                            if (value == null) {
                                                if (data.containsKey(key)) return "false"
                                            } else {
                                                if (data[key] != value) return "false"
                                            }
                                        }
                                        return "true"
                                    }
                                    "eval" -> {
                                        val bindings = SimpleBindings()
                                        bindings["player"] = player
                                        bindings["itemStack"] = itemStack
                                        bindings["id"] = itemInfo.id
                                        bindings["itemTag"] = itemInfo.itemTag
                                        bindings["data"] = data
                                        return scripts.computeIfAbsent(content) {
                                            HookerManager.nashornHooker.compile(engine, content)
                                        }.eval(bindings)?.toString() ?: ""
                                    }
                                }
                            } else {
                                val itemTag = itemStack.getNbt()
                                when (type) {
                                    "get" -> return itemTag.getDeep(content)?.toValue()?.toString() ?: ""
                                    "has" -> return (itemTag.getDeep(content) != null).toString()
                                    "check" -> {
                                        content.parseObject<java.util.HashMap<String, String?>>().forEach { (key, value) ->
                                            if (value == null) {
                                                if (itemTag.getDeep(key) == null) return "false"
                                            } else {
                                                if (itemTag.getDeep(key)?.toValue()?.toString() != value) return "false"
                                            }
                                        }
                                        return "true"
                                    }
                                    "eval" -> {
                                        val bindings = SimpleBindings()
                                        bindings["player"] = player
                                        bindings["itemStack"] = itemStack
                                        bindings["itemTag"] = itemTag
                                        return scripts.computeIfAbsent(content) {
                                            HookerManager.nashornHooker.compile(engine, content)
                                        }.eval(bindings)?.toString() ?: ""
                                    }
                                }
                            }
                        }
                    }
                }
                return ""
            }
            else -> return ""
        }
    }
}