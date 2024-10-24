package pers.neige.neigeitems.item;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
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
    @NotNull
    private final ItemStack itemStack;
    @NotNull
    private final NbtItemStack nbtItemStack;
    @NotNull
    private final NbtCompound itemTag;
    @NotNull
    private final NbtCompound neigeItems;
    @Nullable
    private final Nbt<?> dataNbt;
    @NotNull
    private final String id;
    @Nullable
    private volatile HashMap<String, String> data;

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
            @NotNull ItemStack itemStack,
            @NotNull NbtItemStack nbtItemStack,
            @NotNull NbtCompound itemTag,
            @NotNull NbtCompound neigeItems,
            @NotNull String id,
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

    private static HashMap<String, String> toFlatStringMap(@NotNull NbtCompound compound) {
        HashMap<String, String> result = new HashMap<>();
        toFlatStringMap(compound, "", result);
        return result;
    }

    private static void toFlatStringMap(
            @NotNull NbtCompound compound,
            @NotNull String keyPrefix,
            @NotNull HashMap<String, String> result
    ) {
        compound.forEach((key, nbt) -> {
            if (nbt instanceof NbtCompound) {
                toFlatStringMap((NbtCompound) nbt, keyPrefix + key + ".", result);
            } else {
                result.put(keyPrefix + key, nbt.getAsString());
            }
        });
    }

    @NotNull
    public ItemStack getItemStack() {
        return this.itemStack;
    }

    @NotNull
    public NbtItemStack getNbtItemStack() {
        return this.nbtItemStack;
    }

    @NotNull
    public NbtCompound getItemTag() {
        return this.itemTag;
    }

    @NotNull
    public NbtCompound getNeigeItems() {
        return this.neigeItems;
    }

    @NotNull
    public String getId() {
        return this.id;
    }

    @NotNull
    public HashMap<String, String> getData() {
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

    @Nullable
    public String getDataValue(@NotNull String dataKey) {
        if (this.dataNbt instanceof NbtCompound) {
            return ((NbtCompound) dataNbt).getDeepString(dataKey);
        } else {
            return getData().get(dataKey);
        }
    }
}
