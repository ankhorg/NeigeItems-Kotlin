package pers.neige.neigeitems.listener

import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerItemDamageEvent
import pers.neige.neigeitems.annotation.Listener
import pers.neige.neigeitems.item.ItemDurability
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtCompound
import pers.neige.neigeitems.utils.ItemUtils.isNiItem

object PlayerItemDamageListener {
    @JvmStatic
    @Listener(eventPriority = EventPriority.LOWEST)
    private fun listener0(event: PlayerItemDamageEvent) {
        // 获取玩家
        val player = event.player
        // 获取消耗耐久物品
        val itemStack = event.item
        // 获取NI物品信息(不是NI物品就停止操作)
        val itemInfo = itemStack.isNiItem() ?: return
        // NI物品数据
        val neigeItems: NbtCompound = itemInfo.neigeItems

        // 检测已损坏物品
        ItemDurability.durabilityChecker(player, neigeItems, event)
        if (event.isCancelled) return
    }

    @JvmStatic
    @Listener(eventPriority = EventPriority.LOW)
    private fun listener1(event: PlayerItemDamageEvent) {
        // 获取玩家
        val player = event.player
        // 获取消耗耐久物品
        val itemStack = event.item
        // 获取NI物品信息(不是NI物品就停止操作)
        val itemInfo = itemStack.isNiItem() ?: return
        // 物品NBT
        val itemTag: NbtCompound = itemInfo.itemTag
        // NI物品数据
        val neigeItems: NbtCompound = itemInfo.neigeItems

        // 消耗耐久值(已损坏物品事件取消)
        ItemDurability.itemDamage(player, itemStack, itemTag, neigeItems, event)
        if (event.isCancelled) return
    }
}