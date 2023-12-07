package pers.neige.neigeitems.ref.network;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/network/protocol/game/ServerboundMovePlayerPacket", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/PacketPlayInFlying", predicates = "craftbukkit_version:[v1_12_R1,)")
public class RefPacketPlayInFlying implements RefPacket<RefPacketListenerPlayIn> {
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayInFlying;x:D", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public double x;
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayInFlying;y:D", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public double y;
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayInFlying;z:D", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public double z;
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayInFlying;yaw:F", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public float yaw;
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayInFlying;pitch:F", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public float pitch;
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayInFlying;f:Z", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public boolean onGround;
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayInFlying;hasPos:Z", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public boolean changePosition;
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayInFlying;hasLook:Z", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public boolean changeLook;

    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayInFlying;<init>()V", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public RefPacketPlayInFlying() {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "net/minecraft/network/protocol/game/ServerboundMovePlayerPacket$Rot", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "net/minecraft/server/v1_12_R1/PacketPlayInFlying$PacketPlayInLook", predicates = "craftbukkit_version:[v1_12_R1,)")
    public static class Rot extends RefPacketPlayInFlying {
        @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayInFlying$PacketPlayInLook;<init>()V", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
        public Rot() {
            throw new UnsupportedOperationException();
        }

        @HandleBy(reference = "Lnet/minecraft/network/protocol/game/ServerboundMovePlayerPacket$Rot;<init>(FFZ)V", predicates = "craftbukkit_version:[v1_17_R1,)")
        public Rot(float yaw, float pitch, boolean onGroun) {
            throw new UnsupportedOperationException();
        }
    }

    @HandleBy(reference = "net/minecraft/network/protocol/game/ServerboundMovePlayerPacket$Pos", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "net/minecraft/server/v1_12_R1/PacketPlayInFlying$PacketPlayInPosition", predicates = "craftbukkit_version:[v1_12_R1,)")
    public static class Pos extends RefPacketPlayInFlying {
        @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayInFlying$PacketPlayInPosition;<init>()V", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
        public Pos() {
            throw new UnsupportedOperationException();
        }

        @HandleBy(reference = "Lnet/minecraft/network/protocol/game/ServerboundMovePlayerPacket$Pos;<init>(DDDZ)V", predicates = "craftbukkit_version:[v1_17_R1,)")
        public Pos(double x, double y, double z, boolean onGround) {
            throw new UnsupportedOperationException();
        }
    }

    @HandleBy(reference = "net/minecraft/network/protocol/game/ServerboundMovePlayerPacket$PosRot", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "net/minecraft/server/v1_12_R1/PacketPlayInFlying$PacketPlayInPositionLook", predicates = "craftbukkit_version:[v1_12_R1,)")
    public static class PosRot extends RefPacketPlayInFlying {
        @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayInFlying$PacketPlayInPositionLook;<init>()V", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
        public PosRot() {
            throw new UnsupportedOperationException();
        }

        @HandleBy(reference = "Lnet/minecraft/network/protocol/game/ServerboundMovePlayerPacket$PosRot;<init>(DDDFFZ)V", predicates = "craftbukkit_version:[v1_17_R1,)")
        public PosRot(double x, double y, double z, float yaw, float pitch, boolean onGround) {
            throw new UnsupportedOperationException();
        }
    }
}
