package pers.neige.neigeitems.ref.block.spawner;

import pers.neige.neigeitems.ref.block.RefBlockEntity;
import pers.neige.neigeitems.ref.block.RefBlockPos;
import pers.neige.neigeitems.ref.block.RefBlockState;
import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/world/level/block/entity/SpawnerBlockEntity", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/TileEntityMobSpawner", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public final class RefSpawnerBlockEntity extends RefBlockEntity {
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/TileEntityMobSpawner;<init>()V", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public RefSpawnerBlockEntity() {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/world/level/block/entity/SpawnerBlockEntity;<init>(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    public RefSpawnerBlockEntity(RefBlockPos pos, RefBlockState state) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/world/level/block/entity/SpawnerBlockEntity;getSpawner()Lnet/minecraft/world/level/BaseSpawner;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/TileEntityMobSpawner;getSpawner()Lnet/minecraft/server/v1_12_R1/MobSpawnerAbstract;", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native RefBaseSpawner getSpawner();
}
