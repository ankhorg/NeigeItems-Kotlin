package pers.neige.neigeitems.ref.server.level;

import org.bukkit.event.entity.CreatureSpawnEvent;
import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.entity.RefEntity;
import pers.neige.neigeitems.ref.world.RefWorld;

@HandleBy(reference = "net/minecraft/server/level/ServerLevel", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/WorldServer", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public final class RefWorldServer extends RefWorld {
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/WorldServer;tracker:Lnet/minecraft/server/v1_12_R1/EntityTracker;", predicates = "craftbukkit_version:[v1_12_R1,v1_14_R1)")
    public RefTrackedEntity tracker;

    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/WorldServer;manager:Lnet/minecraft/server/v1_12_R1/PlayerChunkMap;", predicates = "craftbukkit_version:[v1_12_R1,v1_14_R1)")
    private final RefChunkMap manager = null;

    @HandleBy(reference = "Lnet/minecraft/server/level/ServerLevel;addEntity(Lnet/minecraft/world/entity/Entity;Lorg/bukkit/event/entity/CreatureSpawnEvent$SpawnReason;)Z", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/WorldServer;addEntity(Lnet/minecraft/server/v1_12_R1/Entity;Lorg/bukkit/event/entity/CreatureSpawnEvent$SpawnReason;)Z", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native boolean addEntity(RefEntity entity, CreatureSpawnEvent.SpawnReason spawnReason);

    @HandleBy(reference = "Lnet/minecraft/server/level/ServerLevel;getChunkSource()Lnet/minecraft/server/level/ServerChunkCache;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_14_R1/WorldServer;getChunkProvider()Lnet/minecraft/server/v1_14_R1/ChunkProviderServer;", predicates = "craftbukkit_version:[v1_14_R1,v1_17_R1)")
    public native RefServerChunkCache getChunkSource();
}
