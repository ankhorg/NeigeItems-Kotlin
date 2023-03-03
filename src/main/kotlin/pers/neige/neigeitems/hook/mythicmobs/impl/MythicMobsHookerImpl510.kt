package pers.neige.neigeitems.hook.mythicmobs.impl

import io.lumine.mythic.api.mobs.MobManager
import io.lumine.mythic.bukkit.MythicBukkit
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent
import io.lumine.mythic.bukkit.events.MythicMobSpawnEvent
import io.lumine.mythic.bukkit.events.MythicReloadedEvent
import io.lumine.mythic.bukkit.utils.config.file.FileConfiguration
import io.lumine.mythic.bukkit.utils.config.file.YamlConfiguration
import io.lumine.mythic.core.config.MythicConfigImpl
import io.lumine.mythic.core.items.ItemExecutor
import org.bukkit.Bukkit
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.NeigeItems.bukkitScheduler
import pers.neige.neigeitems.NeigeItems.plugin
import pers.neige.neigeitems.hook.mythicmobs.MythicMobsHooker
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.function.registerBukkitListener
import taboolib.common.platform.function.submit

/**
 * 5.1.0版本MM挂钩
 *
 * @constructor 启用5.1.0版本MM挂钩
 */
class MythicMobsHookerImpl510 : MythicMobsHooker() {
    private val test = YamlConfiguration()

    private val itemManager: ItemExecutor = MythicBukkit.inst().itemManager

    private val mobManager: MobManager = MythicBukkit.inst().mobManager

    override val mobInfos = HashMap<String, ConfigurationSection>()

    private val apiHelper = MythicBukkit.inst().apiHelper

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
                val mythicConfig = mythicMob.config as MythicConfigImpl
                // 不要问我为什么这么干, 谁问谁死
                val fc = mythicConfig::class.java.getDeclaredField("fc")
                fc.isAccessible = true
                // 获取MM怪物的ConfigurationSection
                val fileConfiguration: FileConfiguration = fc.get(mythicConfig) as FileConfiguration
                // 获取MM怪物的ConfigurationSection
                val configSection = fileConfiguration.getConfigurationSection(mythicId).clone()

                mobInfos[mythicId] = configSection
            }
        }
    }

    private fun io.lumine.mythic.bukkit.utils.config.ConfigurationSection.clone(): ConfigurationSection {
        val tempConfigSection = org.bukkit.configuration.file.YamlConfiguration() as ConfigurationSection
        this.getKeys(false).forEach { key ->
            val value = this.get(key)
            if (value is io.lumine.mythic.bukkit.utils.config.ConfigurationSection) {
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

    override fun getItemStackSync(id: String): ItemStack? {
        return if (Bukkit.isPrimaryThread()) {
            itemManager.getItemStack(id)
        } else {
            bukkitScheduler.callSyncMethod(plugin) {
                itemManager.getItemStack(id)
            }.get()
        }
    }

    override fun castSkill(entity: Entity, skill: String, trigger: Entity?) {
        apiHelper.castSkill(entity, skill, trigger, entity.location, null, null, 1.0F)
    }

    override fun getItemIds(): List<String> {
        return itemManager.itemNames.toList()
    }
}