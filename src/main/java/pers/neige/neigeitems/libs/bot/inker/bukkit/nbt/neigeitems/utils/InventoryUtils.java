package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils;

import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.annotation.CbVersion;

public class InventoryUtils {
    /**
     * 为自定义容器设置标题.
     * 1.12.2 版本中, 对开启或正在开启的容器使用本方法将发生意想不到的错误, 现在没时间查了, 以后会尝试修复.
     *
     * @param inventory 待设置容器
     * @param title     待设置标题
     */
    public static void setTitle(
            @NotNull Inventory inventory,
            @NotNull String title
    ) throws IllegalAccessException {
        if (CbVersion.v1_16_R3.isSupport() && ServerUtils.isSupportAdventure() && ServerUtils.isPaper()) {
            PaperInventoryUtils.setTitle(inventory, title);
        } else {
            SpigotInventoryUtils.setTitle(inventory, title);
        }
    }

    /**
     * 为自定义容器设置标题.
     * 1.12.2 版本中, 对开启或正在开启的容器使用本方法将发生意想不到的错误, 现在没时间查了, 以后会尝试修复.
     *
     * @param inventory 待设置容器
     * @param title     JSON格式待设置标题
     */
    public static void setJsonTitle(
            @NotNull Inventory inventory,
            @NotNull String title
    ) throws IllegalAccessException {
        if (CbVersion.v1_16_R3.isSupport() && ServerUtils.isSupportAdventure() && ServerUtils.isPaper()) {
            PaperInventoryUtils.setJsonTitle(inventory, title);
        } else {
            SpigotInventoryUtils.setJsonTitle(inventory, title);
        }
    }
}
