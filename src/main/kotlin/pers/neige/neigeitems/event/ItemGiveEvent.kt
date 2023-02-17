package pers.neige.neigeitems.event

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.platform.type.BukkitProxyEvent

/**
 * 物品给予事件, /ni give指令触发, 可取消
 *
 * @property id 物品ID
 * @property player 物品接收者
 * @property itemStack 待给予物品
 * @property amount 给予数量
 * @constructor 物品给予事件
 */
class ItemGiveEvent(
    val id: String,
    val player: Player,
    var itemStack: ItemStack,
    var amount: Int = itemStack.amount
) : BukkitProxyEvent()