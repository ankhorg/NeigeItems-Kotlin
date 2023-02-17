package pers.neige.neigeitems.hook.mythicmobs

import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.event.MythicEquipEvent
import pers.neige.neigeitems.manager.ConfigManager
import pers.neige.neigeitems.manager.HookerManager
import pers.neige.neigeitems.manager.ItemManager
import pers.neige.neigeitems.utils.SectionUtils.parseSection
import taboolib.common.platform.event.ProxyListener
import taboolib.module.nms.ItemTag
import taboolib.module.nms.ItemTagData
import taboolib.module.nms.getItemTag
import java.util.*

/**
 * MM挂钩
 */
abstract class MythicMobsHooker {
    /**
     * MM怪物生成事件监听器, 监听器优先级HIGH, 得以覆盖MM自身的装备操作
     */
    abstract val spawnListener: ProxyListener

    /**
     * MM怪物死亡事件监听器, 监听器优先级NORMAL
     */
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
     * 为MM怪物穿戴装备
     *
     * @param equipment 装备信息
     * @param dropEquipment 掉落装备概率
     * @param entity 怪物实体
     * @param internalName 怪物ID
     */
    fun loadEquipment(
        equipment: List<String>,
        dropEquipment: List<String>,
        entity: LivingEntity,
        internalName: String
    ) {
        val entityEquipment = entity.equipment
        val dropChance = HashMap<String, Double>()

        // 获取死亡后相应NI物品掉落几率
        for (value in dropEquipment) {
            val string = value.parseSection()
            var id = string.lowercase(Locale.getDefault())
            var chance = 1.toDouble()
            if (string.contains(" ")) {
                val index = string.indexOf(" ")
                id = string.substring(0, index).lowercase(Locale.getDefault())
                chance = string.substring(index+1).toDoubleOrNull() ?: 1.toDouble()
            }
            dropChance[id] = chance
        }

        // 获取出生附带装备信息
        for (value in equipment) {
            val string = value.parseSection()
            if (string.contains(": ")) {
                val index = string.indexOf(": ")
                val slot = string.substring(0, index).lowercase(Locale.getDefault())
                val info = string.substring(index+2)
                val args = info.split(" ")

                var data: String? = null
                if (args.size > 2) data = args.drop(2).joinToString(" ")

                if (args.size > 1) {
                    val probability = args[1].toDoubleOrNull()
                    if (probability != null && Math.random() > probability) continue
                }

                try {
                    (ItemManager.getItemStack(args[0], null, data)
                        ?: HookerManager.easyItemHooker?.getItemStack(args[0])
                        ?: getItemStackSync(args[0]))?.let { itemStack ->
                        dropChance[slot]?.let { chance ->
                            val itemTag = itemStack.getItemTag()
                            itemTag.computeIfAbsent("NeigeItems") { ItemTag() }?.asCompound()?.set("dropChance", ItemTagData(chance))
                            itemTag.saveTo(itemStack)
                        }
                        val event = MythicEquipEvent(entity, internalName, slot, itemStack)

                        when (slot) {
                            "helmet" -> {
                                event.call()
                                when {
                                    !event.isCancelled -> entityEquipment?.helmet = event.itemStack
                                    else -> {}
                                }
                            }
                            "chestplate" -> {
                                event.call()
                                when {
                                    !event.isCancelled -> entityEquipment?.chestplate = event.itemStack
                                    else -> {}
                                }
                            }
                            "leggings" -> {
                                event.call()
                                when {
                                    !event.isCancelled -> entityEquipment?.leggings = event.itemStack
                                    else -> {}
                                }
                            }
                            "boots" -> {
                                event.call()
                                when {
                                    !event.isCancelled -> entityEquipment?.boots = event.itemStack
                                    else -> {}
                                }
                            }
                            "mainhand" -> {
                                event.call()
                                when {
                                    !event.isCancelled -> entityEquipment?.setItemInMainHand(event.itemStack)
                                    else -> {}
                                }
                            }
                            "offhand" -> {
                                event.call()
                                when {
                                    !event.isCancelled -> entityEquipment?.setItemInOffHand(event.itemStack)
                                    else -> {}
                                }
                            }
                            else -> {}
                        }
                    }
                } catch (error: Throwable) {
                    ConfigManager.config.getString("Messages.equipFailed")?.let { message ->
                        println(message
                            .replace("{mobID}", internalName)
                            .replace("{itemID}", args[0]))
                    }
                    error.printStackTrace()
                }
            }
        }
    }

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
        val equipments = ArrayList<ItemStack>()
        entityEquipment?.helmet?.clone()?.let { equipments.add(it) }
        entityEquipment?.chestplate?.clone()?.let { equipments.add(it) }
        entityEquipment?.leggings?.clone()?.let { equipments.add(it) }
        entityEquipment?.boots?.clone()?.let { equipments.add(it) }
        entityEquipment?.itemInMainHand?.clone()?.let { equipments.add(it) }
        entityEquipment?.itemInOffHand?.clone()?.let { equipments.add(it) }

        loadEquipmentDrop(equipments, dropItems, player)
    }

    /**
     * 根据掉落信息加载掉落物品
     *
     * @param equipments 怪物身上的装备
     * @param dropItems 用于存储待掉落物品
     * @param player 用于解析物品的玩家
     */
    fun loadEquipmentDrop(
        equipments: ArrayList<ItemStack>,
        dropItems: ArrayList<ItemStack>,
        player: LivingEntity?
    ) {
        // 遍历怪物身上的装备, 看看哪个是生成时自带的需要掉落的NI装备
        for (itemStack in equipments) {
            if (itemStack.type != Material.AIR) {
                val itemTag = itemStack.getItemTag()

                itemTag["NeigeItems"]?.asCompound()?.let { neigeItems ->
                    neigeItems["dropChance"]?.asDouble()?.let {
                        if (Math.random() <= it) {
                            val id = neigeItems["id"]?.asString()
                            // 处理NI物品(根据玩家信息重新生成)
                            if (id != null) {
                                val target = when (player) {
                                    is OfflinePlayer -> player
                                    else -> null
                                }
                                ItemManager.getItemStack(id, target, neigeItems["data"]?.asString())?.let {
                                    // 丢进待掉落列表里
                                    dropItems.add(it)
                                }
                                // 处理MM/EI物品(单纯移除NBT)
                            } else {
                                neigeItems.remove("dropChance")
                                if (neigeItems.isEmpty()) {
                                    itemTag.remove("NeigeItems")
                                }
                                itemTag.saveTo(itemStack)
                                dropItems.add(itemStack)
                            }
                        }
                    }
                }
            }
        }
    }
}