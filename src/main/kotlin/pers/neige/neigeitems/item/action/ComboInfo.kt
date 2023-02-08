package pers.neige.neigeitems.item.action

import java.util.concurrent.CopyOnWriteArrayList

/**
 * 用于记录连击
 *
 * @property type 连击类型
 * @property time 触发时间
 */
class ComboInfo (
    val type: String,
    val time: Long,
)