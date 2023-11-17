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
import pers.neige.neigeitems.NeigeItems.plugin
import pers.neige.neigeitems.action.ActionContext
import pers.neige.neigeitems.action.ActionResult
import pers.neige.neigeitems.action.ResultType
import pers.neige.neigeitems.action.impl.StringAction
import pers.neige.neigeitems.action.result.DelayResult
import pers.neige.neigeitems.action.result.Results
import pers.neige.neigeitems.item.ItemInfo
import pers.neige.neigeitems.item.action.ItemAction
import pers.neige.neigeitems.manager.ConfigManager.config
import pers.neige.neigeitems.utils.ActionUtils.consume
import pers.neige.neigeitems.utils.ActionUtils.isCoolDown
import pers.neige.neigeitems.utils.ConfigUtils
import pers.neige.neigeitems.utils.ItemUtils.getNbt
import pers.neige.neigeitems.utils.PlayerUtils.getMetadataEZ
import pers.neige.neigeitems.utils.PlayerUtils.setMetadataEZ
import pers.neige.neigeitems.utils.SchedulerUtils.*
import pers.neige.neigeitems.utils.SectionUtils.parseItemSection
import pers.neige.neigeitems.utils.SectionUtils.parseSection
import java.io.File
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.function.BiFunction

/**
 * 用于管理所有物品动作、所有拥有物品动作的物品及相关动作、监听相关事件做到动作触发
 */
object ActionManager : BaseActionManager(plugin) {
    /**
     * 获取拥有动作的物品ID及相关动作
     */
    val itemActions: ConcurrentHashMap<String, ItemAction> = ConcurrentHashMap<String, ItemAction>()

    init {
        try {
            plugin.getResource("JavaScriptLib/lib.js")?.use { input ->
                InputStreamReader(input, StandardCharsets.UTF_8)
                    .use { reader -> engine.eval(reader) }
            }
        } catch (error: Throwable) {
            error.printStackTrace()
        }
        // 加载所有拥有动作的物品及相关动作
        loadItemActions()
        // 加载自定义动作
        loadCustomActions()
    }

    /**
     * 重载物品动作管理器
     */
    override fun reload() {
        super.reload()
        itemActions.clear()
        loadItemActions()
        loadCustomActions()
    }

    /**
     * 执行物品动作
     *
     * @param action 动作内容
     * @return 执行结果
     */
    @Deprecated("使用BaseActionManager中的方法代替")
    fun runAction(
        action: Any?
    ): ActionResult {
        return runAction(action, ActionContext.empty())
    }

    /**
     * 执行物品动作
     *
     * @param action  动作内容
     * @param context 动作上下文
     * @return 执行结果
     */
    @Deprecated("使用BaseActionManager中的方法代替")
    fun runAction(
        action: Any?,
        context: ActionContext
    ): ActionResult {
        when (action) {
            is String -> return runAction(action, context)
            is List<*> -> return runAction(action, context)
            is Map<*, *> -> return runAction(action as Map<String, *>, context)
            is ConfigurationSection -> return runAction(action, context)
        }
        return Results.SUCCESS
    }

    /**
     * 执行物品动作
     *
     * @param action 动作内容
     * @param context 动作上下文
     * @return 执行结果
     */
    @Deprecated("使用BaseActionManager中的方法代替")
    private fun runAction(
        action: List<*>,
        context: ActionContext
    ): ActionResult {
        action.forEachIndexed { index, value ->
            val result = runAction(value, context)
            when (result.type) {
                ResultType.DELAY -> {
                    runLater((result as DelayResult).delay.toLong()) {
                        runAction(action.subList(index + 1, action.lastIndex), context)
                    }
                    return Results.SUCCESS
                }
                ResultType.STOP -> return result
                else -> {}
            }
        }
        return Results.SUCCESS
    }

