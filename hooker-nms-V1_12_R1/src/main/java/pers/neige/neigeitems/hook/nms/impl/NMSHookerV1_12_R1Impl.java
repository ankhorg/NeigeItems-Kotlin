package pers.neige.neigeitems.hook.nms.impl;

import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.RegistryMaterials;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.hook.nms.NMSHooker;
import pers.neige.neigeitems.hook.nms.NamespacedKey;

import java.util.HashMap;
import java.util.Map;

public final class NMSHookerV1_12_R1Impl extends NMSHooker {
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
            } catch(Throwable ignored) {}
        }
        return result;
    }

    @Override
    public boolean hasCustomModelData(@Nullable ItemMeta itemMeta) {
        return false;
    }

    @Override
    @Nullable
    public Integer getCustomModelData(@Nullable ItemMeta itemMeta) {
        return null;
    }

    @Override
    public void setCustomModelData(@Nullable ItemMeta itemMeta, int data) {}

}