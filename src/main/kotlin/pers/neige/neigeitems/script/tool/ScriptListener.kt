package pers.neige.neigeitems.script.tool

import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import pers.neige.neigeitems.NeigeItems
import pers.neige.neigeitems.manager.ExpansionManager
import pers.neige.neigeitems.utils.ListenerUtils
import pers.neige.neigeitems.utils.SchedulerUtils.sync
import pers.neige.neigeitems.utils.SchedulerUtils.syncAndGet
import java.util.function.Consumer

/**
 * Bukkit 监听器
 *
 * @property event 待监听事件
 * @constructor Bukkit 监听器
 */
class ScriptListener(private val event: Class<Event>) {
    private var priority: EventPriority = EventPriority.NORMAL

    private var ignoreCancelled: Boolean = true

    private var executor: Consumer<Event> = Consumer<Event> {}

    private var listener: Listener? = null

    private var plugin: Plugin = NeigeItems.getInstance()

    /**
     * 设置注册监听器的插件
     *
     * @param plugin 任务
     * @return ScriptListener 本身
     */
    fun setPlugin(plugin: Plugin): ScriptListener {
        this.plugin = plugin
        return this
    }

    /**
     * 设置监听优先级
     *
     * @param priority 监听优先级
     * @return ScriptListener 本身
     */
    fun setPriority(priority: EventPriority): ScriptListener {
        this.priority = priority
        return this
    }

    /**
     * 设置忽略已取消事件
     *
     * @param ignoreCancelled 忽略已取消事件
     * @return ScriptListener 本身
     */
    fun setIgnoreCancelled(ignoreCancelled: Boolean): ScriptListener {
        this.ignoreCancelled = ignoreCancelled
        return this
    }

    /**
     * 设置事件处理器
     *
     * @param executor 事件处理器
     * @return ScriptListener 本身
     */
    fun setExecutor(executor: Consumer<Event>): ScriptListener {
        this.executor = executor
        return this
    }

    /**
     * 注册监听器
     *
     * @return ScriptListener 本身
     */
    fun register(): ScriptListener {
        // HandlerList是非线程安全的, 需要同步注册
        listener = syncAndGet {
            // 如果之前注册过了就先移除并卸载
            unregister()
            ListenerUtils.registerListener(
                event,
                priority,
                plugin,
                ignoreCancelled,
                executor
            )
        }
        // 存入ExpansionManager, 插件重载时自动取消注册
        ExpansionManager.listeners.add(this)
        return this
    }

    /**
     * 卸载监听器
     *
     * @return ScriptListener 本身
     */
    fun unregister(): ScriptListener {
        // 注册了就取消监听
        sync {
            ListenerUtils.unregisterListener(listener)
        }
        return this
    }
}