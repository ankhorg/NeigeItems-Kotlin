package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.invoke;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.inksnow.ankhinvoke.comments.HandleBy;
import pers.neige.neigeitems.JvmHacker;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import pers.neige.neigeitems.ref.entity.RefEntity;
import pers.neige.neigeitems.ref.nbt.RefCraftItemStack;
import pers.neige.neigeitems.ref.nbt.RefCraftMetaItem;
import pers.neige.neigeitems.ref.nbt.RefNbtTagCompound;
import pers.neige.neigeitems.ref.server.level.RefChunkMap;
import pers.neige.neigeitems.ref.server.level.RefServerChunkCache;
import pers.neige.neigeitems.ref.server.level.RefTrackedEntity;
import pers.neige.neigeitems.ref.server.level.RefWorldServer;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;

public class InvokeUtil {
    public static final MethodHandle getMetaFromItemStack;
    public static final MethodHandle setMetaToItemStack;
    public static final MethodHandle getCustomTagFromCraftMetaItem;
    public static final MethodHandle setCustomTagToCraftMetaItem;
    public static final Field chunkMapField;
    public static final MethodHandle constructorOfTrackedEntity;
    public static final MethodHandle getChunkMapFromServerChunkCache;
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
                chunkMapField = RefServerChunkCache.class.getDeclaredField("chunkMap");
                chunkMapField.setAccessible(true);
                constructorOfTrackedEntity = JvmHacker.lookup().findConstructor(RefTrackedEntity.class, MethodType.methodType(void.class, chunkMapField.getType(), RefEntity.class, int.class, int.class, boolean.class));
                getChunkMapFromServerChunkCache = JvmHacker.lookup().findGetter(RefServerChunkCache.class, "chunkMap", chunkMapField.getType());
            } else {
                getMetaFromItemStack = JvmHacker.lookup().findGetter(ItemStack.class, "meta", ItemMeta.class);
                setMetaToItemStack = JvmHacker.lookup().findSetter(ItemStack.class, "meta", ItemMeta.class);
                getCustomTagFromCraftMetaItem = null;
                setCustomTagToCraftMetaItem = null;
                chunkMapField = null;
                constructorOfTrackedEntity = null;
                getChunkMapFromServerChunkCache = null;
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

    public static RefTrackedEntity newTrackedEntity(RefEntity entity, final int range, final int updateInterval, final boolean trackDelta) {
        try {
            return (RefTrackedEntity) constructorOfTrackedEntity.invoke(getChunkMapFromServerChunkCache.invoke(((RefWorldServer) entity.world).getChunkSource()), entity, range, updateInterval, trackDelta);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
