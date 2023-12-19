package pers.neige.neigeitems.ref.block.spawner;

import pers.neige.neigeitems.ref.nbt.RefNbtTagCompound;
import org.inksnow.ankhinvoke.comments.HandleBy;

import java.util.Optional;

@HandleBy(reference = "net/minecraft/world/level/SpawnData", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/MobSpawnerData", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public class RefSpawnData {
    @HandleBy(reference = "Lnet/minecraft/world/level/SpawnData;<init>()V", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/MobSpawnerData;<init>()V", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public RefSpawnData() {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/MobSpawnerData;<init>(Lnet/minecraft/server/v1_12_R1/NBTTagCompound;)V", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public RefSpawnData(RefNbtTagCompound entityToSpawn) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/MobSpawnerData;<init>(ILnet/minecraft/server/v1_12_R1/NBTTagCompound;)V", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public RefSpawnData(int weight, RefNbtTagCompound entityToSpawn) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/world/level/SpawnData;<init>(Lnet/minecraft/nbt/CompoundTag;Ljava/util/Optional;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    public RefSpawnData(RefNbtTagCompound entityToSpawn, Optional<RefCustomSpawnRules> customSpawnRules) {
        throw new UnsupportedOperationException();
    }
}
