package bot.inker.bukkit.nbt.internal.ref.neigeitems.network;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;

@HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/PlayerConnection")
@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/server/network/ServerGamePacketListenerImpl")
public final class RefPlayerConnection {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/PlayerConnection;networkManager:Lnet/minecraft/server/v1_12_R1/NetworkManager;")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;connection:Lnet/minecraft/network/Connection;")
    public final RefNetworkManager networkManager = null;

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/PlayerConnection;sendPacket(Lnet/minecraft/server/v1_12_R1/Packet;)V")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/server/network/PlayerConnection;sendPacket(Lnet/minecraft/network/protocol/Packet;)V")
    @HandleBy(version = CbVersion.v1_18_R1, reference = "Lnet/minecraft/server/network/PlayerConnection;a(Lnet/minecraft/network/protocol/Packet;)V")
    @HandleBy(version = CbVersion.v1_20_R2, reference = "Lnet/minecraft/server/network/PlayerConnection;b(Lnet/minecraft/network/protocol/Packet;)V")
    public native void sendPacket(final RefPacket<?> packet);

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/PlayerConnection;a(Lnet/minecraft/server/v1_12_R1/PacketPlayInFlying;)V")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;handleMovePlayer(Lnet/minecraft/network/protocol/game/ServerboundMovePlayerPacket;)V")
    public native void handleMovePlayer(RefPacketPlayInFlying packet);
}
