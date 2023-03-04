package pers.neige.neigeitems.item

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerPickupItemEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.event.ItemExpirationEvent
import pers.neige.neigeitems.manager.ConfigManager.config
import pers.neige.neigeitems.utils.ItemUtils.isNiItem
import pers.neige.neigeitems.utils.PlayerUtils.getMetadataEZ
import pers.neige.neigeitems.utils.PlayerUtils.setMetadataEZ
import taboolib.common.platform.Schedule
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.nms.getName

object ItemCheck {
    @SubscribeEvent(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun listener(e: PlayerDropItemEvent) {
        val itemStack = e.itemDrop.itemStack
        checkItem(e.player, itemStack)
        e.itemDrop.itemStack = itemStack
    }

    @SubscribeEvent(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun listener(e: PlayerPickupItemEvent) {
        val itemStack = e.item.itemStack
        checkItem(e.player, itemStack)
        e.item.itemStack = itemStack
    }

    @Schedule(period = 100, async = true)
    fun schedule() {
        Bukkit.getOnlinePlayers().forEach { player -> checkItem(player, player.inventory) }
    }

    @SubscribeEvent
    fun listener(e: PlayerJoinEvent) {
        checkItem(e.player, e.player.inventory)
    }

    @SubscribeEvent
    fun listener(e: PlayerRespawnEvent) {
        checkItem(e.player, e.player.inventory)
    }

    @SubscribeEvent(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun listener(e: InventoryOpenEvent) {
        checkItem(e.player as Player, e.inventory)
    }

    /**
     * 检查物品
     *
     * @param player 待检查玩家
     * @param itemStack 待检查物品
     */
    private fun checkItem(player: Player, itemStack: ItemStack) {
        itemStack.isNiItem()?.let { itemInfo ->
            itemInfo.neigeItems["itemTime"]?.asLong()?.let {
                if (System.currentTimeMillis() >= it) {
                    val event = ItemExpirationEvent(player, itemStack, itemInfo)
                    event.call()
                    if (event.isCancelled) return
                    val itemName = itemStack.getName()
                    itemStack.amount = 0
                    player.sendMessage(config.getString("Messages.itemExpirationMessage")
                        ?.replace("{itemName}", itemName))
                }
            }
        }
    }

    /**
     * 检查物品
     *
     * @param player 待检查玩家
     * @param inventory 待检查背包
     */
    private fun checkItem(player: Player, inventory: Inventory) {
        if (player.couldCheck()) {
            inventory.contents.forEach {
                it?.let { checkItem(player, it) }
            }
        }
    }

    /**
     * 当前是否可以检查物品(最多一秒检查一次玩家背包, 防止服务器噶了)
     */
    private fun Player.couldCheck(): Boolean {
        // 获取当前时间
        val time = System.currentTimeMillis()
        // 获取上次检查时间
        val lastTime = this.getMetadataEZ("NI-CheckItemCD", "Long", 0.toLong()) as Long
        // 如果仍处于冷却时间
        if ((lastTime + 1000) > time) {
            return false
        }
        this.setMetadataEZ("NI-CheckItemCD", time)
        return true
    }
}