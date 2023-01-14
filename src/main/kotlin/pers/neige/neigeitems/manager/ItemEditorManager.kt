package pers.neige.neigeitems.manager

import com.alibaba.fastjson2.parseObject
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.manager.HookerManager.papi
import pers.neige.neigeitems.manager.HookerManager.papiColor
import pers.neige.neigeitems.utils.ConfigUtils
import pers.neige.neigeitems.utils.ItemUtils.castToItemTagData
import pers.neige.neigeitems.utils.ItemUtils.putDeepFixed
import pers.neige.neigeitems.utils.ItemUtils.putDeepWithList
import pers.neige.neigeitems.utils.SectionUtils.parseSection
import pers.neige.neigeitems.utils.function.TriConsumer
import pers.neige.neigeitems.utils.function.TriFunction
import taboolib.module.nms.getItemTag
import java.util.*

/**
 * 用于管理所有物品编辑函数
 */
object ItemEditorManager {
    /**
     * 获取所有物品编辑函数
     */
    val itemEditors: HashMap<String, TriFunction<Player, ItemStack, String, Boolean?>> = HashMap<String, TriFunction<Player, ItemStack, String, Boolean?>>()

    /**
     * 获取所有物品编辑函数名
     */
    val editorNames: ArrayList<String> = ArrayList<String>()

    init {
        // 加载基础物品编辑函数
        loadBasicItemEditors()
        // 加载自定义物品编辑函数
        loadCustomItemEditors()
    }

    /**
     * 重载物品编辑函数管理器
     */
    fun reload() {
        itemEditors.clear()
        editorNames.clear()
        loadBasicItemEditors()
        loadCustomItemEditors()
    }

    /**
     * 使用物品编辑函数
     *
     * @param id 函数ID
     * @param player 物品拥有者
     * @param itemStack 待编辑物品
     * @param content 传入的文本
     */
    fun runEditor(id: String, player: Player, itemStack: ItemStack, content: String) {
        itemEditors[id.lowercase(Locale.getDefault())]?.apply(player, itemStack, content)
    }

    /**
     * 添加物品编辑函数
     *
     * @param id 函数ID
     * @param function 物品编辑执行函数
     */
    fun addItemEditor(id: String, function: TriFunction<Player, ItemStack, String, Boolean?>) {
        editorNames.add(id)
        itemEditors[id.lowercase(Locale.getDefault())] = function
    }

    /**
     * 加载自定义物品编辑函数
     */
    private fun loadCustomItemEditors() {
        for (file in ConfigUtils.getAllFiles("CustomItemEditors")) {
            // 防止某个脚本出错导致加载中断
            try {
                pers.neige.neigeitems.script.CompiledScript(file).invoke("main", null)
            } catch (error: Throwable) {}
        }
    }

