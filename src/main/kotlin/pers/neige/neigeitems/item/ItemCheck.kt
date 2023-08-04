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
import pers.neige.neigeitems.utils.SectionUtils.parseSection
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
        data: MutableMap<String, String>?,
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
                // 检测hashCode匹配情况, 不匹配代表需要更新
                if (item.update && (neigeItems["hashCode"]?.asInt() != item.hashCode)) {
                    // 获取待重构节点
                    val rebuild = hashMapOf<String, String>().also {
                        item.rebuildData?.forEach { (key, value) ->
                            when (value) {
                                is String -> it[key.parseSection(data, player, item.sections)] = value.parseSection(data, player, item.sections)
                                is Number -> it[key.parseSection(data, player, item.sections)] = value.toString()
                            }
                        }
                    }
                    // 获取待刷新节点
                    val refresh = arrayListOf<String>().also {
                        item.refreshData.forEach { key ->
                            it.add(key.parseSection(data, player, item.sections))
                        }
                    }
                    // 进行待重构节点覆盖
                    rebuild.forEach { (key, value) ->
                        data?.set(key, value)
                    }
                    // 进行待刷新节点移除
                    refresh.forEach { key ->
                        data?.remove(key)
                    }
                    // 触发预生成事件
                    val preGenerateEvent = ItemUpdateEvent.PreGenerate(player, itemStack, data, item)
                    preGenerateEvent.call()
                    if (preGenerateEvent.isCancelled) return
                    // 生成新物品
                    preGenerateEvent.item.getItemStack(player, preGenerateEvent.data)?.let { newItemStack ->
                        // 触发生成后事件
                        val postGenerateEvent = ItemUpdateEvent.PostGenerate(player, itemStack, newItemStack)
                        postGenerateEvent.call()
                        if (postGenerateEvent.isCancelled) return
                        // 获取旧物品名称
                        val oldName = itemStack.getName()
                        // 进行物品重构
                        newItemStack.getItemTag().also { newItemTag ->
                            neigeItems["charge"]?.let {
                                newItemTag["NeigeItems"]?.asCompound()?.set("charge", it)
                            }
                            neigeItems["durability"]?.let {
                                newItemTag["NeigeItems"]?.asCompound()?.set("durability", it)
                            }
                            preGenerateEvent.item.protectNBT.forEach { key ->
                                itemTag.getDeepOrNull(key)?.also {
                                    newItemTag.putDeepFixed(key, it)
                                }
                            }
                            newItemTag.saveTo(itemStack)
                        }
                        itemStack.type = newItemStack.type
                        itemStack.durability = newItemStack.durability
                        // 发送提示信息
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