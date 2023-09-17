package pers.neige.neigeitems.item.color.impl

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.event.entity.ItemSpawnEvent
import org.bukkit.scoreboard.Team
import pers.neige.neigeitems.NeigeItems.bukkitScheduler
import pers.neige.neigeitems.NeigeItems.plugin
import pers.neige.neigeitems.item.color.ItemColor
import pers.neige.neigeitems.utils.ItemUtils.isNiItem
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.function.registerBukkitListener
import taboolib.common.platform.function.submit
import taboolib.module.nms.ItemTag
import taboolib.module.nms.getItemTag

/**
 * 基于Bukkit功能实现的掉落物光效系统
 *
 * @constructor 启用基于Bukkit功能实现的掉落物光效系统
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
            var team = Bukkit.getServer().scoreboardManager?.mainScoreboard?.getTeam("NIVanilla-$color")
            team?.unregister()
            team = Bukkit.getServer().scoreboardManager?.mainScoreboard?.registerNewTeam("NIVanilla-$color")
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
            val item = it.entity
            val itemStack = item.itemStack
            val itemInfo = itemStack.isNiItem() ?: return@registerBukkitListener
            val colorString = itemInfo.neigeItems.getString("color") ?: return@registerBukkitListener
            val color = colors[colorString] ?: return@registerBukkitListener
            // 挪入相关颜色的Team
            teams[color.toString()]?.addEntry(item.uniqueId.toString())
            // 设置物品发光
            item.isGlowing = true
        }
    }
}