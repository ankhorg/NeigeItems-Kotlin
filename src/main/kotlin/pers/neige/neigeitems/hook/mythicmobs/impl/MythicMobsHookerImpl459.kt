package pers.neige.neigeitems.hook.mythicmobs.impl

import io.lumine.utils.config.file.FileConfiguration
import io.lumine.xikage.mythicmobs.MythicMobs
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobSpawnEvent
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicReloadedEvent
import io.lumine.xikage.mythicmobs.io.MythicConfig
import io.lumine.xikage.mythicmobs.items.ItemManager
import io.lumine.xikage.mythicmobs.mobs.MobManager
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.event.MobInfoReloadedEvent
import pers.neige.neigeitems.hook.mythicmobs.MythicMobsHooker
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.function.registerBukkitListener
import taboolib.common.platform.function.submit

/**
 * 4.5.9版本MM挂钩
 *
 * @constructor 启用4.5.9版本MM挂钩
 */
class MythicMobsHookerImpl459 : MythicMobsHooker() {
    override val version = "459"

    override val spawnEventClass = MythicMobSpawnEvent::class.java

    override val deathEventClass = MythicMobDeathEvent::class.java

    override val reloadEventClass = MythicReloadedEvent::class.java

    private val itemManager: ItemManager = MythicMobs.inst().itemManager

    private val mobManager: MobManager = MythicMobs.inst().mobManager

    override val mobInfos = HashMap<String, ConfigurationSection>()

    private val apiHelper = MythicMobs.inst().apiHelper

    override val spawnListener = registerBukkitListener(MythicMobSpawnEvent::class.java, EventPriority.HIGH) {
        submit(async = true) {
            spawnEvent(
                it.mobType.internalName,
                it.entity as LivingEntity
            )
        }
    }

    override val deathListener = registerBukkitListener(MythicMobDeathEvent::class.java) {
        submit(async = true) {
            deathEvent(
                it.killer,
                it.entity as LivingEntity,
                it.mobType.internalName
            )
        }
    }

    override val reloadListener = registerBukkitListener(MythicReloadedEvent::class.java) {
        loadMobInfos()
    }

    init {
        loadMobInfos()
    }

    private fun loadMobInfos() {
        submit(async = true) {
            mobInfos.clear()
            mobManager.mobTypes.forEach { mythicMob ->
                val mythicId = mythicMob.internalName
                // 获取对应MythicConfig
                val mythicConfig: MythicConfig = mythicMob.config
                // 不要问我为什么这么干, 谁问谁死
                val fc = mythicConfig::class.java.getDeclaredField("fc")
                fc.isAccessible = true
                // 获取MM怪物的ConfigurationSection
                val fileConfiguration: FileConfiguration = fc.get(mythicConfig) as FileConfiguration
                // 获取MM怪物的ConfigurationSection
                fileConfiguration.getConfigurationSection(mythicId)?.clone()?.let {
                    mobInfos[mythicId] = it
                }
            }
            MobInfoReloadedEvent().call()
        }
    }

    private fun io.lumine.utils.config.ConfigurationSection.clone(): ConfigurationSection {
        val tempConfigSection = org.bukkit.configuration.file.YamlConfiguration() as ConfigurationSection
        this.getKeys(false).forEach { key ->
            val value = this.get(key)
            if (value is io.lumine.utils.config.ConfigurationSection) {
                tempConfigSection.set(key, value.clone())
            } else {
                tempConfigSection.set(key, value)
            }
        }
        return tempConfigSection
    }

    override fun getItemStack(id: String): ItemStack? {
        return itemManager.getItemStack(id)
    }

    // 这个版本并不需要同步获取物品
    override fun getItemStackSync(id: String): ItemStack? {
        return itemManager.getItemStack(id)
    }

    override fun castSkill(entity: Entity, skill: String, trigger: Entity?) {
        apiHelper.castSkill(entity, skill, trigger, entity.location, null, null, 1.0F)
    }

    override fun getItemIds(): List<String> {
        return itemManager.itemNames.toList()
    }
}