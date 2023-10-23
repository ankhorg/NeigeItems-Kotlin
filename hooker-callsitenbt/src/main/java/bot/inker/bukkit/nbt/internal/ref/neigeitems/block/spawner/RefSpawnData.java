package bot.inker.bukkit.nbt.internal.ref.neigeitems.block.spawner;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;
import bot.inker.bukkit.nbt.internal.ref.RefNbtTagCompound;

import java.util.Optional;

@HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/MobSpawnerData")
@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/world/level/SpawnData")
public class RefSpawnData {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/MobSpawnerData;<init>()V")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/level/SpawnData;<init>()V")
    public RefSpawnData() {
        throw new UnsupportedOperationException();
    }

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/MobSpawnerData;<init>(Lnet/minecraft/server/v1_12_R1/NBTTagCompound;)V")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "")
    public RefSpawnData(RefNbtTagCompound entityToSpawn) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/MobSpawnerData;<init>(ILnet/minecraft/server/v1_12_R1/NBTTagCompound;)V")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "")
    public RefSpawnData(int weight, RefNbtTagCompound entityToSpawn) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/level/SpawnData;<init>(Lnet/minecraft/nbt/CompoundTag;Ljava/util/Optional;)V")
    public RefSpawnData(RefNbtTagCompound entityToSpawn, Optional<RefCustomSpawnRules> customSpawnRules) {
        throw new UnsupportedOperationException();
    }
}
