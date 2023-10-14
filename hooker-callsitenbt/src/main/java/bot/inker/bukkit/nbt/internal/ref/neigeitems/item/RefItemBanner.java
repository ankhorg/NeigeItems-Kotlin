package bot.inker.bukkit.nbt.internal.ref.neigeitems.item;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;
import bot.inker.bukkit.nbt.internal.ref.RefItem;
import bot.inker.bukkit.nbt.internal.ref.RefNmsItemStack;

@HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/ItemBanner")
@HandleBy(version = CbVersion.v1_17_R1, reference = "net/minecraft/world/item/BannerItem")
public final class RefItemBanner extends RefItem {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/ItemBanner;c(Lnet/minecraft/server/v1_12_R1/ItemStack;)Lnet/minecraft/server/v1_12_R1/EnumColor;")
    @HandleBy(version = CbVersion.v1_13_R1, reference = "")
    public static native RefEnumColor getColor(RefNmsItemStack itemStack);
}
