package pers.neige.neigeitems.hook.mythicmobs

import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.manager.ItemManager
import taboolib.common.platform.event.ProxyListener
import taboolib.module.nms.getItemTag

/**
 * MM挂钩
 */
abstract class MythicMobsHooker {
    // MM怪物生成事件监听器, 一般不用操作, 放着就行
    abstract val spawnListener: ProxyListener

    // MM怪物死亡事件监听器, 一般不用操作, 放着就行
    abstract val deathListener: ProxyListener

    /**
     * 获取MM物品, 不存在对应ID的MM物品则返回null
     *
     * @param id MM物品ID
     * @return MM物品(不存在则返回null)
     */
    abstract fun getItemStack(id: String): ItemStack?

    /**
     * 同步获取MM物品, 不存在对应ID的MM物品则返回null(在5.1.0左右的版本中, MM物品的获取强制同步)
     * 不一定真的同步获取, 只在必要时同步(指高版本)
     *
     * @param id MM物品ID
     * @return MM物品(不存在则为空)
     */
    abstract fun getItemStackSync(id: String): ItemStack?

    /**
     * 释放MM技能
     *
     * @param entity 技能释放者
     * @param skill 技能ID
     */
    abstract fun castSkill(entity: Entity, skill: String, trigger: Entity? = null)

    /**
     * 获取所有MM物品ID
     *
     * @return 所有MM物品ID
     */
    abstract fun getItemIds(): List<String>

    /**
     * 根据掉落信息加载掉落物品
     *
     * @param entity 待掉落物品的MM怪物
     * @param dropItems 用于存储待掉落物品
     * @param player 用于解析物品的玩家
     */
    fun loadEquipmentDrop(
        entity: LivingEntity,
        dropItems: ArrayList<ItemStack>,
        player: LivingEntity?
    ) {
        // 获取MM怪物身上的装备
        val entityEquipment = entity.equipment
        // 一个个的全掏出来, 等会儿挨个康康
        val equipments = java.util.ArrayList<ItemStack>()
        entityEquipment?.helmet?.clone()?.let { equipments.add(it) }
        entityEquipment?.chestplate?.clone()?.let { equipments.add(it) }
        entityEquipment?.leggings?.clone()?.let { equipments.add(it) }
        entityEquipment?.boots?.clone()?.let { equipments.add(it) }
        entityEquipment?.itemInMainHand?.clone()?.let { equipments.add(it) }
        entityEquipment?.itemInOffHand?.clone()?.let { equipments.add(it) }

        // 遍历怪物身上的装备, 看看哪个是生成时自带的需要掉落的NI装备
        for (itemStack in equipments) {
            if (itemStack.type != Material.AIR) {
                val itemTag = itemStack.getItemTag()

                itemTag["NeigeItems"]?.asCompound()?.let { neigeItems ->
                    neigeItems["dropChance"]?.asDouble()?.let {
                        if (Math.random() <= it) {
                            val id = neigeItems["id"]?.asString()
                            if (id != null) {
                                val target = when (player) {
                                    is OfflinePlayer -> player
                                    else -> null
                                }
                                ItemManager.getItemStack(id, target, neigeItems["data"]?.asString())?.let {
                                    // 丢进待掉落列表里
                                    dropItems.add(it)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}