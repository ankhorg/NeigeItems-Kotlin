package pers.neige.neigeitems.item.action

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.NeigeItems.bukkitScheduler
import pers.neige.neigeitems.NeigeItems.plugin
import pers.neige.neigeitems.manager.ActionManager
import taboolib.module.nms.ItemTag
import taboolib.module.nms.getItemTag

class ActionTrigger(val id: String, val type: String, val config: ConfigurationSection) {
    /**
     * 获取物品使用冷却
     */
    val cooldown = config.getString("cooldown")

    /**
     * 获取物品冷却组ID
     */
    val group: String = config.getString("group") ?: "$type-$id"

    /**
     * 获取物品消耗信息
     */
    val consume = config.getConfigurationSection("consume")

    /**
     * 获取异步动作信息
     */
    val actions = config.get("actions")

    /**
     * 获取同步动作信息
     */
    val sync = config.get("sync")

    /**
     * 运行物品动作
     *
     * @param player 待操作玩家
     * @param itemStack 触发物品
     * @param itemTag 物品NBT
     * @param event 触发事件
     */
    fun run(
        player: Player,
        itemStack: ItemStack,
        itemTag: ItemTag? = itemStack.getItemTag(),
        event: Event
    ) {
        // 运行异步动作
        async(player, itemStack, itemTag, event)
        // 运行同步动作
        sync(player, itemStack, itemTag, event)
    }

    /**
     * 运行异步物品动作
     *
     * @param player 待操作玩家
     * @param itemStack 触发物品
     * @param itemTag 物品NBT
     * @param event 触发事件
     */
    fun async(
        player: Player,
        itemStack: ItemStack,
        itemTag: ItemTag? = itemStack.getItemTag(),
        event: Event
    ) {
        bukkitScheduler.runTaskAsynchronously(plugin, Runnable {
            ActionManager.runAction(player, actions, itemStack, itemTag, event)
        })
    }

    /**
     * 运行同步物品动作
     *
     * @param player 待操作玩家
     * @param itemStack 触发物品
     * @param itemTag 物品NBT
     * @param event 触发事件
     */
    fun sync(
        player: Player,
        itemStack: ItemStack,
        itemTag: ItemTag? = itemStack.getItemTag(),
        event: Event
    ) {
        ActionManager.runAction(player, sync, itemStack, itemTag, event)
    }
}