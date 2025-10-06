package pers.neige.neigeitems.item.builder;

import kotlin.text.StringsKt;
import lombok.NonNull;
import lombok.val;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.*;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.EnchantmentUtils;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.TranslationUtils;
import pers.neige.neigeitems.manager.HookerManager;
import pers.neige.neigeitems.utils.ItemUtils;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ItemBuilder {
    public static final ItemStack air = new ItemStack(Material.AIR);
    public static final Map<String, Enchantment> byFieldName = new HashMap<>();

    static {
        val fields = Enchantment.class.getDeclaredFields();
        for (val field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) continue;
            if (!Enchantment.class.isAssignableFrom(field.getType())) continue;
            try {
                byFieldName.put(field.getName().toUpperCase(), (Enchantment) field.get(null));
            } catch (IllegalAccessException exception) {
                exception.printStackTrace();
            }
        }
    }

    protected @Nullable Material material = null;
    protected @Nullable ItemStack itemStack = null;
    protected @Nullable Short damage = null;
    protected @NonNull Map<Enchantment, Short> enchantments = new HashMap<>();
    protected @NonNull Map<Short, Short> enchantmentIdToLevel = new HashMap<>();
    protected @NonNull Map<String, Short> enchantmentKeyToLevel = new HashMap<>();
    protected @Nullable Integer customModelData = null;
    protected @Nullable NbtString name = null;
    protected @Nullable NbtList lore = null;
    protected @Nullable Integer color = null;
    protected @Nullable Integer potionColor = null;
    protected @Nullable Boolean unbreakable = null;
    protected @Nullable Integer hideFlag = null;
    protected @Nullable NbtCompound coverNbt = null;
    protected boolean hasBuild = false;
    protected @Nullable Consumer<ItemStack> postItemInit = null;
    protected @Nullable BiConsumer<ItemStack, NbtCompound> preCoverNbt = null;

    public ItemBuilder() {
    }

    public ItemBuilder(
            @Nullable Material material
    ) {
        this.material = material;
    }

    public ItemBuilder(
            @Nullable ItemStack itemStack
    ) {
        this.itemStack = itemStack == null ? null : NbtUtils.asCraftCopy(itemStack);
    }

    public ItemBuilder(
            @Nullable ConfigReader config
    ) {
        load(config);
    }

    public void load(
            @Nullable ConfigurationSection config
    ) {
        if (config == null) return;
        load(ConfigReader.parse(config));
    }

    public void load(
            @Nullable ConfigReader config
    ) {
        if (config == null) return;
        for (val key : config.keySet()) {
            switch (key.toLowerCase()) {
                case "type":
                case "material": {
                    val materialString = config.getString(key);
                    if (materialString != null) {
                        this.material = HookerManager.getMaterial(materialString);
                    }
                    break;
                }
                case "damage": {
                    this.damage = (short) config.getInt(key);
                    break;
                }
                case "enchantments": {
                    val enchantSection = config.getConfig(key);
                    if (enchantSection != null) {
                        for (val enchantId : enchantSection.keySet()) {
                            val uppercaseEnchantId = enchantId.toUpperCase();
                            val level = (short) enchantSection.getInt(enchantId);
                            Enchantment enchant = Enchantment.getByName(uppercaseEnchantId);
                            if (enchant == null) {
                                enchant = byFieldName.get(uppercaseEnchantId);
                            }
                            if (level > 0 && enchant != null) {
                                this.enchantments.put(enchant, level);
                                if (CbVersion.v1_13_R1.isSupport()) {
                                    this.enchantmentKeyToLevel.put(enchant.getKey().toString(), level);
                                } else {
                                    this.enchantmentIdToLevel.put((short) EnchantmentUtils.getId(enchant), level);
                                }
                            }
                        }
                    }
                    break;
                }
                case "custommodeldata":
                case "custom-model-data": {
                    this.customModelData = config.getInt(key);
                    break;
                }
                case "name": {
                    val rawName = config.getString(key);
                    if (rawName != null) {
                        this.name = NbtString.valueOf(TranslationUtils.toJsonText(ChatColor.translateAlternateColorCodes('&', rawName)));
                    }
                    break;
                }
                case "lore": {
                    val originLore = config.getStringList(key);
                    val finalLore = new NbtList();
                    for (val rawLore : originLore) {
                        for (val loreText : ChatColor.translateAlternateColorCodes('&', rawLore).split("\n")) {
                            finalLore.add(NbtString.valueOf(TranslationUtils.toJsonText(loreText)));
                        }
                    }
                    if (!finalLore.isEmpty()) {
                        this.lore = finalLore;
                    }
                    break;
                }
                case "mini-name": {
                    val rawName = config.getString(key);
                    if (rawName != null) {
                        this.name = NbtString.valueOf(GsonComponentSerializer.gson().serialize(MiniMessage.miniMessage().deserialize(rawName)));
                    }
                    break;
                }
                case "mini-lore": {
                    val originLore = config.getStringList(key);
                    val finalLore = new NbtList();
                    for (val rawLore : originLore) {
                        for (val loreText : rawLore.split("\n")) {
                            finalLore.add(NbtString.valueOf(GsonComponentSerializer.gson().serialize(MiniMessage.miniMessage().deserialize(loreText))));
                        }
                    }
                    if (!finalLore.isEmpty()) {
                        this.lore = finalLore;
                    }
                    break;
                }
                case "color": {
                    val value = config.get(key);
                    if (value instanceof String) {
                        color = StringsKt.toIntOrNull((String) value, 16);
                    } else if (value instanceof Integer) {
                        color = Math.min(0xFFFFFF, Math.max(0, (int) value));
                    }
                    break;
                }
                case "potion-color": {
                    val value = config.get(key);
                    if (value instanceof String) {
                        potionColor = StringsKt.toIntOrNull((String) value, 16);
                    } else if (value instanceof Integer) {
                        potionColor = Math.min(0xFFFFFF, Math.max(0, (int) value));
                    }
                    break;
                }
                case "unbreakable": {
                    this.unbreakable = config.getBoolean(key);
                    break;
                }
                case "item-flags":
                case "itemflags":
                case "hide-flags":
                case "hideflags": {
                    val flags = config.getStringList(key);
                    if (!flags.isEmpty()) {
                        this.hideFlag = 0;
                        for (val flagText : flags) {
                            try {
                                val flag = ItemFlag.valueOf(flagText);
                                this.hideFlag |= (1 << flag.ordinal());
                            } catch (IllegalArgumentException exception) {
                                exception.printStackTrace();
                            }
                        }
                    }
                    break;
                }
                case "nbt": {
                    val nbtConfig = config.getConfig(key);
                    if (nbtConfig != null) {
                        this.coverNbt = ItemUtils.toNbtCompound(nbtConfig);
                    }
                    break;
                }
            }
        }
    }

    /**
     * 根据已有内容构建并返回物品.
     * 返回前会将当前ItemBuilder实例的itemStack字段设置为返回值.
     * 构建过程中可能会对已有内容进行修改, 比如清空所有Enchantment.
     * 构建结果永远不为null, 构建失败将返回AIR.
     * 禁止多次调用build方法, 为防止出现问题, 此时将抛出异常.
     *
     * @return 构建产物.
     */
    public @NonNull ItemStack build() {
        if (hasBuild) {
            throw new UnsupportedOperationException("Already build");
        }
        this.hasBuild = true;
        if (this.material == Material.AIR) {
            this.itemStack = air;
            if (postItemInit != null) {
                postItemInit.accept(air.clone());
            }
            return air;
        }
        ItemStack result;
        if (this.itemStack != null) {
            result = this.itemStack;
            if (this.material != null) {
                result.setType(this.material);
            }
            if (postItemInit != null) {
                postItemInit.accept(result);
            }
        } else {
            if (this.material != null) {
                result = NbtUtils.asCraftCopy(new ItemStack(this.material));
                if (postItemInit != null) {
                    postItemInit.accept(result);
                }
            } else {
                this.itemStack = air;
                if (postItemInit != null) {
                    postItemInit.accept(air.clone());
                }
                return air;
            }
        }
        if (result.getType() == Material.AIR) {
            this.itemStack = air;
            return air;
        }
        if (CbVersion.v1_20_R3.isUpTo()) {
            val nbtItemStack = new NbtItemStack(result);
            val nbt = nbtItemStack.getOrCreateTag();
            if (this.damage != null) {
                if (CbVersion.v1_13_R1.isSupport()) {
                    nbt.putInt(NbtUtils.getDamageNbtKeyOrThrow(), damage);
                } else {
                    result.setDurability(damage);
                }
            }
            if (!this.enchantments.isEmpty()) {
                if (CbVersion.v1_13_R1.isSupport()) {
                    val list = nbt.getOrCreateList(NbtUtils.getEnchantmentsNbtKey());

                    for (val tag : list) {
                        if (!(tag instanceof NbtCompound)) continue;
                        val compound = (NbtCompound) tag;
                        val id = compound.getString(NbtUtils.getEnchantmentIdNbtKey());
                        val lvl = enchantmentKeyToLevel.get(id);
                        if (lvl != null) {
                            compound.putShort(NbtUtils.getEnchantmentLvlNbtKey(), lvl);
                            enchantmentKeyToLevel.remove(id);
                        }
                    }

                    enchantmentKeyToLevel.forEach((id, lvl) -> {
                        val tag = new NbtCompound();
                        tag.putString(NbtUtils.getEnchantmentIdNbtKey(), id);
                        tag.putShort(NbtUtils.getEnchantmentLvlNbtKey(), lvl);
                        list.add(tag);
                    });
                } else {
                    val list = nbt.getOrCreateList(NbtUtils.getEnchantmentsNbtKey());

                    for (val tag : list) {
                        if (!(tag instanceof NbtCompound)) continue;
                        val compound = (NbtCompound) tag;
                        val id = compound.getShort(NbtUtils.getEnchantmentIdNbtKey());
                        val lvl = enchantmentIdToLevel.get(id);
                        if (lvl != null) {
                            compound.putShort(NbtUtils.getEnchantmentLvlNbtKey(), lvl);
                            enchantmentIdToLevel.remove(id);
                        }
                    }
                    enchantmentIdToLevel.forEach((id, lvl) -> {
                        val tag = new NbtCompound();
                        tag.putShort(NbtUtils.getEnchantmentIdNbtKey(), id);
                        tag.putShort(NbtUtils.getEnchantmentLvlNbtKey(), lvl);
                        list.add(tag);
                    });
                }
            }
            if (customModelData != null) {
                val nbtKey = NbtUtils.getCustomModelDataNbtKeyOrNull();
                if (nbtKey != null) {
                    nbt.putInt(nbtKey, customModelData);
                }
            }
            if (name != null || lore != null || color != null) {
                val display = nbt.getOrCreateCompound(NbtUtils.getDisplayNbtKey());
                if (name != null) {
                    display.put(NbtUtils.getNameNbtKey(), name);
                }
                if (lore != null) {
                    display.put(NbtUtils.getLoreNbtKey(), lore);
                }
                if (color != null) {
                    display.putInt("color", color);
                }
            }
            if (potionColor != null) {
                nbt.putInt("CustomPotionColor", potionColor);
            }
            if (unbreakable != null) {
                nbt.putBoolean(NbtUtils.getUnbreakableNbtKey(), unbreakable);
            }
            if (hideFlag != null) {
                nbt.putInt(NbtUtils.getHideFlagsNbtKey(), hideFlag);
            }
            if (preCoverNbt != null) {
                preCoverNbt.accept(result, nbt);
            }
            if (coverNbt != null) {
                nbt.coverWith(coverNbt);
            }
            this.itemStack = result;
        }
        return result;
    }

    public @Nullable Material getType() {
        return material;
    }

    public void setType(@Nullable Material material) {
        this.material = material;
    }

    public @Nullable ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(@Nullable ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public @Nullable Short getDamage() {
        return damage;
    }

    public void setDamage(short damage) {
        this.damage = damage;
    }

    public @NonNull Map<Enchantment, Short> getEnchantments() {
        return enchantments;
    }

    public void setEnchantments(@NonNull Map<Enchantment, Short> enchantments) {
        this.enchantments = enchantments;
    }

    public @Nullable Integer getCustomModelData() {
        return customModelData;
    }

    public void setCustomModelData(int customModelData) {
        this.customModelData = customModelData;
    }

    public boolean getUnbreakable() {
        return unbreakable != null && unbreakable;
    }

    public void setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
    }

    public @Nullable Integer getHideFlag() {
        return hideFlag;
    }

    public void setHideFlag(@Nullable Integer hideFlag) {
        this.hideFlag = hideFlag;
    }

    public @Nullable NbtCompound getCoverNbt() {
        return coverNbt;
    }

    public void setCoverNbt(@Nullable NbtCompound coverNbt) {
        this.coverNbt = coverNbt;
    }

    /**
     * build方法内, 根据material生成ItemStack实例或执行ItemStack#setType后执行的动作.
     *
     * @param postItemInit 执行的动作
     */
    public void runPostItemInit(@Nullable Consumer<ItemStack> postItemInit) {
        this.postItemInit = postItemInit;
    }

    /**
     * build方法内, 物品各个属性设置完毕, 将额外NBT覆盖至物品当前NBT前执行的动作.
     *
     * @param preCoverNbt 执行的动作
     */
    public void runPreCoverNbt(@Nullable BiConsumer<ItemStack, NbtCompound> preCoverNbt) {
        this.preCoverNbt = preCoverNbt;
    }
}
