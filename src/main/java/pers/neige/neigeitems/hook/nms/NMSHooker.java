package pers.neige.neigeitems.hook.nms;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class NMSHooker{
    protected Map<Material, NamespacedKey> loadNamespacedKeys() {
        Map<Material, NamespacedKey> result = new HashMap<>();
        for (Material material : Material.values()) {
            org.bukkit.NamespacedKey bukkitNamespacedKey = material.getKey();
            result.put(material, new NamespacedKey(bukkitNamespacedKey.getNamespace(), bukkitNamespacedKey.getKey()));
        }
        return result;
    }

    protected Map<Material, NamespacedKey> materialNamespacedKeys;

    public NMSHooker() {
        materialNamespacedKeys = loadNamespacedKeys();
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
    public @Nullable Integer getCustomModelData(@Nullable ItemMeta itemMeta) {
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
     * 在指定世界的指定位置掉落一个物品, 在将物品实体添加到世界中之前, 对物品进行某些操作.
     *
     * @param world 掉落物品的世界
     * @param location 掉落物品的坐标
     * @param itemStack 待掉落物品
     * @param function 对掉落物品执行的操作
     */
    public @NotNull Item dropItem(
            @NotNull World world,
            @NotNull Location location,
            @NotNull ItemStack itemStack,
            @NotNull Consumer<Item> function
    ) {
        return world.dropItem(location, itemStack, function::accept);
    }

    /**
     * 通过物品材质, 获取 NamespacedKey.
     *
     * @param material 待获取材质
     */
    public NamespacedKey getNamespacedKey(Material material) {
        return materialNamespacedKeys.get(material);
    }
}