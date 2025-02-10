package pers.neige.neigeitems.command.subcommand

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.command.CommandUtils.literal
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.*
import pers.neige.neigeitems.manager.HookerManager
import pers.neige.neigeitems.manager.HookerManager.append
import pers.neige.neigeitems.manager.HookerManager.hoverText
import pers.neige.neigeitems.manager.HookerManager.suggestCommand
import pers.neige.neigeitems.utils.LangUtils.sendLang
import pers.neige.neigeitems.utils.SchedulerUtils.async

/**
 * ni itemnbt指令
 */
object ItemNBT {
    // ni itemNBT
    val itemNBT: LiteralArgumentBuilder<CommandSender> =
        literal<CommandSender>("itemNBT").executes { context ->
            itemNBT(context)
            1
        }

    private fun itemNBT(context: CommandContext<CommandSender>) {
        async {
            val sender = context.source
            if (sender !is Player) {
                sender.sendLang("Messages.onlyPlayer")
                return@async
            }
            val itemStack = sender.inventory.itemInMainHand
            itemNBTCommand(sender, itemStack)
        }
    }

    private fun itemNBTCommand(sender: Player, itemStack: ItemStack) {
        if (itemStack.type == Material.AIR) return
        val components = HookerManager.nmsHooker.getDisplayNbt(itemStack).format().create()
        var temp = TextComponent()
        var length = 0
        // 我这么搞不是闲得蛋疼, 我知道 Player.Spigot#sendMessage 可以直接传数组
        // 这样操作是为了把一个大号的复合文本以行为单位进行拆分
        // 1.12.2 对聊天包的大小有限制, 超过 32767 就直接踹人
        // 拆成一行一行的, 大小超限制的可能性就大大降低了
        components.forEach { component ->
            val plainText = component.toPlainText()
            if (plainText == "\n") {
                sender.spigot().sendMessage(temp)
                temp = TextComponent()
                length = 0
            } else {
                temp.addExtra(component)
                length++
            }
        }
        if (length != 0) {
            sender.spigot().sendMessage(temp)
        }
    }

    private const val INDENT = "  "
    private const val LIST_INDENT = "§e- "

    /**
     * 将NBT转换为可视化的聊天组件
     *
     * @return 用于表示NBT的聊天组件
     */
    @JvmStatic
    fun NbtCompound.format(): ComponentBuilder {
        val result = ComponentBuilder("§e§m                                                                      ")
            .append("\n")
        val iterator = this.iterator()
        while (iterator.hasNext()) {
            iterator.next().let { (key, value) ->
                result.append(
                    ComponentBuilder("§6$key${value.asPostfix()}§e: §f")
                        .hoverText(key)
                        .suggestCommand(key)
                )
                if (value is NbtCompound) {
                    result.append("\n").append(INDENT)
                }
                result.append(value.asValueString(1))
                if (iterator.hasNext()) result.append("\n")
            }
        }
        return result.append("\n").append("§e§m                                                                      ")
    }

    /**
     * 根据NBT获取类型后缀
     *
     * @return NBT类型后缀
     */
    @JvmStatic
    fun Nbt<*>.asPostfix(): String {
        return when (this) {
            is NbtByte -> " §6(§eByte§6)"
            is NbtShort -> " §6(§eShort§6)"
            is NbtInt -> " §6(§eInt§6)"
            is NbtLong -> " §6(§eLong§6)"
            is NbtFloat -> " §6(§eFloat§6)"
            is NbtDouble -> " §6(§eDouble§6)"
            is NbtString -> " §6(§eString§6)"
            is NbtByteArray -> " §6(§eByteArray§6)"
            is NbtIntArray -> " §6(§eIntArray§6)"
            is NbtLongArray -> " §6(§eLongArray§6)"
            is NbtCompound -> " §6(§eCompound§6)"
            is NbtList -> " §6(§eList§6)"
            else -> " §6(§e妖魔鬼怪§6)"
        }
    }

    /**
     * 将文本转换为聊天组件, 超过20字符的文本将进行省略
     *
     * @return 聊天组件
     */
    @JvmStatic
    fun String.toBuilder(): ComponentBuilder {
        return ComponentBuilder(if (length > 20) "${substring(0, 19)}..." else this)
            .hoverText(this)
            .suggestCommand(this)
    }

    /**
     * 根据当前级数将NBT转换为聊天组件
     *
     * @param level 当前级数
     * @return 聊天组件
     */
    @JvmStatic
    fun Nbt<*>.asValueString(level: Int): ComponentBuilder {
        return when (this) {
            is NbtByte -> asByte.toString().toBuilder()
            is NbtShort -> asShort.toString().toBuilder()
            is NbtInt -> asInt.toString().toBuilder()
            is NbtLong -> asLong.toString().toBuilder()
            is NbtFloat -> asFloat.toString().toBuilder()
            is NbtDouble -> asDouble.toString().toBuilder()
            is NbtString -> asString.toBuilder()
            is NbtByteArray -> {
                ComponentBuilder("").also { result ->
                    result.append("\n")
                    val iterator = this.asByteArray.iterator()
                    while (iterator.hasNext()) {
                        iterator.next().toString().let { byte ->
                            result.append(
                                with(ComponentBuilder("")) {
                                    repeat(level - 1) { append(INDENT) }
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

            is NbtIntArray -> {
                ComponentBuilder("").also { result ->
                    result.append("\n")
                    val iterator = this.asIntArray.iterator()
                    while (iterator.hasNext()) {
                        iterator.next().toString().let { int ->
                            result.append(
                                with(ComponentBuilder("")) {
                                    repeat(level - 1) { append(INDENT) }
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

            is NbtLongArray -> {
                ComponentBuilder("").also { result ->
                    result.append("\n")
                    val iterator = this.asLongArray.iterator()
                    while (iterator.hasNext()) {
                        iterator.next().toString().let { long ->
                            result.append(
                                with(ComponentBuilder("")) {
                                    repeat(level - 1) { append(INDENT) }
                                    append("$LIST_INDENT§f$long")
                                    hoverText(long)
                                    suggestCommand(long)
                                }
                            )
                        }
                        if (iterator.hasNext()) result.append("\n")
                    }
                }
            }

            is NbtList -> {
                ComponentBuilder("").also { result ->
                    result.append("\n")
                    val iterator = this.iterator()
                    while (iterator.hasNext()) {
                        iterator.next().asValueString(level).let {
                            result.append(
                                ComponentBuilder("").also { json ->
                                    repeat(level - 1) { json.append(INDENT) }
                                    json.append(LIST_INDENT)
                                    json.append(it)
                                    if (iterator.hasNext()) json.append("\n")
                                }
                            )
                        }
                    }
                }
            }

            is NbtCompound -> {
                val result = ComponentBuilder("")
                val iterator = this.iterator()
                var first = true
                while (iterator.hasNext()) {
                    iterator.next().let { (key, value) ->
                        result.append(
                            with(ComponentBuilder("")) {
                                if (first) {
                                    first = false
                                } else {
                                    repeat(level) { append(INDENT) }
                                }
                                append("§6$key${value.asPostfix()}§e: §f")
                                hoverText(key)
                                suggestCommand(key)
                            }
                        )
                        if (value is NbtCompound) {
                            result.append("\n")
                            repeat(level + 1) { result.append(INDENT) }
                        }
                        result.append(value.asValueString(level + 1))
                        if (iterator.hasNext()) result.append("\n")
                    }
                }
                result
            }

            else -> ComponentBuilder("妖魔鬼怪")
        }
    }
}