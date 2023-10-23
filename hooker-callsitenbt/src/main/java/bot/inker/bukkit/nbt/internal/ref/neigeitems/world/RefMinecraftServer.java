package bot.inker.bukkit.nbt.internal.ref.neigeitems.world;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;

@HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/MinecraftServer")
@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/server/MinecraftServer")
public final class RefMinecraftServer {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/MinecraftServer;recentTps:[D")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/server/MinecraftServer;recentTps:[D")
    public final double[] recentTps = null;
}
