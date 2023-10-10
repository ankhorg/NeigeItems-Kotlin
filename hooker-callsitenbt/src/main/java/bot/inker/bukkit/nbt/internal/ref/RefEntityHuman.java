package bot.inker.bukkit.nbt.internal.ref;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;

@HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/EntityHuman")
@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/world/entity/player/Player")
public abstract class RefEntityHuman extends RefEntityLiving {
}
