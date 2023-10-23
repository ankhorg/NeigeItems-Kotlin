package bot.inker.bukkit.nbt.neigeitems.utils;

import bot.inker.bukkit.nbt.NbtCompound;
import bot.inker.bukkit.nbt.NeigeItemsUtils;
import bot.inker.bukkit.nbt.internal.ref.RefNbtTagCompound;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.entity.RefCraftEntity;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EntityUtils {
    /**
     * 将实体信息保存至 NBT.
     *
     * @param entity 待保存实体.
     * @return 包含实体信息的 NBT.
     */
    @Nullable
    public static NbtCompound save(
            @NotNull Entity entity
    ) {
        if (entity instanceof RefCraftEntity) {
            RefNbtTagCompound nbt = new RefNbtTagCompound();
            ((RefCraftEntity) entity).getHandle().save(nbt);
            return NeigeItemsUtils.fromNms(nbt);
        }
        return null;
    }

    /**
     * 令实体加载 NBT 信息.
     *
     * @param entity 待操作实体.
     * @param nbt 待加载 NBT.
     */
    public static void load(
            @NotNull Entity entity,
            @NotNull NbtCompound nbt
    ) {
        if (entity instanceof RefCraftEntity) {
            ((RefCraftEntity) entity).getHandle().load(NeigeItemsUtils.toNms(nbt));
        }
    }
}
