package pers.neige.neigeitems.command

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import pers.neige.neigeitems.command.subcommand.*
import pers.neige.neigeitems.command.subcommand.Help.help
import pers.neige.neigeitems.manager.ItemManager
import pers.neige.neigeitems.manager.ItemManager.getItemStack
import pers.neige.neigeitems.utils.LangUtils.sendLang
import pers.neige.neigeitems.utils.SchedulerUtils.async
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand

/**
 * 插件指令
 */
@CommandHeader(name = "NeigeItems", aliases = ["ni"])
object Command {
    @CommandBody
    val main = mainCommand {
        execute<CommandSender> { sender, _, _ ->
            async {
                help(sender)
            }
        }
        incorrectSender { sender, _ ->
            sender.sendLang("Messages.onlyPlayer")
        }
        incorrectCommand { sender, _, _, _ ->
            help(sender)
        }
    }

    @CommandBody
    val test = subCommand {
        dynamic {
            suggestion<Player>(uncheck = true) { _, _ ->
                ItemManager.items.keys.toList()
            }
            dynamic(optional = true) {
                execute<Player> { sender, context, argument ->
                    async {
                        val time = System.currentTimeMillis()
                        repeat(argument.toIntOrNull() ?: 1) {
                            getItemStack(context.argument(-1), sender)
                        }
                        sender.sendMessage("耗时: ${System.currentTimeMillis() - time}ms")
                    }
                }
            }
        }
    }

    @CommandBody
    // ni action [玩家ID] [动作内容] > 执行NI物品动作
    val action = Action.action

    @CommandBody
    // ni edithand [玩家ID] [物品编辑函数ID] [函数内容] > 通过对应编辑函数编辑主手物品
    val edithand = Editor.edithand

    @CommandBody
    // ni editoffhand [玩家ID] [物品编辑函数ID] [函数内容] > 通过对应编辑函数编辑副手物品
    val editoffhand = Editor.editoffhand

    @CommandBody
    // ni editslot [玩家ID] [对应槽位] [物品编辑函数ID] [函数内容] > 通过对应编辑函数编辑对应槽位物品
    val editslot = Editor.editslot

    @CommandBody
    // ni itemNBT > 查看当前手中物品的NBT
    val itemNBT = ItemNBT.itemNBT

    @CommandBody
    // ni get [物品ID] (数量) (是否反复随机) (指向数据) > 根据ID获取NI物品
    val get = Give.get

    @CommandBody
    // ni getSilent
    val getSilent = Give.getSilent

    @CommandBody
    // ni give [玩家ID] [物品ID] (数量) (是否反复随机) (指向数据) > 根据ID给予NI物品
    val give = Give.give

    @CommandBody
    // ni giveSilent
    val giveSilent = Give.giveSilent

    @CommandBody
    // ni giveAll [物品ID] (数量) (是否反复随机) (指向数据) > 根据ID给予所有人NI物品
    val giveAll = Give.giveAll

    @CommandBody
    // ni giveAllSilent
    val giveAllSilent = Give.giveAllSilent

    @CommandBody
    // ni givePack [玩家ID] [物品包ID] (数量) > 根据ID给予NI物品包
    val givePack = GivePack.givePack

    @CommandBody
    // ni givePackSilent
    val givePackSilent = GivePack.givePackSilent

    @CommandBody
    // ni drop [物品ID] [数量] [世界名] [X坐标] [Y坐标] [Z坐标] [是否反复随机] [物品解析对象] (指向数据) > 于指定位置掉落NI物品
    val drop = Drop.drop

    @CommandBody
    // ni dropSilent
    val dropSilent = Drop.dropSilent

    @CommandBody
    // ni dropPack [物品包ID] (数量) [世界名] [X坐标] [Y坐标] [Z坐标] (物品解析对象) > 于指定位置掉落NI物品包
    val dropPack = DropPack.dropPack

    @CommandBody
    // ni dropPackSilent
    val dropPackSilent = DropPack.dropPackSilent

    @CommandBody
    // ni save [物品ID] (保存路径) > 将手中物品以对应ID保存至对应路径
    val save = Save.save

    @CommandBody
    // ni cover [物品ID] (保存路径) > 将手中物品以对应ID覆盖至对应路径
    val cover = Save.cover

    @CommandBody
    val mm = CommandMM

    @CommandBody
    val expansion = CommandExpansion

    @CommandBody
    // ni list (页码) > 查看所有NI物品
    val list = pers.neige.neigeitems.command.subcommand.List.list

    @CommandBody
    // ni search [ID前缀] (页码) > 查看对应ID前缀的NI物品
    val search = Search.search

    @CommandBody
    val reload = Reload.reload

    @CommandBody
    val help = Help.helpCommand

    @CommandBody
    val parse = Parse.parse
}