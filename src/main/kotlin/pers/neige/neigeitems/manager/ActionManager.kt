package pers.neige.neigeitems.manager

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
import org.slf4j.LoggerFactory
import pers.neige.neigeitems.NeigeItems
import pers.neige.neigeitems.action.ActionContext
import pers.neige.neigeitems.action.ActionResult
import pers.neige.neigeitems.action.ResultType
import pers.neige.neigeitems.action.impl.StringAction
import pers.neige.neigeitems.action.result.Results
import pers.neige.neigeitems.event.ItemActionEvent
import pers.neige.neigeitems.item.ItemInfo
import pers.neige.neigeitems.item.action.ItemAction
import pers.neige.neigeitems.item.action.ItemActionType
import pers.neige.neigeitems.manager.ConfigManager.config
import pers.neige.neigeitems.utils.ActionUtils.consume
import pers.neige.neigeitems.utils.ActionUtils.isCoolDown
import pers.neige.neigeitems.utils.ConfigUtils
import pers.neige.neigeitems.utils.ConfigUtils.getMap
import pers.neige.neigeitems.utils.PlayerUtils.getMetadataEZ
import pers.neige.neigeitems.utils.PlayerUtils.setMetadataEZ
import pers.neige.neigeitems.utils.SectionUtils.parseItemSection
import pers.neige.neigeitems.utils.SectionUtils.parseSection
import java.io.File
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap
import java.util.function.BiFunction

/**
 * 用于管理所有物品动作、所有拥有物品动作的物品及相关动作、监听相关事件做到动作触发
 */
object ActionManager : BaseActionManager(NeigeItems.getInstance()) {
    private val logger = LoggerFactory.getLogger(ActionManager::class.java.simpleName)

    /**
     * 获取拥有动作的物品ID及相关动作
     */
    val itemActions: ConcurrentHashMap<String, ItemAction> = ConcurrentHashMap<String, ItemAction>()
    val functions: ConcurrentHashMap<String, pers.neige.neigeitems.action.Action> =
        ConcurrentHashMap<String, pers.neige.neigeitems.action.Action>()

    init {
        loadJSLib()
        // 加载所有拥有动作的物品及相关动作
        loadItemActions()
        // 加载动作组
        loadFunctions()
    }

    /**
     * 重载物品动作管理器
     */
    override fun reload() {
        super.reload()
        itemActions.clear()
        functions.clear()
        loadItemActions()
        loadFunctions()
    }

    /**
     * 添加物品动作
     *
     * @param id 动作ID
     * @param function 动作执行函数
     */
    @Deprecated("用BaseActionManager里那个")
    fun addAction(id: String, function: BiFunction<Player, String, Boolean>) {
        addFunction(id, BiFunction { context, content ->
            val player = context.player
            if (player != null) {
                return@BiFunction CompletableFuture.completedFuture(
                    Results.fromBoolean(
                        function.apply(
                            player,
                            content
                        )
                    )
                )
            }
            return@BiFunction CompletableFuture.completedFuture(Results.SUCCESS)
        })
    }

