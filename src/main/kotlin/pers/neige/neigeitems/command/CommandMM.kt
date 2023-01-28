package pers.neige.neigeitems.command

import pers.neige.neigeitems.command.subcommand.Action
import pers.neige.neigeitems.command.subcommand.MMGive
import pers.neige.neigeitems.command.subcommand.MMSave
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader

/**
 * MM物品兼容指令
 */
object CommandMM {
    @CommandBody
    // ni mm get [物品ID] (数量) > 根据ID获取MM物品
    val get = MMGive.get

    @CommandBody
    // ni mm give [玩家ID] [物品ID] (数量) > 根据ID给予MM物品
    val give = MMGive.give

    @CommandBody
    // ni mm giveAll [物品ID] (数量) > 根据ID给予所有人MM物品
    val giveAll = MMGive.giveAll

    @CommandBody
    // ni mm load [物品ID] (保存路径) > 将对应ID的MM物品保存为NI物品
    val load = MMSave.load

    @CommandBody
    // ni mm cover [物品ID] (保存路径) > 将对应ID的MM物品覆盖为NI物品
    val cover = MMSave.cover

    @CommandBody
    // ni mm loadAll (保存路径) > 将全部MM物品转化为NI物品
    val loadAll = MMSave.loadAll
}