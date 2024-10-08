package pers.neige.neigeitems.command.subcommand

import com.mojang.brigadier.arguments.StringArgumentType.getString
import com.mojang.brigadier.arguments.StringArgumentType.greedyString
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import org.bukkit.command.CommandSender
import pers.neige.neigeitems.command.CommandUtils.argument
import pers.neige.neigeitems.command.CommandUtils.literal
import pers.neige.neigeitems.command.arguments.PlayerArgumentType.getPlayer
import pers.neige.neigeitems.command.arguments.PlayerArgumentType.player
import pers.neige.neigeitems.command.selector.PlayerSelector
import pers.neige.neigeitems.utils.SectionUtils.parseSection

/**
 * ni parse指令
 */
object Parse {
    val parse: LiteralArgumentBuilder<CommandSender> =
        // ni parse
        literal<CommandSender>("parse").then(
            // ni parse [player]
            argument<CommandSender, PlayerSelector>("player", player()).then(
                // ni parse [player] [sections]
                argument<CommandSender, String>("sections", greedyString()).executes { context ->
                    context.source.sendMessage(
                        getString(context, "sections").parseSection(
                            getPlayer(context, "player")
                        )
                    )
                    1
                }
            )
        )
}