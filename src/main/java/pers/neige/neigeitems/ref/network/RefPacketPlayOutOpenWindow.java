package pers.neige.neigeitems.ref.network;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.chat.RefComponent;
import pers.neige.neigeitems.ref.world.inventory.RefMenuType;

@HandleBy(reference = "net/minecraft/network/protocol/game/ClientboundOpenScreenPacket", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/PacketPlayOutOpenWindow", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public class RefPacketPlayOutOpenWindow implements RefPacket<RefPacketListenerPlayOut> {
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutOpenWindow;<init>(ILjava/lang/String;Lnet/minecraft/server/v1_12_R1/IChatBaseComponent;I)V", predicates = "craftbukkit_version:[v1_12_R1,v1_14_R1)")
    public RefPacketPlayOutOpenWindow(int syncId, String type, RefComponent name, int var4) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/network/protocol/game/ClientboundOpenScreenPacket;<init>(ILnet/minecraft/world/inventory/MenuType;Lnet/minecraft/network/chat/Component;)V", predicates = "craftbukkit_version:[v1_17_R1,v1_19_R1)")
    @HandleBy(reference = "Lnet/minecraft/server/v1_14_R1/PacketPlayOutOpenWindow;<init>(ILnet/minecraft/server/v1_14_R1/Containers;Lnet/minecraft/server/v1_14_R1/IChatBaseComponent;)V", predicates = "craftbukkit_version:[v1_14_R1,v1_17_R1)")
    public RefPacketPlayOutOpenWindow(int syncId, RefMenuType type, RefComponent name) {
        throw new UnsupportedOperationException();
    }
}
