package pers.neige.neigeitems.ref.world;

import pers.neige.neigeitems.ref.network.RefServerConnection;
import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/server/MinecraftServer", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/MinecraftServer", predicates = "craftbukkit_version:[v1_12_R1,)")
public final class RefMinecraftServer {
    @HandleBy(reference = "Lnet/minecraft/server/MinecraftServer;recentTps:[D", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/MinecraftServer;recentTps:[D", predicates = "craftbukkit_version:[v1_12_R1,)")
    public final double[] recentTps = null;

    @HandleBy(reference = "Lnet/minecraft/server/MinecraftServer;connection:Lnet/minecraft/server/network/ServerConnectionListener;", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/MinecraftServer;serverConnection:Lnet/minecraft/server/v1_13_R1/ServerConnection;", useAccessor = true, predicates = "craftbukkit_version:[v1_13_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/MinecraftServer;p:Lnet/minecraft/server/v1_12_R1/ServerConnection;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,)")
    public RefServerConnection connection;
}
