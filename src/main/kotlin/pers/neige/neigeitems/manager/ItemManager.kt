package pers.neige.neigeitems.manager

import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.NeigeItems
import pers.neige.neigeitems.item.ItemConfig
import pers.neige.neigeitems.item.ItemGenerator
import pers.neige.neigeitems.item.ItemInfo
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtItemStack
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtNumeric
import pers.neige.neigeitems.manager.ConfigManager.debug
import pers.neige.neigeitems.manager.HookerManager.nmsHooker
import pers.neige.neigeitems.utils.ConfigUtils.clone
import pers.neige.neigeitems.utils.ItemUtils.getNbt
import pers.neige.neigeitems.utils.ItemUtils.invalidNBT
import pers.neige.neigeitems.utils.ItemUtils.isNiItem
import pers.neige.neigeitems.utils.ItemUtils.toStringMap
import java.io.File
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * 物品管理器
 *
 * @constructor 构建物品管理器
 */
object ItemManager : ItemConfigManager() {
    /**
     * 获取所有物品生成器
     */
    val items: ConcurrentHashMap<String, ItemGenerator> = ConcurrentHashMap<String, ItemGenerator>()

    /**
     * 获取物品总数
     */
    val itemAmount get() = itemIds.size

    private val nullHashMap: HashMap<String, String>? = null

    init {
        // 初始化所有物品生成器
        loadItems()
    }

    /**
     * 初始化所有物品生成器
     */
    private fun loadItems() {
        var time = System.currentTimeMillis()
        for ((id, itemConfig) in itemConfigs) {
            items[id] = ItemGenerator(itemConfig)
            if (debug) {
                val current = System.currentTimeMillis() - time
                if (current > 1) {
                    NeigeItems.plugin.logger.info("  物品-$id-加载耗时: ${current}ms")
                }
                time = System.currentTimeMillis()
            }
        }
    }

    /**
     * 添加物品生成器
     * @param itemGenerator 待添加物品生成器
     */
    private fun addItem(itemGenerator: ItemGenerator) {
        itemConfigs[itemGenerator.id] = itemGenerator.itemConfig
        items[itemGenerator.id] = itemGenerator
    }

    /**
     * 重载物品管理器
     */
    fun reload() {
        reloadItemConfigs()
        items.clear()
        loadItems()
    }

    /**
     * 获取物品原始Config的克隆
     *
     * @param id 物品ID
     * @return 物品原始Config的克隆
     */
    fun getOriginConfig(id: String): ConfigurationSection? {
        return itemConfigs[id]?.configSection?.clone()
    }

    /**
     * 获取物品原始Config
     *
     * @param id 物品ID
     * @return 物品原始Config
     */
    fun getRealOriginConfig(id: String): ConfigurationSection? {
        return itemConfigs[id]?.configSection
    }

    /**
     * 获取物品生成器
     *
     * @param id 物品ID
     * @return 物品生成器
     */
    fun getItem(id: String): ItemGenerator? {
        return items[id]
    }

    /**
     * 获取物品
     *
     * @param id 物品ID
     * @return 物品
     */
    fun getItemStack(id: String): ItemStack? {
        return getItemStack(id, null, nullHashMap)
    }

    /**
     * 获取物品
     *
     * @param id 物品ID
     * @param player 用于解析物品的玩家
     * @return 物品
     */
    fun getItemStack(id: String, player: OfflinePlayer?): ItemStack? {
        return getItemStack(id, player, nullHashMap)
    }

    /**
     * 获取物品
     *
     * @param id 物品ID
     * @param data 用于解析物品的指向数据
     * @return 物品
     */
    fun getItemStack(id: String, data: String?): ItemStack? {
        return getItemStack(id, null, data)
    }

    /**
     * 获取物品
     *
     * @param id 物品ID
     * @param data 用于解析物品的指向数据
     * @return 物品
     */
    fun getItemStack(id: String, data: MutableMap<String, String>?): ItemStack? {
        return getItemStack(id, null, data)
    }

