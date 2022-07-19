package pers.neige.neigeitems.utils

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.metadata.FixedMetadataValue
import pers.neige.neigeitems.NeigeItems.plugin
import taboolib.platform.util.giveItem

object PlayerUtils {

    // 给予玩家一定数量的物品
    @JvmStatic
    fun Player.giveItems(itemStack: ItemStack, amount: Int? = null) {
        amount?.let {
            val maxStackSize = itemStack.maxStackSize
            itemStack.amount = maxStackSize
            val leftAmount = amount % maxStackSize
            val repeat = amount / maxStackSize
            giveItem(itemStack, repeat)
            if (leftAmount != 0) {
                itemStack.amount = leftAmount
                giveItem(itemStack)
            }
        } ?: giveItem(itemStack)
    }

    // 获取玩家Metadata
    @JvmStatic
    fun Player.getMetadataEZ(key: String, type: String, def: Any): Any? {
        if(!this.hasMetadata(key)) {
            this.setMetadataEZ(key, def)
            return def
        }
        return when (type) {
            "Boolean" -> this.getMetadata(key)[0].asBoolean()
            "Byte" -> this.getMetadata(key)[0].asByte()
            "Double" -> this.getMetadata(key)[0].asDouble()
            "Float" -> this.getMetadata(key)[0].asFloat()
            "Int" -> this.getMetadata(key)[0].asInt()
            "Long" -> this.getMetadata(key)[0].asLong()
            "Short" -> this.getMetadata(key)[0].asShort()
            "String" -> this.getMetadata(key)[0].asString()
            else -> this.getMetadata(key)[0].value()
        }
    }

    // 设置玩家Metadata
    @JvmStatic
    fun Player.setMetadataEZ(key: String, value: Any) {
        this.setMetadata(key, FixedMetadataValue(plugin, value))
    }
}