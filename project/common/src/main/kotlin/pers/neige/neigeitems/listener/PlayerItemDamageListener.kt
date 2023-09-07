package pers.neige.neigeitems.listener

import bot.inker.bukkit.nbt.NbtCompound
import bot.inker.bukkit.nbt.NbtItemStack
import org.bukkit.event.player.PlayerItemDamageEvent
import pers.neige.neigeitems.item.ItemDurability
import pers.neige.neigeitems.utils.ItemUtils.isNiItem
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent

object PlayerItemDamageListener {
    @SubscribeEvent(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun listener(event: PlayerItemDamageEvent) {
        // 获取玩家
        val player = event.player
        // 获取消耗耐久物品
        val itemStack = event.item
        // 获取NI物品信息(不是NI物品就停止操作)
        val itemInfo = itemStack.isNiItem(true) ?: return
        // NBT物品
        val nbtItemStack: NbtItemStack = itemInfo.nbtItemStack
        // 物品NBT
        val itemTag: NbtCompound = itemInfo.itemTag
        // NI物品数据
        val neigeItems: NbtCompound = itemInfo.neigeItems
        // NI物品id
        val id: String = itemInfo.id
        // NI节点数据
        val data: HashMap<String, String> = itemInfo.data!!

        // 消耗耐久值(已损坏物品事件取消)
        ItemDurability.itemDamage(player, itemStack, itemTag, neigeItems, event)
        if (event.isCancelled) return
    }
}