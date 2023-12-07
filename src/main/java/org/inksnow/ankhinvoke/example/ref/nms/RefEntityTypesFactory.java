package org.inksnow.ankhinvoke.example.ref.nms;

import com.google.common.collect.ImmutableSet;
import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/world/entity/EntityType$EntityFactory", predicates = "craftbukkit_version:[v1_17_R1,)", isInterface = true)
public interface RefEntityTypesFactory<T extends RefEntity> {
    @HandleBy(reference = "Lnet/minecraft/world/entity/EntityType$EntityFactory;create(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)Lnet/minecraft/world/entity/Entity;", predicates = "craftbukkit_version:[v1_17_R1,)", isInterface = true)
    T create(RefNewEntityTypes<T> var1, RefWorld var2);
}
