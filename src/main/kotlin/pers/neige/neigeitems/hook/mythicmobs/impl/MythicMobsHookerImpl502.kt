package pers.neige.neigeitems.hook.mythicmobs.impl

import io.lumine.mythic.bukkit.MythicBukkit
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent
import io.lumine.mythic.bukkit.events.MythicMobSpawnEvent
import io.lumine.mythic.core.config.MythicConfigImpl
import io.lumine.mythic.core.items.ItemExecutor
import io.lumine.mythic.utils.config.file.FileConfiguration
import io.lumine.mythic.utils.config.file.YamlConfiguration
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.NeigeItems.bukkitScheduler
import pers.neige.neigeitems.NeigeItems.plugin
import pers.neige.neigeitems.hook.mythicmobs.MythicMobsHooker
import pers.neige.neigeitems.manager.ConfigManager
import pers.neige.neigeitems.manager.ItemManager.getItemStack
import pers.neige.neigeitems.manager.ItemManager.hasItem
import pers.neige.neigeitems.manager.ItemPackManager.itemPacks
import pers.neige.neigeitems.utils.ItemUtils.dropItems
import pers.neige.neigeitems.utils.ItemUtils.loadItems
import pers.neige.neigeitems.utils.SectionUtils.parseSection
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.function.registerBukkitListener
import taboolib.common.platform.function.submit
import taboolib.module.nms.ItemTagData
import taboolib.module.nms.getItemTag
import java.util.*

/**
 * 5.0.2版本MM挂钩
 *
 * @constructor 启用5.0.2版本MM挂钩
 */
class MythicMobsHookerImpl502 : MythicMobsHooker() {
    private val test = YamlConfiguration()

    private val itemManager: ItemExecutor = MythicBukkit.inst().itemManager

    private val apiHelper = MythicBukkit.inst().apiHelper

    override val spawnListener = registerBukkitListener(MythicMobSpawnEvent::class.java, EventPriority.HIGH, false) {
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
                        ConfigManager.config.getString("Messages.equipFailed")?.let { message ->
                            println(message
                                .replace("{mobID}", it.mobType.internalName)
                                .replace("{itemID}", args[0]))
                        }
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
            val mythicConfig = mythicMob.config as MythicConfigImpl
            val fc = mythicConfig::class.java.getDeclaredField("fc")
            fc.isAccessible = true
            // 获取MM怪物的ConfigurationSection
            val fileConfiguration: FileConfiguration = fc.get(mythicConfig) as FileConfiguration
            // 获取MM怪物的ConfigurationSection
            val configSection = fileConfiguration.getConfigurationSection(mythicId)

            // 如果怪物配置了NeigeItems相关信息
            if (configSection.contains("NeigeItems")) {
                // 获取被击杀的MM怪物
                val entity = it.entity as LivingEntity
                // 获取击杀者
                val player = it.killer
                // 预定于掉落物列表
                val dropItems = ArrayList<ItemStack>()
                // 掉落应该掉落的装备
                loadEquipmentDrop(entity, dropItems, player)

                // 获取NeigeItems相关配置项
                val neigeItems = configSection.getConfigurationSection("NeigeItems")

                var fancy = false
                var offsetXString: String? = null
                var offsetYString: String? = null
                var angleType: String? = null

                // 如果配置了掉落物品包
                if (neigeItems.contains("DropPacks")) {
                    // 获取物品包信息
                    val drops = neigeItems.getStringList("DropPacks")
                    drops.forEach { id ->
                        itemPacks[id.parseSection(if (player is Player) player else null)]?.let { itemPack ->
                            // 尝试加载多彩掉落
                            if (itemPack.fancyDrop) {
                                fancy = true
                                offsetXString = itemPack.offsetXString
                                offsetYString = itemPack.offsetYString
                                angleType = itemPack.angleType
                            }
                            // 如果是玩家
                            if (player is Player) {
                                // 加载物品掉落信息
                                loadItems(dropItems, itemPack.items, player, HashMap<String, String>(), itemPack.sections)
                            }
                        }
                    }
                }
                // 判断是否是玩家击杀
                if (player is Player) {
                    // 如果配置了掉落相关信息
                    if (neigeItems.contains("Drops")) {
                        // 获取掉落相关的配置项
                        val drops = neigeItems.getStringList("Drops")
                        // 加载掉落信息
                        loadItems(dropItems, drops, player)
                    }
                }

                // 如果配置了多彩掉落信息
                if (neigeItems.contains("FancyDrop")) {
                    // 获取多彩掉落相关信息
                    val fancyDrop = neigeItems.getConfigurationSection("FancyDrop")
                    // 获取掉落偏移信息
                    val offset = fancyDrop.getConfigurationSection("offset")
                    // 多彩掉落
                    dropItems(dropItems, entity.location, player, offset.getString("x"), offset.getString("y"), fancyDrop.getString("angle.type"))
                // 如果物品包中含有多彩掉落信息
                } else if (fancy) {
                    // 多彩掉落
                    dropItems(dropItems, entity.location, player, offsetXString, offsetYString, angleType)
                } else {
                    // 普通掉落
                    dropItems(dropItems, entity.location, player)
                }
            }
        }
    }

    override fun getItemStack(id: String): ItemStack? {
        return itemManager.getItemStack(id)
    }

    override fun getItemStackSync(id: String): ItemStack? {
        return bukkitScheduler.callSyncMethod(plugin) {
            itemManager.getItemStack(id)
        }.get()
    }

    override fun castSkill(entity: Entity, skill: String, trigger: Entity?) {
        apiHelper.castSkill(entity, skill, trigger, entity.location, null, null, 1.0F)
    }

    override fun getItemIds(): List<String> {
        return itemManager.itemNames.toList()
    }
}