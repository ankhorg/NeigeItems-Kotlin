package bot.inker.bukkit.nbt;

import bot.inker.bukkit.nbt.internal.ref.RefNbtTagCompound;
import org.jetbrains.annotations.NotNull;

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
