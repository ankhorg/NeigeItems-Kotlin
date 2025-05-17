package pers.neige.neigeitems.hook.nms.impl;

import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.component.ItemLore;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.hook.nms.NMSHooker;
import pers.neige.neigeitems.item.ItemPlaceholder;
import pers.neige.neigeitems.item.builder.ItemBuilder;
import pers.neige.neigeitems.item.builder.NewItemBuilder;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtCompound;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtUtils;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.ComponentUtils;
import pers.neige.neigeitems.utils.ItemUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    public ItemBuilder newItemBuilder(@Nullable Material material) {
        return new NewItemBuilder(material);
    }

    @Override
    @NotNull
    public ItemBuilder newItemBuilder(@Nullable ItemStack itemStack) {
        return new NewItemBuilder(itemStack);
    }

    @Override
    @NotNull
    public ItemBuilder newItemBuilder(@Nullable ConfigReader config) {
        return new NewItemBuilder(config);
    }

    @Override
    public void editNameAndLoreAfterMojangMotherDead(@NotNull ItemStack itemStack, BiFunction<ItemStack, String, ItemPlaceholder.ParseResult> handler) {
        if (!(itemStack instanceof CraftItemStack)) return;
        net.minecraft.world.item.ItemStack nmsItemStack = ((CraftItemStack) itemStack).handle;
        Component name = nmsItemStack.get(DataComponents.CUSTOM_NAME);
        if (name != null) {
            String json = CraftChatMessage.toJSON(name);
            ItemPlaceholder.ParseResult parsed = handler.apply(itemStack, json);
            if (parsed.getChanged()) {
                nmsItemStack.set(DataComponents.CUSTOM_NAME, CraftChatMessage.fromJSON(parsed.getText()));
            }
        }
        ItemLore lore = nmsItemStack.get(DataComponents.LORE);
        if (lore != null) {
            List<Component> lines = lore.lines();
            boolean edited = false;
            if (!lines.isEmpty()) {
                List<Component> newLines = new ArrayList<>();
                for (Component line : lines) {
                    if (line == null) {
                        newLines.add(null);
                        continue;
                    }
                    String json = CraftChatMessage.toJSON(line);
                    ItemPlaceholder.ParseResult parsed = handler.apply(itemStack, json);
                    if (parsed.getChanged()) {
                        newLines.add(CraftChatMessage.fromJSON(parsed.getText()));
                        edited = true;
                    } else {
                        newLines.add(line);
                    }
                }
                lines = newLines;
            }
            List<Component> styledLines = lore.styledLines();
            if (!styledLines.isEmpty()) {
                List<Component> newStyledLines = new ArrayList<>();
                for (Component styledLine : styledLines) {
                    if (styledLine == null) {
                        newStyledLines.add(null);
                        continue;
                    }
                    String json = CraftChatMessage.toJSON(styledLine);
                    ItemPlaceholder.ParseResult parsed = handler.apply(itemStack, json);
                    if (parsed.getChanged()) {
                        newStyledLines.add(CraftChatMessage.fromJSON(parsed.getText()));
                        edited = true;
                    } else {
                        newStyledLines.add(styledLine);
                    }
                }
                styledLines = newStyledLines;
            }
            if (edited) {
                nmsItemStack.set(DataComponents.LORE, new ItemLore(lines, styledLines));
            }
        }
    }

    @Override
    @NotNull
    public NbtCompound getDisplayNbt(@NotNull ItemStack itemStack) {
        net.minecraft.world.item.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
        CompoundTag compound = new CompoundTag();
        for (Map.Entry<DataComponentType<?>, Optional<?>> entry : nmsItemStack.getComponentsPatch().entrySet()) {
            TypedDataComponent<?> component = TypedDataComponent.createUnchecked(entry.getKey(), entry.getValue().get());
            ResourceLocation key = (ResourceLocation) ComponentUtils.getKeyByType(component.type());
            compound.put(key.toString(), component.encodeValue(NewItemBuilder.registryOps).getOrThrow());
        }
        return NbtCompound.Unsafe.of(compound);
    }

    @Override
    @Nullable
    public ConfigurationSection save(@Nullable ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) return null;
        ConfigurationSection result = new YamlConfiguration();
        result.set("material", itemStack.getType().toString());

        NbtCompound nbt = ItemUtils.getNbtOrNull(itemStack);
        if (nbt != null && !nbt.isEmpty()) {
            result.set("nbt", ItemUtils.toStringMap(nbt));
        }

        NbtCompound components = getDisplayNbt(itemStack);
        components.remove("minecraft:custom_data");
        if (!components.isEmpty()) {
            result.set("components", ItemUtils.toStringMap(components));
        }

        return result;
    }

    @Override
    public void overrideComponent(@NotNull ItemStack receiver, @NotNull ItemStack provider, @NotNull List<String> components) {
        CraftItemStack receiverCraft = receiver instanceof CraftItemStack ? (CraftItemStack) receiver : (CraftItemStack) NbtUtils.getCraftDelegate(receiver);
        if (receiverCraft == null) return;
        CraftItemStack providerCraft = provider instanceof CraftItemStack ? (CraftItemStack) provider : (CraftItemStack) NbtUtils.getCraftDelegate(provider);
        if (providerCraft == null) return;
        net.minecraft.world.item.ItemStack receiverNms = receiverCraft.handle;
        net.minecraft.world.item.ItemStack providerNms = providerCraft.handle;
        DataComponentPatch patch = providerNms.getComponentsPatch();
        components.forEach(componentId -> {
            DataComponentType<?> componentType = (DataComponentType<?>) ComponentUtils.getDataComponentType(componentId);
            if (componentType == null) return;
            Optional<?> optional = patch.get(componentType);
            Object component = optional == null ? null : optional.orElse(null);
            receiverNms.set((DataComponentType<? super Object>) componentType, component);
        });
    }
}