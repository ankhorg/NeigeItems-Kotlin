package pers.neige.neigeitems.ref.server.level;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/server/level/ServerChunkCache", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/ChunkProviderServer", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public class RefServerChunkCache {
    @HandleBy(reference = "Lnet/minecraft/server/level/ServerChunkCache;chunkMap:Lnet/minecraft/server/level/ChunkMap;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_14_R1/ChunkProviderServer;playerChunkMap:Lnet/minecraft/server/v1_14_R1/PlayerChunkMap;", predicates = "craftbukkit_version:[v1_14_R1,v1_17_R1)")
    public final RefChunkMap chunkMap = null;
}
