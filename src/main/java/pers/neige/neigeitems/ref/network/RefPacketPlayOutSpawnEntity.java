package pers.neige.neigeitems.ref.network;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.block.RefBlockPos;
import pers.neige.neigeitems.ref.entity.RefEntity;
import pers.neige.neigeitems.ref.entity.RefEntityType;
import pers.neige.neigeitems.ref.world.RefVec3;

import java.util.UUID;

@HandleBy(reference = "net/minecraft/network/protocol/game/ClientboundAddEntityPacket", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/PacketPlayOutSpawnEntity", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public class RefPacketPlayOutSpawnEntity implements RefPacket<RefPacketListenerPlayOut> {
    @HandleBy(reference = "Lnet/minecraft/network/protocol/game/ClientboundAddEntityPacket;id:I", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutSpawnEntity;a:I", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public final int id = 0;
    @HandleBy(reference = "Lnet/minecraft/network/protocol/game/ClientboundAddEntityPacket;uuid:Ljava/util/UUID;", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutSpawnEntity;b:Ljava/util/UUID;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public final UUID uuid = null;
    @HandleBy(reference = "Lnet/minecraft/network/protocol/game/ClientboundAddEntityPacket;type:Lnet/minecraft/world/entity/EntityType;", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_14_R1/PacketPlayOutSpawnEntity;k:Lnet/minecraft/server/v1_14_R1/EntityTypes;", useAccessor = true, predicates = "craftbukkit_version:[v1_14_R1,v1_17_R1)")
    public final RefEntityType<?> type = null;
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutSpawnEntity;k:I", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_14_R1)")
    public final int typeId = 0;
    @HandleBy(reference = "Lnet/minecraft/network/protocol/game/ClientboundAddEntityPacket;x:D", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutSpawnEntity;c:D", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public final double x = 0.0;
    @HandleBy(reference = "Lnet/minecraft/network/protocol/game/ClientboundAddEntityPacket;y:D", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutSpawnEntity;d:D", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public final double y = 0.0;
    @HandleBy(reference = "Lnet/minecraft/network/protocol/game/ClientboundAddEntityPacket;z:D", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutSpawnEntity;e:D", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public final double z = 0.0;
    @HandleBy(reference = "Lnet/minecraft/network/protocol/game/ClientboundAddEntityPacket;xa:I", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutSpawnEntity;f:I", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public final int xa = 0;
    @HandleBy(reference = "Lnet/minecraft/network/protocol/game/ClientboundAddEntityPacket;ya:I", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutSpawnEntity;g:I", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public final int ya = 0;
    @HandleBy(reference = "Lnet/minecraft/network/protocol/game/ClientboundAddEntityPacket;za:I", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutSpawnEntity;h:I", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public final int za = 0;
    @HandleBy(reference = "Lnet/minecraft/network/protocol/game/ClientboundAddEntityPacket;xRot:B", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutSpawnEntity;i:I", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public final byte pitch = 0;
    @HandleBy(reference = "Lnet/minecraft/network/protocol/game/ClientboundAddEntityPacket;yRot:B", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutSpawnEntity;j:I", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public final byte yaw = 0;
    @HandleBy(reference = "Lnet/minecraft/network/protocol/game/ClientboundAddEntityPacket;yHeadRot:B", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public final byte headYaw = 0;
    @HandleBy(reference = "Lnet/minecraft/network/protocol/game/ClientboundAddEntityPacket;data:I", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutSpawnEntity;l:I", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public final int data = 0;

    // 参数2在1.12-1.13代表type, 在1.14+代表data
    @HandleBy(reference = "Lnet/minecraft/network/protocol/game/ClientboundAddEntityPacket;<init>(Lnet/minecraft/world/entity/Entity;I)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutSpawnEntity;<init>(Lnet/minecraft/server/v1_12_R1/Entity;I)V", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public RefPacketPlayOutSpawnEntity(RefEntity entity, int typeOrData) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutSpawnEntity;<init>(Lnet/minecraft/server/v1_12_R1/Entity;II)V", predicates = "craftbukkit_version:[v1_12_R1,v1_14_R1)")
    public RefPacketPlayOutSpawnEntity(RefEntity entity, int type, int data) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutSpawnEntity;<init>(Lnet/minecraft/server/v1_12_R1/Entity;IILnet/minecraft/server/v1_12_R1/BlockPosition;)V", predicates = "craftbukkit_version:[v1_12_R1,v1_14_R1)")
    public RefPacketPlayOutSpawnEntity(RefEntity entity, int type, int data, RefBlockPos position) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/server/v1_14_R1/PacketPlayOutSpawnEntity;<init>()V", predicates = "craftbukkit_version:[v1_14_R1,v1_17_R1)")
    public RefPacketPlayOutSpawnEntity() {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/network/protocol/game/ClientboundAddEntityPacket;<init>(ILjava/util/UUID;DDDFFLnet/minecraft/world/entity/EntityType;ILnet/minecraft/world/phys/Vec3;)V", predicates = "craftbukkit_version:[v1_17_R1,v1_19_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_14_R1/PacketPlayOutSpawnEntity;<init>(ILjava/util/UUID;DDDFFLnet/minecraft/server/v1_14_R1/EntityTypes;ILnet/minecraft/server/v1_14_R1/Vec3D;)V", predicates = "craftbukkit_version:[v1_14_R1,v1_17_R1)")
    public RefPacketPlayOutSpawnEntity(int id, UUID uuid, double x, double y, double z, float pitch, float yaw, RefEntityType<?> type, int data, RefVec3 velocity) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/network/protocol/game/ClientboundAddEntityPacket;<init>(ILjava/util/UUID;DDDFFLnet/minecraft/world/entity/EntityType;ILnet/minecraft/world/phys/Vec3;D)V", predicates = "craftbukkit_version:[v1_19_R1,)")
    public RefPacketPlayOutSpawnEntity(int id, UUID uuid, double x, double y, double z, float pitch, float yaw, RefEntityType<?> type, int data, RefVec3 velocity, double headYaw) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/network/protocol/game/ClientboundAddEntityPacket;<init>(Lnet/minecraft/world/entity/Entity;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_14_R1/PacketPlayOutSpawnEntity;<init>(Lnet/minecraft/server/v1_14_R1/Entity;)V", predicates = "craftbukkit_version:[v1_14_R1,v1_17_R1)")
    public RefPacketPlayOutSpawnEntity(RefEntity entity) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/network/protocol/game/ClientboundAddEntityPacket;<init>(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/EntityType;ILnet/minecraft/core/BlockPos;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_14_R1/PacketPlayOutSpawnEntity;<init>(Lnet/minecraft/server/v1_14_R1/Entity;Lnet/minecraft/server/v1_14_R1/EntityTypes;ILnet/minecraft/server/v1_14_R1/BlockPosition;)V", predicates = "craftbukkit_version:[v1_14_R1,v1_17_R1)")
    public RefPacketPlayOutSpawnEntity(RefEntity entity, RefEntityType<?> type, int data, RefBlockPos velocity) {
        throw new UnsupportedOperationException();
    }
}
