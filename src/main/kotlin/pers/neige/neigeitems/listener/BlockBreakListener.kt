package pers.neige.neigeitems.listener

import bot.inker.bukkit.nbt.NbtCompound
import org.bukkit.Material
import org.bukkit.event.EventPriority
import org.bukkit.event.block.BlockBreakEvent
import pers.neige.neigeitems.annotation.Listener
import pers.neige.neigeitems.item.ItemDurability
import pers.neige.neigeitems.manager.ActionManager
import pers.neige.neigeitems.utils.ItemUtils.isNiItem

object BlockBreakListener {
    @Listener(eventPriority = EventPriority.LOWEST)
    fun breakBlock(event: BlockBreakEvent) {
        // 获取挖掘者
        val player = event.player
        // 获取主手物品
        val itemStack = player.inventory.itemInMainHand
        // 空检测
        if (itemStack.type == Material.AIR) return
        // 获取NI物品信息(不是NI物品就停止操作)
        val itemInfo = itemStack.isNiItem() ?: return
        // NI物品数据
        val neigeItems: NbtCompound = itemInfo.neigeItems

        // 检测已损坏物品
        ItemDurability.basic(player, neigeItems, event)
        if (event.isCancelled) return
        // 执行物品动作
        ActionManager.breakBlockListener(player, itemStack, itemInfo, event)
        if (event.isCancelled) return
    }
}