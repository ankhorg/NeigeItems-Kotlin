package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils;

import org.bukkit.Bukkit;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import pers.neige.neigeitems.ref.world.RefCraftServer;

public class ServerUtils {
    private static final boolean SYNC_COMMANDS_SUPPORT = CbVersion.v1_13_R1.isSupport();
    private static final boolean IS_PAPER = hasClass("com.destroystokyo.paper.PaperConfig") || hasClass("io.papermc.paper.configuration.Configuration");
    private static final boolean SUPPORT_ADVENTURE = hasClass("net.kyori.adventure.text.Component");

    private static boolean hasClass(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException | NoClassDefFoundError var2) {
            return false;
        }
    }

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

    /**
     * 获取当前服务器是否为 Paper.
     */
    public static boolean isPaper() {
        return IS_PAPER;
    }

    /**
     * 获取当前服务器是否存在 AdventureAPI.
     */
    public static boolean isSupportAdventure() {
        return SUPPORT_ADVENTURE;
    }
}
