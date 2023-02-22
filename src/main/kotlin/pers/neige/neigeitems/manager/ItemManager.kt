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
import pers.neige.neigeitems.utils.ConfigUtils.clone
import pers.neige.neigeitems.utils.ItemUtils.invalidNBT
import pers.neige.neigeitems.utils.ItemUtils.isNiItem
import pers.neige.neigeitems.utils.ItemUtils.toMap
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
        for ((id, itemConfig) in itemConfigs) {
            items[id] = ItemGenerator(itemConfig)
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
        return getItemStack(id, null, HashMap<String, String>())
    }

    /**
     * 获取物品
     *
     * @param id 物品ID
     * @param player 用于解析物品的玩家
     * @return 物品
     */
    fun getItemStack(id: String, player: OfflinePlayer?): ItemStack? {
        return getItemStack(id, player, HashMap<String, String>())
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
                    // 设置子ID/损伤值
                    if (itemStack.durability > 0) {
                        configSection.set("damage", itemStack.durability)
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
}