package pers.neige.neigeitems.event

import org.bukkit.OfflinePlayer
import org.bukkit.inventory.ItemStack
import taboolib.platform.type.BukkitProxyEvent

/**
 * 物品更新事件, 可取消
 *
 * @property player 持有物品的玩家
 * @property oldItem 待更新物品
 * @property newItem 待覆盖物品
 * @constructor 物品更新事件
 */
class ItemUpdateEvent(
    val player: OfflinePlayer?,
    val oldItem: ItemStack,
    val newItem: ItemStack
) : BukkitProxyEvent()