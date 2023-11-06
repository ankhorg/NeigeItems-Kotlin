package bot.inker.bukkit.nbt.internal.ref.neigeitems.network;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.chat.RefComponent;

@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/network/protocol/game/ClientboundSetSubtitleTextPacket")
public final class RefClientboundSetSubtitleTextPacket implements RefPacket<RefPacketListenerPlayOut> {
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/network/protocol/game/ClientboundSetSubtitleTextPacket;<init>(Lnet/minecraft/network/chat/Component;)V")
    public RefClientboundSetSubtitleTextPacket(RefComponent title) {
        throw new UnsupportedOperationException();
    }
}
