package pers.neige.neigeitems.hook.nms.impl;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.component.CustomData;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.hook.nms.NMSHooker;
import pers.neige.neigeitems.item.builder.ItemBuilder;
import pers.neige.neigeitems.item.builder.NewItemBuilder;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtCompound;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtUtils;

import java.lang.reflect.Field;

/**
 * 1.20.4+ 版本, ItemStack 特殊兼容
 */
public class NMSHookerItemStack extends NMSHooker {
    private final Field customTag;

    public NMSHookerItemStack() {
        super();
        try {
            customTag = Class.forName("org.bukkit.craftbukkit.inventory.CraftMetaItem").getDeclaredField("customTag");
        } catch (NoSuchFieldException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        customTag.setAccessible(true);
    }

    @Override
    @NotNull

    public ItemBuilder newItemBuilder() {
        return new NewItemBuilder();
    }

    @Override
    @NotNull
    public ItemBuilder newItemBuilder(
            @Nullable Material material
    ) {
        return new NewItemBuilder(material);
    }

    @Override
    @NotNull
    public ItemBuilder newItemBuilder(
            @Nullable ItemStack itemStack
    ) {
        return new NewItemBuilder(itemStack);
    }

    @Override
    @NotNull
    public ItemBuilder newItemBuilder(
            @Nullable ConfigurationSection config
    ) {
        return new NewItemBuilder(config);
    }

    @Nullable
    public NbtCompound getCustomNbt(@Nullable ItemStack itemStack) {
        if (itemStack != null && itemStack.getType() != Material.AIR) {
            if (itemStack instanceof CraftItemStack) {
                net.minecraft.world.item.ItemStack handle = ((CraftItemStack) itemStack).handle;
                CustomData customData = handle.get(DataComponents.CUSTOM_DATA);
                return customData == null ? null : NbtCompound.createUnsafe(customData.getUnsafe());
            } else {
                try {
                    CompoundTag customTag = (CompoundTag) this.customTag.get(NbtUtils.getItemMeta(itemStack));
                    return NbtCompound.createUnsafe(customTag);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }
        return null;
    }

    @Nullable
    public NbtCompound getOrCreateCustomNbt(@Nullable ItemStack itemStack) {
        if (itemStack != null && itemStack.getType() != Material.AIR) {
            if (itemStack instanceof CraftItemStack) {
                net.minecraft.world.item.ItemStack handle = ((CraftItemStack) itemStack).handle;
                CustomData customData = handle.get(DataComponents.CUSTOM_DATA);
                if (customData == null) {
                    customData = CustomData.of(new CompoundTag());
                    handle.set(DataComponents.CUSTOM_DATA, customData);
                }
                return NbtCompound.createUnsafe(customData.getUnsafe());
            } else {
                try {
                    ItemMeta itemMeta = NbtUtils.getItemMeta(itemStack);
                    CompoundTag customTag = (CompoundTag) this.customTag.get(itemMeta);
                    if (customTag == null) {
                        customTag = new CompoundTag();
                        this.customTag.set(itemMeta, customTag);
                    }
                    return NbtCompound.createUnsafe(customTag);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }
        return null;
    }

    @Nullable
    public String getDisplayNameFromCraftItemStack(@Nullable ItemStack itemStack) {
        if (itemStack instanceof CraftItemStack) {
            if (itemStack.getType() != Material.AIR) {
                net.minecraft.world.item.ItemStack handle = ((CraftItemStack) itemStack).handle;
                Component name = handle.get(DataComponents.CUSTOM_NAME);
                return name == null ? null : name.getString();
            }
        }
        return null;
    }
}