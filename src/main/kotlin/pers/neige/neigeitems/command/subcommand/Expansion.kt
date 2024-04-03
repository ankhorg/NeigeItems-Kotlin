package pers.neige.neigeitems.command.subcommand

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import org.bukkit.command.CommandSender
import pers.neige.neigeitems.command.CommandUtils.literal
import pers.neige.neigeitems.command.subcommand.expansion.Build


object Expansion {
    // ni expansion
    val expansion: LiteralArgumentBuilder<CommandSender> = literal<CommandSender>("expansion").then(Build.build)
}