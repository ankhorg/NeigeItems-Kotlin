package pers.neige.neigeitems.event

import org.bukkit.Location
import org.bukkit.OfflinePlayer
import org.bukkit.inventory.ItemStack
import taboolib.platform.type.BukkitProxyEvent

/**
 * 物品包掉落事件, /ni dropPack指令触发, 可取消
 *
 * @property id 物品ID
 * @property itemStacks 待掉落物品
 * @property location 掉落位置
 * @property parser 物品解析对象
 * @constructor 物品包掉落事件
 */
class ItemPackDropEvent(
    val id: String,
    var itemStacks: List<ItemStack>,
    var location: Location,
    val parser: OfflinePlayer?,
) : BukkitProxyEvent()