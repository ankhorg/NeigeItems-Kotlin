package pers.neige.neigeitems.script.tool

import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import pers.neige.neigeitems.NeigeItems.bukkitScheduler
import pers.neige.neigeitems.NeigeItems.plugin
import pers.neige.neigeitems.manager.ExpansionManager

/**
 * bukkit任务
 *
 * @constructor bukkit监听器
 */
class ScriptTask {
    private var task: Runnable? = null

    private var async = false

    private var period: Long = -1

    private var delay: Long = -1

    private var bukkitTask: BukkitTask? = null

    /**
     * 设置任务
     *
     * @param task 任务
     * @return ScriptTask本身
     */
    fun setTask(task: Runnable): ScriptTask {
        this.task = task
        return this
    }

    /**
     * 设置是否异步执行
     *
     * @param async 是否异步
     * @return ScriptTask本身
     */
    fun setAsync(async: Boolean): ScriptTask {
        this.async = async
        return this
    }

    /**
     * 设置任务执行间隔
     *
     * @param period 执行间隔
     * @return ScriptTask本身
     */
    fun setPeriod(period: Long): ScriptTask {
        this.period = period.coerceAtLeast(-1)
        return this
    }

    /**
     * 设置任务执行延迟
     *
     * @param delay 执行延迟
     * @return ScriptTask本身
     */
    fun setDelay(delay: Long): ScriptTask {
        this.delay = delay.coerceAtLeast(-1)
        return this
    }

    /**
     * 注册任务
     *
     * @return ScriptTask本身
     */
    fun register(): ScriptTask {
        val bukkitRunnable = object : BukkitRunnable() {
            override fun run() {
                task?.run()
            }
        }
        // 我没研究过能不能异步注册, 所以直接同步, 稳妥一点
        bukkitTask = if (Bukkit.isPrimaryThread()) {
            // 如果之前注册过了就先移除并卸载
            unRegister()
            // 注册任务
            when {
                async && period > 0 -> {
                    bukkitRunnable.runTaskTimerAsynchronously(plugin, delay.coerceAtLeast(0), period)
                }
                async && delay > 0 -> {
                    bukkitRunnable.runTaskLaterAsynchronously(plugin, delay)
                }
                async -> {
                    bukkitRunnable.runTaskAsynchronously(plugin)
                }
                period > 0 -> {
                    bukkitRunnable.runTaskTimer(plugin, delay.coerceAtLeast(0), period)
                }
                delay > 0 -> {
                    bukkitRunnable.runTaskLater(plugin, delay)
                }
                else -> {
                    bukkitRunnable.runTask(plugin)
                }
            }
        } else {
            bukkitScheduler.callSyncMethod(plugin) {
                // 如果之前注册过了就先移除并卸载
                unRegister()
                // 注册任务
                return@callSyncMethod when {
                    async && period > 0 -> {
                        bukkitRunnable.runTaskTimerAsynchronously(plugin, delay.coerceAtLeast(0), period)
                    }
                    async && delay > 0 -> {
                        bukkitRunnable.runTaskLaterAsynchronously(plugin, delay)
                    }
                    async -> {
                        bukkitRunnable.runTaskAsynchronously(plugin)
                    }
                    period > 0 -> {
                        bukkitRunnable.runTaskTimer(plugin, delay.coerceAtLeast(0), period)
                    }
                    delay > 0 -> {
                        bukkitRunnable.runTaskLater(plugin, delay)
                    }
                    else -> {
                        bukkitRunnable.runTask(plugin)
                    }
                }
            }.get()
        }
        // 存入ExpansionManager, 插件重载时自动取消注册
        ExpansionManager.tasks.add(this)
        return this
    }

    /**
     * 卸载任务
     *
     * @return ScriptTask本身
     */
    fun unRegister(): ScriptTask {
        // 注册了就取消任务
        if (Bukkit.isPrimaryThread()) {
            bukkitTask?.also {
                bukkitScheduler.cancelTask(it.taskId)
            }
        } else {
            bukkitScheduler.callSyncMethod(plugin) {
                bukkitTask?.also {
                    bukkitScheduler.cancelTask(it.taskId)
                }
            }
        }
        return this
    }
}