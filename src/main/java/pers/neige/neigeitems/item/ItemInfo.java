package pers.neige.neigeitems.item;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
    @NotNull
    private final String id;
    @Nullable
    private HashMap<String, String> data;

    /**
     * 用于在判断NI物品后返回NI物品信息, 详见ItemUtils#isNiItem
     *
     * @param itemStack 物品本身
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
        this.id = id;
        this.data = data;
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
            String dataString = this.neigeItems.getString("data");
            if (dataString != null) {
                this.data = JsonUtils.toMap(dataString);
            }
        }
        return this.data;
    }
}
