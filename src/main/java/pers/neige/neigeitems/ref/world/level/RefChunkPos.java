package pers.neige.neigeitems.ref.world.level;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/world/level/ChunkPos", predicates = "craftbukkit_version:[v1_17_R1,)")
public class RefChunkPos {
    @HandleBy(reference = "Lnet/minecraft/world/level/ChunkPos;<init>(II)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    public RefChunkPos(int x, int z) {
        throw new UnsupportedOperationException();
    }
}
