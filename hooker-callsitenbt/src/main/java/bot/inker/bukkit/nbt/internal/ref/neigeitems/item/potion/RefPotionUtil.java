package bot.inker.bukkit.nbt.internal.ref.neigeitems.item.potion;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;
import bot.inker.bukkit.nbt.internal.ref.RefNmsItemStack;

@HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/PotionUtil")
@HandleBy(version = CbVersion.v1_13_R1, reference = "")
public final class RefPotionUtil {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/PotionUtil;d(Lnet/minecraft/server/v1_12_R1/ItemStack;)Lnet/minecraft/server/v1_12_R1/PotionRegistry;")
    @HandleBy(version = CbVersion.v1_13_R1, reference = "")
    public static native RefPotionRegistry getPotionRegistry(RefNmsItemStack itemStack);
}
