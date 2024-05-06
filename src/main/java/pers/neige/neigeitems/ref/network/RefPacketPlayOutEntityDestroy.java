package pers.neige.neigeitems.ref.network;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/server/v1_12_R1/PacketPlayOutEntityDestroy", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
public class RefPacketPlayOutEntityDestroy implements RefPacket<RefPacketListenerPlayOut> {
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutEntityDestroy;a:[I", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    private int[] entityIds;

    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutEntityDestroy;<init>()V", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public RefPacketPlayOutEntityDestroy() {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutEntityDestroy;<init>([I)V", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public RefPacketPlayOutEntityDestroy(int... entityIds) {
        throw new UnsupportedOperationException();
    }
}
