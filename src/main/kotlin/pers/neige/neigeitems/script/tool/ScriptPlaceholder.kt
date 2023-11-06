package pers.neige.neigeitems.script.tool

import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import pers.neige.neigeitems.hook.placeholderapi.PlaceholderExpansion
import pers.neige.neigeitems.manager.ConfigManager
import pers.neige.neigeitems.manager.ExpansionManager
import pers.neige.neigeitems.manager.HookerManager.papiHooker
import pers.neige.neigeitems.utils.SchedulerUtils.sync
import java.util.function.BiFunction

/**
 * PAPI 扩展
 *
 * @property identifier 扩展名
 * @constructor PAPI 扩展
 */
class ScriptPlaceholder(private val identifier: String) {
    private var author: String = "unknown"

    private var version: String = "1.0.0"

    private var executor: BiFunction<OfflinePlayer, String, String> =
        BiFunction<OfflinePlayer, String, String> { _, _ ->
            return@BiFunction ""
        }

    private var placeholderExpansion: PlaceholderExpansion? = null

    /**
     * 设置作者
     *
     * @param author 作者
     * @return ScriptPlaceholder 本身
     */
    fun setAuthor(author: String): ScriptPlaceholder {
        this.author = author
        return this
    }

    /**
     * 设置版本
     *
     * @param version 版本
     * @return ScriptPlaceholder 本身
     */
    fun setVersion(version: String): ScriptPlaceholder {
        this.version = version
        return this
    }

    /**
     * 设置 PAPI 变量处理器
     *
     * @param executor PAPI 变量处理器
     * @return ScriptPlaceholder本身
     */
    fun setExecutor(executor: BiFunction<OfflinePlayer, String, String>): ScriptPlaceholder {
        this.executor = executor
        return this
    }

    /**
     * 注册 PAPI 扩展
     *
     * @return ScriptPlaceholder 本身
     */
    fun register(): ScriptPlaceholder {
        // 存入ExpansionManager, 插件重载时自动取消注册
        ExpansionManager.placeholders[identifier]?.unregister()
        ExpansionManager.placeholders[identifier] = this
        papiHooker?.newPlaceholderExpansion(identifier, author, version, executor)?.also {
            // papi是用HashMap存的扩展, 得主线程注册, 防止出现线程安全问题
            sync {
                placeholderExpansion = it
                it.register()
            }
            // papiHooker为null说明没安装PlaceholderAPI
        } ?: let {
            // 后台进行提示
            Bukkit.getLogger()
                .info(ConfigManager.config.getString("Messages.invalidPlugin")?.replace("{plugin}", "PlaceholderAPI"))
        }
        return this
    }

    /**
     * 卸载监听器
     *
     * @return ScriptListener 本身
     */
    fun unregister(): ScriptPlaceholder {
        sync {
            placeholderExpansion?.also {
                it.unregister()
            }
        }
        return this
    }
}