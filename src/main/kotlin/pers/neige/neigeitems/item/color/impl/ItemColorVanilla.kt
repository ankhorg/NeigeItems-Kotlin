package pers.neige.neigeitems.item.color.impl

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.event.entity.ItemSpawnEvent
import org.bukkit.scoreboard.Team
import pers.neige.neigeitems.NeigeItems.bukkitScheduler
import pers.neige.neigeitems.NeigeItems.plugin
import pers.neige.neigeitems.item.color.ItemColor
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.function.registerBukkitListener
import taboolib.common.platform.function.submit
import taboolib.module.nms.nmsGeneric

/**
 * 用于实现掉落物光效功能
 */
class ItemColorVanilla : ItemColor() {
    override val mode = "Vanilla"

    private val teams = HashMap<String, Team>()

    /**
     * 在主计分板初始化Team
     */
    private fun loadTeams() {
        for ((id, color) in colors) {
            // 注册Team
            var team = Bukkit.getServer().scoreboardManager?.mainScoreboard?.getTeam("NI-$color")
            team?.unregister()
            team = Bukkit.getServer().scoreboardManager?.mainScoreboard?.registerNewTeam("NI-$color")
            // 1.13+设置color即可改变光效发光颜色
            team?.color = color
            // 1.12-需要给prefix设置颜色才能改变发光颜色
            team?.prefix = color.toString()
            // 存入缓存
            team?.let {teams[color.toString()] = it}
        }
    }

    init {
        loadTeams()

        registerBukkitListener(ItemSpawnEvent::class.java, EventPriority.NORMAL, false) {
            submit(async = true) {
                val item = it.entity
                val itemStack = item.itemStack
                if (itemStack.type != Material.AIR) {
                    // 由于是异步操作, 物品仍可能为空气(还没获取NBT就被玩家捡走了), 所以直接使用nmsGeneric.getItemTag
                    val itemTag = nmsGeneric.getItemTag(itemStack)

                    // 检测物品是否有用于标记光效颜色的特殊NBT
                    itemTag["NeigeItems"]?.asCompound()?.get("color")?.asString()?.let {
                        // 颜色违规就会报错(一般情况不会违规, 物品生成时已进行过相关检查), 所以catch一下
                        try {
                            val color = ChatColor.valueOf(it)
                            // 有关Team存储用的是HashMap, 这玩意儿异步不得
                            bukkitScheduler.callSyncMethod(plugin) {
                                // 挪入相关颜色的Team
                                teams[color.toString()]?.addEntry(item.uniqueId.toString())
                                // 设置物品发光
                                item.isGlowing = true
                            }
                        } catch (error: IllegalArgumentException) {}
                    }
                }
            }
        }
    }
}