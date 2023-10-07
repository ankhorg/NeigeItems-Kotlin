package bot.inker.bukkit.nbt.internal.ref;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketListenerPlayOut;

@HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/PacketPlayOutAnimation")
@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/network/protocol/game/ClientboundAnimatePacket")
public final class RefPacketPlayOutAnimation implements RefPacket<RefPacketListenerPlayOut> {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutAnimation;<init>(Lnet/minecraft/server/v1_12_R1/Entity;I)V")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/network/protocol/game/ClientboundAnimatePacket;<init>(Lnet/minecraft/world/entity/Entity;I)V")
    public RefPacketPlayOutAnimation(RefEntity entity, int animationId) {
        throw new UnsupportedOperationException();
    }
}