    /**
     * 执行物品动作
     *
     * @param action 动作文本
     * @param context 动作上下文
     * @return 执行结果
     */
    @Deprecated("使用BaseActionManager中的方法代替")
    private fun runAction(
        action: String,
        context: ActionContext
    ): ActionResult {
        // 解析物品变量
        val itemStack = context.itemStack
        val nbt = context.nbt
        val cache = (context.params?.get("cache") ?: context.global) as? MutableMap<String, String>
        val sections = context.params?.get("sections") as? ConfigurationSection
        val actionString = if (itemStack != null && nbt != null) {
            action.parseItemSection(
                itemStack,
                nbt,
                context.data,
                context.player,
                cache,
                sections
            )
        } else {
            action.parseSection(
                cache,
                context.player,
                sections
            )
        }
        // 解析动作类型及动作内容
        val info = actionString.split(": ", limit = 2)
        val type = info[0]
        val content = info.getOrNull(1) ?: ""
        // 尝试加载物品动作, 返回执行结果
        actions[type.lowercase(Locale.getDefault())]?.apply(context, content)?.also { return it }
        // 尝试加载物品编辑函数, 返回执行结果
        val player = context.player
        if (itemStack != null && player != null) {
            return Results.fromBoolean(
                ItemEditorManager.runEditorWithResult(type, content, itemStack, player) ?: return Results.SUCCESS
            )
        }
        return Results.SUCCESS
    }

    /**
     * 执行物品动作
     *
     * @param action 动作内容
     * @param context 动作上下文
     * @return 执行结果
     */
    @Deprecated("使用BaseActionManager中的方法代替")
    private fun runAction(
        action: ConfigurationSection,
        context: ActionContext
    ): ActionResult {
        // 动作执行条件
        val condition: String? = action.getString("condition")
        // 循环执行条件
        val whileCondition: String? = action.getString("while")
        // 动作内容
        val actions = action.get("actions")
        // 强制异步动作内容
        val async = action.get("async")
        // 强制同步动作内容
        val sync = action.get("sync")
        // 结束动作内容
        val finally = action.get("finally")
        // 不满足条件时执行的内容
        val deny = action.get("deny")
        // 模式检测
        when {
            // condition模式
            condition != null -> {
                // 如果条件通过
                if (parseCondition(condition, context).type == ResultType.SUCCESS) {
                    // 执行动作
                    runAction(sync, async, context)
                    return runAction(actions, context)
                    // 条件未通过
                } else {
                    // 执行deny动作
                    return runAction(deny, context)
                }
            }
            // while模式
            whileCondition != null -> {
                while (parseCondition(whileCondition, context).type == ResultType.SUCCESS) {
                    // 执行动作
                    runAction(sync, async, context)
                    val result = runAction(actions, context)
                    if (result.type == ResultType.STOP) {
                        runAction(finally, context)
                        return Results.STOP
                    }
                }
                runAction(finally, context)
                return Results.SUCCESS
            }
            else -> {
                runAction(sync, async, context)
                return runAction(actions, context)
            }
        }
    }

    /**
     * 执行物品动作
     *
     * @param action 动作内容
     * @param context 动作上下文
     * @return 执行结果
     */
    @Deprecated("使用BaseActionManager中的方法代替")
    private fun runAction(
        action: Map<String, *>,
        context: ActionContext
    ): ActionResult {
        // 动作执行条件
        val condition: String? = action["condition"] as String?
        // 循环执行条件
        val whileCondition: String? = action["while"] as String?
        // 动作内容
        val actions = action["actions"]
        // 强制异步动作内容
        val async = action["async"]
        // 强制同步动作内容
        val sync = action["sync"]
        // 结束动作内容
        val finally = action["finally"]
        // 不满足条件时执行的内容
        val deny = action["deny"]
        // 模式检测
        when {
            // condition模式
            condition != null -> {
                // 如果条件通过
                if (parseCondition(condition, context).type == ResultType.SUCCESS) {
                    // 执行动作
                    runAction(sync, async, context)
                    return runAction(actions, context)
                    // 条件未通过
                } else {
                    // 执行deny动作
                    return runAction(deny, context)
                }
            }
            // while模式
            whileCondition != null -> {
                while (parseCondition(whileCondition, context).type == ResultType.SUCCESS) {
                    // 执行动作
                    runAction(sync, async, context)
                    val result = runAction(actions, context)
                    if (result.type == ResultType.STOP) {
                        runAction(finally, context)
                        return Results.STOP
                    }
                }
                runAction(finally, context)
                return Results.SUCCESS
            }
            else -> {
                runAction(sync, async, context)
                return runAction(actions, context)
            }
        }
    }

