package pers.neige.neigeitems.ref.world;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.util.Consumer;
import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.entity.RefEntity;
import pers.neige.neigeitems.ref.server.level.RefWorldServer;

@HandleBy(reference = "org/bukkit/craftbukkit/v1_12_R1/CraftWorld", predicates = "craftbukkit_version:[v1_12_R1,)")
public final class RefCraftWorld {
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_17_R1/CraftWorld;getHandle()Lnet/minecraft/server/level/ServerLevel;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/CraftWorld;getHandle()Lnet/minecraft/server/v1_12_R1/WorldServer;", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native RefWorldServer getHandle();

    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_17_R1/CraftRegionAccessor;createEntity(Lorg/bukkit/Location;Ljava/lang/Class;Z)Lnet/minecraft/world/entity/Entity;", predicates = "craftbukkit_version:[v1_17_R1,)")
    public native RefEntity createEntity(Location location, Class<? extends Entity> clazz, boolean randomizeData);

    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/CraftWorld;createEntity(Lorg/bukkit/Location;Ljava/lang/Class;)Lnet/minecraft/server/v1_12_R1/Entity;", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native RefEntity createEntity(Location location, Class<? extends Entity> clazz);

    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_12_R1/CraftWorld;addEntity(Lnet/minecraft/server/v1_12_R1/Entity;Lorg/bukkit/event/entity/CreatureSpawnEvent$SpawnReason;Lorg/bukkit/util/Consumer;)Lorg/bukkit/entity/Entity;", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public native <T extends Entity> T addEntity(RefEntity entity, CreatureSpawnEvent.SpawnReason reason, Consumer<T> function);

    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_17_R1/CraftRegionAccessor;addEntity(Lnet/minecraft/world/entity/Entity;Lorg/bukkit/event/entity/CreatureSpawnEvent$SpawnReason;Lorg/bukkit/util/Consumer;Z)Lorg/bukkit/entity/Entity;", predicates = "craftbukkit_version:[v1_17_R1,v1_20_R2)")
    public native <T extends Entity> T addEntity(RefEntity entity, CreatureSpawnEvent.SpawnReason reason, Consumer<T> function, boolean randomizeData);

    @HandleBy(reference = "Lorg/bukkit/craftbukkit/v1_20_R2/CraftRegionAccessor;addEntity(Lnet/minecraft/world/entity/Entity;Lorg/bukkit/event/entity/CreatureSpawnEvent$SpawnReason;Ljava/util/function/Consumer;Z)Lorg/bukkit/entity/Entity;", predicates = "craftbukkit_version:[v1_20_R2,)")
    public native <T extends Entity> T addEntity(RefEntity entity, CreatureSpawnEvent.SpawnReason reason, java.util.function.Consumer<T> function, boolean randomizeData);
}
