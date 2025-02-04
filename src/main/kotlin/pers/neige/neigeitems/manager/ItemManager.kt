package pers.neige.neigeitems.manager

import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.slf4j.LoggerFactory
import pers.neige.neigeitems.event.ItemUpdateEvent
import pers.neige.neigeitems.item.ItemConfig
import pers.neige.neigeitems.item.ItemGenerator
import pers.neige.neigeitems.item.ItemInfo
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtUtils
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.annotation.CbVersion
import pers.neige.neigeitems.manager.ConfigManager.debug
import pers.neige.neigeitems.manager.HookerManager.nmsHooker
import pers.neige.neigeitems.utils.ConfigUtils.clone
import pers.neige.neigeitems.utils.ConfigUtils.getFileOrCreate
import pers.neige.neigeitems.utils.ItemUtils.getDamage
import pers.neige.neigeitems.utils.ItemUtils.getDirectTag
import pers.neige.neigeitems.utils.ItemUtils.getItemId
import pers.neige.neigeitems.utils.ItemUtils.getName
import pers.neige.neigeitems.utils.ItemUtils.getNbt
import pers.neige.neigeitems.utils.ItemUtils.getNbtOrNull
import pers.neige.neigeitems.utils.ItemUtils.invalidNBT
import pers.neige.neigeitems.utils.ItemUtils.isNiItem
import pers.neige.neigeitems.utils.ItemUtils.setDamage
import pers.neige.neigeitems.utils.ItemUtils.toStringMap
import pers.neige.neigeitems.utils.LangUtils.sendLang
import pers.neige.neigeitems.utils.SectionUtils.parseSection
import java.io.File
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * 物品管理器
 *
 * @constructor 构建物品管理器
 */
