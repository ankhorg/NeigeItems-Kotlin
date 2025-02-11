package pers.neige.neigeitems.hook.nms;

import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ItemTag;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.item.ItemPlaceholder;
import pers.neige.neigeitems.item.builder.ItemBuilder;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.Nbt;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtCompound;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtList;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtUtils;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.TranslationUtils;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.WorldUtils;
import pers.neige.neigeitems.utils.ItemUtils;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class NMSHooker {
    protected final Map<Material, NamespacedKey> materialNamespacedKeys = loadNamespacedKeys();

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
    public Item dropItem(@NotNull World world, @NotNull Location location, @NotNull ItemStack itemStack, @NotNull Consumer<Item> function) {
        return WorldUtils.dropItem(world, location, itemStack, function);
    }

    /**
     * 根据给定的文本生成一个 HoverEvent.
     *
     * @param text 待操作物品.
     * @return 生成的 HoverEvent.
     */
    @NotNull
    public HoverEvent hoverText(@NotNull String text) {
        return new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(text));
    }

    /**
     * 根据给定的物品生成一个 HoverEvent.
     *
     * @param itemStack 待操作物品.
     * @return 生成的 HoverEvent.
     */
    @NotNull
    public HoverEvent hoverItem(@NotNull ItemStack itemStack) {
        String nbtString;
        if (itemStack.getType() == Material.AIR) {
            nbtString = "{}";
        } else {
            nbtString = ItemUtils.getNbt(itemStack).toString();
        }
        return new HoverEvent(HoverEvent.Action.SHOW_ITEM, new net.md_5.bungee.api.chat.hover.content.Item(getNamespacedKey(itemStack.getType()).getKey(), itemStack.getAmount(), ItemTag.ofNbt(nbtString)));
    }

    @NotNull
    public ItemBuilder newItemBuilder() {
        return new ItemBuilder();
    }

    @NotNull
    public ItemBuilder newItemBuilder(@Nullable Material material) {
        return new ItemBuilder(material);
    }

    @NotNull
    public ItemBuilder newItemBuilder(@Nullable ItemStack itemStack) {
        return new ItemBuilder(itemStack);
    }

    @NotNull
    public ItemBuilder newItemBuilder(@Nullable ConfigurationSection config) {
        return new ItemBuilder(config);
    }

    /**
     * 等效于Material#getMaterial, 但为1.12.2版本添加了数字ID处理.
     */
    @Nullable
    public Material getMaterial(@Nullable String material) {
        if (material == null) return null;
        return Material.getMaterial(material.toUpperCase(Locale.ENGLISH));
    }

    /**
     * 给予玩家经验, 修复了1.12.2的逻辑谬误.
     *
     * @param player 接收经验的玩家.
     * @param exp    经验数量.
     */
    public void giveExp(@NotNull Player player, int exp) {
        player.giveExp(exp);
    }

    public void editNameAndLoreAfterMojangMotherDead(@NotNull ItemStack itemStack, BiFunction<ItemStack, String, ItemPlaceholder.ParseResult> handler) {
    }

    /**
     * 获取用于/ni itemnbt 指令的展示用NBT.
     *
     * @param itemStack 待获取物品.
     * @return 展示用NBT
     */
    @NotNull
    public NbtCompound getDisplayNbt(@NotNull ItemStack itemStack) {
        return ItemUtils.getNbt(itemStack);
    }

    protected org.bukkit.NamespacedKey parseToNamespacedKey(String id) {
        String[] args = new String[]{"minecraft", id};
        int index = id.indexOf(":");
        if (index >= 0) {
            args[1] = id.substring(index + 1);
            if (index >= 1) {
                args[0] = id.substring(0, index);
            }
        }
        return new org.bukkit.NamespacedKey(args[0], args[1]);
    }

    @NotNull
    protected Map<Enchantment, Integer> buildEnchantments(@NotNull NbtList ench) {
        Map<Enchantment, Integer> enchantments = new LinkedHashMap<>(ench.size());

        for (Nbt<?> nbt : ench) {
            String id = ((NbtCompound) nbt).getString(NbtUtils.getEnchantmentIdNbtKey());
            int level = ((NbtCompound) nbt).getShort(NbtUtils.getEnchantmentLvlNbtKey());
            Enchantment enchant = Enchantment.getByKey(parseToNamespacedKey(id));
            if (enchant != null) {
                enchantments.put(enchant, level);
            }
        }

        return enchantments;
    }

    /**
     * 将物品保存为NI可识别的配置文件, 性能较差, 不建议于性能敏感处使用.
     *
     * @param itemStack 待转换物品.
     * @return NI可识别的配置文件
     */
    @Nullable
    public ConfigurationSection save(@Nullable ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) return null;
        ConfigurationSection result = new YamlConfiguration();
        result.set("material", itemStack.getType().toString());
        short damage = ItemUtils.getDamage(itemStack);
        if (damage > 0) {
            result.set("damage", damage);
        }

        NbtCompound nbt = ItemUtils.getNbtOrNull(itemStack);
        if (nbt != null) {
            nbt = nbt.clone();

            NbtCompound display = nbt.getCompound(NbtUtils.getDisplayNbtKey());
            if (display != null) {
                int color = display.getInt("color", -1);
                if (color != -1) {
                    result.set("color", Integer.toString(color, 16).toUpperCase(Locale.getDefault()));
                    display.remove("color");
                }
                String rawName = display.getString(NbtUtils.getNameNbtKey());
                if (rawName != null) {
                    String name = TranslationUtils.toLegacyText(rawName);
                    result.set("name", name);
                    display.remove(NbtUtils.getNameNbtKey());
                }
                NbtList rawLore = display.getList(NbtUtils.getLoreNbtKey());
                if (rawLore != null && !rawLore.isEmpty()) {
                    List<String> lore = new ArrayList<>();
                    for (Nbt<?> loreNbt : rawLore) {
                        lore.add(TranslationUtils.toLegacyText(loreNbt.getAsString()));
                    }
                    result.set("lore", lore);
                    display.remove(NbtUtils.getLoreNbtKey());
                }
                if (display.isEmpty()) {
                    nbt.remove(NbtUtils.getDisplayNbtKey());
                }
            }

            String cmdKey = NbtUtils.getCustomModelDataNbtKeyOrNull();
            if (cmdKey != null) {
                int customModelData = nbt.getInt(cmdKey, -1);
                if (customModelData != -1) {
                    result.set("custom-model-data", customModelData);
                    nbt.remove(cmdKey);
                }
            }

            boolean unbreakable = nbt.getBoolean(NbtUtils.getUnbreakableNbtKey());
            if (unbreakable) {
                result.set("unbreakable", true);
                nbt.remove(NbtUtils.getUnbreakableNbtKey());
            }

            int hideFlags = nbt.getInt("HideFlags");
            if (hideFlags > 0) {
                List<String> flags = new ArrayList<>();
                for (ItemFlag flag : ItemFlag.values()) {
                    int bitModifier = (byte) (1 << flag.ordinal());
                    if ((hideFlags & bitModifier) == bitModifier) {
                        flags.add(flag.name());
                    }
                }
                if (!flags.isEmpty()) {
                    result.set("item-flags", flags);
                }
                nbt.remove("HideFlags");
            }

            NbtList enchantmentNbt = nbt.getList(NbtUtils.getEnchantmentsNbtKey());
            if (enchantmentNbt != null) {
                Map<Enchantment, Integer> enchantments = buildEnchantments(enchantmentNbt);
                if (!enchantments.isEmpty()) {
                    ConfigurationSection enchantSection = result.createSection("enchantments");
                    enchantments.forEach((enchant, level) -> {
                        enchantSection.set(enchant.getName(), level);
                    });
                }
                nbt.remove(NbtUtils.getEnchantmentsNbtKey());
            }

            // 设置物品NBT
            if (!nbt.isEmpty()) {
                result.set("nbt", ItemUtils.toStringMap(nbt));
            }
        }
        return result;
    }
}