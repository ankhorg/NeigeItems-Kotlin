package pers.neige.neigeitems.hook.mythicmobs.impl

import io.lumine.mythic.api.mobs.MobManager
import io.lumine.mythic.bukkit.MythicBukkit
import io.lumine.mythic.bukkit.adapters.BukkitEntity
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent
import io.lumine.mythic.bukkit.events.MythicMobSpawnEvent
import io.lumine.mythic.bukkit.events.MythicReloadedEvent
import io.lumine.mythic.core.items.ItemExecutor
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.hook.mythicmobs.MythicMobsHooker
import pers.neige.neigeitems.utils.ListenerUtils
import pers.neige.neigeitems.utils.SchedulerUtils.async
import pers.neige.neigeitems.utils.SchedulerUtils.syncAndGet

/**
 * 5.0.2版本MM挂钩
 *
 * @constructor 启用5.0.2版本MM挂钩
 */
class MythicMobsHookerImpl502 : MythicMobsHooker() {
    override val version = "502"

    override val spawnEventClass = MythicMobSpawnEvent::class.java

    override val deathEventClass = MythicMobDeathEvent::class.java

    override val reloadEventClass = MythicReloadedEvent::class.java

    private val itemManager: ItemExecutor = MythicBukkit.inst().itemManager

    private val mobManager: MobManager = MythicBukkit.inst().mobManager

    private val apiHelper = MythicBukkit.inst().apiHelper

    override val spawnListener = ListenerUtils.registerListener(
        MythicMobSpawnEvent::class.java,
        EventPriority.HIGH
    ) { event ->
        async {
            if (event.entity is LivingEntity) {
                spawnEvent(
                    event.mobType.internalName,
                    event.entity as LivingEntity,
                    event.mobLevel.toInt()
                )
            }
        }
    }

    override val deathListener = ListenerUtils.registerListener(
        MythicMobDeathEvent::class.java,
        EventPriority.HIGH
    ) { event ->
        async {
            if (event.entity is LivingEntity) {
                deathEvent(
                    event.killer,
                    event.entity as LivingEntity,
                    event.mobType.internalName,
                    event.mobLevel.toInt()
                )
            }
        }
    }

    override val reloadListener = ListenerUtils.registerListener(
        MythicReloadedEvent::class.java
    ) {
        loadMobInfos()
    }

    override fun hasItem(id: String): Boolean {
        return itemManager.itemNames.contains(id)
    }

    override fun getItemStack(id: String): ItemStack? {
        return itemManager.getItemStack(id)
    }

    override fun getItemStackSync(id: String): ItemStack? {
        return syncAndGet {
            itemManager.getItemStack(id)
        }
    }

    override fun castSkill(entity: Entity, skill: String, trigger: Entity?) {
        apiHelper.castSkill(entity, skill, trigger, entity.location, null, null, 1.0F)
    }

    override fun getItemIds(): List<String> {
        return itemManager.itemNames.toList()
    }

    override fun isMythicMob(entity: Entity): Boolean {
        return apiHelper.isMythicMob(entity)
    }

    override fun getMythicId(entity: Entity): String? {
        return if (apiHelper.isMythicMob(entity))
            apiHelper.getMythicMobInstance(entity).type.internalName
        else
            null
    }

    override fun getEntity(event: Event): Entity? {
        return when (event) {
            is MythicMobSpawnEvent -> event.entity
            is MythicMobDeathEvent -> event.entity
            else -> null
        }
    }

    override fun getKiller(event: Event): LivingEntity? {
        return when (event) {
            is MythicMobDeathEvent -> event.killer
            else -> null
        }
    }

    override fun getInternalName(event: Event): String? {
        return when (event) {
            is MythicMobSpawnEvent -> event.mobType.internalName
            is MythicMobDeathEvent -> event.mobType.internalName
            else -> null
        }
    }

    override fun getMobLevel(event: Event): Double? {
        return when (event) {
            is MythicMobSpawnEvent -> event.mobLevel
            is MythicMobDeathEvent -> event.mobLevel
            else -> null
        }
    }

    override fun adapt(entity: Entity): Any {
        return BukkitEntity(entity)
    }
}