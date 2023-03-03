package pers.neige.neigeitems.script.tool

import org.bukkit.Bukkit
import org.bukkit.event.Event
import pers.neige.neigeitems.NeigeItems.bukkitScheduler
import pers.neige.neigeitems.NeigeItems.plugin
import pers.neige.neigeitems.manager.ExpansionManager
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.ProxyListener
import taboolib.common.platform.function.registerBukkitListener
import taboolib.common.platform.function.unregisterListener
import taboolib.platform.BukkitCommand
import java.util.function.Consumer

/**
 * bukkit监听器
 *
 * @property event 待监听事件
 * @constructor bukkit监听器
 */
class ScriptListener(val event: Class<Event>) {
    private var priority: EventPriority = EventPriority.NORMAL

    private var ignoreCancelled: Boolean = true

    private var executor: Consumer<Event> = Consumer<Event> {}

    private var listener: ProxyListener? = null

    /**
     * 设置监听优先级
     *
     * @param priority 监听优先级
     * @return ScriptListener本身
     */
    fun setPriority(priority: EventPriority): ScriptListener {
        this.priority = priority
        return this
    }

    /**
     * 设置忽略已取消事件
     *
     * @param ignoreCancelled 忽略已取消事件
     * @return ScriptListener本身
     */
    fun setIgnoreCancelled(ignoreCancelled: Boolean): ScriptListener {
        this.ignoreCancelled = ignoreCancelled
        return this
    }

    /**
     * 设置事件处理器
     *
     * @param executor 事件处理器
     * @return ScriptListener本身
     */
    fun setExecutor(executor: Consumer<Event>): ScriptListener {
        this.executor = executor
        return this
    }

    /**
     * 注册监听器
     *
     * @return ScriptListener本身
     */
    fun register(): ScriptListener {
        // 我没研究过能不能异步注册, 所以直接同步, 稳妥一点
        listener = if (Bukkit.isPrimaryThread()) {
            // 如果之前注册过了就先移除并卸载
            unRegister()
            // 注册监听器
            registerBukkitListener(event, priority, ignoreCancelled) {
                executor.accept(it)
            }
        } else {
            bukkitScheduler.callSyncMethod(plugin) {
                // 如果之前注册过了就先移除并卸载
                unRegister()
                // 注册监听器
                return@callSyncMethod registerBukkitListener(event, priority, ignoreCancelled) {
                    executor.accept(it)
                }
            }.get()
        }
        // 存入ExpansionManager, 插件重载时自动取消注册
        ExpansionManager.listeners.add(this)
        return this
    }

    /**
     * 卸载监听器
     *
     * @return ScriptListener本身
     */
    fun unRegister(): ScriptListener {
        // 注册了就取消监听
        if (Bukkit.isPrimaryThread()) {
            listener?.also {
                unregisterListener(it)
            }
        } else {
            bukkitScheduler.callSyncMethod(plugin) {
                listener?.also {
                    unregisterListener(it)
                }
            }
        }
        return this
    }
}