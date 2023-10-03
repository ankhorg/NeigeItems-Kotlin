package bot.inker.bukkit.nbt.internal.ref;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;

@HandleBy(version = CbVersion.v1_12_R1, reference = "org/bukkit/craftbukkit/v1_12_R1/CraftServer")
public final class RefCraftServer {
    @HandleBy(version = CbVersion.v1_13_R1, reference = "Lorg/bukkit/craftbukkit/v1_13_R2/CraftServer;syncCommands()V")
    public native void syncCommands();
}
