package pers.neige.neigeitems.hook.mythicmobs.impl

import io.lumine.xikage.mythicmobs.MythicMobs
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobSpawnEvent
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicReloadedEvent
import io.lumine.xikage.mythicmobs.items.ItemManager
import io.lumine.xikage.mythicmobs.mobs.MobManager
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.event.Event
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.hook.mythicmobs.MythicMobsHooker
import pers.neige.neigeitems.utils.ListenerUtils
import pers.neige.neigeitems.utils.SchedulerUtils.async

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

    private val apiHelper = MythicMobs.inst().apiHelper

    override val spawnListener = ListenerUtils.registerListener(
        MythicMobSpawnEvent::class.java,
        org.bukkit.event.EventPriority.HIGH
    ) { event ->
        async {
            if (event.entity is LivingEntity) {
                spawnEvent(
                    event.mobType.internalName,
                    event.entity as LivingEntity,
                    event.mobLevel
                )
            }
        }
    }

    override val deathListener = ListenerUtils.registerListener(
        MythicMobDeathEvent::class.java
    ) { event ->
        async {
            if (event.entity is LivingEntity) {
                deathEvent(
                    event.killer,
                    event.entity as LivingEntity,
                    event.mobType.internalName,
                    event.mobLevel
                )
            }
        }
    }

    override val reloadListener = ListenerUtils.registerListener(
        MythicReloadedEvent::class.java
    ) {
        loadMobInfos()
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
            is MythicMobSpawnEvent -> event.mobLevel.toDouble()
            is MythicMobDeathEvent -> event.mobLevel.toDouble()
            else -> null
        }
    }
}