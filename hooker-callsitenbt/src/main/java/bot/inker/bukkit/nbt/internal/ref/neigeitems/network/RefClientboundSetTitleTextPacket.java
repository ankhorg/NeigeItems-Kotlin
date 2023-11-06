package bot.inker.bukkit.nbt.internal.ref.neigeitems.network;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.chat.RefComponent;

@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/network/protocol/game/ClientboundSetTitleTextPacket")
public final class RefClientboundSetTitleTextPacket implements RefPacket<RefPacketListenerPlayOut> {
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/network/protocol/game/ClientboundSetTitleTextPacket;<init>(Lnet/minecraft/network/chat/Component;)V")
    public RefClientboundSetTitleTextPacket(RefComponent title) {
        throw new UnsupportedOperationException();
    }
}
