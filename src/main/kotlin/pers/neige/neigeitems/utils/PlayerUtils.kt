package pers.neige.neigeitems.utils

import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.metadata.Metadatable
import pers.neige.neigeitems.NeigeItems

/**
 * 玩家相关工具类
 */
object PlayerUtils {
    /**
     * 给予玩家物品
     * @param itemStack 待给予物品
     */
    @JvmStatic
    fun Player.giveItem(itemStack: ItemStack) {
        if (itemStack.type != Material.AIR) {
            inventory.addItem(itemStack).values.forEach { world.dropItem(location, it) }
        }
    }

    /**
     * 重复给予玩家物品一定次数
     * @param itemStack 待给予物品
     * @param repeat 重复次数
     */
    @JvmStatic
    fun Player.giveItem(itemStack: ItemStack, repeat: Int) {
        if (itemStack.type != Material.AIR) {
            // CraftInventory.addItem 的执行过程中, 实质上有可能修改ItemStack的amount, 如果不注意这一点, 则会吞物品而不自知
            val preAmount = itemStack.amount
            repeat(repeat) {
                inventory.addItem(itemStack).values.forEach { world.dropItem(location, it) }
                itemStack.amount = preAmount
            }
        }
    }

    /**
     * 给予玩家一定数量的物品
     * @param itemStack 待给予物品
     * @param amount 给予数量
     */
    @JvmStatic
    fun Player.giveItems(itemStack: ItemStack, amount: Int?) {
        val preAmount = itemStack.amount
        // 最大堆叠
        val maxStackSize = itemStack.maxStackSize
        // 整组给予数量
        val repeat = (amount ?: 1) / maxStackSize
        // 单独给予数量
        val leftAmount = (amount ?: 1) % maxStackSize
        // 整组给予
        if (repeat > 0) {
            repeat(repeat) {
                itemStack.amount = maxStackSize
                inventory.addItem(itemStack).values.forEach { world.dropItem(location, it) }
            }
        }
        // 单独给予
        if (leftAmount > 0) {
            itemStack.amount = leftAmount
            inventory.addItem(itemStack).values.forEach { world.dropItem(location, it) }
        }
        itemStack.amount = preAmount
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
    @Deprecated("没有意义")
    fun Metadatable.getMetadataEZ(key: String, type: String, def: Any): Any? {
        if (!this.hasMetadata(key)) {
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
     * 获取Metadata, 不含对应Metadata将设置并返回默认值
     *
     * @param key Metadata键
     * @param def 默认值
     * @return Metadata值
     */
    @JvmStatic
    fun Metadatable.getMetadataEZ(key: String, def: Any): Any? {
        if (!this.hasMetadata(key)) {
            this.setMetadataEZ(key, def)
            return def
        }
        return this.getMetadata(key)[0].value()
    }

    /**
     * 设置Metadata
     *
     * @param key Metadata键
     * @param value Metadata值
     */
    @JvmStatic
    fun Metadatable.setMetadataEZ(key: String, value: Any) {
        this.setMetadata(key, FixedMetadataValue(NeigeItems.getInstance(), value))
    }

    @JvmStatic
    fun Player.sendMessage(builder: ComponentBuilder) {
        this.spigot().sendMessage(*builder.create())
    }

    @JvmStatic
    fun Player.sendActionBar(message: String) {
        this.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent(message))
    }
}