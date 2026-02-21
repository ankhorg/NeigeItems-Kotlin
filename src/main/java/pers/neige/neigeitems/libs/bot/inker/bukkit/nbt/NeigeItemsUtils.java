package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt;

import lombok.NonNull;
import pers.neige.neigeitems.ref.nbt.RefNbtTagCompound;

public class NeigeItemsUtils {
    public static @NonNull NbtCompound fromNms(
        @NonNull RefNbtTagCompound nbt
    ) {
        return new NbtCompound(nbt);
    }

    public static @NonNull RefNbtTagCompound toNms(
        @NonNull NbtCompound nbt
    ) {
        return nbt.delegate;
    }
}
