package pers.neige.neigeitems.manager

import bot.inker.bukkit.nbt.NbtCompound
import org.bukkit.Bukkit
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import pers.neige.neigeitems.NeigeItems.plugin
import pers.neige.neigeitems.item.ItemInfo
import pers.neige.neigeitems.item.action.ComboInfo
import pers.neige.neigeitems.item.action.ItemAction
import pers.neige.neigeitems.manager.ConfigManager.comboInterval
import pers.neige.neigeitems.manager.ConfigManager.config
import pers.neige.neigeitems.manager.HookerManager.mythicMobsHooker
import pers.neige.neigeitems.manager.HookerManager.nashornHooker
import pers.neige.neigeitems.manager.HookerManager.papi
import pers.neige.neigeitems.manager.HookerManager.papiColor
import pers.neige.neigeitems.manager.HookerManager.vaultHooker
import pers.neige.neigeitems.manager.ItemEditorManager.runEditorWithResult
import pers.neige.neigeitems.utils.ActionUtils.consume
import pers.neige.neigeitems.utils.ActionUtils.isCoolDown
import pers.neige.neigeitems.utils.ConfigUtils
import pers.neige.neigeitems.utils.ItemUtils.getNbt
import pers.neige.neigeitems.utils.PlayerUtils.getMetadataEZ
import pers.neige.neigeitems.utils.PlayerUtils.sendActionBar
import pers.neige.neigeitems.utils.PlayerUtils.setMetadataEZ
import pers.neige.neigeitems.utils.SchedulerUtils.runLater
import pers.neige.neigeitems.utils.SchedulerUtils.sync
import pers.neige.neigeitems.utils.SectionUtils.parseItemSection
import pers.neige.neigeitems.utils.SectionUtils.parseSection
import pers.neige.neigeitems.utils.StringUtils.split
import pers.neige.neigeitems.utils.StringUtils.splitOnce
import java.io.File
import java.io.InputStreamReader
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.function.BiFunction
import javax.script.CompiledScript
import javax.script.SimpleBindings

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

    /**
     * 获取用于编译condition的脚本引擎
     */
    val engine = nashornHooker.getGlobalEngine().also { engine ->
        // 加载顶级成员
        plugin.getResource("JavaScriptLib/lib.js")?.use { input ->
            InputStreamReader(input, "UTF-8").use { reader ->
                engine.eval(reader)
            }
        }
    }

    /**
     * 获取缓存的已编译condition脚本
     */
    val conditionScripts = ConcurrentHashMap<String, CompiledScript>()

    /**
     * 获取缓存的已编译action脚本
     */
    val actionScripts = ConcurrentHashMap<String, CompiledScript>()

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
     * @return 是否继续执行(执行List<String>中的物品动作时, 某个动作返回false则终止动作执行)
     */
    fun runAction(
        player: Player,
        action: String
    ): Boolean {
        return runAction(player, action, null, null, null, null, HashMap<String, Any?>())
    }

    /**
     * 执行物品动作
     *
     * @param player 执行玩家
     * @param action 动作内容
     * @param global 用于存储整个动作运行过程中的全局变量
     * @param map 传入js的顶级变量
     * @return 是否继续执行(执行List<String>中的物品动作时, 某个动作返回false则终止动作执行)
     */
    fun runAction(
        player: Player,
        action: Any?,
        global: MutableMap<String, Any?> = HashMap<String, Any?>(),
        map: Map<String, Any?>? = null
    ): Boolean {
        when (action) {
            is String -> return runAction(player, action, null, null, null, null, global, map)
            is List<*> -> return runAction(player, action, null, null, null, null, global, 0, action.size, map)
            is Map<*, *> -> return runAction(player, action as Map<String, *>, null, null, null, null, global, map)
            is ConfigurationSection -> return runAction(player, action, null, null, null, null, global, map)
        }
        return true
    }

    /**
     * 执行物品动作
     *
     * @param player 执行玩家
     * @param action 动作内容
     * @param itemStack 用于解析条件, 可为空
     * @param itemTag 用于解析nbt及data, 可为空
     * @param data 物品节点数据
     * @param event 用于解析条件, 可为空
     * @param global 用于存储整个动作运行过程中的全局变量
     * @param map 传入js的顶级变量
     * @return 是否继续执行(执行List<String>中的物品动作时, 某个动作返回false则终止动作执行)
     */
    fun runAction(
        player: Player,
        action: Any?,
        itemStack: ItemStack? = null,
        itemTag: NbtCompound? = itemStack?.let{ itemStack.getNbt() },
        data: MutableMap<String, String>? = null,
        event: Event? = null,
        global: MutableMap<String, Any?> = HashMap<String, Any?>(),
        map: Map<String, Any?>? = null
    ): Boolean {
        when (action) {
            is String -> return runAction(player, action, itemStack, itemTag, data, event, global, map)
            is List<*> -> return runAction(player, action, itemStack, itemTag, data, event, global, 0, action.size, map)
            is Map<*, *> -> return runAction(player, action as Map<String, *>, itemStack, itemTag, data, event, global, map)
            is ConfigurationSection -> return runAction(player, action, itemStack, itemTag, data, event, global, map)
        }
        return true
    }

    /**
     * 执行物品动作
     *
     * @param player 执行玩家
     * @param actionType 动作类型
     * @param actionContent 动作内容
     * @param itemStack 用于解析条件, 可为空
     * @param itemTag 用于解析nbt及data, 可为空
     * @param data 物品节点数据
     * @param event 用于解析条件, 可为空
     * @param global 用于存储整个动作运行过程中的全局变量
     * @param map 传入js的顶级变量
     * @return 是否继续执行(执行List<String>中的物品动作时, 某个动作返回false则终止动作执行)
     */
    private fun runAction(
        player: Player,
        actionType: String,
        actionContent: String,
        itemStack: ItemStack? = null,
        itemTag: NbtCompound? = itemStack?.let{ itemStack.getNbt() },
        data: MutableMap<String, String>? = null,
        event: Event? = null,
        global: MutableMap<String, Any?>,
        map: Map<String, Any?>? = null
    ): Boolean {
        // 获取动作id
        val lowercaseActionType = actionType.lowercase(Locale.getDefault())
        // 尝试加载物品动作, 返回执行结果
        actions[lowercaseActionType]?.apply(player, actionContent)?.also { return it }
        // 尝试加载物品编辑函数, 返回执行结果
        itemStack?.also { runEditorWithResult(actionType, actionContent, itemStack, player)?.also { return it } }
        // 尝试加载内置函数, 返回执行结果
        when (lowercaseActionType) {
            "js" -> {
                // 动作中调用的顶级变量
                val bindings = SimpleBindings()
                map?.forEach { (key, value) ->
                    value?.let { bindings[key] = it }
                }
                val vars = HashMap<String, Any?>()
                bindings["variables"] = vars
                bindings["vars"] = vars
                player.let { bindings["player"] = it }
                itemStack?.let { bindings["itemStack"] = it }
                itemTag?.let { bindings["itemTag"] = it }
                data?.let { bindings["data"] = it }
                event?.let { bindings["event"] = it }
                bindings["global"] = global
                bindings["glo"] = global
                val result =  try {
                    actionScripts.computeIfAbsent(actionContent) {
                        nashornHooker.compile(engine, actionContent)
                    }.eval(bindings) ?: true
                } catch (error: Throwable) {
                    error.printStackTrace()
                    true
                }
                // js动作返回false等同于break
                return if (result is Boolean) {
                    result
                } else {
                    true
                }
            }
            "setglobal" -> {
                val info = actionContent.splitOnce(" ")
                if (info.size > 1) {
                    global[info[0]] = info[1]
                }
            }
            "delglobal" -> {
                global.remove(actionContent)
            }
        }
        return true
    }

    /**
     * 执行物品动作
     *
     * @param player 执行玩家
     * @param action 动作文本
     * @param start 动作从第几个开始执行(默认为0)
     * @param end 动作执行到第几个结束
     * @param itemStack 用于解析条件, 可为空
     * @param itemTag 用于解析nbt及data, 可为空
     * @param data 物品节点数据
     * @param event 用于解析条件, 可为空
     * @param global 用于存储整个动作运行过程中的全局变量
     * @param map 传入js的顶级变量
     */
    private fun runAction(
        player: Player,
        action: List<*>,
        itemStack: ItemStack? = null,
        itemTag: NbtCompound? = itemStack?.let{ itemStack.getNbt() },
        data: MutableMap<String, String>? = null,
        event: Event? = null,
        global: MutableMap<String, Any?>,
        start: Int = 0,
        end: Int = action.size,
        map: Map<String, Any?>? = null
    ): Boolean {
        // 动作执行延迟
        var delay = 0L

        // 动作执行起止
        val actionStart = start.coerceAtLeast(0).coerceAtMost(action.size)
        val actionEnd = end.coerceAtLeast(actionStart).coerceAtMost(action.size)

        // 遍历所有动作
        for (index in actionStart until actionEnd) {
            // 延迟执行
            if (delay > 0) {
                runLater(delay) {
                    runAction(player, action, itemStack, itemTag, data, event, global, index, actionEnd, map)
                }
                // 停止当前操作
                return true
            }

            // 动作内容
            val value = action[index]

            // 如果属于一条文本
            if (value is String) {
                // 解析物品变量
                val actionString = when (itemTag) {
                    null -> value.parseSection(
                        (map?.get("cache") ?: global) as? MutableMap<String, String>,
                        player,
                        map?.get("sections") as? ConfigurationSection,
                    )
                    else -> value.parseItemSection(
                        itemStack!!,
                        itemTag,
                        data,
                        player,
                        (map?.get("cache") ?: global) as? MutableMap<String, String>,
                        map?.get("sections") as? ConfigurationSection
                    )
                }
                // 解析动作类型及动作内容
                val info = actionString.splitOnce(": ")
                val actionType = info[0]
                val actionContent = info.getOrNull(1) ?: ""

                // 执行动作
                when {
                    // 延迟
                    actionType == "delay" -> delay += actionContent.toLongOrNull() ?: 0
                    // 正常执行
                    else -> {
                        if (!runAction(player, actionType, actionContent, itemStack, itemTag, data, event, global, map)) return false
                    }
                }
                // 如果属于其他类型
            } else {
                // 直接执行
                if (!runAction(player, value, itemStack, itemTag, data, event, global, map)) return false
            }
        }
        return true
    }

    /**
     * 执行物品动作
     *
     * @param player 执行玩家
     * @param action 动作文本
     * @param itemStack 用于解析条件, 可为空
     * @param itemTag 用于解析nbt及data, 可为空
     * @param data 物品节点数据
     * @param event 用于解析条件, 可为空
     * @param global 用于存储整个动作运行过程中的全局变量
     * @param map 传入js的顶级变量
     * @return 是否继续执行(执行List<String>中的物品动作时, 某个动作返回false则终止动作执行)
     */
    private fun runAction(
        player: Player,
        action: String,
        itemStack: ItemStack? = null,
        itemTag: NbtCompound? = itemStack?.let{ itemStack.getNbt() },
        data: MutableMap<String, String>? = null,
        event: Event? = null,
        global: MutableMap<String, Any?>,
        map: Map<String, Any?>? = null
    ): Boolean {
        // 解析物品变量
        val actionString = when (itemTag) {
            null -> action.parseSection(
                (map?.get("cache") ?: global) as? MutableMap<String, String>,
                player,
                map?.get("sections") as? ConfigurationSection,
            )
            else -> action.parseItemSection(
                itemStack!!,
                itemTag,
                data,
                player,
                (map?.get("cache") ?: global) as? MutableMap<String, String>,
                map?.get("sections") as? ConfigurationSection
            )
        }
        // 解析动作类型及动作内容
        val info = actionString.splitOnce(": ")
        val actionType = info[0]
        val actionContent = info.getOrNull(1) ?: ""
        // 执行动作
        return runAction(player, actionType, actionContent, itemStack, itemTag, data, event, global, map)
    }

    /**
     * 执行物品动作
     *
     * @param player 执行玩家
     * @param action 动作内容
     * @param itemStack 用于解析条件, 可为空
     * @param itemTag 用于解析nbt及data, 可为空
     * @param data 物品节点数据
     * @param event 用于解析条件, 可为空
     * @param global 用于存储整个动作运行过程中的全局变量
     * @param map 传入js的顶级变量
     * @return 是否继续执行(执行List<String>中的物品动作时, 某个动作返回false则终止动作执行)
     */
    private fun runAction(
        player: Player,
        action: ConfigurationSection,
        itemStack: ItemStack? = null,
        itemTag: NbtCompound? = itemStack?.let{ itemStack.getNbt() },
        data: MutableMap<String, String>? = null,
        event: Event? = null,
        global: MutableMap<String, Any?>,
        map: Map<String, Any?>? = null
    ): Boolean {
        // 动作执行条件
        val condition: String? = action.getString("condition")
        // 循环执行条件
        val whileCondition: String? = action.getString("while")
        // 动作内容
        val actions = action.get("actions")
        // 结束动作内容
        val finally = action.get("finally")
        // 不满足条件时执行的内容
        val deny = action.get("deny")

        // 模式检测
        when {
            // condition模式
            condition != null -> {
                // 如果条件通过
                if (parseCondition(condition, player, itemStack, itemTag, data, event, global, map)) {
                    // 执行动作
                    return runAction(player, actions, itemStack, itemTag, data, event, global, map)
                    // 条件未通过
                } else {
                    // 执行deny动作
                    return runAction(player, deny, itemStack, itemTag, data, event, global, map)
                }
            }
            // while模式
            whileCondition != null -> {
                while (parseCondition(whileCondition, player, itemStack, itemTag, data, event, global, map)) {
                    // 执行动作
                    val result = runAction(player, actions, itemStack, itemTag, data, event, global, map)
                    if (!result) {
                        runAction(player, finally, itemStack, itemTag, data, event, global, map)
                        return false
                    }
                }
                runAction(player, finally, itemStack, itemTag, data, event, global, map)
                return true
            }
            else ->  {
                return runAction(player, actions, itemStack, itemTag, data, event, global, map)
            }
        }
    }

    /**
     * 执行物品动作
     *
     * @param player 执行玩家
     * @param action 动作内容
     * @param itemStack 用于解析条件, 可为空
     * @param itemTag 用于解析nbt及data, 可为空
     * @param data 物品节点数据
     * @param event 用于解析条件, 可为空
     * @param global 用于存储整个动作运行过程中的全局变量
     * @param map 传入js的顶级变量
     * @return 是否继续执行(执行List<String>中的物品动作时, 某个动作返回false则终止动作执行)
     */
    private fun runAction(
        player: Player,
        action: Map<String, *>,
        itemStack: ItemStack? = null,
        itemTag: NbtCompound? = itemStack?.let{ itemStack.getNbt() },
        data: MutableMap<String, String>? = null,
        event: Event? = null,
        global: MutableMap<String, Any?>,
        map: Map<String, Any?>? = null
    ): Boolean {
        // 动作执行条件
        val condition: String? = action["condition"] as String?
        // 循环执行条件
        val whileCondition: String? = action["while"] as String?
        // 动作内容
        val actions = action["actions"]
        // 结束动作内容
        val finally = action["finally"]
        // 不满足条件时执行的内容
        val deny = action["deny"]

        // 模式检测
        when {
            // condition模式
            condition != null -> {
                // 如果条件通过
                if (parseCondition(condition, player, itemStack, itemTag, data, event, global, map)) {
                    // 执行动作
                    return runAction(player, actions, itemStack, itemTag, data, event, global, map)
                    // 条件未通过
                } else {
                    // 执行deny动作
                    return runAction(player, deny, itemStack, itemTag, data, event, global, map)
                }
            }
            // while模式
            whileCondition != null -> {
                while (parseCondition(whileCondition, player, itemStack, itemTag, data, event, global, map)) {
                    // 执行动作
                    val result = runAction(player, actions, itemStack, itemTag, data, event, global, map)
                    if (!result) {
                        runAction(player, finally, itemStack, itemTag, data, event, global, map)
                        return false
                    }
                }
                runAction(player, finally, itemStack, itemTag, data, event, global, map)
                return true
            }
            else ->  {
                return runAction(player, actions, itemStack, itemTag, data, event, global, map)
            }
        }
    }


    /**
     * 解析条件
     *
     * @param condition 条件内容
     * @param player 执行玩家
     * @param global 用于存储整个动作运行过程中的全局变量
     * @param map 传入js的顶级变量
     * @return 是否继续执行(执行List<String>中的物品动作时, 某个动作返回false则终止动作执行)
     */
    fun parseCondition(
        condition: String?,
        player: Player?,
        global: MutableMap<String, Any?> = HashMap<String, Any?>(),
        map: Map<String, Any?>? = null
    ): Boolean {
        return parseCondition(
            condition = condition,
            player = player,
            global = global,
            map = map,
            event = null
        )
    }

    /**
     * 解析条件
     *
     * @param condition 条件内容
     * @param player 执行玩家
     * @param itemStack 用于解析条件, 可为空
     * @param itemTag 用于解析nbt及data, 可为空
     * @param data 物品节点数据
     * @param event 用于解析条件, 可为空
     * @param global 用于存储整个动作运行过程中的全局变量
     * @param map 传入js的顶级变量
     * @return 是否继续执行(执行List<String>中的物品动作时, 某个动作返回false则终止动作执行)
     */
    fun parseCondition(
        condition: String?,
        player: Player?,
        itemStack: ItemStack? = null,
        itemTag: NbtCompound? = itemStack?.let{ itemStack.getNbt() },
        data: MutableMap<String, String>? = null,
        event: Event? = null,
        global: MutableMap<String, Any?> = HashMap<String, Any?>(),
        map: Map<String, Any?>? = null
    ): Boolean {
        val pass = condition?.let {
            // 条件中调用的顶级变量
            val bindings = SimpleBindings()
            map?.forEach { (key, value) ->
                value?.let { bindings[key] = it }
            }
            val vars = HashMap<String, Any?>()
            bindings["variables"] = vars
            bindings["vars"] = vars
            player?.let { bindings["player"] = it }
            itemStack?.let { bindings["itemStack"] = it }
            itemTag?.let { bindings["itemTag"] = it }
            data?.let { bindings["data"] = it }
            event?.let { bindings["event"] = it }
            bindings["global"] = global
            bindings["glo"] = global
            // 解析条件
            try {
                // 条件里返回null就直接转换成false
                conditionScripts.computeIfAbsent(condition) {
                    nashornHooker.compile(engine, condition)
                }.eval(bindings) ?: false
            } catch (error: Throwable) {
                // 条件报错就视作false
                error.printStackTrace()
                false
            }
        }
        return when (pass) {
            is Boolean -> pass
            // 这里的null指没有条件, 故而视为true
            null -> true
            else -> false
        }
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
        // 是否升级旧版本配置
        val upgrade = config.getBoolean("ItemAction.upgrade")
        // 遍历物品动作配置文件
        for (file: File in ConfigUtils.getAllFiles("ItemActions")) {
            // 仅加载.yml文件
            if (!file.name.endsWith(".yml")) continue
            // 将当前文件转换为YamlConfiguration
            val config = YamlConfiguration.loadConfiguration(file)
            var upgraded = false
            // 遍历顶级键
            config.getKeys(false).forEach { id ->
                // 当前物品的动作配置文件
                config.getConfigurationSection(id)?.let { configurationSection ->
                    // 进行升级操作
                    if (upgrade && upgradeConfig(configurationSection)) upgraded = true
                    // 加载物品动作
                    itemActions[id] = ItemAction(id, configurationSection)
                }
            }
            // 保存升级内容
            if (upgrade && upgraded) config.save(file)
        }
        if (upgrade) {
            config.set("ItemAction.upgrade", false)
            plugin.saveConfig()
        }
    }

    /**
     * 升级旧版本物品动作配置文件
     *
     * @param configurationSection
     * @return 这配置是不是旧配置
     */
    private fun upgradeConfig(configurationSection: ConfigurationSection): Boolean {
        var upgraded = false

        // 消耗配置
        val consume = configurationSection.getConfigurationSection("consume")
        // 冷却时间配置
        val cooldown = configurationSection.get("cooldown")
        // 冷却组配置
        val group = configurationSection.get("group")
        // 动作配置
        arrayListOf("left", "right", "all", "eat", "drop", "pick").forEach { id ->
            // 配置了当前动作
            if (configurationSection.contains(id) && configurationSection.getConfigurationSection(id) == null) {
                upgraded = true
                val config = YamlConfiguration()
                // 当前配置有消耗动作
                if (consume?.getBoolean(id) == true
                    // all类型的特殊判断
                    || (id == "all" && consume?.getBoolean("left") == true || consume?.getBoolean("right") == true )) {
                    // 转换消耗数量
                    config.set("consume.amount", consume.get("amount"))
                    // 转换冷却时间
                    config.get("consume.cooldown")?.let { config.set("cooldown", cooldown) }
                    // 转换冷却组
                    config.get("consume.group")?.let { config.set("group", cooldown) }
                }
                // 转换冷却时间
                config.set("cooldown", cooldown)
                // 转换冷却组
                config.set("group", group)
                // 转成sync
                config.set("sync", configurationSection.getStringList(id))
                // 设置升级后配置
                configurationSection.set(id, config)
            }
        }
        if (upgraded) {
            // 移除消耗配置
            configurationSection.set("consume", null)
            // 移除冷却时间配置
            configurationSection.set("cooldown", null)
            // 移除冷却组配置
            configurationSection.set("group", null)
        }
        return upgraded
    }

    /**
     * 加载自定义动作
     */
    private fun loadCustomActions() {
        for (file in ConfigUtils.getAllFiles("CustomActions")) {
            // 仅加载.js文件
            if (!file.name.endsWith(".js")) continue
            // 防止某个脚本出错导致加载中断
            try {
                val script = pers.neige.neigeitems.script.CompiledScript(file)
                script.invoke("main", null)
            } catch (_: Throwable) {}
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
            sync {
                player.chat(papi(player, string))
            }
            true
        }
        // 强制玩家发送消息(将&解析为颜色符号)
        addAction("chatWithColor") { player, string ->
            sync {
                player.chat(papiColor(player, string))
            }
            true
        }
        // 强制玩家执行指令
        addAction("command") { player, string ->
            sync {
                Bukkit.dispatchCommand(player, papiColor(player, string))
            }
            true
        }
        // 强制玩家执行指令
        actions["command"]?.let { addAction("player", it) }
        // 强制玩家执行指令(不将&解析为颜色符号)
        addAction("commandNoColor") { player, string ->
            sync {
                Bukkit.dispatchCommand(player, papi(player, string))
            }
            true
        }
        // 后台执行指令
        addAction("console") { player, string ->
            sync {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), papiColor(player, string))
            }
            true
        }
        // 后台执行指令(不将&解析为颜色符号)
        addAction("consoleNoColor") { player, string ->
            sync {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), papi(player, string))
            }
            true
        }
        // 发送Title
        addAction("title") { player, string ->
            sync {
                papiColor(player, string).split(' ', '\\').also { args ->
                    val title = args.getOrNull(0)
                    val subtitle = args.getOrNull(1) ?: ""
                    val fadeIn = args.getOrNull(2)?.toIntOrNull() ?: 10
                    val stay = args.getOrNull(3)?.toIntOrNull() ?: 70
                    val fadeOut = args.getOrNull(4)?.toIntOrNull() ?: 20
                    player.sendTitle(title, subtitle, fadeIn, stay, fadeOut)
                }
            }
            true
        }
        // 发送Title(不将&解析为颜色符号)
        addAction("titleNoColor") { player, string ->
            sync {
                papi(player, string).split(' ', '\\').also { args ->
                    val title = args.getOrNull(0)
                    val subtitle = args.getOrNull(1) ?: ""
                    val fadeIn = args.getOrNull(2)?.toIntOrNull() ?: 10
                    val stay = args.getOrNull(3)?.toIntOrNull() ?: 70
                    val fadeOut = args.getOrNull(4)?.toIntOrNull() ?: 20
                    player.sendTitle(title, subtitle, fadeIn, stay, fadeOut)
                }
            }
            true
        }
        // 发送ActionBar
        addAction("actionBar") { player, string ->
            sync {
                player.sendActionBar(papiColor(player, string))
            }
            true
        }
        // 发送ActionBar(不将&解析为颜色符号)
        addAction("actionBarNoColor") { player, string ->
            sync {
                player.sendActionBar(papi(player, string))
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
            sync {
                player.giveExp(papi(player, string).toIntOrNull() ?: 0)
            }
            true
        }
        // 扣除玩家经验
        addAction("takeExp") { player, string ->
            sync {
                player.giveExp((papi(player, string).toIntOrNull() ?: 0) * -1)
            }
            true
        }
        // 设置玩家经验
        addAction("setExp") { player, string ->
            sync {
                player.totalExperience = papi(player, string).toIntOrNull() ?: 0
            }
            true
        }
        // 给予玩家经验等级
        addAction("giveLevel") { player, string ->
            sync {
                player.giveExpLevels(papi(player, string).toIntOrNull() ?: 0)
            }
            true
        }
        // 扣除玩家经验等级
        addAction("takeLevel") { player, string ->
            sync {
                player.giveExpLevels((papi(player, string).toIntOrNull() ?: 0) * -1)
            }
            true
        }
        // 设置玩家经验等级
        addAction("setLevel") { player, string ->
            sync {
                player.level = papi(player, string).toIntOrNull() ?: 0
            }
            true
        }
        // 给予玩家饱食度
        addAction("giveFood") { player, string ->
            sync {
                player.foodLevel = (player.foodLevel + (papi(player, string).toIntOrNull() ?: 0)).coerceAtLeast(0).coerceAtMost(20)
            }
            true
        }
        // 扣除玩家饱食度
        addAction("takeFood") { player, string ->
            sync {
                player.foodLevel = (player.foodLevel - (papi(player, string).toIntOrNull() ?: 0)).coerceAtLeast(0).coerceAtMost(20)
            }
            true
        }
        // 设置玩家饱食度
        addAction("setFood") { player, string ->
            sync {
                player.foodLevel = (papi(player, string).toIntOrNull() ?: 0).coerceAtLeast(0).coerceAtMost(20)
            }
            true
        }
        // 给予玩家饱和度
        addAction("giveSaturation") { player, string ->
            sync {
                player.saturation = (player.saturation + (papi(player, string).toFloatOrNull() ?: 0F)).coerceAtLeast(0F).coerceAtMost(player.foodLevel.toFloat())
            }
            true
        }
        // 扣除玩家饱和度
        addAction("takeSaturation") { player, string ->
            sync {
                player.saturation = (player.saturation - (papi(player, string).toFloatOrNull() ?: 0F)).coerceAtLeast(0F).coerceAtMost(player.foodLevel.toFloat())
            }
            true
        }
        // 设置玩家饱和度
        addAction("setSaturation") { player, string ->
            sync {
                player.saturation = (papi(player, string).toFloatOrNull() ?: 0F).coerceAtLeast(0F).coerceAtMost(player.foodLevel.toFloat())
            }
            true
        }
        // 给予玩家生命
        addAction("giveHealth") { player, string ->
            sync {
                player.health = (player.health + (papi(player, string).toDoubleOrNull() ?: 0.toDouble())).coerceAtMost(player.maxHealth)
            }
            true
        }
        // 扣除玩家生命
        addAction("takeHealth") { player, string ->
            sync {
                player.health = (player.health - (papi(player, string).toDoubleOrNull() ?: 0.toDouble())).coerceAtLeast(0.toDouble())
            }
            true
        }
        // 设置玩家生命
        addAction("setHealth") { player, string ->
            sync {
                player.health = (papi(player, string).toDoubleOrNull() ?: 0.toDouble()).coerceAtLeast(0.toDouble()).coerceAtMost(player.maxHealth)
            }
            true
        }
        // 释放MM技能
        addAction("castSkill") { player, string ->
            mythicMobsHooker?.castSkill(player, string, player)
            true
        }
        // 连击记录
        addAction("combo") { player, string ->
            val info = papi(player, string).splitOnce(" ")
            // 连击组
            val comboGroup = info[0]
            // 连击类型
            val comboType = info.getOrNull(1) ?: ""
            if(!player.hasMetadata("NI-Combo-$comboGroup")) {
                player.setMetadataEZ("NI-Combo-$comboGroup", ArrayList<ComboInfo>())
            }
            // 当前时间
            val time = System.currentTimeMillis()
            // 记录列表
            val comboInfos = player.getMetadata("NI-Combo-$comboGroup")[0].value() as ArrayList<ComboInfo>
            // 为空则填入
            if (comboInfos.isEmpty()) {
                comboInfos.add(ComboInfo(comboType, time))
            } else {
                // 连击中断
                if (comboInfos.last().time + comboInterval < time) {
                    comboInfos.clear()
                }
                // 填入信息
                comboInfos.add(ComboInfo(comboType, time))
            }
            true
        }
        // 连击清空
        addAction("comboClear") { player, string ->
            player.setMetadataEZ("NI-Combo-${papi(player, string)}", ArrayList<ComboInfo>())
            true
        }
        // 设置药水效果
        addAction("setPotionEffect") { player, string ->
            val args = string.split(" ", limit = 3)
            if (args.size == 3) {
                val type = PotionEffectType.getByName(args[0].uppercase())
                val amplifier = args[1].toIntOrNull()
                val duration = args[2].toIntOrNull()
                if (type != null && duration != null && amplifier != null) {
                    sync {
                        player.addPotionEffect(PotionEffect(type, duration * 20, amplifier - 1), true)
                    }
                }
            }
            true
        }
        // 移除药水效果
        addAction("removePotionEffect") { player, string ->
            val type = PotionEffectType.getByName(string.uppercase())
            if (type != null) {
                sync {
                    player.removePotionEffect(type)
                }
            }
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
    fun interactListener(
        player: Player,
        itemStack: ItemStack,
        itemInfo: ItemInfo,
        event: PlayerInteractEvent
    ) {
        val id = itemInfo.id

        // 获取物品动作
        val itemAction = itemActions[id] ?: return
        // 获取基础触发器
        val basicTrigger = when (event.action) {
            // 左键类型
            Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK -> {
                when {
                    player.isSneaking -> {
                        // 如果既没有shift_left又没有shift_all就爬
                        if (!itemAction.hasShiftLeftAction) return
                        itemAction.triggers["shift_left"]
                    }
                    else -> {
                        // 如果既没有left又没有all就爬
                        if (!itemAction.hasLeftAction) return
                        itemAction.triggers["left"]
                    }
                }
            }
            // 右键类型
            Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK -> {
                when {
                    player.isSneaking -> {
                        // 如果既没有shift_right又没有shift_all就爬
                        if (!itemAction.hasShiftRightAction) return
                        itemAction.triggers["shift_right"]
                    }
                    else -> {
                        // 如果既没有right又没有all就爬
                        if (!itemAction.hasRightAction) return
                        itemAction.triggers["right"]
                    }
                }
            }
            // 停止操作
            else -> return
        }
        // 获取all触发器
        val allTrigger = when {
            player.isSneaking -> itemAction.triggers["shift_all"]
            else -> itemAction.triggers["all"]
        }

        val itemTag = itemInfo.itemTag
        val neigeItems = itemInfo.neigeItems
        val data = itemInfo.data

        // 获取消耗信息
        val consume = basicTrigger?.consume ?: allTrigger?.consume
        // 取消交互事件
        event.isCancelled = true
        // 检测冷却
        if ((basicTrigger ?: allTrigger)!!.isCoolDown(player, itemStack, itemTag, data)) return
        // 用于存储整个动作执行过程中的全局变量
        val global = HashMap<String, Any?>()
        // 如果物品需要消耗
        if (consume != null) {
            // 预执行动作
            runAction(player, consume.get("pre"), itemStack, itemTag, data, event, global)
            // 检测条件
            consume.getString("condition")?.let {
                // 不满足条件就爬
                if (!parseCondition(it, player, itemStack, itemTag, data, event, global)) {
                    // 跑一下deny动作
                    runAction(player, consume.get("deny"), itemStack, itemTag, data, event, global)
                    // 爬
                    return
                }
            }
            // 获取待消耗数量
            val amount: Int = consume.getString("amount")?.parseItemSection(itemStack, itemTag, data, player, global as? MutableMap<String, String>, null)?.toIntOrNull() ?: 1
            // 消耗物品
            if (!itemStack.consume(player, amount, itemTag, neigeItems)) {
                // 跑一下deny动作
                runAction(player, consume.get("deny"), itemStack, itemTag, data, event, global)
                // 数量不足
                return
            }
        }
        // 执行动作
        basicTrigger?.run(player, itemStack, itemTag, data, event, global)
        allTrigger?.run(player, itemStack, itemTag, data, event, global)
    }

    // 吃或饮用
    fun eatListener(
        player: Player,
        itemStack: ItemStack,
        itemInfo: ItemInfo,
        event: PlayerItemConsumeEvent
    ) {
        basicHandler(player, itemStack, itemInfo, event, "eat", cancel = true, cancelIfCooldown = false, giveLater = true)
    }

    // 丢弃物品
    fun dropListener(
        player: Player,
        itemStack: ItemStack,
        itemInfo: ItemInfo,
        event: PlayerDropItemEvent
    ) {
        basicHandler(player, itemStack, itemInfo, event, "drop", cancel = false, cancelIfCooldown = true)
    }

    // 拾取物品
    fun pickListener(
        player: Player,
        itemStack: ItemStack,
        itemInfo: ItemInfo,
        event: EntityPickupItemEvent
    ) {
        basicHandler(player, itemStack, itemInfo, event, "pick", cancel = false, cancelIfCooldown = true, consumeItem = false)
    }

    // 点击物品
    fun clickListener(
        player: Player,
        itemStack: ItemStack,
        itemInfo: ItemInfo,
        event: InventoryClickEvent
    ) {
        basicHandler(player, itemStack, itemInfo, event, "click")
    }

    // 物品被点击
    fun beClickedListener(
        player: Player,
        itemStack: ItemStack,
        itemInfo: ItemInfo,
        event: InventoryClickEvent
    ) {
        basicHandler(player, itemStack, itemInfo, event, "beclicked")
    }

    // 射箭时由弓触发
    fun shootBowListener(
        player: Player,
        itemStack: ItemStack,
        itemInfo: ItemInfo,
        event: EntityShootBowEvent
    ) {
        basicHandler(player, itemStack, itemInfo, event, "shoot_bow", cancel = false, cancelIfCooldown = true, consumeItem = false)
    }

    // 射箭时由箭触发
    fun shootArrowListener(
        player: Player,
        itemStack: ItemStack,
        itemInfo: ItemInfo,
        event: EntityShootBowEvent
    ) {
        basicHandler(player, itemStack, itemInfo, event, "shoot_arrow", cancel = false, cancelIfCooldown = true, consumeItem = false)
    }

    // 格挡时由盾触发
    fun blockingListener(
        player: Player,
        itemStack: ItemStack,
        itemInfo: ItemInfo,
        event: EntityDamageByEntityEvent
    ) {
        basicHandler(player, itemStack, itemInfo, event, "blocking", cancel = false, cancelIfCooldown = true)
    }

    // 攻击实体时由主手物品触发
    fun damageListener(
        player: Player,
        itemStack: ItemStack,
        itemInfo: ItemInfo,
        event: EntityDamageByEntityEvent
    ) {
        basicHandler(player, itemStack, itemInfo, event, "damage", cancel = false, cancelIfCooldown = true)
    }

    // 击杀实体时触发
    fun killListener(
        player: Player,
        itemStack: ItemStack,
        itemInfo: ItemInfo,
        event: EntityDamageByEntityEvent,
        key: String
    ) {
        basicHandler(player, itemStack, itemInfo, event, key, cancel = false, consumeItem = false)
    }

    // 挖掘方块时由主手物品触发
    fun breakBlockListener(
        player: Player,
        itemStack: ItemStack,
        itemInfo: ItemInfo,
        event: BlockBreakEvent
    ) {
        basicHandler(player, itemStack, itemInfo, event, "break_block", cancel = false, cancelIfCooldown = true)
    }

    // 适用于基础情况
    fun basicHandler(
        player: Player,
        itemStack: ItemStack,
        itemInfo: ItemInfo,
        event: Event,
        key: String,
        cancel: Boolean = true,
        cancelIfCooldown: Boolean = false,
        giveLater: Boolean = false,
        consumeItem: Boolean = true
    ) {
        val id = itemInfo.id
        // 获取物品动作
        val itemAction = itemActions[id] ?: return
        // 获取基础触发器
        val trigger = itemAction.triggers[key]
        // 没有对应物品动作就停止判断
        if (trigger == null) return

        val itemTag = itemInfo.itemTag
        val neigeItems = itemInfo.neigeItems
        val data = itemInfo.data

        // 取消事件
        if (event is Cancellable && cancel) {
            event.isCancelled = true
        }
        // 检测冷却
        if (trigger.isCoolDown(player, itemStack, itemTag, data)) {
            if ((cancel || cancelIfCooldown) && event is Cancellable) {
                event.isCancelled = true
            }
            return
        }
        // 用于存储整个动作执行过程中的全局变量
        val global = HashMap<String, Any?>()
        if (consumeItem) {
            // 获取物品消耗信息
            val consume =  trigger.consume
            // 如果该物品需要被消耗
            if (consume != null) {
                // 预执行动作
                runAction(player, consume.get("pre"), itemStack, itemTag, data, event, global)
                // 检测条件
                consume.getString("condition")?.let {
                    // 不满足条件就爬
                    if (!parseCondition(it, player, itemStack, itemTag, data, event, global)) {
                        // 跑一下deny动作
                        runAction(player, consume.get("deny"), itemStack, itemTag, data, event, global)
                        // 爬
                        return
                    }
                }
                // 获取待消耗数量
                val amount: Int = consume.getString("amount")?.parseItemSection(itemStack, itemTag, data, player, global as? MutableMap<String, String>, null)?.toIntOrNull() ?: 1
                // 消耗物品
                if (!itemStack.consume(player, amount, itemTag, neigeItems, giveLater)) {
                    // 跑一下deny动作
                    runAction(player, consume.get("deny"), itemStack, itemTag, data, event, global)
                    // 数量不足
                    return
                }
            }
        }
        // 执行动作
        trigger.run(player, itemStack, itemTag, data, event, global)
    }

    fun tick(
        player: Player,
        itemStack: ItemStack,
        itemInfo: ItemInfo,
        type: String
    ) {
        val id = itemInfo.id
        // 获取物品动作
        val itemAction = itemActions[id] ?: let { return }
        // 获取基础触发器
        val trigger = itemAction.triggers[type]
        // 没有对应物品动作就停止判断
        if (trigger == null) return

        val itemTag = itemInfo.itemTag
        val data = itemInfo.data

        // 检测冷却
        val tick = trigger.tick?.parseItemSection(itemStack, itemTag, data, player)?.toLongOrNull() ?: 1000
        // 如果冷却存在且大于0
        if (tick > 0) {
            // 获取上次使用时间
            val lastTick = player.getMetadataEZ("NI-TICK-${trigger.group}", "Long", 0.toLong()) as Long
            // 如果仍处于冷却时间
            if (lastTick > 0) {
                player.setMetadataEZ("NI-TICK-${trigger.group}", lastTick - 1)
                return
            } else {
                player.setMetadataEZ("NI-TICK-${trigger.group}", tick)
            }
        }
        // 执行动作
        trigger.run(player, itemStack, itemTag, data, null, HashMap<String, Any?>())
    }
}
