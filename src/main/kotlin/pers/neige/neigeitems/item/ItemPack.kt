package pers.neige.neigeitems.item

class ItemPack(
    val id: String,
    val items: List<String>,
    val fancyDrop: Boolean,
    val offsetXString: String? = null,
    val offsetYString: String? = null,
    val angleType: String? = null
) {
}