    @Deprecated("使用BaseActionManager中的方法代替")
    private fun runAction(
        sync: Any?,
        async: Any?,
        context: ActionContext
    ) {
        if (Bukkit.isPrimaryThread()) {
            runAction(sync, context)
            async(plugin) {
                runAction(async, context)
            }
        } else {
            sync(plugin) {
                runAction(sync, context)
            }
            runAction(async, context)
        }
    }

    /**
     * 执行物品动作
     *
     * @param player 执行玩家
     * @param action 动作文本
     * @return 是否继续执行(执行List<String>中的物品动作时, 某个动作返回false则终止动作执行)
     */
    @Deprecated("使用BaseActionManager中的方法代替")
    fun runAction(
        player: Player,
        action: String
    ): Boolean {
        val result = runAction(StringAction(action), ActionContext(player))
        return when (result.type) {
            ResultType.STOP -> false
            else -> true
        }
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
    @Deprecated("使用BaseActionManager中的方法代替")
    fun runAction(
        player: Player,
        action: Any?,
        global: MutableMap<String, Any?> = HashMap(),
        map: Map<String, Any?>? = null
    ): Boolean {
        val result = runAction(action, ActionContext(player, global, map))
        return when (result.type) {
            ResultType.STOP -> false
            else -> true
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
    @Deprecated("使用BaseActionManager中的方法代替")
    fun runAction(
        player: Player,
        action: Any?,
        itemStack: ItemStack? = null,
        itemTag: NbtCompound? = itemStack?.let { itemStack.getNbt() },
        data: MutableMap<String, String>? = null,
        event: Event? = null,
        global: MutableMap<String, Any?> = HashMap(),
        map: Map<String, Any?>? = null
    ): Boolean {
        val result = runAction(action, ActionContext(player, global, map, itemStack, itemTag, data, event))
        return when (result.type) {
            ResultType.STOP -> false
            else -> true
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
    @Deprecated("使用BaseActionManager中的方法代替")
    fun parseCondition(
        condition: String?,
        player: Player?,
        global: MutableMap<String, Any?> = HashMap(),
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
    @Deprecated("使用BaseActionManager中的方法代替")
    fun parseCondition(
        condition: String?,
        player: Player?,
        itemStack: ItemStack? = null,
        itemTag: NbtCompound? = itemStack?.let { itemStack.getNbt() },
        data: MutableMap<String, String>? = null,
        event: Event? = null,
        global: MutableMap<String, Any?> = HashMap(),
        map: Map<String, Any?>? = null
    ): Boolean {
        val result = parseCondition(condition, ActionContext(player, global, map, itemStack, itemTag, data, event))
        return when (result.type) {
            ResultType.STOP -> false
            else -> true
        }
    }

    /**
     * 添加物品动作
     *
     * @param id 动作ID
     * @param function 动作执行函数
     */
    fun addAction(id: String, function: BiFunction<Player, String, Boolean>) {
        addFunction(id, BiFunction { context, content ->
            val player = context.player
            if (player != null) {
                return@BiFunction Results.fromBoolean(function.apply(player, content))
            }
            return@BiFunction Results.SUCCESS
        })
    }

    override fun runAction(
        action: StringAction,
        context: ActionContext
    ): ActionResult {
        // 解析物品变量
        val itemStack = context.itemStack
        val nbt = context.nbt
        val cache = (context.params?.get("cache") ?: context.global) as? MutableMap<String, String>
        val sections = context.params?.get("sections") as? ConfigurationSection
        val actionString = if (itemStack != null && nbt != null) {
            action.action.parseItemSection(
                itemStack,
                nbt,
                context.data,
                context.player,
                cache,
                sections
            )
        } else {
            action.action.parseSection(
                cache,
                context.player,
                sections
            )
        }
        // 解析动作类型及动作内容
        val info = actionString.split(": ", limit = 2)
        val type = info[0]
        val content = info.getOrNull(1) ?: ""
        // 尝试加载物品动作, 返回执行结果
        actions[type.lowercase(Locale.getDefault())]?.apply(context, content)?.also { return it }
        // 尝试加载物品编辑函数, 返回执行结果
        val player = context.player
        if (itemStack != null && player != null) {
            return Results.fromBoolean(
                ItemEditorManager.runEditorWithResult(type, content, itemStack, player) ?: return Results.SUCCESS
            )
        }
        return Results.SUCCESS
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
                    || (id == "all" && consume?.getBoolean("left") == true || consume?.getBoolean("right") == true)
                ) {
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
    @Deprecated("现在建议使用 Expansion 进行自定义动作注册")
    private fun loadCustomActions() {
        for (file in ConfigUtils.getAllFiles("CustomActions")) {
            // 仅加载.js文件
            if (!file.name.endsWith(".js")) continue
            // 防止某个脚本出错导致加载中断
            try {
                val script = pers.neige.neigeitems.script.CompiledScript(file)
                script.invoke("main", null)
            } catch (_: Throwable) {
            }
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
        // 动作上下文
        val context = ActionContext(player, global, null, itemStack, itemTag, data, event)
        // 如果物品需要消耗
        if (consume != null) {
            // 预执行动作
            runAction(consume.pre, context)
            // 检测条件
            consume.condition?.let {
                // 不满足条件就爬
                if (parseCondition(it, context).type == ResultType.STOP) {
                    // 跑一下deny动作
                    runAction(consume.deny, context)
                    // 爬
                    return
                }
            }
            // 获取待消耗数量
            val amount: Int = consume.amount
                ?.parseItemSection(itemStack, itemTag, data, player, global as? MutableMap<String, String>, null)
                ?.toIntOrNull() ?: 1
            // 消耗物品
            if (!itemStack.consume(player, amount, itemTag, neigeItems)) {
                // 跑一下deny动作
                runAction(consume.deny, context)
                // 数量不足
                return
            }
        }
        // 执行动作
        basicTrigger?.run(context)
        allTrigger?.run(context)
    }

    // 吃或饮用
    fun eatListener(
        player: Player,
        itemStack: ItemStack,
        itemInfo: ItemInfo,
        event: PlayerItemConsumeEvent
    ) {
        basicHandler(
            player,
            itemStack,
            itemInfo,
            event,
            "eat",
            cancel = true,
            cancelIfCooldown = false,
            giveLater = true
        )
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
        basicHandler(
            player,
            itemStack,
            itemInfo,
            event,
            "pick",
            cancel = false,
            cancelIfCooldown = true,
            consumeItem = false
        )
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
        basicHandler(
            player,
            itemStack,
            itemInfo,
            event,
            "shoot_bow",
            cancel = false,
            cancelIfCooldown = true,
            consumeItem = false
        )
    }

    // 射箭时由箭触发
    fun shootArrowListener(
        player: Player,
        itemStack: ItemStack,
        itemInfo: ItemInfo,
        event: EntityShootBowEvent
    ) {
        basicHandler(
            player,
            itemStack,
            itemInfo,
            event,
            "shoot_arrow",
            cancel = false,
            cancelIfCooldown = true,
            consumeItem = false
        )
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
        // 动作上下文
        val context = ActionContext(player, global, null, itemStack, itemTag, data, event)
        if (consumeItem) {
            // 获取物品消耗信息
            val consume = trigger.consume
            // 如果该物品需要被消耗
            if (consume != null) {
                // 预执行动作
                runAction(consume.pre, context)
                // 检测条件
                consume.condition?.let {
                    // 不满足条件就爬
                    if (parseCondition(it, context).type == ResultType.STOP) {
                        // 跑一下deny动作
                        runAction(consume.deny, context)
                        // 爬
                        return
                    }
                }
                // 获取待消耗数量
                val amount: Int = consume.amount
                    ?.parseItemSection(itemStack, itemTag, data, player, global as? MutableMap<String, String>, null)
                    ?.toIntOrNull() ?: 1
                // 消耗物品
                if (!itemStack.consume(player, amount, itemTag, neigeItems, giveLater)) {
                    // 跑一下deny动作
                    runAction(consume.deny, context)
                    // 数量不足
                    return
                }
            }
        }
        // 执行动作
        trigger.run(context)
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
        trigger.run(ActionContext(player, HashMap(), null, itemStack, itemTag, data))
    }
}
