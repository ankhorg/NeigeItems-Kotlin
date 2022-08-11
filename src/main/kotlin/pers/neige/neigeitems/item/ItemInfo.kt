package pers.neige.neigeitems.item

import taboolib.module.nms.ItemTag

/**
 * 用于在判断NI物品后返回NI物品信息, 详见ItemUtils#isNiItem
 *
 * @property itemTag 物品NBT
 * @property neigeItems NI物品特殊NBT
 * @property id 物品ID
 */
class ItemInfo(
    val itemTag: ItemTag,
    val neigeItems: ItemTag,
    val id: String
) {}