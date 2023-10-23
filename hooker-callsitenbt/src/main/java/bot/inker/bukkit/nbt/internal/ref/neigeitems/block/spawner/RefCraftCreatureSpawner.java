package bot.inker.bukkit.nbt.internal.ref.neigeitems.block.spawner;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.block.RefCraftBlockEntityState;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

@HandleBy(version = CbVersion.v1_12_R1, reference = "org/bukkit/craftbukkit/v1_12_R1/block/CraftCreatureSpawner")
public final class RefCraftCreatureSpawner extends RefCraftBlockEntityState<RefSpawnerBlockEntity> {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lorg/bukkit/craftbukkit/v1_12_R1/block/CraftCreatureSpawner;<init>(Lorg/bukkit/Material;Lnet/minecraft/server/v1_12_R1/TileEntityMobSpawner;)V")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "")
    public RefCraftCreatureSpawner(Material material, RefSpawnerBlockEntity tileEntity) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lorg/bukkit/craftbukkit/v1_17_R1/block/CraftCreatureSpawner;<init>(Lorg/bukkit/World;Lnet/minecraft/world/level/block/entity/SpawnerBlockEntity;)V")
    public RefCraftCreatureSpawner(World world, RefSpawnerBlockEntity tileEntity) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lorg/bukkit/craftbukkit/v1_12_R1/block/CraftCreatureSpawner;getSpawnedType()Lorg/bukkit/entity/EntityType;")
    public native EntityType getSpawnedType();

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lorg/bukkit/craftbukkit/v1_12_R1/block/CraftCreatureSpawner;setSpawnedType(Lorg/bukkit/entity/EntityType;)V")
    public native void setSpawnedType(@NotNull EntityType creatureType);

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lorg/bukkit/craftbukkit/v1_12_R1/block/CraftCreatureSpawner;getCreatureTypeName()Ljava/lang/String;")
    public native void setCreatureTypeByName(@NotNull String creatureType);

    @NotNull
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lorg/bukkit/craftbukkit/v1_12_R1/block/CraftCreatureSpawner;setCreatureTypeByName(Ljava/lang/String;)V")
    public native String getCreatureTypeName();

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lorg/bukkit/craftbukkit/v1_12_R1/block/CraftCreatureSpawner;getDelay()I")
    public native int getDelay();

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lorg/bukkit/craftbukkit/v1_12_R1/block/CraftCreatureSpawner;setDelay(I)V")
    public native void setDelay(int delay);

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lorg/bukkit/craftbukkit/v1_12_R1/block/CraftCreatureSpawner;getMinSpawnDelay()I")
    public native int getMinSpawnDelay();

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lorg/bukkit/craftbukkit/v1_12_R1/block/CraftCreatureSpawner;setMinSpawnDelay(I)V")
    public native void setMinSpawnDelay(int delay);

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lorg/bukkit/craftbukkit/v1_12_R1/block/CraftCreatureSpawner;getMaxSpawnDelay()I")
    public native int getMaxSpawnDelay();

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lorg/bukkit/craftbukkit/v1_12_R1/block/CraftCreatureSpawner;setMaxSpawnDelay(I)V")
    public native void setMaxSpawnDelay(int delay);

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lorg/bukkit/craftbukkit/v1_12_R1/block/CraftCreatureSpawner;getSpawnCount()I")
    public native int getSpawnCount();

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lorg/bukkit/craftbukkit/v1_12_R1/block/CraftCreatureSpawner;setSpawnCount(I)V")
    public native void setSpawnCount(int spawnCount);

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lorg/bukkit/craftbukkit/v1_12_R1/block/CraftCreatureSpawner;getMaxNearbyEntities()I")
    public native int getMaxNearbyEntities();

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lorg/bukkit/craftbukkit/v1_12_R1/block/CraftCreatureSpawner;setMaxNearbyEntities(I)V")
    public native void setMaxNearbyEntities(int maxNearbyEntities);

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lorg/bukkit/craftbukkit/v1_12_R1/block/CraftCreatureSpawner;getRequiredPlayerRange()I")
    public native int getRequiredPlayerRange();

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lorg/bukkit/craftbukkit/v1_12_R1/block/CraftCreatureSpawner;setRequiredPlayerRange(I)V")
    public native void setRequiredPlayerRange(int requiredPlayerRange);

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lorg/bukkit/craftbukkit/v1_12_R1/block/CraftCreatureSpawner;getSpawnRange()I")
    public native int getSpawnRange();

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lorg/bukkit/craftbukkit/v1_12_R1/block/CraftCreatureSpawner;setSpawnRange(I)V")
    public native void setSpawnRange(int spawnRange);
}
