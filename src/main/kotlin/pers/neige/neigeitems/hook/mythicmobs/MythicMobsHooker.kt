package pers.neige.neigeitems.hook.mythicmobs

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import pers.neige.neigeitems.NeigeItems
import pers.neige.neigeitems.manager.ItemManager
import pers.neige.neigeitems.utils.ItemUtils.getItems
import pers.neige.neigeitems.utils.PlayerUtils.setMetadataEZ
import pers.neige.neigeitems.utils.SectionUtils.parseSection
import taboolib.common.platform.event.ProxyListener
import taboolib.module.nms.getItemTag
import kotlin.math.cos
import kotlin.math.sin

abstract class MythicMobsHooker {
    // MM怪物生成事件监听器, 一般不用操作, 放着就行
    abstract val spawnListener: ProxyListener

    // MM怪物死亡事件监听器, 一般不用操作, 放着就行
    abstract val deathListener: ProxyListener

    // 获取MM物品
    abstract fun getItemStack(id: String): ItemStack?

    // 同步获取MM物品(在5.1.0左右的版本中, MM物品的获取强制同步)
    // 不一定真的同步获取, 只在必要时同步(指高版本)
    abstract fun getItemStackSync(id: String): ItemStack?

    // 释放MM技能
    abstract fun castSkill(entity: Entity, skill: String)

    // 获取所有MM物品ID
    abstract fun getItemIds(): List<String>

    /**
     * 根据掉落信息加载掉落物品
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

    /**
     * 根据掉落信息加载掉落物品
     * @param dropItems 用于存储待掉落物品
     * @param drops 掉落信息
     * @param player 用于解析物品的玩家
     */
    fun loadDrops(
        dropItems: ArrayList<ItemStack>,
        drops: List<String>,
        player: Player? = null
    ) {
        for (drop in drops) {
            val args = drop.parseSection(player).split(" ")

            val data: String? = when {
                args.size > 4 -> args.subList(4, args.size).joinToString(" ")
                else -> null
            }

            // 获取概率并进行概率随机
            if (args.size > 2) {
                val probability = args[2].toDoubleOrNull()
                if (probability != null && Math.random() > probability) continue
            }
            // 如果NI和MM都不存在对应物品就跳过去
            if (!ItemManager.hasItem(args[0]) && getItemStackSync(args[0]) != null) continue

            // 获取掉落数量
            var amount = 1
            if (args.size > 1) {
                if (args[1].contains("-")) {
                    val index = args[1].indexOf("-")
                    val min = args[1].substring(0, index).toIntOrNull()
                    val max = args[1].substring(index+1, args[1].length).toIntOrNull()
                    if (min != null && max != null) {
                        amount = (min + Math.round(Math.random()*(max-min))).toInt()
                    }
                } else {
                    args[1].toIntOrNull()?.let {
                        amount = it
                    }
                }
            }
            // 看看需不需要每次都随机生成
            if (args.size > 3 && args[3] == "false") {
                // 真只随机一次啊?那嗯怼吧
                ItemManager.getItemStack(args[0], player, data)?.let { itemStack ->
                    val maxStackSize = itemStack.maxStackSize
                    itemStack.amount = maxStackSize
                    var givenAmt = 0
                    while ((givenAmt + maxStackSize) <= amount) {
                        dropItems.add(itemStack.clone())
                        givenAmt += maxStackSize
                    }
                    if (givenAmt < amount) {
                        itemStack.amount = amount - givenAmt
                        dropItems.add(itemStack.clone())
                    }
                }
            } else {
                // 随机生成, 那疯狂造就完事儿了
                when {
                    ItemManager.hasItem(args[0]) -> {
                        repeat(amount) {
                            ItemManager.getItemStack(args[0], player, data)?.let { itemStack ->
                                dropItems.add(itemStack)
                            }
                        }
                    }
                    else -> {
                        getItemStackSync(args[0])?.getItems(amount)?.forEach { dropItems.add(it) }
                    }
                }
            }
        }
    }
}