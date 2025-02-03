package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.invoke;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pers.neige.neigeitems.JvmHacker;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import pers.neige.neigeitems.ref.nbt.RefCraftItemStack;
import pers.neige.neigeitems.ref.nbt.RefCraftMetaItem;
import pers.neige.neigeitems.ref.nbt.RefNbtTagCompound;

import java.lang.invoke.MethodHandle;

public class InvokeUtil {
    public static final MethodHandle getMetaFromItemStack;
    public static final MethodHandle setMetaToItemStack;
    public static final MethodHandle getCustomTagFromCraftMetaItem;
    public static final MethodHandle setCustomTagToCraftMetaItem;
    /**
     * 1.20.5+ 版本起, Mojang献祭了自己的亲妈, 换来了物品格式的改动.
     */
    private final static boolean MOJANG_MOTHER_DEAD = CbVersion.v1_20_R4.isSupport();

    static {
        try {
            if (MOJANG_MOTHER_DEAD) {
                getMetaFromItemStack = null;
                setMetaToItemStack = null;
                getCustomTagFromCraftMetaItem = JvmHacker.lookup().findGetter(Class.forName("org.bukkit.craftbukkit.inventory.CraftMetaItem"), "customTag", RefNbtTagCompound.class);
                setCustomTagToCraftMetaItem = JvmHacker.lookup().findSetter(Class.forName("org.bukkit.craftbukkit.inventory.CraftMetaItem"), "customTag", RefNbtTagCompound.class);
            } else {
                getMetaFromItemStack = JvmHacker.lookup().findGetter(ItemStack.class, "meta", ItemMeta.class);
                setMetaToItemStack = JvmHacker.lookup().findSetter(ItemStack.class, "meta", ItemMeta.class);
                getCustomTagFromCraftMetaItem = null;
                setCustomTagToCraftMetaItem = null;
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static ItemMeta getItemMeta(ItemStack itemStack) {
        if (itemStack instanceof RefCraftItemStack) return null;
        try {
            return (ItemMeta) getMetaFromItemStack.invoke(itemStack);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void setItemMeta(ItemStack itemStack, ItemMeta meta) {
        if (itemStack instanceof RefCraftItemStack) return;
        try {
            setMetaToItemStack.invoke(itemStack, meta);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static RefNbtTagCompound getCustomTag(RefCraftMetaItem itemMeta) {
        try {
            return (RefNbtTagCompound) getCustomTagFromCraftMetaItem.invoke(itemMeta);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void setCustomTag(ItemMeta itemMeta, RefNbtTagCompound customTag) {
        try {
            setCustomTagToCraftMetaItem.invoke(itemMeta, customTag);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
