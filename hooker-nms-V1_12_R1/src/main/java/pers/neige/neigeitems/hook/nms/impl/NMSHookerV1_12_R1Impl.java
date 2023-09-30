package pers.neige.neigeitems.hook.nms.impl;

import net.minecraft.server.v1_12_R1.EntityItem;
import net.minecraft.server.v1_12_R1.MinecraftKey;
import net.minecraft.server.v1_12_R1.RegistryMaterials;
import net.minecraft.server.v1_12_R1.WorldServer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers;
import org.bukkit.entity.Item;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.hook.nms.NMSHooker;
import pers.neige.neigeitems.hook.nms.NamespacedKey;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class NMSHookerV1_12_R1Impl extends NMSHooker {
    private final Map<Material, String> materialComponentKeys = new HashMap<>();

    public NMSHookerV1_12_R1Impl() {
        super();
        try {
            Field nameField = net.minecraft.server.v1_12_R1.Item.class.getDeclaredField("name");
            nameField.setAccessible(true);

            for (Material material : Material.values()) {
                try {
                    net.minecraft.server.v1_12_R1.Item item = CraftMagicNumbers.getItem(material);
                    if (item != null) {
                        String name = (String)nameField.get(item);
                        StringBuilder stringBuilder = new StringBuilder();
                        for (char c : name.toCharArray()) {
                            if (Character.isUpperCase(c)) {
                                stringBuilder.append("_").append(Character.toLowerCase(c));
                            } else {
                                stringBuilder.append(c);
                            }
                        }
                        materialComponentKeys.put(material, stringBuilder.toString());
                    }
                } catch(Throwable ignored) {}
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
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
            } catch(Throwable ignored) {}
        }
        return result;
    }

    @Override
    public boolean hasCustomModelData(@Nullable ItemMeta itemMeta) {
        return false;
    }

    @Override
    public @Nullable Integer getCustomModelData(@Nullable ItemMeta itemMeta) {
        return null;
    }

    @Override
    public void setCustomModelData(@Nullable ItemMeta itemMeta, int data) {}

    @Override
    public @NotNull Item dropItem(
            @NotNull World world,
            @NotNull Location location,
            @NotNull ItemStack itemStack,
            @NotNull Consumer<Item> function
    ) {
        CraftWorld craftWorld = (CraftWorld) world;
        WorldServer nmsWorld = craftWorld.getHandle();

        EntityItem entity = new EntityItem(
                nmsWorld,
                location.getX(),
                location.getY(),
                location.getZ(),
                CraftItemStack.asNMSCopy(itemStack)
        );

        entity.pickupDelay = 10;
        function.accept(((Item) entity.getBukkitEntity()));
        nmsWorld.addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return (Item) entity.getBukkitEntity();
    }

    @Override
    public String getComponentKey(Material material) {
        return materialComponentKeys.get(material);
    }

    @Override
    public String getNamespace(Material material) {
        return materialNamespacedKeys.get(material).getNamespace();
    }

    @Override
    public String getKey(Material material) {
        return materialNamespacedKeys.get(material).getKey();
    }
}