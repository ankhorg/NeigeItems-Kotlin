package pers.neige.neigeitems.ref.network;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.nbt.RefNmsItemStack;

import java.util.List;

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
