package pers.neige.neigeitems.listener

import bot.inker.bukkit.nbt.NbtCompound
import bot.inker.bukkit.nbt.NbtItemStack
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityShootBowEvent
import pers.neige.neigeitems.item.ItemDurability
import pers.neige.neigeitems.utils.ItemUtils.isNiItem
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent

object EntityShootBowListener {
    @SubscribeEvent(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun listener(event: EntityShootBowEvent) {
        // 获取玩家
        val player = event.entity
        if (player !is Player) return
        // 获取弓
        val itemStack = event.bow
        // 获取NI物品信息(不是NI物品就停止操作)
        val itemInfo = itemStack?.isNiItem(true) ?: return
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

        // 检测已损坏物品
        ItemDurability.shootBow(player, neigeItems, event)
        if (event.isCancelled) return
    }
}