package bot.inker.bukkit.nbt.internal.ref.neigeitems.network;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.argument.RefAnchor;

@HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/PacketPlayInFlying")
@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/network/protocol/game/ServerboundMovePlayerPacket")
public class RefPacketPlayInFlying implements RefPacket<RefPacketListenerPlayIn> {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayInFlying;x:D", accessor = true)
    @HandleBy(version = CbVersion.v1_17_R1, reference = "")
    public double x;
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayInFlying;y:D", accessor = true)
    @HandleBy(version = CbVersion.v1_17_R1, reference = "")
    public double y;
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayInFlying;z:D", accessor = true)
    @HandleBy(version = CbVersion.v1_17_R1, reference = "")
    public double z;
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayInFlying;yaw:F", accessor = true)
    @HandleBy(version = CbVersion.v1_17_R1, reference = "")
    public float yaw;
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayInFlying;pitch:F", accessor = true)
    @HandleBy(version = CbVersion.v1_17_R1, reference = "")
    public float pitch;
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayInFlying;f:Z", accessor = true)
    @HandleBy(version = CbVersion.v1_17_R1, reference = "")
    public boolean onGround;
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayInFlying;hasPos:Z", accessor = true)
    @HandleBy(version = CbVersion.v1_17_R1, reference = "")
    public boolean changePosition;
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayInFlying;hasLook:Z", accessor = true)
    @HandleBy(version = CbVersion.v1_17_R1, reference = "")
    public boolean changeLook;

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayInFlying;<init>()V")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "")
    public RefPacketPlayInFlying() {
        throw new UnsupportedOperationException();
    }

    @HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/PacketPlayInFlying$PacketPlayInLook")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/network/protocol/game/ServerboundMovePlayerPacket$Rot")
    public static class Rot extends RefPacketPlayInFlying {
        @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayInFlying$PacketPlayInLook;<init>()V")
        public Rot() {
            throw new UnsupportedOperationException();
        }

        @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/network/protocol/game/ServerboundMovePlayerPacket$Rot;<init>(FFZ)V")
        public Rot(float yaw, float pitch, boolean onGroun) {
            throw new UnsupportedOperationException();
        }
    }

    @HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/PacketPlayInFlying$PacketPlayInPosition")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/network/protocol/game/ServerboundMovePlayerPacket$Pos")
    public static class Pos extends RefPacketPlayInFlying {
        @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayInFlying$PacketPlayInPosition;<init>()V")
        public Pos() {
            throw new UnsupportedOperationException();
        }

        @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/network/protocol/game/ServerboundMovePlayerPacket$Pos;<init>(DDDZ)V")
        public Pos(double x, double y, double z, boolean onGround) {
            throw new UnsupportedOperationException();
        }
    }

    @HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/PacketPlayInFlying$PacketPlayInPositionLook")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/network/protocol/game/ServerboundMovePlayerPacket$PosRot")
    public static class PosRot extends RefPacketPlayInFlying {
        @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayInFlying$PacketPlayInPositionLook;<init>()V")
        public PosRot() {
            throw new UnsupportedOperationException();
        }

        @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/network/protocol/game/ServerboundMovePlayerPacket$PosRot;<init>(DDDFFZ)V")
        public PosRot(double x, double y, double z, float yaw, float pitch, boolean onGround) {
            throw new UnsupportedOperationException();
        }
    }
}
