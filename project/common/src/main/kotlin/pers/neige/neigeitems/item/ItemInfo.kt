package pers.neige.neigeitems.item

import bot.inker.bukkit.nbt.NbtCompound
import bot.inker.bukkit.nbt.NbtItemStack

/**
 * 用于在判断NI物品后返回NI物品信息, 详见ItemUtils#isNiItem
 *
 * @property nbtItemStack NbtItemStack
 * @property itemTag 物品NBT
 * @property neigeItems NI物品特殊NBT
 * @property id 物品ID
 * @property data 指向数据
 */
class ItemInfo(
    val nbtItemStack: NbtItemStack,
    val itemTag: NbtCompound,
    val neigeItems: NbtCompound,
    val id: String,
    val data: HashMap<String, String>?,
)