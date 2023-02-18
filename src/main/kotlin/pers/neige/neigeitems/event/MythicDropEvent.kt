package pers.neige.neigeitems.event

import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.item.ItemPack
import taboolib.platform.type.BukkitProxyEvent

/**
 * MM怪物掉落NI装备事件, MM怪物死亡时异步触发
 */
class MythicDropEvent {
    /**
     * 加载好配置, 还没生成物品时触发, 可取消
     *
     * @property internalName 怪物ID
     * @property entity 怪物实体(你可能需要通过编辑它身上的装备来改变掉落装备)
     * @property killer 怪物击杀者
     * @property drops NeigeItems.Drop
     * @property dropPacks NeigeItems.DropPacks
     * @property offsetXString NeigeItems.FancyDrop.offset.x(可能在后续被物品包配置覆盖)
     * @property offsetYString NeigeItems.FancyDrop.offset.y(可能在后续被物品包配置覆盖)
     * @property angleType NeigeItems.FancyDrop.offset.angle.type(可能在后续被物品包配置覆盖)
     * @constructor MM怪物掉落NI装备配置加载事件
     */
    class ConfigLoaded(
        val internalName: String,
        val entity: LivingEntity,
        val killer: LivingEntity?,
        var drops: List<String>?,
        var dropPacks: List<String>?,
        var offsetXString: String?,
        var offsetYString: String?,
        var angleType: String?
    ) : BukkitProxyEvent()

    /**
     * 生成物品后, 准备掉落前触发
     *
     * @property internalName 怪物ID
     * @property entity 怪物实体
     * @property player 怪物击杀者
     * @property dropItems 待掉落物品
     * @property offsetXString 多彩掉落横向偏移
     * @property offsetYString 多彩掉落纵向偏移
     * @property angleType 多彩掉落喷射模式
     * @constructor MM怪物掉落NI装备事件
     */
    class Drop(
        val internalName: String,
        val entity: LivingEntity,
        val player: Player,
        var dropItems: List<ItemStack>,
        var offsetXString: String?,
        var offsetYString: String?,
        var angleType: String?
    ) : BukkitProxyEvent()
}