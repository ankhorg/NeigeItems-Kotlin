package pers.neige.neigeitems.hook.nms.impl;

import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.hook.nms.NMSHooker;

public final class NMSHookerV1_13_R1Impl extends NMSHooker {
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