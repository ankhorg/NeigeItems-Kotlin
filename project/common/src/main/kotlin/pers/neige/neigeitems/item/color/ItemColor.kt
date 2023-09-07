package pers.neige.neigeitems.item.color

import org.bukkit.ChatColor

/**
 * 掉落物光效系统
 */
abstract class ItemColor {
    /**
     * 获取物品光效实现模式(Vanilla/Protocol)
     */
    abstract val mode: String

    companion object {
        /**
         * 获取物品光效颜色对应Map
         */
        val colors = let {
            val hashMap = HashMap<String, ChatColor>()
            hashMap["BLACK"] = ChatColor.BLACK
            hashMap["DARK_BLUE"] = ChatColor.DARK_BLUE
            hashMap["DARK_GREEN"] = ChatColor.DARK_GREEN
            hashMap["DARK_AQUA"] = ChatColor.DARK_AQUA
            hashMap["DARK_RED"] = ChatColor.DARK_RED
            hashMap["DARK_PURPLE"] = ChatColor.DARK_PURPLE
            hashMap["GOLD"] = ChatColor.GOLD
            hashMap["GRAY"] = ChatColor.GRAY
            hashMap["DARK_GRAY"] = ChatColor.DARK_GRAY
            hashMap["BLUE"] = ChatColor.BLUE
            hashMap["GREEN"] = ChatColor.GREEN
            hashMap["AQUA"] = ChatColor.AQUA
            hashMap["RED"] = ChatColor.RED
            hashMap["LIGHT_PURPLE"] = ChatColor.LIGHT_PURPLE
            hashMap["YELLOW"] = ChatColor.YELLOW
            hashMap["WHITE"] = ChatColor.WHITE
            hashMap
        }
    }
}