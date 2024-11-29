package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.invoke.InvokeUtil;
import pers.neige.neigeitems.ref.nbt.*;

import java.io.*;
import java.util.Map;

public class NbtUtils {
    /**
     * 1.16.2+ 版本起, NbtIo#readCompressed 及 NbtIo#writeCompressed 方法支持使用 File 作为参数.
     */
    private static final boolean READ_COMPRESSED_FROM_FILE_SUPPORT = CbVersion.v1_16_R2.isSupport();
    /**
     * 1.17.1+ 版本起, NbtIo#read 及 NbtIo#write 方法支持使用 File 作为参数.
     */
    private static final boolean READ_FROM_FILE_SUPPORT = CbVersion.v1_17_R1.isSupport();
    /**
     * 1.16.2+ 版本起, NbtIo#read 方法参数由 DataInputStream 改为 DataInput.
     */
    private static final boolean READ_FROM_DATA_INPUT_SUPPORT = CbVersion.v1_17_R1.isSupport();

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
        return itemStack instanceof RefCraftItemStack;
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
        if (itemStack instanceof RefCraftItemStack) {
            return itemStack.getItemMeta();
        } else {
            return InvokeUtil.getItemMeta(itemStack);
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
    @NotNull
    @SuppressWarnings("unchecked")
    public static ItemStack bukkitCopy(@NotNull ItemStack itemStack) {
        ItemStack result = itemStack.clone();
        RefCraftMetaItem refItemMeta = (RefCraftMetaItem) (Object) InvokeUtil.getItemMeta(itemStack);
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
    @NotNull
    public static ItemStack asCopy(@NotNull ItemStack itemStack) {
        if (itemStack instanceof RefCraftItemStack) {
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
    @NotNull
    public static ItemStack asBukkitCopy(@NotNull ItemStack itemStack) {
        if (itemStack instanceof RefCraftItemStack) {
            return RefCraftItemStack.asBukkitCopy(((RefCraftItemStack) itemStack).handle);
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
    @NotNull
    public static ItemStack asCraftCopy(@NotNull ItemStack itemStack) {
        if (itemStack instanceof RefCraftItemStack) {
            return itemStack.clone();
        } else {
            return RefCraftItemStack.asCraftCopy(itemStack);
        }
    }

    /**
     * 从压缩文件中读取 NbtCompound.
     */
    public static NbtCompound readCompressed(File file) throws IOException {
        if (READ_COMPRESSED_FROM_FILE_SUPPORT) {
            return new NbtCompound(RefNbtIo.readCompressed(file));
        } else {
            try (FileInputStream stream = new FileInputStream(file)) {
                return new NbtCompound(RefNbtIo.readCompressed(stream));
            }
        }
    }

    /**
     * 从压缩文件中读取 NbtCompound.
     */
    public static NbtCompound readCompressed(InputStream stream) throws IOException {
        return new NbtCompound(RefNbtIo.readCompressed(stream));
    }

    /**
     * 向压缩文件写入 NbtCompound.
     */
    public static void writeCompressed(NbtCompound compound, File file) throws IOException {
        if (READ_COMPRESSED_FROM_FILE_SUPPORT) {
            RefNbtIo.writeCompressed(compound.delegate, file);
        } else {
            try (FileOutputStream stream = new FileOutputStream(file)) {
                RefNbtIo.writeCompressed(compound.delegate, stream);
            }
        }
    }

    /**
     * 向压缩文件写入 NbtCompound.
     */
    public static void writeCompressed(NbtCompound compound, OutputStream stream) throws IOException {
        RefNbtIo.writeCompressed(compound.delegate, stream);
    }

    /**
     * 从未压缩文件中读取 NbtCompound.
     */
    public static NbtCompound read(File file) throws IOException {
        if (READ_COMPRESSED_FROM_FILE_SUPPORT) {
            return new NbtCompound(RefNbtIo.read(file));
        } else {
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                try (DataInputStream dataInputStream = new DataInputStream(fileInputStream)) {
                    if (READ_FROM_DATA_INPUT_SUPPORT) {
                        return new NbtCompound(RefNbtIo.read((DataInput) dataInputStream));
                    } else {
                        return new NbtCompound(RefNbtIo.read(dataInputStream));
                    }
                }
            }
        }
    }

    /**
     * 从未压缩文件中读取 NbtCompound.
     */
    public static NbtCompound read(DataInputStream stream) throws IOException {
        if (READ_FROM_DATA_INPUT_SUPPORT) {
            return new NbtCompound(RefNbtIo.read((DataInput) stream));
        } else {
            return new NbtCompound(RefNbtIo.read(stream));
        }
    }

    /**
     * 向未压缩文件写入 NbtCompound.
     */
    public static void write(NbtCompound compound, File file) throws IOException {
        if (READ_FROM_FILE_SUPPORT) {
            RefNbtIo.write(compound.delegate, file);
        } else {
            try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                try (DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream)) {
                    RefNbtIo.write(compound.delegate, dataOutputStream);
                }
            }
        }
    }

    /**
     * 向未压缩文件写入 NbtCompound.
     */
    public static void write(NbtCompound compound, DataOutput output) throws IOException {
        RefNbtIo.write(compound.delegate, output);
    }

    /**
     * 把物品保存成 NbtCompound.
     */
    @NotNull
    public static NbtCompound save(
            @Nullable ItemStack itemStack
    ) {
        RefNbtTagCompound nmsNbt = new RefNbtTagCompound();
        RefNmsItemStack nmsItemStack;
        if (itemStack instanceof RefCraftItemStack) {
            nmsItemStack = ((RefCraftItemStack) itemStack).handle;
        } else {
            nmsItemStack = RefCraftItemStack.asNMSCopy(itemStack);
        }
        nmsItemStack.save(nmsNbt);
        return new NbtCompound(nmsNbt);
    }

    /**
     * 根据 NbtCompound 掏一个物品出来.
     */
    @NotNull
    public static ItemStack of(
            @Nullable NbtCompound nbt
    ) {
        if (nbt == null) return new ItemStack(Material.AIR);
        if (CbVersion.v1_13_R1.isSupport()) {
            return RefCraftItemStack.asCraftMirror(RefNmsItemStack.of(nbt.delegate));
        } else {
            return RefCraftItemStack.asCraftMirror(new RefNmsItemStack(nbt.delegate));
        }
    }

    @NotNull
    public static String getNameNbtKey() {
        return RefCraftMetaItem.NAME.NBT;
    }

    @NotNull
    public static String getLocNameNbtKey() {
        return RefCraftMetaItem.LOCNAME.NBT;
    }

    @NotNull
    public static String getDisplayNbtKey() {
        return RefCraftMetaItem.DISPLAY.NBT;
    }

    @NotNull
    public static String getLoreNbtKey() {
        return RefCraftMetaItem.LORE.NBT;
    }

    @Nullable
    public static String getCustomModelDataNbtKeyOrNull() {
        if (CbVersion.v1_14_R1.isSupport()) {
            return RefCraftMetaItem.CUSTOM_MODEL_DATA.NBT;
        } else {
            return null;
        }
    }

    @NotNull
    public static String getCustomModelDataNbtKeyOrThrow() {
        if (CbVersion.v1_14_R1.isSupport()) {
            return RefCraftMetaItem.CUSTOM_MODEL_DATA.NBT;
        } else {
            throw new UnsupportedOperationException("invalid version");
        }
    }

    @NotNull
    public static String getEnchantmentsNbtKey() {
        return RefCraftMetaItem.ENCHANTMENTS.NBT;
    }

    @NotNull
    public static String getEnchantmentIdNbtKey() {
        return RefCraftMetaItem.ENCHANTMENTS_ID.NBT;
    }

    @NotNull
    public static String getEnchantmentLvlNbtKey() {
        return RefCraftMetaItem.ENCHANTMENTS_LVL.NBT;
    }

    @NotNull
    public static String getRepairNbtKey() {
        return RefCraftMetaItem.REPAIR.NBT;
    }

    @NotNull
    public static String getAttributesNbtKey() {
        return RefCraftMetaItem.ATTRIBUTES.NBT;
    }

    @NotNull
    public static String getAttributesIdentifierNbtKey() {
        return RefCraftMetaItem.ATTRIBUTES_IDENTIFIER.NBT;
    }

    @NotNull
    public static String getAttributesNameNbtKey() {
        return RefCraftMetaItem.ATTRIBUTES_NAME.NBT;
    }

    @NotNull
    public static String getAttributesValueNbtKey() {
        return RefCraftMetaItem.ATTRIBUTES_VALUE.NBT;
    }

    @NotNull
    public static String getAttributesTypeNbtKey() {
        return RefCraftMetaItem.ATTRIBUTES_TYPE.NBT;
    }

    @NotNull
    public static String getAttributesUUIDHighNbtKey() {
        return RefCraftMetaItem.ATTRIBUTES_UUID_HIGH.NBT;
    }

    @NotNull
    public static String getAttributesUUIDLowNbtKey() {
        return RefCraftMetaItem.ATTRIBUTES_UUID_LOW.NBT;
    }

    @Nullable
    public static String getAttributesSlotNbtKeyOrNull() {
        if (CbVersion.v1_13_R1.isSupport()) {
            return RefCraftMetaItem.ATTRIBUTES_SLOT.NBT;
        } else {
            return null;
        }
    }

    @NotNull
    public static String getAttributesSlotNbtKeyOrThrow() {
        if (CbVersion.v1_13_R1.isSupport()) {
            return RefCraftMetaItem.ATTRIBUTES_SLOT.NBT;
        } else {
            throw new UnsupportedOperationException("invalid version");
        }
    }

    @NotNull
    public static String getHideFlagsNbtKey() {
        return RefCraftMetaItem.HIDEFLAGS.NBT;
    }

    @NotNull
    public static String getUnbreakableNbtKey() {
        return RefCraftMetaItem.UNBREAKABLE.NBT;
    }

    @Nullable
    public static String getDamageNbtKeyOrNull() {
        if (CbVersion.v1_13_R1.isSupport()) {
            return RefCraftMetaItem.DAMAGE.NBT;
        } else {
            return null;
        }
    }

    @NotNull
    public static String getDamageNbtKeyOrThrow() {
        if (CbVersion.v1_13_R1.isSupport()) {
            return RefCraftMetaItem.DAMAGE.NBT;
        } else {
            throw new UnsupportedOperationException("invalid version");
        }
    }

    @Nullable
    public static String getBlockDataNbtKeyOrNull() {
        if (CbVersion.v1_14_R1.isSupport()) {
            return RefCraftMetaItem.BLOCK_DATA.NBT;
        } else {
            return null;
        }
    }

    @NotNull
    public static String getBlockDataNbtKeyOrThrow() {
        if (CbVersion.v1_14_R1.isSupport()) {
            return RefCraftMetaItem.BLOCK_DATA.NBT;
        } else {
            throw new UnsupportedOperationException("invalid version");
        }
    }

    @Nullable
    public static String getBukkitCustomTagNbtKeyOrNull() {
        if (CbVersion.v1_13_R1.isSupport()) {
            return RefCraftMetaItem.BUKKIT_CUSTOM_TAG.NBT;
        } else {
            return null;
        }
    }

    @NotNull
    public static String getBukkitCustomTagNbtKeyOrThrow() {
        if (CbVersion.v1_13_R1.isSupport()) {
            return RefCraftMetaItem.BUKKIT_CUSTOM_TAG.NBT;
        } else {
            throw new UnsupportedOperationException("invalid version");
        }
    }

    @Nullable
    public static String getCanDestroyNbtKeyOrNull() {
        if (CbVersion.v1_18_R1.isSupport()) {
            return RefCraftMetaItem.CAN_DESTROY.NBT;
        } else {
            return null;
        }
    }

    @NotNull
    public static String getCanDestroyNbtKeyOrThrow() {
        if (CbVersion.v1_18_R1.isSupport()) {
            return RefCraftMetaItem.CAN_DESTROY.NBT;
        } else {
            throw new UnsupportedOperationException("invalid version");
        }
    }

    @Nullable
    public static String getCanPlaceOnNbtKeyOrNull() {
        if (CbVersion.v1_18_R1.isSupport()) {
            return RefCraftMetaItem.CAN_PLACE_ON.NBT;
        } else {
            return null;
        }
    }

    @NotNull
    public static String getCanPlaceOnNbtKeyOrThrow() {
        if (CbVersion.v1_18_R1.isSupport()) {
            return RefCraftMetaItem.CAN_PLACE_ON.NBT;
        } else {
            throw new UnsupportedOperationException("invalid version");
        }
    }
}
