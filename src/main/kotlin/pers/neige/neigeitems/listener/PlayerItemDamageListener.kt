package pers.neige.neigeitems.listener

import bot.inker.bukkit.nbt.NbtCompound
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerItemDamageEvent
import pers.neige.neigeitems.annotations.Listener
import pers.neige.neigeitems.item.ItemDurability
import pers.neige.neigeitems.utils.ItemUtils.isNiItem

object PlayerItemDamageListener {
    @Listener(eventPriority = EventPriority.LOWEST)
    fun listener(event: PlayerItemDamageEvent) {
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