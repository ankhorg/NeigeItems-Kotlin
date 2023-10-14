package bot.inker.bukkit.nbt.neigeitems.utils;

import bot.inker.bukkit.nbt.internal.ref.RefItem;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.util.RefCraftMagicNumbers;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MaterialUtils {
    /**
     * 根据物品获取对应材质的翻译键.
     *
     * @param itemStack 待检测物品.
     * @return 对应材质的翻译键.
     */
    @Nullable
    public static String getTranslationKey(
            @NotNull ItemStack itemStack
    ) {
        return getTranslationKey(itemStack.getType());
    }

    /**
     * 根据材质获取对应的翻译键.
     *
     * @param material 待检测材质.
     * @return 对应的翻译键.
     */
    @Nullable
    public static String getTranslationKey(
            @NotNull Material material
    ) {
        RefItem item = RefCraftMagicNumbers.getItem(material);
        if (item == null) return null;
        return item.getDescriptionId();
    }
}