    /**
     * 获取物品
     *
     * @param id 物品ID
     * @param player 用于解析物品的玩家
     * @param data 用于解析物品的指向数据
     * @return 物品
     */
    fun getItemStack(id: String, player: OfflinePlayer?, data: String?): ItemStack? {
        return items[id]?.getItemStack(player, data)
    }

    /**
     * 获取物品
     *
     * @param id 物品ID
     * @param player 用于解析物品的玩家
     * @param data 用于解析物品的指向数据
     * @return 物品
     */
    fun getItemStack(id: String, player: OfflinePlayer?, data: MutableMap<String, String>?): ItemStack? {
        return items[id]?.getItemStack(player, data)
    }


    /**
     * 是否存在对应ID的物品
     *
     * @param id 物品ID
     * @return 是否存在对应ID的物品
     */
    fun hasItem(id: String): Boolean {
        return items.containsKey(id)
    }

    /**
     * 保存物品
     *
     * @param itemStack 保存物品
     * @param id 物品ID
     * @param file 保存文件
     * @param config 文件转换来的YamlConfiguration
     * @param cover 是否覆盖
     * @return 1 保存成功; 0 ID冲突; 2 你保存了个空气
     */
    fun saveItem(itemStack: ItemStack, id: String, file: File, config: YamlConfiguration, cover: Boolean): Int {
        // 检测是否为空气
        if (itemStack.type != Material.AIR) {
            // 检测节点是否存在
            if (!hasItem(id) || cover) {
                // 创建物品节点
                val configSection = config.createSection(id)
                // 设置物品材质
                configSection.set("material", itemStack.type.toString())
                // 设置子ID/损伤值
                if (itemStack.durability > 0) {
                    configSection.set("damage", itemStack.durability)
                }
                // 如果物品有ItemMeta
                if (itemStack.hasItemMeta()) {
                    // 获取ItemMeta
                    val itemMeta = itemStack.itemMeta
                    // 获取物品NBT
                    val itemNBT = itemStack.getNbt()
                    // 设置CustomModelData
                    nmsHooker.getCustomModelData(itemMeta)?.let {
                        configSection.set("custommodeldata", it)
                    }
                    // 设置物品名
                    if (itemMeta?.hasDisplayName() == true) {
                        configSection.set("name", itemMeta.displayName)
                    }
                    // 设置Lore
                    if (itemMeta?.hasLore() == true) {
                        configSection.set("lore", itemMeta.lore)
                    }
                    // 设置是否无法破坏
                    if (itemMeta?.isUnbreakable == true) {
                        configSection.set("unbreakable", itemMeta.isUnbreakable)
                    }
                    // 设置物品附魔
                    if (itemMeta?.hasEnchants() == true) {
                        val enchantSection = configSection.createSection("enchantments")
                        for ((enchant, level) in itemMeta.enchants) {
                            enchantSection.set(enchant.name, level)
                        }
                    }
                    // 设置ItemFlags
                    itemMeta?.itemFlags?.let {
                        if (it.isNotEmpty()) {
                            configSection.set("hideflags", it.map { flag -> flag.name })
                        }
                    }
                    // 设置物品颜色
                    val color = itemNBT.getDeepInt("display.color", -1)
                    if (color != -1) {
                        configSection.set("color", color.toString(16).uppercase(Locale.getDefault()))
                    }
                    // 设置物品NBT
                    if (!itemNBT.isEmpty()) {
                        configSection.set("nbt", itemNBT.toStringMap(invalidNBT))
                    }
                }
                // 保存文件
                config.save(file)
                // 物品保存好了, 信息加进ItemManager里
                addItem(ItemGenerator(ItemConfig(id, file, config)))
                if (cover) return 0
                return 1
            }
            return 0
        }
        return 2
    }

    /**
     * 保存物品
     *
     * @param itemStack 保存物品
     * @param id 物品ID
     * @param path 保存路径
     * @param cover 是否覆盖
     * @return 1 保存成功; 0 ID冲突; 2 你保存了个空气
     */
    fun saveItem(itemStack: ItemStack, id: String, path: String = "$id.yml", cover: Boolean): Int {
        val file = File(plugin.dataFolder, "${File.separator}Items${File.separator}$path")
        if (!file.exists()) {
            file.createNewFile()
        }
        val config = YamlConfiguration.loadConfiguration(file)
        return saveItem(itemStack, id, file, config, cover)
    }

