package bot.inker.bukkit.nbt.neigeitems.utils;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.ref.RefCraftItemStack;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.entity.RefCraftEntity;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.entity.RefEntity;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.entity.RefEntityItem;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.world.RefAABB;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.world.RefCraftWorld;
import bot.inker.bukkit.nbt.internal.ref.neigeitems.world.RefWorldServer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class WorldUtils {
    /**
     * 1.17+ 版本起, World#dropItem 方法可以传入一个 Consumer, 用于在实体添加到世界中之前对实体进行一些操作.
     */
    private static final boolean DROP_ITEM_WITH_CONSUMER_SUPPORT = CbVersion.v1_17_R1.isSupport();
    /**
     * 1.13+ 版本起, CraftWorld#getNearbyEntities 方法中的 Predicate 参数由 Guava 实现变更为 Java 实现.
     */
    private static final boolean JAVA_PREDICATE_SUPPORT = CbVersion.v1_13_R1.isSupport();

    /**
     * 获取世界中掉落物实体的最大存活时长(tick).
     *
     * @param world 待检测世界.
     * @return 世界中掉落物实体的最大存活时长(tick).
     */
    public static int getDespawnRate(
            World world
    ) {
        return ((RefCraftWorld) (Object) world).getHandle().spigotConfig.itemDespawnRate;
    }

    /**
     * 在指定世界的指定坐标生成一个掉落物, 生成实体前对实体进行一些操作.
     *
     * @param world     待掉落世界.
     * @param location  待掉落坐标.
     * @param itemStack 待掉落物品.
     * @param function  掉落前对物品执行的操作.
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
     * @param world     待掉落世界.
     * @param location  待掉落坐标.
     * @param itemStack 待掉落物品.
     * @param velocity  待添加向量.
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

    /**
     * 检索实体周围, 指定大小立方体区域内的实体.
     *
     * @param entity 待检索实体.
     * @param x      立方体 x 轴边长的一半.
     * @param y      立方体 y 轴边长的一半.
     * @param z      立方体 z 轴边长的一半.
     * @param filter 对应实体需要满足的条件.
     * @return 生成的掉落物.
     */
    @NotNull
    public static List<Entity> getNearbyEntities(
            @NotNull Entity entity,
            double x,
            double y,
            double z,
            @Nullable Predicate<? super Entity> filter
    ) {
        return getNearbyEntities(entity, x, y, z, Integer.MAX_VALUE, filter);
    }

    /**
     * 检索实体周围, 指定大小立方体区域内的实体.
     *
     * @param entity 待检索实体.
     * @param x      立方体 x 轴边长的一半.
     * @param y      立方体 y 轴边长的一半.
     * @param z      立方体 z 轴边长的一半.
     * @param limit  检索数量上限.
     * @return 生成的掉落物.
     */
    @NotNull
    public static List<Entity> getNearbyEntities(
            @NotNull Entity entity,
            double x,
            double y,
            double z,
            int limit
    ) {
        return getNearbyEntities(entity, x, y, z, limit, null);
    }

    /**
     * 检索实体周围, 指定大小立方体区域内的实体.
     *
     * @param entity 待检索实体.
     * @param x      立方体 x 轴边长的一半.
     * @param y      立方体 y 轴边长的一半.
     * @param z      立方体 z 轴边长的一半.
     * @param limit  检索数量上限.
     * @param filter 对应实体需要满足的条件.
     * @return 生成的掉落物.
     */
    @NotNull
    public static List<Entity> getNearbyEntities(
            @NotNull Entity entity,
            double x,
            double y,
            double z,
            int limit,
            @Nullable Predicate<? super Entity> filter
    ) {
        Location location = entity.getLocation();
        return getEntities(entity, entity.getWorld(), location.getX() - x, location.getY() - y, location.getZ() - z, location.getX() + x, location.getY() + y, location.getZ() + z, limit, filter);
    }

    /**
     * 检索指定世界的指定坐标周围, 指定大小立方体区域内的实体.
     *
     * @param world    待检索世界.
     * @param location 待检索坐标.
     * @param x        立方体 x 轴边长的一半.
     * @param y        立方体 y 轴边长的一半.
     * @param z        立方体 z 轴边长的一半.
     * @param filter   对应实体需要满足的条件.
     * @return 生成的掉落物.
     */
    @NotNull
    public static List<Entity> getNearbyEntities(
            @NotNull World world,
            @NotNull Location location,
            double x,
            double y,
            double z,
            @Nullable Predicate<? super Entity> filter
    ) {
        return getNearbyEntities(world, location, x, y, z, Integer.MAX_VALUE, filter);
    }

    /**
     * 检索指定世界的指定坐标周围, 指定大小立方体区域内的实体.
     *
     * @param world    待检索世界.
     * @param location 待检索坐标.
     * @param x        立方体 x 轴边长的一半.
     * @param y        立方体 y 轴边长的一半.
     * @param z        立方体 z 轴边长的一半.
     * @param limit    检索数量上限.
     * @return 生成的掉落物.
     */
    @NotNull
    public static List<Entity> getNearbyEntities(
            @NotNull World world,
            @NotNull Location location,
            double x,
            double y,
            double z,
            int limit
    ) {
        return getNearbyEntities(world, location, x, y, z, limit, null);
    }

    /**
     * 检索指定世界的指定坐标周围, 指定大小立方体区域内的实体.
     *
     * @param world    待检索世界.
     * @param location 待检索坐标.
     * @param x        立方体 x 轴边长的一半.
     * @param y        立方体 y 轴边长的一半.
     * @param z        立方体 z 轴边长的一半.
     * @param limit    检索数量上限.
     * @param filter   对应实体需要满足的条件.
     * @return 生成的掉落物.
     */
    @NotNull
    public static List<Entity> getNearbyEntities(
            @NotNull World world,
            @NotNull Location location,
            double x,
            double y,
            double z,
            int limit,
            @Nullable Predicate<? super Entity> filter
    ) {
        return getEntities(null, world, location.getX() - x, location.getY() - y, location.getZ() - z, location.getX() + x, location.getY() + y, location.getZ() + z, limit, filter);
    }

    /**
     * 检索指定世界指定大小立方体区域内的实体.
     *
     * @param world  待检索世界.
     * @param minX   立方体 x 轴最小值.
     * @param minY   立方体 y 轴最小值.
     * @param minZ   立方体 z 轴最小值.
     * @param maxX   立方体 x 轴最大值.
     * @param maxY   立方体 y 轴最大值.
     * @param maxZ   立方体 z 轴最大值.
     * @param filter 对应实体需要满足的条件.
     * @return 生成的掉落物.
     */
    @NotNull
    public static List<Entity> getEntities(
            @NotNull World world,
            double minX,
            double minY,
            double minZ,
            double maxX,
            double maxY,
            double maxZ,
            @Nullable Predicate<? super Entity> filter
    ) {
        return getEntities(null, world, minX, minY, minZ, maxX, maxY, maxZ, Integer.MAX_VALUE, filter);
    }

    /**
     * 检索指定世界指定大小立方体区域内的实体.
     *
     * @param world 待检索世界.
     * @param minX  立方体 x 轴最小值.
     * @param minY  立方体 y 轴最小值.
     * @param minZ  立方体 z 轴最小值.
     * @param maxX  立方体 x 轴最大值.
     * @param maxY  立方体 y 轴最大值.
     * @param maxZ  立方体 z 轴最大值.
     * @param limit 检索数量上限.
     * @return 生成的掉落物.
     */
    @NotNull
    public static List<Entity> getEntities(
            @NotNull World world,
            double minX,
            double minY,
            double minZ,
            double maxX,
            double maxY,
            double maxZ,
            int limit
    ) {
        return getEntities(null, world, minX, minY, minZ, maxX, maxY, maxZ, limit, null);
    }

    /**
     * 检索指定世界指定大小立方体区域内的实体.
     *
     * @param world  待检索世界.
     * @param minX   立方体 x 轴最小值.
     * @param minY   立方体 y 轴最小值.
     * @param minZ   立方体 z 轴最小值.
     * @param maxX   立方体 x 轴最大值.
     * @param maxY   立方体 y 轴最大值.
     * @param maxZ   立方体 z 轴最大值.
     * @param limit  检索数量上限.
     * @param filter 对应实体需要满足的条件.
     * @return 生成的掉落物.
     */
    @NotNull
    public static List<Entity> getEntities(
            @NotNull World world,
            double minX,
            double minY,
            double minZ,
            double maxX,
            double maxY,
            double maxZ,
            int limit,
            @Nullable Predicate<? super Entity> filter
    ) {
        return getEntities(null, world, minX, minY, minZ, maxX, maxY, maxZ, limit, filter);
    }

    /**
     * 检索指定世界指定大小立方体区域内的实体.
     *
     * @param except 排除在外的实体.
     * @param world  待检索世界.
     * @param minX   立方体 x 轴最小值.
     * @param minY   立方体 y 轴最小值.
     * @param minZ   立方体 z 轴最小值.
     * @param maxX   立方体 x 轴最大值.
     * @param maxY   立方体 y 轴最大值.
     * @param maxZ   立方体 z 轴最大值.
     * @param limit  检索数量上限.
     * @param filter 对应实体需要满足的条件.
     * @return 生成的掉落物.
     */
    @NotNull
    public static List<Entity> getEntities(
            @Nullable Entity except,
            @NotNull World world,
            double minX,
            double minY,
            double minZ,
            double maxX,
            double maxY,
            double maxZ,
            int limit,
            @Nullable Predicate<? super Entity> filter
    ) {
        RefEntity realExcept = null;
        if (except instanceof RefCraftEntity) {
            realExcept = ((RefCraftEntity) except).getHandle();
        }
        RefWorldServer worldServer = ((RefCraftWorld) (Object) world).getHandle();
        RefAABB aabb = new RefAABB(minX, minY, minZ, maxX, maxY, maxZ);
        final List<Entity> entities = new ArrayList<>();
        if (JAVA_PREDICATE_SUPPORT) {
            worldServer.getEntities(realExcept, aabb, (Predicate<RefEntity>) refEntity -> {
                if (entities.size() < limit) {
                    Entity entity = refEntity.getBukkitEntity();
                    if (filter == null || filter.test(entity)) {
                        entities.add(entity);
                        return true;
                    }
                }
                return false;
            });
        } else {
            worldServer.getEntities(realExcept, aabb, (com.google.common.base.Predicate<RefEntity>) refEntity -> {
                if (entities.size() < limit) {
                    Entity entity = refEntity.getBukkitEntity();
                    if (filter == null || filter.test(entity)) {
                        entities.add(entity);
                        return true;
                    }
                }
                return false;
            });
        }
        return entities;
    }
}
