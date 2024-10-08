package pers.neige.neigeitems.command.subcommand

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import org.bukkit.command.CommandSender
import pers.neige.neigeitems.action.ActionContext
import pers.neige.neigeitems.command.CommandUtils.argument
import pers.neige.neigeitems.command.CommandUtils.literal
import pers.neige.neigeitems.command.arguments.ActionArgumentType.action
import pers.neige.neigeitems.command.arguments.ActionArgumentType.getAction
import pers.neige.neigeitems.command.arguments.PlayerArgumentType.getPlayer
import pers.neige.neigeitems.command.arguments.PlayerArgumentType.player
import pers.neige.neigeitems.command.selector.PlayerSelector
import pers.neige.neigeitems.manager.ActionManager

/**
 * ni action指令
 */
object Action {
    val action: LiteralArgumentBuilder<CommandSender> =
        // ni action
        literal<CommandSender>("action").then(
            // ni action [player]
            argument<CommandSender, PlayerSelector>("player", player()).then(
                // ni action [player] [actions]
                argument<CommandSender, pers.neige.neigeitems.action.Action>("actions", action()).executes { context ->
                    ActionManager.runAction(
                        getAction(context, "actions"), ActionContext(getPlayer(context, "player"))
                    )
                    1
                }
            )
        )
}