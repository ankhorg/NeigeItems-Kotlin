package pers.neige.neigeitems.manager

import com.alibaba.fastjson2.parseObject
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.neosearch.stringsearcher.StringSearcher
import pers.neige.neigeitems.manager.HookerManager.papi
import pers.neige.neigeitems.utils.ConfigUtils
import pers.neige.neigeitems.utils.ItemUtils.castToItemTagData
import pers.neige.neigeitems.utils.ItemUtils.putDeepFixed
import pers.neige.neigeitems.utils.ItemUtils.putDeepWithList
import pers.neige.neigeitems.utils.SectionUtils.parseSection
import pers.neige.neigeitems.utils.function.TriFunction
import taboolib.common.env.RuntimeDependencies
import taboolib.common.env.RuntimeDependency
import taboolib.module.nms.getItemTag
import java.util.*

@RuntimeDependencies(
    RuntimeDependency(
        "!org.neosearch.stringsearcher:multiple-string-searcher:0.1.1",
        test = "!org.neosearch.stringsearcher.StringSearcher"
    )
)
/**
 * 用于管理所有物品编辑函数
 */
object ItemEditorManager {
    /**
     * 获取所有物品编辑函数
     */
    val itemEditors: HashMap<String, TriFunction<Player, ItemStack, String, Boolean?>> = HashMap<String, TriFunction<Player, ItemStack, String, Boolean?>>()

    /**
     * 基础物品编辑函数
     */
    private val basicItemEditors: HashMap<String, TriFunction<Player, ItemStack, String, Boolean?>> = HashMap<String, TriFunction<Player, ItemStack, String, Boolean?>>()
    
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
    
