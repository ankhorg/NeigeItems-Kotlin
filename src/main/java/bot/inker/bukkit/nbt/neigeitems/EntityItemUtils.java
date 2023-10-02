package bot.inker.bukkit.nbt.neigeitems;

import bot.inker.bukkit.nbt.internal.annotation.CbVersion;
import bot.inker.bukkit.nbt.internal.ref.RefCraftItem;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Item;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class EntityItemUtils {
    private static final boolean UUID_OWNER_SUPPORT = CbVersion.v1_13_R1.isSupport();
    private static final boolean SET_OWNER_SUPPORT = CbVersion.v1_16_R3.isSupport();

    /**
     * 获取掉落物实体的已存活时长(tick).
     * 若该物品不属于 CraftItem 则返回 null.
     *
     * @param item 待检测物品.
     * @return 掉落物实体的已存活时长(tick).
     */
    @Nullable
    public static Integer getAge(@NotNull Item item) {
        if ((Object) item instanceof RefCraftItem) {
            return ((RefCraftItem) (Object) item).item.age;
        }
        return null;
    }

    /**
     * 设置掉落物实体的已存活时长(tick).
     *
     * @param item 待检测物品.
     * @param age 掉落物实体的已存活时长(tick).
     */
    public static void setAge(@NotNull Item item, int age) {
        if ((Object) item instanceof RefCraftItem) {
            ((RefCraftItem) (Object) item).item.age = age;
        }
    }

    /**
     * 获取掉落物实体的最大存活时长(tick).
     *
     * @param item 待检测物品.
     * @return 掉落物实体的最大存活时长(tick).
     */
    public static int getDespawnRate(@NotNull Item item) {
        return WorldUtils.getDespawnRate(item.getWorld());
    }

    /**
     * 获取掉落物实体的拥有者(不存在或无法获取则返回 null).
     *
     * @param item 待设置物品.
     * @return 物品拥有者.
     */
    @Nullable
    public static OfflinePlayer getOwner(@NotNull Item item) {
        if (SET_OWNER_SUPPORT) {
            UUID owner = item.getOwner();
            if (owner == null) return null;
            return Bukkit.getOfflinePlayer(owner);
        } else {
            if ((Object) item instanceof RefCraftItem) {
                if (UUID_OWNER_SUPPORT) {
                    UUID ownerUUID = ((RefCraftItem) (Object) item).item.ownerUUID;
                    if (ownerUUID == null) return null;
                    return Bukkit.getOfflinePlayer(ownerUUID);
                } else {
                    String ownerName = ((RefCraftItem) (Object) item).item.ownerName;
                    if (ownerName == null) return null;
                    return Bukkit.getOfflinePlayer(ownerName);
                }
            }
        }
        return null;
    }

    /**
     * 设置掉落物实体的拥有者.
     *
     * @param item 待设置物品.
     * @param player 物品拥有者.
     */
    public static void setOwner(@NotNull Item item, @NotNull OfflinePlayer player) {
        if (SET_OWNER_SUPPORT) {
            item.setOwner(player.getUniqueId());
        } else {
            if ((Object) item instanceof RefCraftItem) {
                if (UUID_OWNER_SUPPORT) {
                    ((RefCraftItem) (Object) item).item.ownerUUID = player.getUniqueId();
                } else {
                    ((RefCraftItem) (Object) item).item.ownerName = player.getName();
                }
            }
        }
    }

    /**
     * 获取掉落物实体的丢出者(不存在或无法获取则返回 null).
     *
     * @param item 待设置物品.
     * @return 物品丢出者.
     */
    @Nullable
    public static OfflinePlayer getThrower(@NotNull Item item) {
        if (SET_OWNER_SUPPORT) {
            UUID thrower = item.getThrower();
            if (thrower == null) return null;
            return Bukkit.getOfflinePlayer(thrower);
        } else {
            if ((Object) item instanceof RefCraftItem) {
                if (UUID_OWNER_SUPPORT) {
                    UUID throwerUUID = ((RefCraftItem) (Object) item).item.throwerUUID;
                    if (throwerUUID == null) return null;
                    return Bukkit.getOfflinePlayer(throwerUUID);
                } else {
                    String throwerName = ((RefCraftItem) (Object) item).item.throwerName;
                    if (throwerName == null) return null;
                    return Bukkit.getOfflinePlayer(throwerName);
                }
            }
        }
        return null;
    }

    /**
     * 设置掉落物实体的丢出者.
     *
     * @param item 待设置物品.
     * @param player 物品丢出者.
     */
    public static void setThrower(@NotNull Item item, @NotNull OfflinePlayer player) {
        if (SET_OWNER_SUPPORT) {
            item.setThrower(player.getUniqueId());
        } else {
            if ((Object) item instanceof RefCraftItem) {
                if (UUID_OWNER_SUPPORT) {
                    ((RefCraftItem) (Object) item).item.throwerUUID = player.getUniqueId();
                } else {
                    ((RefCraftItem) (Object) item).item.throwerName = player.getName();
                }
            }
        }
    }
}
