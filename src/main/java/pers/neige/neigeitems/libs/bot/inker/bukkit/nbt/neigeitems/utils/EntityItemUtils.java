package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils;

import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import pers.neige.neigeitems.ref.entity.RefCraftItem;
import pers.neige.neigeitems.ref.entity.RefEntityItem;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Item;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class EntityItemUtils {
    /**
     * 1.17+ 版本起, EntityItem 内部 despawnRate 字段即表示掉落物的最大存活时长.
     * 此前版本需要从掉落物所在世界实例中获取 SpigotWorldConfig, 获取世界配置中掉落物的最大存活时长.
     */
    private static final boolean HAS_DESPAWN_RATE = CbVersion.v1_17_R1.isSupport();
    /**
     * 1.13+ 版本起, owner 和 thrower 以 UUID 形式存储, 1.12.2 版本则为 String 形式的玩家名.
     */
    private static final boolean UUID_SUPPORT = CbVersion.v1_13_R1.isSupport();
    /**
     * 1.16.3+ 版本起, Item 类提供获取和设置 owner/thrower 的方法.
     * 1.16.2 和 1.16.3 都是 v1_16_R2, 所以按 v1_16_R3 起算.
     */
    private static final boolean GET_AND_SET_SUPPORT = CbVersion.v1_16_R3.isSupport();
    /**
     * 1.20.2+ 版本起, CraftItem 类移除私有字段 item.
     * 改为通过 getHandle 方法获取对应的 ItemEntity.
     */
    private static final boolean REMOVE_CRAFTITEM_ITEM = CbVersion.v1_20_R2.isSupport();

    private static RefEntityItem getHandle(
            RefCraftItem item
    ) {
        if (REMOVE_CRAFTITEM_ITEM) {
            return item.getHandle();
        } else {
            return item.item;
        }
    }

    /**
     * 判断给定的 Item 实体是否属于 CraftItem.
     *
     * @param item 待检测物品.
     * @return 是否属于 CraftItem.
     */
    public static boolean isCraftItem(
            Item item
    ) {
        return (Object) item instanceof RefCraftItem;
    }

    /**
     * 获取掉落物实体的已存活时长(tick).
     * 若该物品不属于 CraftItem 则返回 null.
     *
     * @param item 待检测物品.
     * @return 掉落物实体的已存活时长(tick).
     */
    @Nullable
    public static Integer getAge(
            @NotNull Item item
    ) {
        if ((Object) item instanceof RefCraftItem) {
            return getHandle((RefCraftItem) (Object) item).age;
        }
        return null;
    }

    /**
     * 设置掉落物实体的已存活时长(tick).
     *
     * @param item 待检测物品.
     * @param age  掉落物实体的已存活时长(tick).
     */
    public static void setAge(
            @NotNull Item item,
            int age
    ) {
        if ((Object) item instanceof RefCraftItem) {
            getHandle((RefCraftItem) (Object) item).age = age;
        }
    }

    /**
     * 获取掉落物实体的最大存活时长(tick).
     *
     * @param item 待检测物品.
     * @return 掉落物实体的最大存活时长(tick).
     */
    public static int getDespawnRate(
            @NotNull Item item
    ) {
        if (HAS_DESPAWN_RATE && ((Object) item instanceof RefCraftItem)) {
            return getHandle((RefCraftItem) (Object) item).despawnRate;
        } else {
            return WorldUtils.getDespawnRate(item.getWorld());
        }
    }

    /**
     * 获取掉落物实体的拥有者(不存在或无法获取则返回 null).
     * 1.12.2 的 owner 是 String, 1.13+ 的 owner 是 UUID, 所以返回 OfflinePlayer.
     *
     * @param item 待检测物品.
     * @return 物品拥有者.
     */
    @Nullable
    public static OfflinePlayer getOwner(
            @NotNull Item item
    ) {
        if (GET_AND_SET_SUPPORT) {
            UUID owner = item.getOwner();
            if (owner == null) return null;
            return Bukkit.getOfflinePlayer(owner);
        } else {
            if ((Object) item instanceof RefCraftItem) {
                if (UUID_SUPPORT) {
                    UUID ownerUUID = getHandle((RefCraftItem) (Object) item).ownerUUID;
                    if (ownerUUID == null) return null;
                    return Bukkit.getOfflinePlayer(ownerUUID);
                } else {
                    String ownerName = getHandle((RefCraftItem) (Object) item).ownerName;
                    if (ownerName == null) return null;
                    return Bukkit.getOfflinePlayer(ownerName);
                }
            }
        }
        return null;
    }

    /**
     * 获取掉落物实体的拥有者玩家名(不存在或无法获取则返回 null).
     * 1.13+ 版本永远返回 null.
     *
     * @param item 待检测物品.
     * @return 物品拥有者玩家名.
     */
    @Nullable
    public static String getOwnerName(
            @NotNull Item item
    ) {
        if ((Object) item instanceof RefCraftItem && !UUID_SUPPORT) {
            return getHandle((RefCraftItem) (Object) item).ownerName;
        }
        return null;
    }

    /**
     * 获取掉落物实体的拥有者UUID(不存在或无法获取则返回 null).
     * 1.12.2 版本永远返回 null.
     *
     * @param item 待检测物品.
     * @return 物品拥有者UUID.
     */
    @Nullable
    public static UUID getOwnerUUID(
            @NotNull Item item
    ) {
        if (GET_AND_SET_SUPPORT) {
            return item.getOwner();
        } else {
            if ((Object) item instanceof RefCraftItem && UUID_SUPPORT) {
                return getHandle((RefCraftItem) (Object) item).ownerUUID;
            }
        }
        return null;
    }

    /**
     * 设置掉落物实体的拥有者.
     * 1.12.2 的 owner 是 String, 1.13+ 的 owner 是 UUID, 所以需要 OfflinePlayer.
     *
     * @param item   待设置物品.
     * @param player 物品拥有者.
     */
    public static void setOwner(
            @NotNull Item item,
            @NotNull OfflinePlayer player
    ) {
        if (GET_AND_SET_SUPPORT) {
            item.setOwner(player.getUniqueId());
        } else {
            if ((Object) item instanceof RefCraftItem) {
                if (UUID_SUPPORT) {
                    getHandle((RefCraftItem) (Object) item).ownerUUID = player.getUniqueId();
                } else {
                    getHandle((RefCraftItem) (Object) item).ownerName = player.getName();
                }
            }
        }
    }

    /**
     * 设置掉落物实体的拥有者.
     * 1.13+ 版本调用该方法将不会产生任何效果.
     *
     * @param item 待设置物品.
     * @param name 物品拥有者玩家名.
     */
    public static void setOwnerName(
            @NotNull Item item,
            @NotNull String name
    ) {
        if ((Object) item instanceof RefCraftItem && !UUID_SUPPORT) {
            getHandle((RefCraftItem) (Object) item).ownerName = name;
        }
    }

    /**
     * 设置掉落物实体的拥有者.
     * 1.12.2 版本调用该方法将不会产生任何效果.
     *
     * @param item 待设置物品.
     * @param uuid 物品拥有者UUID.
     */
    public static void setOwnerUUID(
            @NotNull Item item,
            @NotNull UUID uuid
    ) {
        if (GET_AND_SET_SUPPORT) {
            item.setOwner(uuid);
        } else {
            if ((Object) item instanceof RefCraftItem && UUID_SUPPORT) {
                getHandle((RefCraftItem) (Object) item).ownerUUID = uuid;
            }
        }
    }

    /**
     * 获取掉落物实体的丢出者(不存在或无法获取则返回 null).
     * 1.12.2 的 thrower 是 String, 1.13+ 的 thrower 是 UUID, 所以返回 OfflinePlayer.
     *
     * @param item 待检测物品.
     * @return 物品丢出者.
     */
    @Nullable
    public static OfflinePlayer getThrower(
            @NotNull Item item
    ) {
        if (GET_AND_SET_SUPPORT) {
            UUID thrower = item.getThrower();
            if (thrower == null) return null;
            return Bukkit.getOfflinePlayer(thrower);
        } else {
            if ((Object) item instanceof RefCraftItem) {
                if (UUID_SUPPORT) {
                    UUID throwerUUID = getHandle((RefCraftItem) (Object) item).throwerUUID;
                    if (throwerUUID == null) return null;
                    return Bukkit.getOfflinePlayer(throwerUUID);
                } else {
                    String throwerName = getHandle((RefCraftItem) (Object) item).throwerName;
                    if (throwerName == null) return null;
                    return Bukkit.getOfflinePlayer(throwerName);
                }
            }
        }
        return null;
    }

    /**
     * 获取掉落物实体的丢出者玩家名(不存在或无法获取则返回 null).
     * 1.13+ 版本永远返回 null.
     *
     * @param item 待检测物品.
     * @return 物品丢出者玩家名.
     */
    @Nullable
    public static String getThrowerName(
            @NotNull Item item
    ) {
        if ((Object) item instanceof RefCraftItem && !UUID_SUPPORT) {
            return getHandle((RefCraftItem) (Object) item).throwerName;
        }
        return null;
    }

    /**
     * 获取掉落物实体的丢出者UUID(不存在或无法获取则返回 null).
     * 1.12.2 版本永远返回 null.
     *
     * @param item 待检测物品.
     * @return 物品丢出者UUID.
     */
    @Nullable
    public static UUID getThrowerUUID(
            @NotNull Item item
    ) {
        if (GET_AND_SET_SUPPORT) {
            return item.getThrower();
        } else {
            if ((Object) item instanceof RefCraftItem && UUID_SUPPORT) {
                return getHandle((RefCraftItem) (Object) item).throwerUUID;
            }
        }
        return null;
    }

    /**
     * 设置掉落物实体的丢出者.
     * 1.12.2 的 thrower 是 String, 1.13+ 的 thrower 是 UUID, 所以需要 OfflinePlayer.
     *
     * @param item   待设置物品.
     * @param player 物品丢出者.
     */
    public static void setThrower(
            @NotNull Item item,
            @NotNull OfflinePlayer player
    ) {
        if (GET_AND_SET_SUPPORT) {
            item.setThrower(player.getUniqueId());
        } else {
            if ((Object) item instanceof RefCraftItem) {
                if (UUID_SUPPORT) {
                    getHandle((RefCraftItem) (Object) item).throwerUUID = player.getUniqueId();
                } else {
                    getHandle((RefCraftItem) (Object) item).throwerName = player.getName();
                }
            }
        }
    }

    /**
     * 设置掉落物实体的丢出者.
     * 1.13+ 版本调用该方法将不会产生任何效果.
     *
     * @param item 待设置物品.
     * @param name 物品丢出者玩家名.
     */
    public static void setThrowerName(
            @NotNull Item item,
            @NotNull String name
    ) {
        if ((Object) item instanceof RefCraftItem && !UUID_SUPPORT) {
            getHandle((RefCraftItem) (Object) item).throwerName = name;
        }
    }

    /**
     * 设置掉落物实体的丢出者.
     * 1.12.2 版本调用该方法将不会产生任何效果.
     *
     * @param item 待设置物品.
     * @param uuid 物品丢出者UUID.
     */
    public static void setThrowerUUID(
            @NotNull Item item,
            @NotNull UUID uuid
    ) {
        if (GET_AND_SET_SUPPORT) {
            item.setThrower(uuid);
        } else {
            if ((Object) item instanceof RefCraftItem && UUID_SUPPORT) {
                getHandle((RefCraftItem) (Object) item).throwerUUID = uuid;
            }
        }
    }
}
