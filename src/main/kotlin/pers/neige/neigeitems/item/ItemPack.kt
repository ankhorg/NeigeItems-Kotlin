package pers.neige.neigeitems.item

import com.alibaba.fastjson2.parseObject
import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.manager.ConfigManager
import pers.neige.neigeitems.manager.HookerManager
import pers.neige.neigeitems.manager.ItemManager
import pers.neige.neigeitems.utils.ConfigUtils.clone
import pers.neige.neigeitems.utils.ConfigUtils.loadFromString
import pers.neige.neigeitems.utils.ConfigUtils.loadGlobalSections
import pers.neige.neigeitems.utils.ConfigUtils.saveToString
import pers.neige.neigeitems.utils.ItemUtils
import pers.neige.neigeitems.utils.ItemUtils.getItems
import pers.neige.neigeitems.utils.SamplingUtils.aExpj
import pers.neige.neigeitems.utils.SectionUtils.parseSection
import java.util.HashMap
import java.util.concurrent.ThreadLocalRandom

/**
 * 物品包信息
 *
 * @property id 物品包ID
 * @param rawConfigSection 物品包配置
 */
class ItemPack(
    /**
     * 获取物品包ID
     */
    val id: String,
    rawConfigSection: ConfigurationSection
) {
    /**
     * 获取物品包配置
     */
    val configSection = loadGlobalSections(rawConfigSection)

    /**
     * 获取物品包物品配置
     */
    val items: List<String> = configSection.getStringList("Items")

    /**
     * 获取物品包物品配置
     */
    val sections: ConfigurationSection? = configSection.getConfigurationSection("sections").also {
        configSection.set("sections", null)
    }

    /**
     * 获取物品包多彩掉落配置
     */
    val fancyDropConfig = configSection.getConfigurationSection("FancyDrop")

    /**
     * 获取物品包是否配置了多彩掉落信息
     */
    val fancyDrop = fancyDropConfig != null

    /**
     * 获取多彩掉落横向偏移文本
     */
    val offsetXString: String? = fancyDropConfig?.getString("offset.x")

    /**
     * 获取多彩掉落纵向偏移文本
     */
    val offsetYString: String? = fancyDropConfig?.getString("offset.y")

    /**
     * 获取多彩掉落类型文本
     */
    val angleType: String? = fancyDropConfig?.getString("angle.type")

    /**
     * 获取解析后物品包配置
     *
     * @return 解析后物品包配置
     */
    fun getSection(): ConfigurationSection? {
        return getSection(null, null as? HashMap<String, String>)
    }

    /**
     * 获取解析后物品包配置
     *
     * @param player 用于解析内容的玩家
     * @return 解析后物品包配置
     */
    fun getSection(player: OfflinePlayer?): ConfigurationSection? {
        return getSection(player, null as? HashMap<String, String>)
    }

    /**
     * 获取解析后物品包配置
     *
     * @param player 用于解析内容的玩家
     * @param data 指向数据
     * @return 解析后物品包配置
     */
    fun getSection(player: OfflinePlayer?, data: String?): ConfigurationSection? {
        return getSection(player, when (data) {
            null -> HashMap<String, String>()
            else -> data.parseObject<HashMap<String, String>>()
        })
    }

    /**
     * 获取解析后物品包配置
     *
     * @param player 用于解析内容的玩家
     * @param data 指向数据
     * @return 解析后物品包配置
     */
    fun getSection(player: OfflinePlayer?, data: HashMap<String, String>?): ConfigurationSection? {
        // 物品包configuration
        val configSection = this.configSection.clone()
        // 加载缓存
        val cache = data ?: HashMap<String, String>()

        // 获取私有节点配置
        val sections = this.sections
        // 对文本化配置进行全局节点解析
        val configString = configSection
            .saveToString(id)
            .parseSection(cache, player, sections)
        // Debug信息
        if (ConfigManager.config.getBoolean("Main.Debug")) print(configString)
        if (ConfigManager.config.getBoolean("Main.Debug") && sections != null) print(sections.saveToString("$id-sections"))
        return configString.loadFromString(id) ?: YamlConfiguration()
    }

    /**
     * 获取物品
     *
     * @return 物品
     */
    fun getItemStacks(): List<ItemStack> {
        return getItemStacks(null, null as? HashMap<String, String>)
    }

    /**
     * 获取物品
     *
     * @param player 用于解析内容的玩家
     * @return 物品
     */
    fun getItemStacks(player: OfflinePlayer?): List<ItemStack> {
        return getItemStacks(player, null as? HashMap<String, String>)
    }

    /**
     * 获取物品
     *
     * @param player 用于解析内容的玩家
     * @param data 指向数据
     * @return 物品
     */
    fun getItemStacks(player: OfflinePlayer?, data: String?): List<ItemStack> {
        return getItemStacks(player, when (data) {
            null -> HashMap<String, String>()
            else -> data.parseObject<HashMap<String, String>>()
        })
    }

    /**
     * 获取物品
     *
     * @param player 用于解析内容的玩家
     * @param data 指向数据
     * @return 物品
     */
    fun getItemStacks(player: OfflinePlayer?, data: HashMap<String, String>?): List<ItemStack> {
        val itemStacks = ArrayList<ItemStack>()
        getSection(player, data)?.also { config ->
            val minItems = config.getInt("MinItems", -1)
            val maxItems = config.getInt("MaxItems", -1)
            val items = config.getStringList("Items")
            // 最小值为null或大于0的整数
            val trueMin = when {
                (minItems > 0) -> minItems
                else -> null
            }
            // 最大值为null或大于最小值(没有最小值时也需要大于0)且小于物品条目总数的整数
            val trueMax = when {
                (maxItems > 0 && (trueMin == null || maxItems > trueMin) && maxItems < items.size) -> maxItems
                else -> null
            }
            // 不指定最小/最大掉落行数
            if (trueMin == null && trueMax == null) {
                items.forEach {
                    it.split("\n").forEach { info ->
                        itemStacks.addAll(ItemInfo(info).getItemStacks(player))
                    }
                }
                // 仅指定最大掉落数
            } else if (trueMin == null && trueMax != null) {
                var amount = 0
                all@ for (rawInfo in items) {
                    // 根据换行符分割, 分割完遍历随机
                    for (info in rawInfo.split("\n")) {
                        val currentItems = ItemInfo(info).getItemStacks(player)
                        // 如果当前物品不为空
                        if (currentItems.isNotEmpty()) {
                            // 如果数量还够
                            if (trueMax > amount) {
                                // 怼进去
                                itemStacks.addAll(currentItems)
                                // 记录数量
                                amount ++
                                // 数量超了, 停止操作
                            } else {
                                break@all
                            }
                        }
                    }
                }
                // 仅指定最小掉落数
            } else if (trueMax == null && trueMin != null) {
                // 记录所有物品信息
                val info = HashMap<ItemInfo, Double>()
                for (rawInfo in items) {
                    // 根据换行符分割, 分割完遍历随机
                    for (value in rawInfo.split("\n")) {
                        val itemInfo = ItemInfo(value)
                        info[itemInfo] = itemInfo.probability * 100000
                    }
                }

                // 根据概率抽选最小数量信息, 将概率更改为100%(即必定成功生成)
                aExpj(info, trueMin).forEach { itemInfo ->
                    itemInfo.probability = 1.0
                }
                // 添加物品
                info.forEach { (itemInfo, _) ->
                    itemStacks.addAll(itemInfo.getItemStacks(player))
                }
                // 指定最大/最小掉落数
            } else if (trueMin != null && trueMax != null) {
                // 记录所有物品信息
                val info = HashMap<ItemInfo, Double>()
                for (rawInfo in items) {
                    // 根据换行符分割, 分割完遍历随机
                    for (value in rawInfo.split("\n")) {
                        val itemInfo = ItemInfo(value)
                        if (itemInfo.probability > 0) {
                            info[itemInfo] = itemInfo.probability * 100000
                        }
                    }
                }

                // 根据概率抽选最小数量信息, 将概率更改为100%(即必定成功生成)
                aExpj(info, trueMin).forEach { itemInfo ->
                    itemInfo.probability = 1.0
                }
                var amount = 0
                // 添加物品
                info.forEach { (itemInfo, _) ->
                    val currentItems = itemInfo.getItemStacks(player)
                    // 如果当前物品不为空
                    if (currentItems.isNotEmpty()) {
                        // 如果数量还够
                        if (trueMax > amount) {
                            // 怼进去
                            itemStacks.addAll(currentItems)
                            // 记录数量
                            amount ++
                            // 数量超了, 停止操作
                        } else {
                            return@forEach
                        }
                    }
                }
            }
        }
        return itemStacks
    }

    /**
     * 物品包物品信息
     *
     * @property info 物品ID (数量(或随机最小数量-随机最大数量)) (生成概率) (是否反复随机) (指向数据)
     */
    class ItemInfo(val info: String) {
        /**
         * 获取物品参数
         */
        val args: List<String> = info.split(" ", limit = 5)

        /**
         * 获取物品ID
         */
        var id = args[0]

        /**
         * 获取物品数量
         */
        var amount = args.getOrNull(1)?.let {
            when {
                it.contains("-") -> {
                    val index = args[1].indexOf("-")
                    val min = args[1].substring(0, index).toIntOrNull()
                    val max = args[1].substring(index+1, args[1].length).toIntOrNull()
                    if (min != null && max != null) {
                        ThreadLocalRandom.current().nextInt(min, max+1)
                    } else {
                        null
                    }
                }
                else -> {
                    it.toIntOrNull()
                }
            }
        } ?: 1

        /**
         * 获取生成概率(0-1)
         */
        var probability = args.getOrNull(2)?.toDoubleOrNull() ?: 1.0

        /**
         * 获取是否反复随机
         */
        var random = args.getOrNull(3)?.toBooleanStrictOrNull() ?: true

        /**
         * 获取指向数据
         */
        var data: String? = args.getOrNull(4)

        /**
         * 生成物品
         *
         * @return 生成出的物品
         */
        fun getItemStacks(): ArrayList<ItemStack> {
            return getItemStacks(null)
        }

        /**
         * 生成物品
         *
         * @param player 用于解析物品的玩家
         * @return 生成出的物品
         */
        fun getItemStacks(player: OfflinePlayer?): ArrayList<ItemStack> {
            return ArrayList<ItemStack>().also { itemStacks ->
                // 进行概率随机
                if (ThreadLocalRandom.current().nextDouble() > probability) return@also
                // 看看需不需要每次都随机生成
                if (random) {
                    // 随机生成, 那疯狂造就完事儿了
                    when {
                        ItemManager.hasItem(id) -> {
                            repeat(amount) {
                                ItemManager.getItemStack(id, player, data)?.let { itemStack ->
                                    itemStacks.add(itemStack)
                                }
                            }
                        }
                        HookerManager.easyItemHooker?.hasItem(id) == true -> {
                            HookerManager.easyItemHooker?.getItemStack(id)?.getItems(amount)?.forEach { itemStacks.add(it) }
                        }
                        // 对于MM物品, 这个配置项不代表是否随机生成, 代表物品是否合并
                        else -> {
                            HookerManager.mythicMobsHooker?.getItemStackSync(id)?.getItems(amount)?.forEach { itemStacks.add(it) }
                        }
                    }
                } else {
                    // 真只随机一次啊?那嗯怼吧
                    when {
                        ItemManager.hasItem(id) -> {
                            ItemManager.getItemStack(id, player, data)?.getItems(amount)?.forEach { itemStacks.add(it) }
                        }
                        HookerManager.easyItemHooker?.hasItem(id) == true -> {
                            HookerManager.easyItemHooker?.getItemStack(id)?.let { itemStack ->
                                repeat(amount) {
                                    itemStacks.add(itemStack)
                                }
                            }
                        }
                        else -> {
                            HookerManager.mythicMobsHooker?.getItemStackSync(id)?.let { itemStack ->
                                repeat(amount) {
                                    itemStacks.add(itemStack)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}