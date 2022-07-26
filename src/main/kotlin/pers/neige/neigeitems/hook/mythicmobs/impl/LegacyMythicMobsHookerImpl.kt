package pers.neige.neigeitems.hook.mythicmobs.impl

import io.lumine.utils.config.file.FileConfiguration
import io.lumine.utils.config.file.YamlConfiguration
import io.lumine.xikage.mythicmobs.MythicMobs
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobSpawnEvent
import io.lumine.xikage.mythicmobs.io.MythicConfig
import io.lumine.xikage.mythicmobs.items.ItemManager
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import pers.neige.neigeitems.NeigeItems.bukkitScheduler
import pers.neige.neigeitems.NeigeItems.plugin
import pers.neige.neigeitems.hook.mythicmobs.MythicMobsHooker
import pers.neige.neigeitems.manager.ItemManager.getItemStack
import pers.neige.neigeitems.manager.ItemManager.hasItem
import pers.neige.neigeitems.utils.PlayerUtils.setMetadataEZ
import pers.neige.neigeitems.utils.SectionUtils.parseSection
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.function.registerBukkitListener
import taboolib.common.platform.function.submit
import taboolib.module.nms.ItemTagData
import taboolib.module.nms.getItemTag
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class LegacyMythicMobsHookerImpl : MythicMobsHooker() {
    private val test = YamlConfiguration()

    private val itemManager: ItemManager = MythicMobs.inst().itemManager

    private val apiHelper = MythicMobs.inst().apiHelper

    private val pluginManager = Bukkit.getPluginManager()

    override val spawnListener = registerBukkitListener(MythicMobSpawnEvent::class.java, EventPriority.LOWEST, false) {
        submit(async = true) {
            val entity = it.entity as LivingEntity
            val config = it.mobType.config.getNestedConfig("NeigeItems")
            val equipment = config.getStringList("Equipment")
            val dropEquipment = config.getStringList("DropEquipment")
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
                    if (!hasItem(args[0])) continue

                    try {
                        getItemStack(args[0], null, data)?.let { itemStack ->
                            dropChance[slot]?.let { chance ->
                                val itemTag = itemStack.getItemTag()
                                itemTag["NeigeItems"]?.asCompound()?.set("dropChance", ItemTagData(chance))
                                itemTag.saveTo(itemStack)
                            }

                            when (slot) {
                                "helmet" -> entityEquipment?.helmet = itemStack
                                "chestplate" -> entityEquipment?.chestplate = itemStack
                                "leggings" -> entityEquipment?.leggings = itemStack
                                "boots" -> entityEquipment?.boots = itemStack
                                "mainhand" -> entityEquipment?.setItemInMainHand(itemStack)
                                "offhand" -> entityEquipment?.setItemInOffHand(itemStack)
                                else -> {}
                            }
                        }
                    } catch (error: Throwable) {
                        print("§e[NI] §6在尝试给ID为 §f${it.mobType.internalName}§6 的MM怪物穿戴ID为 §f${args[0]}§6 的NI物品时发生了错误.")
                        error.printStackTrace()
                    }
                }
            }
        }
    }

    override val deathListener = registerBukkitListener(MythicMobDeathEvent::class.java, EventPriority.NORMAL, false) {
        submit(async = true) {
            // 获取MM怪物配置
            val mythicMob = it.mobType
            // 获取MM怪物ID
            val mythicId = mythicMob.internalName
            // 获取对应MythicConfig
            val mythicConfig: MythicConfig = mythicMob.config
            val fc = mythicConfig::class.java.getDeclaredField("fc")
            fc.isAccessible = true
            // 获取MM怪物的ConfigurationSection
            val fileConfiguration: FileConfiguration = fc.get(mythicConfig) as FileConfiguration
            val configSection = fileConfiguration.getConfigurationSection(mythicId)

            // 如果怪物配置了NeigeItems相关信息
            if (configSection.contains("NeigeItems")) {
                // 获取被击杀的MM怪物
                val entity = it.entity as LivingEntity
                // 获取击杀者
                val player = it.killer
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

                // 预定于掉落物列表
                val dropItems = ArrayList<ItemStack>()
                // 遍历怪物身上的装备, 看看哪个是生成时自带的需要掉落的NI装备
                for (itemStack in equipments) {
                    if (itemStack.type != Material.AIR) {
                        val itemTag = itemStack.getItemTag()

                        itemTag["NeigeItems"]?.asCompound()?.let { neigeItems ->
                            neigeItems["dropChance"]?.asDouble()?.let {
                                if (Math.random() <= it) {
                                    val id = neigeItems["id"]?.asString()
                                    if (id != null) {
                                        getItemStack(id, player as OfflinePlayer, neigeItems["data"]?.asString())?.let {
                                            // 丢进待掉落列表里
                                            dropItems.add(it)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // 获取NeigeItems相关配置项
                val neigeItems = configSection.getConfigurationSection("NeigeItems")
                // 判断是否是玩家击杀
                if (player is Player) {
                    // 如果配置了掉落相关信息
                    if (neigeItems.contains("Drops")) {
                        // 获取掉落相关的配置项
                        val drops = neigeItems.getStringList("Drops")

                        // 遍历相关配置
                        for (drop in drops) {
                            val args = drop.parseSection(player).split(" ")

                            val data: String? = when {
                                args.size > 4 -> args.slice(4..args.size).joinToString(" ")
                                else -> null
                            }

                            // 获取概率并进行概率随机
                            if (args.size > 2) {
                                val probability = args[2].toDoubleOrNull()
                                if (probability != null && Math.random() > probability) continue
                            }
                            // 如果NI和MM都不存在对应物品就跳过去
                            if (!hasItem(args[0]) && !itemManager.getItem(args[0]).isPresent) continue

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
                                getItemStack(args[0], player, data)?.let { itemStack ->
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
                                for (index in 0..amount) {
                                    getItemStack(args[0], player, data)?.let { itemStack ->
                                        dropItems.add(itemStack)
                                    } ?: let {
                                        itemManager.getItemStack(args[0])?.let { itemStack ->
                                            dropItems.add(itemStack)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // 如果配置了多彩掉落信息
                if (neigeItems.contains("FancyDrop")) {
                    // 获取多彩掉落相关信息
                    val fancyDrop = neigeItems.getConfigurationSection("FancyDrop")
                    // 获取掉落偏移信息
                    val offset = fancyDrop.getConfigurationSection("offset")
                    // 获取横向偏移量
                    val offsetXString = offset.getString("x").parseSection(player?.let { it as OfflinePlayer })
                    val offsetX: Double = if (offsetXString.contains("-")) {
                        val index = offsetXString.indexOf("-")
                        val min = offsetXString.substring(0, index).toDoubleOrNull()
                        val max = offsetXString.substring(index+1).toDoubleOrNull()
                        when {
                            min != null && max != null -> min + Math.random()*(max-min)
                            else -> 0.1
                        }
                    } else {
                        offsetXString.toDoubleOrNull() ?: 0.1
                    }
                    // 获取纵向偏移量
                    val offsetYString = offset.getString("y").parseSection(player?.let { it as OfflinePlayer })
                    val offsetY: Double = if (offsetYString.contains("-")) {
                        val index = offsetYString.indexOf("-")
                        val min = offsetYString.substring(0, index).toDoubleOrNull()
                        val max = offsetYString.substring(index+1).toDoubleOrNull()
                        when {
                            min != null && max != null -> min + Math.random()*(max-min)
                            else -> 0.1
                        }
                    } else {
                        offsetYString.toDoubleOrNull() ?: 0.1
                    }
                    // 获取发射角度类型
                    val angleType = fancyDrop.getString("angle.type").parseSection(player?.let { it as OfflinePlayer })
                    // 获取怪物死亡位置
                    val location = entity.location
                    // 开始掉落
                    for ((index, itemStack) in dropItems.withIndex()) {
                        val itemTag = itemStack.getItemTag()

                        bukkitScheduler.callSyncMethod(plugin) {
                            val item = location.world?.dropItem(location, itemStack)
                            itemTag["NeigeItems"]?.asCompound()?.let { neigeItems ->
                                neigeItems["owner"]?.asString()?.let { owner ->
                                    item?.setMetadataEZ("NI-Owner", owner)
                                }
                            }
                            item
                        }.get()?.let { item ->
                            val vector = Vector(offsetX, offsetY, 0.0)
                            if (angleType == "random") {
                                val angleCos = cos(Math.PI * 2 * Math.random())
                                val angleSin = sin(Math.PI * 2 * Math.random())
                                val x = angleCos * vector.x + angleSin * vector.z
                                val z = -angleSin * vector.x + angleCos * vector.z
                                vector.setX(x).z = z
                            } else if (angleType == "round") {
                                val angleCos = cos(Math.PI * 2 * index/dropItems.size)
                                val angleSin = sin(Math.PI * 2 * index/dropItems.size)
                                val x = angleCos * vector.x + angleSin * vector.z
                                val z = -angleSin * vector.x + angleCos * vector.z
                                vector.setX(x).z = z
                            }
                            item.velocity = vector

                            itemTag["NeigeItems"]?.asCompound()?.let { neigeItems ->
                                neigeItems["dropSkill"]?.asString()?.let { dropSkill ->
                                    if (pluginManager.isPluginEnabled("MythicMobs")) {
                                        castSkill(item, dropSkill)
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // 普通掉落
                    for (itemStack in dropItems) {
                        val location = entity.location
                        val itemTag = itemStack.getItemTag()

                        bukkitScheduler.callSyncMethod(plugin) {
                            val item = location.world?.dropItem(location, itemStack)
                            itemTag["NeigeItems"]?.asCompound()?.let { neigeItems ->
                                neigeItems["owner"]?.asString()?.let { owner ->
                                    item?.setMetadataEZ("NI-Owner", owner)
                                }
                            }
                            item
                        }.get()?.let { item ->
                            itemTag["NeigeItems"]?.asCompound()?.let { neigeItems ->
                                neigeItems["dropSkill"]?.asString()?.let { dropSkill ->
                                    if (pluginManager.isPluginEnabled("MythicMobs")) {
                                        castSkill(item, dropSkill)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun getItemStack(id: String): ItemStack? {
        return itemManager.getItemStack(id)
    }

    // 这个版本并不需要同步获取物品
    override fun getItemStackSync(id: String): ItemStack? {
        return itemManager.getItemStack(id)
    }

    override fun castSkill(entity: Entity, skill: String) {
        apiHelper.castSkill(entity, skill)
    }

    override fun getItemIds(): List<String> {
        return itemManager.itemNames.toList()
    }
}