package pers.neige.neigeitems.ref.entity;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.entity.animal.RefEntitySheep;

@HandleBy(reference = "net/minecraft/world/entity/EntityType", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_13_R1/EntityTypes", predicates = "craftbukkit_version:[v1_13_R1,)")
public class RefNewEntityTypes<T extends RefEntity> {
  @HandleBy(reference = "Lnet/minecraft/world/entity/EntityType;register(Ljava/lang/String;Lnet/minecraft/world/entity/EntityType$Builder;)Lnet/minecraft/world/entity/EntityType;", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/EntityTypes;a(Ljava/lang/String;Lnet/minecraft/server/v1_13_R1/EntityTypes$a;)Lnet/minecraft/server/v1_13_R1/EntityTypes;", useAccessor = true, predicates = "craftbukkit_version:[v1_13_R1,)")
  public static native <T extends RefEntity> RefNewEntityTypes<T> registerEntity(String id, RefEntityTypesBuilder<T> type);

  @HandleBy(reference = "Lnet/minecraft/world/entity/EntityType;SHEEP:Lnet/minecraft/world/entity/EntityType;", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
  public static final RefNewEntityTypes<RefEntitySheep> SHEEP = null;
}
