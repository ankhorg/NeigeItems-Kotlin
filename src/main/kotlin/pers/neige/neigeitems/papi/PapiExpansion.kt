package pers.neige.neigeitems.papi

import com.alibaba.fastjson2.parseObject
import org.bukkit.Material
import org.bukkit.entity.Player
import pers.neige.neigeitems.NeigeItems
import pers.neige.neigeitems.annotation.Awake
import pers.neige.neigeitems.hook.placeholderapi.PlaceholderExpansion
import pers.neige.neigeitems.manager.HookerManager
import pers.neige.neigeitems.manager.HookerManager.papiHooker
import pers.neige.neigeitems.utils.ItemUtils.getNbt
import pers.neige.neigeitems.utils.ItemUtils.isNiItem
import pers.neige.neigeitems.utils.SectionUtils.parseSection
import java.io.InputStreamReader
import java.util.concurrent.ConcurrentHashMap
import javax.script.CompiledScript
import javax.script.SimpleBindings

/**
 * ni papi扩展
 */
object PapiExpansion {
    private var expansion: PlaceholderExpansion? = null
    private val engine = HookerManager.nashornHooker.getGlobalEngine().also { engine ->
        NeigeItems.getInstance().getResource("JavaScriptLib/lib.js")?.use { input ->
            InputStreamReader(input, "UTF-8").use { reader ->
                engine.eval(reader)
            }
        }
    }
    private val scripts = ConcurrentHashMap<String, CompiledScript>()

    @JvmStatic
    @Awake(lifeCycle = Awake.LifeCycle.ENABLE)
    private fun init() {
        expansion = papiHooker?.newPlaceholderExpansion("ni", "Neige", "1.0.0") { player, param ->
            val params = param.split("_", limit = 2)
            when (val key = params[0].lowercase()) {
                "parse" -> return@newPlaceholderExpansion params.getOrNull(1)?.parseSection(player) ?: ""
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
                                } ?: return@newPlaceholderExpansion ""
                                if (itemStack.type == Material.AIR) return@newPlaceholderExpansion ""
                                if (key == "data") {
                                    val itemInfo = itemStack.isNiItem(true) ?: return@newPlaceholderExpansion ""
                                    val data: HashMap<String, String> = itemInfo.data
                                    when (type) {
                                        "get" -> return@newPlaceholderExpansion data[content] ?: ""
                                        "has" -> return@newPlaceholderExpansion data.containsKey(content).toString()
                                        "check" -> {
                                            content.parseObject<java.util.HashMap<String, String?>>()
                                                .forEach { (key, value) ->
                                                    if (value == null) {
                                                        if (data.containsKey(key)) return@newPlaceholderExpansion "false"
                                                    } else {
                                                        if (data[key] != value) return@newPlaceholderExpansion "false"
                                                    }
                                                }
                                            return@newPlaceholderExpansion "true"
                                        }

                                        "eval" -> {
                                            val bindings = SimpleBindings()
                                            bindings["player"] = player
                                            bindings["itemStack"] = itemStack
                                            bindings["id"] = itemInfo.id
                                            bindings["itemTag"] = itemInfo.itemTag
                                            bindings["data"] = data
                                            return@newPlaceholderExpansion scripts.computeIfAbsent(content) {
                                                HookerManager.nashornHooker.compile(engine, content)
                                            }.eval(bindings)?.toString() ?: ""
                                        }
                                    }
                                } else {
                                    val itemTag = itemStack.getNbt()
                                    when (type) {
                                        "get" -> return@newPlaceholderExpansion itemTag.getDeepStringOrNull(content)
                                            ?: ""

                                        "has" -> return@newPlaceholderExpansion (itemTag.getDeep(content) != null).toString()
                                        "check" -> {
                                            content.parseObject<java.util.HashMap<String, String?>>()
                                                .forEach { (key, value) ->
                                                    if (value == null) {
                                                        if (itemTag.getDeep(key) == null) return@newPlaceholderExpansion "false"
                                                    } else {
                                                        if (itemTag.getDeepStringOrNull(key) != value) return@newPlaceholderExpansion "false"
                                                    }
                                                }
                                            return@newPlaceholderExpansion "true"
                                        }

                                        "eval" -> {
                                            val bindings = SimpleBindings()
                                            bindings["player"] = player
                                            bindings["itemStack"] = itemStack
                                            bindings["itemTag"] = itemTag
                                            return@newPlaceholderExpansion scripts.computeIfAbsent(content) {
                                                HookerManager.nashornHooker.compile(engine, content)
                                            }.eval(bindings)?.toString() ?: ""
                                        }
                                    }
                                }
                            }
                        }
                    }
                    return@newPlaceholderExpansion ""
                }

                "amount" -> {
                    if (player !is Player) return@newPlaceholderExpansion "0"
                    val itemId = params.getOrNull(1) ?: return@newPlaceholderExpansion "0"
                    var result = 0
                    val contents = player.inventory.contents
                    for (itemStack in contents) {
                        val itemInfo = itemStack.isNiItem()
                        if (itemInfo != null && itemInfo.id == itemId) {
                            result += itemStack.amount
                        }
                    }
                    return@newPlaceholderExpansion result.toString()
                }

                else -> return@newPlaceholderExpansion ""
            }
        }
        expansion?.register()
    }
}