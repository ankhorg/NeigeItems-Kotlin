package pers.neige.neigeitems.event

import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.ItemStack
import taboolib.platform.type.BukkitProxyEvent

/**
 * 物品生成事件(可修改其中物品), 不可取消
 *
 * @property id 物品ID
 * @property player 用于解析物品的玩家
 * @property itemStack 生成的物品
 * @property cache 节点缓存
 * @property configSection 物品配置(经过了前后两次papi解析, 不包含节点配置)
 * @property sections 节点配置(经过了一次papi解析)
 * @constructor 物品生成事件
 */
class ItemGeneratorEvent(
    val id: String,
    val player: OfflinePlayer?,
    var itemStack: ItemStack,
    val cache: HashMap<String, String>,
    val configSection: ConfigurationSection,
    val sections: ConfigurationSection?
    ) : BukkitProxyEvent() {
    override val allowCancelled: Boolean
        get() = false
}