    /**
     * 加载基础物品编辑函数
     */
    private fun loadBasicItemEditors() {
        // 给物品设置材质
        addItemEditor("setMaterial") { _, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取对应材质
                Material.matchMaterial(content.uppercase(Locale.getDefault()))?.let { material ->
                    // 设置物品材质
                    itemStack.type = material
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品设置材质(解析其中的papi变量)
        addItemEditor("setMaterialPapi") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取对应材质
                Material.matchMaterial(papi(player, content).uppercase(Locale.getDefault()))?.let { material ->
                    // 设置物品材质
                    itemStack.type = material
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品设置材质(解析其中的即时声明节点)
        addItemEditor("setMaterialSection") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取对应材质
                Material.matchMaterial(content.parseSection(player).uppercase(Locale.getDefault()))?.let { material ->
                    // 设置物品材质
                    itemStack.type = material
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品设置数量
        addItemEditor("setAmount") { _, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取对应数量
                content.toIntOrNull()?.let { amount ->
                    itemStack.amount = amount.coerceAtLeast(0).coerceAtMost(itemStack.type.maxStackSize)
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品设置数量(解析其中的papi变量)
        addItemEditor("setAmountPapi") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取对应数量
                papi(player, content).toIntOrNull()?.let { amount ->
                    itemStack.amount = amount.coerceAtLeast(0).coerceAtMost(itemStack.type.maxStackSize)
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品设置数量(解析其中的即时声明节点)
        addItemEditor("setAmountSection") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取对应数量
                content.parseSection(player).toIntOrNull()?.let { amount ->
                    itemStack.amount = amount.coerceAtLeast(0).coerceAtMost(itemStack.type.maxStackSize)
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品添加数量
        addItemEditor("addAmount") { _, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取对应数量
                content.toIntOrNull()?.let { amount ->
                    itemStack.amount = (itemStack.amount + amount).coerceAtLeast(0).coerceAtMost(itemStack.type.maxStackSize)
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品添加数量(解析其中的papi变量)
        addItemEditor("addAmountPapi") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取对应数量
                papi(player, content).toIntOrNull()?.let { amount ->
                    itemStack.amount = (itemStack.amount + amount).coerceAtLeast(0).coerceAtMost(itemStack.type.maxStackSize)
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品添加数量(解析其中的即时声明节点)
        addItemEditor("addAmountSection") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取对应数量
                content.parseSection(player).toIntOrNull()?.let { amount ->
                    itemStack.amount = (itemStack.amount + amount).coerceAtLeast(0).coerceAtMost(itemStack.type.maxStackSize)
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品扣除数量
        addItemEditor("takeAmount") { _, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取对应数量
                content.toIntOrNull()?.let { amount ->
                    itemStack.amount = (itemStack.amount - amount).coerceAtLeast(0).coerceAtMost(itemStack.type.maxStackSize)
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品扣除数量(解析其中的papi变量)
        addItemEditor("takeAmountPapi") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取对应数量
                papi(player, content).toIntOrNull()?.let { amount ->
                    itemStack.amount = (itemStack.amount - amount).coerceAtLeast(0).coerceAtMost(itemStack.type.maxStackSize)
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品扣除数量(解析其中的即时声明节点)
        addItemEditor("takeAmountSection") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取对应数量
                content.parseSection(player).toIntOrNull()?.let { amount ->
                    itemStack.amount = (itemStack.amount - amount).coerceAtLeast(0).coerceAtMost(itemStack.type.maxStackSize)
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品设置显示名
        addItemEditor("setName") { _, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取itemMeta
                itemStack.itemMeta?.let { itemMeta ->
                    // 获取并设置显示名
                    itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', content))
                    // 将改动完成的itemMeta设置回去
                    itemStack.itemMeta = itemMeta
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品显示名添加前缀
        addItemEditor("addNamePrefix") { _, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取itemMeta
                itemStack.itemMeta?.let { itemMeta ->
                    // 获取并设置显示名
                    itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', content) + itemMeta.displayName)
                    // 将改动完成的itemMeta设置回去
                    itemStack.itemMeta = itemMeta
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品显示名添加后缀
        addItemEditor("addNamePostfix") { _, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取itemMeta
                itemStack.itemMeta?.let { itemMeta ->
                    // 获取并设置显示名
                    itemMeta.setDisplayName(itemMeta.displayName + ChatColor.translateAlternateColorCodes('&', content))
                    // 将改动完成的itemMeta设置回去
                    itemStack.itemMeta = itemMeta
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品设置显示名(解析其中的papi变量)
        addItemEditor("setNamePapi") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取itemMeta
                itemStack.itemMeta?.let { itemMeta ->
                    // 获取并设置显示名
                    itemMeta.setDisplayName(papiColor(player, content))
                    // 将改动完成的itemMeta设置回去
                    itemStack.itemMeta = itemMeta
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品显示名添加前缀(解析其中的papi变量)
        addItemEditor("addNamePrefixPapi") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取itemMeta
                itemStack.itemMeta?.let { itemMeta ->
                    // 获取并设置显示名
                    itemMeta.setDisplayName(papiColor(player, content) + itemMeta.displayName)
                    // 将改动完成的itemMeta设置回去
                    itemStack.itemMeta = itemMeta
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品显示名添加后缀(解析其中的papi变量)
        addItemEditor("addNamePostfixPapi") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取itemMeta
                itemStack.itemMeta?.let { itemMeta ->
                    // 获取并设置显示名
                    itemMeta.setDisplayName(itemMeta.displayName + papiColor(player, content))
                    // 将改动完成的itemMeta设置回去
                    itemStack.itemMeta = itemMeta
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品设置显示名(解析其中的即时声明节点)
        addItemEditor("setNameSection") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取itemMeta
                itemStack.itemMeta?.let { itemMeta ->
                    // 获取并设置显示名
                    itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', content.parseSection(player)))
                    // 将改动完成的itemMeta设置回去
                    itemStack.itemMeta = itemMeta
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品显示名添加前缀(解析其中的即时声明节点)
        addItemEditor("addNamePrefixSection") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取itemMeta
                itemStack.itemMeta?.let { itemMeta ->
                    // 获取并设置显示名
                    itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', content.parseSection(player)) + itemMeta.displayName)
                    // 将改动完成的itemMeta设置回去
                    itemStack.itemMeta = itemMeta
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品显示名添加后缀(解析其中的即时声明节点)
        addItemEditor("addNamePostfixSection") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取itemMeta
                itemStack.itemMeta?.let { itemMeta ->
                    // 获取并设置显示名
                    itemMeta.setDisplayName(itemMeta.displayName + ChatColor.translateAlternateColorCodes('&', content.parseSection(player)))
                    // 将改动完成的itemMeta设置回去
                    itemStack.itemMeta = itemMeta
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品添加lore
        addItemEditor("addLore") { _, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取itemMeta
                itemStack.itemMeta?.let { itemMeta ->
                    // 获取并设置lore
                    itemMeta.lore = (itemMeta.lore ?: ArrayList<String>()).also { lore ->
                        // 解析颜色符号, 通过\n换行, 最后添加到原lore中
                        lore.addAll(ChatColor.translateAlternateColorCodes('&', content).split("\\n"))
                    }
                    // 将改动完成的itemMeta设置回去
                    itemStack.itemMeta = itemMeta
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品设置lore
        addItemEditor("setLore") { _, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取itemMeta
                itemStack.itemMeta?.let { itemMeta ->
                    // 解析颜色符号, 通过\n换行, 设置lore
                    itemMeta.lore = ChatColor.translateAlternateColorCodes('&', content).split("\\n")
                    // 将改动完成的itemMeta设置回去
                    itemStack.itemMeta = itemMeta
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品添加lore(解析其中的papi变量)
        addItemEditor("addLorePapi") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取itemMeta
                itemStack.itemMeta?.let { itemMeta ->
                    // 获取并设置lore
                    itemMeta.lore = (itemMeta.lore ?: ArrayList<String>()).also { lore ->
                        // 解析颜色符号与papi变量, 通过\n换行, 最后添加到原lore中
                        lore.addAll(papiColor(player, content).split("\\n"))
                    }
                    // 将改动完成的itemMeta设置回去
                    itemStack.itemMeta = itemMeta
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品设置lore(解析其中的papi变量)
        addItemEditor("setLorePapi") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取itemMeta
                itemStack.itemMeta?.let { itemMeta ->
                    // 解析颜色符号与papi变量, 通过\n换行, 设置lore
                    itemMeta.lore = papiColor(player, content).split("\\n")
                    // 将改动完成的itemMeta设置回去
                    itemStack.itemMeta = itemMeta
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品添加lore(解析其中的即时声明节点)
        addItemEditor("addLoreSection") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取itemMeta
                itemStack.itemMeta?.let { itemMeta ->
                    // 获取并设置lore
                    itemMeta.lore = (itemMeta.lore ?: ArrayList<String>()).also { lore ->
                        // 解析颜色符号与即时声明节点, 通过\n换行, 最后添加到原lore中
                        lore.addAll(ChatColor.translateAlternateColorCodes('&', content.parseSection(player)).split("\\n"))
                    }
                    // 将改动完成的itemMeta设置回去
                    itemStack.itemMeta = itemMeta
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品设置lore(解析其中的即时声明节点)
        addItemEditor("setLoreSection") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取itemMeta
                itemStack.itemMeta?.let { itemMeta ->
                    // 解析颜色符号与即时声明节点, 通过\n换行, 设置lore
                    itemMeta.lore = ChatColor.translateAlternateColorCodes('&', content.parseSection(player)).split("\\n")
                    // 将改动完成的itemMeta设置回去
                    itemStack.itemMeta = itemMeta
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品设置子ID/损伤值
        addItemEditor("setDamage") { _, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取待设置子ID/损伤值
                content.toShortOrNull()?.let { damage ->
                    // 获取物品最大耐久
                    val maxDurability = itemStack.type.maxDurability
                    // 如果最大耐久不为0(属于有耐久物品)
                    if (maxDurability != 0.toShort()) {
                        // 如果设置的损伤值超过了最大耐久
                        if (damage > maxDurability) {
                            // 直接扬了
                            itemStack.amount = 0
                        } else {
                            // 设置物品材质子ID/损伤值
                            itemStack.durability = damage.coerceAtLeast(0)
                        }
                    // 如果物品属于无耐久物品
                    } else {
                        // 设置物品材质子ID/损伤值
                        itemStack.durability = damage
                    }
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品设置子ID/损伤值(解析其中的papi变量)
        addItemEditor("setDamagePapi") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取待设置子ID/损伤值
                papi(player, content).toShortOrNull()?.let { damage ->
                    // 获取物品最大耐久
                    val maxDurability = itemStack.type.maxDurability
                    // 如果最大耐久不为0(属于有耐久物品)
                    if (maxDurability != 0.toShort()) {
                        // 如果设置的损伤值超过了最大耐久
                        if (damage > maxDurability) {
                            // 直接扬了
                            itemStack.amount = 0
                        } else {
                            // 设置物品材质子ID/损伤值
                            itemStack.durability = damage.coerceAtLeast(0)
                        }
                    // 如果物品属于无耐久物品
                    } else {
                        // 设置物品材质子ID/损伤值
                        itemStack.durability = damage
                    }
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品设置子ID/损伤值(解析其中的即时声明节点)
        addItemEditor("setDamageSection") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取待设置子ID/损伤值
                content.parseSection(player).toShortOrNull()?.let { damage ->
                    // 获取物品最大耐久
                    val maxDurability = itemStack.type.maxDurability
                    // 如果最大耐久不为0(属于有耐久物品)
                    if (maxDurability != 0.toShort()) {
                        // 如果设置的损伤值超过了最大耐久
                        if (damage > maxDurability) {
                            // 直接扬了
                            itemStack.amount = 0
                        } else {
                            // 设置物品材质子ID/损伤值
                            itemStack.durability = damage.coerceAtLeast(0)
                        }
                    // 如果物品属于无耐久物品
                    } else {
                        // 设置物品材质子ID/损伤值
                        itemStack.durability = damage
                    }
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品增加子ID/损伤值
        addItemEditor("addDamage") { _, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取待设置子ID/损伤值
                content.toShortOrNull()?.let { damage ->
                    // 计算目标值
                    val result = (itemStack.durability + damage).toShort()
                    // 获取物品最大耐久
                    val maxDurability = itemStack.type.maxDurability
                    // 如果最大耐久不为0(属于有耐久物品)
                    if (maxDurability != 0.toShort()) {
                        // 如果设置的损伤值超过了最大耐久
                        if (result > maxDurability) {
                            // 直接扬了
                            itemStack.amount = 0
                        } else {
                            // 设置物品材质子ID/损伤值
                            itemStack.durability = result.coerceAtLeast(0)
                        }
                        // 如果物品属于无耐久物品
                    } else {
                        // 设置物品材质子ID/损伤值
                        itemStack.durability = result
                    }
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品增加子ID/损伤值(解析其中的papi变量)
        addItemEditor("addDamagePapi") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取待设置子ID/损伤值
                papi(player, content).toShortOrNull()?.let { damage ->
                    // 计算目标值
                    val result = (itemStack.durability + damage).toShort()
                    // 获取物品最大耐久
                    val maxDurability = itemStack.type.maxDurability
                    // 如果最大耐久不为0(属于有耐久物品)
                    if (maxDurability != 0.toShort()) {
                        // 如果设置的损伤值超过了最大耐久
                        if (result > maxDurability) {
                            // 直接扬了
                            itemStack.amount = 0
                        } else {
                            // 设置物品材质子ID/损伤值
                            itemStack.durability = result.coerceAtLeast(0)
                        }
                        // 如果物品属于无耐久物品
                    } else {
                        // 设置物品材质子ID/损伤值
                        itemStack.durability = result
                    }
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品增加子ID/损伤值(解析其中的即时声明节点)
        addItemEditor("addDamageSection") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取待设置子ID/损伤值
                content.parseSection(player).toShortOrNull()?.let { damage ->
                    // 计算目标值
                    val result = (itemStack.durability + damage).toShort()
                    // 获取物品最大耐久
                    val maxDurability = itemStack.type.maxDurability
                    // 如果最大耐久不为0(属于有耐久物品)
                    if (maxDurability != 0.toShort()) {
                        // 如果设置的损伤值超过了最大耐久
                        if (result > maxDurability) {
                            // 直接扬了
                            itemStack.amount = 0
                        } else {
                            // 设置物品材质子ID/损伤值
                            itemStack.durability = result.coerceAtLeast(0)
                        }
                        // 如果物品属于无耐久物品
                    } else {
                        // 设置物品材质子ID/损伤值
                        itemStack.durability = result
                    }
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品减少子ID/损伤值
        addItemEditor("takeDamage") { _, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取待设置子ID/损伤值
                content.toShortOrNull()?.let { damage ->
                    // 计算目标值
                    val result = (itemStack.durability - damage).toShort()
                    // 获取物品最大耐久
                    val maxDurability = itemStack.type.maxDurability
                    // 如果最大耐久不为0(属于有耐久物品)
                    if (maxDurability != 0.toShort()) {
                        // 如果设置的损伤值超过了最大耐久
                        if (result > maxDurability) {
                            // 直接扬了
                            itemStack.amount = 0
                        } else {
                            // 设置物品材质子ID/损伤值
                            itemStack.durability = result.coerceAtLeast(0)
                        }
                        // 如果物品属于无耐久物品
                    } else {
                        // 设置物品材质子ID/损伤值
                        itemStack.durability = result
                    }
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品减少子ID/损伤值(解析其中的papi变量)
        addItemEditor("takeDamagePapi") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取待设置子ID/损伤值
                papi(player, content).toShortOrNull()?.let { damage ->
                    // 计算目标值
                    val result = (itemStack.durability - damage).toShort()
                    // 获取物品最大耐久
                    val maxDurability = itemStack.type.maxDurability
                    // 如果最大耐久不为0(属于有耐久物品)
                    if (maxDurability != 0.toShort()) {
                        // 如果设置的损伤值超过了最大耐久
                        if (result > maxDurability) {
                            // 直接扬了
                            itemStack.amount = 0
                        } else {
                            // 设置物品材质子ID/损伤值
                            itemStack.durability = result.coerceAtLeast(0)
                        }
                        // 如果物品属于无耐久物品
                    } else {
                        // 设置物品材质子ID/损伤值
                        itemStack.durability = result
                    }
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品减少子ID/损伤值(解析其中的即时声明节点)
        addItemEditor("takeDamageSection") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取待设置子ID/损伤值
                content.parseSection(player).toShortOrNull()?.let { damage ->
                    // 计算目标值
                    val result = (itemStack.durability - damage).toShort()
                    // 获取物品最大耐久
                    val maxDurability = itemStack.type.maxDurability
                    // 如果最大耐久不为0(属于有耐久物品)
                    if (maxDurability != 0.toShort()) {
                        // 如果设置的损伤值超过了最大耐久
                        if (result > maxDurability) {
                            // 直接扬了
                            itemStack.amount = 0
                        } else {
                            // 设置物品材质子ID/损伤值
                            itemStack.durability = result.coerceAtLeast(0)
                        }
                        // 如果物品属于无耐久物品
                    } else {
                        // 设置物品材质子ID/损伤值
                        itemStack.durability = result
                    }
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品设置CustomModelData
        addItemEditor("setCustomModelData") { _, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取itemMeta
                itemStack.itemMeta?.let { itemMeta ->
                    // 获取待设置CustomModelData
                    content.toIntOrNull()?.let { customModelData ->
                        try {
                            // 设置CustomModelData
                            itemMeta.setCustomModelData(customModelData)
                        } catch (_: NoSuchMethodError) {}
                    }
                    // 将改动完成的itemMeta设置回去
                    itemStack.itemMeta = itemMeta
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品设置CustomModelData(解析其中的papi变量)
        addItemEditor("setCustomModelDataPapi") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取itemMeta
                itemStack.itemMeta?.let { itemMeta ->
                    // 获取待设置CustomModelData
                    papi(player, content).toIntOrNull()?.let { customModelData ->
                        try {
                            // 设置CustomModelData
                            itemMeta.setCustomModelData(customModelData)
                        } catch (_: NoSuchMethodError) {}
                    }
                    // 将改动完成的itemMeta设置回去
                    itemStack.itemMeta = itemMeta
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品设置CustomModelData(解析其中的即时声明节点)
        addItemEditor("setCustomModelDataSection") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取itemMeta
                itemStack.itemMeta?.let { itemMeta ->
                    // 获取待设置CustomModelData
                    content.parseSection(player).toIntOrNull()?.let { customModelData ->
                        try {
                            // 设置CustomModelData
                            itemMeta.setCustomModelData(customModelData)
                        } catch (_: NoSuchMethodError) {}
                    }
                    // 将改动完成的itemMeta设置回去
                    itemStack.itemMeta = itemMeta
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品设置无法破坏
        addItemEditor("setUnbreakable") { _, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取itemMeta
                itemStack.itemMeta?.let { itemMeta ->
                    // 获取待设置无法破坏
                    content.lowercase(Locale.getDefault()).toBooleanStrictOrNull()?.let { unbreakable ->
                        itemMeta.isUnbreakable = unbreakable
                    }
                    // 将改动完成的itemMeta设置回去
                    itemStack.itemMeta = itemMeta
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品设置无法破坏(解析其中的papi变量)
        addItemEditor("setUnbreakablePapi") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取itemMeta
                itemStack.itemMeta?.let { itemMeta ->
                    // 获取待设置无法破坏
                    papi(player, content).lowercase(Locale.getDefault()).toBooleanStrictOrNull()?.let { unbreakable ->
                        itemMeta.isUnbreakable = unbreakable
                    }
                    // 将改动完成的itemMeta设置回去
                    itemStack.itemMeta = itemMeta
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品设置无法破坏(解析其中的即时声明节点)
        addItemEditor("setUnbreakableSection") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取itemMeta
                itemStack.itemMeta?.let { itemMeta ->
                    // 获取待设置无法破坏
                    content.parseSection(player).lowercase(Locale.getDefault()).toBooleanStrictOrNull()?.let { unbreakable ->
                        itemMeta.isUnbreakable = unbreakable
                    }
                    // 将改动完成的itemMeta设置回去
                    itemStack.itemMeta = itemMeta
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品设置附魔
        addItemEditor("setEnchantment") { _, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 移除物品原有附魔
                itemStack.enchantments.forEach { (enchantment, _) ->
                    itemStack.removeEnchantment(enchantment)
                }
                // 获取待设置附魔
                val enchantments = content.parseObject<HashMap<String, Int>>()
                // 遍历添加
                enchantments.forEach { (key, value) ->
                    val enchantment = Enchantment.getByName(key.uppercase(Locale.getDefault()))
                    val level = value
                    // 判断附魔名称 && 等级 是否合法
                    if (enchantment != null && level > 0) {
                        // 添加附魔
                        itemStack.addUnsafeEnchantment(enchantment, level)
                    }
                }
                return@addItemEditor true
            }
            return@addItemEditor false
        }
        // 给物品设置附魔(解析其中的papi变量)
        addItemEditor("setEnchantmentPapi") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 移除物品原有附魔
                itemStack.enchantments.forEach { (enchantment, _) ->
                    itemStack.removeEnchantment(enchantment)
                }
                // 获取待设置附魔
                val enchantments = papi(player, content).parseObject<HashMap<String, Int>>()
                // 遍历添加
                enchantments.forEach { (key, value) ->
                    val enchantment = Enchantment.getByName(key.uppercase(Locale.getDefault()))
                    val level = value
                    // 判断附魔名称 && 等级 是否合法
                    if (enchantment != null && level > 0) {
                        // 添加附魔
                        itemStack.addUnsafeEnchantment(enchantment, level)
                    }
                }
                return@addItemEditor true
            }
            return@addItemEditor false
        }
        // 给物品设置附魔(解析其中的即时声明节点)
        addItemEditor("setEnchantmentSection") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 移除物品原有附魔
                itemStack.enchantments.forEach { (enchantment, _) ->
                    itemStack.removeEnchantment(enchantment)
                }
                // 获取待设置附魔
                val enchantments = content.parseSection(player).parseObject<HashMap<String, Int>>()
                // 遍历添加
                enchantments.forEach { (key, value) ->
                    val enchantment = Enchantment.getByName(key.uppercase(Locale.getDefault()))
                    val level = value
                    // 判断附魔名称 && 等级 是否合法
                    if (enchantment != null && level > 0) {
                        // 添加附魔
                        itemStack.addUnsafeEnchantment(enchantment, level)
                    }
                }
                return@addItemEditor true
            }
            return@addItemEditor false
        }
        // 给物品添加附魔
        addItemEditor("addEnchantment") { _, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取待设置附魔
                val enchantments = content.parseObject<HashMap<String, Int>>()
                // 遍历添加
                enchantments.forEach { (key, value) ->
                    val enchantment = Enchantment.getByName(key.uppercase(Locale.getDefault()))
                    val level = value
                    // 判断附魔名称 && 等级 是否合法
                    if (enchantment != null && level > 0) {
                        // 添加附魔
                        itemStack.addUnsafeEnchantment(enchantment, level)
                    }
                }
                return@addItemEditor true
            }
            return@addItemEditor false
        }
        // 给物品添加附魔(解析其中的papi变量)
        addItemEditor("addEnchantmentPapi") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取待设置附魔
                val enchantments = papi(player, content).parseObject<HashMap<String, Int>>()
                // 遍历添加
                enchantments.forEach { (key, value) ->
                    val enchantment = Enchantment.getByName(key.uppercase(Locale.getDefault()))
                    val level = value
                    // 判断附魔名称 && 等级 是否合法
                    if (enchantment != null && level > 0) {
                        // 添加附魔
                        itemStack.addUnsafeEnchantment(enchantment, level)
                    }
                }
                return@addItemEditor true
            }
            return@addItemEditor false
        }
        // 给物品添加附魔(解析其中的即时声明节点)
        addItemEditor("addEnchantmentSection") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取待设置附魔
                val enchantments = content.parseSection(player).parseObject<HashMap<String, Int>>()
                // 遍历添加
                enchantments.forEach { (key, value) ->
                    val enchantment = Enchantment.getByName(key.uppercase(Locale.getDefault()))
                    val level = value
                    // 判断附魔名称 && 等级 是否合法
                    if (enchantment != null && level > 0) {
                        // 添加附魔
                        itemStack.addUnsafeEnchantment(enchantment, level)
                    }
                }
                return@addItemEditor true
            }
            return@addItemEditor false
        }
        // 给物品添加附魔(已存在的不覆盖)
        addItemEditor("addNotCoverEnchantment") { _, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取待设置附魔
                val enchantments = content.parseObject<HashMap<String, Int>>()
                // 遍历添加
                enchantments.forEach { (key, value) ->
                    val enchantment = Enchantment.getByName(key.uppercase(Locale.getDefault()))
                    val level = value
                    // 判断附魔名称 && 等级 是否合法
                    if (enchantment != null && level > 0) {
                        // 如果该物品不包含当前附魔
                        if (!itemStack.containsEnchantment(enchantment)) {
                            // 添加附魔
                            itemStack.addUnsafeEnchantment(enchantment, level)
                        }
                    }
                }
                return@addItemEditor true
            }
            return@addItemEditor false
        }
        // 给物品添加附魔(已存在的不覆盖)(解析其中的papi变量)
        addItemEditor("addNotCoverEnchantmentPapi") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取待设置附魔
                val enchantments = papi(player, content).parseObject<HashMap<String, Int>>()
                // 遍历添加
                enchantments.forEach { (key, value) ->
                    val enchantment = Enchantment.getByName(key.uppercase(Locale.getDefault()))
                    val level = value
                    // 判断附魔名称 && 等级 是否合法
                    if (enchantment != null && level > 0) {
                        // 如果该物品不包含当前附魔
                        if (!itemStack.containsEnchantment(enchantment)) {
                            // 添加附魔
                            itemStack.addUnsafeEnchantment(enchantment, level)
                        }
                    }
                }
                return@addItemEditor true
            }
            return@addItemEditor false
        }
        // 给物品添加附魔(已存在的不覆盖)(解析其中的即时声明节点)
        addItemEditor("addNotCoverEnchantmentSection") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取待设置附魔
                val enchantments = content.parseSection(player).parseObject<HashMap<String, Int>>()
                // 遍历添加
                enchantments.forEach { (key, value) ->
                    val enchantment = Enchantment.getByName(key.uppercase(Locale.getDefault()))
                    val level = value
                    // 判断附魔名称 && 等级 是否合法
                    if (enchantment != null && level > 0) {
                        // 如果该物品不包含当前附魔
                        if (!itemStack.containsEnchantment(enchantment)) {
                            // 添加附魔
                            itemStack.addUnsafeEnchantment(enchantment, level)
                        }
                    }
                }
                return@addItemEditor true
            }
            return@addItemEditor false
        }
        // 给物品移除附魔
        addItemEditor("removeEnchantment") { _, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取待移除附魔
                val enchantments = content.split(" ")
                // 遍历移除
                enchantments.forEach { value ->
                    // 获取当前待移除附魔
                    Enchantment.getByName(value.uppercase(Locale.getDefault()))?.let { enchantment ->
                        // 移除附魔
                        itemStack.removeEnchantment(enchantment)
                    }
                }
                return@addItemEditor true
            }
            return@addItemEditor false
        }
        // 给物品移除附魔(解析其中的papi变量)
        addItemEditor("removeEnchantmentPapi") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取待移除附魔
                val enchantments = papi(player, content).split(" ")
                // 遍历移除
                enchantments.forEach { value ->
                    // 获取当前待移除附魔
                    Enchantment.getByName(value.uppercase(Locale.getDefault()))?.let { enchantment ->
                        // 移除附魔
                        itemStack.removeEnchantment(enchantment)
                    }
                }
                return@addItemEditor true
            }
            return@addItemEditor false
        }
        // 给物品移除附魔(解析其中的即时声明节点)
        addItemEditor("removeEnchantmentSection") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取待移除附魔
                val enchantments = content.parseSection(player).split(" ")
                // 遍历移除
                enchantments.forEach { value ->
                    // 获取当前待移除附魔
                    Enchantment.getByName(value.uppercase(Locale.getDefault()))?.let { enchantment ->
                        // 移除附魔
                        itemStack.removeEnchantment(enchantment)
                    }
                }
                return@addItemEditor true
            }
            return@addItemEditor false
        }
        // 给物品附魔升级
        addItemEditor("levelUpEnchantment") { _, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取待设置附魔
                val enchantments = content.parseObject<HashMap<String, Int>>()
                // 遍历添加
                enchantments.forEach { (key, value) ->
                    val enchantment = Enchantment.getByName(key.uppercase(Locale.getDefault()))
                    var level = value
                    // 判断附魔名称 && 等级 是否合法
                    if (enchantment != null && level != 0) {
                        // 如果该物品包含当前附魔
                        if (itemStack.containsEnchantment(enchantment)) {
                            // 修改目标等级
                            level += itemStack.getEnchantmentLevel(enchantment)
                        }
                        // 如果目标等级大于零
                        if (level > 0) {
                            // 设置附魔
                            itemStack.addUnsafeEnchantment(enchantment, level)
                        // 如果目标等级小于零
                        } else {
                            // 移除附魔
                            itemStack.removeEnchantment(enchantment)
                        }
                    }
                }
                return@addItemEditor true
            }
            return@addItemEditor false
        }
        // 给物品附魔升级(解析其中的papi变量)
        addItemEditor("levelUpEnchantmentPapi") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取待设置附魔
                val enchantments = papi(player, content).parseObject<HashMap<String, Int>>()
                // 遍历添加
                enchantments.forEach { (key, value) ->
                    val enchantment = Enchantment.getByName(key.uppercase(Locale.getDefault()))
                    var level = value
                    // 判断附魔名称 && 等级 是否合法
                    if (enchantment != null && level != 0) {
                        // 如果该物品包含当前附魔
                        if (itemStack.containsEnchantment(enchantment)) {
                            // 修改目标等级
                            level += itemStack.getEnchantmentLevel(enchantment)
                        }
                        // 如果目标等级大于零
                        if (level > 0) {
                            // 设置附魔
                            itemStack.addUnsafeEnchantment(enchantment, level)
                            // 如果目标等级小于零
                        } else {
                            // 移除附魔
                            itemStack.removeEnchantment(enchantment)
                        }
                    }
                }
                return@addItemEditor true
            }
            return@addItemEditor false
        }
        // 给物品附魔升级(解析其中的即时声明节点)
        addItemEditor("levelUpEnchantmentSection") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取待设置附魔
                val enchantments = content.parseSection(player).parseObject<HashMap<String, Int>>()
                // 遍历添加
                enchantments.forEach { (key, value) ->
                    val enchantment = Enchantment.getByName(key.uppercase(Locale.getDefault()))
                    var level = value
                    // 判断附魔名称 && 等级 是否合法
                    if (enchantment != null && level != 0) {
                        // 如果该物品包含当前附魔
                        if (itemStack.containsEnchantment(enchantment)) {
                            // 修改目标等级
                            level += itemStack.getEnchantmentLevel(enchantment)
                        }
                        // 如果目标等级大于零
                        if (level > 0) {
                            // 设置附魔
                            itemStack.addUnsafeEnchantment(enchantment, level)
                            // 如果目标等级小于零
                        } else {
                            // 移除附魔
                            itemStack.removeEnchantment(enchantment)
                        }
                    }
                }
                return@addItemEditor true
            }
            return@addItemEditor false
        }
        // 给物品附魔降级
        addItemEditor("levelDownEnchantment") { _, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取待设置附魔
                val enchantments = content.parseObject<HashMap<String, Int>>()
                // 遍历添加
                enchantments.forEach { (key, value) ->
                    val enchantment = Enchantment.getByName(key.uppercase(Locale.getDefault()))
                    var level = value
                    // 判断附魔名称 && 等级 是否合法
                    if (enchantment != null && level != 0) {
                        level *= -1
                        // 如果该物品包含当前附魔
                        if (itemStack.containsEnchantment(enchantment)) {
                            // 修改目标等级
                            level += itemStack.getEnchantmentLevel(enchantment)
                        }
                        // 如果目标等级大于零
                        if (level > 0) {
                            // 设置附魔
                            itemStack.addUnsafeEnchantment(enchantment, level)
                            // 如果目标等级小于零
                        } else {
                            // 移除附魔
                            itemStack.removeEnchantment(enchantment)
                        }
                    }
                }
                return@addItemEditor true
            }
            return@addItemEditor false
        }
        // 给物品附魔降级(解析其中的papi变量)
        addItemEditor("levelDownEnchantmentPapi") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取待设置附魔
                val enchantments = papi(player, content).parseObject<HashMap<String, Int>>()
                // 遍历添加
                enchantments.forEach { (key, value) ->
                    val enchantment = Enchantment.getByName(key.uppercase(Locale.getDefault()))
                    var level = value
                    // 判断附魔名称 && 等级 是否合法
                    if (enchantment != null && level != 0) {
                        level *= -1
                        // 如果该物品包含当前附魔
                        if (itemStack.containsEnchantment(enchantment)) {
                            // 修改目标等级
                            level += itemStack.getEnchantmentLevel(enchantment)
                        }
                        // 如果目标等级大于零
                        if (level > 0) {
                            // 设置附魔
                            itemStack.addUnsafeEnchantment(enchantment, level)
                            // 如果目标等级小于零
                        } else {
                            // 移除附魔
                            itemStack.removeEnchantment(enchantment)
                        }
                    }
                }
                return@addItemEditor true
            }
            return@addItemEditor false
        }
        // 给物品附魔降级(解析其中的即时声明节点)
        addItemEditor("levelDownEnchantmentSection") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取待设置附魔
                val enchantments = content.parseSection(player).parseObject<HashMap<String, Int>>()
                // 遍历添加
                enchantments.forEach { (key, value) ->
                    val enchantment = Enchantment.getByName(key.uppercase(Locale.getDefault()))
                    var level = value
                    // 判断附魔名称 && 等级 是否合法
                    if (enchantment != null && level != 0) {
                        level *= -1
                        // 如果该物品包含当前附魔
                        if (itemStack.containsEnchantment(enchantment)) {
                            // 修改目标等级
                            level += itemStack.getEnchantmentLevel(enchantment)
                        }
                        // 如果目标等级大于零
                        if (level > 0) {
                            // 设置附魔
                            itemStack.addUnsafeEnchantment(enchantment, level)
                            // 如果目标等级小于零
                        } else {
                            // 移除附魔
                            itemStack.removeEnchantment(enchantment)
                        }
                    }
                }
                return@addItemEditor true
            }
            return@addItemEditor false
        }
        // 给物品设置属性隐藏
        addItemEditor("setItemFlag") { _, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取itemMeta
                itemStack.itemMeta?.let { itemMeta ->
                    // 移除物品原有属性
                    itemMeta.removeItemFlags(*itemMeta.itemFlags.toTypedArray())
                    // 获取待设置属性
                    val itemFlags = content.split(" ")
                    // 设置属性隐藏
                    itemFlags.forEach { itemFlagString ->
                        try {
                            val itemFlag = ItemFlag.valueOf(itemFlagString)
                            itemMeta.addItemFlags(itemFlag)
                        } catch (_: IllegalArgumentException) {}
                    }
                    // 将改动完成的itemMeta设置回去
                    itemStack.itemMeta = itemMeta
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品设置属性隐藏(解析其中的papi变量)
        addItemEditor("setItemFlagPapi") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取itemMeta
                itemStack.itemMeta?.let { itemMeta ->
                    // 移除物品原有属性
                    itemMeta.removeItemFlags(*itemMeta.itemFlags.toTypedArray())
                    // 获取待设置属性
                    val itemFlags = papi(player, content).split(" ")
                    // 设置属性隐藏
                    itemFlags.forEach { itemFlagString ->
                        try {
                            val itemFlag = ItemFlag.valueOf(itemFlagString)
                            itemMeta.addItemFlags(itemFlag)
                        } catch (_: IllegalArgumentException) {}
                    }
                    // 将改动完成的itemMeta设置回去
                    itemStack.itemMeta = itemMeta
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品设置属性隐藏(解析其中的即时声明节点)
        addItemEditor("setItemFlagSection") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取itemMeta
                itemStack.itemMeta?.let { itemMeta ->
                    // 移除物品原有属性
                    itemMeta.removeItemFlags(*itemMeta.itemFlags.toTypedArray())
                    // 获取待设置属性
                    val itemFlags = content.parseSection(player).split(" ")
                    // 设置属性隐藏
                    itemFlags.forEach { itemFlagString ->
                        try {
                            val itemFlag = ItemFlag.valueOf(itemFlagString)
                            itemMeta.addItemFlags(itemFlag)
                        } catch (_: IllegalArgumentException) {}
                    }
                    // 将改动完成的itemMeta设置回去
                    itemStack.itemMeta = itemMeta
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品添加属性隐藏
        addItemEditor("addItemFlag") { _, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取itemMeta
                itemStack.itemMeta?.let { itemMeta ->
                    // 获取待设置属性
                    val itemFlags = content.split(" ")
                    // 设置属性隐藏
                    itemFlags.forEach { itemFlagString ->
                        try {
                            val itemFlag = ItemFlag.valueOf(itemFlagString)
                            itemMeta.addItemFlags(itemFlag)
                        } catch (_: IllegalArgumentException) {}
                    }
                    // 将改动完成的itemMeta设置回去
                    itemStack.itemMeta = itemMeta
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品添加属性隐藏(解析其中的papi变量)
        addItemEditor("addItemFlagPapi") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取itemMeta
                itemStack.itemMeta?.let { itemMeta ->
                    // 获取待设置属性
                    val itemFlags = papi(player, content).split(" ")
                    // 设置属性隐藏
                    itemFlags.forEach { itemFlagString ->
                        try {
                            val itemFlag = ItemFlag.valueOf(itemFlagString)
                            itemMeta.addItemFlags(itemFlag)
                        } catch (_: IllegalArgumentException) {}
                    }
                    // 将改动完成的itemMeta设置回去
                    itemStack.itemMeta = itemMeta
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品添加属性隐藏(解析其中的即时声明节点)
        addItemEditor("addItemFlagSection") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取itemMeta
                itemStack.itemMeta?.let { itemMeta ->
                    // 获取待设置属性
                    val itemFlags = content.parseSection(player).split(" ")
                    // 设置属性隐藏
                    itemFlags.forEach { itemFlagString ->
                        try {
                            val itemFlag = ItemFlag.valueOf(itemFlagString)
                            itemMeta.removeItemFlags(itemFlag)
                        } catch (_: IllegalArgumentException) {}
                    }
                    // 将改动完成的itemMeta设置回去
                    itemStack.itemMeta = itemMeta
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品移除属性隐藏
        addItemEditor("removeItemFlag") { _, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取itemMeta
                itemStack.itemMeta?.let { itemMeta ->
                    // 获取待移除属性
                    val itemFlags = content.split(" ")
                    // 遍历移除相应属性
                    itemFlags.forEach { itemFlagString ->
                        try {
                            val itemFlag = ItemFlag.valueOf(itemFlagString)
                            itemMeta.removeItemFlags(itemFlag)
                        } catch (_: IllegalArgumentException) {}
                    }
                    // 将改动完成的itemMeta设置回去
                    itemStack.itemMeta = itemMeta
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品移除属性隐藏(解析其中的papi变量)
        addItemEditor("removeItemFlagPapi") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取itemMeta
                itemStack.itemMeta?.let { itemMeta ->
                    // 获取待移除属性
                    val itemFlags = papi(player, content).split(" ")
                    // 遍历移除相应属性
                    itemFlags.forEach { itemFlagString ->
                        try {
                            val itemFlag = ItemFlag.valueOf(itemFlagString)
                            itemMeta.removeItemFlags(itemFlag)
                        } catch (_: IllegalArgumentException) {}
                    }
                    // 将改动完成的itemMeta设置回去
                    itemStack.itemMeta = itemMeta
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品移除属性隐藏(解析其中的即时声明节点)
        addItemEditor("removeItemFlagSection") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取itemMeta
                itemStack.itemMeta?.let { itemMeta ->
                    // 获取待移除属性
                    val itemFlags = content.parseSection(player).split(" ")
                    // 遍历移除相应属性
                    itemFlags.forEach { itemFlagString ->
                        try {
                            val itemFlag = ItemFlag.valueOf(itemFlagString)
                            itemMeta.addItemFlags(itemFlag)
                        } catch (_: IllegalArgumentException) {}
                    }
                    // 将改动完成的itemMeta设置回去
                    itemStack.itemMeta = itemMeta
                    return@addItemEditor true
                }
            }
            return@addItemEditor false
        }
        // 给物品设置NBT
        addItemEditor("setNBT") { _, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取物品NBT
                val itemTag = itemStack.getItemTag()
                // 获取并遍历添加NBT
                content.parseObject<HashMap<String, String>>().forEach { (key, value) ->
                    itemTag.putDeepFixed(key, value.castToItemTagData())
                }
                // 保存物品NBT
                itemTag.saveTo(itemStack)
                return@addItemEditor true
            }
            return@addItemEditor false
        }
        // 给物品设置NBT(解析其中的papi变量)
        addItemEditor("setNBTPapi") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取物品NBT
                val itemTag = itemStack.getItemTag()
                // 获取并遍历添加NBT
                papi(player, content).parseObject<HashMap<String, String>>().forEach { (key, value) ->
                    itemTag.putDeepFixed(key, value.castToItemTagData())
                }
                // 保存物品NBT
                itemTag.saveTo(itemStack)
                return@addItemEditor true
            }
            return@addItemEditor false
        }
        // 给物品设置NBT(解析其中的即时声明节点)
        addItemEditor("setNBTSection") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取物品NBT
                val itemTag = itemStack.getItemTag()
                // 获取并遍历添加NBT
                content.parseSection(player).parseObject<HashMap<String, String>>().forEach { (key, value) ->
                    itemTag.putDeepFixed(key, value.castToItemTagData())
                }
                // 保存物品NBT
                itemTag.saveTo(itemStack)
                return@addItemEditor true
            }
            return@addItemEditor false
        }
        // 给物品设置NBT(支持List)
        addItemEditor("setNBTWithList") { _, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取物品NBT
                val itemTag = itemStack.getItemTag()
                // 获取并遍历添加NBT
                content.parseObject<HashMap<String, String>>().forEach { (key, value) ->
                    itemTag.putDeepWithList(key, value.castToItemTagData())
                }
                // 保存物品NBT
                itemTag.saveTo(itemStack)
                return@addItemEditor true
            }
            return@addItemEditor false
        }
        // 给物品设置NBT(支持List)(解析其中的papi变量)
        addItemEditor("setNBTWithListPapi") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取物品NBT
                val itemTag = itemStack.getItemTag()
                // 获取并遍历添加NBT
                papi(player, content).parseObject<HashMap<String, String>>().forEach { (key, value) ->
                    itemTag.putDeepWithList(key, value.castToItemTagData())
                }
                // 保存物品NBT
                itemTag.saveTo(itemStack)
                return@addItemEditor true
            }
            return@addItemEditor false
        }
        // 给物品设置NBT(支持List)(解析其中的即时声明节点)
        addItemEditor("setNBTWithListSection") { player, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取物品NBT
                val itemTag = itemStack.getItemTag()
                // 获取并遍历添加NBT
                content.parseSection(player).parseObject<HashMap<String, String>>().forEach { (key, value) ->
                    itemTag.putDeepWithList(key, value.castToItemTagData())
                }
                // 保存物品NBT
                itemTag.saveTo(itemStack)
                return@addItemEditor true
            }
            return@addItemEditor false
        }
    }
}
