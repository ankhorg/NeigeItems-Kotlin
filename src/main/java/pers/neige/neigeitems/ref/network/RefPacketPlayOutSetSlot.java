package pers.neige.neigeitems.ref.network;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.block.RefBlockPos;
import pers.neige.neigeitems.ref.entity.RefEntity;
import pers.neige.neigeitems.ref.entity.RefEntityType;
import pers.neige.neigeitems.ref.nbt.RefNmsItemStack;
import pers.neige.neigeitems.ref.world.RefVec3;

import java.util.UUID;

@HandleBy(reference = "net/minecraft/network/protocol/game/ClientboundContainerSetSlotPacket", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/PacketPlayOutSetSlot", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public class RefPacketPlayOutSetSlot implements RefPacket<RefPacketListenerPlayOut> {
    @HandleBy(reference = "Lnet/minecraft/network/protocol/game/ClientboundContainerSetSlotPacket;itemStack:Lnet/minecraft/world/item/ItemStack;", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutSetSlot;c:Lnet/minecraft/server/v1_12_R1/ItemStack;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public RefNmsItemStack itemStack;
}
