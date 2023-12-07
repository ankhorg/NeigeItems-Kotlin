package pers.neige.neigeitems.ref.network;

import pers.neige.neigeitems.ref.argument.RefAnchor;
import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/network/protocol/game/ClientboundPlayerLookAtPacket", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_13_R1/PacketPlayOutLookAt", predicates = "craftbukkit_version:[v1_13_R1,)")
public final class RefPacketPlayOutLookAt implements RefPacket<RefPacketListenerPlayOut> {
    @HandleBy(reference = "Lnet/minecraft/network/protocol/game/ClientboundPlayerLookAtPacket;<init>(Lnet/minecraft/commands/arguments/EntityAnchorArgument$Anchor;DDD)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_13_R1/PacketPlayOutLookAt;<init>(Lnet/minecraft/server/v1_13_R1/ArgumentAnchor$Anchor;DDD)V", predicates = "craftbukkit_version:[v1_13_R1,)")
    public RefPacketPlayOutLookAt(RefAnchor selfAnchor, double targetX, double targetY, double targetZ) {
        throw new UnsupportedOperationException();
    }
}
