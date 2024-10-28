package pers.neige.neigeitems.item

import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Scoreboard

/**
 * 掉落物光效系统
 */
object ItemColor {
    /**
     * 获取物品光效颜色对应Map
     */
    @JvmStatic
    val colors = HashMap<String, ChatColor>().also {
        it["BLACK"] = ChatColor.BLACK
        it["DARK_BLUE"] = ChatColor.DARK_BLUE
        it["DARK_GREEN"] = ChatColor.DARK_GREEN
        it["DARK_AQUA"] = ChatColor.DARK_AQUA
        it["DARK_RED"] = ChatColor.DARK_RED
        it["DARK_PURPLE"] = ChatColor.DARK_PURPLE
        it["GOLD"] = ChatColor.GOLD
        it["GRAY"] = ChatColor.GRAY
        it["DARK_GRAY"] = ChatColor.DARK_GRAY
        it["BLUE"] = ChatColor.BLUE
        it["GREEN"] = ChatColor.GREEN
        it["AQUA"] = ChatColor.AQUA
        it["RED"] = ChatColor.RED
        it["LIGHT_PURPLE"] = ChatColor.LIGHT_PURPLE
        it["YELLOW"] = ChatColor.YELLOW
        it["WHITE"] = ChatColor.WHITE
    }

    private val checkedScoreboard = HashSet<Scoreboard>()

    /**
     * 根据玩家当前计分板进行Team初始化.
     * 玩家的计分板不一定一成不变,
     * 可能刚进服时玩家是主计分板,
     * 过一段时间其他插件又根据需要切换了玩家的计分板.
     * 需要保证每个计分板内都存在对应的Team,
     * 不然玩家的客户端将认为掉落物所在的Team与玩家无关,
     * 从而导致掉落物只发出白色光效.
     *
     * @param player 待操作玩家
     */
    @JvmStatic
    fun initTeam(player: Player) {
        if (!checkedScoreboard.contains(player.scoreboard)) {
            for ((id, color) in colors) {
                // 注册Team
                var team = player.scoreboard.getTeam("NI-$color")
                team?.unregister()
                team = player.scoreboard.registerNewTeam("NI-$color")
                // 1.13+设置color即可改变光效发光颜色
                team.color = color
                // 1.12-需要给prefix设置颜色才能改变发光颜色
                team.prefix = color.toString()
            }
            checkedScoreboard.add(player.scoreboard)
        }
    }
}