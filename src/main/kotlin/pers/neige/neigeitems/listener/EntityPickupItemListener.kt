package pers.neige.neigeitems.listener

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityPickupItemEvent
import pers.neige.neigeitems.annotation.Listener
import pers.neige.neigeitems.manager.ActionManager
import pers.neige.neigeitems.manager.ConfigManager
import pers.neige.neigeitems.utils.ItemUtils.isNiItem
import pers.neige.neigeitems.utils.PlayerUtils.getMetadataEZ
import pers.neige.neigeitems.utils.PlayerUtils.sendActionBar

object EntityPickupItemListener {
    @JvmStatic
    @Listener(eventPriority = EventPriority.LOWEST)
    private fun owner(event: EntityPickupItemEvent) {
        // 获取玩家
        val player = event.entity
        if (player !is Player) return
        // 物品拥有者检测
        val item = event.item
        if (!item.hasMetadata("NI-Owner")) return
        // 获取归属者
        val owner = item.getMetadataEZ("NI-Owner", "") as String
        // 检测拾取者是否是拥有者
        if (player.name == owner) return
        // 不是拥有者, 禁止拾取
        event.isCancelled = true
        // 是否隐藏掉落物
        val hide = item.scoreboardTags.contains("NI-Hide")
        // 不隐藏的话给予提示
        if (hide) return
        // 通过actionbar进行对应提示
        ConfigManager.config.getString("Messages.invalidOwnerMessage")?.let {
            when (ConfigManager.config.getString("ItemOwner.messageType")) {
                "actionbar" -> player.sendActionBar(it.replace("{name}", owner))
                "message" -> player.sendMessage(it.replace("{name}", owner))
            }
        }
    }

    @JvmStatic
    @Listener(eventPriority = EventPriority.HIGH)
    private fun listener(event: EntityPickupItemEvent) {
        // 获取玩家
        val player = event.entity
        if (player !is Player) return
        // 获取拾取物品
        val itemStack = event.item.itemStack
        // 获取NI物品信息(不是NI物品就停止操作)
        val itemInfo = itemStack.isNiItem() ?: return

        try {
            if (itemStack.amount != 0 && itemStack.type != Material.AIR) {
                // 执行物品动作
                ActionManager.pickListener(player, itemStack, itemInfo, event)
            }
        } catch (error: Throwable) {
            error.printStackTrace()
        }

        // 应用对itemStack的操作
        if (itemStack.amount == 0 || itemStack.type == Material.AIR) {
            event.item.remove()
            // 就让Item保持AIR会导致后面监听事件的插件报错, 不如干脆取消事件算了
            event.isCancelled = true
        }
    }
}