package bot.inker.bukkit.nbt.neigeitems;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.ref.RefCraftItemStack;
import bot.inker.bukkit.nbt.internal.ref.RefCraftWorld;
import bot.inker.bukkit.nbt.internal.ref.RefEntityItem;
import bot.inker.bukkit.nbt.internal.ref.RefWorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.NeigeItems;

import java.util.function.Consumer;

public class WorldUtils {
    private static final boolean DROP_ITEM_WITH_CONSUMER_SUPPORT = CbVersion.v1_17_R1.isSupport();

    /**
     * 获取世界中掉落物实体的最大存活时长(tick).
     *
     * @param world 待检测世界.
     * @return 世界中掉落物实体的最大存活时长(tick).
     */
    public static int getDespawnRate(World world) {
        return ((RefCraftWorld) (Object) world).getHandle().spigotConfig.itemDespawnRate;
    }

    /**
     * 在指定世界的指定坐标生成一个掉落物, 生成实体前对实体进行一些操作.
     *
     * @param world 待掉落世界.
     * @param location 待掉落坐标.
     * @param itemStack 待掉落物品.
     * @param function 掉落前对物品执行的操作.
     * @return 生成的掉落物.
     */
    @NotNull
    public static Item dropItem(
            @NotNull World world,
            @NotNull Location location,
            @NotNull ItemStack itemStack,
            @Nullable Consumer<Item> function
    ) {
        RefWorldServer worldServer = ((RefCraftWorld) (Object) world).getHandle();

        RefEntityItem entity = new RefEntityItem(
                worldServer,
                location.getX(),
                location.getY(),
                location.getZ(),
                RefCraftItemStack.asNMSCopy(itemStack)
        );
        Item itemEntity = (Item) entity.getBukkitEntity();
        entity.pickupDelay = 10;
        if (function != null) {
            function.accept(itemEntity);
        }

        worldServer.addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return itemEntity;
    }

    /**
     * 在指定世界的指定坐标生成一个掉落物, 生成实体前对实体添加一个向量.
     *
     * @param world 待掉落世界.
     * @param location 待掉落坐标.
     * @param itemStack 待掉落物品.
     * @param velocity 待添加向量.
     * @return 生成的掉落物.
     */
    @NotNull
    public static Item dropItem(
            @NotNull World world,
            @NotNull Location location,
            @NotNull ItemStack itemStack,
            @NotNull Vector velocity
    ) {
        RefWorldServer worldServer = ((RefCraftWorld) (Object) world).getHandle();

        RefEntityItem entity = new RefEntityItem(
                worldServer,
                location.getX(),
                location.getY(),
                location.getZ(),
                RefCraftItemStack.asNMSCopy(itemStack)
        );
        Item itemEntity = (Item) entity.getBukkitEntity();
        entity.pickupDelay = 10;
        itemEntity.setVelocity(velocity);

        worldServer.addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return itemEntity;
    }
}
