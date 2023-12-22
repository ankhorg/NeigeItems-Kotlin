package pers.neige.neigeitems.ref.nbt;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/world/level/ItemLike", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_13_R1/IMaterial", predicates = "craftbukkit_version:[v1_13_R1,v1_17_R1)")
public final class RefIMaterial {
  @HandleBy(reference = "Lnet/minecraft/world/level/ItemLike;asItem()Lnet/minecraft/world/item/Item;", isInterface = true, predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/IMaterial;getItem()Lnet/minecraft/server/v1_13_R1/Item;", isInterface = true, predicates = "craftbukkit_version:[v1_13_R1,v1_17_R1)")
  public native RefItem getItem();
}
