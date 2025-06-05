package pers.neige.neigeitems.item;

import lombok.NonNull;
import lombok.var;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ItemColor {
    private static final @NonNull Map<String, ChatColor> colors = new HashMap<>();
    private static final @NonNull Set<Scoreboard> checkedScoreboard = new HashSet<>();

    static {
        colors.put("BLACK", ChatColor.BLACK);
        colors.put("DARK_BLUE", ChatColor.DARK_BLUE);
        colors.put("DARK_GREEN", ChatColor.DARK_GREEN);
        colors.put("DARK_AQUA", ChatColor.DARK_AQUA);
        colors.put("DARK_RED", ChatColor.DARK_RED);
        colors.put("DARK_PURPLE", ChatColor.DARK_PURPLE);
        colors.put("GOLD", ChatColor.GOLD);
        colors.put("GRAY", ChatColor.GRAY);
        colors.put("DARK_GRAY", ChatColor.DARK_GRAY);
        colors.put("BLUE", ChatColor.BLUE);
        colors.put("GREEN", ChatColor.GREEN);
        colors.put("AQUA", ChatColor.AQUA);
        colors.put("RED", ChatColor.RED);
        colors.put("LIGHT_PURPLE", ChatColor.LIGHT_PURPLE);
        colors.put("YELLOW", ChatColor.YELLOW);
        colors.put("WHITE", ChatColor.WHITE);
    }

    /**
     * 获取物品光效颜色对应Map
     */
    public static @NonNull Map<String, ChatColor> getColors() {
        return colors;
    }

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
    public static void initTeam(@NonNull Player player) {
        if (!checkedScoreboard.contains(player.getScoreboard())) {
            colors.forEach((id, color) -> {
                // 注册Team
                var team = player.getScoreboard().getTeam("NI-" + color);
                if (team != null) team.unregister();
                team = player.getScoreboard().registerNewTeam("NI-" + color);
                // 1.13+设置color即可改变光效发光颜色
                team.setColor(color);
                // 1.12-需要给prefix设置颜色才能改变发光颜色
                team.setPrefix(color.toString());
            });
            checkedScoreboard.add(player.getScoreboard());
        }
    }
}
