package pers.neige.neigeitems.hook.nms.impl;

import lombok.NonNull;
import lombok.val;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ChunkMap;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.EntityUtils;

import java.util.Map;

/**
 * 1.21.5+ 版本, World.getEntity 特殊兼容
 */
public class NMSHookerWorldEntity extends NMSHookerItemStack {
    @Override
    public @Nullable Entity getEntityFromID1(
            @NonNull World world,
            int entityId
    ) {
        val trackedEntity = ((Map<Integer, ChunkMap.TrackedEntity>) (Object) ((CraftWorld) world).getHandle().getChunkSource().chunkMap.entityMap).get(entityId);
        if (trackedEntity == null) return null;
        return EntityUtils.getEntityFromTrackedEntity(trackedEntity);
    }

    @Override
    public Object getDataComponentType(String key) {
        return BuiltInRegistries.DATA_COMPONENT_TYPE.getValue(ResourceLocation.parse(key));
    }

    @Override
    public Object getKeyByType(Object type) {
        return BuiltInRegistries.DATA_COMPONENT_TYPE.getKey((DataComponentType<?>) type);
    }
}