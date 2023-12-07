package pers.neige.neigeitems.ref.entity;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "org/bukkit/craftbukkit/v1_12_R1/entity/CraftEntity", predicates = "craftbukkit_version:[v1_12_R1,)")
public abstract class RefCraftEntity {
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_17_R1/entity/CraftEntity;getHandle()Lnet/minecraft/world/entity/Entity;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/entity/CraftEntity;getHandle()Lnet/minecraft/server/v1_12_R1/Entity;", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native RefEntity getHandle();
}
