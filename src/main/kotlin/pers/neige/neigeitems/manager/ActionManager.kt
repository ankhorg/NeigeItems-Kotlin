package pers.neige.neigeitems.manager

import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.NeigeItems.bukkitScheduler
import pers.neige.neigeitems.NeigeItems.plugin
import pers.neige.neigeitems.item.ItemAction
import pers.neige.neigeitems.item.ItemConfig
import pers.neige.neigeitems.manager.ConfigManager.config
import pers.neige.neigeitems.manager.HookerManager.papiHooker
import pers.neige.neigeitems.manager.HookerManager.vaultHooker
import pers.neige.neigeitems.utils.ConfigUtils
import pers.neige.neigeitems.utils.PlayerUtils.getMetadataEZ
import pers.neige.neigeitems.utils.PlayerUtils.setMetadataEZ
import pers.neige.neigeitems.utils.SectionUtils.parseItemSection
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.nms.ItemTag
import taboolib.module.nms.ItemTagData
import taboolib.module.nms.getItemTag
import taboolib.platform.util.actionBar
import taboolib.platform.util.giveItem
import java.io.File
import java.text.DecimalFormat
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.ConcurrentHashMap
import java.util.function.BiFunction

object ActionManager {
    val itemActions: ConcurrentHashMap<String, ItemAction> = ConcurrentHashMap<String, ItemAction>()

    val actions = HashMap<String, BiFunction<Player, String, Boolean>>()

    init {
        loadItemActions()
        loadBasicActions()
    }

    fun reload() {
        itemActions.clear()
        loadItemActions()
    }

    fun runAction(player: Player, action: List<String>, itemTag: ItemTag? = null) {
        for (value in action) {
            if (!runAction(player, value, itemTag)) break
        }
    }

    fun runAction(player: Player, action: String, itemTag: ItemTag? = null): Boolean {
        var actionString: String = ""
        itemTag?.let {
            actionString = action.parseItemSection(itemTag).replace("\\<", "<").replace("\\>", ">")
        } ?: let {
            actionString = action
        }
        var actionType = action.lowercase(Locale.getDefault())
        var actionContent = ""
        val index = action.indexOf(": ")
        if (index != -1) {
            actionType = action.substring(0, index).lowercase(Locale.getDefault())
            actionContent = action.substring(index+2, action.length)
        }
        actions[actionType]?.let {
            val actionFunction: BiFunction<Player, String, Boolean> = it
            return actionFunction.apply(player, actionContent)
        }
        return true
    }

    private fun loadItemActions() {
        for (file: File in ConfigUtils.getAllFiles("ItemActions")) {
            val config = YamlConfiguration.loadConfiguration(file)
            config.getKeys(false).forEach { id ->
                config.getConfigurationSection(id)?.let {
                    itemActions[id] = ItemAction(id, it)
                }
            }
        }
    }

    fun addAction(id: String, function: BiFunction<Player, String, Boolean>) {
        actions[id.lowercase(Locale.getDefault())] = function
    }

