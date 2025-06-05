package pers.neige.neigeitems.item.builder;

import com.mojang.serialization.DataResult;
import io.papermc.paper.adventure.PaperAdventure;
import kotlin.text.StringsKt;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.component.Unbreakable;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.NeigeItems;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.Nbt;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtCompound;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtUtils;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.ComponentUtils;
import pers.neige.neigeitems.manager.HookerManager;
import pers.neige.neigeitems.utils.ItemUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

public class NewItemBuilder extends ItemBuilder {
    public static final RegistryOps<Tag> registryOps = MinecraftServer.getServer().registryAccess().createSerializationContext(NbtOps.INSTANCE);
    private static final boolean LOWER_THAN_1_21_4 = !CbVersion.v1_21_R4.isSupport();

    private final @NonNull Set<ItemFlag> hideFlag = new HashSet<>();
    private final @NonNull ItemEnchantments.Mutable mutableEnchantments = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
    private @Nullable Component name = null;
    private @Nullable List<Component> lore = null;
    private @Nullable ResourceLocation tooltipStyle = null;
    private DataComponentPatch components = null;

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

    public static <T> void loadComponent(
            @NonNull DataComponentPatch.Builder builder,
            @NonNull DataComponentType<T> type,
            @NonNull Object value
    ) throws IllegalStateException {
        Tag tag = (Tag) Nbt.Unsafe.getDelegate(ItemUtils.toNbt(value));
        DataResult<T> result = type.codecOrThrow().parse(registryOps, tag);
        builder.set(type, result.getOrThrow());
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
                                this.mutableEnchantments.set(CraftEnchantment.bukkitToMinecraftHolder(enchant), level);
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
                        Component component = CraftChatMessage.fromStringOrNull(ChatColor.translateAlternateColorCodes('&', rawName));
                        this.name = component == null ? CommonComponents.EMPTY : component;
                    }
                    break;
                }
                case "lore": {
                    List<String> originLore = config.getStringList(key);
                    List<Component> finalLore = new ArrayList<>();
                    for (String rawLore : originLore) {
                        for (String loreText : ChatColor.translateAlternateColorCodes('&', rawLore).split("\n")) {
                            Component component = CraftChatMessage.fromStringOrNull(loreText);
                            finalLore.add(component == null ? CommonComponents.EMPTY : component);
                        }
                    }
                    if (!finalLore.isEmpty()) {
                        this.lore = finalLore;
                    }
                    break;
                }
                case "mini-name": {
                    String rawName = config.getString(key);
                    if (rawName != null) {
                        this.name = PaperAdventure.asVanilla(MiniMessage.miniMessage().deserialize(rawName));
                    }
                    break;
                }
                case "mini-lore": {
                    List<String> originLore = config.getStringList(key);
                    List<Component> finalLore = new ArrayList<>();
                    for (String rawLore : originLore) {
                        for (String loreText : rawLore.split("\n")) {
                            finalLore.add(PaperAdventure.asVanilla(MiniMessage.miniMessage().deserialize(loreText)));
                        }
                    }
                    if (!finalLore.isEmpty()) {
                        this.lore = finalLore;
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
                        for (String flagText : flags) {
                            ItemFlag flag = ItemFlag.valueOf(flagText);
                            this.hideFlag.add(flag);
                        }
                    }
                    break;
                }
                case "tooltip-style": {
                    String rawTooltipStyle = config.getString(key);
                    if (rawTooltipStyle != null) {
                        this.tooltipStyle = ResourceLocation.parse(rawTooltipStyle);
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
                    if (componentsConfig != null) {
                        DataComponentPatch.Builder builder = DataComponentPatch.builder();
                        for (String componentKey : componentsConfig.keySet()) {
                            Object componentValue = componentsConfig.get(componentKey);
                            if (componentValue == null) continue;
                            DataComponentType<?> type = (DataComponentType<?>) ComponentUtils.getDataComponentType(componentKey);
                            if (type == null) {
                                NeigeItems.getInstance().getLogger().warning("Unknown component type: " + componentKey);
                                continue;
                            }
                            try {
                                loadComponent(builder, type, componentValue);
                            } catch (IllegalStateException e) {
                                NeigeItems.getInstance().getLogger().log(Level.WARNING, "Invalid component value: " + componentValue, e);
                            }
                        }
                        components = builder.build();
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
        CraftItemStack result;
        if (this.itemStack != null) {
            result = this.itemStack instanceof CraftItemStack ? (CraftItemStack) this.itemStack : CraftItemStack.asCraftCopy(this.itemStack);
            if (this.material != null) {
                result.setType(this.material);
            }
            if (postItemInit != null) {
                postItemInit.accept(result);
            }
        } else {
            if (this.material != null) {
                result = CraftItemStack.asCraftCopy(new ItemStack(this.material));
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
        result.lore();
        net.minecraft.world.item.ItemStack handle = result.handle;
        if (this.damage != null) {
            handle.setDamageValue(damage);
        }
        if (!this.enchantments.isEmpty()) {
            if (LOWER_THAN_1_21_4) {
                mutableEnchantments.showInTooltip = !this.hideFlag.contains(ItemFlag.HIDE_ENCHANTS);
            }
            handle.set(DataComponents.ENCHANTMENTS, mutableEnchantments.toImmutable());
        }
        if (customModelData != null) {
            if (LOWER_THAN_1_21_4) {
                handle.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(customModelData));
            } else {
                NeigeItems.getInstance().getLogger().warning("1.21.4+的版本请使用components配置项通过组件配置CustomModelData");
            }
        }
        if (name != null) {
            handle.set(DataComponents.CUSTOM_NAME, name);
        }
        if (lore != null) {
            handle.set(DataComponents.LORE, new ItemLore(lore, new ArrayList<>()));
        }
        if (color != null) {
            if (LOWER_THAN_1_21_4) {
                handle.set(DataComponents.DYED_COLOR, new DyedItemColor(color, !this.hideFlag.contains(ItemFlag.HIDE_DYE)));
            } else {
                NeigeItems.getInstance().getLogger().warning("1.21.4+的版本请使用components配置项通过组件配置皮革颜色");
            }
        }
        if (Boolean.TRUE.equals(unbreakable)) {
            if (LOWER_THAN_1_21_4) {
                handle.set(DataComponents.UNBREAKABLE, new Unbreakable(!this.hideFlag.contains(ItemFlag.HIDE_UNBREAKABLE)));
            } else {
                NeigeItems.getInstance().getLogger().warning("1.21.4+的版本请使用components配置项通过组件配置无法破坏");
            }
        }
        if (tooltipStyle != null) {
            handle.set(DataComponents.TOOLTIP_STYLE, tooltipStyle);
        }
        if (components != null) {
            handle.applyComponents(components);
        }
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
