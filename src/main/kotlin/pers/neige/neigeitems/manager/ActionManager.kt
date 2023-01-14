package pers.neige.neigeitems.manager

import org.bukkit.Bukkit
import org.bukkit.Bukkit.isPrimaryThread
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import pers.neige.neigeitems.NeigeItems.bukkitScheduler
import pers.neige.neigeitems.NeigeItems.plugin
import pers.neige.neigeitems.item.ItemAction
import pers.neige.neigeitems.manager.HookerManager.mythicMobsHooker
import pers.neige.neigeitems.manager.HookerManager.papi
import pers.neige.neigeitems.manager.HookerManager.papiColor
import pers.neige.neigeitems.manager.HookerManager.vaultHooker
import pers.neige.neigeitems.utils.ActionUtils.consume
import pers.neige.neigeitems.utils.ActionUtils.consumeAndReturn
import pers.neige.neigeitems.utils.ActionUtils.isCoolDown
import pers.neige.neigeitems.utils.ConfigUtils
import pers.neige.neigeitems.utils.ItemUtils.isNiItem
import pers.neige.neigeitems.utils.SectionUtils.parseItemSection
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.nms.ItemTag
import taboolib.platform.util.giveItem
import java.io.File
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.function.BiFunction

/**
 * 用于管理所有物品动作、所有拥有物品动作的物品及相关动作、监听相关事件做到动作触发
 */
object ActionManager {
    /**
     * 获取拥有动作的物品ID及相关动作
     */
    val itemActions: ConcurrentHashMap<String, ItemAction> = ConcurrentHashMap<String, ItemAction>()

    /**
     * 获取物品动作实现函数
     */
    val actions = HashMap<String, BiFunction<Player, String, Boolean>>()

    init {
        // 加载所有拥有动作的物品及相关动作
        loadItemActions()
        // 加载基础物品动作
        loadBasicActions()
        // 加载自定义动作
        loadCustomActions()
    }

    /**
     * 重载物品动作管理器
     */
    fun reload() {
        itemActions.clear()
        actions.clear()
        loadItemActions()
        loadBasicActions()
        loadCustomActions()
    }

    /**
     * 执行物品动作
     *
     * @param player 执行玩家
     * @param action 动作文本
     * @param itemTag 用于解析nbt及data, 可为空
     */
    fun runAction(player: Player, action: List<String>, itemTag: ItemTag?, start: Int?, end: Int?) {
        // 动作执行延迟
        var delay = 0L

        val actionStart = start?.coerceAtLeast(0)?.coerceAtMost(action.size) ?: 0
        val actionEnd = (end?.coerceAtLeast(0)?.coerceAtMost(action.size) ?: action.size).coerceAtLeast(actionStart)

        // 遍历所有动作
        for (i in actionStart until actionEnd) {
            // 延迟执行
            if (delay > 0) {
                if (isPrimaryThread()) {
                    bukkitScheduler.runTaskLater(plugin, Runnable {
                        runAction(player, action, itemTag, i, actionEnd)
                    }, delay)
                } else {
                    bukkitScheduler.runTaskLaterAsynchronously(plugin, Runnable {
                        runAction(player, action, itemTag, i, actionEnd)
                    }, delay)
                }
                break
            }

            val value = action[i]

            // 解析物品变量
            val actionString = when (itemTag) {
                null -> value
                else -> value.parseItemSection(itemTag, player)
            }
            // 解析动作类型及动作内容
            var actionType = actionString.lowercase(Locale.getDefault())
            var actionContent = ""
            val index = actionString.indexOf(": ")
            if (index != -1) {
                actionType = actionString.substring(0, index).lowercase(Locale.getDefault())
                actionContent = actionString.substring(index+2, actionString.length)
            }

            when {
                // 延迟
                actionType == "delay" -> delay += actionContent.toLongOrNull() ?: 0
                // 正常执行
                else -> if (!runAction(player, actionType, actionContent)) break
            }
        }
    }