    private fun loadBasicActions() {
        // 向玩家发送消息
        addAction("tell", BiFunction<Player, String, Boolean> { player, string ->
            papiHooker?.let {
                player.sendMessage(papiHooker.papi(player, ChatColor.translateAlternateColorCodes('&', string)))
            } ?: let {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', string))
            }
            true
        })
        // 向玩家发送消息(不将&解析为颜色符号)
        addAction("tellNoColor", BiFunction<Player, String, Boolean> { player, string ->
            papiHooker?.let {
                player.sendMessage(papiHooker.papi(player, string))
            } ?: let {
                player.sendMessage(string)
            }
            true
        })
        // 强制玩家发送消息
        addAction("chat", BiFunction<Player, String, Boolean> { player, string ->
            papiHooker?.let {
                player.chat(papiHooker.papi(player, string))
            } ?: let {
                player.chat(string)
            }
            true
        })
        // 强制玩家发送消息(将&解析为颜色符号)
        addAction("chatWithColor", BiFunction<Player, String, Boolean> { player, string ->
            papiHooker?.let {
                player.chat(papiHooker.papi(player, ChatColor.translateAlternateColorCodes('&', string)))
            } ?: let {
                player.chat(ChatColor.translateAlternateColorCodes('&', string))
            }
            true
        })
        // 强制玩家执行指令
        addAction("command", BiFunction<Player, String, Boolean> { player, string ->
            bukkitScheduler.callSyncMethod(plugin, Callable {
                papiHooker?.let {
                    Bukkit.dispatchCommand(player, papiHooker.papi(player, ChatColor.translateAlternateColorCodes('&', string)))
                } ?: let {
                    Bukkit.dispatchCommand(player, ChatColor.translateAlternateColorCodes('&', string))
                }
            })
            true
        })
        // 强制玩家执行指令
        actions["command"]?.let { addAction("player", it) }
        // 强制玩家执行指令(不将&解析为颜色符号)
        addAction("commandNoColor", BiFunction<Player, String, Boolean> { player, string ->
            bukkitScheduler.callSyncMethod(plugin, Callable {
                papiHooker?.let {
                    Bukkit.dispatchCommand(player, papiHooker.papi(player, string))
                } ?: let {
                    Bukkit.dispatchCommand(player, string)
                }
            })
            true
        })
        // 后台执行指令
        addAction("console", BiFunction<Player, String, Boolean> { player, string ->
            bukkitScheduler.callSyncMethod(plugin, Callable {
                papiHooker?.let {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), papiHooker.papi(player, ChatColor.translateAlternateColorCodes('&', string)))
                } ?: let {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ChatColor.translateAlternateColorCodes('&', string))
                }
            })
            true
        })
        // 后台执行指令(不将&解析为颜色符号)
        addAction("consoleNoColor", BiFunction<Player, String, Boolean> { player, string ->
            bukkitScheduler.callSyncMethod(plugin, Callable {
                papiHooker?.let {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), papiHooker.papi(player, string))
                } ?: let {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), string)
                }
            })
            true
        })
        // 给予玩家金钱
        addAction("giveMoney", BiFunction<Player, String, Boolean> { player, string ->
            papiHooker?.let {
                vaultHooker?.giveMoney(player, papiHooker.papi(player, string).toDoubleOrNull() ?: 0.toDouble())
            } ?: let {
                vaultHooker?.giveMoney(player, string.toDoubleOrNull() ?: 0.toDouble())
            }
            true
        })
        // 扣除玩家金钱
        addAction("takeMoney", BiFunction<Player, String, Boolean> { player, string ->
            papiHooker?.let {
                vaultHooker?.takeMoney(player, papiHooker.papi(player, string).toDoubleOrNull() ?: 0.toDouble())
            } ?: let {
                vaultHooker?.takeMoney(player, string.toDoubleOrNull() ?: 0.toDouble())
            }
            true
        })
        // 给予玩家经验
        addAction("giveExp", BiFunction<Player, String, Boolean> { player, string ->
            papiHooker?.let {
                player.giveExp(papiHooker.papi(player, string).toIntOrNull() ?: 0)
            } ?: let {
                player.giveExp(string.toIntOrNull() ?: 0)
            }
            true
        })
        // 扣除玩家经验
        addAction("takeExp", BiFunction<Player, String, Boolean> { player, string ->
            papiHooker?.let {
                player.giveExp((papiHooker.papi(player, string).toIntOrNull() ?: 0)*-1)
            } ?: let {
                player.giveExp((string.toIntOrNull() ?: 0)*-1)
            }
            true
        })
        // 设置玩家经验
        addAction("setExp", BiFunction<Player, String, Boolean> { player, string ->
            papiHooker?.let {
                player.exp = papiHooker.papi(player, string).toFloatOrNull() ?: 0.toFloat()
            } ?: let {
                player.exp = string.toFloatOrNull() ?: 0.toFloat()
            }
            true
        })
        // 给予玩家经验等级
        addAction("giveLevel", BiFunction<Player, String, Boolean> { player, string ->
            papiHooker?.let {
                player.giveExpLevels(papiHooker.papi(player, string).toIntOrNull() ?: 0)
            } ?: let {
                player.giveExpLevels(string.toIntOrNull() ?: 0)
            }
            true
        })
        // 扣除玩家经验等级
        addAction("takeLevel", BiFunction<Player, String, Boolean> { player, string ->
            papiHooker?.let {
                player.giveExpLevels((papiHooker.papi(player, string).toIntOrNull() ?: 0)*-1)
            } ?: let {
                player.giveExpLevels((string.toIntOrNull() ?: 0)*-1)
            }
            true
        })
        // 设置玩家经验等级
        addAction("setLevel", BiFunction<Player, String, Boolean> { player, string ->
            papiHooker?.let {
                player.level = papiHooker.papi(player, string).toIntOrNull() ?: 0
            } ?: let {
                player.level = string.toIntOrNull() ?: 0
            }
            true
        })
        // 延迟(单位是tick)
        addAction("delay", BiFunction<Player, String, Boolean> { player, string ->
            papiHooker?.let {
                Thread.sleep((papiHooker.papi(player, string).toLongOrNull() ?: 0)*50)
            } ?: let {
                Thread.sleep((string.toLongOrNull() ?: 0)*50)
            }
            true
        })
        // 终止
        addAction("return", BiFunction<Player, String, Boolean> { player, string ->
            false
        })
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    fun listener(event: PlayerInteractEvent) {
        // 获取玩家
        val player = event.player
        // 获取手持物品
        val itemStack = event.item
        if (itemStack == null || itemStack.type == Material.AIR) return
        // 获取交互类型
        val action = event.action
        val leftAction = (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK)
        val rightAction = (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)
        // 获取物品NBT
        val itemTag = itemStack.getItemTag()
        // 如果为非NI物品则终止操作
        val neigeItems = itemTag["NeigeItems"]?.asCompound() ?: let { return }
        // 获取物品id
        val id = neigeItems["id"]?.asString() ?: let { return }
        // 获取物品动作
        val itemAction = itemActions[id] ?: let { return }
        // 获取物品消耗信息
        val consume =  itemAction.consume
        // 如果物品配置了消耗事件
        if (consume != null) {
            // 获取待消耗数量
            val amount: Int = consume.getInt("amount", 1)
            // 获取物品使用次数
            val charge = neigeItems["charge"]
            // 检测数量是否充足/是否存在使用次数
            if (itemStack.amount >= amount || charge != null) {
                // 获取左键是否消耗
                val left = (leftAction && consume.getBoolean("left", false))
                // 获取右键是否消耗
                val right = (rightAction && consume.getBoolean("right", false))
                // 获取右键是否消耗
                val all = ((leftAction || rightAction) && consume.getBoolean("all", false))
                // 如果该物品需要被消耗
                if (left || right) {
                    // 取消交互事件
                    event.isCancelled = true
                    // 获取冷却
                    val cooldown = consume.getLong("cooldown", 0)
                    // 如果冷却存在且大于0
                    if (cooldown > 0) {
                        // 获取当前时间
                        val time = Date().time
                        // 获取上次使用时间
                        val lastTime = player.getMetadataEZ("NI-Consume-CD-$id", "Long", 0.toLong()) as Long
                        // 如果仍处于冷却时间
                        if ((lastTime + cooldown) > time) {
                            config.getString("Messages.itemCooldown")?.let {
                                val message = it.replace("{time}", DecimalFormat("0.#").format((lastTime + cooldown - time)/1000))
                                player.actionBar(message)
                            }
                            // 终止操作
                            return
                        }
                        player.setMetadataEZ("NI-Consume-CD-$id", time)
                    }
                    // 如果物品存在使用次数
                    if (charge != null) {
                        var itemClone: ItemStack? = null
                        // 拆分物品
                        if (itemStack.amount != 1) {
                            itemClone = itemStack.clone()
                            itemClone.amount = itemClone.amount - 1
                            itemStack.amount = 1
                        }
                        // 更新次数
                        val chargeInt = charge.asInt()
                        if (chargeInt == 1) {
                            itemStack.amount = 0
                        } else {
                            neigeItems["charge"] = ItemTagData(chargeInt - 1)
                            itemTag.saveTo(itemStack)
                        }
                        if (itemClone != null) player.giveItem(itemClone)
                    } else {
                        // 消耗物品
                        itemStack.amount = itemStack.amount - amount
                    }
                    // 执行动作
                    bukkitScheduler.runTaskAsynchronously(plugin, Runnable {
                        itemAction.all?.let {
                            when (it) {
                                is String -> runAction(player, it, itemTag)
                                is List<*> -> runAction(player, it as List<String>, itemTag)
                                else -> return@Runnable
                            }
                        }
                        if (left) {
                            itemAction.left?.let {
                                when (it) {
                                    is String -> runAction(player, it, itemTag)
                                    is List<*> -> runAction(player, it as List<String>, itemTag)
                                    else -> return@Runnable
                                }
                            }
                        }
                        if (right) {
                            itemAction.right?.let {
                                when (it) {
                                    is String -> runAction(player, it, itemTag)
                                    is List<*> -> runAction(player, it as List<String>, itemTag)
                                    else -> return@Runnable
                                }
                            }
                        }
                    })
                }
            }
        } else {
            // 取消交互事件
            event.isCancelled = true
            bukkitScheduler.runTaskAsynchronously(plugin, Runnable {
                // 获取冷却
                val cooldown = itemAction.cooldown
                // 如果冷却存在且大于0
                if (cooldown > 0) {
                    // 获取当前时间
                    val time = Date().time
                    // 获取上次使用时间
                    val lastTime = player.getMetadataEZ("NI-CD-$id", "Long", 0.toLong()) as Long
                    // 如果仍处于冷却时间
                    if ((lastTime + cooldown) > time) {
                        config.getString("Messages.itemCooldown")?.let {
                            val message = it.replace("{time}", DecimalFormat("0.#").format((lastTime + cooldown - time)/1000))
                            player.actionBar(message)
                        }
                        // 终止操作
                        return@Runnable
                    }
                    player.setMetadataEZ("NI-CD-$id", time)
                }
                // 执行动作
                itemAction.all?.let {
                    when (it) {
                        is String -> runAction(player, it, itemTag)
                        is List<*> -> runAction(player, it as List<String>, itemTag)
                        else -> return@Runnable
                    }
                }
                if (leftAction) {
                    itemAction.left?.let {
                        when (it) {
                            is String -> runAction(player, it, itemTag)
                            is List<*> -> runAction(player, it as List<String>, itemTag)
                            else -> return@Runnable
                        }
                    }
                }
                if (rightAction) {
                    itemAction.right?.let {
                        when (it) {
                            is String -> runAction(player, it, itemTag)
                            is List<*> -> runAction(player, it as List<String>, itemTag)
                            else -> return@Runnable
                        }
                    }
                }
            })
        }
    }
}