package pers.neige.neigeitems.command

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.NeigeItems.bukkitScheduler
import pers.neige.neigeitems.NeigeItems.plugin
import pers.neige.neigeitems.manager.*
import pers.neige.neigeitems.manager.ConfigManager.config
import pers.neige.neigeitems.manager.HookerManager.mythicMobsHooker
import pers.neige.neigeitems.manager.ItemManager.getItemStack
import pers.neige.neigeitems.manager.ItemManager.saveItem
import pers.neige.neigeitems.utils.ItemUtils.dropItems
import pers.neige.neigeitems.utils.PlayerUtils.giveItems
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand
import taboolib.common.platform.function.submit
import taboolib.module.chat.TellrawJson
import taboolib.module.nms.getName
import taboolib.platform.BukkitAdapter
import taboolib.platform.util.giveItem
import taboolib.platform.util.hoverItem
import java.io.File
import java.util.*
import java.util.concurrent.Callable
import kotlin.math.ceil

@CommandHeader(name = "NeigeItems", aliases = ["ni"])
object Command {
    private val bukkitAdapter = BukkitAdapter()

    @CommandBody
    val main = mainCommand {
        execute<CommandSender> { sender, _, _ ->
            submit(async = true) {
                help(sender)
            }
        }
        incorrectSender { sender, _ ->
            config.getString("Messages.onlyPlayer")?.let { sender.sendMessage(it) }
        }
        incorrectCommand { sender, _, _, _ ->
            help(sender)
        }
    }

    @CommandBody
    val test = subCommand {
        dynamic(commit = "item") {
            suggestion<Player>(uncheck = true) { _, _ ->
                ItemManager.items.keys.toList()
            }
            dynamic(optional = true, commit = "amount") {
                execute<Player> { sender, context, argument ->
                    submit(async = true) {
                        argument.toIntOrNull()?.let { amount ->
                            repeat(amount.coerceAtLeast(1)) {
                                ItemManager.getItemStack(context.argument(-1), sender) ?: let {
                                    sender.sendMessage(
                                        config.getString("Messages.unknownItem")?.replace("{itemID}", argument)
                                    )
                                }
                            }
                        } ?: let {
                            sender.sendMessage(config.getString("Messages.invalidAmount"))
                        }
                    }
                }
            }
        }
    }

    @CommandBody
    val list = subCommand {
        execute<CommandSender> { sender, _, _, ->
            listCommandAsync(sender, 1)
        }
        dynamic(commit = "page") {
            suggestion<CommandSender>(uncheck = true) { _, _ ->
                (1..ceil(ItemManager.itemAmount.toDouble()/config.getDouble("ItemList.ItemAmount")).toInt()).toList().map { it.toString() }
            }
            execute<CommandSender> { sender, _, argument ->
                listCommandAsync(sender, argument.toIntOrNull()?:1)
            }
        }
    }

    @CommandBody
    // ni get [物品ID] (数量) (是否反复随机) (指向数据) > 根据ID获取NI物品
    val get = subCommand {
        execute<Player> { sender, _, _ ->
            submit(async = true) {
                help(sender)
            }
        }
        // ni get [物品ID]
        dynamic(commit = "item") {
            suggestion<Player>(uncheck = true) { _, _ ->
                ItemManager.items.keys.toList()
            }
            execute<Player> { sender, _, argument ->
                giveCommandAsync(sender, sender, argument, "1")
            }
            // ni get [物品ID] (数量)
            dynamic(optional = true, commit = "amount") {
                suggestion<Player>(uncheck = true) { _, _ ->
                    arrayListOf("amount")
                }
                execute<Player> { sender, context, argument ->
                    giveCommandAsync(sender, sender, context.argument(-1), argument)
                }
                // ni get [物品ID] (数量) (是否反复随机)
                dynamic(optional = true, commit = "random") {
                    suggestion<Player>(uncheck = true) { _, _ ->
                        arrayListOf("true", "false")
                    }
                    execute<Player> { sender, context, argument ->
                        giveCommandAsync(sender, sender, context.argument(-2), context.argument(-1), argument)
                    }
                    // ni get [物品ID] (数量) (是否反复随机) (指向数据)
                    dynamic(optional = true, commit = "data") {
                        suggestion<Player>(uncheck = true) { _, _ ->
                            arrayListOf("data")
                        }
                        execute<Player> { sender, context, argument ->
                            giveCommandAsync(sender, sender, context.argument(-3), context.argument(-2), context.argument(-1), argument)
                        }
                    }
                }
            }
        }
    }

