package pers.neige.neigeitems.command.subcommand

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import pers.neige.colonel.argument
import pers.neige.colonel.literal
import pers.neige.neigeitems.action.ActionContext
import pers.neige.neigeitems.annotation.CustomField
import pers.neige.neigeitems.colonel.argument.command.ActionArgument
import pers.neige.neigeitems.colonel.argument.command.PlayerArgument

/**
 * ni action指令
 */
object Action {
    @JvmStatic
    @CustomField(fieldType = "root")
    val action = literal<CommandSender, Unit>("action") {
        argument("player", PlayerArgument.NULLABLE) {
            argument("actions", ActionArgument.INSTANCE) {
                setNullExecutor { context ->
                    val player = context.getArgument<Player>("player")
                    val actions = context.getArgument<pers.neige.neigeitems.action.Action>("actions")
                    actions.eval(ActionContext(player))
                }
            }
        }
    }
}