    /**
     * 判断ItemStack是否为NI物品并返回NI物品信息
     *
     * @return NI物品信息, 非NI物品返回null
     */
    fun isNiItem(itemStack: ItemStack): ItemInfo? {
        return itemStack.isNiItem(false)
    }

    /**
     * 判断ItemStack是否为NI物品并返回NI物品信息
     *
     * @param parseData 是否将data解析为HashMap
     * @return NI物品信息, 非NI物品返回null
     */
    fun isNiItem(itemStack: ItemStack, parseData: Boolean): ItemInfo? {
        return itemStack.isNiItem(parseData)
    }

    /**
     * 设置物品使用次数
     *
     * @param amount 使用次数
     */
    @JvmStatic
    fun ItemStack.setCharge(amount: Int) {
        // 直掏NBT
        val nbtItemStack = NbtItemStack(this)
        val directTag = nbtItemStack.directTag
        // 不是NI物品还加个屁的使用次数
        val neigeItems = directTag.getCompound("NeigeItems") ?: return
        // 掏出最大使用次数NBT
        val maxChargeNbt = neigeItems.get("maxCharge")
        // 类型检测
        if (maxChargeNbt !is NbtNumeric<*>) return
        // 获取物品最大使用次数
        val maxCharge = maxChargeNbt.asInt
        // 计算新使用次数
        val newCharge = amount.coerceAtMost(maxCharge).coerceAtLeast(0)
        // 修改使用次数
        neigeItems.putInt("charge", newCharge)
    }

    /**
     * 添加物品使用次数
     *
     * @param amount 添加量(可为负)
     */
    @JvmStatic
    fun ItemStack.addCharge(amount: Int) {
        // 直掏NBT
        val nbtItemStack = NbtItemStack(this)
        val directTag = nbtItemStack.directTag
        // 不是NI物品还加个屁的使用次数
        val neigeItems = directTag.getCompound("NeigeItems") ?: return
        // 掏出使用次数NBT
        val chargeNbt = neigeItems.get("charge")
        // 类型检测
        if (chargeNbt !is NbtNumeric<*>) return
        // 获取物品使用次数
        val charge = chargeNbt.asInt
        // 获取物品最大使用次数
        val maxCharge = neigeItems.getInt("maxCharge")
        // 计算新使用次数
        val newCharge = (charge + amount).coerceAtMost(maxCharge).coerceAtLeast(0)
        // 最终使用次数为0
        if (newCharge == 0) {
            // 扬了
            this.amount = 0
            // 不为0
        } else {
            // 修改使用次数
            neigeItems.putInt("charge", newCharge)
        }
    }

    /**
     * 设置物品最大使用次数
     *
     * @param amount 最大使用次数
     */
    @JvmStatic
    fun ItemStack.setMaxCharge(amount: Int) {
        // 直掏NBT
        val nbtItemStack = NbtItemStack(this)
        val directTag = nbtItemStack.directTag
        // 不是NI物品还加个屁的使用次数
        val neigeItems = directTag.getCompound("NeigeItems") ?: return
        // 掏出使用次数NBT
        val chargeNbt = neigeItems.get("charge")
        // 类型检测
        if (chargeNbt !is NbtNumeric<*>) return
        // 计算物品使用次数
        val charge = chargeNbt.asInt.coerceAtMost(amount.coerceAtLeast(1))
        // 修改使用次数
        neigeItems.putInt("charge", charge)
        // 修改最大使用次数
        neigeItems.putInt("maxCharge", amount.coerceAtLeast(1))
    }

