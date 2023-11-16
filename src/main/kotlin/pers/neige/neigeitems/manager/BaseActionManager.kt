package pers.neige.neigeitems.manager

import org.bukkit.Bukkit
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.plugin.Plugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import pers.neige.neigeitems.NeigeItems.plugin
import pers.neige.neigeitems.action.Action
import pers.neige.neigeitems.action.ActionContext
import pers.neige.neigeitems.action.ActionResult
import pers.neige.neigeitems.action.ResultType
import pers.neige.neigeitems.action.ResultType.*
import pers.neige.neigeitems.action.impl.ConditionAction
import pers.neige.neigeitems.action.impl.ListAction
import pers.neige.neigeitems.action.impl.StringAction
import pers.neige.neigeitems.action.impl.WhileAction
import pers.neige.neigeitems.action.result.DelayResult
import pers.neige.neigeitems.action.result.Results
import pers.neige.neigeitems.item.action.ComboInfo
import pers.neige.neigeitems.manager.ConfigManager.comboInterval
import pers.neige.neigeitems.manager.HookerManager.mythicMobsHooker
import pers.neige.neigeitems.manager.HookerManager.nashornHooker
import pers.neige.neigeitems.manager.HookerManager.papi
import pers.neige.neigeitems.manager.HookerManager.papiColor
import pers.neige.neigeitems.manager.HookerManager.vaultHooker
import pers.neige.neigeitems.manager.ItemEditorManager.runEditorWithResult
import pers.neige.neigeitems.utils.PlayerUtils.sendActionBar
import pers.neige.neigeitems.utils.PlayerUtils.setMetadataEZ
import pers.neige.neigeitems.utils.SchedulerUtils.*
import pers.neige.neigeitems.utils.SectionUtils.parseItemSection
import pers.neige.neigeitems.utils.SectionUtils.parseSection
import pers.neige.neigeitems.utils.StringUtils.split
import pers.neige.neigeitems.utils.StringUtils.splitOnce
import java.io.InputStreamReader
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.function.BiConsumer
import java.util.function.BiFunction
import javax.script.CompiledScript

/**
 * 用于管理所有物品动作、所有拥有物品动作的物品及相关动作、监听相关事件做到动作触发
 */
