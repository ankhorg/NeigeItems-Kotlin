package bot.inker.bukkit.nbt.internal.ref.neigeitems.world;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.network.RefServerConnection;

@HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/MinecraftServer")
@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/server/MinecraftServer")
public final class RefMinecraftServer {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/MinecraftServer;recentTps:[D")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/server/MinecraftServer;recentTps:[D")
    public final double[] recentTps = null;

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/MinecraftServer;p:Lnet/minecraft/server/v1_12_R1/ServerConnection;", accessor = true)
    @HandleBy(version = CbVersion.v1_13_R1, reference = "Lnet/minecraft/server/v1_13_R1/MinecraftServer;serverConnection:Lnet/minecraft/server/v1_13_R1/ServerConnection;", accessor = true)
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/server/MinecraftServer;connection:Lnet/minecraft/server/network/ServerConnectionListener;", accessor = true)
    public RefServerConnection connection;
}
