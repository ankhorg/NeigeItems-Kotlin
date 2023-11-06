package pers.neige.neigeitems.hook.nms.impl;

import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.RegistryMaterials;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers;
import pers.neige.neigeitems.hook.nms.NamespacedKey;

import java.util.HashMap;
import java.util.Map;

/**
 * 1.12.2 版本, NamespacedKey 特殊兼容
 */
public class NMSHookerNamespacedKey extends NMSHookerCustomModelData {
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
}