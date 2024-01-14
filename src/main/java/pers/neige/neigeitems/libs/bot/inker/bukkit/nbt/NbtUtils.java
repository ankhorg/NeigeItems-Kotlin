package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.ref.nbt.*;

import java.util.Map;

public class NbtUtils {
    /**
     * 获取物品NBT, 如果物品没有NBT就创建一个空NBT, 设置并返回.
     *
     * @param itemStack 待获取NBT的物品.
     * @return 物品NBT.
     */
    public static @NotNull RefNbtTagCompound getOrCreateTag(@NotNull RefNmsItemStack itemStack) {
        if (itemStack.getTag() == null) {
            itemStack.setTag(new RefNbtTagCompound());
        }
        return itemStack.getTag();
    }

    /**
     * 使用给定的 RefNbtTagCompound 覆盖当前 RefNbtTagCompound.
     *
     * @param baseCompound    被覆盖的基础 RefNbtTagCompound.
     * @param overlayCompound 用于提供覆盖值的 RefNbtTagCompound.
     * @return baseCompound.
     */
    public static @NotNull RefNbtTagCompound coverWith(@NotNull RefNbtTagCompound baseCompound, @NotNull RefNbtTagCompound overlayCompound) {
        // 遍历附加NBT
        overlayCompound.tags.forEach((key, value) -> {
            // 如果二者包含相同键
            RefNbtBase overrideValue = baseCompound.tags.get(key);
            if (overrideValue != null) {
                // 如果二者均为COMPOUND
                if (overrideValue instanceof RefNbtTagCompound && value instanceof RefNbtTagCompound) {
                    // 合并
                    baseCompound.tags.put(key, coverWith((RefNbtTagCompound) overrideValue, (RefNbtTagCompound) value));
                    // 类型不一致
                } else {
                    // 覆盖
                    baseCompound.tags.put(key, value);
                }
                // 这个键原NBT里没有
            } else {
                // 添加
                baseCompound.tags.put(key, value);
            }
        });
        return baseCompound;
    }

    /**
     * 检测给定的 ItemStack 实例是否属于 CraftItemStack 子类.
     *
     * @param itemStack 待检测物品.
     * @return 检测结果.
     */
    public static boolean isCraftItemStack(@NotNull ItemStack itemStack) {
        return (Object) itemStack instanceof RefCraftItemStack;
    }

    /**
     * 通过给定的 ItemStack 实例获取 ItemStack.
     * org.bukkit.inventory.ItemStack 将返回 ItemMeta 原本
     * org.bukkit.craftbukkit.xxx.inventory.CraftItemStack 将返回 ItemMeta 副本
     *
     * @param itemStack 待获取物品.
     * @return ItemMeta.
     */
    @Nullable
    public static ItemMeta getItemMeta(@NotNull ItemStack itemStack) {
        if ((Object) itemStack instanceof RefCraftItemStack) {
            return itemStack.getItemMeta();
        } else {
            return ((RefBukkitItemStack) (Object) itemStack).meta;
        }
    }

    /**
     * 仅可用于 org.bukkit.inventory.ItemStack, 不可用于 CraftItemStack.
     * 获取给定物品的克隆, 返回值为 ItemStack.
     * 修复了 org.bukkit.inventory.ItemStack#clone 在克隆 CraftMetaItem 时对 unhandledTags 浅复制的问题.
     *
     * @param itemStack 待操作物品.
     * @return 物品克隆.
     */
    public static ItemStack bukkitCopy(@NotNull ItemStack itemStack) {
        ItemStack result = itemStack.clone();
        RefCraftMetaItem refItemMeta = (RefCraftMetaItem) (Object) ((RefBukkitItemStack) (Object) result).meta;
        if (refItemMeta != null) {
            try {
                // paper用的TreeMap, spigot用的HashMap
                Map<String, RefNbtBase> unhandledTags = refItemMeta.unhandledTags.getClass().newInstance();
                refItemMeta.unhandledTags.forEach((key, value) -> unhandledTags.put(key, value.rClone()));
                refItemMeta.unhandledTags = unhandledTags;
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 获取给定物品的克隆, 返回值有可能为 ItemStack 或 CraftItemStack.
     * 修复了 org.bukkit.inventory.ItemStack#clone 在克隆 CraftMetaItem 时对 unhandledTags 浅复制的问题.
     *
     * @param itemStack 待操作物品.
     * @return 物品克隆.
     */
    public static ItemStack asCopy(@NotNull ItemStack itemStack) {
        if ((Object) itemStack instanceof RefCraftItemStack) {
            return itemStack.clone();
        } else {
            return bukkitCopy(itemStack);
        }
    }

    /**
     * 将给定的物品转换为 org.bukkit.inventory.ItemStack 形式的克隆.
     * 给定物品可能属于 ItemStack, 也可能属于 ItemStack 在 OBC 的子类 CraftItemStack.
     *
     * @param itemStack 待操作物品.
     * @return org.bukkit.inventory.ItemStack 形式的物品克隆.
     */
    public static ItemStack asBukkitCopy(@NotNull ItemStack itemStack) {
        if ((Object) itemStack instanceof RefCraftItemStack) {
            return RefCraftItemStack.asBukkitCopy(((RefCraftItemStack) (Object) itemStack).handle);
        } else {
            return bukkitCopy(itemStack);
        }
    }

    /**
     * 将给定的物品转换为 CraftItemStack 形式的克隆.
     * 给定物品可能属于 ItemStack, 也可能属于 ItemStack 在 OBC 的子类 CraftItemStack.
     *
     * @param itemStack 待操作物品.
     * @return CraftItemStack 形式的物品克隆.
     */
    public static ItemStack asCraftCopy(@NotNull ItemStack itemStack) {
        if ((Object) itemStack instanceof RefCraftItemStack) {
            return itemStack.clone();
        } else {
            return (ItemStack) (Object) RefCraftItemStack.asCraftCopy(itemStack);
        }
    }
}
