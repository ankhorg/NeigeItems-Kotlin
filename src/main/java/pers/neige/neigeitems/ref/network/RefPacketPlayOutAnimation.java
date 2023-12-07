package pers.neige.neigeitems.ref.network;

import pers.neige.neigeitems.ref.entity.RefEntity;
import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/network/protocol/game/ClientboundAnimatePacket", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/PacketPlayOutAnimation", predicates = "craftbukkit_version:[v1_12_R1,)")
public final class RefPacketPlayOutAnimation implements RefPacket<RefPacketListenerPlayOut> {
    @HandleBy(reference = "Lnet/minecraft/network/protocol/game/ClientboundAnimatePacket;<init>(Lnet/minecraft/world/entity/Entity;I)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutAnimation;<init>(Lnet/minecraft/server/v1_12_R1/Entity;I)V", predicates = "craftbukkit_version:[v1_12_R1,)")
    public RefPacketPlayOutAnimation(RefEntity entity, int animationId) {
        throw new UnsupportedOperationException();
    }
}
