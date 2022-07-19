package pers.neige.neigeitems.manager

import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.NeigeItems.bukkitScheduler
import pers.neige.neigeitems.NeigeItems.plugin
import pers.neige.neigeitems.item.ItemAction
import pers.neige.neigeitems.item.ItemConfig
import pers.neige.neigeitems.manager.ConfigManager.config
import pers.neige.neigeitems.manager.HookerManager.papi
import pers.neige.neigeitems.manager.HookerManager.papiColor
import pers.neige.neigeitems.manager.HookerManager.papiHooker
import pers.neige.neigeitems.manager.HookerManager.vaultHooker
import pers.neige.neigeitems.utils.ActionUtils.consume
import pers.neige.neigeitems.utils.ActionUtils.isCoolDown
import pers.neige.neigeitems.utils.ConfigUtils
import pers.neige.neigeitems.utils.ItemUtils.isNiItem
import pers.neige.neigeitems.utils.PlayerUtils.getMetadataEZ
import pers.neige.neigeitems.utils.PlayerUtils.setMetadataEZ
import pers.neige.neigeitems.utils.SectionUtils.parseItemSection
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.nms.ItemTag
import taboolib.module.nms.ItemTagData
import taboolib.module.nms.getItemTag
import taboolib.platform.util.actionBar
import taboolib.platform.util.giveItem
import java.io.File
import java.text.DecimalFormat
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.ConcurrentHashMap
import java.util.function.BiFunction

object ActionManager {
    val itemActions: ConcurrentHashMap<String, ItemAction> = ConcurrentHashMap<String, ItemAction>()

    val actions = HashMap<String, BiFunction<Player, String, Boolean>>()

    init {
        loadItemActions()
        loadBasicActions()
    }

    fun reload() {
        itemActions.clear()
        loadItemActions()
    }

    fun runAction(player: Player, action: List<String>, itemTag: ItemTag? = null) {
        for (value in action) {
            if (!runAction(player, value, itemTag)) break
        }
    }

    fun runAction(player: Player, action: String, itemTag: ItemTag? = null): Boolean {
        val actionString = when (itemTag) {
            null -> action
            else -> action.parseItemSection(itemTag, player).replace("\\<", "<").replace("\\>", ">")
        }
        var actionType = actionString.lowercase(Locale.getDefault())
        var actionContent = ""
        val index = actionString.indexOf(": ")
        if (index != -1) {
            actionType = actionString.substring(0, index).lowercase(Locale.getDefault())
            actionContent = actionString.substring(index+2, actionString.length)
        }
        actions[actionType]?.let {
            val actionFunction: BiFunction<Player, String, Boolean> = it
            return actionFunction.apply(player, actionContent)
        }
        return true
    }

    private fun loadItemActions() {
        for (file: File in ConfigUtils.getAllFiles("ItemActions")) {
            val config = YamlConfiguration.loadConfiguration(file)
            config.getKeys(false).forEach { id ->
                config.getConfigurationSection(id)?.let {
                    itemActions[id] = ItemAction(id, it)
                }
            }
        }
    }

    fun addAction(id: String, function: BiFunction<Player, String, Boolean>) {
        actions[id.lowercase(Locale.getDefault())] = function
    }

