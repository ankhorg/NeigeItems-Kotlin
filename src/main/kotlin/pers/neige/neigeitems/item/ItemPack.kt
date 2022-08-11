package pers.neige.neigeitems.item

/**
 * 物品包信息
 *
 * @property id 物品包ID
 * @property items 物品包物品内容
 * @property fancyDrop 是否多彩掉落
 * @property offsetXString 多彩掉落横向偏移文本
 * @property offsetYString 多彩掉落纵向偏移文本
 * @property angleType 多彩掉落物品转角类型
 */
class ItemPack(
    val id: String,
    val items: List<String>,
    val fancyDrop: Boolean,
    val offsetXString: String? = null,
    val offsetYString: String? = null,
    val angleType: String? = null
) {
}