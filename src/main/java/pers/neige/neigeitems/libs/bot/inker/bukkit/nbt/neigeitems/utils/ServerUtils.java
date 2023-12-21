package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils;

import org.bukkit.Bukkit;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import pers.neige.neigeitems.ref.world.RefCraftServer;

public class ServerUtils {
    private static final boolean SYNC_COMMANDS_SUPPORT = CbVersion.v1_13_R1.isSupport();

    /**
     * 与客户端玩家同步指令列表.
     */
    public static void syncCommands() {
        if (SYNC_COMMANDS_SUPPORT) {
            ((RefCraftServer) (Object) Bukkit.getServer()).syncCommands();
        }
    }

    /**
     * 获取服务器 TPS.
     */
    public static double[] getTps() {
        return ((RefCraftServer) (Object) Bukkit.getServer()).getServer().recentTps;
    }
}
