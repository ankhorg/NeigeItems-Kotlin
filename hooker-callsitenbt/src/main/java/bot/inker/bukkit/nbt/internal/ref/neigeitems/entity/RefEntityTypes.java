package bot.inker.bukkit.nbt.internal.ref.neigeitems.entity;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.RefMinecraftKey;

@HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/EntityTypes")
@HandleBy(version = CbVersion.v1_13_R1, reference = "")
public final class RefEntityTypes {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/EntityTypes;a(Lnet/minecraft/server/v1_12_R1/MinecraftKey;)Ljava/lang/String;")
    @HandleBy(version = CbVersion.v1_13_R1, reference = "")
    public static native String getTranslationKey(RefMinecraftKey key);
}
