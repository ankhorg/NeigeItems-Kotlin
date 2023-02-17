package pers.neige.neigeitems.event

import org.bukkit.Location
import org.bukkit.OfflinePlayer
import org.bukkit.inventory.ItemStack
import taboolib.platform.type.BukkitProxyEvent

/**
 * 物品掉落事件, /ni drop指令触发, 可取消
 *
 * @property id 物品ID
 * @property itemStack 待掉落物品
 * @property amount 给予数量
 * @property location 掉落位置
 * @property parser 物品解析对象
 * @constructor 物品掉落事件
 */
class ItemDropEvent(
    val id: String,
    var itemStack: ItemStack,
    var amount: Int = itemStack.amount,
    var location: Location,
    val parser: OfflinePlayer?,
) : BukkitProxyEvent()