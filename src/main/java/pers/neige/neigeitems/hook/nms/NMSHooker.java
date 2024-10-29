package pers.neige.neigeitems.hook.nms;

import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ItemTag;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.item.builder.ItemBuilder;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtCompound;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtItemStack;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.WorldUtils;
import pers.neige.neigeitems.ref.chat.RefChatFormatting;
import pers.neige.neigeitems.ref.chat.RefChatSerializer;
import pers.neige.neigeitems.ref.chat.RefCraftChatMessage;
import pers.neige.neigeitems.ref.nbt.RefCraftItemStack;
import pers.neige.neigeitems.ref.nbt.RefNbtBase;
import pers.neige.neigeitems.ref.nbt.RefNbtTagCompound;
import pers.neige.neigeitems.ref.nbt.RefNbtTagString;
import pers.neige.neigeitems.utils.ItemUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class NMSHooker {
    protected Map<Material, NamespacedKey> materialNamespacedKeys;

    public NMSHooker() {
        materialNamespacedKeys = loadNamespacedKeys();
    }

    protected Map<Material, NamespacedKey> loadNamespacedKeys() {
        Map<Material, NamespacedKey> result = new HashMap<>();
        for (Material material : Material.values()) {
            org.bukkit.NamespacedKey bukkitNamespacedKey = material.getKey();
            result.put(material, new NamespacedKey(bukkitNamespacedKey.getNamespace(), bukkitNamespacedKey.getKey()));
        }
        return result;
    }

    /**
     * 检测 ItemMeta 是否存在 CustomModelData.
     * 对于不存在 CustomModelData 的版本, 将永远返回 false.
     *
     * @param itemMeta 待操作 ItemMeta
     * @return 是否存在 CustomModelData
     */
    public boolean hasCustomModelData(@Nullable ItemMeta itemMeta) {
        return itemMeta != null && itemMeta.hasCustomModelData();
    }

    /**
     * 获取 ItemMeta 中的 CustomModelData.
     * 如果 ItemMeta 中不存在 CustomModelData, 将返回 null.
     * 对于不存在 CustomModelData 的版本, 将永远返回 null.
     *
     * @param itemMeta 待操作 ItemMeta
     * @return 是否存在 CustomModelData
     */
    @Nullable
    public Integer getCustomModelData(@Nullable ItemMeta itemMeta) {
        if (itemMeta == null || !hasCustomModelData(itemMeta)) {
            return null;
        } else {
            return itemMeta.getCustomModelData();
        }
    }

    /**
     * 设置 ItemMeta 中的 CustomModelData.
     * 对于不存在 CustomModelData 的版本, 将等同于空方法.
     *
     * @param itemMeta 待操作 ItemMeta
     */
    public void setCustomModelData(@Nullable ItemMeta itemMeta, int data) {
        if (itemMeta != null) {
            itemMeta.setCustomModelData(data);
        }
    }

    /**
     * 通过物品材质, 获取 NamespacedKey.
     *
     * @param material 待获取材质
     */
    public NamespacedKey getNamespacedKey(Material material) {
        return materialNamespacedKeys.get(material);
    }

    /**
     * 在指定世界的指定坐标生成一个掉落物, 生成实体前对实体进行一些操作.
     *
     * @param world     待掉落世界.
     * @param location  待掉落坐标.
     * @param itemStack 待掉落物品.
     * @param function  掉落前对物品执行的操作.
     * @return 生成的掉落物.
     */
    @Deprecated
    @NotNull
    public Item dropItem(
            @NotNull World world,
            @NotNull Location location,
            @NotNull ItemStack itemStack,
            @NotNull Consumer<Item> function
    ) {
        return WorldUtils.dropItem(world, location, itemStack, function);
    }

    /**
     * 根据给定的文本生成一个 HoverEvent.
     *
     * @param text 待操作物品.
     * @return 生成的 HoverEvent.
     */
    @NotNull
    public HoverEvent hoverText(
            @NotNull String text
    ) {
        return new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                new Text(text)
        );
    }

    /**
     * 根据给定的物品生成一个 HoverEvent.
     *
     * @param itemStack 待操作物品.
     * @return 生成的 HoverEvent.
     */
    @NotNull
    public HoverEvent hoverItem(
            @NotNull ItemStack itemStack
    ) {
        String nbtString;
        if (itemStack.getType() == Material.AIR) {
            nbtString = "{}";
        } else {
            nbtString = ItemUtils.getNbt(itemStack).toString();
        }
        return new HoverEvent(
                HoverEvent.Action.SHOW_ITEM,
                new net.md_5.bungee.api.chat.hover.content.Item(
                        getNamespacedKey(itemStack.getType()).getKey(),
                        itemStack.getAmount(),
                        ItemTag.ofNbt(nbtString)
                )
        );
    }

    @NotNull
    public ItemBuilder newItemBuilder() {
        return new ItemBuilder();
    }

    @NotNull
    public ItemBuilder newItemBuilder(
            @Nullable Material material
    ) {
        return new ItemBuilder(material);
    }

    @NotNull
    public ItemBuilder newItemBuilder(
            @Nullable ItemStack itemStack
    ) {
        return new ItemBuilder(itemStack);
    }

    @NotNull
    public ItemBuilder newItemBuilder(
            @Nullable ConfigurationSection config
    ) {
        return new ItemBuilder(config);
    }

    @Nullable
    public NbtCompound getCustomNbt(@Nullable ItemStack itemStack) {
        if (itemStack != null && itemStack.getType() != Material.AIR) {
            return new NbtItemStack(itemStack).getTag();
        }
        return null;
    }

    @Nullable
    public NbtCompound getOrCreateCustomNbt(@Nullable ItemStack itemStack) {
        if (itemStack != null && itemStack.getType() != Material.AIR) {
            return new NbtItemStack(itemStack).getOrCreateTag();
        }
        return null;
    }

    @Nullable
    public String getDisplayNameFromCraftItemStack(@Nullable ItemStack itemStack) {
        if (itemStack instanceof RefCraftItemStack) {
            if (itemStack.getType() != Material.AIR) {
                RefNbtTagCompound tag = ((RefCraftItemStack) itemStack).handle.getTag();
                if (tag != null) {
                    RefNbtBase display = tag.get("display");
                    if (display instanceof RefNbtTagCompound) {
                        RefNbtBase tagName = ((RefNbtTagCompound) display).get("Name");
                        if (tagName instanceof RefNbtTagString) {
                            String rawName = tagName.asString();
                            if (CbVersion.current() == CbVersion.v1_12_R1) {
                                return rawName;
                            } else if (CbVersion.v1_15_R1.isSupport()) {
                                return RefCraftChatMessage.fromComponent(RefChatSerializer.fromJson(rawName));
                            } else {
                                return RefCraftChatMessage.fromComponent(RefChatSerializer.fromJson(rawName), RefChatFormatting.WHITE);
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    @Nullable
    public Entity getEntityFromID(
            @NotNull World world,
            int id
    ) {
        return WorldUtils.getEntityFromID(world, id);
    }
}