    /**
     * 执行物品动作
     *
     * @param player 执行玩家
     * @param action 动作文本
     * @param itemTag 用于解析nbt及data, 可为空
     */
    fun runAction(player: Player, action: List<String>, itemTag: ItemTag?) {
        runAction(player, action, itemTag, null, null)
    }

    /**
     * 执行物品动作
     *
     * @param player 执行玩家
     * @param action 动作文本
     */
    fun runAction(player: Player, action: List<String>) {
        runAction(player, action, null)
    }

    /**
     * 执行物品动作
     *
     * @param player 执行玩家
     * @param action 动作文本
     * @return 是否继续执行(执行List<String>中的物品动作时, 某个动作返回false则终止动作执行)
     */
    fun runAction(player: Player, action: String): Boolean {
        return runAction(player, action, null)
    }

    /**
     * 执行物品动作
     *
     * @param player 执行玩家
     * @param action 动作文本
     * @param itemTag 用于解析nbt及data, 可为空
     * @return 是否继续执行(执行List<String>中的物品动作时, 某个动作返回false则终止动作执行)
     */
    fun runAction(player: Player, action: String, itemTag: ItemTag? = null): Boolean {
        // 解析物品变量
        val actionString = when (itemTag) {
            null -> action
            else -> action.parseItemSection(itemTag, player)
        }
        // 解析动作类型及动作内容
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

    /**
     * 执行物品动作
     *
     * @param player 执行玩家
     * @param actionType 动作类型
     * @param actionContent 动作内容
     * @return 是否继续执行(执行List<String>中的物品动作时, 某个动作返回false则终止动作执行)
     */
    fun runAction(player: Player, actionType: String, actionContent: String): Boolean {
        actions[actionType]?.let {
            val actionFunction: BiFunction<Player, String, Boolean> = it
            return actionFunction.apply(player, actionContent)
        }
        return true
    }

    /**
     * 添加物品动作
     *
     * @param id 动作ID
     * @param function 动作执行函数
     */
    fun addAction(id: String, function: BiFunction<Player, String, Boolean>) {
        actions[id.lowercase(Locale.getDefault())] = function
    }

    /**
     * 加载所有拥有动作的物品及相关动作
     */
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

    /**
     * 加载自定义动作
     */
    private fun loadCustomActions() {
        for (file in ConfigUtils.getAllFiles("CustomActions")) {
            // 防止某个脚本出错导致加载中断
            try {
                pers.neige.neigeitems.script.CompiledScript(file).invoke("main", null)
            } catch (error: Throwable) {}
        }
    }

    /**
     * 加载基础物品动作
     */
    private fun loadBasicActions() {
        // 向玩家发送消息
        addAction("tell") { player, string ->
            player.sendMessage(papiColor(player, string))
            true
        }
        // 向玩家发送消息(不将&解析为颜色符号)
        addAction("tellNoColor") { player, string ->
            player.sendMessage(papi(player, string))
            true
        }
        // 强制玩家发送消息
        addAction("chat") { player, string ->
            runTreadSafe {
                player.chat(papi(player, string))
            }
            true
        }
        // 强制玩家发送消息(将&解析为颜色符号)
        addAction("chatWithColor") { player, string ->
            runTreadSafe {
                player.chat(papiColor(player, string))
            }
            true
        }
        // 强制玩家执行指令
        addAction("command") { player, string ->
            runTreadSafe {
                Bukkit.dispatchCommand(player, papiColor(player, string))
            }
            true
        }
        // 强制玩家执行指令
        actions["command"]?.let { addAction("player", it) }
        // 强制玩家执行指令(不将&解析为颜色符号)
        addAction("commandNoColor") { player, string ->
            runTreadSafe {
                Bukkit.dispatchCommand(player, papi(player, string))
            }
            true
        }
        // 后台执行指令
        addAction("console") { player, string ->
            runTreadSafe {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), papiColor(player, string))
            }
            true
        }
        // 后台执行指令(不将&解析为颜色符号)
        addAction("consoleNoColor") { player, string ->
            runTreadSafe {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), papi(player, string))
            }
            true
        }
        // 给予玩家金钱
        addAction("giveMoney") { player, string ->
            vaultHooker?.giveMoney(player, papi(player, string).toDoubleOrNull() ?: 0.toDouble())
            true
        }
        // 扣除玩家金钱
        addAction("takeMoney") { player, string ->
            vaultHooker?.takeMoney(player, papi(player, string).toDoubleOrNull() ?: 0.toDouble())
            true
        }
        // 给予玩家经验
        addAction("giveExp") { player, string ->
            runTreadSafe {
                player.giveExp(papi(player, string).toIntOrNull() ?: 0)
            }
            true
        }
        // 扣除玩家经验
        addAction("takeExp") { player, string ->
            runTreadSafe {
                player.giveExp((papi(player, string).toIntOrNull() ?: 0) * -1)
            }
            true
        }
        // 设置玩家经验
        addAction("setExp") { player, string ->
            runTreadSafe {
                player.totalExperience = papi(player, string).toIntOrNull() ?: 0
            }
            true
        }
        // 给予玩家经验等级
        addAction("giveLevel") { player, string ->
            runTreadSafe {
                player.giveExpLevels(papi(player, string).toIntOrNull() ?: 0)
            }
            true
        }
        // 扣除玩家经验等级
        addAction("takeLevel") { player, string ->
            runTreadSafe {
                player.giveExpLevels((papi(player, string).toIntOrNull() ?: 0) * -1)
            }
            true
        }
        // 设置玩家经验等级
        addAction("setLevel") { player, string ->
            runTreadSafe {
                player.level = papi(player, string).toIntOrNull() ?: 0
            }
            true
        }
        // 给予玩家饱食度
        addAction("giveFood") { player, string ->
            runTreadSafe {
                player.foodLevel = (player.foodLevel + (papi(player, string).toIntOrNull() ?: 0)).coerceAtLeast(0).coerceAtMost(20)
            }
            true
        }
        // 扣除玩家饱食度
        addAction("takeFood") { player, string ->
            runTreadSafe {
                player.foodLevel = (player.foodLevel - (papi(player, string).toIntOrNull() ?: 0)).coerceAtLeast(0).coerceAtMost(20)
            }
            true
        }
        // 设置玩家饱食度
        addAction("setFood") { player, string ->
            runTreadSafe {
                player.foodLevel = (papi(player, string).toIntOrNull() ?: 0).coerceAtLeast(0).coerceAtMost(20)
            }
            true
        }
        // 给予玩家生命
        addAction("giveHealth") { player, string ->
            runTreadSafe {
                player.health = (player.health + (papi(player, string).toDoubleOrNull() ?: 0.toDouble())).coerceAtMost(player.maxHealth)
            }
            true
        }
        // 扣除玩家生命
        addAction("takeHealth") { player, string ->
            runTreadSafe {
                player.health = (player.health - (papi(player, string).toDoubleOrNull() ?: 0.toDouble())).coerceAtLeast(0.toDouble())
            }
            true
        }
        // 设置玩家生命
        addAction("setHealth") { player, string ->
            runTreadSafe {
                player.health = (papi(player, string).toDoubleOrNull() ?: 0.toDouble()).coerceAtLeast(0.toDouble()).coerceAtMost(player.maxHealth)
            }
            true
        }
        // 释放MM技能
        addAction("castSkill") { player, string ->
            mythicMobsHooker?.castSkill(player, string, player)
            true
        }
        // 延迟(单位是tick)
        addAction("delay") { _, _ ->
            true
        }
        // 终止
        addAction("return") { _, _ ->
            false
        }
    }

    // 物品左右键交互
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
        // 没有对应物品动作就停止判断
        if (!itemAction.hasClickAction) return
        // 获取物品消耗信息
        val consume =  itemAction.consume
        // 获取交互类型
        val action = event.action
        val leftAction = (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK)
        val rightAction = (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)
        val allAction = (leftAction || rightAction)
        // 如果物品配置了消耗事件
        if (consume != null) {
            // 获取左键是否消耗
            val left = (leftAction && consume.getBoolean("left", false))
            // 获取右键是否消耗
            val right = (rightAction && consume.getBoolean("right", false))
            // 获取左右键是否消耗
            val all = (allAction && consume.getBoolean("all", false))
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
                    if (all) itemAction.run(player, itemAction.allSync, itemTag)
                    if (left) itemAction.run(player, itemAction.leftSync, itemTag)
                    if (right) itemAction.run(player, itemAction.rightSync, itemTag)
                    bukkitScheduler.runTaskAsynchronously(plugin, Runnable {
                        if (all) itemAction.run(player, itemAction.all, itemTag)
                        if (left) itemAction.run(player, itemAction.left, itemTag)
                        if (right) itemAction.run(player, itemAction.right, itemTag)
                    })
                }
            }
            // 消耗检测完毕, 检测不消耗情况
            if ((leftAction && !left && (itemAction.left != null || itemAction.leftSync != null))
                || (rightAction && !right && (itemAction.right != null || itemAction.rightSync != null))
                || (allAction && !all && (itemAction.all != null || itemAction.allSync != null))) {
                // 取消交互事件
                event.isCancelled = true
                // 检测冷却
                if (!itemAction.isCoolDown(player)) {
                    // 执行动作
                    if (allAction && !all) itemAction.run(player, itemAction.allSync, itemTag)
                    if (leftAction && !left) itemAction.run(player, itemAction.leftSync, itemTag)
                    if (rightAction && !right) itemAction.run(player, itemAction.rightSync, itemTag)
                    bukkitScheduler.runTaskAsynchronously(plugin, Runnable {
                        if (allAction && !all) itemAction.run(player, itemAction.all, itemTag)
                        if (leftAction && !left) itemAction.run(player, itemAction.left, itemTag)
                        if (rightAction && !right) itemAction.run(player, itemAction.right, itemTag)
                    })
                }
            }
        } else {
            if ((leftAction && (itemAction.left != null || itemAction.leftSync != null))
                || (rightAction && (itemAction.right != null || itemAction.rightSync != null))
                || (allAction && (itemAction.all != null || itemAction.allSync != null))) {
                // 取消交互事件
                event.isCancelled = true
                // 检测冷却
                if (!itemAction.isCoolDown(player)) {
                    // 执行动作
                    if (allAction) itemAction.run(player, itemAction.allSync, itemTag)
                    if (leftAction) itemAction.run(player, itemAction.leftSync, itemTag)
                    if (rightAction) itemAction.run(player, itemAction.rightSync, itemTag)
                    bukkitScheduler.runTaskAsynchronously(plugin, Runnable {
                        if (allAction) itemAction.run(player, itemAction.all, itemTag)
                        if (leftAction) itemAction.run(player, itemAction.left, itemTag)
                        if (rightAction) itemAction.run(player, itemAction.right, itemTag)
                    })
                }
            }
        }
    }

    // 吃或饮用
    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun listener(event: PlayerItemConsumeEvent) {
        // 获取玩家
        val player = event.player
        // 获取手持物品
        val itemStack = event.item.clone()
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
        // 没有对应物品动作就停止判断
        if (!itemAction.hasEatAction) return
        // 获取物品消耗信息
        val consume =  itemAction.consume
        event.isCancelled = true
        // 如果该物品需要被消耗
        if (consume != null && consume.getBoolean("eat", false)) {
            // 检测冷却
            if (consume.isCoolDown(player, id)) return
            // 获取待消耗数量
            val amount: Int = consume.getInt("amount", 1)
            // 消耗物品
            itemStack.consumeAndReturn(amount, itemTag, neigeItems)?.let { itemStacks ->
                // 设置物品
                if (event.item == player.inventory.itemInMainHand) {
                    player.inventory.setItemInMainHand(itemStacks[0])
                } else {
                    player.inventory.setItemInOffHand(itemStacks[0])
                }
                if (itemStacks.size > 1) {
                    bukkitScheduler.runTaskLater(plugin, Runnable {
                        player.giveItem(itemStacks[1])
                    }, 1)
                }
                // 执行动作
                itemAction.run(player, itemAction.eatSync, itemTag)
                bukkitScheduler.runTaskAsynchronously(plugin, Runnable {
                    itemAction.run(player, itemAction.eat, itemTag)
                })
            }
        } else {
            // 检测冷却
            if (!itemAction.isCoolDown(player)) {
                // 执行动作
                itemAction.run(player, itemAction.eatSync, itemTag)
                bukkitScheduler.runTaskAsynchronously(plugin, Runnable {
                    itemAction.run(player, itemAction.eat, itemTag)
                })
            }
        }
    }

    // 丢弃物品
    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun listener(event: PlayerDropItemEvent) {
        // 获取玩家
        val player = event.player
        // 获取掉落物品
        val itemStack = event.itemDrop.itemStack
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
        // 没有对应物品动作就停止判断
        if (!itemAction.hasDropAction) return
        // 获取物品消耗信息
        val consume =  itemAction.consume
        // 如果该物品需要被消耗
        if (consume != null && consume.getBoolean("drop", false)) {
            // 检测冷却
            if (!consume.isCoolDown(player, id)) {
                // 获取待消耗数量
                val amount: Int = consume.getInt("amount", 1)
                // 消耗物品
                if (itemStack.consume(player, amount, itemTag, neigeItems)) {
                    // 执行动作
                    itemAction.run(player, itemAction.dropSync, itemTag)
                    bukkitScheduler.runTaskAsynchronously(plugin, Runnable {
                        itemAction.run(player, itemAction.drop, itemTag)
                    })
                }
            } else {
                event.isCancelled = true
            }
        } else {
            // 检测冷却
            if (!itemAction.isCoolDown(player)) {
                itemAction.run(player, itemAction.dropSync, itemTag)
                bukkitScheduler.runTaskAsynchronously(plugin, Runnable {
                    // 执行动作
                    itemAction.run(player, itemAction.drop, itemTag)
                })
            } else {
                event.isCancelled = true
            }
        }
    }

    // 拾取物品
    @SubscribeEvent(priority = EventPriority.HIGH)
    fun listener(event: EntityPickupItemEvent) {
        // 获取玩家
        val player = event.entity
        if (player !is Player) return
        // 获取拾取物品
        val itemStack = event.item.itemStack
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
        // 没有对应物品动作就停止判断
        if (!itemAction.hasPickAction) return
        // 获取物品消耗信息
        val consume =  itemAction.consume
        // 如果该物品需要被消耗
        if (consume != null && consume.getBoolean("pick", false)) {
            // 检测冷却
            if (consume.isCoolDown(player, id)) return
            // 获取待消耗数量
            val amount: Int = consume.getInt("amount", 1)
            // 消耗物品
            if (itemStack.consume(player, amount, itemTag, neigeItems)) {
                // 执行动作
                itemAction.run(player, itemAction.pickSync, itemTag)
                bukkitScheduler.runTaskAsynchronously(plugin, Runnable {
                    itemAction.run(player, itemAction.pick, itemTag)
                })
            }
        } else {
            // 检测冷却
            if (!itemAction.isCoolDown(player)) {
                // 执行动作
                itemAction.run(player, itemAction.pickSync, itemTag)
                bukkitScheduler.runTaskAsynchronously(plugin, Runnable {
                    itemAction.run(player, itemAction.pick, itemTag)
                })
            }
        }
    }

    private fun runTreadSafe(task: Runnable) {
        if (isPrimaryThread()) {
            task.run()
        } else {
            bukkitScheduler.runTask(plugin, task)
        }
    }
}
