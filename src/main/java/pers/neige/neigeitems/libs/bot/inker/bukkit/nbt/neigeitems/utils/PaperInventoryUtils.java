package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.neige.neigeitems.NeigeItems;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import pers.neige.neigeitems.ref.world.inventory.RefContainer;
import pers.neige.neigeitems.ref.world.inventory.RefCraftInventory;

import java.lang.reflect.Field;

public class PaperInventoryUtils {
    private static final Logger logger = LoggerFactory.getLogger(PaperInventoryUtils.class);

    private final static Class<?> MINECRAFT_INVENTORY_CLASS;
    private final static Class<?> PAPER_CUSTOM_INVENTORY_CLASS;
    private final static Field MINECRAFT_INVENTORY_TITLE_FIELD;
    private final static Field MINECRAFT_INVENTORY_ADVENTURE_TITLE_FIELD;
    private final static Field PAPER_CUSTOM_INVENTORY_TITLE_FIELD;
    private final static Field PAPER_CUSTOM_INVENTORY_ADVENTURE_TITLE_FIELD;

    static {
        InventoryHolder tempHolder = new InventoryHolder() {
            @NotNull
            @Override
            public Inventory getInventory() {
                return Bukkit.createInventory(null, 9);
            }
        };
        MINECRAFT_INVENTORY_CLASS = ((RefCraftInventory) Bukkit.createInventory(tempHolder, 9)).getInventory().getClass();
        PAPER_CUSTOM_INVENTORY_CLASS = ((RefCraftInventory) Bukkit.createInventory(tempHolder, InventoryType.HOPPER)).getInventory().getClass();
        if (CbVersion.v1_16_R3.isSupport() && ServerUtils.isSupportAdventure() && ServerUtils.isPaper()) {
            try {
                MINECRAFT_INVENTORY_TITLE_FIELD = MINECRAFT_INVENTORY_CLASS.getDeclaredField("title");
                MINECRAFT_INVENTORY_TITLE_FIELD.setAccessible(true);
                MINECRAFT_INVENTORY_ADVENTURE_TITLE_FIELD = MINECRAFT_INVENTORY_CLASS.getDeclaredField("adventure$title");
                MINECRAFT_INVENTORY_ADVENTURE_TITLE_FIELD.setAccessible(true);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
            try {
                PAPER_CUSTOM_INVENTORY_TITLE_FIELD = PAPER_CUSTOM_INVENTORY_CLASS.getDeclaredField("title");
                PAPER_CUSTOM_INVENTORY_TITLE_FIELD.setAccessible(true);
                PAPER_CUSTOM_INVENTORY_ADVENTURE_TITLE_FIELD = PAPER_CUSTOM_INVENTORY_CLASS.getDeclaredField("adventure$title");
                PAPER_CUSTOM_INVENTORY_ADVENTURE_TITLE_FIELD.setAccessible(true);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        } else {
            MINECRAFT_INVENTORY_TITLE_FIELD = null;
            MINECRAFT_INVENTORY_ADVENTURE_TITLE_FIELD = null;
            PAPER_CUSTOM_INVENTORY_TITLE_FIELD = null;
            PAPER_CUSTOM_INVENTORY_ADVENTURE_TITLE_FIELD = null;
        }
    }

    /**
     * 为自定义容器设置标题.
     *
     * @param inventory 待设置容器
     * @param title     待设置标题
     */
    public static void setTitle(
            @NotNull Inventory inventory,
            @NotNull String title
    ) throws IllegalAccessException {
        if (inventory instanceof RefCraftInventory) {
            RefContainer container = ((RefCraftInventory) inventory).getInventory();
            if (MINECRAFT_INVENTORY_CLASS.isAssignableFrom(container.getClass())) {
                MINECRAFT_INVENTORY_TITLE_FIELD.set(container, title);
                MINECRAFT_INVENTORY_ADVENTURE_TITLE_FIELD.set(container, LegacyComponentSerializer.legacySection().deserialize(title));
            } else if (PAPER_CUSTOM_INVENTORY_CLASS.isAssignableFrom(container.getClass())) {
                PAPER_CUSTOM_INVENTORY_TITLE_FIELD.set(container, title);
                PAPER_CUSTOM_INVENTORY_ADVENTURE_TITLE_FIELD.set(container, LegacyComponentSerializer.legacySection().deserialize(title));
            } else {
                logger.warn("PaperInventoryUtils#setTitle 方法仅支持转换自定义容器(MinecraftInventory/PaperInventoryCustomHolderContainer)! 当前容器全限定类名: {}", container.getClass().getCanonicalName());
            }
        } else {
            logger.warn("PaperInventoryUtils#setTitle 方法仅支持转换 CraftInventory! 当前容器全限定类名: {}", inventory.getClass().getCanonicalName());
        }
    }

    /**
     * 为自定义容器设置标题.
     *
     * @param inventory 待设置容器
     * @param title     JSON格式待设置标题
     */
    public static void setJsonTitle(
            @NotNull Inventory inventory,
            @NotNull String title
    ) throws IllegalAccessException {
        if (inventory instanceof RefCraftInventory) {
            RefContainer container = ((RefCraftInventory) inventory).getInventory();
            if (MINECRAFT_INVENTORY_CLASS.isAssignableFrom(container.getClass())) {
                MINECRAFT_INVENTORY_TITLE_FIELD.set(container, LegacyComponentSerializer.legacySection().serialize(GsonComponentSerializer.gson().deserialize(title)));
                MINECRAFT_INVENTORY_ADVENTURE_TITLE_FIELD.set(container, GsonComponentSerializer.gson().deserialize(title));
            } else if (PAPER_CUSTOM_INVENTORY_CLASS.isAssignableFrom(container.getClass())) {
                PAPER_CUSTOM_INVENTORY_TITLE_FIELD.set(container, LegacyComponentSerializer.legacySection().serialize(GsonComponentSerializer.gson().deserialize(title)));
                PAPER_CUSTOM_INVENTORY_ADVENTURE_TITLE_FIELD.set(container, GsonComponentSerializer.gson().deserialize(title));
            } else {
                logger.warn("PaperInventoryUtils#setTitle 方法仅支持转换自定义容器(MinecraftInventory/PaperInventoryCustomHolderContainer)! 当前容器全限定类名: {}", container.getClass().getName());
            }
        } else {
            logger.warn("PaperInventoryUtils#setTitle 方法仅支持转换 CraftInventory! 当前容器全限定类名: {}", inventory.getClass().getName());
        }
    }

    /**
     * 为自定义容器设置标题.
     *
     * @param inventory 待设置容器
     * @param title     待设置标题
     */
    public static void setTitle(
            @NotNull Inventory inventory,
            @NotNull Component title
    ) throws IllegalAccessException {
        if (inventory instanceof RefCraftInventory) {
            RefContainer container = ((RefCraftInventory) inventory).getInventory();
            if (MINECRAFT_INVENTORY_CLASS.isAssignableFrom(container.getClass())) {
                MINECRAFT_INVENTORY_TITLE_FIELD.set(container, LegacyComponentSerializer.legacySection().serialize(title));
                MINECRAFT_INVENTORY_ADVENTURE_TITLE_FIELD.set(container, title);
            } else if (PAPER_CUSTOM_INVENTORY_CLASS.isAssignableFrom(container.getClass())) {
                PAPER_CUSTOM_INVENTORY_TITLE_FIELD.set(container, LegacyComponentSerializer.legacySection().serialize(title));
                PAPER_CUSTOM_INVENTORY_ADVENTURE_TITLE_FIELD.set(container, title);
            } else {
                logger.warn("PaperInventoryUtils#setTitle 方法仅支持转换自定义容器(MinecraftInventory/PaperInventoryCustomHolderContainer)! 当前容器全限定类名: {}", container.getClass().getName());
            }
        } else {
            logger.warn("PaperInventoryUtils#setTitle 方法仅支持转换 CraftInventory! 当前容器全限定类名: {}", inventory.getClass().getName());
        }
    }
}
