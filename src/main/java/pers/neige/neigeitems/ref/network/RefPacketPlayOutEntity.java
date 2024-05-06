package pers.neige.neigeitems.ref.network;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/server/v1_12_R1/PacketPlayOutEntity", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
public class RefPacketPlayOutEntity implements RefPacket<RefPacketListenerPlayOut> {
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutEntity;a:I", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public int entityId;
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutEntity;b:I", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public int xa;
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutEntity;c:I", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public int ya;
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutEntity;d:I", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public int za;
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutEntity;e:B", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public byte yRot;
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutEntity;f:B", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public byte xRot;
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutEntity;g:Z", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public boolean onGround;
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutEntity;h:Z", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public boolean hasRot;

    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutEntity;<init>()V", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public RefPacketPlayOutEntity() {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutEntity;<init>(I)V", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public RefPacketPlayOutEntity(int entityId) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "net/minecraft/server/v1_12_R1/PacketPlayOutEntity$PacketPlayOutEntityLook", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public static class RefPacketPlayOutEntityLook extends RefPacketPlayOutEntity {
        @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutEntity$PacketPlayOutEntityLook;<init>()V", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
        public RefPacketPlayOutEntityLook() {
            throw new UnsupportedOperationException();
        }

        @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutEntity$PacketPlayOutEntityLook;<init>(IBBZ)V", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
        public RefPacketPlayOutEntityLook(int entityId, byte yRot, byte xRot, boolean onGround) {
            throw new UnsupportedOperationException();
        }
    }

    @HandleBy(reference = "net/minecraft/server/v1_12_R1/PacketPlayOutEntity$PacketPlayOutRelEntityMove", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public static class RefPacketPlayOutRelEntityMove extends RefPacketPlayOutEntity {
        @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutEntity$PacketPlayOutRelEntityMove;<init>()V", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
        public RefPacketPlayOutRelEntityMove() {
            throw new UnsupportedOperationException();
        }

        @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutEntity$PacketPlayOutRelEntityMove;<init>(IJJJZ)V", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
        public RefPacketPlayOutRelEntityMove(int entityId, long xa, long ya, long za, boolean onGround) {
            throw new UnsupportedOperationException();
        }
    }

    @HandleBy(reference = "net/minecraft/server/v1_12_R1/PacketPlayOutEntity$PacketPlayOutRelEntityMoveLook", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public static class RefPacketPlayOutRelEntityMoveLook extends RefPacketPlayOutEntity {
        @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutEntity$PacketPlayOutRelEntityMoveLook;<init>()V", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
        public RefPacketPlayOutRelEntityMoveLook() {
            throw new UnsupportedOperationException();
        }

        @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutEntity$PacketPlayOutRelEntityMoveLook;<init>(IJJJBBZ)V", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
        public RefPacketPlayOutRelEntityMoveLook(int entityId, long xa, long ya, long za, byte yRot, byte xRot, boolean onGround) {
            throw new UnsupportedOperationException();
        }
    }
}
