package pers.neige.neigeitems.item

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.event.entity.ItemSpawnEvent
import pers.neige.neigeitems.manager.TeamManager.teams
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submit
import taboolib.module.nms.getItemTag

// 用于实现掉落物光效功能
object ItemColor {
    @SubscribeEvent
    fun listener(event: ItemSpawnEvent) {
        submit(async = true) {
            val item = event.entity
            val itemStack = item.itemStack
            // 1.12.2情况下可能为null
            if (itemStack != null && itemStack.type != Material.AIR) {
                val itemTag = itemStack.getItemTag()

                // 检测物品是否有用于标记光效颜色的特殊NBT
                itemTag["NeigeItems"]?.asCompound()?.get("color")?.asString()?.let {
                    // 颜色违规就会报错(一般情况不会违规, 物品生成时已进行过相关检查), 所以catch一下
                    try {
                        val color = ChatColor.valueOf(it)
                        // 挪入相关颜色的Team
                        teams[color.toString()]?.addEntry(item.uniqueId.toString())
                        // 设置物品发光
                        item.isGlowing = true
                    } catch (error: Throwable) {}
                }
            }
        }
    }
}