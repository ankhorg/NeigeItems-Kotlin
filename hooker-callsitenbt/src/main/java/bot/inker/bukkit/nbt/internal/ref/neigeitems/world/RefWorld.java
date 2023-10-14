package bot.inker.bukkit.nbt.internal.ref.neigeitems.world;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;
import org.spigotmc.SpigotWorldConfig;

@HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/World")
@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/world/level/Level")
public abstract class RefWorld {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/World;spigotConfig:Lorg/spigotmc/SpigotWorldConfig;")
    @HandleBy(version = CbVersion.v1_17_R1, reference = "Lnet/minecraft/world/level/Level;spigotConfig:Lorg/spigotmc/SpigotWorldConfig;")
    public SpigotWorldConfig spigotConfig;
}
