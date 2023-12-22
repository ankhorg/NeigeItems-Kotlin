package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt;

import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.ref.nbt.RefNbtTagCompound;

public class NeigeItemsUtils {
    @NotNull
    public static NbtCompound fromNms(
            @NotNull RefNbtTagCompound nbt
    ) {
        return new NbtCompound(nbt);
    }

    @NotNull
    public static RefNbtTagCompound toNms(
            @NotNull NbtCompound nbt
    ) {
        return nbt.delegate;
    }
}