    @CommandBody
    // ni give [玩家ID] [物品ID] (数量) (是否反复随机) (指向数据) > 根据ID给予NI物品
    val give = subCommand {
        execute<Player> { sender, _, _ ->
            submit(async = true) {
                help(sender)
            }
        }
        dynamic(commit = "player") {
            suggestion<CommandSender>(uncheck = true) { _, _ ->
                Bukkit.getOnlinePlayers().map { it.name }
            }
            execute<Player> { sender, _, _ ->
                submit(async = true) {
                    help(sender)
                }
            }
            // ni give [玩家ID] [物品ID]
            dynamic(commit = "item") {
                suggestion<CommandSender>(uncheck = true) { _, _ ->
                    ItemManager.items.keys.toList()
                }
                execute<CommandSender> { sender, context, argument ->
                    giveCommandAsync(sender, Bukkit.getPlayerExact(context.argument(-1)), argument, "1")
                }
                // ni give [玩家ID] [物品ID] (数量)
                dynamic(optional = true, commit = "amount") {
                    suggestion<CommandSender>(uncheck = true) { _, _ ->
                        arrayListOf("amount")
                    }
                    execute<CommandSender> { sender, context, argument ->
                        giveCommandAsync(sender, Bukkit.getPlayerExact(context.argument(-2)), context.argument(-1), argument)
                    }
                    // ni give [玩家ID] [物品ID] (数量) (是否反复随机)
                    dynamic(optional = true, commit = "random") {
                        suggestion<CommandSender>(uncheck = true) { _, _ ->
                            arrayListOf("true", "false")
                        }
                        execute<CommandSender> { sender, context, argument ->
                            giveCommandAsync(sender, Bukkit.getPlayerExact(context.argument(-3)), context.argument(-2), context.argument(-1), argument)
                        }
                        // ni give [玩家ID] [物品ID] (数量) (是否反复随机) (指向数据)
                        dynamic(optional = true, commit = "data") {
                            suggestion<CommandSender>(uncheck = true) { _, _ ->
                                arrayListOf("data")
                            }
                            execute<CommandSender> { sender, context, argument ->
                                giveCommandAsync(sender, Bukkit.getPlayerExact(context.argument(-4)), context.argument(-3), context.argument(-2), context.argument(-1), argument)
                            }
                        }
                    }
                }
            }
        }
    }

    @CommandBody
    // ni giveAll [物品ID] (数量) (是否反复随机) (指向数据) > 根据ID给予所有人NI物品
    val giveAll = subCommand {
        execute<Player> { sender, _, _ ->
            submit(async = true) {
                help(sender)
            }
        }
        // ni giveAll [物品ID]
        dynamic(commit = "item") {
            suggestion<CommandSender>(uncheck = true) { _, _ ->
                ItemManager.items.keys.toList()
            }
            execute<CommandSender> { sender, _, argument ->
                giveAllCommandAsync(sender, argument, "1")
            }
            // ni giveAll [物品ID] (数量)
            dynamic(optional = true, commit = "amount") {
                suggestion<CommandSender>(uncheck = true) { _, _ ->
                    arrayListOf("amount")
                }
                execute<CommandSender> { sender, context, argument ->
                    giveAllCommandAsync(sender, context.argument(-1), argument)
                }
                // ni giveAll [物品ID] (数量) (是否反复随机)
                dynamic(optional = true, commit = "random") {
                    suggestion<CommandSender>(uncheck = true) { _, _ ->
                        arrayListOf("true", "false")
                    }
                    execute<CommandSender> { sender, context, argument ->
                        giveAllCommandAsync(sender, context.argument(-2), context.argument(-1), argument)
                    }
                    // ni giveAll [物品ID] (数量) (是否反复随机) (指向数据)
                    dynamic(optional = true, commit = "data") {
                        suggestion<CommandSender>(uncheck = true) { _, _ ->
                            arrayListOf("data")
                        }
                        execute<CommandSender> { sender, context, argument ->
                            giveAllCommandAsync(sender, context.argument(-3), context.argument(-2), context.argument(-1), argument)
                        }
                    }
                }
            }
        }
    }

