package pers.neige.neigeitems.internal.ref.world;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.internal.ref.entity.RefEntity;
import org.bukkit.Chunk;

import java.util.List;

@HandleBy(reference = "net/minecraft/server/level/chunk/LevelChunk", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/Chunk", predicates = "craftbukkit_version:[v1_12_R1,)")
public final class RefChunk {
    @HandleBy(reference = "Lnet/minecraft/world/level/chunk/Chunk;bukkitChunk:Lorg/bukkit/Chunk;", predicates = "craftbukkit_version:[v1_17_R1,v1_19_R3)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Chunk;bukkitChunk:Lorg/bukkit/Chunk;", predicates = "craftbukkit_version:[v1_12_R1,)")
    public Chunk bukkitChunk;

    @HandleBy(reference = "net/minecraft/server/level/chunk/LevelChunk", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/Chunk;getEntitySlices()[Ljava/util/List;", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native List<RefEntity>[] getEntitySlices();
}
