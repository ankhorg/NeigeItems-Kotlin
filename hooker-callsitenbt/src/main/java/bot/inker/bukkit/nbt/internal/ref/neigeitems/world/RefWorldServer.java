package bot.inker.bukkit.nbt.internal.ref.neigeitems.world;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.entity.RefEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;

@HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/WorldServer")
@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/server/level/ServerLevel")
public final class RefWorldServer extends RefWorld {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/WorldServer;addEntity(Lnet/minecraft/server/v1_12_R1/Entity;Lorg/bukkit/event/entity/CreatureSpawnEvent$SpawnReason;)Z")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/server/level/ServerLevel;addEntity(Lnet/minecraft/world/entity/Entity;Lorg/bukkit/event/entity/CreatureSpawnEvent$SpawnReason;)Z", accessor = true)
    public native boolean addEntity(RefEntity entity, CreatureSpawnEvent.SpawnReason spawnReason);
}
