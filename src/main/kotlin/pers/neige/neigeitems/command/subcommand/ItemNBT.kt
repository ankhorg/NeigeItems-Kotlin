package pers.neige.neigeitems.command.subcommand

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.command.Command
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.submit
import taboolib.module.chat.TellrawJson
import taboolib.module.nms.ItemTag
import taboolib.module.nms.ItemTagData
import taboolib.module.nms.ItemTagType
import taboolib.module.nms.getItemTag

object ItemNBT {
    val itemNBT = subCommand {
        execute<Player> { sender, _, _, ->
            itemnbtCommandAsync(sender.inventory.itemInMainHand, sender)
        }
    }

    private fun itemnbtCommandAsync (itemStack: ItemStack, sender: Player) {
        submit (async = true) {
            itemnbtCommand(itemStack, sender)
        }
    }

    private fun itemnbtCommand (itemStack: ItemStack, sender: Player) {
        if (itemStack.type != Material.AIR) {
            itemStack.getItemTag().format().sendTo(Command.bukkitAdapter.adaptCommandSender(sender))
        }
    }

    val INDENT = "  "
    val LIST_INDENT = "§e- "

    @JvmStatic
    fun ItemTag.format(): TellrawJson {
        val result = TellrawJson().append("§e§m                                                                      \n")
        val iterator = this.iterator()
        while (iterator.hasNext()) {
            iterator.next().let { (key, value) ->
                result.append(
                    TellrawJson()
                        .append("§6$key${value.type.asPostfix()}§e: §f")
                        .hoverText(key)
                        .suggestCommand(key)
                )
                if (value.type == ItemTagType.COMPOUND) {
                    result.append("\n").append(INDENT)
                }
                result.append(value.asValueString(1))
                if (iterator.hasNext()) result.append("\n")
            }
        }
        return result.append("\n§e§m                                                                      ")
    }

    fun ItemTagType.asPostfix(): String {
        return when (this) {
            ItemTagType.BYTE -> " §6(§eByte§6)"
            ItemTagType.SHORT ->  " §6(§eShort§6)"
            ItemTagType.INT ->  " §6(§eInt§6)"
            ItemTagType.LONG ->  " §6(§eLong§6)"
            ItemTagType.FLOAT ->  " §6(§eFloat§6)"
            ItemTagType.DOUBLE ->  " §6(§eDouble§6)"
            ItemTagType.STRING ->  " §6(§eString§6)"
            ItemTagType.BYTE_ARRAY -> " §6(§eByteArray§6)"
            ItemTagType.INT_ARRAY -> " §6(§eIntArray§6)"
            ItemTagType.COMPOUND -> " §6(§eCompound§6)"
            ItemTagType.LIST -> " §6(§eList§6)"
            else -> " §6(§e妖魔鬼怪§6)"
        }
    }

    @JvmStatic
    fun ItemTagData.asValueString(level: Int): TellrawJson {
        return when (this.type) {
            ItemTagType.BYTE,
            ItemTagType.SHORT,
            ItemTagType.INT,
            ItemTagType.LONG,
            ItemTagType.FLOAT,
            ItemTagType.DOUBLE,
            ItemTagType.STRING -> {
                this.asString().let {
                    TellrawJson()
                        .append(if (it.length > 20) "${it.substring(0, 19)}..." else it)
                        .hoverText(it)
                        .suggestCommand(it)
                }
            }
            ItemTagType.BYTE_ARRAY -> {
                TellrawJson().also { result ->
                    result.append("\n")
                    val iterator = this.asByteArray().iterator()
                    while (iterator.hasNext()) {
                        iterator.next().toString().let { byte ->
                            result.append(
                                with (TellrawJson()) {
                                    repeat (level-1) { append(INDENT) }
                                    append("$LIST_INDENT§f$byte")
                                    hoverText(byte)
                                    suggestCommand(byte)
                                }
                            )
                        }
                        if (iterator.hasNext()) result.append("\n")
                    }
                }
            }
            ItemTagType.INT_ARRAY -> {
                TellrawJson().also { result ->
                    result.append("\n")
                    val iterator = this.asIntArray().iterator()
                    while (iterator.hasNext()) {
                        iterator.next().toString().let { int ->
                            result.append(
                                with (TellrawJson()) {
                                    repeat (level-1) { append(INDENT) }
                                    append("$LIST_INDENT§f$int")
                                    hoverText(int)
                                    suggestCommand(int)
                                }
                            )
                        }
                        if (iterator.hasNext()) result.append("\n")
                    }
                }
            }
            ItemTagType.LIST -> {
                TellrawJson().also { result ->
                    result.append("\n")
                    val iterator = this.asList().iterator()
                    while (iterator.hasNext()) {
                        iterator.next().asValueString(level).let {
                            result.append(
                                TellrawJson().also { json ->
                                    repeat (level-1) { json.append(INDENT) }
                                    json.append(LIST_INDENT)
                                    json.append(it)
                                    if (iterator.hasNext()) json.append("\n")
                                }
                            )
                        }
                    }
                }
            }
            ItemTagType.COMPOUND -> {
                val result = TellrawJson()
                val iterator = this.asCompound().iterator()
                var first = true
                while (iterator.hasNext()) {
                    iterator.next().let { (key, value) ->
                        result.append(
                            TellrawJson().also { json ->
                                if (first) {
                                    first = false
                                } else {
                                    repeat (level) { json.append(INDENT) }
                                }
                                json.append("§6$key${value.type.asPostfix()}§e: §f")
                                json.hoverText(key)
                                json.suggestCommand(key)
                            }
                        )
                        if (value.type == ItemTagType.COMPOUND) {
                            result.append("\n")
                            repeat (level+1) { result.append(INDENT) }
                        }
                        result.append(value.asValueString(level+1))
                        if (iterator.hasNext()) result.append("\n")
                    }
                }
                result
            }
            else -> TellrawJson().append("妖魔鬼怪")
        }
    }
}