    @CommandBody
    // ni drop [物品ID] [数量] [世界名] [X坐标] [Y坐标] [Z坐标] [是否反复随机] [物品解析对象] (指向数据) > 于指定位置掉落NI物品
    val drop = subCommand {
        // ni drop [物品ID]
        dynamic(commit = "item") {
            suggestion<CommandSender>(uncheck = true) { _, _ ->
                ItemManager.items.keys.toList()
            }
            execute<Player> { sender, _, _ ->
                submit(async = true) {
                    help(sender)
                }
            }
            // ni drop [物品ID] [数量]
            dynamic(commit = "amount") {
                suggestion<CommandSender>(uncheck = true) { _, _ ->
                    arrayListOf("amount")
                }
                execute<Player> { sender, _, _ ->
                    submit(async = true) {
                        help(sender)
                    }
                }
                // ni drop [物品ID] [数量] [世界名]
                dynamic(commit = "world") {
                    suggestion<CommandSender>(uncheck = true) { _, _ ->
                        Bukkit.getWorlds().map { it.name }
                    }
                    execute<Player> { sender, _, _ ->
                        submit(async = true) {
                            help(sender)
                        }
                    }
                    // ni drop [物品ID] [数量] [世界名] [X坐标]
                    dynamic(commit = "x") {
                        suggestion<CommandSender>(uncheck = true) { _, _ ->
                            arrayListOf("x")
                        }
                        execute<Player> { sender, _, _ ->
                            submit(async = true) {
                                help(sender)
                            }
                        }
                        // ni drop [物品ID] [数量] [世界名] [X坐标] [Y坐标]
                        dynamic(commit = "y") {
                            suggestion<CommandSender>(uncheck = true) { _, _ ->
                                arrayListOf("y")
                            }
                            execute<Player> { sender, _, _ ->
                                submit(async = true) {
                                    help(sender)
                                }
                            }
                            // ni drop [物品ID] [数量] [世界名] [X坐标] [Y坐标] [Z坐标]
                            dynamic(commit = "z") {
                                suggestion<CommandSender>(uncheck = true) { _, _ ->
                                    arrayListOf("z")
                                }
                                execute<Player> { sender, _, _ ->
                                    submit(async = true) {
                                        help(sender)
                                    }
                                }
                                // ni drop [物品ID] [数量] [世界名] [X坐标] [Y坐标] [Z坐标] [是否反复随机]
                                dynamic(commit = "random") {
                                    suggestion<CommandSender>(uncheck = true) { _, _ ->
                                        arrayListOf("true", "false")
                                    }
                                    execute<Player> { sender, _, _ ->
                                        submit(async = true) {
                                            help(sender)
                                        }
                                    }
                                    // ni drop [物品ID] [数量] [世界名] [X坐标] [Y坐标] [Z坐标] [是否反复随机] [物品解析对象]
                                    dynamic(commit = "data") {
                                        suggestion<CommandSender>(uncheck = true) { _, _ ->
                                            Bukkit.getOnlinePlayers().map { it.name }
                                        }
                                        execute<CommandSender> { sender, context, argument ->
                                            dropCommandAsync(sender, context.argument(-7), context.argument(-6), context.argument(-5), context.argument(-4), context.argument(-3), context.argument(-2), context.argument(-1), argument)
                                        }
                                        // ni drop [物品ID] [数量] [世界名] [X坐标] [Y坐标] [Z坐标] [是否反复随机] [物品解析对象] (指向数据)
                                        dynamic(optional = true, commit = "player") {
                                            suggestion<CommandSender>(uncheck = true) { _, _ ->
                                                arrayListOf("data")
                                            }
                                            execute<CommandSender> { sender, context, argument ->
                                                dropCommandAsync(sender, context.argument(-8), context.argument(-7), context.argument(-6), context.argument(-5), context.argument(-4), context.argument(-3), context.argument(-2), context.argument(-1), argument)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @CommandBody
    // ni save [物品ID] (保存路径) > 将手中物品以对应ID保存至对应路径
    val save = subCommand {
        execute<Player> { sender, _, _ ->
            submit(async = true) {
                help(sender)
            }
        }
        // ni save [物品ID]
        dynamic(commit = "id") {
            suggestion<Player>(uncheck = true) { _, _ ->
                arrayListOf("id")
            }
            execute<Player> { sender, _, argument ->
                submit(async = true) {
                    when (saveItem(sender.inventory.itemInMainHand, argument, "$argument.yml", false)) {
                        // 保存成功
                        1 -> {
                            sender.sendMessage(config.getString("Messages.successSaveInfo")
                                ?.replace("{name}", sender.inventory.itemInMainHand.getName())
                                ?.replace("{itemID}", argument)
                                ?.replace("{path}", "$argument.yml"))
                        }
                        // 已存在对应ID物品
                        0 -> sender.sendMessage(config.getString("Messages.existedKey")?.replace("{itemID}", argument))
                        // 你保存了个空气
                        else -> sender.sendMessage(config.getString("Messages.airItem"))
                    }
                }
            }
            // ni save [物品ID] (保存路径)
            dynamic(commit = "path") {
                suggestion<Player>(uncheck = true) { _, _ ->
                    ItemManager.files.map { it.path.replace("plugins${File.separator}NeigeItems${File.separator}Items${File.separator}", "") }
                }
                execute<Player> { sender, context, argument ->
                    submit(async = true) {
                        when (saveItem(sender.inventory.itemInMainHand, context.argument(-1), argument, false)) {
                            // 保存成功
                            1 -> {
                                sender.sendMessage(config.getString("Messages.successSaveInfo")
                                    ?.replace("{name}", sender.inventory.itemInMainHand.getName())
                                    ?.replace("{itemID}", context.argument(-1))
                                    ?.replace("{path}", argument))
                            }
                            // 已存在对应ID物品
                            0 -> sender.sendMessage(config.getString("Messages.existedKey")?.replace("{itemID}", context.argument(-1)))
                            // 你保存了个空气
                            else -> sender.sendMessage(config.getString("Messages.airItem"))
                        }
                    }
                }
            }
        }
    }

    @CommandBody
    // ni cover [物品ID] (保存路径) > 将手中物品以对应ID覆盖至对应路径
    val cover = subCommand {
        execute<Player> { sender, _, _ ->
            submit(async = true) {
                help(sender)
            }
        }
        // ni cover [物品ID]
        dynamic(commit = "id") {
            suggestion<Player>(uncheck = true) { _, _ ->
                arrayListOf("id")
            }
            execute<Player> { sender, _, argument ->
                submit(async = true) {
                    when (saveItem(sender.inventory.itemInMainHand, argument, "$argument.yml", true)) {
                        // 你保存了个空气
                        2 -> sender.sendMessage(config.getString("Messages.airItem"))
                        // 保存成功
                        else -> {
                            sender.sendMessage(config.getString("Messages.successSaveInfo")
                                ?.replace("{name}", sender.inventory.itemInMainHand.getName())
                                ?.replace("{itemID}", argument)
                                ?.replace("{path}", "$argument.yml"))
                        }
                    }
                }
            }
            // ni cover [物品ID] (保存路径)
            dynamic(commit = "path") {
                suggestion<Player>(uncheck = true) { _, _ ->
                    ItemManager.files.map { it.path.replace("plugins${File.separator}NeigeItems${File.separator}Items${File.separator}", "") }
                }
                execute<Player> { sender, context, argument ->
                    submit(async = true) {
                        when (saveItem(sender.inventory.itemInMainHand, context.argument(-1), argument, true)) {
                            // 你保存了个空气
                            2 -> sender.sendMessage(config.getString("Messages.airItem"))
                            // 保存成功
                            else -> {
                                sender.sendMessage(config.getString("Messages.successSaveInfo")
                                    ?.replace("{name}", sender.inventory.itemInMainHand.getName())
                                    ?.replace("{itemID}", context.argument(-1))
                                    ?.replace("{path}", argument))
                            }
                        }
                    }
                }
            }
        }
    }

    @CommandBody
    val mm = subCommand {
        dynamic(commit = "action") {
            suggestion<CommandSender>(uncheck = true) { _, _ ->
                arrayListOf("load", "cover", "loadAll", "get", "give", "giveAll")
            }
            execute<CommandSender> { sender, _, argument ->
                submit(async = true) {
                    mythicMobsHooker?.let {
                        when (argument) {
                            // ni mm loadAll
                            "loadAll" -> {
                                mythicMobsHooker.getItemIds().forEach { id ->
                                    mythicMobsHooker.getItemStackSync(id)?.let { itemStack ->
                                        when (saveItem(itemStack, id, config.getString("Main.MMItemsPath") ?: "MMItems.yml", false)) {
                                            // 保存成功
                                            1 -> {
                                                sender.sendMessage(config.getString("Messages.successSaveInfo")
                                                    ?.replace("{name}", itemStack.getName())
                                                    ?.replace("{itemID}", id)
                                                    ?.replace("{path}", config.getString("Main.MMItemsPath") ?: "MMItems.yml"))
                                            }
                                            // 已存在对应ID物品
                                            0 -> sender.sendMessage(config.getString("Messages.existedKey")?.replace("{itemID}", id))
                                        }
                                    }
                                }
                            }
                            else -> help(sender)
                        }
                    } ?: sender.sendMessage(config.getString("Messages.invalidPlugin")?.replace("{plugin}", "MythicMobs"))
                }
            }
            dynamic(commit = "action") {
                suggestion<CommandSender>(uncheck = true) { _, context ->
                    mythicMobsHooker?.let {
                        when (context.argument(-1)) {
                            // ni mm load [物品ID]
                            "load" -> mythicMobsHooker.getItemIds()
                            // ni mm cover [物品ID]
                            "cover" -> mythicMobsHooker.getItemIds()
                            // ni mm loadAll (保存路径)
                            "loadAll" -> ItemManager.files.map { it.path.replace("plugins${File.separator}NeigeItems${File.separator}Items${File.separator}", "") }
                            // ni mm get [物品ID]
                            "get" -> mythicMobsHooker.getItemIds()
                            // ni mm give [玩家ID]
                            "give" -> Bukkit.getOnlinePlayers().map { it.name }
                            // ni mm giveAll [物品ID]
                            "giveAll" -> mythicMobsHooker.getItemIds()
                            else -> arrayListOf()
                        }
                    } ?: arrayListOf()
                }
                execute<CommandSender> { sender, context, argument ->
                    submit(async = true) {
                        mythicMobsHooker?.let {
                            when (context.argument(-1)) {
                                // ni mm load [物品ID]
                                "load" -> {
                                    mythicMobsHooker.getItemStackSync(argument)?.let { itemStack ->
                                        when (saveItem(itemStack, argument, "$argument.yml", false)) {
                                            // 保存成功
                                            1 -> {
                                                sender.sendMessage(config.getString("Messages.successSaveInfo")
                                                    ?.replace("{name}", itemStack.getName())
                                                    ?.replace("{itemID}", argument)
                                                    ?.replace("{path}", "$argument.yml"))
                                            }
                                            // 已存在对应ID物品
                                            0 -> sender.sendMessage(config.getString("Messages.existedKey")?.replace("{itemID}", argument))
                                        }
                                        // 未知物品
                                    } ?: let {
                                        sender.sendMessage(config.getString("Messages.unknownItem")?.replace("{itemID}", argument))
                                    }
                                }
                                // ni mm cover [物品ID]
                                "cover" -> {
                                    mythicMobsHooker.getItemStackSync(argument)?.let { itemStack ->
                                        if (saveItem(itemStack, argument, "$argument.yml", true) != 2) {
                                            // 保存成功
                                            sender.sendMessage(config.getString("Messages.successSaveInfo")
                                                ?.replace("{name}", itemStack.getName())
                                                ?.replace("{itemID}", argument)
                                                ?.replace("{path}", "$argument.yml"))
                                        }
                                        // 未知物品
                                    } ?: let {
                                        sender.sendMessage(config.getString("Messages.unknownItem")?.replace("{itemID}", argument))
                                    }
                                }
                                // ni mm loadAll (保存路径)
                                "loadAll" -> {
                                    mythicMobsHooker.getItemIds().forEach { id ->
                                        mythicMobsHooker.getItemStackSync(id)?.let { itemStack ->
                                            when (saveItem(itemStack, id, argument, false)) {
                                                // 保存成功
                                                1 -> {
                                                    sender.sendMessage(config.getString("Messages.successSaveInfo")
                                                        ?.replace("{name}", itemStack.getName())
                                                        ?.replace("{itemID}", id)
                                                        ?.replace("{path}", argument))
                                                }
                                                // 已存在对应ID物品
                                                0 -> sender.sendMessage(config.getString("Messages.existedKey")?.replace("{itemID}", id))
                                            }
                                        }
                                    }
                                }
                                // ni mm get [物品ID]
                                "get" -> {
                                    if (sender is Player) {
                                        giveAddonCommand( sender, sender, argument, mythicMobsHooker.getItemStackSync(argument), 1)
                                    } else {
                                        config.getString("Messages.onlyPlayer")?.let { sender.sendMessage(it) }
                                    }
                                }
                                // ni mm giveAll [物品ID]
                                "giveAll" -> {
                                    Bukkit.getOnlinePlayers().forEach { player ->
                                        giveAddonCommand( sender, player, argument, mythicMobsHooker.getItemStackSync(argument), 1)
                                    }
                                }
                                else -> help(sender)
                            }
                        } ?: sender.sendMessage(config.getString("Messages.invalidPlugin")?.replace("{plugin}", "MythicMobs"))
                    }
                }
                dynamic(commit = "action") {
                    suggestion<CommandSender>(uncheck = true) { _, context ->
                        mythicMobsHooker?.let {
                            when (context.argument(-2)) {
                                // ni mm load [物品ID] (保存路径)
                                "load" -> ItemManager.files.map { it.path.replace("plugins${File.separator}NeigeItems${File.separator}Items${File.separator}", "") }
                                // ni mm cover [物品ID] (保存路径)
                                "cover" -> ItemManager.files.map { it.path.replace("plugins${File.separator}NeigeItems${File.separator}Items${File.separator}", "") }
                                // ni mm get [物品ID] (数量)
                                "get" -> arrayListOf("amount")
                                // ni mm give [玩家ID] [物品ID]
                                "give" -> mythicMobsHooker.getItemIds()
                                // ni mm giveAll [物品ID] (数量)
                                "giveAll" -> arrayListOf("amount")
                                else -> arrayListOf()
                            }
                        } ?: arrayListOf()
                    }
                    execute<CommandSender> { sender, context, argument ->
                        submit(async = true) {
                            mythicMobsHooker?.let {
                                when (context.argument(-2)) {
                                    // ni mm load [物品ID] (保存路径)
                                    "load" -> {
                                        mythicMobsHooker.getItemStackSync(context.argument(-1))?.let { itemStack ->
                                            when (saveItem(itemStack, context.argument(-1), argument, false)) {
                                                // 保存成功
                                                1 -> {
                                                    sender.sendMessage(config.getString("Messages.successSaveInfo")
                                                        ?.replace("{name}", itemStack.getName())
                                                        ?.replace("{itemID}", context.argument(-1))
                                                        ?.replace("{path}", argument))
                                                }
                                                // 已存在对应ID物品
                                                0 -> sender.sendMessage(config.getString("Messages.existedKey")?.replace("{itemID}", context.argument(-1)))
                                            }
                                            // 未知物品
                                        } ?: let {
                                            sender.sendMessage(config.getString("Messages.unknownItem")?.replace("{itemID}", context.argument(-1)))
                                        }
                                    }
                                    // ni mm cover [物品ID] (保存路径)
                                    "cover" -> {
                                        mythicMobsHooker.getItemStackSync(context.argument(-1))?.let { itemStack ->
                                            if (saveItem(itemStack, context.argument(-1), argument, true) != 2) {
                                                // 保存成功
                                                sender.sendMessage(config.getString("Messages.successSaveInfo")
                                                    ?.replace("{name}", itemStack.getName())
                                                    ?.replace("{itemID}", context.argument(-1))
                                                    ?.replace("{path}", argument))
                                            }
                                            // 未知物品
                                        } ?: let {
                                            sender.sendMessage(config.getString("Messages.unknownItem")?.replace("{itemID}", context.argument(-1)))
                                        }
                                    }
                                    // ni mm get [物品ID] (数量)
                                    "get" -> {
                                        if (sender is Player) {
                                            giveAddonCommand( sender, sender, context.argument(-1), mythicMobsHooker.getItemStackSync(context.argument(-1)), argument.toIntOrNull())
                                        } else {
                                            config.getString("Messages.onlyPlayer")?.let { sender.sendMessage(it) }
                                        }
                                    }
                                    // ni mm give [玩家ID] [物品ID]
                                    "give" -> {
                                        giveAddonCommand( sender, Bukkit.getPlayerExact(context.argument(-1)), argument, mythicMobsHooker.getItemStackSync(argument), 1)
                                    }
                                    // ni mm giveAll [物品ID] (数量)
                                    "giveAll" -> {
                                        Bukkit.getOnlinePlayers().forEach { player ->
                                            giveAddonCommand( sender, player, context.argument(-1), mythicMobsHooker.getItemStackSync(context.argument(-1)), argument.toIntOrNull())
                                        }
                                    }
                                    else -> help(sender)
                                }
                            } ?: sender.sendMessage(config.getString("Messages.invalidPlugin")?.replace("{plugin}", "MythicMobs"))
                        }
                    }
                    dynamic(commit = "action") {
                        suggestion<CommandSender>(uncheck = true) { _, context ->
                            mythicMobsHooker?.let {
                                when (context.argument(-3)) {
                                    // ni mm give [玩家ID] [物品ID] (数量)
                                    "give" -> mythicMobsHooker.getItemIds()
                                    else -> arrayListOf()
                                }
                            } ?: arrayListOf()
                        }
                        execute<CommandSender> { sender, context, argument ->
                            submit(async = true) {
                                mythicMobsHooker?.let {
                                    when (context.argument(-3)) {
                                        // ni mm give [玩家ID] [物品ID] (数量)
                                        "give" -> {
                                            giveAddonCommand( sender, Bukkit.getPlayerExact(context.argument(-2)), context.argument(-1), mythicMobsHooker.getItemStackSync(context.argument(-1)), argument.toIntOrNull())
                                        }
                                        else -> help(sender)
                                    }
                                } ?: sender.sendMessage(config.getString("Messages.invalidPlugin")?.replace("{plugin}", "MythicMobs"))
                            }
                        }
                    }
                }
            }
        }
    }

    @CommandBody
    val reload = subCommand {
        execute<CommandSender> { sender, _, _ ->
            reloadCommand(sender)
        }
    }

    @CommandBody
    val help = subCommand {
        execute<CommandSender> { sender, _, _ ->
            help(sender)
        }
    }
    private fun listCommandAsync (sender: CommandSender, page: Int) {
        submit (async = true) {
            listCommand(sender, page)
        }
    }

    private fun listCommand (
        // 行为发起人, 用于接收反馈信息
        sender: CommandSender,
        // 页码
        page: Int
    ) {
        val pageAmount = ceil(ItemManager.itemAmount.toDouble()/config.getDouble("ItemList.ItemAmount")).toInt()
        val realPage = page.coerceAtMost(pageAmount).coerceAtLeast(1)
        // 发送前缀
        config.getString("ItemList.Prefix")?.let { sender.sendMessage(it) }
        // 预构建待发送信息
        val listMessage = TellrawJson()
        // 获取当前序号
        val prevItemAmount = ((realPage-1)*config.getInt("ItemList.ItemAmount"))+1
        // 逐个获取物品
        for (index in (prevItemAmount until prevItemAmount + config.getInt("ItemList.ItemAmount"))) {
            if (index == ItemManager.itemIds.size + 1) break
            val id = ItemManager.itemIds[index-1]
            // 替换信息内变量
            var listItemMessage = (config.getString("ItemList.ItemFormat") ?: "")
                .replace("{index}", index.toString())
                .replace("{ID}", id)
            // 构建信息及物品
            if (sender is Player) {
                getItemStack(id, sender)?.let { itemStack ->
                    val listItemMessageList = listItemMessage.split("{name}")
                    val listItemRaw = TellrawJson()
                    for ((i, it) in listItemMessageList.withIndex()) {
                        listItemRaw.append(
                            TellrawJson()
                                .append(it)
                                .runCommand("/ni get $id")
                                .hoverText(config.getString("Messages.clickGiveMessage")?:"")
                        )
                        if (i+1 != listItemMessageList.size) {
                            listItemRaw.append(
                                TellrawJson()
                                    .append(itemStack.getName())
                                    .hoverItem(itemStack)
                                    .runCommand("/ni get $id")
                            )
                        }
                    }
                    listItemRaw.sendTo(bukkitAdapter.adaptCommandSender(sender))
                }
            } else {
                // 在不传入玩家变量的情况下尝试构建物品获取物品名
                // 如果用户在js节点中调用了玩家对象, 这样搞就会报错
                try {
                    getItemStack(id)?.let { itemStack ->
                        sender.sendMessage(listItemMessage.replace("{name}", itemStack.getName()))
                    }
                } catch (error: Throwable) {
                    val itemKeySection = ItemManager.getOriginConfig(id)
                    val itemName = when {
                        itemKeySection?.contains("name") == true -> itemKeySection.getString("name")
                        else -> Material.matchMaterial((itemKeySection?.getString("material")?:"").uppercase(Locale.getDefault()))
                            ?.let { ItemStack(it).getName() }
                    }
                    listItemMessage = itemName?.let { listItemMessage.replace("{name}", it) }.toString()
                    sender.sendMessage(listItemMessage)
                }
            }
        }
        val prevRaw = TellrawJson()
            .append(config.getString("ItemList.Prev")?:"")
        if (realPage != 1) {
            prevRaw
                .hoverText((config.getString("ItemList.Prev")?:"") + ": " + (realPage-1).toString())
                .runCommand("/ni list ${realPage-1}")
        }
        val nextRaw = TellrawJson()
            .append(config.getString("ItemList.Next")?:"")
        if (realPage != pageAmount) {
            nextRaw.hoverText((config.getString("ItemList.Next")?:"") + ": " + (realPage+1))
            nextRaw.runCommand("/ni list ${realPage+1}")
        }
        var listSuffixMessage = (config.getString("ItemList.Suffix")?:"")
            .replace("{current}", realPage.toString())
            .replace("{total}", pageAmount.toString())
        if (sender is Player) {
            listSuffixMessage = listSuffixMessage
                .replace("{prev}", "!@#$%{prev}!@#$%")
                .replace("{next}", "!@#$%{next}!@#$%")
            val listSuffixMessageList = listSuffixMessage.split("!@#$%")
            listSuffixMessageList.forEach { value ->
                when (value) {
                    "{prev}" -> listMessage.append(prevRaw)
                    "{next}" -> listMessage.append(nextRaw)
                    else -> listMessage.append(value)
                }
            }
            // 向玩家发送信息
            listMessage.sendTo(bukkitAdapter.adaptCommandSender(sender))
        } else {
            sender.sendMessage(listSuffixMessage
                .replace("{prev}", config.getString("ItemList.Prev")?:"")
                .replace("{next}", config.getString("ItemList.Next")?:""))
        }
    }

    private fun giveCommand(
        // 行为发起人, 用于接收反馈信息
        sender: CommandSender,
        // 物品接收者
        player: Player?,
        // 待给予物品ID
        id: String,
        // 给予数量
        amount: String?,
        // 是否反复随机
        random: String?,
        // 指向数据
        data: String?
    ) {
        giveCommand(sender, player, id, amount?.toIntOrNull(), random, data)
    }

    private fun giveCommandAsync(
        sender: CommandSender,
        player: Player?,
        id: String,
        amount: String? = null,
        random: String? = null,
        data: String? = null
    ) {
        submit(async = true) {
            giveCommand(sender, player, id, amount, random, data)
        }
    }

    private fun giveAllCommandAsync(
        sender: CommandSender,
        id: String,
        amount: String? = null,
        random: String? = null,
        data: String? = null
    ) {
        submit(async = true) {
            Bukkit.getOnlinePlayers().forEach { player ->
                giveCommand(sender, player, id, amount, random, data)
            }
        }
    }

    private fun giveCommand(
        sender: CommandSender,
        player: Player?,
        id: String,
        amount: Int?,
        random: String?,
        data: String?
    ) {
        player?.let {
            when (random) {
                "false", "0" -> {
                    // 获取数量
                    amount?.let {
                        // 给物品
                        ItemManager.getItemStack(id, player, data)?.let { itemStack ->
                            bukkitScheduler.callSyncMethod(plugin, Callable {
                                player.giveItems(itemStack, amount.coerceAtLeast(1))
                            })
                            sender.sendMessage(config.getString("Messages.successInfo")
                                ?.replace("{player}", player.name)
                                ?.replace("{amount}", amount.toString())
                                ?.replace("{name}", itemStack.getName()))
                            player.sendMessage(config.getString("Messages.givenInfo")
                                ?.replace("{amount}", amount.toString())
                                ?.replace("{name}", itemStack.getName()))
                            // 未知物品ID
                        } ?: let {
                            sender.sendMessage(config.getString("Messages.unknownItem")?.replace("{itemID}", id))
                        }
                        // 无效数字
                    } ?: let {
                        sender.sendMessage(config.getString("Messages.invalidAmount"))
                    }
                }
                else -> {
                    // 获取数量
                    amount?.let {
                        val dropData = HashMap<String, Int>()
                        // 给物品
                        repeat(amount.coerceAtLeast(1)) {
                            ItemManager.getItemStack(id, player, data)?.let { itemStack ->
                                bukkitScheduler.callSyncMethod(plugin, Callable {
                                    player.giveItem(itemStack)
                                })
                                dropData[itemStack.getName()] = dropData[itemStack.getName()]?.let { it + 1 } ?: let { 1 }
                                // 未知物品ID
                            } ?: let {
                                sender.sendMessage(config.getString("Messages.unknownItem")?.replace("{itemID}", id))
                                return@repeat
                            }
                        }
                        for ((name, amt) in dropData) {
                            sender.sendMessage(config.getString("Messages.successInfo")
                                ?.replace("{player}", player.name)
                                ?.replace("{amount}", amt.toString())
                                ?.replace("{name}", name))
                            player.sendMessage(config.getString("Messages.givenInfo")
                                ?.replace("{amount}", amt.toString())
                                ?.replace("{name}", name))
                        }
                        // 无效数字
                    } ?: let {
                        sender.sendMessage(config.getString("Messages.invalidAmount"))
                    }
                }
            }
            // 无效玩家
        } ?: let {
            sender.sendMessage(config.getString("Messages.invalidPlayer"))
        }
    }

    private fun giveAddonCommand(
        sender: CommandSender,
        player: Player?,
        id: String,
        itemStack: ItemStack?,
        amount: Int?
    ) {
        submit (async = true) {
            player?.let {
                // 获取数量
                amount?.let {
                    // 给物品
                    itemStack?.let {
                        bukkitScheduler.callSyncMethod(plugin, Callable {
                            player.giveItems(itemStack, amount.coerceAtLeast(1))
                        })
                        sender.sendMessage(config.getString("Messages.successInfo")
                            ?.replace("{player}", player.name)
                            ?.replace("{amount}", amount.toString())
                            ?.replace("{name}", itemStack.getName()))
                        player.sendMessage(config.getString("Messages.givenInfo")
                            ?.replace("{amount}", amount.toString())
                            ?.replace("{name}", itemStack.getName()))
                        // 未知物品ID
                    } ?: let {
                        sender.sendMessage(config.getString("Messages.unknownItem")?.replace("{itemID}", id))
                    }
                    // 无效数字
                } ?: let {
                    sender.sendMessage(config.getString("Messages.invalidAmount"))
                }
                // 无效玩家
            } ?: let {
                sender.sendMessage(config.getString("Messages.invalidPlayer"))
            }
        }
    }

    private fun dropCommand(
        // 行为发起人, 用于接收反馈信息
        sender: CommandSender,
        // 待掉落物品ID
        id: String,
        // 掉落数量
        amount: String,
        // 掉落世界名
        worldName: String,
        // 掉落世界x坐标
        xString: String,
        // 掉落世界y坐标
        yString: String,
        // 掉落世界z坐标
        zString: String,
        // 是否反复随机
        random: String,
        // 物品解析对象, 用于生成物品
        parser: String,
        // 指向数据
        data: String?
    ) {
        Bukkit.getWorld(worldName)?.let { world ->
            val x = xString.toDoubleOrNull()
            val y = yString.toDoubleOrNull()
            val z = zString.toDoubleOrNull()
            if (x != null && y != null && z != null) {
                dropCommand(sender, id, amount.toIntOrNull(), Location(world, x, y, z), random, Bukkit.getPlayerExact(parser), data)
            } else {
                sender.sendMessage(config.getString("Messages.invalidLocation"))
            }
        } ?: let {
            sender.sendMessage(config.getString("Messages.invalidWorld"))
        }
    }

    private fun dropCommandAsync(
        sender: CommandSender,
        id: String,
        amount: String,
        worldName: String,
        xString: String,
        yString: String,
        zString: String,
        random: String,
        parser: String,
        data: String? = null
    ) {
        submit(async = true) {
            dropCommand(sender, id, amount, worldName, xString, yString, zString, random, parser, data)
        }
    }

    private fun dropCommand(
        sender: CommandSender,
        id: String,
        amount: Int?,
        location: Location?,
        random: String,
        parser: Player?,
        data: String?
    ) {
        parser?.let {
            when (random) {
                "false", "0" -> {
                    // 获取数量
                    amount?.let {
                        // 掉物品
                        ItemManager.getItemStack(id, parser, data)?.let { itemStack ->
                            bukkitScheduler.callSyncMethod(plugin, Callable {
                                location?.dropItems(itemStack, amount.coerceAtLeast(1))
                            })
                            sender.sendMessage(config.getString("Messages.dropSuccessInfo")
                                ?.replace("{world}", location?.world?.name ?: "")
                                ?.replace("{x}", location?.x.toString())
                                ?.replace("{y}", location?.y.toString())
                                ?.replace("{z}", location?.z.toString())
                                ?.replace("{amount}", amount.toString())
                                ?.replace("{name}", itemStack.getName()))
                            // 未知物品ID
                        } ?: let {
                            sender.sendMessage(config.getString("Messages.unknownItem")?.replace("{itemID}", id))
                        }
                        // 无效数字
                    } ?: let {
                        sender.sendMessage(config.getString("Messages.invalidAmount"))
                    }
                }
                else -> {
                    // 获取数量
                    amount?.let {
                        val dropData = HashMap<String, Int>()
                        // 掉物品
                        repeat(amount.coerceAtLeast(1)) {
                            ItemManager.getItemStack(id, parser, data)?.let { itemStack ->
                                bukkitScheduler.callSyncMethod(plugin, Callable {
                                    location?.dropItems(itemStack)
                                })
                                dropData[itemStack.getName()] = dropData[itemStack.getName()]?.let { it + 1 } ?: let { 1 }
                                // 未知物品ID
                            } ?: let {
                                sender.sendMessage(config.getString("Messages.unknownItem")?.replace("{itemID}", id))
                                return@repeat
                            }
                        }
                        for((name, amt) in dropData) {
                            sender.sendMessage(config.getString("Messages.dropSuccessInfo")
                                ?.replace("{world}", location?.world?.name ?: "")
                                ?.replace("{x}", location?.x.toString())
                                ?.replace("{y}", location?.y.toString())
                                ?.replace("{z}", location?.z.toString())
                                ?.replace("{amount}", amt.toString())
                                ?.replace("{name}", name))
                        }
                        // 无效数字
                    } ?: let {
                        sender.sendMessage(config.getString("Messages.invalidAmount"))
                    }
                }
            }
            // 未知解析对象
        } ?: let {
            sender.sendMessage(config.getString("Messages.invalidParser"))
        }
    }

    private fun reloadCommand(sender: CommandSender) {
        submit(async = true) {
            ConfigManager.reload()
            ItemManager.reload()
            ScriptManager.reload()
            SectionManager.reload()
            ActionManager.reload()
            sender.sendMessage(config.getString("Messages.reloadedMessage"))
        }
    }

    private fun help(sender: CommandSender) {
        config.getStringList("Messages.helpMessages").forEach {
            sender.sendMessage(it)
        }
    }

    private fun help(sender: ProxyCommandSender) {
        config.getStringList("Messages.helpMessages").forEach {
            sender.sendMessage(it)
        }
    }
}