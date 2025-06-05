package pers.neige.neigeitems.ref.block.spawner;

import org.inksnow.ankhinvoke.comments.HandleBy;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.ref.util.RefWeightedRandomList;
import pers.neige.neigeitems.ref.util.RefWrapper;

import java.util.List;

@HandleBy(reference = "net/minecraft/world/level/BaseSpawner", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/MobSpawnerAbstract", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public abstract class RefBaseSpawner {
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/MobSpawnerAbstract;mobs:Ljava/util/List;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public final List<RefSpawnData> mobs = null;

    @HandleBy(reference = "Lnet/minecraft/world/level/BaseSpawner;spawnPotentials:Lnet/minecraft/util/random/SimpleWeightedRandomList;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public RefWeightedRandomList<RefWrapper<RefSpawnData>> spawnPotentials = null;

    @HandleBy(reference = "Lnet/minecraft/world/level/BaseSpawner;nextSpawnData:Lnet/minecraft/world/level/SpawnData;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/MobSpawnerAbstract;spawnData:Lnet/minecraft/server/v1_12_R1/MobSpawnerData;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public @Nullable RefSpawnData spawnData;
}
