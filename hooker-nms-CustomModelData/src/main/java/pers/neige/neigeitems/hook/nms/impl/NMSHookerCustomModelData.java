package pers.neige.neigeitems.hook.nms.impl;

import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

/**
 * 1.14- 版本, CustomModelData 特殊兼容
 */
public class NMSHookerCustomModelData extends NMSHookerHoverEvent {
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
    public void setCustomModelData(@Nullable ItemMeta itemMeta, int data) {
    }
}