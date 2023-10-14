package bot.inker.bukkit.nbt.neigeitems.utils;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.world.RefCraftServer;
import org.bukkit.Bukkit;

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
}
