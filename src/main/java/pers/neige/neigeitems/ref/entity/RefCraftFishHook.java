package pers.neige.neigeitems.ref.entity;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "org/bukkit/craftbukkit/v1_17_R1/entity/CraftFishHook", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "org/bukkit/craftbukkit/v1_12_R1/entity/CraftFish", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
public final class RefCraftFishHook extends RefCraftEntity {
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/entity/CraftFishHook;getHandle()Lnet/minecraft/world/entity/projectile/FishingHook;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/entity/CraftFish;getHandle()Lnet/minecraft/server/v1_12_R1/EntityFishingHook;", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public native RefEntityFishingHook getHandle();
}
