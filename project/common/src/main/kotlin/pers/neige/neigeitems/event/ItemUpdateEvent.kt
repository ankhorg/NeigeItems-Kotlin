package pers.neige.neigeitems.event

import org.bukkit.OfflinePlayer
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.item.ItemGenerator
import taboolib.platform.type.BukkitProxyEvent

/**
 * 物品更新事件
 */
class ItemUpdateEvent {
    /**
     * 物品更新事件(生成新物品前触发), 可取消
     *
     * @property player 持有物品的玩家
     * @property oldItem 待更新物品
     * @property data 旧物品内部的指向数据
     * @property item 根据旧物品的物品ID获得的NI物品生成器
     * @constructor 物品更新事件(生成新物品前触发)
     */
    class PreGenerate(
        val player: OfflinePlayer?,
        val oldItem: ItemStack,
        val data: MutableMap<String, String>?,
        val item: ItemGenerator
    ) : BukkitProxyEvent()

    /**
     * 物品更新事件(生成新物品后触发), 可取消
     *
     * @property player 持有物品的玩家
     * @property oldItem 待更新物品
     * @property newItem 待覆盖物品
     * @constructor 物品更新事件(生成新物品后触发)
     */
    class PostGenerate(
        val player: OfflinePlayer?,
        val oldItem: ItemStack,
        val newItem: ItemStack
    ) : BukkitProxyEvent()
}