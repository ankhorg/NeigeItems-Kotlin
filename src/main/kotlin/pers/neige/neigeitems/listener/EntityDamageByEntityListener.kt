package pers.neige.neigeitems.listener

import bot.inker.bukkit.nbt.NbtCompound
import bot.inker.bukkit.nbt.NbtItemStack
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import pers.neige.neigeitems.item.ItemDurability
import pers.neige.neigeitems.utils.ItemUtils.isNiItem
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent

object EntityDamageByEntityListener {
    @SubscribeEvent(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun listener(event: EntityDamageByEntityEvent) {
        if (event.cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK || event.damager !is Player) return
        // 获取玩家(攻击者)
        val player = event.damager as Player
        // 获取凶器
        val itemStack = player.inventory.itemInMainHand
        // 获取NI物品信息(不是NI物品就停止操作)
        val itemInfo = itemStack.isNiItem(true) ?: return
        // NI物品数据
        val neigeItems: NbtCompound = itemInfo.neigeItems

        // 检测已损坏物品
        ItemDurability.entityDamageByEntity(player, neigeItems, event)
        if (event.isCancelled) return
    }
}