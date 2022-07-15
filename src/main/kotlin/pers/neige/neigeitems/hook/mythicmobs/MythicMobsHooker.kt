package pers.neige.neigeitems.hook.mythicmobs

import org.bukkit.entity.Entity
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.event.ProxyListener

abstract class MythicMobsHooker {
    abstract val spawnListener: ProxyListener

    abstract val deathListener: ProxyListener

    abstract fun getItemStack(id: String): ItemStack?

    abstract fun getItemStackSync(id: String): ItemStack?

    abstract fun castSkill(entity: Entity, skill: String)

    abstract fun getItemIds(): List<String>
}