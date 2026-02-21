package pers.neige.neigeitems.ref.server.level;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.entity.RefEntityPlayer;
import pers.neige.neigeitems.ref.network.RefPacket;

import java.util.function.Predicate;

@HandleBy(reference = "net/minecraft/server/level/ServerEntity$Synchronizer", predicates = "craftbukkit_version:[v1_21_R6,)")
public interface RefServerEntitySynchronizer {
    @HandleBy(reference = "Lnet/minecraft/server/level/ServerEntity$Synchronizer;sendToTrackingPlayers(Lnet/minecraft/network/protocol/Packet;)V", predicates = "craftbukkit_version:[v1_21_R6,)")
    void sendToTrackingPlayers(RefPacket<?> packet);

    @HandleBy(reference = "Lnet/minecraft/server/level/ServerEntity$Synchronizer;sendToTrackingPlayersAndSelf(Lnet/minecraft/network/protocol/Packet;)V", predicates = "craftbukkit_version:[v1_21_R6,)")
    void sendToTrackingPlayersAndSelf(RefPacket<?> packet);

    @HandleBy(reference = "Lnet/minecraft/server/level/ServerEntity$Synchronizer;sendToTrackingPlayersFiltered(Lnet/minecraft/network/protocol/Packet;Ljava/util/function/Predicate;)V", predicates = "craftbukkit_version:[v1_21_R6,)")
    void sendToTrackingPlayersFiltered(RefPacket<?> packet, Predicate<RefEntityPlayer> filter);
}
