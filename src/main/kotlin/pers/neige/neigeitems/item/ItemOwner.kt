package pers.neige.neigeitems.item

import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityPickupItemEvent
import pers.neige.neigeitems.manager.ConfigManager.config
import pers.neige.neigeitems.utils.PlayerUtils.getMetadataEZ
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.platform.util.actionBar

object ItemOwner {
    // 拾取物品
    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun listener(event: EntityPickupItemEvent) {
        // 获取玩家
        val player = event.entity
        if (player !is Player) return
        // 获取拾取物品
        val item = event.item
        if (item.hasMetadata("NI-Owner")) {
            val owner = item.getMetadataEZ("NI-Owner", "String", "") as String
            if (player.name != owner) {
                event.isCancelled = true
                config.getString("Messages.invalidOwnerMessage")?.let {
                    player.actionBar(it.replace("{name}", owner))
                }
            }
        }
    }
}