package pers.neige.neigeitems.command.subcommand

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import org.bukkit.command.CommandSender
import pers.neige.neigeitems.command.CommandUtils
import pers.neige.neigeitems.command.subcommand.mm.Give
import pers.neige.neigeitems.command.subcommand.mm.Load

object MM {
    // ni mm
    val mm: LiteralArgumentBuilder<CommandSender> = CommandUtils.literal<CommandSender>("mm")
        .then(Load.load)
        .then(Load.cover)
        .then(Load.loadAll)
        .then(Give.get)
        .then(Give.getSilent)
        .then(Give.give)
        .then(Give.giveSilent)
        .then(Give.giveAll)
        .then(Give.giveAllSilent)
}