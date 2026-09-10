package pers.neige.neigeitems.item.builder;

import kotlin.text.StringsKt;
import lombok.NonNull;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.NeigeItems;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.HookerManager;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.Nbt;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtCompound;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtUtils;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.ComponentUtils;
import pers.neige.neigeitems.ref.adventure.RefAdventureComponent;
import pers.neige.neigeitems.ref.chat.RefComponent;
import pers.neige.neigeitems.ref.chat.RefCraftChatMessage;
import pers.neige.neigeitems.ref.core.component.RefDataComponentPatch;
import pers.neige.neigeitems.ref.core.component.RefDataComponentPatch$Builder;
import pers.neige.neigeitems.ref.core.component.RefDataComponentType;
import pers.neige.neigeitems.ref.core.component.RefDataComponents;
import pers.neige.neigeitems.ref.core.component.RefItemLore;
import pers.neige.neigeitems.ref.nbt.RefCraftItemStack;
import pers.neige.neigeitems.ref.nbt.RefNbtBase;
import pers.neige.neigeitems.ref.nbt.RefNmsItemStack;
import pers.neige.neigeitems.ref.serialization.RefCodec;
import pers.neige.neigeitems.ref.serialization.RefDynamicOps;
import pers.neige.neigeitems.ref.util.RefUnit;
import pers.neige.neigeitems.utils.ItemUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class NewItemBuilder extends ItemBuilder {
    private static final boolean LOWER_THAN_1_21_R4 = !CbVersion.v1_21_R4.isSupport();

    private @Nullable RefComponent nameComponent = null;
    private @Nullable RefComponent itemNameComponent = null;
    private @Nullable List<RefComponent> loreComponents = null;
    private @Nullable RefDataComponentPatch components = null;

    public NewItemBuilder() {
    }

    public NewItemBuilder(
        @Nullable Material material
    ) {
        this.material = material;
    }

    public NewItemBuilder(
        @Nullable ItemStack itemStack
    ) {
        this.itemStack = itemStack == null ? null : NbtUtils.asCraftCopy(itemStack);
    }

    public NewItemBuilder(
        @Nullable ConfigReader config
    ) {
        load(config);
    }

    @SuppressWarnings("unchecked")
    private static <T> T decodeComponent(
        @NonNull RefDataComponentType<T> type,
        @NonNull Object value
    ) throws IllegalStateException {
        RefNbtBase tag = (RefNbtBase) Nbt.Unsafe.getDelegate(ItemUtils.toNbt(value));
        RefDynamicOps<RefNbtBase> ops = (RefDynamicOps<RefNbtBase>) NbtUtils.registryOps;
        RefCodec<T> codec = type.codecOrThrow();
        return codec.parse(ops, tag).getOrThrow();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void setComponent(
        @NonNull RefNmsItemStack itemStack,
        @NonNull RefDataComponentType<?> type,
        @Nullable Object value
    ) {
        itemStack.set((RefDataComponentType) type, value);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void setDecodedComponent(
        @NonNull RefNmsItemStack itemStack,
        @NonNull RefDataComponentType<?> type,
        @NonNull Object value
    ) {
        setComponent(itemStack, type, decodeComponent((RefDataComponentType) type, value));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void setPatchComponent(
        @NonNull RefDataComponentPatch$Builder builder,
        @NonNull RefDataComponentType<?> type,
        @NonNull Object value
    ) {
        builder.set((RefDataComponentType) type, decodeComponent((RefDataComponentType) type, value));
    }

    private static RefComponent parseLegacyComponent(
        @NonNull String value
    ) {
        return RefCraftChatMessage.fromString(ChatColor.translateAlternateColorCodes('&', value))[0];
    }

    public static <T> void loadComponent(
        @NonNull RefDataComponentPatch$Builder builder,
        @NonNull RefDataComponentType<T> type,
        @NonNull Object value
    ) throws IllegalStateException {
        builder.set(type, decodeComponent(type, value));
    }

    @Override
    public void load(
        @Nullable ConfigReader config
    ) {
        if (config == null) return;
        for (String key : config.keySet()) {
            switch (key.toLowerCase()) {
                case "type":
                case "material": {
                    String materialString = config.getString(key);
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
                    ConfigReader enchantSection = config.getConfig(key);
                    if (enchantSection != null) {
                        for (String enchantId : enchantSection.keySet()) {
                            String uppercaseEnchantId = enchantId.toUpperCase();
                            short level = (short) enchantSection.getInt(enchantId);
                            Enchantment enchant = Enchantment.getByName(uppercaseEnchantId);
                            if (enchant == null) {
                                enchant = byFieldName.get(uppercaseEnchantId);
                            }
                            if (level > 0 && enchant != null) {
                                this.enchantments.put(enchant, level);
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
                    String rawName = config.getString(key);
                    if (rawName != null) {
                        this.nameComponent = parseLegacyComponent(rawName);
                    }
                    break;
                }
                case "item-name": {
                    String rawName = config.getString(key);
                    if (rawName != null) {
                        this.itemNameComponent = parseLegacyComponent(rawName);
                    }
                    break;
                }
                case "lore": {
                    List<String> originLore = config.getStringList(key);
                    List<RefComponent> finalLore = new ArrayList<>();
                    for (String rawLore : originLore) {
                        for (String loreText : ChatColor.translateAlternateColorCodes('&', rawLore).split("\\n")) {
                            finalLore.add(parseLegacyComponent(loreText));
                        }
                    }
                    if (!finalLore.isEmpty()) {
                        this.loreComponents = finalLore;
                    }
                    break;
                }
                case "mini-name": {
                    String rawName = config.getString(key);
                    if (rawName != null) {
                        this.nameComponent = new RefAdventureComponent(MiniMessage.miniMessage().deserialize(rawName)).deepConverted();
                    }
                    break;
                }
                case "mini-item-name": {
                    String rawName = config.getString(key);
                    if (rawName != null) {
                        this.itemNameComponent = new RefAdventureComponent(MiniMessage.miniMessage().deserialize(rawName)).deepConverted();
                    }
                    break;
                }
                case "mini-lore": {
                    List<String> originLore = config.getStringList(key);
                    List<RefComponent> finalLore = new ArrayList<>();
                    for (String rawLore : originLore) {
                        for (String loreText : rawLore.split("\\n")) {
                            finalLore.add(new RefAdventureComponent(MiniMessage.miniMessage().deserialize(loreText)).deepConverted());
                        }
                    }
                    if (!finalLore.isEmpty()) {
                        this.loreComponents = finalLore;
                    }
                    break;
                }
                case "color": {
                    Object value = config.get(key);
                    if (value instanceof String) {
                        color = StringsKt.toIntOrNull((String) value, 16);
                    } else if (value instanceof Integer) {
                        color = Math.min(0xFFFFFF, Math.max(0, (int) value));
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
                    List<String> flags = config.getStringList(key);
                    if (!flags.isEmpty()) {
                        this.hideFlag = 0;
                        for (String flagText : flags) {
                            try {
                                ItemFlag flag = ItemFlag.valueOf(flagText);
                                this.hideFlag |= (1 << flag.ordinal());
                            } catch (IllegalArgumentException exception) {
                                exception.printStackTrace();
                            }
                        }
                    }
                    break;
                }
                case "nbt": {
                    ConfigReader nbtConfig = config.getConfig(key);
                    if (nbtConfig != null) {
                        this.coverNbt = ItemUtils.toNbtCompound(nbtConfig);
                    }
                    break;
                }
                case "components": {
                    ConfigReader componentsConfig = config.getConfig(key);
                    if (componentsConfig == null) break;
                    RefDataComponentPatch$Builder builder = RefDataComponentPatch.builder();
                    for (String componentKey : componentsConfig.keySet()) {
                        Object componentValue = componentsConfig.get(componentKey);
                        if (componentValue == null) continue;
                        RefDataComponentType<?> type = (RefDataComponentType<?>) ComponentUtils.getDataComponentType(componentKey);
                        if (type == null) {
                            NeigeItems.getInstance().getLogger().warning("Unknown component type: " + componentKey);
                            continue;
                        }
                        try {
                            setPatchComponent(builder, type, componentValue);
                        } catch (IllegalStateException exception) {
                            NeigeItems.getInstance().getLogger().log(Level.WARNING, "Invalid component value: " + componentValue, exception);
                        }
                    }
                    this.components = builder.build();
                    break;
                }
            }
        }
    }

    private Map<String, Object> buildEnchantmentsComponent() {
        Map<String, Object> levels = new LinkedHashMap<>();
        for (Map.Entry<Enchantment, Short> entry : enchantments.entrySet()) {
            levels.put(entry.getKey().getKey().toString(), (int) entry.getValue());
        }
        if (LOWER_THAN_1_21_R4 && NewItemBuilderCompatibility.hasHideFlag(hideFlag == null ? 0 : hideFlag, "HIDE_ENCHANTS")) {
            Map<String, Object> value = new LinkedHashMap<>();
            value.put("levels", levels);
            value.put("show_in_tooltip", false);
            return value;
        }
        return levels;
    }

    private void applyComponents(
        @NonNull RefNmsItemStack itemStack
    ) {
        if (damage != null) {
            itemStack.setDamageValue(damage);
        }
        if (!enchantments.isEmpty()) {
            setDecodedComponent(itemStack, RefDataComponents.ENCHANTMENTS, buildEnchantmentsComponent());
        }
        if (customModelData != null) {
            NewItemBuilderCompatibility.applyCustomModelData(itemStack, customModelData);
        }
        if (nameComponent != null) {
            setComponent(itemStack, RefDataComponents.CUSTOM_NAME, nameComponent);
        }
        if (itemNameComponent != null) {
            setComponent(itemStack, RefDataComponents.ITEM_NAME, itemNameComponent);
        }
        if (loreComponents != null) {
            setComponent(itemStack, RefDataComponents.LORE, new RefItemLore(loreComponents));
        }
        if (color != null) {
            if (LOWER_THAN_1_21_R4) {
                Map<String, Object> value = new LinkedHashMap<>();
                value.put("rgb", color);
                value.put("show_in_tooltip", !NewItemBuilderCompatibility.hasHideFlag(hideFlag == null ? 0 : hideFlag, "HIDE_DYE"));
                setDecodedComponent(itemStack, RefDataComponents.DYED_COLOR, value);
            } else {
                setDecodedComponent(itemStack, RefDataComponents.DYED_COLOR, color);
            }
        }
        if (Boolean.TRUE.equals(unbreakable)) {
            if (LOWER_THAN_1_21_R4) {
                Map<String, Object> value = new LinkedHashMap<>();
                value.put("show_in_tooltip", !NewItemBuilderCompatibility.hasHideFlag(hideFlag == null ? 0 : hideFlag, "HIDE_UNBREAKABLE"));
                setDecodedComponent(itemStack, RefDataComponents.UNBREAKABLE, value);
            } else {
                setComponent(itemStack, RefDataComponents.UNBREAKABLE, RefUnit.INSTANCE);
            }
        }
        NewItemBuilderCompatibility.applyHideFlags(itemStack, hideFlag == null ? 0 : hideFlag);
        if (components != null) {
            itemStack.applyComponents(components);
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
    @Override
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
        RefCraftItemStack result;
        if (this.itemStack != null) {
            result = this.itemStack instanceof RefCraftItemStack
                ? (RefCraftItemStack) this.itemStack
                : RefCraftItemStack.asCraftCopy(this.itemStack);
            if (this.material != null) {
                result.setType(this.material);
            }
            if (postItemInit != null) {
                postItemInit.accept(result);
            }
        } else {
            if (this.material != null) {
                result = RefCraftItemStack.asCraftCopy(new ItemStack(this.material));
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
        applyComponents(result.handle);
        NbtCompound nbt = ItemUtils.getNbt(result);
        if (preCoverNbt != null) {
            preCoverNbt.accept(result, nbt);
        }
        if (coverNbt != null) {
            nbt.coverWith(coverNbt);
        }
        this.itemStack = result;
        return result;
    }
}
