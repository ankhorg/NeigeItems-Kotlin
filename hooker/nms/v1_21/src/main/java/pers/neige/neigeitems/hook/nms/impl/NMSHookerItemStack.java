package pers.neige.neigeitems.hook.nms.impl;

import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.hook.nms.NMSHooker;
import pers.neige.neigeitems.item.builder.ItemBuilder;
import pers.neige.neigeitems.item.builder.NewItemBuilder;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtUtils;

/**
 * 1.20.4+ 版本, ItemStack 特殊兼容
 */
public class NMSHookerItemStack extends NMSHooker {
    @Override
    public @NonNull ItemBuilder newItemBuilder() {
        return new NewItemBuilder();
    }

    @Override
    public @NonNull ItemBuilder newItemBuilder(@Nullable Material material) {
        return new NewItemBuilder(material);
    }

    @Override
    public @NonNull ItemBuilder newItemBuilder(@Nullable ItemStack itemStack) {
        return new NewItemBuilder(itemStack);
    }

    @Override
    public @NonNull ItemBuilder newItemBuilder(@Nullable ConfigReader config) {
        return new NewItemBuilder(config);
    }

    @Override
    public @Nullable ConfigurationSection save(@Nullable ItemStack itemStack) {
        return NbtUtils.saveAfterV21(itemStack);
    }
}