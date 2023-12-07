package org.inksnow.ankhinvoke.example.ref.nms;

import org.inksnow.ankhinvoke.comments.HandleBy;
import org.inksnow.ankhinvoke.example.ref.craftbukkit.RefCraftEntity;

@HandleBy(reference = "net/minecraft/world/entity/Entity", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/Entity", predicates = "craftbukkit_version:[v1_12_R1,)")
public class RefEntity {
  @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;setPos(DDD)V", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;setPosition(DDD)V", predicates = "craftbukkit_version:[v1_12_R1,)")
  public native void setPosition(double x, double y, double z);

  @HandleBy(reference = "Lnet/minecraft/world/entity/Entity;getBukkitEntity()Lorg/bukkit/craftbukkit/v1_17_R1/entity/CraftEntity;", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Entity;getBukkitEntity()Lorg/bukkit/craftbukkit/v1_12_R1/entity/CraftEntity;", predicates = "craftbukkit_version:[v1_12_R1,)")
  public native RefCraftEntity getBukkitEntity();
}
