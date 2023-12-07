package pers.neige.neigeitems.internal.ref.entity;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "org/bukkit/craftbukkit/v1_12_R1/entity/CraftPlayer", predicates = "craftbukkit_version:[v1_12_R1,)")
public final class RefCraftPlayer extends RefCraftHumanEntity {
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_17_R1/entity/CraftPlayer;getHandle()Lnet/minecraft/server/level/ServerPlayer;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/entity/CraftPlayer;getHandle()Lnet/minecraft/server/v1_12_R1/EntityPlayer;", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native RefEntityPlayer getHandle();
}
