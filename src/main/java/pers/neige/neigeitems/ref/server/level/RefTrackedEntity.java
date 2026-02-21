package pers.neige.neigeitems.ref.server.level;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.entity.RefEntity;
import pers.neige.neigeitems.ref.util.RefIntHashMap;

@HandleBy(reference = "net/minecraft/server/level/ChunkMap$TrackedEntity", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_14_R1/PlayerChunkMap$EntityTracker", predicates = "craftbukkit_version:[v1_14_R1,v1_17_R1)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/EntityTracker", predicates = "craftbukkit_version:[v1_12_R1,v1_14_R1)")
public class RefTrackedEntity {
    @HandleBy(reference = "Lnet/minecraft/server/level/ChunkMap$TrackedEntity;serverEntity:Lnet/minecraft/server/level/ServerEntity;", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_14_R1/PlayerChunkMap$EntityTracker;trackerEntry:Lnet/minecraft/server/v1_14_R1/EntityTrackerEntry;", useAccessor = true, predicates = "craftbukkit_version:[v1_14_R1,v1_17_R1)")
    public final RefServerEntity serverEntity = null;
    @HandleBy(reference = "Lnet/minecraft/server/level/ChunkMap$TrackedEntity;entity:Lnet/minecraft/world/entity/Entity;", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    public final RefEntity entity = null;
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityTracker;trackedEntities:Lnet/minecraft/server/v1_12_R1/IntHashMap;", predicates = "craftbukkit_version:[v1_12_R1,v1_14_R1)")
    public final RefIntHashMap<RefServerEntity> trackedEntities = null;

    @HandleBy(reference = "Lnet/minecraft/server/level/ChunkMap$TrackedEntity;<init>(Lnet/minecraft/server/level/ChunkMap;Lnet/minecraft/world/entity/Entity;IIZ)V", predicates = "craftbukkit_version:[v1_21_R1,)")
    public RefTrackedEntity(RefChunkMap chunkMap, RefEntity entity, final int range, final int updateInterval, final boolean trackDelta) {
        throw new UnsupportedOperationException();
    }
}
