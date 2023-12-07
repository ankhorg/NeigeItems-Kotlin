package pers.neige.neigeitems.ref.block.spawner;

import pers.neige.neigeitems.ref.block.RefCraftBlockEntityState;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "org/bukkit/craftbukkit/v1_12_R1/block/CraftCreatureSpawner", predicates = "craftbukkit_version:[v1_12_R1,)")
public final class RefCraftCreatureSpawner extends RefCraftBlockEntityState<RefSpawnerBlockEntity> {
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/block/CraftCreatureSpawner;<init>(Lorg/bukkit/Material;Lnet/minecraft/server/v1_12_R1/TileEntityMobSpawner;)V", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public RefCraftCreatureSpawner(Material material, RefSpawnerBlockEntity tileEntity) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_17_R1/block/CraftCreatureSpawner;<init>(Lorg/bukkit/World;Lnet/minecraft/world/level/block/entity/SpawnerBlockEntity;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    public RefCraftCreatureSpawner(World world, RefSpawnerBlockEntity tileEntity) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/block/CraftCreatureSpawner;getSpawnedType()Lorg/bukkit/entity/EntityType;", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native EntityType getSpawnedType();

    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/block/CraftCreatureSpawner;setSpawnedType(Lorg/bukkit/entity/EntityType;)V", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native void setSpawnedType(EntityType creatureType);

    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/block/CraftCreatureSpawner;getCreatureTypeName()Ljava/lang/String;", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native void setCreatureTypeByName(String creatureType);

    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/block/CraftCreatureSpawner;setCreatureTypeByName(Ljava/lang/String;)V", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native String getCreatureTypeName();

    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/block/CraftCreatureSpawner;getDelay()I", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native int getDelay();

    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/block/CraftCreatureSpawner;setDelay(I)V", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native void setDelay(int delay);

    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/block/CraftCreatureSpawner;getMinSpawnDelay()I", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native int getMinSpawnDelay();

    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/block/CraftCreatureSpawner;setMinSpawnDelay(I)V", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native void setMinSpawnDelay(int delay);

    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/block/CraftCreatureSpawner;getMaxSpawnDelay()I", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native int getMaxSpawnDelay();

    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/block/CraftCreatureSpawner;setMaxSpawnDelay(I)V", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native void setMaxSpawnDelay(int delay);

    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/block/CraftCreatureSpawner;getSpawnCount()I", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native int getSpawnCount();

    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/block/CraftCreatureSpawner;setSpawnCount(I)V", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native void setSpawnCount(int spawnCount);

    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/block/CraftCreatureSpawner;getMaxNearbyEntities()I", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native int getMaxNearbyEntities();

    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/block/CraftCreatureSpawner;setMaxNearbyEntities(I)V", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native void setMaxNearbyEntities(int maxNearbyEntities);

    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/block/CraftCreatureSpawner;getRequiredPlayerRange()I", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native int getRequiredPlayerRange();

    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/block/CraftCreatureSpawner;setRequiredPlayerRange(I)V", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native void setRequiredPlayerRange(int requiredPlayerRange);

    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/block/CraftCreatureSpawner;getSpawnRange()I", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native int getSpawnRange();

    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/block/CraftCreatureSpawner;setSpawnRange(I)V", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native void setSpawnRange(int spawnRange);
}
