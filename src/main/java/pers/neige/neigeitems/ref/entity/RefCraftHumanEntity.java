package pers.neige.neigeitems.ref.entity;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "org/bukkit/craftbukkit/v1_12_R1/entity/CraftHumanEntity", predicates = "craftbukkit_version:[v1_12_R1,)")
public class RefCraftHumanEntity extends RefCraftLivingEntity {
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_17_R1/entity/CraftHumanEntity;getHandle()Lnet/minecraft/world/entity/player/Player;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/entity/CraftHumanEntity;getHandle()Lnet/minecraft/server/v1_12_R1/EntityHuman;", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native RefEntityHuman getHandle();
}