abstract class BaseActionManager(val plugin: Plugin) {
    /**
     * 获取物品动作实现函数
     */
    val actions = HashMap<String, BiFunction<ActionContext, String, ActionResult>>()

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
        // 加载基础物品动作
        loadBasicActions()
    }

    open fun reload() {
        conditionScripts.clear()
        actionScripts.clear()
    }

    /**
     * 执行物品动作
     *
     * @param action 动作内容
     * @return 执行结果
     */
    fun runAction(
        action: Any?
    ): ActionResult {
        return runAction(action, ActionContext.empty())
    }

    /**
     * 执行物品动作
     *
     * @param action 动作内容
     * @param context 动作上下文
     * @return 执行结果
     */
    fun runAction(
        action: Any?,
        context: ActionContext
    ): ActionResult {
//        return Action.parse(action).run(this, context)
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
    fun runAction(
        action: Action,
        context: ActionContext
    ): ActionResult {
        return action.run(this, context)
    }

    /**
     * 执行物品动作
     *
     * @param action 动作内容
     * @param context 动作上下文
     * @return 执行结果
     */
    open fun runAction(
        action: StringAction,
        context: ActionContext
    ): ActionResult {
        return actions[action.key]?.apply(context, action.content) ?: Results.SUCCESS
    }

    /**
     * 执行物品动作
     *
     * @param action 动作内容
     * @param context 动作上下文
     * @return 执行结果
     */
    open fun runAction(
        action: ListAction,
        context: ActionContext
    ): ActionResult {
        val actions = action.actions
        actions.forEachIndexed { index, value ->
            val result = value.run(this, context)
            when (result.type) {
                DELAY -> {
                    runLater((result as DelayResult).delay.toLong()) {
                        runAction(actions.subList(index + 1, actions.lastIndex), context)
                    }
                    return Results.SUCCESS
                }
                STOP -> return result
                else -> {}
            }
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
    open fun runAction(
        action: ConditionAction,
        context: ActionContext
    ): ActionResult {
        // 如果条件通过
        if (parseCondition(action.condition, context).type == SUCCESS) {
            // 执行动作
            Action.run(action.sync, action.async, this, context)
            return action.actions.run(this, context)
            // 条件未通过
        } else {
            // 执行deny动作
            return action.deny.run(this, context)
        }
    }

    /**
     * 执行物品动作
     *
     * @param action 动作内容
     * @param context 动作上下文
     * @return 执行结果
     */
    open fun runAction(
        action: WhileAction,
        context: ActionContext
    ): ActionResult {
        // while循环判断条件
        while (parseCondition(action.condition, context).type == SUCCESS) {
            // 执行动作
            Action.run(action.sync, action.async, this, context)
            val result = action.actions.run(this, context)
            // 执行中止
            if (result.type == STOP) {
                action.finally.run(this, context)
                return Results.STOP
            }
        }
        // 执行finally块
        action.finally.run(this, context)
        return Results.SUCCESS
    }


    /**
     * 执行物品动作
     *
     * @param action 动作内容
     * @param context 动作上下文
     * @return 执行结果
     */
    protected fun runAction(
        action: List<*>,
        context: ActionContext
    ): ActionResult {
        action.forEachIndexed { index, value ->
            val result = runAction(value, context)
            when (result.type) {
                DELAY -> {
                    runLater((result as DelayResult).delay.toLong()) {
                        runAction(action.subList(index + 1, action.lastIndex), context)
                    }
                    return Results.SUCCESS
                }
                STOP -> return result
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
    protected open fun runAction(
        action: String,
        context: ActionContext
    ): ActionResult {
        // 解析动作类型及动作内容
        val info = action.split(": ", limit = 2)
        val type = info[0]
        val content = info.getOrNull(1) ?: ""
        // 尝试加载物品动作, 返回执行结果
        return actions[type.lowercase(Locale.getDefault())]?.apply(context, content) ?: Results.SUCCESS
    }

    /**
     * 执行物品动作
     *
     * @param action 动作内容
     * @param context 动作上下文
     * @return 执行结果
     */
    protected fun runAction(
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
                if (parseCondition(condition, context).type == SUCCESS) {
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
                while (parseCondition(whileCondition, context).type == SUCCESS) {
                    // 执行动作
                    runAction(sync, async, context)
                    val result = runAction(actions, context)
                    if (result.type == STOP) {
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
    protected fun runAction(
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
                if (parseCondition(condition, context).type == SUCCESS) {
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
                while (parseCondition(whileCondition, context).type == SUCCESS) {
                    // 执行动作
                    runAction(sync, async, context)
                    val result = runAction(actions, context)
                    if (result.type == STOP) {
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
     * 解析条件
     *
     * @param condition 条件内容
     * @param context 动作上下文
     * @return 执行结果
     */
    fun parseCondition(
        condition: String?,
        context: ActionContext
    ): ActionResult {
        condition ?: return Results.SUCCESS
        val result =
            try {
                conditionScripts.computeIfAbsent(condition) {
                    nashornHooker.compile(engine, condition)
                }.eval(context.bindings) ?: Results.STOP
            } catch (error: Throwable) {
                error.printStackTrace()
                Results.STOP
            }
        return when (result) {
            is ActionResult -> result
            is Boolean -> Results.fromBoolean(result)
            else -> Results.STOP
        }
    }

    /**
     * 添加物品动作
     *
     * @param id 动作ID
     * @param function 动作执行函数
     */
    fun addFunction(id: String, function: BiFunction<ActionContext, String, ActionResult>) {
        actions[id.lowercase(Locale.getDefault())] = function
    }

    /**
     * 添加物品动作
     *
     * @param id 动作ID
     * @param function 动作执行函数
     */
    fun addConsumer(id: String, function: BiConsumer<ActionContext, String>) {
        actions[id.lowercase(Locale.getDefault())] = BiFunction { context, content ->
            function.accept(context, content)
            return@BiFunction Results.SUCCESS
        }
    }

    /**
     * 加载基础物品动作
     */
    protected fun loadBasicActions() {
        // 向玩家发送消息
        addConsumer("tell") { context, content ->
            val player = context.player ?: return@addConsumer
            player.sendMessage(papiColor(player, content))
        }
        // 向玩家发送消息(不将&解析为颜色符号)
        addConsumer("tellNoColor") { context, content ->
            val player = context.player ?: return@addConsumer
            player.sendMessage(papi(player, content))
        }
        // 强制玩家发送消息
        addConsumer("chat") { context, content ->
            val player = context.player ?: return@addConsumer
            sync(plugin) {
                player.chat(papi(player, content))
            }
        }
        // 强制玩家发送消息(将&解析为颜色符号)
        addConsumer("chatWithColor") { context, content ->
            val player = context.player ?: return@addConsumer
            sync(plugin) {
                player.chat(papiColor(player, content))
            }
        }
        // 强制玩家执行指令
        addConsumer("command") { context, content ->
            val player = context.player ?: return@addConsumer
            sync(plugin) {
                Bukkit.dispatchCommand(player, papiColor(player, content))
            }
        }
        // 强制玩家执行指令
        actions["command"]?.let { addFunction("player", it) }
        // 强制玩家执行指令(不将&解析为颜色符号)
        addConsumer("commandNoColor") { context, content ->
            val player = context.player ?: return@addConsumer
            sync(plugin) {
                Bukkit.dispatchCommand(player, papi(player, content))
            }
        }
        // 后台执行指令
        addConsumer("console") { context, content ->
            val player = context.player ?: return@addConsumer
            sync(plugin) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), papiColor(player, content))
            }
        }
        // 后台执行指令(不将&解析为颜色符号)
        addConsumer("consoleNoColor") { context, content ->
            val player = context.player ?: return@addConsumer
            sync(plugin) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), papi(player, content))
            }
        }
        // 发送Title
        addConsumer("title") { context, content ->
            val player = context.player ?: return@addConsumer
            sync(plugin) {
                papiColor(player, content).split(' ', '\\').also { args ->
                    val title = args.getOrNull(0)
                    val subtitle = args.getOrNull(1) ?: ""
                    val fadeIn = args.getOrNull(2)?.toIntOrNull() ?: 10
                    val stay = args.getOrNull(3)?.toIntOrNull() ?: 70
                    val fadeOut = args.getOrNull(4)?.toIntOrNull() ?: 20
                    player.sendTitle(title, subtitle, fadeIn, stay, fadeOut)
                }
            }
        }
        // 发送Title(不将&解析为颜色符号)
        addConsumer("titleNoColor") { context, content ->
            val player = context.player ?: return@addConsumer
            sync(plugin) {
                papi(player, content).split(' ', '\\').also { args ->
                    val title = args.getOrNull(0)
                    val subtitle = args.getOrNull(1) ?: ""
                    val fadeIn = args.getOrNull(2)?.toIntOrNull() ?: 10
                    val stay = args.getOrNull(3)?.toIntOrNull() ?: 70
                    val fadeOut = args.getOrNull(4)?.toIntOrNull() ?: 20
                    player.sendTitle(title, subtitle, fadeIn, stay, fadeOut)
                }
            }
        }
        // 发送ActionBar
        addConsumer("actionBar") { context, content ->
            val player = context.player ?: return@addConsumer
            sync(plugin) {
                player.sendActionBar(papiColor(player, content))
            }
        }
        // 发送ActionBar(不将&解析为颜色符号)
        addConsumer("actionBarNoColor") { context, content ->
            val player = context.player ?: return@addConsumer
            sync(plugin) {
                player.sendActionBar(papi(player, content))
            }
        }
        // 给予玩家金钱
        addConsumer("giveMoney") { context, content ->
            val player = context.player ?: return@addConsumer
            vaultHooker?.giveMoney(player, papi(player, content).toDoubleOrNull() ?: 0.toDouble())
        }
        // 扣除玩家金钱
        addConsumer("takeMoney") { context, content ->
            val player = context.player ?: return@addConsumer
            vaultHooker?.takeMoney(player, papi(player, content).toDoubleOrNull() ?: 0.toDouble())
        }
        // 给予玩家经验
        addConsumer("giveExp") { context, content ->
            val player = context.player ?: return@addConsumer
            sync(plugin) {
                player.giveExp(papi(player, content).toIntOrNull() ?: 0)
            }
        }
        // 扣除玩家经验
        addConsumer("takeExp") { context, content ->
            val player = context.player ?: return@addConsumer
            sync(plugin) {
                player.giveExp((papi(player, content).toIntOrNull() ?: 0) * -1)
            }
        }
        // 设置玩家经验
        addConsumer("setExp") { context, content ->
            val player = context.player ?: return@addConsumer
            sync(plugin) {
                player.totalExperience = papi(player, content).toIntOrNull() ?: 0
            }
        }
        // 给予玩家经验等级
        addConsumer("giveLevel") { context, content ->
            val player = context.player ?: return@addConsumer
            sync(plugin) {
                player.giveExpLevels(papi(player, content).toIntOrNull() ?: 0)
            }
        }
        // 扣除玩家经验等级
        addConsumer("takeLevel") { context, content ->
            val player = context.player ?: return@addConsumer
            sync(plugin) {
                player.giveExpLevels((papi(player, content).toIntOrNull() ?: 0) * -1)
            }
        }
        // 设置玩家经验等级
        addConsumer("setLevel") { context, content ->
            val player = context.player ?: return@addConsumer
            sync(plugin) {
                player.level = papi(player, content).toIntOrNull() ?: 0
            }
        }
        // 给予玩家饱食度
        addConsumer("giveFood") { context, content ->
            val player = context.player ?: return@addConsumer
            sync(plugin) {
                player.foodLevel =
                    (player.foodLevel + (papi(player, content).toIntOrNull() ?: 0)).coerceAtLeast(0).coerceAtMost(20)
            }
        }
        // 扣除玩家饱食度
        addConsumer("takeFood") { context, content ->
            val player = context.player ?: return@addConsumer
            sync(plugin) {
                player.foodLevel =
                    (player.foodLevel - (papi(player, content).toIntOrNull() ?: 0)).coerceAtLeast(0).coerceAtMost(20)
            }
        }
        // 设置玩家饱食度
        addConsumer("setFood") { context, content ->
            val player = context.player ?: return@addConsumer
            sync(plugin) {
                player.foodLevel = (papi(player, content).toIntOrNull() ?: 0).coerceAtLeast(0).coerceAtMost(20)
            }
        }
        // 给予玩家饱和度
        addConsumer("giveSaturation") { context, content ->
            val player = context.player ?: return@addConsumer
            sync(plugin) {
                player.saturation =
                    (player.saturation + (papi(player, content).toFloatOrNull() ?: 0F)).coerceAtLeast(0F)
                        .coerceAtMost(player.foodLevel.toFloat())
            }
        }
        // 扣除玩家饱和度
        addConsumer("takeSaturation") { context, content ->
            val player = context.player ?: return@addConsumer
            sync(plugin) {
                player.saturation =
                    (player.saturation - (papi(player, content).toFloatOrNull() ?: 0F)).coerceAtLeast(0F)
                        .coerceAtMost(player.foodLevel.toFloat())
            }
        }
        // 设置玩家饱和度
        addConsumer("setSaturation") { context, content ->
            val player = context.player ?: return@addConsumer
            sync(plugin) {
                player.saturation = (papi(player, content).toFloatOrNull() ?: 0F).coerceAtLeast(0F)
                    .coerceAtMost(player.foodLevel.toFloat())
            }
        }
        // 给予玩家生命
        addConsumer("giveHealth") { context, content ->
            val player = context.player ?: return@addConsumer
            sync(plugin) {
                player.health = (player.health + (papi(player, content).toDoubleOrNull() ?: 0.toDouble())).coerceAtMost(
                    player.maxHealth
                )
            }
        }
        // 扣除玩家生命
        addConsumer("takeHealth") { context, content ->
            val player = context.player ?: return@addConsumer
            sync(plugin) {
                player.health =
                    (player.health - (papi(player, content).toDoubleOrNull() ?: 0.toDouble())).coerceAtLeast(
                        0.toDouble()
                    )
            }
        }
        // 设置玩家生命
        addConsumer("setHealth") { context, content ->
            val player = context.player ?: return@addConsumer
            sync(plugin) {
                player.health = (papi(player, content).toDoubleOrNull() ?: 0.toDouble()).coerceAtLeast(0.toDouble())
                    .coerceAtMost(player.maxHealth)
            }
        }
        // 释放MM技能
        addConsumer("castSkill") { context, content ->
            val player = context.player ?: return@addConsumer
            mythicMobsHooker?.castSkill(player, content, player)
        }
        // 连击记录
        addConsumer("combo") { context, content ->
            val player = context.player ?: return@addConsumer
            val info = papi(player, content).split(" ", limit = 2)
            // 连击组
            val comboGroup = info[0]
            // 连击类型
            val comboType = info.getOrNull(1) ?: ""
            if (!player.hasMetadata("NI-Combo-$comboGroup")) {
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
        }
        // 连击清空
        addConsumer("comboClear") { context, content ->
            val player = context.player ?: return@addConsumer
            player.setMetadataEZ("NI-Combo-${papi(player, content)}", ArrayList<ComboInfo>())
        }
        // 设置药水效果
        addConsumer("setPotionEffect") { context, content ->
            val player = context.player ?: return@addConsumer
            val args = content.split(" ", limit = 3)
            if (args.size == 3) {
                val type = PotionEffectType.getByName(args[0].uppercase())
                val amplifier = args[1].toIntOrNull()
                val duration = args[2].toIntOrNull()
                if (type != null && duration != null && amplifier != null) {
                    sync(plugin) {
                        player.addPotionEffect(PotionEffect(type, duration * 20, amplifier - 1), true)
                    }
                }
            }
        }
        // 移除药水效果
        addConsumer("removePotionEffect") { context, content ->
            val player = context.player ?: return@addConsumer
            val type = PotionEffectType.getByName(content.uppercase())
            if (type != null) {
                sync(plugin) {
                    player.removePotionEffect(type)
                }
            }
        }
        // 延迟(单位是tick)
        addFunction("delay") { _, content ->
            DelayResult(content.toIntOrNull() ?: 0)
        }
        // 终止
        addFunction("return") { _, _ ->
            Results.STOP
        }
        // js
        addFunction("js") { context, content ->
            val result = try {
                actionScripts.computeIfAbsent(content) {
                    nashornHooker.compile(engine, content)
                }.eval(context.bindings) ?: Results.SUCCESS
            } catch (error: Throwable) {
                error.printStackTrace()
                Results.SUCCESS
            }
            return@addFunction when (result) {
                is ActionResult -> result
                is Boolean -> Results.fromBoolean(result)
                else -> Results.SUCCESS
            }
        }
        addConsumer("setglobal") { context, content ->
            val info = content.splitOnce(" ")
            if (info.size > 1) {
                context.global[info[0]] = info[1]
            }
        }
        addConsumer("delglobal") { context, content ->
            context.global.remove(content)
        }
    }
}
