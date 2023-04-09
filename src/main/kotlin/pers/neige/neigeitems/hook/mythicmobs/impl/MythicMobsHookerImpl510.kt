package pers.neige.neigeitems.hook.mythicmobs.impl

import io.lumine.mythic.api.mobs.MobManager
import io.lumine.mythic.bukkit.MythicBukkit
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent
import io.lumine.mythic.bukkit.events.MythicMobSpawnEvent
import io.lumine.mythic.bukkit.events.MythicReloadedEvent
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
import pers.neige.neigeitems.event.MobInfoReloadedEvent
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
    override val version = "510"

    override val spawnEventClass = MythicMobSpawnEvent::class.java

    override val deathEventClass = MythicMobDeathEvent::class.java

    override val reloadEventClass = MythicReloadedEvent::class.java

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