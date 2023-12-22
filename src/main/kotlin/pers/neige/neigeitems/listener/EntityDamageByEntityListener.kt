package pers.neige.neigeitems.listener

import org.bukkit.Material
import org.bukkit.entity.Damageable
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.annotation.Listener
import pers.neige.neigeitems.item.ItemDurability
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtCompound
import pers.neige.neigeitems.manager.ActionManager
import pers.neige.neigeitems.utils.ItemUtils.isNiItem

object EntityDamageByEntityListener {
    @Listener(eventPriority = EventPriority.LOWEST)
    fun damage(event: EntityDamageByEntityEvent) {
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

    @Listener(eventPriority = EventPriority.MONITOR)
    fun kill(event: EntityDamageByEntityEvent) {
        // 获取攻击者
        val attacker = event.damager
        // 获取受击者
        val defender = event.entity
        // 检测攻击类型 && 攻击者是否为玩家 && 受击者是否存在血量概念
        if (event.cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK
            || attacker !is Player
            || defender !is Damageable
        ) return
        // 检测事件伤害是否大于受击者当前生命值
        if (event.finalDamage < defender.health) return
        // 执行动作
        val inventory = attacker.inventory
        killAction(attacker, inventory.itemInMainHand, event, "kill_hand")
        killAction(attacker, inventory.itemInOffHand, event, "kill_offhand")
        inventory.helmet?.let { killAction(attacker, it, event, "kill_head") }
        inventory.chestplate?.let { killAction(attacker, it, event, "kill_chest") }
        inventory.leggings?.let { killAction(attacker, it, event, "kill_legs") }
        inventory.boots?.let { killAction(attacker, it, event, "kill_feet") }
    }

    private fun killAction(
        player: Player,
        itemStack: ItemStack,
        event: EntityDamageByEntityEvent,
        key: String
    ) {
        // 获取NI物品信息(不是NI物品就停止操作)
        val itemInfo = itemStack.isNiItem() ?: return

        // 执行物品动作
        ActionManager.killListener(player, itemStack, itemInfo, event, key)
    }
}