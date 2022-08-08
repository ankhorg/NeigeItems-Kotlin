package pers.neige.neigeitems.manager

import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.NeigeItems.plugin
import pers.neige.neigeitems.item.ItemConfig
import pers.neige.neigeitems.item.ItemGenerator
import pers.neige.neigeitems.utils.ConfigUtils.clone
import pers.neige.neigeitems.utils.ItemUtils.toMap
import taboolib.module.nms.getItemTag
import java.io.File
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object ItemManager : ItemConfigManager() {
    // 所有物品生成器
    val items: ConcurrentHashMap<String, ItemGenerator> = ConcurrentHashMap<String, ItemGenerator>()
    // 物品总数
    val itemAmount get() = itemIds.size

    init {
        // 初始化所有物品生成器
        loadItems()
    }

    // 初始化所有物品生成器
    private fun loadItems() {
        for ((id, itemConfig) in itemConfigs) {
            items[id] = ItemGenerator(itemConfig)
        }
    }

    // 添加物品
    private fun addItem(itemGenerator: ItemGenerator) {
        itemConfigs[itemGenerator.id] = itemGenerator.itemConfig
        items[itemGenerator.id] = itemGenerator
    }

    // 重载物品管理器
    fun reload() {
        reloadItemConfigs()
        items.clear()
        loadItems()
    }

    // 获取物品
    fun getOriginConfig(id: String): ConfigurationSection? {
        return itemConfigs[id]?.configSection?.clone()
    }

    // 获取物品
    fun getRealOriginConfig(id: String): ConfigurationSection? {
        return itemConfigs[id]?.configSection
    }

    // 获取物品生成器
    fun getItem(id: String): ItemGenerator? {
        return items[id]
    }

    // 获取物品
    fun getItemStack(id: String, player: OfflinePlayer? = null, data: String? = null): ItemStack? {
        return items[id]?.getItemStack(player, data)
    }


    // 获取物品
    fun hasItem(id: String): Boolean {
        return items.containsKey(id)
    }

    /**
     * 保存物品
     * @param itemStack 保存物品
     * @param id 物品ID
     * @param path 保存路径
     * @param cover 是否覆盖
     * @return 1 保存成功; 0 ID冲突; 2 你保存了个空气
     */
    fun saveItem(itemStack: ItemStack, id: String, path: String = "$id.yml", cover: Boolean): Int {
        // 检测是否为空气
        if (itemStack.type != Material.AIR) {
            // 获取路径文件
            val file = File(plugin.dataFolder, "${File.separator}Items${File.separator}$path")
            if(!file.exists()) { file.createNewFile() }
            val config = YamlConfiguration.loadConfiguration(file)
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
                    } catch (error: NoSuchMethodError) {}
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
                        configSection.set("hideflags", it.map { flag -> flag.name })
                    }
                    // 设置物品颜色
                    display?.asCompound()?.let {
                        it["color"]?.asInt()?.let { color ->
                            configSection.set("color", color.toString(16).uppercase(Locale.getDefault()))
                        }
                    }
                    // 设置物品NBT
                    if (!itemNBT.isEmpty()) {
                        configSection.set("nbt", itemNBT.toMap())
                    }
                }
                // 保存文件
                config.save(file)
                // 物品保存好了, 信息加进ItemManager里
                addItem(ItemGenerator(ItemConfig(id, file)))
                if (cover) return 0
                return 1
            }
            return 0
        }
        return 2
    }
}