package pers.neige.neigeitems.ref.network;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.nbt.RefNmsItemStack;

@HandleBy(reference = "net/minecraft/network/protocol/game/ClientboundSetCursorItemPacket", predicates = "craftbukkit_version:[v1_21_R2,)")
public class RefClientboundSetCursorItemPacket implements RefPacket<RefPacketListenerPlayOut> {
    @HandleBy(reference = "Lnet/minecraft/network/protocol/game/ClientboundSetCursorItemPacket;contents()Lnet/minecraft/world/item/ItemStack;", useAccessor = true, predicates = "craftbukkit_version:[v1_21_R2,)")
    public native RefNmsItemStack contents();
}
