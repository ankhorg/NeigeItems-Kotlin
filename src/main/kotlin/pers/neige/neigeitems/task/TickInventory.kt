package pers.neige.neigeitems.task

import org.bukkit.Bukkit
import org.bukkit.Material
import pers.neige.neigeitems.annotation.Schedule
import pers.neige.neigeitems.item.ItemCheck
import pers.neige.neigeitems.manager.ActionManager
import pers.neige.neigeitems.utils.ItemUtils.isNiItem

object TickInventory {
    @JvmStatic
    @Schedule(period = 1, async = true)
    fun schedule() {
        Bukkit.getOnlinePlayers().forEach { player ->
            val inventory = player.inventory
            for (index in 0 until 41) {
                kotlin.runCatching {
                    // 获取物品
                    val itemStack = inventory.getItem(index)
                    // 获取NI物品信息(不是NI物品就停止操作)
                    val itemInfo = itemStack?.isNiItem() ?: return@runCatching

                    // 检测物品过期, 检测物品更新
                    ItemCheck.checkItem(player, itemStack, itemInfo)
                    if (itemStack.amount != 0 && itemStack.type != Material.AIR) {
                        // 执行物品动作
                        ActionManager.tick(player, itemStack, itemInfo, "tick_$index")
                        when (index) {
                            inventory.heldItemSlot -> ActionManager.tick(player, itemStack, itemInfo, "tick_hand")
                            40 -> ActionManager.tick(player, itemStack, itemInfo, "tick_offhand")
                            39 -> ActionManager.tick(player, itemStack, itemInfo, "tick_head")
                            38 -> ActionManager.tick(player, itemStack, itemInfo, "tick_chest")
                            37 -> ActionManager.tick(player, itemStack, itemInfo, "tick_legs")
                            36 -> ActionManager.tick(player, itemStack, itemInfo, "tick_feet")
                        }
                    }
                }
            }
        }
    }
}