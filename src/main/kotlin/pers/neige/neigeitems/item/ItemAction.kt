package pers.neige.neigeitems.item

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import pers.neige.neigeitems.manager.ActionManager
import taboolib.module.nms.ItemTag

/**
 * 物品动作信息
 *
 * @property id 物品ID
 * @property config 物品动作配置
 * @constructor 根据ID及配置加载对应物品的物品动作信息
 */
class ItemAction(val id: String, val config: ConfigurationSection) {
    /**
     * 获取物品消耗信息
     */
    val consume: ConfigurationSection? = config.getConfigurationSection("consume")

    /**
     * 获取物品使用冷却
     */
    val cooldown = config.getLong("cooldown", 0)

    /**
     * 获取物品冷却组ID
     */
    val group = config.getString("group", id)

    /**
     * 获取使用物品左键交互触发的物品动作, 不存在则返回null
     */
    val left = config.get("left")

    /**
     * 获取使用物品右键交互触发的物品动作, 不存在则返回null
     */
    val right = config.get("right")

    /**
     * 获取使用物品左键或右键交互触发的物品动作, 不存在则返回null
     */
    val all = config.get("all")

    /**
     * 获取食用或饮用物品触发的物品动作, 不存在则返回null
     */
    val eat = config.get("eat")

    /**
     * 获取丢弃物品触发的物品动作, 不存在则返回null
     */
    val drop = config.get("drop")

    /**
     * 获取捡起物品触发的物品动作, 不存在则返回null
     */
    val pick = config.get("pick")

    /**
     * 运行某个动作
     *
     * @param player 待操作玩家
     * @param action 动作类型
     * @param itemTag 物品NBT
     */
    fun run(player: Player, action: Any?, itemTag: ItemTag?) {
        action?.let {
            when (action) {
                is String -> ActionManager.runAction(player, action, itemTag)
                is List<*> -> ActionManager.runAction(player, action.map { value -> value.toString() }, itemTag)
                else -> return
            }
        }
    }
}