package pers.neige.neigeitems.event

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.platform.type.BukkitProxyEvent

/**
 * 物品包给予事件, /ni givePack指令触发, 可取消
 *
 * @property id 物品包ID
 * @property player 物品接收者
 * @property itemStacks 待给予物品
 * @constructor 物品包给予事件
 */
class ItemPackGiveEvent(
    val id: String,
    val player: Player,
    var itemStacks: List<ItemStack>
) : BukkitProxyEvent()