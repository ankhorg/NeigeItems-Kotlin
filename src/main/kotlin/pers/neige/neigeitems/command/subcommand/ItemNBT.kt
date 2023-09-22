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

//    private fun itemnbtCommandAsyncA (itemStack: ItemStack, sender: Player) {
//        submit (async = true) {
//            itemnbtCommandA(itemStack, sender)
//        }
//    }
//
//    private fun itemnbtCommandA (itemStack: ItemStack, sender: Player) {
//        if (itemStack.type != Material.AIR) {
//            itemStack.getNbt().format().forEach {
//                it.sendTo(Command.bukkitAdapter.adaptCommandSender(sender))
//            }
//        }
//    }

    val INDENT = "  "
    val LIST_INDENT = "§e- "

//    fun NbtCompound.format(): ArrayList<ComponentText> {
//        val result = arrayListOf<ComponentText>()
//        result.add(Components.text("§e§m                                                                      "))
//        forEach { (key, value) ->
//            result.addAll(value.toComponentText(key))
//        }
//        result.add(Components.text("§e§m                                                                      "))
//        return result
//    }
//
//    /**
//     * Nbt 转 ComponentText.
//     *
//     * @param key Nbt键
//     * @param level 所处层级
//     */
//    fun Nbt<*>.toComponentText(key: String, level: Int = 0): ArrayList<ComponentText> {
//        val result = arrayListOf<ComponentText>()
//        // 形似"test (String): "
//        val keyComponent = Components.text("${INDENT.repeat(level)}§6$key${this.postfix()}§e: §f")
//            .hoverText(key)
//            .clickSuggestCommand(key)
//        // 类型检测
//        when (this) {
//            is NbtByte -> {
//                result.add(this.asByte.toString().let {
//                    keyComponent.append(
//                        // 形似"test (String): test"
//                        Components.text(if (it.length > 20) "${it.substring(0, 19)}..." else it)
//                            .hoverText(it)
//                            .clickSuggestCommand(it)
//                    )
//                })
//            }
//            is NbtShort -> {
//                result.add(this.asShort.toString().let {
//                    keyComponent.append(
//                        // 形似"test (String): test"
//                        Components.text(if (it.length > 20) "${it.substring(0, 19)}..." else it)
//                            .hoverText(it)
//                            .clickSuggestCommand(it)
//                    )
//                })
//            }
//            is NbtInt -> {
//                result.add(this.asInt.toString().let {
//                    keyComponent.append(
//                        // 形似"test (String): test"
//                        Components.text(if (it.length > 20) "${it.substring(0, 19)}..." else it)
//                            .hoverText(it)
//                            .clickSuggestCommand(it)
//                    )
//                })
//            }
//            is NbtLong -> {
//                result.add(this.asLong.toString().let {
//                    keyComponent.append(
//                        // 形似"test (String): test"
//                        Components.text(if (it.length > 20) "${it.substring(0, 19)}..." else it)
//                            .hoverText(it)
//                            .clickSuggestCommand(it)
//                    )
//                })
//            }
//            is NbtFloat -> {
//                result.add(this.asFloat.toString().let {
//                    keyComponent.append(
//                        // 形似"test (String): test"
//                        Components.text(if (it.length > 20) "${it.substring(0, 19)}..." else it)
//                            .hoverText(it)
//                            .clickSuggestCommand(it)
//                    )
//                })
//            }
//            is NbtDouble -> {
//                result.add(this.asDouble.toString().let {
//                    keyComponent.append(
//                        // 形似"test (String): test"
//                        Components.text(if (it.length > 20) "${it.substring(0, 19)}..." else it)
//                            .hoverText(it)
//                            .clickSuggestCommand(it)
//                    )
//                })
//            }
//            is NbtString -> {
//                result.add(this.asString.let {
//                    keyComponent.append(
//                        // 形似"test (String): test"
//                        Components.text(if (it.length > 20) "${it.substring(0, 19)}..." else it)
//                            .hoverText(it)
//                            .clickSuggestCommand(it)
//                    )
//                })
//            }
//            is NbtByteArray -> {
//                // 先塞key
//                result.add(keyComponent)
//                // 挨个塞value
//                this.asByteArray.forEach {
//                    val value = it.toString()
//                    // 形似"- 127"
//                    result.add(Components.text("${INDENT.repeat(level)}$LIST_INDENT§f$value")
//                        .hoverText(value)
//                        .clickSuggestCommand(value)
//                    )
//                }
//            }
//            is NbtIntArray -> {
//                result.add(keyComponent)
//                this.asIntArray.forEach {
//                    val value = it.toString()
//                    result.add(Components.text("${INDENT.repeat(level)}$LIST_INDENT§f$value")
//                        .hoverText(value)
//                        .clickSuggestCommand(value)
//                    )
//                }
//            }
//            is NbtLongArray -> {
//                result.add(keyComponent)
//                this.asLongArray.forEach {
//                    val value = it.toString()
//                    result.add(Components.text("${INDENT.repeat(level)}$LIST_INDENT§f$value")
//                        .hoverText(value)
//                        .clickSuggestCommand(value)
//                    )
//                }
//            }
//            is NbtCompound -> {
//                result.add(keyComponent)
//                this.forEach { (key, value) ->
//                    result.addAll(value.toComponentText(key, level + 1))
//                }
//            }
//            is NbtList -> {
//                result.add(keyComponent)
//                this.forEach {
//                    result.addAll(it.toListComponentText(level))
//                }
//            }
//            else -> result.add(keyComponent.append(" §6(§e妖魔鬼怪§6)"))
//        }
//        return result
//    }
//
//    fun Nbt<*>.toListComponentText(level: Int = 0): ArrayList<ComponentText> {
//        val result = arrayListOf<ComponentText>()
//        when (this) {
//            is NbtByte -> {
//                result.add(this.asByte.toString().let {
//                    Components.text("${INDENT.repeat(level)}$LIST_INDENT")
//                        .append(if (it.length > 20) "${it.substring(0, 19)}..." else it)
//                        .hoverText(it)
//                        .clickSuggestCommand(it)
//                })
//            }
//            is NbtShort -> {
//                result.add(this.asShort.toString().let {
//                    Components.text("${INDENT.repeat(level)}$LIST_INDENT")
//                        .append(if (it.length > 20) "${it.substring(0, 19)}..." else it)
//                        .hoverText(it)
//                        .clickSuggestCommand(it)
//                })
//            }
//            is NbtInt -> {
//                result.add(this.asInt.toString().let {
//                    Components.text("${INDENT.repeat(level)}$LIST_INDENT")
//                        .append(if (it.length > 20) "${it.substring(0, 19)}..." else it)
//                        .hoverText(it)
//                        .clickSuggestCommand(it)
//                })
//            }
//            is NbtLong -> {
//                result.add(this.asLong.toString().let {
//                    Components.text("${INDENT.repeat(level)}$LIST_INDENT")
//                        .append(if (it.length > 20) "${it.substring(0, 19)}..." else it)
//                        .hoverText(it)
//                        .clickSuggestCommand(it)
//                })
//            }
//            is NbtFloat -> {
//                result.add(this.asFloat.toString().let {
//                    Components.text("${INDENT.repeat(level)}$LIST_INDENT")
//                        .append(if (it.length > 20) "${it.substring(0, 19)}..." else it)
//                        .hoverText(it)
//                        .clickSuggestCommand(it)
//                })
//            }
//            is NbtDouble -> {
//                result.add(this.asDouble.toString().let {
//                    Components.text("${INDENT.repeat(level)}$LIST_INDENT")
//                        .append(if (it.length > 20) "${it.substring(0, 19)}..." else it)
//                        .hoverText(it)
//                        .clickSuggestCommand(it)
//                })
//            }
//            is NbtString -> {
//                result.add(this.asString.let {
//                    Components.text("${INDENT.repeat(level)}$LIST_INDENT")
//                        .append(if (it.length > 20) "${it.substring(0, 19)}..." else it)
//                        .hoverText(it)
//                        .clickSuggestCommand(it)
//                })
//            }
//            is NbtByteArray -> {
//                var first = true
//                this.asByteArray.forEach {
//                    val value = it.toString()
//                    if (first) {
//                        result.add(Components.text("${INDENT.repeat(level)}$LIST_INDENT$LIST_INDENT§f$value")
//                            .hoverText(value)
//                            .clickSuggestCommand(value)
//                        )
//                        first = false
//                    } else {
//                        result.add(Components.text("${INDENT.repeat(level+1)}$LIST_INDENT§f$value")
//                            .hoverText(value)
//                            .clickSuggestCommand(value)
//                        )
//                    }
//                }
//            }
//            is NbtIntArray -> {
//                var first = true
//                this.asIntArray.forEach {
//                    val value = it.toString()
//                    if (first) {
//                        result.add(Components.text("${INDENT.repeat(level)}$LIST_INDENT$LIST_INDENT§f$value")
//                            .hoverText(value)
//                            .clickSuggestCommand(value)
//                        )
//                        first = false
//                    } else {
//                        result.add(Components.text("${INDENT.repeat(level+1)}$LIST_INDENT§f$value")
//                            .hoverText(value)
//                            .clickSuggestCommand(value)
//                        )
//                    }
//                }
//            }
//            is NbtLongArray -> {
//                var first = true
//                this.asLongArray.forEach {
//                    val value = it.toString()
//                    if (first) {
//                        result.add(Components.text("${INDENT.repeat(level)}$LIST_INDENT$LIST_INDENT§f$value")
//                            .hoverText(value)
//                            .clickSuggestCommand(value)
//                        )
//                        first = false
//                    } else {
//                        result.add(Components.text("${INDENT.repeat(level+1)}$LIST_INDENT§f$value")
//                            .hoverText(value)
//                            .clickSuggestCommand(value)
//                        )
//                    }
//                }
//            }
//            is NbtCompound -> {
//                var first = true
//                this.forEach { (key, value) ->
//                    if (first) {
//                        result.addAll(value.toComponentText("$LIST_INDENT§6$key", level))
//                        first = false
//                    } else {
//                        result.addAll(value.toComponentText(key, level + 1))
//                    }
//                }
//            }
//            is NbtList -> {
//                var first = true
//                this.forEach {
//                    if (first) {
//                        val list = it.toListComponentText(level)
//                        result.add(Components.text("${INDENT.repeat(level)}$LIST_INDENT").append(list.removeFirst()))
//                        list.forEach {
//                            result.add(Components.text(INDENT).append(it))
//                        }
//                        first = false
//                    } else {
//                        result.addAll(it.toListComponentText(level + 1))
//                    }
//                }
//            }
//            else -> result.add(Components.text(" §6(§e妖魔鬼怪§6)"))
//        }
//        return result
//    }
//
//    fun Nbt<*>.postfix(): String {
//        return when (this) {
//            is NbtByte -> " §6(§eByte§6)"
//            is NbtShort -> " §6(§eShort§6)"
//            is NbtInt -> " §6(§eInt§6)"
//            is NbtLong -> " §6(§eLong§6)"
//            is NbtFloat -> " §6(§eFloat§6)"
//            is NbtDouble -> " §6(§eDouble§6)"
//            is NbtString -> " §6(§eString§6)"
//            is NbtByteArray -> " §6(§eByteArray§6)"
//            is NbtIntArray -> " §6(§eIntArray§6)"
//            is NbtLongArray -> " §6(§eLongArray§6)"
//            is NbtCompound -> " §6(§eCompound§6)"
//            is NbtList -> " §6(§eList§6)"
//            else -> " §6(§e妖魔鬼怪§6)"
//        }
//    }

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