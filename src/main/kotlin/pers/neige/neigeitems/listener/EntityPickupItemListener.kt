package pers.neige.neigeitems.listener

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityPickupItemEvent
import pers.neige.neigeitems.annotations.Listener
import pers.neige.neigeitems.item.ItemOwner
import pers.neige.neigeitems.manager.ActionManager
import pers.neige.neigeitems.utils.ItemUtils.isNiItem

object EntityPickupItemListener {
    @Listener(eventPriority = EventPriority.LOWEST)
    fun owner(event: EntityPickupItemEvent) {
        // 获取玩家
        val player = event.entity
        if (player !is Player) return

        // 物品拥有者检测
        ItemOwner.check(player, event.item, event)
        if (event.isCancelled) return
    }

    @Listener(eventPriority = EventPriority.HIGH)
    fun listener(event: EntityPickupItemEvent) {
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