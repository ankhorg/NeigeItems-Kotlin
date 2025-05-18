package pers.neige.neigeitems.command.subcommand

import com.alibaba.fastjson2.parseObject
import com.mojang.brigadier.arguments.StringArgumentType.getString
import com.mojang.brigadier.arguments.StringArgumentType.greedyString
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import org.bukkit.command.CommandSender
import pers.neige.neigeitems.action.ActionContext
import pers.neige.neigeitems.command.CommandUtils.argument
import pers.neige.neigeitems.command.CommandUtils.literal
import pers.neige.neigeitems.command.arguments.FunctionArgumentType.function
import pers.neige.neigeitems.command.arguments.FunctionArgumentType.getFunctionSelector
import pers.neige.neigeitems.command.arguments.PlayerArgumentType.getPlayer
import pers.neige.neigeitems.command.arguments.PlayerArgumentType.player
import pers.neige.neigeitems.command.selector.FunctionSelector
import pers.neige.neigeitems.command.selector.PlayerSelector
import pers.neige.neigeitems.utils.LangUtils.sendLang

/**
 * ni function指令
 */
object Function {
    val function: LiteralArgumentBuilder<CommandSender> =
        // ni function
        literal<CommandSender>("function").then(
            // ni function [player]
            argument<CommandSender, PlayerSelector>("player", player()).then(
                // ni function [player] [function]
                argument<CommandSender, FunctionSelector>("function", function()).executes { context ->
                    handle(context)
                }.then(
                    // ni function [player] [function] (data)
                    argument<CommandSender, String>("data", greedyString()).executes { context ->
                        handle(context, getString(context, "data"))
                    }
                )
            )
        )

    private fun handle(
        context: CommandContext<CommandSender>,
        data: String? = null
    ): Int {
        val sender = context.source
        val functionSelector = getFunctionSelector(context, "function")
        val function = functionSelector.select(context) ?: let {
            sender.sendLang("Messages.invalidFunction", mapOf(Pair("{function}", functionSelector.text)))
            return 1
        }
        val params = when (data) {
            null -> HashMap()
            else -> data.parseObject<HashMap<String, Any>>()
        }
        function.eval(ActionContext(getPlayer(context, "player"), params, params))
        return 1
    }
}