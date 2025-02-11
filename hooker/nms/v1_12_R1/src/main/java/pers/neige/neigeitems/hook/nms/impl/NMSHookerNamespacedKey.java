package pers.neige.neigeitems.hook.nms.impl;

import kotlin.text.StringsKt;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.MathHelper;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.RegistryMaterials;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.hook.nms.NamespacedKey;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.Nbt;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtCompound;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtList;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtUtils;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.EnchantmentUtils;

import java.util.*;

/**
 * 1.12.2 版本, NamespacedKey 特殊兼容
 */
public class NMSHookerNamespacedKey extends NMSHookerCustomModelData {
    private Material[] byId = new Material[383];
    private final Map<Integer, Enchantment> enchantmentsById = new HashMap<>();

    public NMSHookerNamespacedKey() {
        for (Material material : Material.values()) {
            if (byId.length > material.getId()) {
                byId[material.getId()] = material;
            } else {
                byId = Arrays.copyOfRange(byId, 0, material.getId() + 2);
                byId[material.getId()] = material;
            }
        }
        for (Enchantment enchantment : Enchantment.values()) {
            enchantmentsById.put(EnchantmentUtils.getId(enchantment), enchantment);
        }
    }

    @Override
    protected Map<Material, NamespacedKey> loadNamespacedKeys() {
        RegistryMaterials<MinecraftKey, net.minecraft.server.v1_12_R1.Item> REGISTRY = net.minecraft.server.v1_12_R1.Item.REGISTRY;
        Map<Material, NamespacedKey> result = new HashMap<>();
        for (Material material : Material.values()) {
            try {
                net.minecraft.server.v1_12_R1.Item item = CraftMagicNumbers.getItem(material);
                if (item != null) {
                    MinecraftKey minecraftKey = REGISTRY.b(item);
                    result.put(material, new NamespacedKey(minecraftKey.b(), minecraftKey.getKey()));
                }
            } catch (Throwable ignored) {
            }
        }
        return result;
    }

    @Override
    @Nullable
    public Material getMaterial(@Nullable String material) {
        if (material == null) return null;
        Material result = Material.getMaterial(material.toUpperCase(Locale.ENGLISH));
        if (result != null) return result;
        Integer id = StringsKt.toIntOrNull(material);
        if (id == null) return null;
        return byId.length > id && id >= 0 ? byId[id] : null;
    }

    @Override
    public void giveExp(@NotNull Player player, int exp) {
        EntityPlayer realPlayer = ((CraftPlayer) player).getHandle();
        realPlayer.addScore(exp);
        realPlayer.exp += (float) exp / (float) realPlayer.getExpToLevel();
        realPlayer.expTotal = MathHelper.clamp(realPlayer.expTotal + exp, 0, Integer.MAX_VALUE);

        while (realPlayer.exp < 0.0F) {
            float f = realPlayer.exp * (float) realPlayer.getExpToLevel();
            if (realPlayer.expLevel > 0) {
                realPlayer.levelDown(-1);
                realPlayer.exp = 1.0F + f / (float) realPlayer.getExpToLevel();
            } else {
                realPlayer.levelDown(-1);
                realPlayer.exp = 0.0F;
            }
        }

        while (realPlayer.exp >= 1.0F) {
            realPlayer.exp = (realPlayer.exp - 1.0F) * (float) realPlayer.getExpToLevel();
            realPlayer.levelDown(1);
            realPlayer.exp /= (float) realPlayer.getExpToLevel();
        }
    }

    @Override
    @NotNull
    protected Map<Enchantment, Integer> buildEnchantments(@NotNull NbtList ench) {
        Map<Enchantment, Integer> enchantments = new LinkedHashMap<>(ench.size());

        for (Nbt<?> nbt : ench) {
            int id = ((NbtCompound) nbt).getShort(NbtUtils.getEnchantmentIdNbtKey());
            int level = ((NbtCompound) nbt).getShort(NbtUtils.getEnchantmentLvlNbtKey());
            Enchantment enchant = enchantmentsById.get(id);
            if (enchant != null) {
                enchantments.put(enchant, level);
            }
        }

        return enchantments;
    }
}