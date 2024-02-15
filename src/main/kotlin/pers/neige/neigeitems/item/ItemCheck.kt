package pers.neige.neigeitems.item

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.event.ItemExpirationEvent
import pers.neige.neigeitems.event.ItemUpdateEvent
import pers.neige.neigeitems.manager.ConfigManager.config
import pers.neige.neigeitems.manager.ItemManager
import pers.neige.neigeitems.utils.ItemUtils.getName
import pers.neige.neigeitems.utils.ItemUtils.getNbt
import pers.neige.neigeitems.utils.LangUtils.sendLang
import pers.neige.neigeitems.utils.PlayerUtils.getMetadataEZ
import pers.neige.neigeitems.utils.PlayerUtils.setMetadataEZ
import pers.neige.neigeitems.utils.SectionUtils.parseSection

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
        itemInfo: ItemInfo
    ) {
        kotlin.runCatching {
            val id = itemInfo.id
            val itemTag = itemInfo.itemTag
            val neigeItems = itemInfo.neigeItems
            val nbtItemStack = itemInfo.nbtItemStack
            // 检测过期物品
            if (neigeItems.containsKey("itemTime")) {
                if (System.currentTimeMillis() >= neigeItems.getLong("itemTime")) {
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
                if (item.update && neigeItems.containsKey("hashCode") && (neigeItems.getInt("hashCode") != item.hashCode)) {
                    val data = itemInfo.data
                    // 获取待重构节点
                    val rebuild = hashMapOf<String, String>().also {
                        item.rebuildData?.forEach { (key, value) ->
                            when (value) {
                                is String -> it[key.parseSection(data, player, item.sections)] =
                                    value.parseSection(data, player, item.sections)

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
                        data.set(key, value)
                    }
                    // 进行待刷新节点移除
                    refresh.forEach { key ->
                        data.remove(key)
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

                        // 获取新物品的NBT
                        newItemStack.getNbt().also { newItemTag ->
                            // 把NeigeItems的特殊NBT掏出来
                            newItemTag.getCompound("NeigeItems")?.also { newNeigeItems ->
                                // 还原物品使用次数
                                if (neigeItems.containsKey("charge")) {
                                    newNeigeItems.putInt("charge", neigeItems.getInt("charge"))
                                }
                                // 还原物品自定义耐久
                                if (neigeItems.containsKey("durability")) {
                                    newNeigeItems.putInt("durability", neigeItems.getInt("durability"))
                                }
                                // 修复保护NBT
                                preGenerateEvent.item.protectNBT.forEach { key ->
                                    itemTag.getDeep(key)?.also {
                                        newItemTag.putDeep(key, it)
                                    }
                                }
                                // 将新物品的NBT覆盖至原物品
                                nbtItemStack.setTag(newItemTag)
                            }
                        }
                        // 还原物品类型
                        itemStack.type = newItemStack.type
                        // 还原损伤值(1.12.2需要)
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
        val lastTime = this.getMetadataEZ("NI-CheckInvCD", 0.toLong()) as Long
        // 如果仍处于冷却时间
        if ((lastTime + 1000) > time) {
            return false
        }
        this.setMetadataEZ("NI-CheckInvCD", time)
        return true
    }
}