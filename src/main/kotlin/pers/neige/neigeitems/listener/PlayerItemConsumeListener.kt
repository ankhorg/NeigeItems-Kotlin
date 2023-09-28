package pers.neige.neigeitems.listener

import bot.inker.bukkit.nbt.NbtCompound
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.inventory.EquipmentSlot
import pers.neige.neigeitems.item.ItemDurability
import pers.neige.neigeitems.manager.ActionManager
import pers.neige.neigeitems.utils.ItemUtils.isNiItem
import pers.neige.neigeitems.utils.ItemUtils.saveToSafe
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent

object PlayerItemConsumeListener {
    @SubscribeEvent(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun listener(event: PlayerItemConsumeEvent) {
        // 获取玩家
        val player = event.player
        // 获取手持物品
        val itemStack = event.item
        // 获取NI物品信息(不是NI物品就停止操作)
        val itemInfo = itemStack.isNiItem(true) ?: return
        // 物品NBT
        val itemTag: NbtCompound = itemInfo.itemTag
        // NI物品数据
        val neigeItems: NbtCompound = itemInfo.neigeItems

        // 检测东西在哪只手上
        val hand = if (itemStack.isSimilar(player.inventory.itemInMainHand)) {
            EquipmentSlot.HAND
        } else {
            EquipmentSlot.OFF_HAND
        }

        try {
            // 检测已损坏物品
            ItemDurability.consume(player, neigeItems, event)
            if (event.isCancelled) return
            // 执行物品动作
            ActionManager.eatListener(player, itemStack, itemInfo, event)
        } catch (error: Throwable) {
            error.printStackTrace()
        }

        // 他妈的。。。。。
        itemTag.saveToSafe(itemStack)

        // 设置物品
        if (hand == EquipmentSlot.HAND) {
            player.inventory.setItemInMainHand(itemStack)
        } else {
            player.inventory.setItemInOffHand(itemStack)
        }
    }
}