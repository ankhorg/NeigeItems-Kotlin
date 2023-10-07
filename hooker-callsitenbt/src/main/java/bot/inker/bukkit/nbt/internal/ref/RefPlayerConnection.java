package bot.inker.bukkit.nbt.internal.ref;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;

@HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/PlayerConnection")
@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/server/network/ServerGamePacketListenerImpl")
public final class RefPlayerConnection {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/PlayerConnection;sendPacket(Lnet/minecraft/server/v1_12_R1/Packet;)V")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;send(Lnet/minecraft/network/protocol/Packet;)V")
    public native void sendPacket(final RefPacket<?> packet);
}
