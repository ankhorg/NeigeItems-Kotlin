package bot.inker.bukkit.nbt.internal.ref.neigeitems.item.potion;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;

@HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/PotionUtil")
@HandleBy(version = CbVersion.v1_13_R1, reference = "")
public final class RefPotionRegistry {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/PotionRegistry;b(Ljava/lang/String;)Ljava/lang/String;")
    @HandleBy(version = CbVersion.v1_13_R1, reference = "")
    public native String getTranslationKey(String prefix);
}
