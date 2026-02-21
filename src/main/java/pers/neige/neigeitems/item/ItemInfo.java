package pers.neige.neigeitems.item;

import lombok.NonNull;
import lombok.val;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.Nbt;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtCompound;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtItemStack;
import pers.neige.neigeitems.utils.JsonUtils;

import java.util.HashMap;

/**
 * 用于在判断NI物品后返回NI物品信息, 详见ItemUtils#isNiItem
 */
public final class ItemInfo {
    private final @NonNull ItemStack itemStack;
    private final @NonNull NbtItemStack nbtItemStack;
    private final @NonNull NbtCompound itemTag;
    private final @NonNull NbtCompound neigeItems;
    private final @Nullable Nbt<?> dataNbt;
    private final @NonNull String id;
    private volatile @Nullable HashMap<String, String> data;

    /**
     * 用于在判断NI物品后返回NI物品信息, 详见ItemUtils#isNiItem
     *
     * @param itemStack    物品本身
     * @param nbtItemStack NbtItemStack
     * @param itemTag      物品NBT
     * @param neigeItems   NI物品特殊NBT
     * @param id           物品ID
     * @param data         指向数据
     */
    public ItemInfo(
        @NonNull ItemStack itemStack,
        @NonNull NbtItemStack nbtItemStack,
        @NonNull NbtCompound itemTag,
        @NonNull NbtCompound neigeItems,
        @NonNull String id,
        @Nullable HashMap<String, String> data
    ) {
        this.itemStack = itemStack;
        this.nbtItemStack = nbtItemStack;
        this.itemTag = itemTag;
        this.neigeItems = neigeItems;
        this.dataNbt = neigeItems.get("data");
        this.id = id;
        this.data = data;
    }

    private static @NonNull HashMap<String, String> toFlatStringMap(@NonNull NbtCompound compound) {
        val result = new HashMap<String, String>();
        toFlatStringMap(compound, "", result);
        return result;
    }

    private static void toFlatStringMap(
        @NonNull NbtCompound compound,
        @NonNull String keyPrefix,
        @NonNull HashMap<String, String> result
    ) {
        compound.forEach((key, nbt) -> {
            if (nbt instanceof NbtCompound) {
                toFlatStringMap((NbtCompound) nbt, keyPrefix + key + ".", result);
            } else {
                result.put(keyPrefix + key, nbt.getAsString());
            }
        });
    }

    public @NonNull ItemStack getItemStack() {
        return this.itemStack;
    }

    public @NonNull NbtItemStack getNbtItemStack() {
        return this.nbtItemStack;
    }

    public @NonNull NbtCompound getItemTag() {
        return this.itemTag;
    }

    public @NonNull NbtCompound getNeigeItems() {
        return this.neigeItems;
    }

    public @NonNull String getId() {
        return this.id;
    }

    public @NonNull HashMap<String, String> getData() {
        if (this.data == null) {
            synchronized (this) {
                if (this.data == null) {
                    if (dataNbt instanceof NbtCompound) {
                        this.data = toFlatStringMap((NbtCompound) dataNbt);
                    } else if (dataNbt != null) {
                        this.data = JsonUtils.toMap(dataNbt.getAsString());
                    } else {
                        this.data = new HashMap<>();
                    }
                }
            }
        }
        return this.data;
    }

    public @Nullable String getDataValue(@NonNull String dataKey) {
        if (this.dataNbt instanceof NbtCompound) {
            return ((NbtCompound) dataNbt).getDeepString(dataKey);
        } else {
            return getData().get(dataKey);
        }
    }
}
