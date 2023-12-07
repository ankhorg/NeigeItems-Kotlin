package pers.neige.neigeitems.internal.ref.item.potion;

import org.inksnow.ankhinvoke.comments.HandleBy;
import bot.inker.bukkit.nbt.internal.ref.RefNmsItemStack;

@HandleBy(reference = "net/minecraft/server/v1_12_R1/PotionUtil", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
public final class RefPotionUtil {
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/PotionUtil;d(Lnet/minecraft/server/v1_12_R1/ItemStack;)Lnet/minecraft/server/v1_12_R1/PotionRegistry;", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public static native RefPotionRegistry getPotionRegistry(RefNmsItemStack itemStack);
}
