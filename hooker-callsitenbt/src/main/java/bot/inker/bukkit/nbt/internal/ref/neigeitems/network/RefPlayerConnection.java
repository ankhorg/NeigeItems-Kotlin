package pers.neige.neigeitems.internal.ref.network;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.internal.ref.entity.RefRelativeMovement;

import java.util.Set;

@HandleBy(reference = "net/minecraft/server/network/ServerGamePacketListenerImpl", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/PlayerConnection", predicates = "craftbukkit_version:[v1_12_R1,)")
public final class RefPlayerConnection {
    @HandleBy(reference = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;connection:Lnet/minecraft/network/Connection;", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PlayerConnection;networkManager:Lnet/minecraft/server/v1_12_R1/NetworkManager;", predicates = "craftbukkit_version:[v1_12_R1,)")
    public final RefNetworkManager networkManager = null;
    @HandleBy(reference = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;awaitingTeleport:I", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PlayerConnection;teleportAwait:I", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,)")
    public int awaitingTeleport;

    @HandleBy(reference = "Lnet/minecraft/server/network/ServerPlayerConnection;send(Lnet/minecraft/network/protocol/Packet;)V", isInterface = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PlayerConnection;sendPacket(Lnet/minecraft/server/v1_12_R1/Packet;)V", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native void sendPacket(final RefPacket<?> packet);

    @HandleBy(reference = "Lnet/minecraft/network/protocol/game/ServerGamePacketListener;handleMovePlayer(Lnet/minecraft/network/protocol/game/ServerboundMovePlayerPacket;)V", isInterface = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PlayerConnection;a(Lnet/minecraft/server/v1_12_R1/PacketPlayInFlying;)V", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native void handleMovePlayer(RefPacketPlayInFlying packet);

    @HandleBy(reference = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;internalTeleport(DDDFFLjava/util/Set;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PlayerConnection;internalTeleport(DDDFFLjava/util/Set;)V", predicates = "craftbukkit_version:[v1_12_R1,)")
    public native void internalTeleport(double d0, double d1, double d2, float f, float f1, Set<RefRelativeMovement> set);
}
