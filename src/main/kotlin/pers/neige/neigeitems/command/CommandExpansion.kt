package pers.neige.neigeitems.command

import pers.neige.neigeitems.command.subcommand.ExpansionBuild
import taboolib.common.platform.command.CommandBody

/**
 * 扩展相关指令
 */
object CommandExpansion {
    @CommandBody
    // ni expansion build [扩展路径] > 将对应扩展打包为jar插件
    val build = ExpansionBuild.build
}