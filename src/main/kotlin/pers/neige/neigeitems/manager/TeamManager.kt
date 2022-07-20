package pers.neige.neigeitems.manager

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.scoreboard.Team
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

// Team管理器, 用于实现掉落物光效功能
object TeamManager {
    // 所有Team
    val teams = HashMap<String, Team>()

    // 在开服后加载(因为插件加载的时候主计分板可能还没生成, 所以延迟到开服后)
    @Awake(LifeCycle.ACTIVE)
    fun loadTeams() {
        // 遍历所有颜色(按理讲, 可以排除加粗、乱码、下划线等, 但是没必要, 填那些颜色只会产生白光, 不会报错)
        val colors = ChatColor.values()
        for (color in colors) {
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
}