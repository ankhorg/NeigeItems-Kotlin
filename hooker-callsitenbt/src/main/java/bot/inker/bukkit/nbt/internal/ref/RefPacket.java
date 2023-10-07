package bot.inker.bukkit.nbt.internal.ref;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;

@HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/Packet")
@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/network/protocol/Packet")
public interface RefPacket<T extends RefPacketListener> {
}
