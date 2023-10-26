package bot.inker.bukkit.nbt.internal.ref.neigeitems.network;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.argument.RefAnchor;

@HandleBy(version = CbVersion.v1_13_R1, reference = "net/minecraft/server/v1_13_R1/PacketPlayOutLookAt")
@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/network/protocol/game/ClientboundPlayerLookAtPacket")
public final class RefPacketPlayOutLookAt implements RefPacket<RefPacketListenerPlayOut> {
    @HandleBy(version = CbVersion.v1_13_R1, reference = "Lnet/minecraft/server/v1_13_R1/PacketPlayOutLookAt;<init>(Lnet/minecraft/server/v1_13_R1/ArgumentAnchor$Anchor;DDD)V")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/network/protocol/game/ClientboundPlayerLookAtPacket;<init>(Lnet/minecraft/commands/arguments/EntityAnchorArgument$Anchor;DDD)V")
    public RefPacketPlayOutLookAt(RefAnchor selfAnchor, double targetX, double targetY, double targetZ) {
        throw new UnsupportedOperationException();
    }
}
