package pers.neige.neigeitems.ref.network;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.entity.RefEntity;

@HandleBy(reference = "net/minecraft/server/v1_12_R1/PacketPlayOutEntityTeleport", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
public class RefPacketPlayOutEntityTeleport implements RefPacket<RefPacketListenerPlayOut> {
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutEntityTeleport;a:I", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public int entityId;
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutEntityTeleport;b:D", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public double x;
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutEntityTeleport;c:D", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public double y;
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutEntityTeleport;d:D", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public double z;
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutEntityTeleport;e:B", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public byte yRot;
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutEntityTeleport;f:B", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public byte xRot;
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutEntityTeleport;g:Z", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public boolean onGround;

    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutEntityTeleport;<init>()V", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public RefPacketPlayOutEntityTeleport() {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutEntityTeleport;<init>(Lnet/minecraft/server/v1_12_R1/Entity;)V", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public RefPacketPlayOutEntityTeleport(RefEntity entity) {
        throw new UnsupportedOperationException();
    }
}