    private fun loadBasicActions() {
        // 向玩家发送消息
        addAction("tell", BiFunction<Player, String, Boolean> { player, string ->
            player.sendMessage(papiColor(player, string))
            true
        })
        // 向玩家发送消息(不将&解析为颜色符号)
        addAction("tellNoColor", BiFunction<Player, String, Boolean> { player, string ->
            player.sendMessage(papi(player, string))
            true
        })
        // 强制玩家发送消息
        addAction("chat", BiFunction<Player, String, Boolean> { player, string ->
            player.chat(papi(player, string))
            true
        })
        // 强制玩家发送消息(将&解析为颜色符号)
        addAction("chatWithColor", BiFunction<Player, String, Boolean> { player, string ->
            player.chat(papiColor(player, string))
            true
        })
        // 强制玩家执行指令
        addAction("command", BiFunction<Player, String, Boolean> { player, string ->
            bukkitScheduler.callSyncMethod(plugin, Callable {
                Bukkit.dispatchCommand(player, papiColor(player, string))
            })
            true
        })
        // 强制玩家执行指令
        actions["command"]?.let { addAction("player", it) }
        // 强制玩家执行指令(不将&解析为颜色符号)
        addAction("commandNoColor", BiFunction<Player, String, Boolean> { player, string ->
            bukkitScheduler.callSyncMethod(plugin, Callable {
                Bukkit.dispatchCommand(player, papi(player, string))
            })
            true
        })
        // 后台执行指令
        addAction("console", BiFunction<Player, String, Boolean> { player, string ->
            bukkitScheduler.callSyncMethod(plugin, Callable {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), papiColor(player, string))
            })
            true
        })
        // 后台执行指令(不将&解析为颜色符号)
        addAction("consoleNoColor", BiFunction<Player, String, Boolean> { player, string ->
            bukkitScheduler.callSyncMethod(plugin, Callable {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), papi(player, string))
            })
            true
        })
        // 给予玩家金钱
        addAction("giveMoney", BiFunction<Player, String, Boolean> { player, string ->
            vaultHooker?.giveMoney(player, papi(player, string).toDoubleOrNull() ?: 0.toDouble())
            true
        })
        // 扣除玩家金钱
        addAction("takeMoney", BiFunction<Player, String, Boolean> { player, string ->
            vaultHooker?.takeMoney(player, papi(player, string).toDoubleOrNull() ?: 0.toDouble())
            true
        })
        // 给予玩家经验
        addAction("giveExp", BiFunction<Player, String, Boolean> { player, string ->
            player.giveExp(papi(player, string).toIntOrNull() ?: 0)
            true
        })
        // 扣除玩家经验
        addAction("takeExp", BiFunction<Player, String, Boolean> { player, string ->
            player.giveExp((papi(player, string).toIntOrNull() ?: 0)*-1)
            true
        })
        // 设置玩家经验
        addAction("setExp", BiFunction<Player, String, Boolean> { player, string ->
            player.exp = papi(player, string).toFloatOrNull() ?: 0.toFloat()
            true
        })
        // 给予玩家经验等级
        addAction("giveLevel", BiFunction<Player, String, Boolean> { player, string ->
            player.giveExpLevels(papi(player, string).toIntOrNull() ?: 0)
            true
        })
        // 扣除玩家经验等级
        addAction("takeLevel", BiFunction<Player, String, Boolean> { player, string ->
            player.giveExpLevels((papi(player, string).toIntOrNull() ?: 0)*-1)
            true
        })
        // 设置玩家经验等级
        addAction("setLevel", BiFunction<Player, String, Boolean> { player, string ->
            player.level = papi(player, string).toIntOrNull() ?: 0
            true
        })
        // 给予玩家饱食度
        addAction("giveFood", BiFunction<Player, String, Boolean> { player, string ->
            player.foodLevel = player.foodLevel + (papi(player, string).toIntOrNull() ?: 0)
            true
        })
        // 扣除玩家饱食度
        addAction("takeFood", BiFunction<Player, String, Boolean> { player, string ->
            player.foodLevel = player.foodLevel - (papi(player, string).toIntOrNull() ?: 0)
            true
        })
        // 设置玩家饱食度
        addAction("setFood", BiFunction<Player, String, Boolean> { player, string ->
            player.foodLevel = papi(player, string).toIntOrNull() ?: 0
            true
        })
        // 给予玩家生命
        addAction("giveHealth", BiFunction<Player, String, Boolean> { player, string ->
            player.health = (player.health + (papi(player, string).toDoubleOrNull() ?: 0.toDouble())).coerceAtMost(player.maxHealth)
            true
        })
        // 扣除玩家生命
        addAction("takeHealth", BiFunction<Player, String, Boolean> { player, string ->
            player.health = (player.health - (papi(player, string).toDoubleOrNull() ?: 0.toDouble())).coerceAtLeast(0.toDouble())
            true
        })
        // 设置玩家生命
        addAction("setHealth", BiFunction<Player, String, Boolean> { player, string ->
            player.health = (papi(player, string).toDoubleOrNull() ?: 0.toDouble()).coerceAtLeast(0.toDouble()).coerceAtMost(player.maxHealth)
            true
        })
        // 延迟(单位是tick)
        addAction("delay", BiFunction<Player, String, Boolean> { player, string ->
            Thread.sleep((papi(player, string).toLongOrNull() ?: 0)*50)
            true
        })
        // 终止
        addAction("return", BiFunction<Player, String, Boolean> { player, string ->
            false
        })
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun listener(event: PlayerInteractEvent) {
        // 获取玩家
        val player = event.player
        // 获取操作物品
        val itemStack = event.item
        // 物品NBT
        val itemTag: ItemTag
        // NI物品数据
        val neigeItems: ItemTag
        // NI物品id
        val id: String
        when (val itemInfo = itemStack?.isNiItem()) {
            null -> return
            else -> {
                itemTag = itemInfo.itemTag
                neigeItems = itemInfo.neigeItems
                id = itemInfo.id
            }
        }
        // 获取物品动作
        val itemAction = itemActions[id] ?: let { return }
        if (itemAction.left == null && itemAction.right == null && itemAction.all == null) return
        // 获取物品消耗信息
        val consume =  itemAction.consume
        // 获取交互类型
        val action = event.action
        val leftAction = (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK)
        val rightAction = (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)
        // 如果物品配置了消耗事件
        if (consume != null) {
            // 获取左键是否消耗
            val left = (leftAction && consume.getBoolean("left", false))
            // 获取右键是否消耗
            val right = (rightAction && consume.getBoolean("right", false))
            // 获取左右键是否消耗
            val all = ((leftAction || rightAction) && consume.getBoolean("all", false))
            // 如果该物品需要被消耗
            if (left || right || all) {
                // 取消交互事件
                event.isCancelled = true
                // 检测冷却
                if (consume.isCoolDown(player, id)) return
                // 获取待消耗数量
                val amount: Int = consume.getInt("amount", 1)
                // 消耗物品
                if (itemStack.consume(player, amount, itemTag, neigeItems)) {
                    // 消耗成功, 执行动作
                    bukkitScheduler.runTaskAsynchronously(plugin, Runnable {
                        itemAction.run(player, itemAction.all, itemTag)
                        if (left) itemAction.run(player, itemAction.left, itemTag)
                        if (right) itemAction.run(player, itemAction.right, itemTag)
                    })
                }
            }
        } else {
            // 取消交互事件
            event.isCancelled = true
            bukkitScheduler.runTaskAsynchronously(plugin, Runnable {
                // 检测冷却
                if (itemAction.isCoolDown(player)) return@Runnable
                // 执行动作
                itemAction.run(player, itemAction.all, itemTag)
                if (leftAction) itemAction.run(player, itemAction.left, itemTag)
                if (rightAction) itemAction.run(player, itemAction.right, itemTag)
            })
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun listener(event: PlayerItemConsumeEvent) {
        // 获取玩家
        val player = event.player
        // 获取手持物品
        val itemStack = event.item
        // 物品NBT
        val itemTag: ItemTag
        // NI物品数据
        val neigeItems: ItemTag
        // NI物品id
        val id: String
        when (val itemInfo = itemStack.isNiItem()) {
            null -> return
            else -> {
                itemTag = itemInfo.itemTag
                neigeItems = itemInfo.neigeItems
                id = itemInfo.id
            }
        }
        // 获取物品动作
        val itemAction = itemActions[id] ?: let { return }
        itemAction.eat ?: let { return }
        // 获取物品消耗信息
        val consume =  itemAction.consume
        // 如果该物品需要被消耗
        if (consume != null && consume.getBoolean("eat", false)) {
            // 取消交互事件
            event.isCancelled = true
            // 检测冷却
            if (consume.isCoolDown(player, id)) return
            // 获取待消耗数量
            val amount: Int = consume.getInt("amount", 1)
            // 消耗物品
            if (itemStack.consume(player, amount, itemTag, neigeItems)) {
                // 设置物品
                if (event.item == player.inventory.itemInMainHand) {
                    player.inventory.setItemInMainHand(itemStack)
                } else {
                    player.inventory.setItemInOffHand(itemStack)
                }
                // 执行动作
                bukkitScheduler.runTaskAsynchronously(plugin, Runnable {
                    itemAction.run(player, itemAction.eat, itemTag)
                })
            }
        } else {
            // 取消交互事件
            event.isCancelled = true
            bukkitScheduler.runTaskAsynchronously(plugin, Runnable {
                // 检测冷却
                if (itemAction.isCoolDown(player)) return@Runnable
                // 执行动作
                itemAction.run(player, itemAction.eat, itemTag)
            })
        }
    }
}