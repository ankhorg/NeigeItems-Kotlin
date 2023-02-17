package pers.neige.neigeitems.event

import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import taboolib.platform.type.BukkitProxyEvent

/**
 * MM怪物通过NI设置穿戴装备事件, MM怪物生成时触发, 可取消
 *
 * @property entity 怪物实体
 * @property internalName 怪物ID
 * @property slot 槽位(helmet/chestplate/leggings/boots/mainhand/offhand)
 * @property itemStack 待掉落物品
 * @constructor MM怪物通过NI设置穿戴装备事件
 */
class MythicEquipEvent(
    val entity: LivingEntity,
    val internalName: String,
    val slot: String,
    var itemStack: ItemStack,
) : BukkitProxyEvent()