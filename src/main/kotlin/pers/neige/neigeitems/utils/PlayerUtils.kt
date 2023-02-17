package pers.neige.neigeitems.utils

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.metadata.Metadatable
import pers.neige.neigeitems.NeigeItems.plugin
import taboolib.platform.util.giveItem

/**
 * 玩家相关工具类
 */
object PlayerUtils {
    /**
     * 给予玩家一定数量的物品
     * @param itemStack 待给予物品
     * @param amount 给予数量
     */
    @JvmStatic
    fun Player.giveItems(itemStack: ItemStack, amount: Int?) {
        // 最大堆叠
        val maxStackSize = itemStack.maxStackSize
        // 整组给予数量
        val repeat = (amount ?: 1) / maxStackSize
        // 单独给予数量
        val leftAmount = (amount ?: 1) % maxStackSize
        // 整组给予
        if (repeat > 0) {
            val cloneItemStack = itemStack.clone()
            cloneItemStack.amount = maxStackSize
            repeat(repeat) {
                inventory.addItem(cloneItemStack).values.forEach { world.dropItem(location, it) }
            }
        }
        // 单独给予
        if (leftAmount > 0) {
            val itemLeft = itemStack.clone()
            itemLeft.amount = leftAmount
            giveItem(itemLeft)
        }
    }

    /**
     * 获取Metadata, 不含对应Metadata将设置并返回默认值
     *
     * @param key Metadata键
     * @param type Metadata类型
     * @param def 默认值
     * @return Metadata值
     */
    @JvmStatic
    fun Metadatable.getMetadataEZ(key: String, type: String, def: Any): Any? {
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

    /**
     * 设置Metadata
     *
     * @param key Metadata键
     * @param value Metadata值
     */
    @JvmStatic
    fun Metadatable.setMetadataEZ(key: String, value: Any) {
        this.setMetadata(key, FixedMetadataValue(plugin, value))
    }
}