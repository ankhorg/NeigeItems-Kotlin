package pers.neige.neigeitems.ref.world;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.server.RefMinecraftServer;

@HandleBy(reference = "org/bukkit/craftbukkit/v1_12_R1/CraftServer", predicates = "craftbukkit_version:[v1_12_R1,)")
public final class RefCraftServer {
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_13_R1/CraftServer;syncCommands()V", predicates = "craftbukkit_version:[v1_13_R1,)")
    public native void syncCommands();

    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_17_R1/CraftServer;getServer()Lnet/minecraft/server/dedicated/DedicatedServer;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/CraftServer;getServer()Lnet/minecraft/server/v1_12_R1/MinecraftServer;", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native RefMinecraftServer getServer();
}
