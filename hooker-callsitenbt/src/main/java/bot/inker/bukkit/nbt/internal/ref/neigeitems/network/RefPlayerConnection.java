package bot.inker.bukkit.nbt.internal.ref.neigeitems.network;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.entity.RefRelativeMovement;

import java.util.Set;

@HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/PlayerConnection")
@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/server/network/ServerGamePacketListenerImpl")
public final class RefPlayerConnection {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/PlayerConnection;networkManager:Lnet/minecraft/server/v1_12_R1/NetworkManager;")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;connection:Lnet/minecraft/network/Connection;")
    public final RefNetworkManager networkManager = null;
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/PlayerConnection;teleportAwait:I", accessor = true)
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;awaitingTeleport:I", accessor = true)
    public int awaitingTeleport;

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/PlayerConnection;sendPacket(Lnet/minecraft/server/v1_12_R1/Packet;)V")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/server/network/ServerPlayerConnection;send(Lnet/minecraft/network/protocol/Packet;)V", isInterface = true)
    public native void sendPacket(final RefPacket<?> packet);

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/PlayerConnection;a(Lnet/minecraft/server/v1_12_R1/PacketPlayInFlying;)V")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/network/protocol/game/ServerGamePacketListener;handleMovePlayer(Lnet/minecraft/network/protocol/game/ServerboundMovePlayerPacket;)V", isInterface = true)
    public native void handleMovePlayer(RefPacketPlayInFlying packet);

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/PlayerConnection;internalTeleport(DDDFFLjava/util/Set;)V")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;internalTeleport(DDDFFLjava/util/Set;)V")
    public native void internalTeleport(double d0, double d1, double d2, float f, float f1, Set<RefRelativeMovement> set);
}