    override fun runAction(
        action: StringAction, context: ActionContext
    ): CompletableFuture<ActionResult> {
        // 解析物品变量
        val itemStack = context.itemStack
        val type = action.key
        val content = let {
            val nbt = context.nbt
            val cache = (context.params?.get("cache") ?: context.global) as? MutableMap<String, String>
            val sections = context.params?.get("sections") as? ConfigurationSection
            if (itemStack != null && nbt != null) {
                action.content.parseItemSection(
                    itemStack, nbt, context.data, context.player, cache, sections
                )
            } else {
                action.content.parseSection(
                    cache, context.player, sections
                )
            }
        }
        // 尝试加载物品动作, 返回执行结果
        actions[type]?.apply(context, content)?.also { return it }
        // 尝试加载物品编辑函数, 返回执行结果
        val player = context.player
        if (itemStack != null && player != null) {
            ItemEditorManager.runEditorWithResult(type, content, itemStack, player)
        }
        return CompletableFuture.completedFuture(Results.SUCCESS)
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

    private fun loadFunctions() {
        ConfigUtils.getAllFiles("Functions").filter { it.name.endsWith(".yml") }.getMap().forEach { (id, config) ->
            try {
                functions[id] = compile(config)
            } catch (throwable: Throwable) {
                logger.warn("error occurred while loading function: $id", throwable)
            }
        }
    }

    override fun loadBasicActions() {
        super.loadBasicActions()
        // 触发动作组
        addFunction("func") { context, content ->
            content ?: return@addFunction CompletableFuture.completedFuture(Results.SUCCESS)
            val function = functions[content] ?: return@addFunction CompletableFuture.completedFuture(Results.SUCCESS)
            return@addFunction runActionWithResult(function, context)
        }
    }

    // 物品左右键交互
    fun interactListener(
        player: Player, itemStack: ItemStack, itemInfo: ItemInfo, event: PlayerInteractEvent
    ) {
        val id = itemInfo.id

        // 获取物品动作
        val itemAction = itemActions[id] ?: return
        // 获取基础触发类型
        val basicType = when (event.action) {
            // 左键类型
            Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK -> {
                when {
                    player.isSneaking -> {
                        // 如果既没有shift_left又没有shift_all就爬
                        if (!itemAction.hasShiftLeftAction) return
                        ItemActionType.SHIFT_LEFT
                    }

                    else -> {
                        // 如果既没有left又没有all就爬
                        if (!itemAction.hasLeftAction) return
                        ItemActionType.LEFT
                    }
                }
            }
            // 右键类型
            Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK -> {
                when {
                    player.isSneaking -> {
                        // 如果既没有shift_right又没有shift_all就爬
                        if (!itemAction.hasShiftRightAction) return
                        ItemActionType.SHIFT_RIGHT
                    }

                    else -> {
                        // 如果既没有right又没有all就爬
                        if (!itemAction.hasRightAction) return
                        ItemActionType.RIGHT
                    }
                }
            }
            // 停止操作
            else -> return
        }
        val basicTrigger = itemAction.triggers[basicType.type]
        var allType = ItemActionType.SHIFT_ALL
        // 获取all触发器
        val allTrigger = when {
            player.isSneaking -> {
                itemAction.triggers[ItemActionType.SHIFT_ALL.type]
            }

            else -> {
                allType = ItemActionType.ALL
                itemAction.triggers[ItemActionType.ALL.type]
            }
        }

        val itemTag = itemInfo.itemTag
        val neigeItems = itemInfo.neigeItems

        // 取消交互事件
        event.isCancelled = true
        // 检测冷却
        if ((basicTrigger ?: allTrigger)!!.isCoolDown(player, itemStack, itemInfo)) return
        // 触发基础动作事件
        if (basicTrigger != null && !ItemActionEvent(player, itemStack, itemInfo, basicType, basicTrigger).call()) {
            return
        }
        // 触发all动作事件
        if (allTrigger != null && !ItemActionEvent(player, itemStack, itemInfo, allType, allTrigger).call()) {
            return
        }
        // 获取消耗信息
        val consume = basicTrigger?.consume ?: allTrigger?.consume
        val data = itemInfo.data
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
            val amount: Int = consume.amount?.parseItemSection(
                itemStack, itemInfo, player, global as? MutableMap<String, String>, null
            )?.toIntOrNull() ?: 1
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
        player: Player, itemStack: ItemStack, itemInfo: ItemInfo, event: PlayerItemConsumeEvent
    ) {
        basicHandler(
            player,
            itemStack,
            itemInfo,
            event,
            ItemActionType.EAT.type,
            cancel = true,
            cancelIfCooldown = false,
            giveLater = true
        )
    }

    // 丢弃物品
    fun dropListener(
        player: Player, itemStack: ItemStack, itemInfo: ItemInfo, event: PlayerDropItemEvent
    ) {
        basicHandler(
            player, itemStack, itemInfo, event, ItemActionType.DROP.type, cancel = false, cancelIfCooldown = true
        )
    }

    // 拾取物品
    fun pickListener(
        player: Player, itemStack: ItemStack, itemInfo: ItemInfo, event: EntityPickupItemEvent
    ) {
        basicHandler(
            player,
            itemStack,
            itemInfo,
            event,
            ItemActionType.PICK.type,
            cancel = false,
            cancelIfCooldown = true,
            consumeItem = false
        )
    }

    // 点击物品
    fun clickListener(
        player: Player, itemStack: ItemStack, itemInfo: ItemInfo, event: InventoryClickEvent
    ) {
        basicHandler(
            player, itemStack, itemInfo, event, ItemActionType.CLICK.type
        )
    }

    // 物品被点击
    fun beClickedListener(
        player: Player, itemStack: ItemStack, itemInfo: ItemInfo, event: InventoryClickEvent
    ) {
        basicHandler(
            player, itemStack, itemInfo, event, ItemActionType.BECLICKED.type
        )
    }

    // 射箭时由弓触发
    fun shootBowListener(
        player: Player, itemStack: ItemStack, itemInfo: ItemInfo, event: EntityShootBowEvent
    ) {
        basicHandler(
            player,
            itemStack,
            itemInfo,
            event,
            ItemActionType.SHOOT_BOW.type,
            cancel = false,
            cancelIfCooldown = true,
            consumeItem = false
        )
    }

    // 射箭时由箭触发
    fun shootArrowListener(
        player: Player, itemStack: ItemStack, itemInfo: ItemInfo, event: EntityShootBowEvent
    ) {
        basicHandler(
            player,
            itemStack,
            itemInfo,
            event,
            ItemActionType.SHOOT_ARROW.type,
            cancel = false,
            cancelIfCooldown = true,
            consumeItem = false
        )
    }

    // 格挡时由盾触发
    fun blockingListener(
        player: Player, itemStack: ItemStack, itemInfo: ItemInfo, event: EntityDamageByEntityEvent
    ) {
        basicHandler(
            player, itemStack, itemInfo, event, ItemActionType.BLOCKING.type, cancel = false, cancelIfCooldown = true
        )
    }

    // 攻击实体时由主手物品触发
    fun damageListener(
        player: Player, itemStack: ItemStack, itemInfo: ItemInfo, event: EntityDamageByEntityEvent
    ) {
        basicHandler(
            player, itemStack, itemInfo, event, ItemActionType.DAMAGE.type, cancel = false, cancelIfCooldown = true
        )
    }

    // 击杀实体时触发
    fun killListener(
        player: Player, itemStack: ItemStack, itemInfo: ItemInfo, event: EntityDamageByEntityEvent, key: String
    ) {
        basicHandler(
            player, itemStack, itemInfo, event, key, cancel = false, consumeItem = false
        )
    }

    // 挖掘方块时由主手物品触发
    fun breakBlockListener(
        player: Player, itemStack: ItemStack, itemInfo: ItemInfo, event: BlockBreakEvent
    ) {
        basicHandler(
            player, itemStack, itemInfo, event, ItemActionType.BREAK_BLOCK.type, cancel = false, cancelIfCooldown = true
        )
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
        // 获取基础触发器, 没有对应物品动作就停止判断
        val trigger = itemAction.triggers[key] ?: return

        val itemTag = itemInfo.itemTag
        val neigeItems = itemInfo.neigeItems

        // 检测冷却
        if (trigger.isCoolDown(player, itemStack, itemInfo)) {
            if ((cancel || cancelIfCooldown) && event is Cancellable) {
                event.isCancelled = true
            }
            return
        }
        val type = ItemActionType.matchType(key)
        // 事件中止就停止判断
        if (type != null && !ItemActionEvent(player, itemStack, itemInfo, type, trigger).call()) {
            return
        }
        // 取消事件
        if (event is Cancellable && cancel) {
            event.isCancelled = true
        }
        val data = itemInfo.data
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
                val amount: Int = consume.amount?.parseItemSection(
                    itemStack, itemInfo, player, global as? MutableMap<String, String>, null
                )?.toIntOrNull() ?: 1
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
        player: Player, itemStack: ItemStack, itemInfo: ItemInfo, key: String
    ) {
        val id = itemInfo.id
        // 获取物品动作
        val itemAction = itemActions[id] ?: let { return }
        // 获取基础触发器, 没有对应物品动作就停止判断
        val trigger = itemAction.triggers[key] ?: return

        val itemTag = itemInfo.itemTag

        // 检测冷却
        val tick = trigger.tick?.parseItemSection(itemStack, itemInfo, player)?.toLongOrNull() ?: 10
        // 如果冷却存在且大于0
        if (tick > 0) {
            // 获取上次使用时间
            val lastTick = player.getMetadataEZ("NI-TICK-${trigger.group}", 0.toLong()) as Long
            // 如果仍处于冷却时间
            if (lastTick > 0) {
                player.setMetadataEZ("NI-TICK-${trigger.group}", lastTick - 1)
                return
            }
        }
        val type = ItemActionType.matchType(key)
        // 没有对应物品动作或事件中止就停止判断
        if (type != null && !ItemActionEvent(player, itemStack, itemInfo, type, trigger).call()) {
            return
        }
        player.setMetadataEZ("NI-TICK-${trigger.group}", tick)
        // 执行动作
        trigger.run(ActionContext(player, HashMap(), null, itemStack, itemTag, itemInfo.data))
    }
}
