package pers.neige.neigeitems.ref.network;

import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/network/protocol/game/ClientboundSetTitlesAnimationPacket", predicates = "craftbukkit_version:[v1_17_R1,)")
public final class RefClientboundSetTitlesAnimationPacket implements RefPacket<RefPacketListenerPlayOut> {
    @HandleBy(reference = "Lnet/minecraft/network/protocol/game/ClientboundSetTitlesAnimationPacket;<init>(III)V", predicates = "craftbukkit_version:[v1_17_R1,)")
    public RefClientboundSetTitlesAnimationPacket(int fadeIn, int stay, int fadeOut) {
        throw new UnsupportedOperationException();
    }
}
