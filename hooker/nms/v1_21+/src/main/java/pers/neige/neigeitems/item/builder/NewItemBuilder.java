package pers.neige.neigeitems.item.builder;

import kotlin.text.StringsKt;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.component.Unbreakable;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtCompound;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtUtils;
import pers.neige.neigeitems.utils.ItemUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NewItemBuilder extends ItemBuilder {
    @NotNull
    private final Set<ItemFlag> hideFlag = new HashSet<>();
    @NotNull
    private ItemEnchantments.Mutable mutableEnchantments = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
    @Nullable
    private Component name = null;
    @Nullable
    private List<Component> lore = null;

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
            @Nullable ConfigurationSection config
    ) {
        load(config);
    }

    @Override
    public void load(
            @Nullable ConfigurationSection config
    ) {
        if (config == null) return;
        for (String key : config.getKeys(false)) {
            switch (key.toLowerCase()) {
                case "type":
                case "material": {
                    String materialString = config.getString(key);
                    if (materialString != null) {
                        this.material = Material.matchMaterial(materialString.toUpperCase());
                    }
                    break;
                }
                case "damage": {
                    this.damage = (short) config.getInt(key);
                    break;
                }
                case "enchantments": {
                    ConfigurationSection enchantSection = config.getConfigurationSection(key);
                    if (enchantSection != null) {
                        for (String enchantId : enchantSection.getKeys(false)) {
                            String uppercaseEnchantId = enchantId.toUpperCase();
                            short level = (short) enchantSection.getInt(enchantId);
                            Enchantment enchant = Enchantment.getByName(uppercaseEnchantId);
                            if (enchant == null) {
                                enchant = this.byFieldName.get(uppercaseEnchantId);
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
                        this.name = Component.literal(ChatColor.translateAlternateColorCodes('&', rawName));
                    }
                    break;
                }
                case "lore": {
                    List<String> originLore = config.getStringList(key);
                    List<Component> finalLore = new ArrayList<>();
                    for (String rawLore : originLore) {
                        for (String loreText : ChatColor.translateAlternateColorCodes('&', rawLore).split("\n")) {
                            finalLore.add(Component.literal(loreText));
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
                case "nbt": {
                    ConfigurationSection nbtConfig = config.getConfigurationSection(key);
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
    @NotNull
    @Override
    public ItemStack build() {
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
        net.minecraft.world.item.ItemStack handle = result.handle;
        if (this.damage != null) {
            handle.setDamageValue(damage);
        }
        if (!this.enchantments.isEmpty()) {
            mutableEnchantments.showInTooltip = !this.hideFlag.contains(ItemFlag.HIDE_ENCHANTS);
            handle.set(DataComponents.ENCHANTMENTS, mutableEnchantments.toImmutable());
        }
        if (customModelData != null) {
            handle.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(customModelData));
        }
        if (name != null) {
            handle.set(DataComponents.CUSTOM_NAME, name);
        }
        if (lore != null) {
            handle.set(DataComponents.LORE, new ItemLore(lore, new ArrayList<>()));
        }
        if (color != null) {
            handle.set(DataComponents.DYED_COLOR, new DyedItemColor(color, !this.hideFlag.contains(ItemFlag.HIDE_DYE)));
        }
        if (Boolean.TRUE.equals(unbreakable)) {
            handle.set(DataComponents.UNBREAKABLE, new Unbreakable(!this.hideFlag.contains(ItemFlag.HIDE_UNBREAKABLE)));
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
