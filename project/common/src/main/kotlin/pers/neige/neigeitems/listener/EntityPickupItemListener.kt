package pers.neige.neigeitems.listener

import bot.inker.bukkit.nbt.NbtCompound
import bot.inker.bukkit.nbt.NbtItemStack
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityPickupItemEvent
import pers.neige.neigeitems.item.ItemCheck
import pers.neige.neigeitems.item.ItemOwner
import pers.neige.neigeitems.manager.ActionManager
import pers.neige.neigeitems.utils.ItemUtils.isNiItem
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent

object EntityPickupItemListener {
    @SubscribeEvent(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun owner(event: EntityPickupItemEvent) {
        // 获取玩家
        val player = event.entity
        if (player !is Player) return

        // 物品拥有者检测
        ItemOwner.check(player, event.item, event)
        if (event.isCancelled) return
    }

    @SubscribeEvent(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun listener(event: EntityPickupItemEvent) {
        // 获取玩家
        val player = event.entity
        if (player !is Player) return
        // 获取拾取物品
        val itemStack = event.item.itemStack
        // 获取NI物品信息(不是NI物品就停止操作)
        val itemInfo = itemStack.isNiItem(true) ?: return
        // NBT物品
        val nbtItemStack: NbtItemStack = itemInfo.nbtItemStack
        // 物品NBT
        val itemTag: NbtCompound = itemInfo.itemTag
        // NI物品数据
        val neigeItems: NbtCompound = itemInfo.neigeItems
        // NI物品id
        val id: String = itemInfo.id
        // NI节点数据
        val data: HashMap<String, String> = itemInfo.data!!

        try {
            // 检测物品过期, 检测物品更新
            ItemCheck.checkItem(player, itemStack, itemInfo)
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
        } else {
            event.item.itemStack = itemStack
        }
    }
}