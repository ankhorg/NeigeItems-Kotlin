package pers.neige.neigeitems.hook.nms.impl;

import net.minecraft.server.v1_13_R1.EntityItem;
import net.minecraft.server.v1_13_R1.WorldServer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_13_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R1.inventory.CraftItemStack;
import org.bukkit.entity.Item;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.hook.nms.NMSHooker;

import java.util.function.Consumer;

public final class NMSHookerV1_13_R1Impl extends NMSHooker {
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
}