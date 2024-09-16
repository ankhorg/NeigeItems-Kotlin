package pers.neige.neigeitems.ref.network;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.block.RefBlockPos;
import pers.neige.neigeitems.ref.entity.RefEntity;
import pers.neige.neigeitems.ref.entity.RefEntityType;
import pers.neige.neigeitems.ref.nbt.RefNmsItemStack;
import pers.neige.neigeitems.ref.world.RefVec3;

import java.util.List;
import java.util.UUID;

@HandleBy(reference = "net/minecraft/network/protocol/game/ClientboundContainerSetContentPacket", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/PacketPlayOutWindowItems", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public class RefPacketPlayOutWindowItems implements RefPacket<RefPacketListenerPlayOut> {
    @HandleBy(reference = "Lnet/minecraft/network/protocol/game/ClientboundContainerSetContentPacket;items:Ljava/util/List;", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutWindowItems;b:Ljava/util/List;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public List<RefNmsItemStack> items;
    @HandleBy(reference = "Lnet/minecraft/network/protocol/game/ClientboundContainerSetContentPacket;carriedItem:Lnet/minecraft/world/item/ItemStack;", useAccessor = true, predicates = "craftbukkit_version:[v1_17_R1,)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutSetSlot;c:Lnet/minecraft/server/v1_12_R1/ItemStack;", useAccessor = true, predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public RefNmsItemStack carriedItem;
}
