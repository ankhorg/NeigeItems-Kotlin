package pers.neige.neigeitems.event

import org.bukkit.OfflinePlayer
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.item.ItemInfo
import taboolib.platform.type.BukkitProxyEvent

/**
 * 物品到期事件, 可取消
 *
 * @property player 持有物品的玩家
 * @property itemStack 到期的物品
 * @property itemInfo 物品信息
 * @constructor 物品到期事件
 */
class ItemExpirationEvent(
    val player: OfflinePlayer?,
    val itemStack: ItemStack,
    val itemInfo: ItemInfo
) : BukkitProxyEvent()