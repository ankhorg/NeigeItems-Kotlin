package pers.neige.neigeitems.ref.world;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "org/bukkit/craftbukkit/v1_12_R1/CraftWorld", predicates = "craftbukkit_version:[v1_12_R1,)")
public final class RefCraftWorld {
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_17_R1/CraftWorld;getHandle()Lnet/minecraft/server/level/ServerLevel;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/CraftWorld;getHandle()Lnet/minecraft/server/v1_12_R1/WorldServer;", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native RefWorldServer getHandle();
}
