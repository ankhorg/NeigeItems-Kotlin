package bot.inker.bukkit.nbt.internal.ref.neigeitems.block.spawner;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.util.RefSimpleWeightedRandomList;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.util.RefWeightedRandomList;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.util.RefWrapper;
import com.google.common.collect.Lists;
import net.minecraft.server.v1_12_R1.MobSpawnerData;

import javax.annotation.Nullable;
import java.util.List;

@HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/MobSpawnerAbstract")
@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/world/level/BaseSpawner")
public abstract class RefBaseSpawner {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/MobSpawnerAbstract;mobs:Ljava/util/List;", accessor = true)
    @HandleBy(version = CbVersion.v1_17_R1, reference = "")
    public final List<RefSpawnData> mobs = null;

    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/level/BaseSpawner;spawnPotentials:Lnet/minecraft/util/random/SimpleWeightedRandomList;")
    public RefWeightedRandomList<RefWrapper<RefSpawnData>> spawnPotentials = null;

    @Nullable
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/MobSpawnerAbstract;spawnData:Lnet/minecraft/server/v1_12_R1/MobSpawnerData;", accessor = true)
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/level/BaseSpawner;nextSpawnData:Lnet/minecraft/world/level/SpawnData;")
    public RefSpawnData spawnData;
}
