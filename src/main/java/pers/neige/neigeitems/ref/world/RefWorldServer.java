package pers.neige.neigeitems.ref.world;

import pers.neige.neigeitems.ref.entity.RefEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/server/level/ServerLevel", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/WorldServer", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public final class RefWorldServer extends RefWorld {
    @HandleBy(reference = "Lnet/minecraft/server/level/ServerLevel;addEntity(Lnet/minecraft/world/entity/Entity;Lorg/bukkit/event/entity/CreatureSpawnEvent$SpawnReason;)Z", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/WorldServer;addEntity(Lnet/minecraft/server/v1_12_R1/Entity;Lorg/bukkit/event/entity/CreatureSpawnEvent$SpawnReason;)Z", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native boolean addEntity(RefEntity entity, CreatureSpawnEvent.SpawnReason spawnReason);
}
