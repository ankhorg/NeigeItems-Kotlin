package pers.neige.neigeitems.event

import taboolib.platform.type.BukkitProxyEvent

/**
 * 插件重载事件, /ni reload指令触发
 */
class PluginReloadEvent {
    /**
     * 插件重载前触发, 不可取消
     */
    class Pre(val type: Type = Type.ALL) : BukkitProxyEvent() {
        override val allowCancelled: Boolean
            get() = false
    }

    /**
     * 插件重载后触发, 不可取消
     */
    class Post(val type: Type = Type.ALL) : BukkitProxyEvent() {
        override val allowCancelled: Boolean
            get() = false
    }

    enum class Type {
        ALL,
        CONFIG,
        ITEM,
        SCRIPT,
        PACK,
        ACTION,
        EDITOR,
        EXPANSION,
    }
}