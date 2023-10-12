package pers.neige.neigeitems.item

import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityPickupItemEvent
import pers.neige.neigeitems.manager.ConfigManager.config
import pers.neige.neigeitems.utils.PlayerUtils.getMetadataEZ
import pers.neige.neigeitems.utils.PlayerUtils.sendActionBar

/**
 * 用于实现掉落物归属功能
 */
object ItemOwner {
    fun check(
        player: Player,
        item: Item,
        event: EntityPickupItemEvent
    ) {
        if (item.hasMetadata("NI-Owner")) {
            // 获取归属者
            val owner = item.getMetadataEZ("NI-Owner", "String", "") as String
            // 检测拾取者是否是拥有者
            if (player.name != owner) {
                // 不是拥有者, 禁止拾取
                event.isCancelled = true
                // 是否隐藏掉落物
                val hide = item.getMetadataEZ("NI-Hide", "Byte", 0.toByte()) as Byte
                // 不隐藏的话给予提示
                if (hide != 1.toByte()) {
                    // 通过actionbar进行对应提示
                    config.getString("Messages.invalidOwnerMessage")?.let {
                        when (config.getString("ItemOwner.messageType")) {
                            "actionbar" -> player.sendActionBar(it.replace("{name}", owner))
                            "message" -> player.sendMessage(it.replace("{name}", owner))
                        }
                    }
                }
            }
        }
    }
}