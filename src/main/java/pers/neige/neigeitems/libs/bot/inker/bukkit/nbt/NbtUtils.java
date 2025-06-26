package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt;

import lombok.NonNull;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.item.ItemPlaceholder;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.invoke.InvokeUtil;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.ComponentUtils;
import pers.neige.neigeitems.ref.RefMinecraftKey;
import pers.neige.neigeitems.ref.adventure.RefPaperAdventure;
import pers.neige.neigeitems.ref.chat.RefComponent;
import pers.neige.neigeitems.ref.chat.RefCraftChatMessage;
import pers.neige.neigeitems.ref.core.component.*;
import pers.neige.neigeitems.ref.nbt.*;
import pers.neige.neigeitems.ref.resources.RefRegistryOps;
import pers.neige.neigeitems.ref.server.RefMinecraftServer;
import pers.neige.neigeitems.utils.ItemUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class NbtUtils {
    public static final Object registryOps;
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
     * 1.20.5+ 版本起, Mojang献祭了自己的亲妈, 换来了物品格式的改动.
     */
    private final static boolean MOJANG_MOTHER_DEAD = CbVersion.v1_20_R4.isSupport();

    static {
        if (MOJANG_MOTHER_DEAD) {
            registryOps = RefMinecraftServer.getServer().registryAccess().createSerializationContext(RefNbtOps.INSTANCE);
        } else {
            registryOps = null;
        }
    }

    /**
     * 获取物品NBT, 如果物品没有NBT就创建一个空NBT, 设置并返回.
     *
     * @param itemStack 待获取NBT的物品.
     * @return 物品NBT.
     */
    public static @NonNull RefNbtTagCompound getOrCreateTag(@NonNull RefNmsItemStack itemStack) {
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
    public static @NonNull RefNbtTagCompound coverWith(@NonNull RefNbtTagCompound baseCompound, @NonNull RefNbtTagCompound overlayCompound) {
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
    public static boolean isCraftItemStack(@NonNull ItemStack itemStack) {
        return itemStack instanceof RefCraftItemStack;
    }

    /**
     * 通过给定的 ItemStack 实例获取 ItemStack.
     * org.bukkit.inventory.ItemStack 将返回 ItemMeta 原本.
     * org.bukkit.craftbukkit.xxx.inventory.CraftItemStack 将返回 ItemMeta 副本.
     * 1.21+无论什么都返回副本.
     *
     * @param itemStack 待获取物品.
     * @return ItemMeta.
     */
    public static @Nullable ItemMeta getItemMeta(@NonNull ItemStack itemStack) {
        if (MOJANG_MOTHER_DEAD) return itemStack.getItemMeta();
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
    @SuppressWarnings("unchecked")
    public static @NonNull ItemStack bukkitCopy(@NonNull ItemStack itemStack) {
        if (MOJANG_MOTHER_DEAD) return itemStack.clone();
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
    public static @NonNull ItemStack asCopy(@NonNull ItemStack itemStack) {
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
    public static @NonNull ItemStack asBukkitCopy(@NonNull ItemStack itemStack) {
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
    public static @NonNull ItemStack asCraftCopy(@NonNull ItemStack itemStack) {
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
    public static @NonNull NbtCompound save(
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
    public static @NonNull ItemStack of(
            @Nullable NbtCompound nbt
    ) {
        if (nbt == null) return new ItemStack(Material.AIR);
        if (CbVersion.v1_13_R1.isSupport()) {
            return RefCraftItemStack.asCraftMirror(RefNmsItemStack.of(nbt.delegate));
        } else {
            return RefCraftItemStack.asCraftMirror(new RefNmsItemStack(nbt.delegate));
        }
    }

    public static @NonNull String getNameNbtKey() {
        return RefCraftMetaItem.NAME.NBT;
    }

    public static @NonNull String getLocNameNbtKey() {
        return RefCraftMetaItem.LOCNAME.NBT;
    }

    public static @NonNull String getDisplayNbtKey() {
        return RefCraftMetaItem.DISPLAY.NBT;
    }

    public static @NonNull String getLoreNbtKey() {
        return RefCraftMetaItem.LORE.NBT;
    }

    public static @Nullable String getCustomModelDataNbtKeyOrNull() {
        if (CbVersion.v1_14_R1.isSupport()) {
            return RefCraftMetaItem.CUSTOM_MODEL_DATA.NBT;
        } else {
            return null;
        }
    }

    public static @NonNull String getCustomModelDataNbtKeyOrThrow() {
        if (CbVersion.v1_14_R1.isSupport()) {
            return RefCraftMetaItem.CUSTOM_MODEL_DATA.NBT;
        } else {
            throw new UnsupportedOperationException("invalid version");
        }
    }

    public static @NonNull String getEnchantmentsNbtKey() {
        return RefCraftMetaItem.ENCHANTMENTS.NBT;
    }

    public static @NonNull String getEnchantmentIdNbtKey() {
        return RefCraftMetaItem.ENCHANTMENTS_ID.NBT;
    }

    public static @NonNull String getEnchantmentLvlNbtKey() {
        return RefCraftMetaItem.ENCHANTMENTS_LVL.NBT;
    }

    public static @NonNull String getRepairNbtKey() {
        return RefCraftMetaItem.REPAIR.NBT;
    }

    public static @NonNull String getAttributesNbtKey() {
        return RefCraftMetaItem.ATTRIBUTES.NBT;
    }

    public static @NonNull String getAttributesIdentifierNbtKey() {
        return RefCraftMetaItem.ATTRIBUTES_IDENTIFIER.NBT;
    }

    public static @NonNull String getAttributesNameNbtKey() {
        return RefCraftMetaItem.ATTRIBUTES_NAME.NBT;
    }

    public static @NonNull String getAttributesValueNbtKey() {
        return RefCraftMetaItem.ATTRIBUTES_VALUE.NBT;
    }

    public static @NonNull String getAttributesTypeNbtKey() {
        return RefCraftMetaItem.ATTRIBUTES_TYPE.NBT;
    }

    public static @NonNull String getAttributesUUIDHighNbtKey() {
        return RefCraftMetaItem.ATTRIBUTES_UUID_HIGH.NBT;
    }

    public static @NonNull String getAttributesUUIDLowNbtKey() {
        return RefCraftMetaItem.ATTRIBUTES_UUID_LOW.NBT;
    }

    public static @Nullable String getAttributesSlotNbtKeyOrNull() {
        if (CbVersion.v1_13_R1.isSupport()) {
            return RefCraftMetaItem.ATTRIBUTES_SLOT.NBT;
        } else {
            return null;
        }
    }

    public static @NonNull String getAttributesSlotNbtKeyOrThrow() {
        if (CbVersion.v1_13_R1.isSupport()) {
            return RefCraftMetaItem.ATTRIBUTES_SLOT.NBT;
        } else {
            throw new UnsupportedOperationException("invalid version");
        }
    }

    public static @NonNull String getHideFlagsNbtKey() {
        return RefCraftMetaItem.HIDEFLAGS.NBT;
    }

    public static @NonNull String getUnbreakableNbtKey() {
        return RefCraftMetaItem.UNBREAKABLE.NBT;
    }

    public static @Nullable String getDamageNbtKeyOrNull() {
        if (CbVersion.v1_13_R1.isSupport()) {
            return RefCraftMetaItem.DAMAGE.NBT;
        } else {
            return null;
        }
    }

    public static @NonNull String getDamageNbtKeyOrThrow() {
        if (CbVersion.v1_13_R1.isSupport()) {
            return RefCraftMetaItem.DAMAGE.NBT;
        } else {
            throw new UnsupportedOperationException("invalid version");
        }
    }

    public static @Nullable String getBlockDataNbtKeyOrNull() {
        if (CbVersion.v1_14_R1.isSupport()) {
            return RefCraftMetaItem.BLOCK_DATA.NBT;
        } else {
            return null;
        }
    }

    public static @NonNull String getBlockDataNbtKeyOrThrow() {
        if (CbVersion.v1_14_R1.isSupport()) {
            return RefCraftMetaItem.BLOCK_DATA.NBT;
        } else {
            throw new UnsupportedOperationException("invalid version");
        }
    }

    public static @Nullable String getBukkitCustomTagNbtKeyOrNull() {
        if (CbVersion.v1_13_R1.isSupport()) {
            return RefCraftMetaItem.BUKKIT_CUSTOM_TAG.NBT;
        } else {
            return null;
        }
    }

    public static @NonNull String getBukkitCustomTagNbtKeyOrThrow() {
        if (CbVersion.v1_13_R1.isSupport()) {
            return RefCraftMetaItem.BUKKIT_CUSTOM_TAG.NBT;
        } else {
            throw new UnsupportedOperationException("invalid version");
        }
    }

    public static @Nullable String getCanDestroyNbtKeyOrNull() {
        if (CbVersion.v1_18_R1.isSupport()) {
            return RefCraftMetaItem.CAN_DESTROY.NBT;
        } else {
            return null;
        }
    }

    public static @NonNull String getCanDestroyNbtKeyOrThrow() {
        if (CbVersion.v1_18_R1.isSupport()) {
            return RefCraftMetaItem.CAN_DESTROY.NBT;
        } else {
            throw new UnsupportedOperationException("invalid version");
        }
    }

    public static @Nullable String getCanPlaceOnNbtKeyOrNull() {
        if (CbVersion.v1_18_R1.isSupport()) {
            return RefCraftMetaItem.CAN_PLACE_ON.NBT;
        } else {
            return null;
        }
    }

    public static @NonNull String getCanPlaceOnNbtKeyOrThrow() {
        if (CbVersion.v1_18_R1.isSupport()) {
            return RefCraftMetaItem.CAN_PLACE_ON.NBT;
        } else {
            throw new UnsupportedOperationException("invalid version");
        }
    }

    public static void setComponents(@NonNull ItemStack receiver, @NonNull ItemStack provider) {
        if (!MOJANG_MOTHER_DEAD) return;
        if (!(receiver instanceof RefCraftItemStack)) return;
        if (!(provider instanceof RefCraftItemStack)) return;
        ((RefCraftItemStack) receiver).handle.components = ((RefCraftItemStack) provider).handle.components;
    }

    public static @Nullable ItemStack getCraftDelegate(@NonNull ItemStack itemStack) {
        if (!MOJANG_MOTHER_DEAD) return null;
        return ((RefBukkitItemStack) (Object) itemStack).craftDelegate;
    }

    /**
     * 获取展示用NBT.
     *
     * @param itemStack 待获取物品.
     * @return 展示用NBT
     */
    @SuppressWarnings("unchecked")
    public static @NonNull NbtCompound getDisplayNbt(@NonNull ItemStack itemStack) {
        if (MOJANG_MOTHER_DEAD) {
            RefNmsItemStack nmsItemStack = RefCraftItemStack.asNMSCopy(itemStack);
            RefNbtTagCompound compound = new RefNbtTagCompound();
            for (Map.Entry<RefDataComponentType<?>, Optional<?>> entry : nmsItemStack.getComponentsPatch().entrySet()) {
                RefTypedDataComponent<?> component = RefTypedDataComponent.createUnchecked(entry.getKey(), entry.getValue().orElse(null));
                RefMinecraftKey key = (RefMinecraftKey) ComponentUtils.getKeyByType(component.type());
                compound.set1(key.toString(), component.encodeValue((RefRegistryOps<RefNbtBase>) registryOps).getOrThrow());
            }
            return new NbtCompound(compound);
        } else {
            return new NbtItemStack(itemStack).getOrCreateTag();
        }
    }

    /**
     * 编辑物品的name和lore, 仅适用于1.21+版本.
     *
     * @param itemStack 待操作物品.
     * @param handler   文本处理器.
     */
    public static void editNameAndLoreAfterV21(@NonNull ItemStack itemStack, BiFunction<ItemStack, String, ItemPlaceholder.ParseResult> handler) {
        if (!MOJANG_MOTHER_DEAD) return;
        if (!(itemStack instanceof RefCraftItemStack))
            itemStack = ((RefBukkitItemStack) (Object) itemStack).craftDelegate;
        RefNmsItemStack nmsItemStack = ((RefCraftItemStack) itemStack).handle;
        RefPatchedDataComponentMap components = nmsItemStack.components;
        if (components == null) return;
        RefComponent name = components.get(RefDataComponents.CUSTOM_NAME);
        if (name != null) {
            String json = RefCraftChatMessage.toJSON(name);
            ItemPlaceholder.ParseResult parsed = handler.apply(itemStack, json);
            if (parsed.getChanged()) {
                nmsItemStack.set(RefDataComponents.CUSTOM_NAME, RefCraftChatMessage.fromJSON(parsed.getText()));
            }
        }
        RefItemLore lore = components.get(RefDataComponents.LORE);
        if (lore != null) {
            List<RefComponent> lines = lore.lines();
            boolean edited = false;
            if (!lines.isEmpty()) {
                List<RefComponent> newLines = new ArrayList<>();
                for (RefComponent line : lines) {
                    if (line == null) {
                        newLines.add(null);
                        continue;
                    }
                    String json = RefCraftChatMessage.toJSON(line);
                    ItemPlaceholder.ParseResult parsed = handler.apply(itemStack, json);
                    if (parsed.getChanged()) {
                        newLines.add(RefCraftChatMessage.fromJSON(parsed.getText()));
                        edited = true;
                    } else {
                        newLines.add(line);
                    }
                }
                lines = newLines;
            }
            List<RefComponent> styledLines = lore.styledLines();
            if (!styledLines.isEmpty()) {
                List<RefComponent> newStyledLines = new ArrayList<>();
                for (RefComponent styledLine : styledLines) {
                    if (styledLine == null) {
                        newStyledLines.add(null);
                        continue;
                    }
                    String json = RefCraftChatMessage.toJSON(styledLine);
                    ItemPlaceholder.ParseResult parsed = handler.apply(itemStack, json);
                    if (parsed.getChanged()) {
                        newStyledLines.add(RefCraftChatMessage.fromJSON(parsed.getText()));
                        edited = true;
                    } else {
                        newStyledLines.add(styledLine);
                    }
                }
                styledLines = newStyledLines;
            }
            if (edited) {
                nmsItemStack.set(RefDataComponents.LORE, new RefItemLore(lines, styledLines));
            }
        }
    }

    /**
     * 将物品保存为NI可识别的配置文件, 性能较差, 不建议于性能敏感处使用, 仅适用于1.21+版本.
     *
     * @param itemStack 待转换物品.
     * @return NI可识别的配置文件
     */
    public static @Nullable ConfigurationSection saveAfterV21(@Nullable ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) return null;
        ConfigurationSection result = new YamlConfiguration();
        result.set("material", itemStack.getType().toString());
        NbtCompound nbt = ItemUtils.getNbtOrNull(itemStack);
        if (nbt != null && !nbt.isEmpty()) {
            result.set("nbt", ItemUtils.toStringMap(nbt));
        }
        if (!(itemStack instanceof RefCraftItemStack))
            itemStack = ((RefBukkitItemStack) (Object) itemStack).craftDelegate;
        RefNmsItemStack nmsItemStack = ((RefCraftItemStack) itemStack).handle;
        RefPatchedDataComponentMap components = nmsItemStack.components;
        if (components == null) return result;
        NbtCompound componentsNbt = NbtUtils.getDisplayNbt(itemStack);
        RefComponent name = components.get(RefDataComponents.CUSTOM_NAME);
        if (name != null) {
            result.set("mini-name", MiniMessage.miniMessage().serialize(RefPaperAdventure.asAdventure(name)));
            componentsNbt.remove("minecraft:custom_name");
        }
        RefItemLore lore = components.get(RefDataComponents.LORE);
        if (lore != null) {
            result.set("mini-lore", RefPaperAdventure.asAdventure(lore.lines()).stream().map(MiniMessage.miniMessage()::serialize).collect(Collectors.toList()));
            componentsNbt.remove("minecraft:lore");
        }
        componentsNbt.remove("minecraft:custom_data");
        if (!componentsNbt.isEmpty()) {
            result.set("components", ItemUtils.toStringMap(componentsNbt));
        }
        return result;
    }
}