    private fun addBasicItemEditor(id: String, function: TriFunction<Player, ItemStack, String, Boolean?>) {
        basicItemEditors[id] = function
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
        addBasicItemEditor("setMaterial") { _, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取对应材质
                Material.matchMaterial(content.uppercase(Locale.getDefault()))?.let { material ->
                    // 设置物品材质
                    itemStack.type = material
                    return@addBasicItemEditor true
                }
            }
            return@addBasicItemEditor false
        }
        // 给物品设置数量
        addBasicItemEditor("setAmount") { _, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取对应数量
                content.toIntOrNull()?.let { amount ->
                    itemStack.amount = amount.coerceAtLeast(0).coerceAtMost(itemStack.type.maxStackSize)
                    return@addBasicItemEditor true
                }
            }
            return@addBasicItemEditor false
        }
        // 给物品添加数量
        addBasicItemEditor("addAmount") { _, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取对应数量
                content.toIntOrNull()?.let { amount ->
                    itemStack.amount = (itemStack.amount + amount).coerceAtLeast(0).coerceAtMost(itemStack.type.maxStackSize)
                    return@addBasicItemEditor true
                }
            }
            return@addBasicItemEditor false
        }
        // 给物品扣除数量
        addBasicItemEditor("takeAmount") { _, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取对应数量
                content.toIntOrNull()?.let { amount ->
                    itemStack.amount = (itemStack.amount - amount).coerceAtLeast(0).coerceAtMost(itemStack.type.maxStackSize)
                    return@addBasicItemEditor true
                }
            }
            return@addBasicItemEditor false
        }
        // 给物品设置显示名
        addBasicItemEditor("setName") { _, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取itemMeta
                itemStack.itemMeta?.let { itemMeta ->
                    // 获取并设置显示名
                    itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', content))
                    // 将改动完成的itemMeta设置回去
                    itemStack.itemMeta = itemMeta
                    return@addBasicItemEditor true
                }
            }
            return@addBasicItemEditor false
        }
        // 给物品显示名添加前缀
        addBasicItemEditor("addNamePrefix") { _, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取itemMeta
                itemStack.itemMeta?.let { itemMeta ->
                    // 获取并设置显示名
                    itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', content) + itemMeta.displayName)
                    // 将改动完成的itemMeta设置回去
                    itemStack.itemMeta = itemMeta
                    return@addBasicItemEditor true
                }
            }
            return@addBasicItemEditor false
        }
        // 给物品显示名添加后缀
        addBasicItemEditor("addNamePostfix") { _, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取itemMeta
                itemStack.itemMeta?.let { itemMeta ->
                    // 获取并设置显示名
                    itemMeta.setDisplayName(itemMeta.displayName + ChatColor.translateAlternateColorCodes('&', content))
                    // 将改动完成的itemMeta设置回去
                    itemStack.itemMeta = itemMeta
                    return@addBasicItemEditor true
                }
            }
            return@addBasicItemEditor false
        }
        // 替换物品显示名(只替换一次)
        addBasicItemEditor("replaceName") { _, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取itemMeta
                itemStack.itemMeta?.let { itemMeta ->
                    // 没Name还替换个头
                    if (!itemMeta.hasDisplayName()) return@addBasicItemEditor true
                    // 获取Name
                    val originName = itemMeta.displayName

                    // 获取 待替换文本: 替换文本
                    val info = ChatColor.translateAlternateColorCodes('&', content).parseObject<HashMap<String, String>>()
                    // 啥也没写还替换个头
                    if (info.isEmpty()) return@addBasicItemEditor true

                    // 构建字符搜索器
                    var stringSearcherTemp = StringSearcher.builder()
                        // 忽视重叠文本, 对于重叠文本, 长文本优先级大于短文本
                        .ignoreOverlaps()
                    // 遍历添加所有待替换文本
                    info.keys.forEach { key ->
                        stringSearcherTemp = stringSearcherTemp.addSearchString(key)
                    }
                    // 构建字符搜索器
                    val stringSearcher = stringSearcherTemp.build()

                    // 搜索当前Name
                    val tokens = stringSearcher.tokenize(originName)
                    // 新Name
                    val name = StringBuilder()
                    // 遍历搜索后的分段文本
                    tokens.forEach { token ->
                        // 当前段文本
                        val now = token.fragment
                        // 如果这段文本为待替换文本
                        if (token.isMatch) {
                            // 获取替换文本
                            info[now]?.let { result ->
                                // 直接替换
                                name.append(result)
                            } ?: let {
                                // 没获取到替换文本, 说明替换过一次了
                                name.append(now)
                            }
                            // 挪出去
                            info.remove(now)
                            // 如果这段文本不是待替换文本
                        } else {
                            // 怼回去
                            name.append(now)
                        }
                    }

                    // 设置Name
                    itemMeta.setDisplayName(name.toString())
                    // 将改动完成的itemMeta设置回去
                    itemStack.itemMeta = itemMeta
                    return@addBasicItemEditor true
                }
            }
            return@addBasicItemEditor false
        }
        // 替换物品显示名(全部替换)
        addBasicItemEditor("replaceAllName") { _, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取itemMeta
                itemStack.itemMeta?.let { itemMeta ->
                    // 没Name还替换个头
                    if (!itemMeta.hasDisplayName()) return@addBasicItemEditor true
                    // 获取Name
                    val originName = itemMeta.displayName

                    // 获取 待替换文本: 替换文本
                    val info = ChatColor.translateAlternateColorCodes('&', content).parseObject<HashMap<String, String>>()
                    // 啥也没写还替换个头
                    if (info.isEmpty()) return@addBasicItemEditor true

                    // 构建字符搜索器
                    var stringSearcherTemp = StringSearcher.builder()
                        // 忽视重叠文本, 对于重叠文本, 长文本优先级大于短文本
                        .ignoreOverlaps()
                    // 遍历添加所有待替换文本
                    info.keys.forEach { key ->
                        stringSearcherTemp = stringSearcherTemp.addSearchString(key)
                    }
                    // 构建字符搜索器
                    val stringSearcher = stringSearcherTemp.build()

                    // 搜索当前Name
                    val tokens = stringSearcher.tokenize(originName)
                    // 新Name
                    val name = StringBuilder()
                    // 遍历搜索后的分段文本
                    tokens.forEach { token ->
                        // 当前段文本
                        val now = token.fragment
                        // 如果这段文本为待替换文本
                        if (token.isMatch) {
                            // 获取替换文本
                            info[now]?.let { result ->
                                // 直接替换
                                name.append(result)
                            }
                            // 如果这段文本不是待替换文本
                        } else {
                            // 怼回去
                            name.append(now)
                        }
                    }

                    // 设置Name
                    itemMeta.setDisplayName(name.toString())
                    // 将改动完成的itemMeta设置回去
                    itemStack.itemMeta = itemMeta
                    return@addBasicItemEditor true
                }
            }
            return@addBasicItemEditor false
        }
        // 给物品添加lore
        addBasicItemEditor("addLore") { _, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取itemMeta
                itemStack.itemMeta?.let { itemMeta ->
                    // 获取并设置lore
                    itemMeta.lore = (itemMeta.lore ?: ArrayList()).also { lore ->
                        // 解析颜色符号, 通过\n换行, 最后添加到原lore中
                        lore.addAll(ChatColor.translateAlternateColorCodes('&', content).split("\\n"))
                    }
                    // 将改动完成的itemMeta设置回去
                    itemStack.itemMeta = itemMeta
                    return@addBasicItemEditor true
                }
            }
            return@addBasicItemEditor false
        }
        // 给物品设置lore
        addBasicItemEditor("setLore") { _, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取itemMeta
                itemStack.itemMeta?.let { itemMeta ->
                    // 解析颜色符号, 通过\n换行, 设置lore
                    itemMeta.lore = ChatColor.translateAlternateColorCodes('&', content).split("\\n")
                    // 将改动完成的itemMeta设置回去
                    itemStack.itemMeta = itemMeta
                    return@addBasicItemEditor true
                }
            }
            return@addBasicItemEditor false
        }
        // 替换物品lore(只替换一次)
        addBasicItemEditor("replaceLore") { _, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取itemMeta
                itemStack.itemMeta?.let { itemMeta ->
                    // 没Lore还替换个头
                    if (!itemMeta.hasLore()) return@addBasicItemEditor true
                    // 获取lore
                    val originLore = itemMeta.lore

                    // 获取 待替换文本: 替换文本
                    val info = ChatColor.translateAlternateColorCodes('&', content).parseObject<HashMap<String, String>>()
                    // 啥也没写还替换个头
                    if (info.isEmpty()) return@addBasicItemEditor true

                    // 构建字符搜索器
                    var stringSearcherTemp = StringSearcher.builder()
                        // 忽视重叠文本, 对于重叠文本, 长文本优先级大于短文本
                        .ignoreOverlaps()
                    // 遍历添加所有待替换文本
                    info.keys.forEach { key ->
                        stringSearcherTemp = stringSearcherTemp.addSearchString(key)
                    }
                    // 构建字符搜索器
                    val stringSearcher = stringSearcherTemp.build()

                    // 新Lore
                    val lore = ArrayList<String>()

                    // 遍历原Lore的每一行
                    originLore?.forEach {
                        // 搜索当前行Lore
                        val tokens = stringSearcher.tokenize(it)
                        // 用于替换后的Lore
                        var text = StringBuilder()
                        // 遍历搜索后的分段文本
                        tokens.forEach { token ->
                            // 当前段文本
                            val now = token.fragment
                            // 如果这段文本为待替换文本
                            if (token.isMatch) {
                                // 获取替换文本
                                info[now]?.let { result ->
                                    // 如果替换文本包含换行符
                                    if (result.contains("\n")) {
                                        // 拆一下
                                        result.split("\n").let { array ->
                                            // 遍历
                                            for (index in array.indices) {
                                                // 怼进去
                                                text.append(array[index])
                                                // 如果还没到底
                                                if (index != (array.size - 1)) {
                                                    // 怼Lore里
                                                    lore.add(text.toString())
                                                    // 重开一行
                                                    text = StringBuilder()
                                                }
                                            }
                                        }
                                    } else {
                                        // 不包含换行符就直接替换
                                        text.append(result)
                                    }
                                } ?: let {
                                    // 没获取到替换文本, 说明替换过一次了
                                    text.append(now)
                                }
                                // 挪出去
                                info.remove(now)
                            // 如果这段文本不是待替换文本
                            } else {
                                // 怼回去
                                text.append(now)
                            }
                        }

                        // 添加Lore
                        lore.add(text.toString())
                    }

                    // 设置Lore
                    itemMeta.lore = lore
                    // 将改动完成的itemMeta设置回去
                    itemStack.itemMeta = itemMeta
                    return@addBasicItemEditor true
                }
            }
            return@addBasicItemEditor false
        }
        // 替换物品lore(全部替换)
        addBasicItemEditor("replaceAllLore") { _, itemStack, content ->
            // 判断是不是空气
            if (itemStack.type != Material.AIR) {
                // 获取itemMeta
                itemStack.itemMeta?.let { itemMeta ->
                    // 没Lore还替换个头
                    if (!itemMeta.hasLore()) return@addBasicItemEditor true
                    // 获取lore
                    val originLore = itemMeta.lore

                    // 获取 待替换文本: 替换文本
                    val info = ChatColor.translateAlternateColorCodes('&', content).parseObject<HashMap<String, String>>()
                    // 啥也没写还替换个头
                    if (info.isEmpty()) return@addBasicItemEditor true

                    // 构建字符搜索器
                    var stringSearcherTemp = StringSearcher.builder()
                        // 忽视重叠文本, 对于重叠文本, 长文本优先级大于短文本
                        .ignoreOverlaps()
                    // 遍历添加所有待替换文本
                    info.keys.forEach { key ->
                        stringSearcherTemp = stringSearcherTemp.addSearchString(key)
                    }
                    // 构建字符搜索器
                    val stringSearcher = stringSearcherTemp.build()

                    // 新Lore
                    val lore = ArrayList<String>()

                    // 遍历原Lore的每一行
                    originLore?.forEach {
                        // 搜索当前行Lore
                        val tokens = stringSearcher.tokenize(it)
                        // 用于替换后的Lore
                        var text = StringBuilder()
                        // 遍历搜索后的分段文本
                        tokens.forEach { token ->
                            // 当前段文本
                            val now = token.fragment
                            // 如果这段文本为待替换文本
                            if (token.isMatch) {
                                // 获取替换文本
                                info[now]?.let { result ->
                                    // 如果替换文本包含换行符
                                    if (result.contains("\n")) {
                                        // 拆一下
                                        result.split("\n").let { array ->
                                            // 遍历
                                            for (index in array.indices) {
                                                // 怼进去
                                                text.append(array[index])
                                                // 如果还没到底
                                                if (index != (array.size - 1)) {
                                                    // 怼Lore里
                                                    lore.add(text.toString())
                                                    // 重开一行
                                                    text = StringBuilder()
                                                }
                                            }
                                        }
                                    } else {
                                        // 不包含换行符就直接替换
                                        text.append(result)
                                    }
                                }
                                // 如果这段文本不是待替换文本
                            } else {
                                // 怼回去
                                text.append(now)
                            }
                        }

                        // 添加Lore
                        lore.add(text.toString())
                    }

                    // 设置Lore
                    itemMeta.lore = lore
                    // 将改动完成的itemMeta设置回去
                    itemStack.itemMeta = itemMeta
                    return@addBasicItemEditor true
                }
            }
            return@addBasicItemEditor false
        }
        // 给物品设置子ID/损伤值
        addBasicItemEditor("setDamage") { _, itemStack, content ->
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
                    return@addBasicItemEditor true
                }
            }
            return@addBasicItemEditor false
        }
        // 给物品增加子ID/损伤值
        addBasicItemEditor("addDamage") { _, itemStack, content ->
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
                    return@addBasicItemEditor true
                }
            }
            return@addBasicItemEditor false
        }
        // 给物品减少子ID/损伤值
        addBasicItemEditor("takeDamage") { _, itemStack, content ->
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
                    return@addBasicItemEditor true
                }
            }
            return@addBasicItemEditor false
        }
        // 给物品设置CustomModelData
        addBasicItemEditor("setCustomModelData") { _, itemStack, content ->
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
                    return@addBasicItemEditor true
                }
            }
            return@addBasicItemEditor false
        }
        // 给物品设置无法破坏
        addBasicItemEditor("setUnbreakable") { _, itemStack, content ->
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
                    return@addBasicItemEditor true
                }
            }
            return@addBasicItemEditor false
        }
        // 给物品设置附魔
        addBasicItemEditor("setEnchantment") { _, itemStack, content ->
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
                return@addBasicItemEditor true
            }
            return@addBasicItemEditor false
        }
        // 给物品添加附魔
        addBasicItemEditor("addEnchantment") { _, itemStack, content ->
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
                return@addBasicItemEditor true
            }
            return@addBasicItemEditor false
        }
        // 给物品添加附魔(已存在的不覆盖)
        addBasicItemEditor("addNotCoverEnchantment") { _, itemStack, content ->
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
                return@addBasicItemEditor true
            }
            return@addBasicItemEditor false
        }
        // 给物品移除附魔
        addBasicItemEditor("removeEnchantment") { _, itemStack, content ->
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
                return@addBasicItemEditor true
            }
            return@addBasicItemEditor false
        }
        // 给物品附魔升级
        addBasicItemEditor("levelUpEnchantment") { _, itemStack, content ->
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
                return@addBasicItemEditor true
            }
            return@addBasicItemEditor false
        }
        // 给物品附魔降级
        addBasicItemEditor("levelDownEnchantment") { _, itemStack, content ->
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
                return@addBasicItemEditor true
            }
            return@addBasicItemEditor false
        }
        // 给物品设置属性隐藏
        addBasicItemEditor("setItemFlag") { _, itemStack, content ->
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
                    return@addBasicItemEditor true
                }
            }
            return@addBasicItemEditor false
        }
        // 给物品添加属性隐藏
        addBasicItemEditor("addItemFlag") { _, itemStack, content ->
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
                    return@addBasicItemEditor true
                }
            }
            return@addBasicItemEditor false
        }
        // 给物品移除属性隐藏
        addBasicItemEditor("removeItemFlag") { _, itemStack, content ->
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
                    return@addBasicItemEditor true
                }
            }
            return@addBasicItemEditor false
        }
        // 给物品设置NBT
        addBasicItemEditor("setNBT") { _, itemStack, content ->
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
                return@addBasicItemEditor true
            }
            return@addBasicItemEditor false
        }
        // 给物品设置NBT(支持List)
        addBasicItemEditor("setNBTWithList") { _, itemStack, content ->
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
                return@addBasicItemEditor true
            }
            return@addBasicItemEditor false
        }
        basicItemEditors.forEach{ (id, function) ->
            addItemEditor(id) { player, itemStack, content ->
                return@addItemEditor function.apply(player, itemStack, content)
            }
            // (解析其中的papi变量)
            addItemEditor("${id}Papi") { player, itemStack, content ->
                return@addItemEditor function.apply(player, itemStack, papi(player, content))
            }
            // (解析其中的即时声明节点)
            addItemEditor("${id}Section") { player, itemStack, content ->
                return@addItemEditor function.apply(player, itemStack, content.parseSection(player))
            }
        }
    }
}
