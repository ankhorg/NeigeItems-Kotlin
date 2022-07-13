package pers.neige.neigeitems.item

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.event.entity.ItemSpawnEvent
import pers.neige.neigeitems.manager.TeamManager.teams
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submit
import taboolib.module.nms.getItemTag

object ItemColor {
    @SubscribeEvent
    fun listener(event: ItemSpawnEvent) {
        submit(async = true) {
            val item = event.entity
            val itemStack = item.itemStack
            if (itemStack.type != Material.AIR) {
                val itemTag = itemStack.getItemTag()

                itemTag["NeigeItems"]?.asCompound()?.get("color")?.asString()?.let {
                    try {
                        val color = ChatColor.valueOf(it)
                        teams[color.toString()]?.addEntry(item.uniqueId.toString())
                        item.isGlowing = true
                    } catch (error: Throwable) {}
                }
            }
        }
    }
}