package pers.neige.neigeitems.internal.ref.item;

import org.inksnow.ankhinvoke.comments.HandleBy;
import bot.inker.bukkit.nbt.internal.ref.RefItem;
import bot.inker.bukkit.nbt.internal.ref.RefNmsItemStack;

@HandleBy(reference = "net/minecraft/world/item/BannerItem", predicates = "craftbukkit_version:[v1_17_R1,)")
@HandleBy(reference = "net/minecraft/server/v1_12_R1/ItemBanner", predicates = "craftbukkit_version:[v1_12_R1,)")
public final class RefItemBanner extends RefItem {
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/ItemBanner;c(Lnet/minecraft/server/v1_12_R1/ItemStack;)Lnet/minecraft/server/v1_12_R1/EnumColor;", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public static native RefEnumColor getColor(RefNmsItemStack itemStack);
}
