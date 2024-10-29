package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import pers.neige.neigeitems.ref.entity.RefCraftEntity;
import pers.neige.neigeitems.ref.entity.RefEntity;
import pers.neige.neigeitems.ref.entity.RefEntityItem;
import pers.neige.neigeitems.ref.nbt.RefCraftItemStack;
import pers.neige.neigeitems.ref.server.level.RefServerEntity;
import pers.neige.neigeitems.ref.server.level.RefTrackedEntity;
import pers.neige.neigeitems.ref.server.level.RefWorldServer;
import pers.neige.neigeitems.ref.util.RefInt2ObjectMap;
import pers.neige.neigeitems.ref.world.RefAABB;
import pers.neige.neigeitems.ref.world.RefCraftWorld;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
     * 1.19.4+ 版本起, Chunk 类移除 bukkitChunk 字段, 需要自行构建 CraftChunk 实例.
     */
    private static final boolean REMOVE_BUKKIT_CHUNK_FIELD = CbVersion.v1_19_R3.isSupport();
    /**
     * 1.17+ 版本起, spawnEntity 方法添加 boolean 类型 randomizeData 参数, 用于随机生成实体数据.
     */
    private static final boolean RANDOMIZE_DATA_SUPPORT = CbVersion.v1_17_R1.isSupport();
    /**
     * 1.20.2+ 版本起, addEntity 方法的 function 参数由 org.bukkit.util.Consumer 变更为 java.util.function.Consumer.
     */
    private static final boolean JAVA_CONSUMER_SUPPORT = CbVersion.v1_20_R2.isSupport();
    /**
     * 1.14+ 版本起, WorldServer 类移除 tracker 字段.
     */
    private static final boolean REMOVED_TRACKER = CbVersion.v1_14_R1.isSupport();

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
            worldServer.getEntities(realExcept, aabb, refEntity -> {
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

    /**
     * 生成一个实体, 但不添加进世界里.
     *
     * @param location 待生成坐标.
     * @param type     实体类型.
     * @return 生成的实体.
     */
    @Nullable
    public static Entity createEntity(
            @NotNull Location location,
            @NotNull EntityType type
    ) {
        return createEntity(location, type, true);
    }

    /**
     * 生成一个实体, 但不添加进世界里.
     *
     * @param location      待生成坐标.
     * @param type          实体类型.
     * @param randomizeData 是否随机实体数据(仅在1.17+版本生效).
     * @return 生成的实体.
     */
    @Nullable
    public static Entity createEntity(
            @NotNull Location location,
            @NotNull EntityType type,
            boolean randomizeData
    ) {
        RefEntity entity = createNmsEntity(location, type, randomizeData);
        return entity == null ? null : entity.getBukkitEntity();
    }

    /**
     * 在指定世界的指定坐标生成一个实体, 生成实体前对实体进行一些操作.
     *
     * @param location 待生成坐标.
     * @param type     实体类型.
     * @param function 生成前对实体执行的操作.
     * @return 生成的实体.
     */
    @Nullable
    public static Entity spawnEntity(
            @NotNull Location location,
            @NotNull EntityType type,
            @Nullable Consumer<Entity> function
    ) {
        return spawnEntity(location, type, true, function, CreatureSpawnEvent.SpawnReason.CUSTOM);
    }

    /**
     * 在指定世界的指定坐标生成一个实体, 生成实体前对实体进行一些操作.
     *
     * @param location      待生成坐标.
     * @param type          实体类型.
     * @param randomizeData 是否随机实体数据(仅在1.17+版本生效).
     * @param function      生成前对实体执行的操作.
     * @return 生成的实体.
     */
    @Nullable
    public static Entity spawnEntity(
            @NotNull Location location,
            @NotNull EntityType type,
            boolean randomizeData,
            @Nullable Consumer<Entity> function
    ) {
        return spawnEntity(location, type, randomizeData, function, CreatureSpawnEvent.SpawnReason.CUSTOM);
    }

    /**
     * 在指定世界的指定坐标生成一个实体, 生成实体前对实体进行一些操作.
     *
     * @param location      待生成坐标.
     * @param type          实体类型.
     * @param randomizeData 是否随机实体数据(仅在1.17+版本生效).
     * @param function      生成前对实体执行的操作.
     * @param spawnReason   生成原因.
     * @return 生成的实体.
     */
    @Nullable
    public static Entity spawnEntity(
            @NotNull Location location,
            @NotNull EntityType type,
            boolean randomizeData,
            @Nullable Consumer<Entity> function,
            @Nullable CreatureSpawnEvent.SpawnReason spawnReason
    ) {
        World world = location.getWorld();
        if (world == null) return null;
        RefCraftWorld craftWorld = (RefCraftWorld) (Object) world;

        RefEntity entity;
        if (RANDOMIZE_DATA_SUPPORT) {
            entity = craftWorld.createEntity(location, type.getEntityClass(), randomizeData);
        } else {
            entity = craftWorld.createEntity(location, type.getEntityClass());
        }
        Entity bukkitEntity = entity.getBukkitEntity();

        if (RANDOMIZE_DATA_SUPPORT) {
            if (JAVA_CONSUMER_SUPPORT) {
                craftWorld.addEntity(entity, spawnReason, function, randomizeData);
            } else {
                if (function != null) {
                    craftWorld.addEntity(entity, spawnReason, (org.bukkit.util.Consumer<Entity>) function::accept, randomizeData);
                } else {
                    craftWorld.addEntity(entity, spawnReason, (org.bukkit.util.Consumer<Entity>) null, randomizeData);
                }
            }
        } else {
            if (function != null) {
                craftWorld.addEntity(entity, spawnReason, function::accept);
            } else {
                craftWorld.addEntity(entity, spawnReason, null);
            }
        }
        return bukkitEntity;
    }

    /**
     * 通过实体ID获取对应的实体.
     *
     * @param world 实体所在世界.
     * @param id    实体ID.
     * @return 对应的实体.
     */
    @Nullable
    public static Entity getEntityFromID(
            @NotNull World world,
            int id
    ) {
        if ((Object) world instanceof RefCraftWorld) {
            RefEntity entity = getEntityFromIDByNms(((RefCraftWorld) (Object) world).getHandle(), id);
            if (entity != null) {
                return entity.getBukkitEntity();
            }
        }
        return null;
    }

    @Nullable
    protected static RefEntity getEntityFromIDByNms(
            @NotNull RefWorldServer world,
            int id
    ) {
        RefServerEntity serverEntity = null;
        if (REMOVED_TRACKER) {
            RefTrackedEntity trackedEntity = ((Map<Integer, RefTrackedEntity>) world.getChunkSource().chunkMap.entityMap).get(id);
            if (trackedEntity != null) {
                serverEntity = trackedEntity.serverEntity;
            }
        } else {
            serverEntity = world.tracker.trackedEntities.get(id);
        }
        if (serverEntity != null) {
            return serverEntity.entity;
        }
        return null;
    }

    /**
     * 生成一个实体, 但不添加进世界里.
     *
     * @param location 待生成坐标.
     * @param type     实体类型.
     * @return 生成的实体.
     */
    @Nullable
    protected static RefEntity createNmsEntity(
            @NotNull Location location,
            @NotNull EntityType type
    ) {
        return createNmsEntity(location, type, true);
    }

    /**
     * 生成一个实体, 但不添加进世界里.
     *
     * @param location      待生成坐标.
     * @param type          实体类型.
     * @param randomizeData 是否随机实体数据(仅在1.17+版本生效).
     * @return 生成的实体.
     */
    @Nullable
    protected static RefEntity createNmsEntity(
            @NotNull Location location,
            @NotNull EntityType type,
            boolean randomizeData
    ) {
        World world = location.getWorld();
        if (world == null) return null;
        RefCraftWorld craftWorld = (RefCraftWorld) (Object) world;

        RefEntity entity;
        if (RANDOMIZE_DATA_SUPPORT) {
            entity = craftWorld.createEntity(location, type.getEntityClass(), randomizeData);
        } else {
            entity = craftWorld.createEntity(location, type.getEntityClass());
        }
        return entity;
    }

    public static Entity getEntityFromServerEntity(
            @NotNull Object serverEntity
    ) {
        if (serverEntity instanceof RefServerEntity) {
            return ((RefServerEntity) serverEntity).entity.getBukkitEntity();
        }
        return null;
    }
}
