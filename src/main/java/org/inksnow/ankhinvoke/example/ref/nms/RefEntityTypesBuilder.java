package org.inksnow.ankhinvoke.example.ref.nms;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/world/entity/EntityType$Builder", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_13_R1/EntityTypes$a", predicates = "craftbukkit_version:[v1_13_R1,)")
public class RefEntityTypesBuilder<T extends RefEntity> {
//    @HandleBy(reference = "Lnet/minecraft/world/entity/EntityTypes$Builder;a(Lnet/minecraft/world/entity/EntityTypes$b;Lnet/minecraft/world/entity/EnumCreatureType;)Lnet/minecraft/world/entity/EntityTypes$Builder;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/world/entity/EntityType$Builder;of(Lnet/minecraft/world/entity/EntityType$EntityFactory;Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native static <T extends RefEntity> RefEntityTypesBuilder<T> of(RefEntityTypesFactory<T> factory, RefMobCategory spawnGroup);

    @HandleBy(reference = "Lnet/minecraft/world/entity/EntityType$Builder;createNothing(Lnet/minecraft/world/entity/MobCategory;)Lnet/minecraft/world/entity/EntityType$Builder;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native static <T extends RefEntity> RefEntityTypesBuilder<T> createNothing(RefMobCategory spawnGroup);

    @HandleBy(reference = "Lnet/minecraft/world/entity/EntityType$Builder;sized(FF)Lnet/minecraft/world/entity/EntityType$Builder;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native RefEntityTypesBuilder<T> sized(float width, float height);

    @HandleBy(reference = "Lnet/minecraft/world/entity/EntityType$Builder;noSummon()Lnet/minecraft/world/entity/EntityType$Builder;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native RefEntityTypesBuilder<T> noSummon();

    @HandleBy(reference = "Lnet/minecraft/world/entity/EntityType$Builder;noSave()Lnet/minecraft/world/entity/EntityType$Builder;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native RefEntityTypesBuilder<T> noSave();

    @HandleBy(reference = "Lnet/minecraft/world/entity/EntityType$Builder;fireImmune()Lnet/minecraft/world/entity/EntityType$Builder;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native RefEntityTypesBuilder<T> fireImmune();

    @HandleBy(reference = "Lnet/minecraft/world/entity/EntityType$Builder;immuneTo([Lnet/minecraft/world/level/block/Block;)Lnet/minecraft/world/entity/EntityType$Builder;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native RefEntityTypesBuilder<T> immuneTo(RefBlock... blocks);

    @HandleBy(reference = "Lnet/minecraft/world/entity/EntityType$Builder;canSpawnFarFromPlayer()Lnet/minecraft/world/entity/EntityType$Builder;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native RefEntityTypesBuilder<T> canSpawnFarFromPlayer();

    @HandleBy(reference = "Lnet/minecraft/world/entity/EntityType$Builder;clientTrackingRange(I)Lnet/minecraft/world/entity/EntityType$Builder;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native RefEntityTypesBuilder<T> clientTrackingRange(int maxTrackingRange);

    @HandleBy(reference = "Lnet/minecraft/world/entity/EntityType$Builder;updateInterval(I)Lnet/minecraft/world/entity/EntityType$Builder;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native RefEntityTypesBuilder<T> updateInterval(int trackingTickInterval);

    @HandleBy(reference = "Lnet/minecraft/world/entity/EntityType$Builder;requiredFeatures([Lnet/minecraft/world/flag/FeatureFlag;)Lnet/minecraft/world/entity/EntityType$Builder;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native RefEntityTypesBuilder<T> requiredFeatures(RefFeatureFlag... features);

    @HandleBy(reference = "Lnet/minecraft/world/entity/EntityType$Builder;build(Ljava/lang/String;)Lnet/minecraft/world/entity/EntityType;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native RefNewEntityTypes<T> build(String id);
}
