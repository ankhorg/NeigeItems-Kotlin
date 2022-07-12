package pers.neige.neigeitems.hook.mythicmobs.impl

import io.lumine.mythic.bukkit.BukkitAdapter
import io.lumine.mythic.bukkit.MythicBukkit
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent
import io.lumine.mythic.bukkit.events.MythicMobSpawnEvent
import io.lumine.mythic.core.items.ItemExecutor
import io.lumine.mythic.core.items.MythicItem
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.hook.mythicmobs.MythicMobsHooker
import pers.neige.neigeitems.manager.ItemManager
import pers.neige.neigeitems.utils.SectionUtils.parseSection
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.function.registerBukkitListener
import taboolib.common.platform.function.submit
import taboolib.module.nms.ItemTagData
import taboolib.module.nms.getItemTag
import java.util.*

class MythicMobsHookerImpl : MythicMobsHooker() {
    private val itemManager: ItemExecutor = MythicBukkit.inst().itemManager

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
                        if (!ItemManager.hasItem(args[0])
                            || (probability != null && Math.random() > probability)) {
                            continue
                        }
                    }

                    try {
                        ItemManager.getItemStack(args[0], null, data)?.let { itemStack ->
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

        }
    }

    override fun getItemStack(id: String): ItemStack? {
        val maybeItem: Optional<MythicItem> = itemManager.getItem(id)
        return when {
            maybeItem.isPresent -> BukkitAdapter.adapt((maybeItem.get()).generateItemStack(1))
            else -> null
        }
    }
}