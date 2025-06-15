package pers.neige.neigeitems.command.subcommand

import org.bukkit.command.CommandSender
import org.bukkit.event.EventPriority
import pers.neige.colonel.literal
import pers.neige.colonel.node.Node
import pers.neige.neigeitems.NeigeItems
import pers.neige.neigeitems.annotation.Awake
import pers.neige.neigeitems.annotation.CustomField

/**
 * ni mm指令
 */
object MM {
    @JvmStatic
    @CustomField(fieldType = "root")
    val mm = literal<CommandSender, Unit>("mm")

    @JvmStatic
    @Awake(lifeCycle = Awake.LifeCycle.ENABLE, priority = EventPriority.LOW)
    fun init() {
        NeigeItems.getInstance().scanner.getCustomFields("mm", Node::class.java).forEach {
            Node.then(mm, it as Node<CommandSender, Unit>, false)
        }
        mm.buildLiteralSearcher()
    }
}