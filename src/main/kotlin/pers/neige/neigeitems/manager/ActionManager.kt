package pers.neige.neigeitems.manager

import org.bukkit.Bukkit
import org.bukkit.Bukkit.isPrimaryThread
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.NeigeItems.bukkitScheduler
import pers.neige.neigeitems.NeigeItems.plugin
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
import pers.neige.neigeitems.utils.ActionUtils.consumeAndReturn
import pers.neige.neigeitems.utils.ActionUtils.isCoolDown
import pers.neige.neigeitems.utils.ConfigUtils
import pers.neige.neigeitems.utils.ItemUtils.isNiItem
import pers.neige.neigeitems.utils.PlayerUtils.setMetadataEZ
import pers.neige.neigeitems.utils.SectionUtils.parseItemSection
import pers.neige.neigeitems.utils.SectionUtils.parseSection
import pers.neige.neigeitems.utils.StringUtils.splitOnce
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.nms.ItemTag
import taboolib.module.nms.getItemTag
import taboolib.platform.util.giveItem
import java.io.File
import java.io.InputStreamReader
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.function.BiFunction
import javax.script.CompiledScript
import javax.script.SimpleBindings
import pers.neige.neigeitems.utils.StringUtils.split
import taboolib.platform.util.sendActionBar

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
    val engine = nashornHooker.getGlobalEngine()

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
        // 加载顶级成员
        try {
            plugin.getResource("JavaScriptLib/lib.js")?.let { engine.eval(InputStreamReader(it, "UTF-8")) }
        } catch (error: Throwable) {
            error.printStackTrace()
        }
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
     * @param start 动作从第几个开始执行(默认为0)
     * @param end 动作执行到第几个结束
     * @param itemStack 用于解析条件, 可为空
     * @param itemTag 用于解析nbt及data, 可为空
     * @param data 物品节点数据
     * @param event 用于解析条件, 可为空
     * @param global 用于存储整个动作运行过程中的全局变量
     * @param map 传入js的顶级变量
     */
    fun runAction(
        player: Player,
        action: List<*>,
        itemStack: ItemStack? = null,
        itemTag: ItemTag? = itemStack?.getItemTag(),
        data: HashMap<String, String>? = null,
        event: Event? = null,
        global: HashMap<String, Any?>,
        start: Int = 0,
        end: Int = action.size,
        map: Map<String, Any?>? = null
    ) {
        // 动作执行延迟
        var delay = 0L

        // 动作执行起止
        val actionStart = start.coerceAtLeast(0).coerceAtMost(action.size)
        val actionEnd = end.coerceAtLeast(actionStart).coerceAtMost(action.size)

        // 遍历所有动作
        for (index in actionStart until actionEnd) {
            // 延迟执行
            if (delay > 0) {
                // 线程判断
                if (isPrimaryThread()) {
                    bukkitScheduler.runTaskLater(plugin, Runnable {
                        runAction(player, action, itemStack, itemTag, data, event, global, index, actionEnd, map)
                    }, delay)
                } else {
                    bukkitScheduler.runTaskLaterAsynchronously(plugin, Runnable {
                        runAction(player, action, itemStack, itemTag, data, event, global, index, actionEnd, map)
                    }, delay)
                }
                // 停止当前操作
                break
            }

            // 动作内容
            val value = action[index]

            // 如果属于一条文本
            if (value is String) {
                // 解析物品变量
                val actionString = when (itemTag) {
                    null -> value.parseSection(
                        map?.get("cache") as HashMap<String, String>?,
                        player,
                        map?.get("sections") as ConfigurationSection?,
                    )
                    else -> value.parseItemSection(
                        itemStack!!,
                        itemTag,
                        data,
                        player,
                        map?.get("cache") as HashMap<String, String>?,
                        map?.get("sections") as ConfigurationSection?
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
                    else -> if (!runAction(player, actionType, actionContent, itemStack, itemTag, data, event, global, map)) break
                }
                // 如果属于其他类型
            } else {
                // 直接执行
                if (!runAction(player, value, itemStack, itemTag, data, event, global, map)) break
            }
        }
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
     */
    fun runAction(
        player: Player,
        action: List<*>,
        itemStack: ItemStack? = null,
        itemTag: ItemTag? = itemStack?.getItemTag(),
        data: HashMap<String, String>? = null,
        event: Event? = null,
        global: HashMap<String, Any?>,
        map: Map<String, Any?>? = null
    ) {
        runAction(player, action, itemStack, itemTag, data, event, global, 0, action.size, map)
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
     * @param action 动作文本
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
        action: String,
        itemStack: ItemStack? = null,
        itemTag: ItemTag? = itemStack?.getItemTag(),
        data: HashMap<String, String>? = null,
        event: Event? = null,
        global: HashMap<String, Any?>,
        map: Map<String, Any?>? = null
    ): Boolean {
        // 解析物品变量
        val actionString = when (itemTag) {
            null -> action.parseSection(
                map?.get("cache") as HashMap<String, String>?,
                player,
                map?.get("sections") as ConfigurationSection?,
            )
            else -> action.parseItemSection(
                itemStack!!,
                itemTag,
                data,
                player,
                map?.get("cache") as HashMap<String, String>?,
                map?.get("sections") as ConfigurationSection?
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
    fun runAction(
        player: Player,
        actionType: String,
        actionContent: String,
        itemStack: ItemStack? = null,
        itemTag: ItemTag? = itemStack?.getItemTag(),
        data: HashMap<String, String>? = null,
        event: Event? = null,
        global: HashMap<String, Any?>,
        map: Map<String, Any?>? = null
    ): Boolean {
        val lowercaseActionType = actionType.lowercase(Locale.getDefault())
        actions[lowercaseActionType]?.apply(player, actionContent)?.also { return it }
        itemStack?.also { runEditorWithResult(actionType, actionContent, itemStack, player)?.also { return it } }
        when (lowercaseActionType) {
            "js" -> {
                // 动作中调用的顶级变量
                val bindings = SimpleBindings()
                map?.forEach { (key, value) ->
                    value?.let { bindings[key] = it }
                }
                bindings["variables"] = HashMap<String, Any?>()
                player.let { bindings["player"] = it }
                itemStack?.let { bindings["itemStack"] = it }
                itemTag?.let { bindings["itemTag"] = it }
                data?.let { bindings["data"] = it }
                event?.let { bindings["event"] = it }
                bindings["global"] = global
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
        action: ConfigurationSection,
        itemStack: ItemStack? = null,
        itemTag: ItemTag? = itemStack?.getItemTag(),
        data: HashMap<String, String>? = null,
        event: Event? = null,
        global: HashMap<String, Any?>,
        map: Map<String, Any?>? = null
    ): Boolean {
        // 动作执行条件
        val condition: String? = action.getString("condition")
        // 动作内容
        val actions = action.get("actions")
        // 不满足条件时执行的内容
        val deny = action.get("deny")

        // 如果没有条件或者条件通过
        if (parseCondition(condition, player, itemStack, itemTag, data, event, global, map)) {
            // 执行动作
            return runAction(player, actions, itemStack, itemTag, data, event, global, map)
            // 条件未通过
        } else {
            // 执行deny动作
            return runAction(player, deny, itemStack, itemTag, data, event, global, map)
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
    fun runAction(
        player: Player,
        action: LinkedHashMap<String, *>,
        itemStack: ItemStack? = null,
        itemTag: ItemTag? = itemStack?.getItemTag(),
        data: HashMap<String, String>? = null,
        event: Event? = null,
        global: HashMap<String, Any?>,
        map: Map<String, Any?>? = null
    ): Boolean {
        // 动作执行条件
        val condition: String? = action["condition"] as String?
        // 动作内容
        val actions = action["actions"]
        // 不满足条件时执行的内容
        val deny = action["deny"]

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
        itemTag: ItemTag? = itemStack?.getItemTag(),
        data: HashMap<String, String>? = null,
        event: Event? = null,
        global: HashMap<String, Any?> = HashMap<String, Any?>(),
        map: Map<String, Any?>? = null
    ): Boolean {
        when (action) {
            is String -> return runAction(player, action, itemStack, itemTag, data, event, global, map)
            is List<*> -> runAction(player, action, itemStack, itemTag, data, event, global, 0, action.size, map)
            is LinkedHashMap<*, *> -> return runAction(player, action as LinkedHashMap<String, *>, itemStack, itemTag, data, event, global, map)
            is ConfigurationSection -> return runAction(player, action, itemStack, itemTag, data, event, global, map)
        }
        return true
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
        itemTag: ItemTag? = itemStack?.getItemTag(),
        data: HashMap<String, String>? = null,
        event: Event? = null,
        global: HashMap<String, Any?> = HashMap<String, Any?>(),
        map: Map<String, Any?>? = null
    ): Boolean {
        val pass = condition?.let {
            // 条件中调用的顶级变量
            val bindings = SimpleBindings()
            map?.forEach { (key, value) ->
                value?.let { bindings[key] = it }
            }
            bindings["variables"] = HashMap<String, Any?>()
            player?.let { bindings["player"] = it }
            itemStack?.let { bindings["itemStack"] = it }
            itemTag?.let { bindings["itemTag"] = it }
            data?.let { bindings["data"] = it }
            event?.let { bindings["event"] = it }
            bindings["global"] = global
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
            runThreadSafe {
                player.chat(papi(player, string))
            }
            true
        }
        // 强制玩家发送消息(将&解析为颜色符号)
        addAction("chatWithColor") { player, string ->
            runThreadSafe {
                player.chat(papiColor(player, string))
            }
            true
        }
        // 强制玩家执行指令
        addAction("command") { player, string ->
            runThreadSafe {
                Bukkit.dispatchCommand(player, papiColor(player, string))
            }
            true
        }
        // 强制玩家执行指令
        actions["command"]?.let { addAction("player", it) }
        // 强制玩家执行指令(不将&解析为颜色符号)
        addAction("commandNoColor") { player, string ->
            runThreadSafe {
                Bukkit.dispatchCommand(player, papi(player, string))
            }
            true
        }
        // 后台执行指令
        addAction("console") { player, string ->
            runThreadSafe {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), papiColor(player, string))
            }
            true
        }
        // 后台执行指令(不将&解析为颜色符号)
        addAction("consoleNoColor") { player, string ->
            runThreadSafe {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), papi(player, string))
            }
            true
        }
        // 发送Title
        addAction("title") { player, string ->
            runThreadSafe {
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
            runThreadSafe {
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
            runThreadSafe {
                player.sendActionBar(papiColor(player, string))
            }
            true
        }
        // 发送ActionBar(不将&解析为颜色符号)
        addAction("actionBarNoColor") { player, string ->
            runThreadSafe {
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
            runThreadSafe {
                player.giveExp(papi(player, string).toIntOrNull() ?: 0)
            }
            true
        }
        // 扣除玩家经验
        addAction("takeExp") { player, string ->
            runThreadSafe {
                player.giveExp((papi(player, string).toIntOrNull() ?: 0) * -1)
            }
            true
        }
        // 设置玩家经验
        addAction("setExp") { player, string ->
            runThreadSafe {
                player.totalExperience = papi(player, string).toIntOrNull() ?: 0
            }
            true
        }
        // 给予玩家经验等级
        addAction("giveLevel") { player, string ->
            runThreadSafe {
                player.giveExpLevels(papi(player, string).toIntOrNull() ?: 0)
            }
            true
        }
        // 扣除玩家经验等级
        addAction("takeLevel") { player, string ->
            runThreadSafe {
                player.giveExpLevels((papi(player, string).toIntOrNull() ?: 0) * -1)
            }
            true
        }
        // 设置玩家经验等级
        addAction("setLevel") { player, string ->
            runThreadSafe {
                player.level = papi(player, string).toIntOrNull() ?: 0
            }
            true
        }
        // 给予玩家饱食度
        addAction("giveFood") { player, string ->
            runThreadSafe {
                player.foodLevel = (player.foodLevel + (papi(player, string).toIntOrNull() ?: 0)).coerceAtLeast(0).coerceAtMost(20)
            }
            true
        }
        // 扣除玩家饱食度
        addAction("takeFood") { player, string ->
            runThreadSafe {
                player.foodLevel = (player.foodLevel - (papi(player, string).toIntOrNull() ?: 0)).coerceAtLeast(0).coerceAtMost(20)
            }
            true
        }
        // 设置玩家饱食度
        addAction("setFood") { player, string ->
            runThreadSafe {
                player.foodLevel = (papi(player, string).toIntOrNull() ?: 0).coerceAtLeast(0).coerceAtMost(20)
            }
            true
        }
        // 给予玩家饱和度
        addAction("giveSaturation") { player, string ->
            runThreadSafe {
                player.saturation = (player.saturation + (papi(player, string).toFloatOrNull() ?: 0F)).coerceAtLeast(0F).coerceAtMost(player.foodLevel.toFloat())
            }
            true
        }
        // 扣除玩家饱和度
        addAction("takeSaturation") { player, string ->
            runThreadSafe {
                player.saturation = (player.saturation - (papi(player, string).toFloatOrNull() ?: 0F)).coerceAtLeast(0F).coerceAtMost(player.foodLevel.toFloat())
            }
            true
        }
        // 设置玩家饱和度
        addAction("setSaturation") { player, string ->
            runThreadSafe {
                player.saturation = (papi(player, string).toFloatOrNull() ?: 0F).coerceAtLeast(0F).coerceAtMost(player.foodLevel.toFloat())
            }
            true
        }
        // 给予玩家生命
        addAction("giveHealth") { player, string ->
            runThreadSafe {
                player.health = (player.health + (papi(player, string).toDoubleOrNull() ?: 0.toDouble())).coerceAtMost(player.maxHealth)
            }
            true
        }
        // 扣除玩家生命
        addAction("takeHealth") { player, string ->
            runThreadSafe {
                player.health = (player.health - (papi(player, string).toDoubleOrNull() ?: 0.toDouble())).coerceAtLeast(0.toDouble())
            }
            true
        }
        // 设置玩家生命
        addAction("setHealth") { player, string ->
            runThreadSafe {
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
        // 类型不对劲/物品为空则终止操作
        if (event.action == Action.PHYSICAL || itemStack == null) return
        // 物品NBT
        val itemTag: ItemTag
        // NI物品数据
        val neigeItems: ItemTag
        // NI物品id
        val id: String
        // NI节点数据
        val data: HashMap<String, String>?
        // 初始化NI物品数据
        when (val itemInfo = itemStack.isNiItem(true)) {
            // 不是NI物品, 终止操作
            null -> return
            else -> {
                itemTag = itemInfo.itemTag
                neigeItems = itemInfo.neigeItems
                id = itemInfo.id
                data = itemInfo.data
            }
        }
        // 获取物品动作
        val itemAction = itemActions[id] ?: let { return }
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
            val amount: Int = consume.getString("amount")?.parseItemSection(itemStack, itemTag, data, player)?.toIntOrNull() ?: 1
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
    @SubscribeEvent(priority = EventPriority.LOWEST, ignoreCancelled = true)
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
        // NI节点数据
        val data: HashMap<String, String>?
        when (val itemInfo = itemStack.isNiItem(true)) {
            null -> return
            else -> {
                itemTag = itemInfo.itemTag
                neigeItems = itemInfo.neigeItems
                id = itemInfo.id
                data = itemInfo.data
            }
        }
        // 获取物品动作
        val itemAction = itemActions[id] ?: let { return }
        // 获取基础触发器
        val trigger = itemAction.triggers["eat"]
        // 没有对应物品动作就停止判断
        if (trigger == null) return

        // 获取物品消耗信息
        val consume =  trigger.consume
        // 取消事件
        event.isCancelled = true
        // 检测冷却
        if (trigger.isCoolDown(player, itemStack, itemTag, data)) return
        // 用于存储整个动作执行过程中的全局变量
        val global = HashMap<String, Any?>()
        // 如果该物品需要被消耗
        if (consume != null) {
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
            val amount: Int = consume.getString("amount")?.parseItemSection(itemStack, itemTag, data, player)?.toIntOrNull() ?: 1
            // 消耗物品
            itemStack.consumeAndReturn(amount, itemTag, neigeItems)?.also { itemStacks ->
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
            } ?: let {
                // 跑一下deny动作
                runAction(player, consume.get("deny"), itemStack, itemTag, data, event, global)
                // 数量不足
                return
            }
        }
        // 执行动作
        trigger.run(player, itemStack, itemTag, data, event, global)
    }

    // 丢弃物品
    @SubscribeEvent(priority = EventPriority.LOWEST, ignoreCancelled = true)
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
        // NI节点数据
        val data: HashMap<String, String>?
        when (val itemInfo = itemStack.isNiItem(true)) {
            null -> return
            else -> {
                itemTag = itemInfo.itemTag
                neigeItems = itemInfo.neigeItems
                id = itemInfo.id
                data = itemInfo.data
            }
        }
        // 获取物品动作
        val itemAction = itemActions[id] ?: let { return }
        // 获取基础触发器
        val trigger = itemAction.triggers["drop"]
        // 没有对应物品动作就停止判断
        if (trigger == null) return

        // 获取物品消耗信息
        val consume =  trigger.consume
        // 检测冷却
        if (trigger.isCoolDown(player, itemStack, itemTag, data)) {
            event.isCancelled = true
            return
        }
        // 用于存储整个动作执行过程中的全局变量
        val global = HashMap<String, Any?>()
        // 如果该物品需要被消耗
        if (consume != null) {
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
            val amount: Int = consume.getString("amount")?.parseItemSection(itemStack, itemTag, data, player)?.toIntOrNull() ?: 1
            // 消耗物品
            if (!itemStack.consume(player, amount, itemTag, neigeItems)) {
                // 跑一下deny动作
                runAction(player, consume.get("deny"), itemStack, itemTag, data, event, global)
                // 数量不足
                return
            }
        }
        // 执行动作
        trigger.run(player, itemStack, itemTag, data, event, global)
        // 应用consume/action对itemStack的操作
        if (itemStack.amount == 0 || itemStack.type == Material.AIR) {
            event.itemDrop.remove()
            // 就让Item保持AIR会导致后面监听事件的插件报错, 不如干脆取消事件算了
            event.isCancelled = true
        } else {
            event.itemDrop.itemStack = itemStack
        }
    }

    // 拾取物品
    @SubscribeEvent(priority = EventPriority.HIGH, ignoreCancelled = true)
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
        // NI节点数据
        val data: HashMap<String, String>?
        when (val itemInfo = itemStack.isNiItem(true)) {
            null -> return
            else -> {
                itemTag = itemInfo.itemTag
                neigeItems = itemInfo.neigeItems
                id = itemInfo.id
                data = itemInfo.data
            }
        }
        // 获取物品动作
        val itemAction = itemActions[id] ?: let { return }
        // 获取基础触发器
        val trigger = itemAction.triggers["pick"]
        // 没有对应物品动作就停止判断
        if (trigger == null) return

        // 获取物品消耗信息
        val consume =  trigger.consume
        // 检测冷却
        if (trigger.isCoolDown(player, itemStack, itemTag, data)) {
            event.isCancelled = true
            return
        }
        // 用于存储整个动作执行过程中的全局变量
        val global = HashMap<String, Any?>()
        // 如果该物品需要被消耗
        if (consume != null) {
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
            val amount: Int = consume.getString("amount")?.parseItemSection(itemStack, itemTag, data, player)?.toIntOrNull() ?: 1
            // 消耗物品
            if (!itemStack.consume(player, amount, itemTag, neigeItems)) {
                // 跑一下deny动作
                runAction(player, consume.get("deny"), itemStack, itemTag, data, event, global)
                // 数量不足
                return
            }
        }
        // 执行动作
        trigger.run(player, itemStack, itemTag, data, event, global)
        // 应用consume/action对itemStack的操作
        if (itemStack.amount == 0 || itemStack.type == Material.AIR) {
            event.item.remove()
            // 就让Item保持AIR会导致后面监听事件的插件报错, 不如干脆取消事件算了
            event.isCancelled = true
        } else {
            event.item.itemStack = itemStack
        }
    }

    // 点击物品
    @SubscribeEvent(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun listener(event: InventoryClickEvent) {
        // 获取玩家
        val player = event.whoClicked
        if (player !is Player) return
        // 获取点击物品
        val itemStack = event.cursor
        // 物品NBT
        val itemTag: ItemTag
        // NI物品数据
        val neigeItems: ItemTag
        // NI物品id
        val id: String
        // NI节点数据
        val data: HashMap<String, String>?
        when (val itemInfo = itemStack?.isNiItem(true)) {
            null -> return
            else -> {
                itemTag = itemInfo.itemTag
                neigeItems = itemInfo.neigeItems
                id = itemInfo.id
                data = itemInfo.data
            }
        }
        // 获取物品动作
        val itemAction = itemActions[id] ?: let { return }
        // 获取基础触发器
        val trigger = itemAction.triggers["click"]
        // 没有对应物品动作就停止判断
        if (trigger == null) return

        // 获取物品消耗信息
        val consume =  trigger.consume
        // 取消交互事件
        event.isCancelled = true
        // 检测冷却
        if (trigger.isCoolDown(player, itemStack, itemTag, data)) return
        // 用于存储整个动作执行过程中的全局变量
        val global = HashMap<String, Any?>()
        // 如果该物品需要被消耗
        if (consume != null) {
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
            val amount: Int = consume.getString("amount")?.parseItemSection(itemStack, itemTag, data, player)?.toIntOrNull() ?: 1
            // 消耗物品
            if (!itemStack.consume(player, amount, itemTag, neigeItems)) {
                // 跑一下deny动作
                runAction(player, consume.get("deny"), itemStack, itemTag, data, event, global)
                // 数量不足
                return
            }
        }
        // 执行动作
        trigger.run(player, itemStack, itemTag, data, event, global)
    }

    // 物品被点击
    @SubscribeEvent(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun beClickedListener(event: InventoryClickEvent) {
        // 获取玩家
        val player = event.whoClicked
        if (player !is Player) return
        // 获取点击物品
        val itemStack = event.currentItem
        // 物品NBT
        val itemTag: ItemTag
        // NI物品数据
        val neigeItems: ItemTag
        // NI物品id
        val id: String
        // NI节点数据
        val data: HashMap<String, String>?
        when (val itemInfo = itemStack?.isNiItem(true)) {
            null -> return
            else -> {
                itemTag = itemInfo.itemTag
                neigeItems = itemInfo.neigeItems
                id = itemInfo.id
                data = itemInfo.data
            }
        }
        // 获取物品动作
        val itemAction = itemActions[id] ?: let { return }
        // 获取基础触发器
        val trigger = itemAction.triggers["beclicked"]
        // 没有对应物品动作就停止判断
        if (trigger == null) return

        // 获取物品消耗信息
        val consume =  trigger.consume
        // 取消交互事件
        event.isCancelled = true
        // 检测冷却
        if (trigger.isCoolDown(player, itemStack, itemTag, data)) return
        // 用于存储整个动作执行过程中的全局变量
        val global = HashMap<String, Any?>()
        // 如果该物品需要被消耗
        if (consume != null) {
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
            val amount: Int = consume.getString("amount")?.parseItemSection(itemStack, itemTag, data, player)?.toIntOrNull() ?: 1
            // 消耗物品
            if (!itemStack.consume(player, amount, itemTag, neigeItems)) {
                // 跑一下deny动作
                runAction(player, consume.get("deny"), itemStack, itemTag, data, event, global)
                // 数量不足
                return
            }
        }
        // 执行动作
        trigger.run(player, itemStack, itemTag, data, event, global)
    }

    private fun runThreadSafe(task: Runnable) {
        if (isPrimaryThread()) {
            task.run()
        } else {
            bukkitScheduler.runTask(plugin, task)
        }
    }
}
