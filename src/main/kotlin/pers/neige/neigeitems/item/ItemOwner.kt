package pers.neige.neigeitems.item

import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityPickupItemEvent
import pers.neige.neigeitems.manager.ConfigManager.config
import pers.neige.neigeitems.utils.PlayerUtils.getMetadataEZ
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.platform.util.actionBar

// 用于实现掉落物归属功能
object ItemOwner {
    // 拾取物品事件
    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun listener(event: EntityPickupItemEvent) {
        // 获取玩家
        val player = event.entity
        if (player !is Player) return
        // 获取拾取物品
        val item = event.item
        // 通过指令/通过击杀MM怪物掉落NI物品时, NI会先检测物品是否有配置归属权的NBT
        // 检测到NBT后, 为物品配置对应的Metadata
        // 与检测NBT相比, 检测Metadata的速度简直快如闪电, 所以采取这种方式
        // 值得一提的是, Metadata关服即清空
        if (item.hasMetadata("NI-Owner")) {
            // 获取归属者
            val owner = item.getMetadataEZ("NI-Owner", "String", "") as String
            // 检测拾取者是否是拥有者
            if (player.name != owner) {
                // 不是拥有者, 禁止拾取
                event.isCancelled = true
                // 通过actionbar进行对应提示
                config.getString("Messages.invalidOwnerMessage")?.let {
                    when (config.getString("ItemOwner.messageType")) {
                        "actionbar" -> player.actionBar(it.replace("{name}", owner))
                        "message" -> player.sendMessage(it.replace("{name}", owner))
                    }
                }
            }
        }
    }
}