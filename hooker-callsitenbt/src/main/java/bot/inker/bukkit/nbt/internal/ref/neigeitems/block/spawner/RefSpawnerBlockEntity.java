package bot.inker.bukkit.nbt.internal.ref.neigeitems.block.spawner;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.block.RefBlockEntity;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.block.RefBlockPos;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.block.RefBlockState;

@HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/TileEntityMobSpawner")
@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/world/level/block/entity/SpawnerBlockEntity")
public final class RefSpawnerBlockEntity extends RefBlockEntity {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/TileEntityMobSpawner;<init>()V")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "")
    public RefSpawnerBlockEntity() {
        throw new UnsupportedOperationException();
    }

    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/level/block/entity/SpawnerBlockEntity;<init>(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V")
    public RefSpawnerBlockEntity(RefBlockPos pos, RefBlockState state) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/TileEntityMobSpawner;getSpawner()Lnet/minecraft/server/v1_12_R1/MobSpawnerAbstract;")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/level/block/entity/SpawnerBlockEntity;getSpawner()Lnet/minecraft/world/level/BaseSpawner;")
    public native RefBaseSpawner getSpawner();
}
