package pers.neige.neigeitems.ref.item.shield;

import pers.neige.neigeitems.ref.nbt.RefNmsItemStack;
import pers.neige.neigeitems.ref.item.RefEnumColor;
import org.inksnow.ankhinvoke.comments.HandleBy;

@HandleBy(reference = "net/minecraft/server/v1_12_R1/TileEntityBanner", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
public final class RefTileEntityBanner {
    @HandleBy(reference = "Lnet/minecraft/server/v1_12_R1/TileEntityBanner;d(Lnet/minecraft/server/v1_12_R1/ItemStack;)Lnet/minecraft/server/v1_12_R1/EnumColor;", predicates = "craftbukkit_version:[v1_12_R1,v1_13_R1)")
    public static native RefEnumColor getColor(RefNmsItemStack itemStack);
}
