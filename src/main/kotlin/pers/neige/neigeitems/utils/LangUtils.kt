package pers.neige.neigeitems.utils

import org.bukkit.command.CommandSender
import pers.neige.neigeitems.manager.ConfigManager.config
import taboolib.common.platform.ProxyCommandSender

object LangUtils {
    /**
     * 发送消息
     *
     * @param key config
     */
    @JvmStatic
    fun ProxyCommandSender.sendLang(key: String) {
        config.getString(key)?.let {
            // 将消息设置为""即代表不发送消息
            if (it != "") {
                this.sendMessage(it)
            }
        }
    }

    /**
     * 发送消息
     *
     * @param key config
     * @param param 占位符: 值
     */
    @JvmStatic
    fun ProxyCommandSender.sendLang(key: String, param: Map<String, String>) {
        config.getString(key)?.let {
            // 将消息设置为""即代表不发送消息
            if (it != "") {
                var message = it
                // 消息一般长度较短，且占位符数量不多，故不用担心遍历替换造成性能问题
                param.forEach { (key, value) ->
                    message = message.replace(key, value)
                }
                this.sendMessage(message)
            }
        }
    }

    /**
     * 发送消息
     *
     * @param key config
     */
    @JvmStatic
    fun CommandSender.sendLang(key: String) {
        config.getString(key)?.let {
            // 将消息设置为""即代表不发送消息
            if (it != "") {
                this.sendMessage(it)
            }
        }
    }

    /**
     * 发送消息
     *
     * @param key config
     * @param param 占位符: 值
     */
    @JvmStatic
    fun CommandSender.sendLang(key: String, param: Map<String, String>) {
        config.getString(key)?.let {
            // 将消息设置为""即代表不发送消息
            if (it != "") {
                var message = it
                // 消息一般长度较短，且占位符数量不多，故不用担心遍历替换造成性能问题
                param.forEach { (key, value) ->
                    message = message.replace(key, value)
                }
                this.sendMessage(message)
            }
        }
    }

    /**
     * 获取消息
     * 未来会适配多语言, 当前版本无意义
     *
     * @param key config
     */
    @JvmStatic
    fun getLang(key: String): String? {
        return config.getString(key)
    }
}