package pers.neige.neigeitems.listener

import org.bukkit.Material
import org.bukkit.event.player.PlayerDropItemEvent
import pers.neige.neigeitems.item.ItemCheck
import pers.neige.neigeitems.manager.ActionManager
import pers.neige.neigeitems.utils.ItemUtils.isNiItem
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.nms.ItemTag

object PlayerDropItemListener {
    @SubscribeEvent(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun listener(event: PlayerDropItemEvent) {
        // 获取玩家
        val player = event.player
        // 获取掉落物品
        val itemStack = event.itemDrop.itemStack
        // 获取NI物品信息(不是NI物品就停止操作)
        val itemInfo = itemStack.isNiItem(true) ?: return
        // 物品NBT
        val itemTag: ItemTag = itemInfo.itemTag
        // NI物品数据
        val neigeItems: ItemTag = itemInfo.neigeItems
        // NI物品id
        val id: String = itemInfo.id
        // NI节点数据
        val data: HashMap<String, String> = itemInfo.data!!

        // 检测物品过期, 检测物品更新
        ItemCheck.checkItem(player, itemStack, itemInfo, itemTag, neigeItems, id, data)
        if (itemStack.amount != 0 && itemStack.type != Material.AIR) {
            // 执行物品动作
            ActionManager.dropListener(player, itemStack, itemTag, neigeItems, id, data, event)
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