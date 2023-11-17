package pers.neige.neigeitems.item.action

import bot.inker.bukkit.nbt.NbtCompound
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.action.Action
import pers.neige.neigeitems.action.ActionContext
import pers.neige.neigeitems.manager.ActionManager
import pers.neige.neigeitems.utils.ItemUtils.getNbt
import pers.neige.neigeitems.utils.SchedulerUtils

class ActionTrigger(val id: String, val type: String, val config: ConfigurationSection) {
    /**
     * 获取物品使用冷却
     */
    val cooldown = config.getString("cooldown", "1000")

    /**
     * 获取tick型触发器触发间隔
     */
    val tick = config.getString("tick", "10")

    /**
     * 获取物品冷却组ID
     */
    val group: String = config.getString("group") ?: "$type-$id"

    /**
     * 获取物品消耗信息
     */
    val consume: ConsumeInfo? = config.getConfigurationSection("consume")?.let { ConsumeInfo(it) }

    /**
     * 获取异步动作信息
     */
    val actions: Action = ActionManager.compile(config.get("actions"))

    /**
     * 获取异步动作信息
     */
    val async: Action = ActionManager.compile(config.get("async"))

    /**
     * 获取同步动作信息
     */
    val sync: Action = ActionManager.compile(config.get("sync"))

    /**
     * 运行异步物品动作
     *
     * @param context 动作上下文
     */
    fun run(
        context: ActionContext
    ) {
        async(context)
        sync(context)
    }

    /**
     * 运行异步物品动作
     *
     * @param context 动作上下文
     */
    fun async(
        context: ActionContext
    ) {
        SchedulerUtils.async {
            actions.eval(ActionManager, context)
            async.eval(ActionManager, context)
        }
    }

    /**
     * 运行同步物品动作
     *
     * @param context 动作上下文
     */
    fun sync(
        context: ActionContext
    ) {
        SchedulerUtils.sync {
            sync.eval(ActionManager, context)
        }
    }

    /**
     * 运行物品动作
     *
     * @param player 待操作玩家
     * @param itemStack 触发物品
     * @param itemTag 物品NBT
     * @param event 触发事件
     */
    @Deprecated("使用run(context: ActionContext)代替")
    fun run(
        player: Player,
        itemStack: ItemStack,
        itemTag: NbtCompound? = itemStack.getNbt(),
        data: MutableMap<String, String>? = null,
        event: Event?,
        global: MutableMap<String, Any?>
    ) {
        // 运行异步动作
        async(player, itemStack, itemTag, data, event, global)
        // 运行同步动作
        sync(player, itemStack, itemTag, data, event, global)
    }

    /**
     * 运行异步物品动作
     *
     * @param player 待操作玩家
     * @param itemStack 触发物品
     * @param itemTag 物品NBT
     * @param event 触发事件
     */
    @Deprecated("使用async(context: ActionContext)代替")
    fun async(
        player: Player,
        itemStack: ItemStack,
        itemTag: NbtCompound? = itemStack.getNbt(),
        data: MutableMap<String, String>? = null,
        event: Event?,
        global: MutableMap<String, Any?>
    ) {
        SchedulerUtils.async {
            ActionManager.runAction(player, actions, itemStack, itemTag, data, event, global)
        }
    }

    /**
     * 运行同步物品动作
     *
     * @param player 待操作玩家
     * @param itemStack 触发物品
     * @param itemTag 物品NBT
     * @param event 触发事件
     */
    @Deprecated("使用sync(context: ActionContext)代替")
    fun sync(
        player: Player,
        itemStack: ItemStack,
        itemTag: NbtCompound? = itemStack.getNbt(),
        data: MutableMap<String, String>? = null,
        event: Event?,
        global: MutableMap<String, Any?>
    ) {
        ActionManager.runAction(sync, ActionContext(player, global, null, itemStack, itemTag, data, event))
    }
}