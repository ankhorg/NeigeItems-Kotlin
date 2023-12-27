package pers.neige.neigeitems.ref.server.level;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/server/level/ChunkMap", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/PlayerChunkMap", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public class RefChunkMap {
    @HandleBy(reference = "Lnet/minecraft/server/level/ChunkMap;entityMap:Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;", predicates = "craftbukkit_version:[v1_18_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/level/PlayerChunkMap;G:Lorg/bukkit/craftbukkit/libs/it/unimi/dsi/fastutil/ints/Int2ObjectMap;", predicates = "craftbukkit_version:[v1_17_R1,v1_18_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_14_R1/PlayerChunkMap;trackedEntities:Lorg/bukkit/craftbukkit/libs/it/unimi/dsi/fastutil/ints/Int2ObjectMap;", predicates = "craftbukkit_version:[v1_14_R1,v1_17_R1)")
    public final Object entityMap = null;
}