object ItemManager : ItemConfigManager() {
    @JvmStatic
    private val logger = LoggerFactory.getLogger(ItemManager::class.java.simpleName)

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
                    logger.info("  物品-{}-加载耗时: {}ms", id, current)
                }
                time = System.currentTimeMillis()
            }
        }
    }

    /**
     * 添加物品生成器
     *
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
                if (itemStack.getDamage() > 0) {
                    configSection.set("damage", itemStack.getDamage())
                }
                // 如果物品有ItemMeta
                if (itemStack.hasItemMeta()) {
                    // 获取ItemMeta
                    val itemMeta = itemStack.itemMeta
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
                    // 获取物品NBT
                    val itemNBT = itemStack.getNbtOrNull()
                    if (itemNBT != null) {
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
        val file = getFileOrCreate("Items${File.separator}$path")
        val config = YamlConfiguration.loadConfiguration(file)
        return saveItem(itemStack, id, file, config, cover)
    }

    /**
     * 判断ItemStack是否为NI物品并返回NI物品信息
     *
     * @return NI物品信息, 非NI物品返回null
     */
    fun isNiItem(itemStack: ItemStack?): ItemInfo? {
        return itemStack.isNiItem(false)
    }

    /**
     * 判断ItemStack是否为NI物品并返回NI物品信息
     *
     * @param parseData 是否将data解析为HashMap
     * @return NI物品信息, 非NI物品返回null
     */
    fun isNiItem(itemStack: ItemStack?, parseData: Boolean): ItemInfo? {
        return itemStack.isNiItem(parseData)
    }

    /**
     * 根据ItemStack获取对应的NI物品ID
     *
     * @return NI物品ID, 非NI物品返回null
     */
    fun getItemId(itemStack: ItemStack?): String? {
        return itemStack.getItemId()
    }

    /**
     * 设置物品使用次数
     *
     * @param amount 使用次数
     */
    @JvmStatic
    fun ItemStack.setCharge(amount: Int) {
        // 直掏NBT
        val directTag = this.getDirectTag() ?: return
        // 不是NI物品还加个屁的使用次数
        val neigeItems = directTag.getCompound("NeigeItems") ?: return
        // 获取物品最大使用次数
        val maxCharge = neigeItems.getIntOrNull("maxCharge") ?: return
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
        val directTag = this.getDirectTag() ?: return
        // 不是NI物品还加个屁的使用次数
        val neigeItems = directTag.getCompound("NeigeItems") ?: return
        // 获取物品使用次数
        val charge = neigeItems.getIntOrNull("charge") ?: return
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
        val directTag = this.getDirectTag() ?: return
        // 不是NI物品还加个屁的使用次数
        val neigeItems = directTag.getCompound("NeigeItems") ?: return
        // 计算物品使用次数
        val charge = (neigeItems.getIntOrNull("charge") ?: return).coerceAtMost(amount.coerceAtLeast(1))
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
        val directTag = this.getDirectTag() ?: return
        // 不是NI物品还加个屁的使用次数
        val neigeItems = directTag.getCompound("NeigeItems") ?: return
        // 计算物品最大使用次数
        val maxCharge = ((neigeItems.getIntOrNull("maxCharge") ?: return) + amount).coerceAtLeast(1)
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
        val directTag = this.getDirectTag() ?: return
        // 不是NI物品还加个屁的自定义耐久
        val neigeItems = directTag.getCompound("NeigeItems") ?: return
        // 获取物品最大耐久值
        val maxDurability = neigeItems.getIntOrNull("maxDurability") ?: return
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
            this.refreshDurability(newDurability, maxDurability)
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
        val directTag = this.getDirectTag() ?: return
        // 不是NI物品还加个屁的自定义耐久
        val neigeItems = directTag.getCompound("NeigeItems") ?: return
        // 获取物品耐久值
        val durability = neigeItems.getIntOrNull("durability") ?: return
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
            this.refreshDurability(newDurability, maxDurability)
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
        val directTag = this.getDirectTag() ?: return
        // 不是NI物品还加个屁的自定义耐久
        val neigeItems = directTag.getCompound("NeigeItems") ?: return
        // 获取物品耐久值
        val durability = (neigeItems.getIntOrNull("durability") ?: return).coerceAtMost(realAmount)
        // 修改耐久值
        neigeItems.putInt("durability", durability)
        // 修改最大耐久值
        neigeItems.putInt("maxDurability", realAmount)
        this.refreshDurability(durability, realAmount)
    }

    /**
     * 添加物品最大耐久值
     *
     * @param amount 添加量(可为负)
     */
    @JvmStatic
    fun ItemStack.addMaxCustomDurability(amount: Int) {
        // 直掏NBT
        val directTag = this.getDirectTag() ?: return
        // 不是NI物品还加个屁的自定义耐久
        val neigeItems = directTag.getCompound("NeigeItems") ?: return
        // 修改后的最大耐久值
        val maxDurability = ((neigeItems.getIntOrNull("maxDurability") ?: return) + amount).coerceAtLeast(1)
        // 获取物品耐久值
        val durability = neigeItems.getInt("durability").coerceAtMost(maxDurability)
        // 修改耐久值
        neigeItems.putInt("durability", durability)
        // 修改最大耐久值
        neigeItems.putInt("maxDurability", maxDurability)
        this.refreshDurability(durability, maxDurability)
    }

    /**
     * 根据自定义耐久计算原版损伤值
     *
     * @param current 当前自定义耐久
     * @param max 当前最大自定义耐久
     * @return 对应的原版损伤值
     */
    @JvmStatic
    fun ItemStack.checkDurability(current: Int, max: Int): Short {
        // 耐久百分比
        val p = current.toDouble() / max
        // 损伤值百分比
        val dp = 1 - p
        // 损伤值
        var d = (type.maxDurability * dp).toInt().toShort()
        // 没满就是没满
        if (d <= 0 && current < max) {
            d = 1
        }
        // 没碎就是没碎
        if (d >= type.maxDurability && current > 0) {
            d = (d - 1).toShort()
        }
        return d
    }

    /**
     * 根据自定义耐久设置原版损伤值
     *
     * @param current 当前自定义耐久
     * @param max 当前最大自定义耐久
     */
    @JvmStatic
    fun ItemStack.refreshDurability(current: Int, max: Int) {
        this.setDamage(this.checkDurability(current, max))
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
        player: OfflinePlayer, sections: MutableMap<String, String?>, protectNBT: List<String>?
    ): Boolean {
        // 判断是不是空气
        if (type == Material.AIR) return false
        // 获取NI物品信息
        val itemInfo = isNiItem(true) ?: return true
        // 填入重构节点
        sections.forEach { (key, value) ->
            if (value == null) {
                itemInfo.data.remove(key)
            } else {
                itemInfo.data[key] = value
            }
        }
        // 生成新物品
        val newItemStack = getItemStack(itemInfo.id, player, itemInfo.data) ?: return true
        rebuildHandler(newItemStack, itemInfo, protectNBT)
        return true
    }

    /**
     * 重构物品
     *
     * @param player 用于重构物品的玩家
     * @param protectSections 保留的节点ID
     * @param protectNBT 需要保护的NBT(重构后不刷新), 可以填null
     * @return 物品是空气时返回false, 其余情况返回true
     */
    @JvmStatic
    fun ItemStack.rebuild(
        player: OfflinePlayer, protectSections: List<String>, protectNBT: List<String>?
    ): Boolean {
        // 判断是不是空气
        if (type == Material.AIR) return false
        // 获取NI物品信息
        val itemInfo = isNiItem(true) ?: return true
        // 填入重构节点
        val data = mutableMapOf<String, String>()
        protectSections.forEach { key ->
            itemInfo.data[key]?.let {
                data[key] = it
            }
        }
        // 生成新物品
        val newItemStack = getItemStack(itemInfo.id, player, data) ?: return true
        rebuildHandler(newItemStack, itemInfo, protectNBT)
        return true
    }


    @JvmStatic
    private fun ItemStack.rebuildHandler(
        newItemStack: ItemStack, itemInfo: ItemInfo, protectNBT: List<String>?
    ) {
        // 还原物品类型
        type = newItemStack.type
        // 还原损伤值(1.12.2需要)
        if (CbVersion.current() == CbVersion.v1_12_R1) {
            setDamage(newItemStack.getDamage())
        }
        // 获取新物品的NBT
        val newItemTag = newItemStack.getNbt()
        // 把NeigeItems的特殊NBT掏出来
        val newNeigeItems = newItemTag.getCompound("NeigeItems")
        if (newNeigeItems != null) {
            // 还原物品使用次数
            if (itemInfo.neigeItems.containsKey("charge")) {
                newNeigeItems.putInt("charge", itemInfo.neigeItems.getInt("charge"))
            }
            // 还原物品自定义耐久
            if (itemInfo.neigeItems.containsKey("durability")) {
                newNeigeItems.putInt("durability", itemInfo.neigeItems.getInt("durability"))
            }
        }
        // 修复保护NBT
        protectNBT?.forEach { key ->
            val pre = itemInfo.itemTag.getDeep(key) ?: return@forEach
            newItemTag.putDeep(key, pre)
        }
        // 将新物品的组件覆盖至原物品
        NbtUtils.setComponents(this, newItemStack)
        // 将新物品的NBT覆盖至原物品
        itemInfo.nbtItemStack.setTag(newItemTag)
    }

    /**
     * 根据物品内的 options.update 配置进行物品更新
     *
     * @param player 用于重构物品的玩家
     * @param itemStack 物品本身
     * @param forceUpdate 是否忽略检测, 强制更新物品
     * @param sendMessage 更新后是否向玩家发送更新提示文本
     */
    fun update(
        player: Player, itemStack: ItemStack, forceUpdate: Boolean = false, sendMessage: Boolean = false
    ) {
        val itemInfo = itemStack.isNiItem() ?: return
        val neigeItems = itemInfo.neigeItems
        // 物品更新
        val item = getItem(itemInfo.id) ?: return
        // 未开启物品更新功能, 中止操作
        if (!item.update) return
        // 检测hashCode匹配情况, 匹配代表不需要更新
        if (neigeItems.getInt("hashCode", item.hashCode) == item.hashCode && !forceUpdate) return
        val data = itemInfo.data
        // 获取待重构节点
        val rebuild = hashMapOf<String, String>().also {
            item.rebuildData?.forEach { (key, value) ->
                it[key.parseSection(data, player, item.sections)] = value.parseSection(data, player, item.sections)
            }
        }
        // 获取待刷新节点
        val refresh = arrayListOf<String>().also {
            item.refreshData.forEach { key ->
                it.add(key.parseSection(data, player, item.sections))
            }
        }
        // 进行待重构节点覆盖
        rebuild.forEach { (key, value) ->
            data[key] = value
        }
        // 进行待刷新节点移除
        refresh.forEach { key ->
            data.remove(key)
        }
        // 触发预生成事件
        val preGenerateEvent = ItemUpdateEvent.PreGenerate(player, itemStack, data, item)
        preGenerateEvent.call()
        if (preGenerateEvent.isCancelled) return
        // 生成新物品
        val newItemStack = preGenerateEvent.item.getItemStack(player, preGenerateEvent.data) ?: return
        // 触发生成后事件
        val postGenerateEvent = ItemUpdateEvent.PostGenerate(player, itemStack, newItemStack)
        postGenerateEvent.call()
        if (postGenerateEvent.isCancelled) return
        // 获取旧物品名称
        val oldName = itemStack.getName()
        // 获取新物品的NBT
        val newItemTag = newItemStack.getNbt()
        // 还原物品类型
        itemStack.type = newItemStack.type
        // 还原损伤值(1.12.2需要)
        if (CbVersion.current() == CbVersion.v1_12_R1) {
            itemStack.setDamage(newItemStack.getDamage())
        }
        // 发送提示信息
        if (sendMessage) {
            player.sendLang("Messages.legacyItemUpdateMessage", mapOf("{name}" to oldName))
        }
        // 把NeigeItems的特殊NBT掏出来
        val newNeigeItems = newItemTag.getCompound("NeigeItems")
        if (newNeigeItems != null) {
            // 还原物品使用次数
            if (neigeItems.containsKey("charge")) {
                newNeigeItems.putInt("charge", neigeItems.getInt("charge"))
            }
            // 还原物品自定义耐久
            if (neigeItems.containsKey("durability")) {
                newNeigeItems.putInt("durability", neigeItems.getInt("durability"))
            }
        }
        // 修复保护NBT
        preGenerateEvent.item.protectNBT.forEach { key ->
            val pre = itemInfo.itemTag.getDeep(key) ?: return@forEach
            newItemTag.putDeep(key, pre)
        }
        // 将新物品的组件覆盖至原物品
        NbtUtils.setComponents(itemStack, newItemStack)
        // 将新物品的NBT覆盖至原物品
        itemInfo.nbtItemStack.setTag(newItemTag)
    }
}
