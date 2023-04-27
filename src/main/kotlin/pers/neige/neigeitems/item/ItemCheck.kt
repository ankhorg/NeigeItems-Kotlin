package pers.neige.neigeitems.item

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.NeigeItems.bukkitScheduler
import pers.neige.neigeitems.NeigeItems.plugin
import pers.neige.neigeitems.event.ItemExpirationEvent
import pers.neige.neigeitems.event.ItemUpdateEvent
import pers.neige.neigeitems.manager.ConfigManager.config
import pers.neige.neigeitems.manager.ConfigManager.updateInterval
import pers.neige.neigeitems.manager.HookerManager
import pers.neige.neigeitems.manager.ItemManager
import pers.neige.neigeitems.utils.ItemUtils.getDeepOrNull
import pers.neige.neigeitems.utils.ItemUtils.isNiItem
import pers.neige.neigeitems.utils.ItemUtils.putDeepFixed
import pers.neige.neigeitems.utils.LangUtils.sendLang
import pers.neige.neigeitems.utils.PlayerUtils.getMetadataEZ
import pers.neige.neigeitems.utils.PlayerUtils.setMetadataEZ
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.Schedule
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.nms.getItemTag
import taboolib.module.nms.getName

/**
 * 物品检测器, 用于回收过期物品并更新旧版物品
 */
object ItemCheck {
    @SubscribeEvent(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun listener(e: PlayerDropItemEvent) {
        val itemStack = e.itemDrop.itemStack
        checkItem(e.player, itemStack)
        e.itemDrop.itemStack = itemStack
    }

    @SubscribeEvent(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun listener(e: EntityPickupItemEvent) {
        val entity = e.entity
        if (entity is Player) {
            val itemStack = e.item.itemStack
            checkItem(entity, itemStack)
            e.item.itemStack = itemStack
        }
    }

    @Awake(LifeCycle.ACTIVE)
    fun schedule() {
        if (updateInterval > 0) {
            bukkitScheduler.runTaskTimerAsynchronously(plugin, Runnable {
                Bukkit.getOnlinePlayers().forEach { player -> checkItem(player, player.inventory) }
            }, 0, updateInterval)
        }
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
    fun checkItem(player: Player, itemStack: ItemStack) {
        kotlin.runCatching {
            itemStack.isNiItem()?.let { itemInfo ->
                // 检测过期物品
                itemInfo.neigeItems["itemTime"]?.asLong()?.let {
                    if (System.currentTimeMillis() >= it) {
                        val event = ItemExpirationEvent(player, itemStack, itemInfo)
                        event.call()
                        if (event.isCancelled) return
                        val itemName = itemStack.getName()
                        itemStack.amount = 0
                        config.getString("Messages.itemExpirationMessage")
                            ?.replace("{itemName}", itemName)
                            ?.let(player::sendMessage)
                        // 都过期了，就不用更新了
                        return
                    }
                }
                // 物品更新
                ItemManager.getItem(itemInfo.id)?.let { item ->
                    if (item.update == true && (itemInfo.neigeItems["hashCode"]?.asInt() != item.hashCode)) {
                        ItemManager.getItemStack(itemInfo.id, player, itemInfo.data)?.let { newItemStack ->
                            val event = ItemUpdateEvent(player, itemStack, newItemStack)
                            event.call()
                            if (event.isCancelled) return
                            val oldName = itemStack.getName()
                            newItemStack.getItemTag().also { newItemTag ->
                                itemInfo.neigeItems["charge"]?.let {
                                    newItemTag["NeigeItems"]?.asCompound()?.set("charge", it)
                                }
                                itemInfo.neigeItems["durability"]?.let {
                                    newItemTag["NeigeItems"]?.asCompound()?.set("durability", it)
                                }
                                item.protectNBT.forEach { key ->
                                    itemInfo.itemTag.getDeepOrNull(key)?.also {
                                        newItemTag.putDeepFixed(key, it)
                                    }
                                }
                                newItemTag.saveTo(itemStack)
                            }
                            itemStack.type = newItemStack.type
                            itemStack.durability = newItemStack.durability
                            player.sendLang("Messages.legacyItemUpdateMessage", mapOf("{name}" to oldName))
                        }
                    }
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
    fun checkItem(player: Player, inventory: Inventory) {
        if (player.couldCheckInventory()) {
            inventory.contents.forEach {
                it?.let { checkItem(player, it) }
            }
        }
    }

    /**
     * 当前是否可以检查物品(最多一秒检查一次玩家背包, 防止服务器噶了)
     */
    private fun Player.couldCheckInventory(): Boolean {
        // 获取当前时间
        val time = System.currentTimeMillis()
        // 获取上次检查时间
        val lastTime = this.getMetadataEZ("NI-CheckInvCD", "Long", 0.toLong()) as Long
        // 如果仍处于冷却时间
        if ((lastTime + 1000) > time) {
            return false
        }
        this.setMetadataEZ("NI-CheckInvCD", time)
        return true
    }
}