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
import pers.neige.neigeitems.manager.UserManager

/**
 * 玩家相关工具类
 */
object PlayerUtils {
    /**
     * 给予玩家物品.
     *
     * @param itemStack 待给予物品
     */
    @JvmStatic
    fun Player.giveItem(itemStack: ItemStack) {
        if (itemStack.type != Material.AIR) {
            inventory.addItem(itemStack).values.forEach { world.dropItem(location, it) }
        }
    }

    /**
     * 重复给予玩家物品一定次数.
     *
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
     * 给予玩家一定数量的物品.
     *
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
     * 检测是否存在对应Metadata.
     *
     * @param key Metadata键
     * @return Metadata值
     */
    @JvmStatic
    fun Player.hasMetadataEZ(key: String): Boolean {
        val user = UserManager.INSTANCE[uniqueId] ?: return false
        return user.metadata.containsKey(key)
    }

    /**
     * 获取Metadata, 不含对应Metadata将返回默认值.
     *
     * @param key Metadata键
     * @param def 默认值
     * @return Metadata值
     */
    @JvmStatic
    fun Player.getMetadataEZ(key: String, def: Any): Any? {
        val user = UserManager.INSTANCE[uniqueId] ?: return def
        return user.metadata.getOrDefault(key, def)
    }

    /**
     * 设置Metadata.
     *
     * @param key Metadata键
     * @param value Metadata值
     */
    @JvmStatic
    fun Player.setMetadataEZ(key: String, value: Any) {
        val user = UserManager.INSTANCE[uniqueId] ?: return
        user.metadata[key] = value
    }

    /**
     * 获取Metadata, 不含对应Metadata将返回默认值.
     *
     * @param key Metadata键
     * @param def 默认值
     * @return Metadata值
     */
    @JvmStatic
    fun Metadatable.getMetadataEZ(key: String, def: Any): Any? {
        if (!this.hasMetadata(key)) {
            return def
        }
        return this.getMetadata(key)[0].value()
    }

    /**
     * 设置Metadata.
     *
     * @param key Metadata键
     * @param value Metadata值
     */
    @JvmStatic
    fun Metadatable.setMetadataEZ(key: String, value: Any) {
        this.setMetadata(key, FixedMetadataValue(NeigeItems.getInstance(), value))
    }

    /**
     * 向玩家发送聊天栏消息.
     *
     * @param builder 待发送消息
     */
    @JvmStatic
    fun Player.sendMessage(builder: ComponentBuilder) {
        this.spigot().sendMessage(*builder.create())
    }

    /**
     * 向玩家发送动作栏消息.
     *
     * @param message 待发送消息
     */
    @JvmStatic
    fun Player.sendActionBar(message: String) {
        this.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent(message))
    }

    /**
     * 检测冷却状态.
     * 冷却完成则重新设置冷却并返回0.
     * 冷却未完成则返回剩余时间.
     *
     * @param key      冷却组ID
     * @param cooldown 冷却刷新时间
     * @return 剩余冷却时间
     */
    @JvmStatic
    fun Player.checkCooldown(key: String, cooldown: Long): Long {
        val user = UserManager.INSTANCE[uniqueId] ?: return Long.MAX_VALUE
        return user.checkCooldown(key, cooldown)
    }

    /**
     * 返回剩余冷却时间.
     *
     * @param key      冷却组ID
     * @param cooldown 冷却刷新时间
     * @return 剩余冷却时间
     */
    @JvmStatic
    fun Player.getCooldown(key: String, cooldown: Long): Long {
        val user = UserManager.INSTANCE[uniqueId] ?: return Long.MAX_VALUE
        return user.getCooldown(key, cooldown)
    }

    /**
     * 设置进入冷却状态.
     *
     * @param key      冷却组ID
     * @param cooldown 冷却刷新时间
     */
    @JvmStatic
    fun Player.setCooldown(key: String, cooldown: Long) {
        val user = UserManager.INSTANCE[uniqueId] ?: return
        user.setCooldown(key, cooldown)
    }
}