package pers.neige.neigeitems.ref.server.level;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.entity.RefEntity;
import pers.neige.neigeitems.ref.network.RefPacket;
import pers.neige.neigeitems.ref.network.RefPlayerConnection;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@HandleBy(reference = "net/minecraft/server/level/ServerEntity", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/EntityTrackerEntry", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public class RefServerEntity {
    @HandleBy(reference = "Lnet/minecraft/server/level/ServerEntity;entity:Lnet/minecraft/world/entity/Entity;", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityTrackerEntry;tracker:Lnet/minecraft/server/v1_12_R1/Entity;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public final RefEntity entity;

    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityTrackerEntry;<init>(Lnet/minecraft/server/v1_12_R1/Entity;IIIZ)V", predicates = "craftbukkit_version:[v1_12_R1,v1_14_R1)")
    public RefServerEntity(RefEntity entity, int i, int j, int k, boolean flag) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/server/level/ServerEntity;<init>(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/Entity;IZLjava/util/function/Consumer;Ljava/util/Set;)V", predicates = "craftbukkit_version:[v1_21_R1,v1_21_R4)")
    public RefServerEntity(
        RefWorldServer worldServer,
        RefEntity entity,
        int updateInterval,
        boolean trackDeltas,
        Consumer<RefPacket<?>> consumer,
        Set<RefPlayerConnection> trackedPlayers
    ) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/server/level/ServerEntity;<init>(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/Entity;IZLjava/util/function/Consumer;Ljava/util/function/BiConsumer;Ljava/util/Set;)V", predicates = "craftbukkit_version:[v1_21_R4,v1_21_R6)")
    public RefServerEntity(
        RefWorldServer worldServer,
        RefEntity entity,
        int updateInterval,
        boolean trackDeltas,
        Consumer<RefPacket<?>> consumer,
        BiConsumer<RefPacket<?>, List<UUID>> broadcastWithIgnore,
        Set<RefPlayerConnection> trackedPlayers
    ) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/server/level/ServerEntity;<init>(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/Entity;IZLnet/minecraft/server/level/ServerEntity$Synchronizer;Ljava/util/Set;)V", predicates = "craftbukkit_version:[v1_21_R6,)")
    public RefServerEntity(
        RefWorldServer worldServer,
        RefEntity entity,
        int updateInterval,
        boolean trackDeltas,
        RefServerEntitySynchronizer synchronizer,
        Set<RefPlayerConnection> trackedPlayers
    ) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityTrackerEntry;e()Lnet/minecraft/server/v1_12_R1/Packet;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_14_R1)")
    public native RefPacket<?> createPacket();

    @HandleBy(reference = "Lnet/minecraft/server/level/ServerEntity;broadcastAndSend(Lnet/minecraft/network/protocol/Packet;)V", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_14_R1/EntityTrackerEntry;broadcastIncludingSelf(Lnet/minecraft/server/v1_14_R1/Packet;)V", predicates = "craftbukkit_version:[v1_14_R1,v1_17_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/EntityTrackerEntry;broadcastIncludingSelf(Lnet/minecraft/server/v1_12_R1/Packet;)V", predicates = "craftbukkit_version:[v1_12_R1,v1_14_R1)")
    public native void broadcastAndSend(RefPacket<?> packet);
}
