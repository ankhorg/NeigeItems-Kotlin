package bot.inker.bukkit.nbt.internal.ref.neigeitems.network;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.chat.RefComponent;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.chat.RefEnumTitleAction;

@HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/PacketPlayOutTitle")
@HandleBy(version = CbVersion.v1_17_R1, reference = "")
public final class RefPacketPlayOutTitle implements RefPacket<RefPacketListenerPlayOut> {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutTitle;<init>(III)V")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "")
    public RefPacketPlayOutTitle(int fadeIn, int stay, int fadeOut) {
        throw new UnsupportedOperationException();
    }

    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/PacketPlayOutTitle;<init>(Lnet/minecraft/server/v1_12_R1/PacketPlayOutTitle$EnumTitleAction;Lnet/minecraft/server/v1_12_R1/IChatBaseComponent;)V")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "")
    public RefPacketPlayOutTitle(RefEnumTitleAction action, RefComponent title) {
        throw new UnsupportedOperationException();
    }
}
