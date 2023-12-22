package pers.neige.neigeitems.ref.network;

import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.ref.chat.RefComponent;
import pers.neige.neigeitems.ref.chat.RefEnumTitleAction;

@HandleBy(reference = "net/minecraft/server/v1_12_R1/PacketPlayOutTitle", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
public final class RefPacketPlayOutTitle implements RefPacket<RefPacketListenerPlayOut> {
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutTitle;<init>(III)V", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public RefPacketPlayOutTitle(int fadeIn, int stay, int fadeOut) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutTitle;<init>(Lnet/minecraft/server/v1_12_R1/PacketPlayOutTitle$EnumTitleAction;Lnet/minecraft/server/v1_12_R1/IChatBaseComponent;)V", predicates = "craftbukkit_version:[v1_12_R1,v1_17_R1)")
    public RefPacketPlayOutTitle(RefEnumTitleAction action, RefComponent title) {
        throw new UnsupportedOperationException();
    }
}
