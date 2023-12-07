package pers.neige.neigeitems.ref.entity;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "org/bukkit/craftbukkit/v1_12_R1/entity/CraftLivingEntity", predicates = "craftbukkit_version:[v1_12_R1,)")
public class RefCraftLivingEntity extends RefCraftEntity {
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_17_R1/entity/CraftLivingEntity;getHandle()Lnet/minecraft/world/entity/LivingEntity;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/entity/CraftLivingEntity;getHandle()Lnet/minecraft/server/v1_12_R1/EntityLiving;", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native RefEntityLiving getHandle();
}
