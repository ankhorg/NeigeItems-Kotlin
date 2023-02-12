package pers.neige.neigeitems.item.action

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.inventory.ItemStack
import taboolib.module.nms.ItemTag
import taboolib.module.nms.getItemTag
import java.util.*

/**
 * 物品动作信息
 *
 * @property id 物品ID
 * @property config 物品动作配置
 * @constructor 根据ID及配置加载对应物品的物品动作信息
 */
class ItemAction(val id: String, val config: ConfigurationSection) {
    /**
     * 获取所有动作触发器
     */
    val triggers = HashMap<String, ActionTrigger?>()

    /**
     * 该物品是否有左键触发的物品动作
     */
    var hasLeftAction = false

    /**
     * 该物品是否有右键触发的物品动作
     */
    var hasRightAction = false

    /**
     * 该物品是否有左键触发的物品动作
     */
    var hasShiftLeftAction = false

    /**
     * 该物品是否有右键触发的物品动作
     */
    var hasShiftRightAction = false

    init {
        // 加载动作触发器
        config.getKeys(false).forEach { trigger ->
            config.getConfigurationSection(trigger)?.let {
                triggers[trigger.lowercase(Locale.getDefault())] = ActionTrigger(id, trigger, it)
            }
        }
        if (triggers.contains("left") || triggers.contains("all")) {
            hasLeftAction = true
        }
        if (triggers.contains("right") || triggers.contains("all")) {
            hasRightAction = true
        }
        if (triggers.contains("shift_left") || triggers.contains("shift_all")) {
            hasShiftLeftAction = true
        }
        if (triggers.contains("shift_right") || triggers.contains("shift_all")) {
            hasShiftRightAction = true
        }
    }

    /**
     * 运行某个动作
     *
     * @param player 待操作玩家
     * @param trigger 触发器
     * @param itemStack 触发物品
     * @param itemTag 物品NBT
     * @param event 触发事件
     */
    fun run(
        player: Player,
        trigger: ActionTrigger?,
        itemStack: ItemStack,
        itemTag: ItemTag? = itemStack.getItemTag(),
        data: HashMap<String, String>? = null,
        event: Event,
        global: HashMap<String, Any?>
    ) {
        trigger?.run(
            player,
            itemStack,
            itemTag,
            data,
            event,
            global
        )
    }

    /**
     * 运行某个动作
     *
     * @param player 待操作玩家
     * @param trigger 触发类型
     * @param itemStack 触发物品
     * @param itemTag 物品NBT
     * @param event 触发事件
     */
    fun run(
        player: Player,
        trigger: String?,
        itemStack: ItemStack,
        itemTag: ItemTag? = itemStack.getItemTag(),
        data: HashMap<String, String>? = null,
        event: Event,
        global: HashMap<String, Any?>
    ) {
        triggers[trigger]?.run(
            player,
            itemStack,
            itemTag,
            data,
            event,
            global
        )
    }
}