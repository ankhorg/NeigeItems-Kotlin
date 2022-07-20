package pers.neige.neigeitems.item

import taboolib.module.nms.ItemTag

// 用于在判断NI物品后返回NI物品信息, 详见ItemUtils#isNiItem
class ItemInfo(
    val itemTag: ItemTag,
    val neigeItems: ItemTag,
    val id: String
) {}