package pers.neige.neigeitems.item.action

import org.bukkit.configuration.ConfigurationSection
import pers.neige.neigeitems.action.Action
import pers.neige.neigeitems.action.ActionContext
import pers.neige.neigeitems.manager.ActionManager
import pers.neige.neigeitems.utils.SchedulerUtils

/**
 * 物品动作触发器
 *
 * @property id 物品ID
 * @property type 触发器类型
 * @property config 触发器配置
 */
class ActionTrigger(val id: String, val type: String, val config: ConfigurationSection) {
    /**
     * 获取物品使用冷却
     */
    val cooldown = config.getString("cooldown", "1000")

    /**
     * 获取tick型触发器触发间隔
     */
    val tick = config.getString("tick", "10")

    /**
     * 获取物品冷却组ID
     */
    val group: String = config.getString("group") ?: "${type}_$id"

    /**
     * 获取物品消耗信息
     */
    val consume: ConsumeInfo? = config.getConfigurationSection("consume")?.let { ConsumeInfo(it) }

    /**
     * 获取异步动作信息
     */
    val actions: Action = ActionManager.compile(config.get("actions"))

    /**
     * 获取异步动作信息
     */
    val async: Action = ActionManager.compile(config.get("async"))

    /**
     * 获取同步动作信息
     */
    val sync: Action = ActionManager.compile(config.get("sync"))

    /**
     * 运行异步物品动作
     *
     * @param context 动作上下文
     */
    fun run(
        context: ActionContext
    ) {
        async(context)
        sync(context)
    }

    /**
     * 运行异步物品动作
     *
     * @param context 动作上下文
     */
    fun async(
        context: ActionContext
    ) {
        SchedulerUtils.async {
            actions.eval(context)
            async.eval(context)
        }
    }

    /**
     * 运行同步物品动作
     *
     * @param context 动作上下文
     */
    fun sync(
        context: ActionContext
    ) {
        SchedulerUtils.sync {
            sync.eval(context)
        }
    }
}