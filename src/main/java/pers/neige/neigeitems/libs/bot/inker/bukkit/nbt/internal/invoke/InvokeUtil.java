package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.invoke;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pers.neige.neigeitems.JvmHacker;
import pers.neige.neigeitems.ref.nbt.RefCraftItemStack;

import java.lang.invoke.MethodHandle;

public class InvokeUtil {
    public static final MethodHandle getMetaFromItemStack;
    public static final MethodHandle setMetaToItemStack;

    static {
        try {
            getMetaFromItemStack = JvmHacker.lookup().findGetter(ItemStack.class, "meta", ItemMeta.class);
            setMetaToItemStack = JvmHacker.lookup().findSetter(ItemStack.class, "meta", ItemMeta.class);
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
}
