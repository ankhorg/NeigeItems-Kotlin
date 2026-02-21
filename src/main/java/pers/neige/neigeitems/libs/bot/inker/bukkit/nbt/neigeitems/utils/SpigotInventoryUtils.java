package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils;

import lombok.NonNull;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.neige.neigeitems.ref.world.inventory.RefContainer;
import pers.neige.neigeitems.ref.world.inventory.RefCraftInventory;

import java.lang.reflect.Field;

public class SpigotInventoryUtils {
    private static final Logger logger = LoggerFactory.getLogger(SpigotInventoryUtils.class.getSimpleName());

    private final static Class<?> MINECRAFT_INVENTORY_CLASS;
    private final static Field TITLE_FIELD;

    static {
        MINECRAFT_INVENTORY_CLASS = ((RefCraftInventory) Bukkit.createInventory(null, 9)).getInventory().getClass();
        try {
            TITLE_FIELD = MINECRAFT_INVENTORY_CLASS.getDeclaredField("title");
            TITLE_FIELD.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 为自定义容器设置标题.
     *
     * @param inventory 待设置容器
     * @param title     待设置标题
     */
    public static void setTitle(
        @NonNull Inventory inventory,
        @NonNull String title
    ) throws IllegalAccessException {
        if (inventory instanceof RefCraftInventory) {
            RefContainer container = ((RefCraftInventory) inventory).getInventory();
            if (MINECRAFT_INVENTORY_CLASS.isAssignableFrom(container.getClass())) {
                TITLE_FIELD.set(container, title);
            } else {
                logger.warn("InventoryUtils#setTitle 方法仅支持转换自定义容器(MinecraftInventory)! 当前容器全限定类名: {}", container.getClass().getCanonicalName());
            }
        } else {
            logger.warn("InventoryUtils#setTitle 方法仅支持转换 CraftInventory! 当前容器全限定类名: {}", inventory.getClass().getCanonicalName());
        }
    }

    /**
     * 为自定义容器设置标题.
     *
     * @param inventory 待设置容器
     * @param title     JSON格式待设置标题
     */
    public static void setJsonTitle(
        @NonNull Inventory inventory,
        @NonNull String title
    ) throws IllegalAccessException {
        if (inventory instanceof RefCraftInventory) {
            RefContainer container = ((RefCraftInventory) inventory).getInventory();
            if (MINECRAFT_INVENTORY_CLASS.isAssignableFrom(container.getClass())) {
                TITLE_FIELD.set(container, BaseComponent.toLegacyText(ComponentSerializer.parse(title)));
            } else {
                logger.warn("InventoryUtils#setTitle 方法仅支持转换自定义容器(MinecraftInventory)! 当前容器全限定类名: {}", container.getClass().getCanonicalName());
            }
        } else {
            logger.warn("InventoryUtils#setTitle 方法仅支持转换 CraftInventory! 当前容器全限定类名: {}", inventory.getClass().getCanonicalName());
        }
    }

    /**
     * 为自定义容器设置标题.
     *
     * @param inventory 待设置容器
     * @param title     待设置标题
     */
    public static void setTitle(
        @NonNull Inventory inventory,
        @NonNull BaseComponent title
    ) throws IllegalAccessException {
        if (inventory instanceof RefCraftInventory) {
            RefContainer container = ((RefCraftInventory) inventory).getInventory();
            if (MINECRAFT_INVENTORY_CLASS.isAssignableFrom(container.getClass())) {
                TITLE_FIELD.set(container, BaseComponent.toLegacyText(title));
            } else {
                logger.warn("InventoryUtils#setTitle 方法仅支持转换自定义容器(MinecraftInventory)! 当前容器全限定类名: {}", container.getClass().getCanonicalName());
            }
        } else {
            logger.warn("InventoryUtils#setTitle 方法仅支持转换 CraftInventory! 当前容器全限定类名: {}", inventory.getClass().getCanonicalName());
        }
    }
}
