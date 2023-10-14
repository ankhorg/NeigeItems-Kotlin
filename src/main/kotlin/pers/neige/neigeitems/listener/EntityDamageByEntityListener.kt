package pers.neige.neigeitems.listener

import bot.inker.bukkit.nbt.NbtCompound
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import pers.neige.neigeitems.annotation.Listener
import pers.neige.neigeitems.item.ItemDurability
import pers.neige.neigeitems.manager.ActionManager
import pers.neige.neigeitems.utils.ItemUtils.isNiItem

object EntityDamageByEntityListener {
    @Listener(eventPriority = EventPriority.LOWEST)
    fun listener(event: EntityDamageByEntityEvent) {
        if (event.cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK || event.damager !is Player) return
        // 获取玩家(攻击者)
        val player = event.damager as Player
        // 获取凶器
        val itemStack = player.inventory.itemInMainHand
        // 获取NI物品信息(不是NI物品就停止操作)
        val itemInfo = itemStack.isNiItem() ?: return
        // NI物品数据
        val neigeItems: NbtCompound = itemInfo.neigeItems

        // 检测已损坏物品
        ItemDurability.basic(player, neigeItems, event)
        if (event.isCancelled) return
        // 执行物品动作
        ActionManager.damageListener(player, itemStack, itemInfo, event)
        if (event.isCancelled) return
    }

    @Listener(eventPriority = EventPriority.LOWEST)
    fun blocking(event: EntityDamageByEntityEvent) {
        // 获取受击者
        val player = event.entity
        // 检测是否为玩家
        if (player !is Player) return
        // 检测是否存在格挡行为
        if (event.getDamage(EntityDamageEvent.DamageModifier.BLOCKING) == 0.0) return
        // 获取主手物品
        var itemStack = player.inventory.itemInMainHand
        // 空检测及类型检测
        if (itemStack.type != Material.SHIELD) {
            // 获取副手物品
            itemStack = player.inventory.itemInOffHand
            // 空检测及类型检测
            if (itemStack.type != Material.SHIELD) return
        }
        // 获取NI物品信息(不是NI物品就停止操作)
        val itemInfo = itemStack.isNiItem() ?: return
        // NI物品数据
        val neigeItems: NbtCompound = itemInfo.neigeItems

        // 检测已损坏物品
        ItemDurability.basic(player, neigeItems, event)
        if (event.isCancelled) return
        // 执行物品动作
        ActionManager.blockingListener(player, itemStack, itemInfo, event)
        if (event.isCancelled) return
    }
}