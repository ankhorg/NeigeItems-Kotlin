package pers.neige.neigeitems.manager

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.scoreboard.Team
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake

object TeamManager {
    val teams = HashMap<String, Team>()

    @Awake(LifeCycle.ACTIVE)
    fun loadTeams() {
        val colors = ChatColor.values()

        for (color in colors) {
            var team = Bukkit.getServer().scoreboardManager?.mainScoreboard?.getTeam("NI-$color")
            team?.unregister()
            team = Bukkit.getServer().scoreboardManager?.mainScoreboard?.registerNewTeam("NI-$color")
            team?.color = color
            team?.prefix = color.toString()
            team?.let {teams[color.toString()] = it}
        }
    }
}