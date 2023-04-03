package pers.neige.neigeitems.event

import taboolib.platform.type.BukkitProxyEvent

/**
 * MM怪物信息重载完毕事件, /mm reload后一段时间触发
 */
class MobInfoReloadedEvent : BukkitProxyEvent() {
    override val allowCancelled: Boolean
        get() = false
}