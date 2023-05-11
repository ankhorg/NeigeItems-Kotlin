package pers.neige.neigeitems.listener

import org.bukkit.Bukkit
import org.bukkit.Material
import pers.neige.neigeitems.item.ItemCheck
import pers.neige.neigeitems.manager.ActionManager
import pers.neige.neigeitems.utils.ItemUtils.isNiItem
import taboolib.common.platform.Schedule
import taboolib.module.nms.ItemTag

object TickInventory {
    @Schedule(period = 1, async = true)
    fun schedule() {
        Bukkit.getOnlinePlayers().forEach { player ->
            val inventory = player.inventory
            for (index in 0 until 41) {
                kotlin.runCatching {
                    // 获取物品
                    val itemStack = inventory.getItem(index)
                    // 获取NI物品信息(不是NI物品就停止操作)
                    val itemInfo = itemStack?.isNiItem(true) ?: return@runCatching
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
                        ActionManager.tick(player, itemStack, itemTag, neigeItems, id, data, "tick_$index")
                        when (index) {
                            inventory.heldItemSlot -> ActionManager.tick(player, itemStack, itemTag, neigeItems, id, data, "tick_hand")
                            40 -> ActionManager.tick(player, itemStack, itemTag, neigeItems, id, data, "tick_offhand")
                            39 -> ActionManager.tick(player, itemStack, itemTag, neigeItems, id, data, "tick_head")
                            38 -> ActionManager.tick(player, itemStack, itemTag, neigeItems, id, data, "tick_chest")
                            37 -> ActionManager.tick(player, itemStack, itemTag, neigeItems, id, data, "tick_legs")
                            36 -> ActionManager.tick(player, itemStack, itemTag, neigeItems, id, data, "tick_feet")
                        }
                    }
                }
            }
        }
    }
}