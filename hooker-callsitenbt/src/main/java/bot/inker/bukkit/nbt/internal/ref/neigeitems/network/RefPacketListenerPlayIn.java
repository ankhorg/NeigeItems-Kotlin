package bot.inker.bukkit.nbt.internal.ref.neigeitems.network;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;

@HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/PacketListenerPlayIn")
@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/network/protocol/game/ServerGamePacketListener")
public interface RefPacketListenerPlayIn extends RefPacketListener {
}
