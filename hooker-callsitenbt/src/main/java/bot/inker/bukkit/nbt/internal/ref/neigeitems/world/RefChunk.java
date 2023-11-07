package bot.inker.bukkit.nbt.internal.ref.neigeitems.world;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.entity.RefEntity;
import org.bukkit.Chunk;

import java.util.List;

@HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/Chunk")
@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/server/level/chunk/LevelChunk")
public final class RefChunk {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/Chunk;bukkitChunk:Lorg/bukkit/Chunk;")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/level/chunk/Chunk;bukkitChunk:Lorg/bukkit/Chunk;")
    @HandleBy(version = CbVersion.v1_19_R3, reference = "")
    public Chunk bukkitChunk;

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/Chunk;getEntitySlices()[Ljava/util/List;")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/server/level/chunk/LevelChunk")
    public native List<RefEntity>[] getEntitySlices();
}
