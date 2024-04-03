package pers.neige.neigeitems.command.subcommand

import com.mojang.brigadier.arguments.StringArgumentType.getString
import com.mojang.brigadier.arguments.StringArgumentType.greedyString
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import org.bukkit.command.CommandSender
import pers.neige.neigeitems.command.CommandUtils.argument
import pers.neige.neigeitems.command.CommandUtils.literal
import pers.neige.neigeitems.command.arguments.EditorIDArgumentType.editorID
import pers.neige.neigeitems.command.arguments.EditorIDArgumentType.getEditorID
import pers.neige.neigeitems.command.arguments.IntegerArgumentType.getInteger
import pers.neige.neigeitems.command.arguments.IntegerArgumentType.integer
import pers.neige.neigeitems.command.arguments.PlayerArgumentType.getPlayer
import pers.neige.neigeitems.command.arguments.PlayerArgumentType.player
import pers.neige.neigeitems.command.selector.PlayerSelector
import pers.neige.neigeitems.manager.ItemEditorManager

object Editor {
    val editHand: LiteralArgumentBuilder<CommandSender> =
        // ni edithand
        literal<CommandSender>("editHand").then(
            // ni edithand [player]
            argument<CommandSender, PlayerSelector>("player", player()).then(
                // ni edithand [player] [editor]
                argument<CommandSender, String>("editor", editorID()).then(
                    // ni edithand [player] [editor] [content]
                    argument<CommandSender, String>("content", greedyString()).executes { context ->
                        val player = getPlayer(context, "player") ?: return@executes 1
                        ItemEditorManager.runEditor(
                            getEditorID(context, "editor"),
                            getString(context, "content"),
                            player.inventory.itemInMainHand,
                            player
                        )
                        1
                    }
                )
            )
        )

    val editOffHand: LiteralArgumentBuilder<CommandSender> =
        // ni editoffhand
        literal<CommandSender>("editOffHand").then(
            // ni editoffhand [player]
            argument<CommandSender, PlayerSelector>("player", player()).then(
                // ni editoffhand [player] [editor]
                argument<CommandSender, String>("editor", editorID()).then(
                    // ni editoffhand [player] [editor] [content]
                    argument<CommandSender, String>("content", greedyString()).executes { context ->
                        val player = getPlayer(context, "player") ?: return@executes 1
                        ItemEditorManager.runEditor(
                            getEditorID(context, "editor"),
                            getString(context, "content"),
                            player.inventory.itemInOffHand,
                            player
                        )
                        1
                    }
                )
            )
        )

    val editSlot: LiteralArgumentBuilder<CommandSender> =
        // ni edithand
        literal<CommandSender>("editSlot").then(
            // ni edithand [player]
            argument<CommandSender, PlayerSelector>("player", player()).then(
                // ni edithand [player] [slot]
                argument<CommandSender, Int>("slot", integer(0, 40)).then(
                    // ni edithand [player] [slot] [editor]
                    argument<CommandSender, String>("editor", editorID()).then(
                        // ni edithand [player] [slot] [editor] [content]
                        argument<CommandSender, String>("content", greedyString()).executes { context ->
                            val player = getPlayer(context, "player") ?: return@executes 1
                            val itemStack = player.inventory.getItem(getInteger(context, "slot")) ?: return@executes 1
                            ItemEditorManager.runEditor(
                                getEditorID(context, "editor"),
                                getString(context, "content"),
                                itemStack,
                                player
                            )
                            1
                        }
                    )
                )
            )
        )
}