    /**
     * 添加物品最大使用次数
     *
     * @param amount 添加量(可为负)
     */
    @JvmStatic
    fun ItemStack.addMaxCharge(amount: Int) {
        // 直掏NBT
        val nbtItemStack = NbtItemStack(this)
        val directTag = nbtItemStack.directTag
        // 不是NI物品还加个屁的使用次数
        val neigeItems = directTag.getCompound("NeigeItems") ?: return
        // 掏出最大使用次数NBT
        val maxChargeNbt = neigeItems.get("maxCharge")
        // 类型检测
        if (maxChargeNbt !is NbtNumeric<*>) return
        // 计算物品最大使用次数
        val maxCharge = (maxChargeNbt.asInt + amount).coerceAtLeast(1)
        // 计算物品使用次数
        val charge = neigeItems.getInt("charge").coerceAtMost(maxCharge)
        // 修改使用次数
        neigeItems.putInt("charge", charge)
        // 修改最大使用次数
        neigeItems.putInt("maxCharge", maxCharge)
    }

    /**
     * 设置物品耐久值
     *
     * @param amount 耐久值
     */
    @JvmStatic
    fun ItemStack.setCustomDurability(amount: Int) {
        // 直掏NBT
        val nbtItemStack = NbtItemStack(this)
        val directTag = nbtItemStack.directTag
        // 不是NI物品还加个屁的自定义耐久
        val neigeItems = directTag.getCompound("NeigeItems") ?: return
        // 掏出最大耐久值NBT
        val maxDurabilityNbt = neigeItems.get("maxDurability")
        // 类型检测
        if (maxDurabilityNbt !is NbtNumeric<*>) return
        // 获取物品最大耐久值
        val maxDurability = maxDurabilityNbt.asInt
        // 计算新耐久值
        val newDurability = amount.coerceAtMost(maxDurability).coerceAtLeast(0)
        // 获取物品是否破坏(默认破坏)
        val itemBreak = neigeItems.getBoolean("itemBreak", true)
        // 破坏
        if (newDurability == 0 && itemBreak) {
            this.amount = 0
            // 不破坏
        } else {
            // 修改耐久值
            neigeItems.putInt("durability", newDurability)
            this.durability = (type.maxDurability * (1 - (newDurability.toDouble() / maxDurability))).toInt().toShort()
        }
    }

    /**
     * 添加物品耐久值
     *
     * @param amount 添加量(可为负)
     */
    @JvmStatic
    fun ItemStack.addCustomDurability(amount: Int) {
        // 直掏NBT
        val nbtItemStack = NbtItemStack(this)
        val directTag = nbtItemStack.directTag
        // 不是NI物品还加个屁的自定义耐久
        val neigeItems = directTag.getCompound("NeigeItems") ?: return
        // 掏出耐久值NBT
        val durabilityNbt = neigeItems.get("durability")
        // 类型检测
        if (durabilityNbt !is NbtNumeric<*>) return
        // 获取物品耐久值
        val durability = durabilityNbt.asInt
        // 获取物品最大耐久值
        val maxDurability = neigeItems.getInt("maxDurability")
        // 计算新耐久值
        val newDurability = (durability + amount).coerceAtMost(maxDurability).coerceAtLeast(0)
        // 获取物品是否破坏(默认破坏)
        val itemBreak = neigeItems.getBoolean("itemBreak", true)
        // 破坏
        if (newDurability == 0 && itemBreak) {
            this.amount = 0
            // 不破坏
        } else {
            // 修改耐久值
            neigeItems.putInt("durability", newDurability)
            this.durability = (type.maxDurability * (1 - (newDurability.toDouble() / maxDurability))).toInt().toShort()
        }
    }

    /**
     * 设置物品最大耐久值
     *
     * @param amount 最大耐久值
     */
    @JvmStatic
    fun ItemStack.setMaxCustomDurability(amount: Int) {
        // 限制下限
        val realAmount = amount.coerceAtLeast(1)
        // 直掏NBT
        val nbtItemStack = NbtItemStack(this)
        val directTag = nbtItemStack.directTag
        // 不是NI物品还加个屁的自定义耐久
        val neigeItems = directTag.getCompound("NeigeItems") ?: return
        // 掏出耐久值NBT
        val durabilityNbt = neigeItems.get("durability")
        // 类型检测
        if (durabilityNbt !is NbtNumeric<*>) return
        // 获取物品耐久值
        val durability = durabilityNbt.asInt.coerceAtMost(realAmount)
        // 修改耐久值
        neigeItems.putInt("durability", durability)
        // 修改最大耐久值
        neigeItems.putInt("maxDurability", realAmount)
        this.durability = (type.maxDurability * (1 - (durability.toDouble() / realAmount))).toInt().toShort()
    }

