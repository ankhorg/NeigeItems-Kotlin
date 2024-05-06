package pers.neige.neigeitems.ref.network;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.entity.RefEntity;

@HandleBy(reference = "net/minecraft/server/v1_12_R1/PacketPlayOutEntityHeadRotation", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
public class RefPacketPlayOutEntityHeadRotation implements RefPacket<RefPacketListenerPlayOut> {
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutEntityHeadRotation;a:I", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    private int entityId;
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutEntityHeadRotation;b:B", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    private byte yHeadRot;

    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutEntityHeadRotation;<init>()V", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public RefPacketPlayOutEntityHeadRotation() {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutEntityHeadRotation;<init>(Lnet/minecraft/server/v1_12_R1/Entity;B)V", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public RefPacketPlayOutEntityHeadRotation(RefEntity entity, byte yHeadRot) {
        throw new UnsupportedOperationException();
    }
}
