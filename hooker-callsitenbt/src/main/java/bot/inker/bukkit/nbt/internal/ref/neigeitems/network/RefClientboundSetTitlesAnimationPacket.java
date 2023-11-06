package bot.inker.bukkit.nbt.internal.ref.neigeitems.network;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;

@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/network/protocol/game/ClientboundSetTitlesAnimationPacket")
public final class RefClientboundSetTitlesAnimationPacket implements RefPacket<RefPacketListenerPlayOut> {
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/network/protocol/game/ClientboundSetTitlesAnimationPacket;<init>(III)V")
    public RefClientboundSetTitlesAnimationPacket(int fadeIn, int stay, int fadeOut) {
        throw new UnsupportedOperationException();
    }
}
