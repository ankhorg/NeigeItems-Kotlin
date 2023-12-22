package pers.neige.neigeitems.ref.entity.animal;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.entity.RefEntity;
import pers.neige.neigeitems.ref.entity.RefEntityType;
import pers.neige.neigeitems.ref.world.RefWorld;

@HandleBy(reference = "net/minecraft/world/entity/animal/Sheep", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/EntitySheep", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public class RefEntitySheep extends RefEntity {
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntitySheep;<init>(Lnet/minecraft/server/v1_12_R1/World;)V", predicates = "craftbukkit_version:[v1_12_R1,v1_14_R1)")
  public RefEntitySheep(RefWorld world) {
    throw new UnsupportedOperationException();
  }

  @HandleBy(reference = "Lnet/minecraft/world/entity/animal/Sheep;<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_14_R1/EntitySheep;<init>(Lnet/minecraft/server/v1_14_R1/EntityTypes;Lnet/minecraft/server/v1_14_R1/World;)V", predicates = "craftbukkit_version:[v1_14_R1,v1_17_R1)")
  public RefEntitySheep(RefEntityType<? extends RefEntitySheep> type, RefWorld world) {
    throw new UnsupportedOperationException();
  }

  @HandleBy(reference = "Lnet/minecraft/world/entity/animal/Sheep;registerGoals()V", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntitySheep;r()V", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
  protected native void setupAi();

  @HandleBy(reference = "Lnet/minecraft/world/entity/animal/Sheep;customServerAiStep()V", predicates = "craftbukkit_version:[v1_17_R1,)")
  @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntitySheep;M()V", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
  protected native void updateAi();
}
