package pers.neige.neigeitems.manager

import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.NeigeItems.plugin
import pers.neige.neigeitems.item.ItemConfig
import pers.neige.neigeitems.item.ItemGenerator
import pers.neige.neigeitems.item.ItemInfo
import pers.neige.neigeitems.manager.ConfigManager.debug
import pers.neige.neigeitems.utils.ConfigUtils.clone
import pers.neige.neigeitems.utils.ItemUtils.getDeepOrNull
import pers.neige.neigeitems.utils.ItemUtils.invalidNBT
import pers.neige.neigeitems.utils.ItemUtils.isNiItem
import pers.neige.neigeitems.utils.ItemUtils.putDeepFixed
import pers.neige.neigeitems.utils.ItemUtils.toMap
import taboolib.module.nms.ItemTagData
import taboolib.module.nms.getItemTag
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
                    println("  物品-$id-加载耗时: ${current}ms")
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
        return getItemStack(id, null, null as? HashMap<String, String>)
    }

    /**
     * 获取物品
     *
     * @param id 物品ID
     * @param player 用于解析物品的玩家
     * @return 物品
     */
    fun getItemStack(id: String, player: OfflinePlayer?): ItemStack? {
        return getItemStack(id, player, null as? HashMap<String, String>)
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
    fun getItemStack(id: String, data: HashMap<String, String>?): ItemStack? {
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
    fun getItemStack(id: String, player: OfflinePlayer?, data: HashMap<String, String>?): ItemStack? {
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
                    val itemNBT = itemStack.getItemTag()
                    // 获取显示信息
                    val display = itemNBT["display"]
                    itemNBT.remove("display")
                    // 设置CustomModelData
                    try {
                        if (itemMeta?.hasCustomModelData() == true) {
                            configSection.set("custommodeldata", itemMeta.customModelData)
                        }
                    } catch (_: NoSuchMethodError) {}
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
                    itemMeta?.itemFlags?.let{
                        if (it.isNotEmpty()) {
                            configSection.set("hideflags", it.map { flag -> flag.name })
                        }
                    }
                    // 设置物品颜色
                    display?.asCompound()?.let {
                        it["color"]?.asInt()?.let { color ->
                            configSection.set("color", color.toString(16).uppercase(Locale.getDefault()))
                        }
                    }
                    // 设置物品NBT
                    if (!itemNBT.isEmpty()) {
                        configSection.set("nbt", itemNBT.toMap(invalidNBT))
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
        if(!file.exists()) { file.createNewFile() }
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
        // 获取物品NBT
        val itemTag = getItemTag()
        // 获取物品最大使用次数(不存在则停止操作)
        val maxCharge = itemTag.getDeepOrNull("NeigeItems.maxCharge")?.asInt() ?: return
        // 计算新使用次数
        val newCharge = amount.coerceAtMost(maxCharge).coerceAtLeast(0)
        // 修改使用次数
        itemTag.putDeepFixed("NeigeItems.charge", ItemTagData(maxCharge))
        // 保存修改
        itemTag.saveTo(this)
    }

    /**
     * 添加物品使用次数
     *
     * @param amount 添加量(可为负)
     */
    @JvmStatic
    fun ItemStack.addCharge(amount: Int) {
        // 获取物品NBT
        val itemTag = getItemTag()
        // 获取物品使用次数(不存在则停止操作)
        val charge = itemTag.getDeepOrNull("NeigeItems.charge")?.asInt() ?: return
        // 获取物品最大使用次数
        val maxCharge = itemTag.getDeepOrNull("NeigeItems.maxCharge")!!.asInt()
        // 计算新使用次数
        val newCharge = (charge + amount).coerceAtMost(maxCharge).coerceAtLeast(0)
        // 修改使用次数
        itemTag.putDeepFixed("NeigeItems.charge", ItemTagData(newCharge))
        // 保存修改
        itemTag.saveTo(this)
    }

    /**
     * 设置物品最大使用次数
     *
     * @param amount 最大使用次数
     */
    @JvmStatic
    fun ItemStack.setMaxCharge(amount: Int) {
        // 获取物品NBT
        val itemTag = getItemTag()
        // 获取物品耐久值(不存在则停止操作)
        val charge = (itemTag.getDeepOrNull("NeigeItems.charge")?.asInt() ?: return).coerceAtMost(amount.coerceAtLeast(1))
        // 修改耐久值
        itemTag.putDeepFixed("NeigeItems.charge", ItemTagData(charge))
        // 修改最大耐久值
        itemTag.putDeepFixed("NeigeItems.maxCharge", ItemTagData(amount.coerceAtLeast(1)))
        // 保存修改
        itemTag.saveTo(this)
    }

    /**
     * 添加物品最大使用次数
     *
     * @param amount 添加量(可为负)
     */
    @JvmStatic
    fun ItemStack.addMaxCharge(amount: Int) {
        // 获取物品NBT
        val itemTag = getItemTag()
        // 获取物品最大耐久值
        val maxCharge = ((itemTag.getDeepOrNull("NeigeItems.maxCharge")?.asInt() ?: return) + amount).coerceAtLeast(1)
        // 获取物品耐久值(不存在则停止操作)
        val charge = itemTag.getDeepOrNull("NeigeItems.charge")!!.asInt().coerceAtMost(maxCharge)
        // 修改耐久值
        itemTag.putDeepFixed("NeigeItems.charge", ItemTagData(charge))
        // 修改最大耐久值
        itemTag.putDeepFixed("NeigeItems.maxCharge", ItemTagData(maxCharge))
        // 保存修改
        itemTag.saveTo(this)
    }

    /**
     * 设置物品耐久值
     *
     * @param amount 耐久值
     */
    @JvmStatic
    fun ItemStack.setCustomDurability(amount: Int) {
        // 获取物品NBT
        val itemTag = getItemTag()
        // 获取物品最大耐久值(不存在则停止操作)
        val maxDurability = itemTag.getDeepOrNull("NeigeItems.maxDurability")?.asInt() ?: return
        // 计算新耐久值
        val newDurability = amount.coerceAtMost(maxDurability).coerceAtLeast(0)
        // 修改耐久值
        itemTag.putDeepFixed("NeigeItems.durability", ItemTagData(newDurability))
        this.durability = (this.durability * (1 - (newDurability.toDouble()/maxDurability))).toInt().toShort()
        // 保存修改
        itemTag.saveTo(this)
    }

    /**
     * 添加物品耐久值
     *
     * @param amount 添加量(可为负)
     */
    @JvmStatic
    fun ItemStack.addCustomDurability(amount: Int) {
        // 获取物品NBT
        val itemTag = getItemTag()
        // 获取物品耐久值(不存在则停止操作)
        val durability = itemTag.getDeepOrNull("NeigeItems.durability")?.asInt() ?: return
        // 获取物品最大耐久值
        val maxDurability = itemTag.getDeepOrNull("NeigeItems.maxDurability")!!.asInt()
        // 计算新耐久值
        val newDurability = (durability + amount).coerceAtMost(maxDurability).coerceAtLeast(0)
        // 修改耐久值
        itemTag.putDeepFixed("NeigeItems.durability", ItemTagData(newDurability))
        this.durability = (this.durability * (1 - (newDurability.toDouble()/maxDurability))).toInt().toShort()
        // 保存修改
        itemTag.saveTo(this)
    }

    /**
     * 设置物品最大耐久值
     *
     * @param amount 最大耐久值
     */
    @JvmStatic
    fun ItemStack.setMaxCustomDurability(amount: Int) {
        // 获取物品NBT
        val itemTag = getItemTag()
        // 获取物品耐久值(不存在则停止操作)
        val durability = (itemTag.getDeepOrNull("NeigeItems.durability")?.asInt() ?: return).coerceAtMost(amount.coerceAtLeast(1))
        // 修改耐久值
        itemTag.putDeepFixed("NeigeItems.durability", ItemTagData(durability))
        // 修改最大耐久值
        itemTag.putDeepFixed("NeigeItems.maxDurability", ItemTagData(amount.coerceAtLeast(1)))
        this.durability = (this.durability * (1 - (durability.toDouble()/amount))).toInt().toShort()
        // 保存修改
        itemTag.saveTo(this)
    }

    /**
     * 添加物品最大耐久值
     *
     * @param amount 添加量(可为负)
     */
    @JvmStatic
    fun ItemStack.addMaxCustomDurability(amount: Int) {
        // 获取物品NBT
        val itemTag = getItemTag()
        // 获取物品最大耐久值
        val maxDurability = ((itemTag.getDeepOrNull("NeigeItems.maxDurability")?.asInt() ?: return) + amount).coerceAtLeast(1)
        // 获取物品耐久值(不存在则停止操作)
        val durability = itemTag.getDeepOrNull("NeigeItems.durability")!!.asInt().coerceAtMost(maxDurability)
        // 修改耐久值
        itemTag.putDeepFixed("NeigeItems.durability", ItemTagData(durability))
        // 修改最大耐久值
        itemTag.putDeepFixed("NeigeItems.maxDurability", ItemTagData(maxDurability))
        this.durability = (this.durability * (1 - (durability.toDouble()/maxDurability))).toInt().toShort()
        // 保存修改
        itemTag.saveTo(this)
    }
}
