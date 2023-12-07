package pers.neige.neigeitems.ref.network;

import pers.neige.neigeitems.ref.chat.RefComponent;
import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/network/protocol/game/ClientboundSetTitleTextPacket", predicates = "craftbukkit_version:[v1_17_R1,)")
public final class RefClientboundSetTitleTextPacket implements RefPacket<RefPacketListenerPlayOut> {
    @HandleBy(reference = "Lnet/minecraft/network/protocol/game/ClientboundSetTitleTextPacket;<init>(Lnet/minecraft/network/chat/Component;)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    public RefClientboundSetTitleTextPacket(RefComponent title) {
        throw new UnsupportedOperationException();
    }
}
