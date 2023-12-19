package pers.neige.neigeitems.ref.entity;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "org/bukkit/craftbukkit/v1_12_R1/entity/CraftItem", predicates = "craftbukkit_version:[v1_12_R1,)")
public final class RefCraftItem extends RefCraftEntity {
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_17_R1/entity/CraftItem;item:Lnet/minecraft/world/entity/item/ItemEntity;", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,v1_20_R2)")
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/entity/CraftItem;item:Lnet/minecraft/server/v1_12_R1/EntityItem;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public RefEntityItem item;

    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_20_R2/entity/CraftItem;getHandle()Lnet/minecraft/world/entity/item/ItemEntity;", predicates = "craftbukkit_version:[v1_20_R2,)")
    public native RefEntityItem getHandle();
}
