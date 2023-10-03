package pers.neige.neigeitems.listener

import bot.inker.bukkit.nbt.NbtCompound
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityShootBowEvent
import pers.neige.neigeitems.item.ItemDurability
import pers.neige.neigeitems.manager.ActionManager
import pers.neige.neigeitems.utils.ItemUtils.isNiItem
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent

object EntityShootBowListener {
    @SubscribeEvent(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun shootBow(event: EntityShootBowEvent) {
        // 获取玩家
        val player = event.entity
        if (player !is Player) return
        // 获取弓
        val itemStack = event.bow
        // 获取NI物品信息(不是NI物品就停止操作)
        val itemInfo = itemStack?.isNiItem() ?: return
        // NI物品数据
        val neigeItems: NbtCompound = itemInfo.neigeItems

        // 检测已损坏物品
        ItemDurability.basic(player, neigeItems, event)
        if (event.isCancelled) return
        // 执行物品动作
        ActionManager.shootBowListener(player, itemStack, itemInfo, event)
        if (event.isCancelled) return
    }

    @SubscribeEvent(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun shootArrow(event: EntityShootBowEvent) {
        // 获取玩家
        val player = event.entity
        if (player !is Player) return
        // 获取箭
        val itemStack = event.consumable
        // 获取NI物品信息(不是NI物品就停止操作)
        val itemInfo = itemStack?.isNiItem() ?: return
        // NI物品数据
        val neigeItems: NbtCompound = itemInfo.neigeItems

        // 检测已损坏物品
        ItemDurability.basic(player, neigeItems, event)
        if (event.isCancelled) return
        // 执行物品动作
        ActionManager.shootArrowListener(player, itemStack, itemInfo, event)
        if (event.isCancelled) return
    }
}