package bot.inker.bukkit.nbt.internal.ref;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;

@HandleBy(version = CbVersion.v1_12_R1, reference = "org/bukkit/craftbukkit/v1_12_R1/CraftWorld")
public final class RefCraftWorld {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lorg/bukkit/craftbukkit/v1_12_R1/CraftWorld;getHandle()Lnet/minecraft/server/v1_12_R1/WorldServer;")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lorg/bukkit/craftbukkit/v1_17_R1/CraftWorld;getHandle()Lnet/minecraft/server/level/ServerLevel;")
    public native RefWorldServer getHandle();
}
