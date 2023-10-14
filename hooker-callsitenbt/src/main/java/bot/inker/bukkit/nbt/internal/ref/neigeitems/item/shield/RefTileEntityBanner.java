package bot.inker.bukkit.nbt.internal.ref.neigeitems.item.shield;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.annotation.HandleBy;
import bot.inker.bukkit.nbt.internal.ref.RefNmsItemStack;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.item.RefEnumColor;

@HandleBy(version = CbVersion.v1_12_R1, reference = "net/minecraft/server/v1_12_R1/TileEntityBanner")
@HandleBy(version = CbVersion.v1_13_R1, reference = "")
public final class RefTileEntityBanner {
    @HandleBy(version = CbVersion.v1_12_R1, reference = "Lnet/minecraft/server/v1_12_R1/TileEntityBanner;d(Lnet/minecraft/server/v1_12_R1/ItemStack;)Lnet/minecraft/server/v1_12_R1/EnumColor;")
    @HandleBy(version = CbVersion.v1_13_R1, reference = "")
    public static native RefEnumColor getColor(RefNmsItemStack itemStack);
}
