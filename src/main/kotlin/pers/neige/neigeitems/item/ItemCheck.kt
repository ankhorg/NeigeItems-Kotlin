package pers.neige.neigeitems.item

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.event.ItemExpirationEvent
import pers.neige.neigeitems.event.ItemUpdateEvent
import pers.neige.neigeitems.manager.ConfigManager.config
import pers.neige.neigeitems.manager.ItemManager
import pers.neige.neigeitems.utils.ItemUtils.getDeepOrNull
import pers.neige.neigeitems.utils.ItemUtils.putDeepFixed
import pers.neige.neigeitems.utils.LangUtils.sendLang
import pers.neige.neigeitems.utils.PlayerUtils.getMetadataEZ
import pers.neige.neigeitems.utils.PlayerUtils.setMetadataEZ
import taboolib.module.nms.ItemTag
import taboolib.module.nms.getItemTag
import taboolib.module.nms.getName

/**
 * 物品检测器, 用于回收过期物品并更新旧版物品
 */
object ItemCheck {
    /**
     * 检查物品
     *
     * @param player 待检查玩家
     * @param itemStack 待检查物品
     */
    fun checkItem(
        player: Player,
        itemStack: ItemStack,
        itemInfo: ItemInfo,
        itemTag: ItemTag,
        neigeItems: ItemTag,
        id: String,
        data: HashMap<String, String>?,
    ) {
        kotlin.runCatching {
            // 检测过期物品
            neigeItems["itemTime"]?.asLong()?.let {
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
            ItemManager.getItem(id)?.let { item ->
                if (item.update && (neigeItems["hashCode"]?.asInt() != item.hashCode)) {
                    ItemManager.getItemStack(id, player, data)?.let { newItemStack ->
                        val event = ItemUpdateEvent(player, itemStack, newItemStack)
                        event.call()
                        if (event.isCancelled) return
                        val oldName = itemStack.getName()
                        newItemStack.getItemTag().also { newItemTag ->
                            neigeItems["charge"]?.let {
                                newItemTag["NeigeItems"]?.asCompound()?.set("charge", it)
                            }
                            neigeItems["durability"]?.let {
                                newItemTag["NeigeItems"]?.asCompound()?.set("durability", it)
                            }
                            item.protectNBT.forEach { key ->
                                itemTag.getDeepOrNull(key)?.also {
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

    /**
     * 当前是否可以检查物品(最多一秒检查一次玩家背包, 防止服务器噶了)
     */
    fun Player.couldCheckInventory(): Boolean {
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