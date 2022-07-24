package pers.neige.neigeitems.item

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.event.entity.ItemSpawnEvent
import pers.neige.neigeitems.manager.TeamManager.teams
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submit
import taboolib.module.nms.getItemTag
import taboolib.module.nms.nmsGeneric

// 用于实现掉落物光效功能
object ItemColor {
    @SubscribeEvent
    fun listener(event: ItemSpawnEvent) {
        submit(async = true) {
            val item = event.entity
            val itemStack = item.itemStack
            if (itemStack.type != Material.AIR) {
                // 由于是异步操作, 物品仍可能为空气(还没获取NBT就被玩家捡走了), 所以直接使用nmsGeneric.getItemTag
                val itemTag = nmsGeneric.getItemTag(itemStack)

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