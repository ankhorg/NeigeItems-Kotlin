package pers.neige.neigeitems.listener

import org.bukkit.Material
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerDropItemEvent
import pers.neige.neigeitems.annotations.Listener
import pers.neige.neigeitems.item.ItemCheck
import pers.neige.neigeitems.manager.ActionManager
import pers.neige.neigeitems.utils.ItemUtils.isNiItem

object PlayerDropItemListener {
    @Listener(eventPriority = EventPriority.LOWEST, ignoreCancelled = true)
    fun listener(event: PlayerDropItemEvent) {
        // 获取玩家
        val player = event.player
        // 获取掉落物品
        val itemStack = event.itemDrop.itemStack
        // 获取NI物品信息(不是NI物品就停止操作)
        val itemInfo = itemStack.isNiItem() ?: return

        try {
            // 检测物品过期, 检测物品更新
            ItemCheck.checkItem(player, itemStack, itemInfo)
            if (itemStack.amount != 0 && itemStack.type != Material.AIR) {
                // 执行物品动作
                ActionManager.dropListener(player, itemStack, itemInfo, event)
            }
        } catch (error: Throwable) {
            error.printStackTrace()
        }

        // 应用对itemStack的操作
        if (itemStack.amount == 0 || itemStack.type == Material.AIR) {
            event.itemDrop.remove()
            // 就让Item保持AIR会导致后面监听事件的插件报错, 不如干脆取消事件算了
            event.isCancelled = true
        } else {
            event.itemDrop.itemStack = itemStack
        }
    }
}