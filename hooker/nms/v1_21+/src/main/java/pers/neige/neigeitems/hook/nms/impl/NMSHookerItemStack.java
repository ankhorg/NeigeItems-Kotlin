package pers.neige.neigeitems.hook.nms.impl;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.component.ItemLore;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.hook.nms.NMSHooker;
import pers.neige.neigeitems.item.builder.ItemBuilder;
import pers.neige.neigeitems.item.builder.NewItemBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

/**
 * 1.20.4+ 版本, ItemStack 特殊兼容
 */
public class NMSHookerItemStack extends NMSHooker {
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

    @Override
    public void editNameAndLoreAfterMojangMotherDead(@NotNull ItemStack itemStack, BiFunction<ItemStack, String, String> handler) {
        if (!(itemStack instanceof CraftItemStack)) return;
        net.minecraft.world.item.ItemStack nmsItemStack = ((CraftItemStack) itemStack).handle;
        Component name = nmsItemStack.get(DataComponents.CUSTOM_NAME);
        if (name != null) {
            nmsItemStack.set(DataComponents.CUSTOM_NAME, CraftChatMessage.fromStringOrNull(handler.apply(itemStack, CraftChatMessage.fromComponent(name))));
        }
        ItemLore lore = nmsItemStack.get(DataComponents.LORE);
        if (lore != null) {
            List<Component> lines = lore.lines();
            boolean edited = false;
            if (!lines.isEmpty()) {
                List<Component> newLines = new ArrayList<>();
                for (Component line : lines) {
                    newLines.add(CraftChatMessage.fromStringOrNull(handler.apply(itemStack, CraftChatMessage.fromComponent(line))));
                }
                lines = newLines;
                edited = true;
            }
            List<Component> styledLines = lore.styledLines();
            if (!styledLines.isEmpty()) {
                List<Component> newStyledLines = new ArrayList<>();
                for (Component styledLine : styledLines) {
                    newStyledLines.add(CraftChatMessage.fromStringOrNull(handler.apply(itemStack, CraftChatMessage.fromComponent(styledLine))));
                }
                styledLines = newStyledLines;
                edited = true;
            }
            if (edited) {
                nmsItemStack.set(DataComponents.LORE, new ItemLore(lines, styledLines));
            }
        }
    }
}