    /**
     * 添加物品最大耐久值
     *
     * @param amount 添加量(可为负)
     */
    @JvmStatic
    fun ItemStack.addMaxCustomDurability(amount: Int) {
        // 直掏NBT
        val nbtItemStack = NbtItemStack(this)
        val directTag = nbtItemStack.directTag
        // 不是NI物品还加个屁的自定义耐久
        val neigeItems = directTag.getCompound("NeigeItems") ?: return
        // 掏出最大耐久
        val maxDurabilityNbt = neigeItems.get("maxDurability")
        // 类型检测
        if (maxDurabilityNbt !is NbtNumeric<*>) return
        // 修改后的最大耐久值
        val maxDurability = (maxDurabilityNbt.asInt + amount).coerceAtLeast(1)
        // 获取物品耐久值
        val durability = neigeItems.getInt("durability").coerceAtMost(maxDurability)
        // 修改耐久值
        neigeItems.putInt("durability", durability)
        // 修改最大耐久值
        neigeItems.putInt("maxDurability", maxDurability)
        this.durability = (type.maxDurability * (1 - (durability.toDouble() / maxDurability))).toInt().toShort()
    }

    /**
     * 重构物品
     *
     * @param player 用于重构物品的玩家
     * @param sections 重构节点(值为null代表刷新该节点)
     */
    @JvmStatic
    fun ItemStack.rebuild(player: OfflinePlayer, sections: MutableMap<String, String?>): Boolean {
        return rebuild(player, sections, null)
    }

    /**
     * 重构物品
     *
     * @param player 用于重构物品的玩家
     * @param sections 重构节点(值为null代表刷新该节点)
     * @param protectNBT 需要保护的NBT(重构后不刷新), 可以填null
     * @return 物品是空气时返回false, 其余情况返回true
     */
    @JvmStatic
    fun ItemStack.rebuild(
        player: OfflinePlayer,
        sections: MutableMap<String, String?>,
        protectNBT: List<String>?
    ): Boolean {
        // 判断是不是空气
        if (type != Material.AIR) {
            // 获取NI物品信息
            val itemInfo = isNiItem(true) ?: return true
            // 填入重构节点
            sections.forEach { (key, value) ->
                when (value) {
                    null -> itemInfo.data.remove(key)
                    else -> itemInfo.data[key] = value
                }
            }
            // 生成新物品
            getItemStack(itemInfo.id, player, itemInfo.data)?.let { newItemStack ->
                // 获取新物品的NBT
                newItemStack.getNbt().also { newItemTag ->
                    // 把NeigeItems的特殊NBT掏出来
                    newItemTag.getCompound("NeigeItems")?.also { newNeigeItems ->
                        // 还原物品使用次数
                        if (itemInfo.neigeItems.containsKey("charge")) {
                            newNeigeItems.putInt("charge", itemInfo.neigeItems.getInt("charge"))
                        }
                        // 还原物品自定义耐久
                        if (itemInfo.neigeItems.containsKey("durability")) {
                            newNeigeItems.putInt("durability", itemInfo.neigeItems.getInt("durability"))
                        }
                        // 修复保护NBT
                        protectNBT?.forEach { key ->
                            itemInfo.itemTag.getDeep(key)?.also {
                                newItemTag.putDeep(key, it)
                            }
                        }
                        // 将新物品的NBT覆盖至原物品
                        itemInfo.nbtItemStack.setTag(newItemTag)
                    }
                }
                // 还原物品类型
                type = newItemStack.type
                // 还原损伤值(1.12.2需要)
                durability = newItemStack.durability
            }
            return true
        }
        return false
    }
}
