package pers.neige.neigeitems.utils

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Entity
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import pers.neige.neigeitems.config.ConfigReader
import pers.neige.neigeitems.item.ItemInfo
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.*
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.api.NbtComponentLike
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.annotation.CbVersion
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.TranslationUtils
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.WorldUtils
import pers.neige.neigeitems.manager.HookerManager.getHookedItem
import pers.neige.neigeitems.manager.HookerManager.mythicMobsHooker
import pers.neige.neigeitems.manager.ItemManager
import pers.neige.neigeitems.utils.PlayerUtils.setMetadataEZ
import pers.neige.neigeitems.utils.SchedulerUtils.callSyncMethod
import pers.neige.neigeitems.utils.SectionUtils.parseSection
import pers.neige.neigeitems.utils.StringUtils.split
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


/**
 * 物品相关工具类
 */
object ItemUtils {
    @JvmStatic
    private val GET_DAMAGE_FROM_ITEM_STACK = CbVersion.current() == CbVersion.v1_12_R1 || CbVersion.v1_20_R4.isSupport

    /**
     * 根据物品获取显示名, 无显示名则返回翻译名.
     *
     * @return 显示名或翻译名.
     */
    @JvmStatic
    fun ItemStack.getName(): String {
        return TranslationUtils.getDisplayOrTranslationName(this)
    }

    /**
     * HashMap 转 NbtCompound
     *
     * @return 转换结果
     */
    @JvmStatic
    fun Map<*, *>.toNbtCompound(): NbtCompound {
        val itemTag = NbtCompound()
        for ((key, value) in this) {
            if (value != null) {
                itemTag[key as String] = value.toNbt()
            }
        }
        return itemTag
    }

    /**
     * ConfigurationSection 转 NbtCompound
     *
     * @return 转换结果
     */
    @JvmStatic
    fun ConfigurationSection.toNbtCompound(): NbtCompound {
        val itemTag = NbtCompound()
        this.getKeys(false).forEach { key ->
            this.get(key)?.let { value ->
                itemTag[key as String] = value.toNbt()
            }
        }
        return itemTag
    }

    /**
     * ConfigReader 转 NbtCompound
     *
     * @return 转换结果
     */
    @JvmStatic
    fun ConfigReader.toNbtCompound(): NbtCompound {
        val itemTag = NbtCompound()
        this.keySet().forEach { key ->
            this.get(key)?.let { value ->
                itemTag[key as String] = value.toNbt()
            }
        }
        return itemTag
    }

    /**
     * 类型强制转换
     * 用于解析 NeigeItems 物品 NBT 配置中的类型强制转换
     *
     * @return 转换结果
     */
    @JvmStatic
    fun String.cast(): Any {
        if (this.startsWith("(")) {
            return when {
                this.startsWith("(String) ") -> this.substring(9, this.length)
                this.startsWith("(Byte) ") -> this.substring(7, this.length).toByteOrNull() ?: this
                this.startsWith("(Short) ") -> this.substring(8, this.length).toShortOrNull() ?: this
                this.startsWith("(Int) ") -> this.substring(6, this.length).toIntOrNull() ?: this
                this.startsWith("(Long) ") -> this.substring(7, this.length).toLongOrNull() ?: this
                this.startsWith("(Float) ") -> this.substring(8, this.length).toFloatOrNull() ?: this
                this.startsWith("(Double) ") -> this.substring(9, this.length).toDoubleOrNull() ?: this
                else -> this
            }
        } else if (this.startsWith("[") && this.endsWith("]")) {
            val list = this.substring(1, this.lastIndex).split(",").map { it.cast() }
            return when {
                list.isEmpty() -> NbtString.valueOf(this)
                list[0] is Byte -> (list as List<Byte>).toTypedArray()
                list[0] is Int -> (list as List<Int>).toTypedArray()
                list[0] is Long -> (list as List<Long>).toTypedArray()
                else -> this
            }
        } else {
            return this
        }
    }

    /**
     * 类型强制转换
     * 用于解析 NeigeItems 物品 NBT 配置中的类型强制转换
     *
     * @return 转换结果
     */
    @JvmStatic
    fun Any.cast(): Any {
        return when (this) {
            is String -> this.cast()
            else -> this
        }
    }

    /**
     * 类型强制转换
     * 用于解析 NeigeItems 物品 NBT 配置中的类型强制转换
     *
     * @return 转换结果
     */
    @JvmStatic
    fun String.castToNbt(): Nbt<*> {
        if (this.startsWith("(")) {
            return when {
                this.startsWith("(String) ") -> {
                    NbtString.valueOf(this.substring(9, this.length))
                }

                this.startsWith("(Byte) ") -> {
                    this.substring(7, this.length).toByteOrNull()?.let { NbtByte.valueOf(it) }
                        ?: NbtString.valueOf(this)
                }

                this.startsWith("(Short) ") -> {
                    this.substring(8, this.length).toShortOrNull()?.let { NbtShort.valueOf(it) } ?: NbtString.valueOf(
                        this
                    )
                }

                this.startsWith("(Int) ") -> {
                    this.substring(6, this.length).toIntOrNull()?.let { NbtInt.valueOf(it) } ?: NbtString.valueOf(this)
                }

                this.startsWith("(Long) ") -> {
                    this.substring(7, this.length).toLongOrNull()?.let { NbtLong.valueOf(it) }
                        ?: NbtString.valueOf(this)
                }

                this.startsWith("(Float) ") -> {
                    this.substring(8, this.length).toFloatOrNull()?.let { NbtFloat.valueOf(it) } ?: NbtString.valueOf(
                        this
                    )
                }

                this.startsWith("(Double) ") -> {
                    this.substring(9, this.length).toDoubleOrNull()?.let { NbtDouble.valueOf(it) } ?: NbtString.valueOf(
                        this
                    )
                }

                else -> NbtString.valueOf(this)
            }
        } else if (this.startsWith("[") && this.endsWith("]")) {
            val list = this.substring(1, this.lastIndex).split(",").map { it.cast() }
            return when {
                list.isEmpty() -> NbtString.valueOf(this)
                list[0] is Byte -> NbtByteArray(list as List<Byte>)
                list[0] is Int -> NbtIntArray(list as List<Int>)
                list[0] is Long -> NbtLongArray(list as List<Long>)
                else -> NbtString.valueOf(this)
            }
        } else {
            return NbtString.valueOf(this)
        }
    }

    /**
     * 将任意值转换为对应的 NBT
     *
     * @return 转换结果
     */
    @JvmStatic
    fun Any.toNbt(): Nbt<*> {
        return when (this) {
            is Nbt<*> -> this
            is Boolean -> NbtByte.valueOf(this)
            is Byte -> NbtByte.valueOf(this)
            is Short -> NbtShort.valueOf(this)
            is Int -> NbtInt.valueOf(this)
            is Long -> NbtLong.valueOf(this)
            is Float -> NbtFloat.valueOf(this)
            is Double -> NbtDouble.valueOf(this)
            is ByteArray -> NbtByteArray(this)
            is IntArray -> NbtIntArray(this)
            is LongArray -> NbtLongArray(this)
            is String -> this.castToNbt()
            is List<*> -> {
                val list = this.map { it?.cast() ?: it }
                when {
                    list.isEmpty() -> NbtList()
                    list[0] is Byte -> NbtByteArray(list as List<Byte>)
                    list[0] is Int -> NbtIntArray(list as List<Int>)
                    list[0] is Long -> NbtLongArray(list as List<Long>)
                    else -> {
                        val nbtList = NbtList()
                        for (obj in list) {
                            obj?.toNbt()?.let { nbtList.add(it) }
                        }
                        nbtList
                    }
                }
            }

            is Map<*, *> -> this.toNbtCompound()
            is ConfigurationSection -> this.toNbtCompound()
            else -> NbtString.valueOf("nm的你塞了个什么东西进来，我给你一拖鞋")
        }
    }

    /**
     * 将 NbtCompound 转换为 Map, 其中所有值都被转换为 String 的形式, 形似 (Int) 1
     *
     * @return 转换结果
     */
    @JvmStatic
    fun NbtCompound.toStringMap(): HashMap<String, Any> {
        val hashMap = HashMap<String, Any>()
        forEach { (key, value) ->
            hashMap[key] = value.toStringValue()
        }
        return hashMap
    }

    /**
     * 将 Nbt 转换为 String, List<String> 或 Map<String, String> , 形似 (Int) 1
     *
     * @return 转换结果
     */
    @JvmStatic
    fun Nbt<*>.toStringValue(): Any {
        return when (this) {
            is NbtByte -> "(Byte) ${this.asByte}"
            is NbtShort -> "(Short) ${this.asShort}"
            is NbtInt -> "(Int) ${this.asInt}"
            is NbtLong -> "(Long) ${this.asLong}"
            is NbtFloat -> "(Float) ${this.asFloat}"
            is NbtDouble -> "(Double) ${this.asDouble}"
            is NbtString -> this.asString
            is NbtByteArray -> {
                arrayListOf<String>().also { list ->
                    this.asByteArray.forEach { list.add("(Byte) $it") }
                }
            }

            is NbtIntArray -> {
                arrayListOf<String>().also { list ->
                    this.asIntArray.forEach { list.add("(Int) $it") }
                }
            }

            is NbtLongArray -> {
                arrayListOf<String>().also { list ->
                    this.asLongArray.forEach { list.add("(Long) $it") }
                }
            }

            is NbtCompound -> this.toStringMap()
            is NbtList -> this.map { it.toStringValue() }
            else -> this.asString
        }
    }

    /**
     * 将 NbtCompound 转换为 Map
     *
     * @param invalidNBT 不进行转换的顶层 NBT
     * @return 转换结果
     */
    @JvmStatic
    fun NbtCompound.toMap(invalidNBT: List<String>): HashMap<String, Any> {
        val hashMap = HashMap<String, Any>()
        forEach { (key, value) ->
            if (!invalidNBT.contains(key)) {
                hashMap[key] = value.toValue()
            }
        }
        return hashMap
    }

    /**
     * 将 NbtCompound 转换为 Map
     *
     * @return 转换结果
     */
    @JvmStatic
    fun NbtCompound.toMap(): HashMap<String, Any> {
        val hashMap = HashMap<String, Any>()
        forEach { (key, value) ->
            hashMap[key] = value.toValue()
        }
        return hashMap
    }

    /**
     * 将 Nbt 解析为对应的值
     *
     * @return 转换结果
     */
    @JvmStatic
    fun Nbt<*>.toValue(): Any {
        return when (this) {
            is NbtByte -> this.asByte
            is NbtShort -> this.asShort
            is NbtInt -> this.asInt
            is NbtLong -> this.asLong
            is NbtFloat -> this.asFloat
            is NbtDouble -> this.asDouble
            is NbtString -> this.asString
            is NbtByteArray -> this.asByteArray.toList()
            is NbtIntArray -> this.asIntArray.toList()
            is NbtLongArray -> this.asLongArray.toList()
            is NbtCompound -> this.toMap()
            is NbtList -> this.map { it.toValue() }
            else -> this.asString
        }
    }

    /**
     * 获取物品NBT
     *
     * @return 物品NBT
     */
    @JvmStatic
    fun ItemStack.getNbt(): NbtCompound {
        return NbtItemStack(this).getOrCreateTag()
    }

    /**
     * 获取物品NBT(无nbt则返回null)
     *
     * @return 物品NBT
     */
    @JvmStatic
    fun ItemStack?.getNbtOrNull(): NbtCompound? {
        if (this != null && this.type != Material.AIR) {
            return NbtItemStack(this).tag
        }
        return null
    }

    /**
     * 获取物品NBT(无nbt则返回null)
     *
     * @return 物品NBT
     */
    @JvmStatic
    fun ItemStack?.getDirectTag(): NbtComponentLike? {
        if (this != null && this.type != Material.AIR) {
            return NbtItemStack(this).directTag
        }
        return null
    }

    /**
     * 获取NBT物品
     *
     * @return NBT物品
     */
    @JvmStatic
    fun ItemStack.toNbtItemStack(): NbtItemStack {
        return NbtItemStack(this)
    }

    /**
     * 获取物品复制
     *
     * @return 物品复制
     */
    @JvmStatic
    fun ItemStack.copy(): ItemStack {
        return NbtUtils.asCopy(this)
    }

    /**
     * 获取CraftItemStack形式的物品复制
     *
     * @return CraftItemStack形式的物品复制
     */
    @JvmStatic
    fun ItemStack.asBukkitCopy(): ItemStack {
        return NbtUtils.asBukkitCopy(this)
    }

    /**
     * 获取ItemStack形式的物品复制
     *
     * @return ItemStack形式的物品复制
     */
    @JvmStatic
    fun ItemStack.asCraftCopy(): ItemStack {
        return NbtUtils.asCraftCopy(this)
    }

    /**
     * 检测ItemStack是否属于CraftItemStack
     *
     * @return ItemStack是否属于CraftItemStack
     */
    @JvmStatic
    fun ItemStack?.isCraftItem(): Boolean {
        if (this == null) return false
        return NbtUtils.isCraftItemStack(this)
    }

    /**
     * 获取物品显示名, 这个方法的意义在于, 方便让js脚本调用
     *
     * @param itemStack 待操作物品
     * @return 物品显示名
     */
    @JvmStatic
    fun getItemName(itemStack: ItemStack): String {
        return itemStack.getName()
    }

    /**
     * 掉落指定数量NI物品并触发掉落技能及掉落归属
     *
     * @param itemStack 待掉落物品
     * @param amount 掉落数量
     * @param entity 掉落技能触发者
     */
    @JvmStatic
    fun Location.dropNiItems(itemStack: ItemStack, amount: Int?, entity: Entity? = null) {
        // 最大堆叠
        val maxStackSize = itemStack.maxStackSize
        // 整组给予数量
        val repeat = (amount ?: 1) / maxStackSize
        // 单独给予数量
        val leftAmount = (amount ?: 1) % maxStackSize
        // 整组给予
        if (repeat > 0) {
            val cloneItemStack = itemStack.copy()
            cloneItemStack.amount = maxStackSize
            repeat(repeat) {
                this.dropNiItem(cloneItemStack, entity)
            }
        }
        // 单独给予
        if (leftAmount > 0) {
            val itemLeft = itemStack.copy()
            itemLeft.amount = leftAmount
            this.dropNiItem(itemLeft, entity)
        }
    }

    /**
     * 掉落NI物品并触发掉落技能及掉落归属
     *
     * @param itemStack 待掉落物品
     * @param entity 掉落技能触发者
     * @param itemTag 物品NBT, 用于解析NI物品数据
     * @param neigeItems NI物品NBT
     * @return 掉落物品
     */
    @JvmStatic
    fun Location.dropNiItem(
        itemStack: ItemStack,
        entity: Entity? = null,
        itemTag: NbtCompound = itemStack.getNbt(),
        neigeItems: NbtCompound? = itemTag.getCompound("NeigeItems")
    ): CompletableFuture<Item?> {
        // 记录掉落物拥有者
        val owner = neigeItems?.getString("owner")
        // 移除相关nbt, 防止物品无法堆叠
        owner?.let {
            neigeItems.remove("owner")
            itemTag.saveToSafe(itemStack)
        }
        // 记录掉落物拥有者
        val hide = neigeItems?.getBoolean("hide", false)
        return callSyncMethod {
            this.world?.let { world ->
                WorldUtils.dropItem(
                    world, this, itemStack
                ) { item ->
                    // 设置拥有者相关Metadata
                    owner?.let {
                        item.setMetadataEZ("NI-Owner", it)
                    }
                    if (hide == true) {
                        item.addScoreboardTag("NI-Hide")
                    }
                    item.addScoreboardTag("NeigeItems")
                    // 掉落物技能
                    neigeItems?.getString("dropSkill")?.let { dropSkill ->
                        mythicMobsHooker?.castSkill(item, dropSkill, entity)
                    }
                }
            }
        }
    }

    /**
     * 判断ItemStack是否为NI物品并返回NI物品信息
     *
     * @return NI物品信息, 非NI物品返回null
     */
    @JvmStatic
    fun ItemStack?.isNiItem(): ItemInfo? {
        return this.isNiItem(false)
    }

    /**
     * 判断ItemStack是否为NI物品并返回NI物品信息
     *
     * @param parseData 是否将data解析为HashMap
     * @return NI物品信息, 非NI物品返回null
     */
    @JvmStatic
    fun ItemStack?.isNiItem(parseData: Boolean): ItemInfo? {
        if (this != null && this.type != Material.AIR) {
            // 获取物品NBT
            val itemTag = this.getNbtOrNull() ?: return null
            // 如果为非NI物品则终止操作
            val neigeItems = itemTag.getCompound("NeigeItems") ?: return null
            // 获取物品id
            val id = neigeItems.getString("id") ?: return null
            val result = ItemInfo(this, NbtItemStack(this), itemTag, neigeItems, id, null)
            if (parseData) result.data
            return result
        }
        return null
    }

    /**
     * 根据ItemStack获取对应的NI物品ID
     *
     * @return NI物品ID, 非NI物品返回null
     */
    @JvmStatic
    fun ItemStack?.getItemId(): String? {
        val directTag = this.getDirectTag()
        return directTag?.getDeepString("NeigeItems.id")
    }

    /**
     * 根据数量将物品超级加倍, 返回一个列表
     *
     * @param amount 需要的物品数量
     * @return 物品列表
     */
    @JvmStatic
    fun ItemStack.getItems(amount: Int?): ArrayList<ItemStack> {
        val list = ArrayList<ItemStack>()
        val maxStackSize = maxStackSize.coerceAtLeast(1)
        amount?.let {
            val item = this.copy()
            item.amount = maxStackSize
            val leftAmount = amount % maxStackSize
            val repeat = amount / maxStackSize
            repeat(repeat) {
                list.add(item)
            }
            if (leftAmount != 0) {
                val itemLeft = this.copy()
                itemLeft.amount = leftAmount
                list.add(itemLeft)
            }
        } ?: list.add(this)
        return list
    }

    /**
     * 用于兼容旧版本API
     */
    @Deprecated(
        "已弃用", ReplaceWith(
            "loadItems(items, itemInfos, player as? OfflinePlayer, null, null, true)",
            "pers.neige.neigeitems.utils.ItemUtils.loadItems",
            "org.bukkit.OfflinePlayer"
        )
    )
    @JvmStatic
    fun loadItems(
        items: ArrayList<ItemStack>, itemInfos: List<String>, player: Player? = null
    ) {
        loadItems(items, itemInfos, player as? OfflinePlayer, null, null, true)
    }

    /**
     * 用于兼容旧版本API
     */
    @Deprecated(
        "已弃用", ReplaceWith(
            "loadItems(items, itemInfos, player as? OfflinePlayer, cache, sections, true)",
            "pers.neige.neigeitems.utils.ItemUtils.loadItems",
            "org.bukkit.OfflinePlayer"
        )
    )
    @JvmStatic
    fun loadItems(
        items: ArrayList<ItemStack>,
        itemInfos: List<String>,
        player: Player? = null,
        cache: MutableMap<String, String>? = null,
        sections: ConfigurationSection? = null
    ) {
        loadItems(items, itemInfos, player as? OfflinePlayer, cache, sections, true)
    }

    /**
     * 用于兼容旧版本API
     */
    @Deprecated(
        "已弃用", ReplaceWith(
            "loadItems(items, itemInfos, player, cache, sections, true)",
            "pers.neige.neigeitems.utils.ItemUtils.loadItems",
            "org.bukkit.OfflinePlayer"
        )
    )
    @JvmStatic
    fun loadItems(
        items: ArrayList<ItemStack>,
        itemInfos: List<String>,
        player: OfflinePlayer? = null,
        cache: MutableMap<String, String>? = null,
        sections: ConfigurationSection? = null
    ) {
        loadItems(items, itemInfos, player, cache, sections, true)
    }

    /**
     * 根据信息加载物品
     *
     * @param items 用于存储待生成物品
     * @param itemInfos 物品信息
     * @param player 用于解析物品的玩家
     */
    @JvmStatic
    fun loadItems(
        items: ArrayList<ItemStack>, itemInfos: List<String>, player: OfflinePlayer? = null
    ) {
        loadItems(items, itemInfos, player, null, null, true)
    }

    /**
     * 根据信息加载物品
     *
     * @param items 用于存储待生成物品
     * @param itemInfos 物品信息
     * @param player 用于解析物品的玩家
     * @param cache 随机节点缓存
     * @param sections 随机节点
     * @param parse 是否解析其中的节点
     */
    @JvmStatic
    fun loadItems(
        items: ArrayList<ItemStack>,
        itemInfos: List<String>,
        player: OfflinePlayer? = null,
        cache: MutableMap<String, String>? = null,
        sections: ConfigurationSection? = null,
        parse: Boolean
    ) {
        for (rawInfo in itemInfos) {
            // 先解析, 解析完根据换行符分割, 分割完遍历随机
            val infos = if (parse) {
                rawInfo.parseSection(cache, player, sections).split("\n")
            } else {
                rawInfo.split("\n")
            }
            for (info in infos) {
                loadItems(items, info, player)
            }
        }
    }

    /**
     * 根据信息加载物品
     *
     * @param itemInfos 物品信息
     * @param player 用于解析物品的玩家
     * @return 物品
     */
    @JvmStatic
    fun loadItems(
        itemInfos: List<String>, player: OfflinePlayer? = null
    ): ArrayList<ItemStack> {
        return ArrayList<ItemStack>().also {
            loadItems(it, itemInfos, player, null, null, false)
        }
    }

    /**
     * 根据信息加载物品
     *
     * @param info 物品信息
     * @param player 用于解析物品的玩家
     * @return 物品
     */
    @JvmStatic
    fun loadItems(
        info: String, player: OfflinePlayer? = null
    ): ArrayList<ItemStack> {
        return ArrayList<ItemStack>().also {
            loadItems(it, info, player)
        }
    }

    /**
     * 根据信息加载物品
     *
     * @param items 用于存储待生成物品
     * @param info 物品信息
     * @param player 用于解析物品的玩家
     */
    @JvmStatic
    fun loadItems(
        items: ArrayList<ItemStack>, info: String, player: OfflinePlayer? = null
    ) {
        // [物品ID] (数量(或随机最小数量-随机最大数量)) (生成概率) (是否反复随机) (指向数据)
        val args = info.split(" ", limit = 5)

        // 获取指向数据
        val data: String? = args.getOrNull(4)

        // 获取概率并进行概率随机
        if (args.size > 2) {
            val probability = args[2].toDoubleOrNull()
            if (probability != null && ThreadLocalRandom.current().nextDouble() > probability) return
        }

        // 获取掉落数量
        var amount = 1
        if (args.size > 1) {
            if (args[1].contains("-")) {
                val index = args[1].indexOf("-")
                val min = args[1].substring(0, index).toIntOrNull()
                val max = args[1].substring(index + 1, args[1].length).toIntOrNull()
                if (min != null && max != null) {
                    amount = ThreadLocalRandom.current().nextInt(min, max + 1)
                }
            } else {
                args[1].toIntOrNull()?.let {
                    amount = it
                }
            }
        }
        // 看看需不需要每次都随机生成
        if (args.size > 3 && args[3] == "false") {
            // 真只随机一次啊?那嗯怼吧
            when {
                ItemManager.hasItem(args[0]) -> {
                    ItemManager.getItemStack(args[0], player, data)?.getItems(amount)?.forEach { items.add(it) }
                }

                else -> {
                    getHookedItem(args[0])?.let { itemStack ->
                        repeat(amount) {
                            items.add(itemStack)
                        }
                    }
                }
            }
        } else {
            // 随机生成, 那疯狂造就完事儿了
            when {
                ItemManager.hasItem(args[0]) -> {
                    repeat(amount) {
                        ItemManager.getItemStack(args[0], player, data)?.let { itemStack ->
                            items.add(itemStack)
                        }
                    }
                }

                // 对于其他物品, 这个配置项不代表是否随机生成, 代表物品是否合并
                else -> {
                    getHookedItem(args[0])?.getItems(amount)?.forEach { items.add(it) }
                }
            }
        }
    }

    /**
     * 根据信息进行物品掉落
     *
     * @param dropItems 掉落物列表
     * @param location 掉落位置
     * @param offsetXString 发射横向偏移量
     * @param offsetYString 发射纵向偏移量
     * @param angleType 发射角度类型
     */
    @JvmStatic
    fun dropItems(
        dropItems: List<ItemStack>,
        location: Location,
        entity: Entity? = null,
        offsetXString: String? = null,
        offsetYString: String? = null,
        angleType: String? = null
    ) {
        // 如果配置了多彩掉落信息
        if (offsetXString != null && offsetYString != null && angleType != null) {
            val offsetX: Double = if (offsetXString.contains("-")) {
                val index = offsetXString.indexOf("-")
                val min = offsetXString.substring(0, index).toDoubleOrNull()
                val max = offsetXString.substring(index + 1).toDoubleOrNull()
                when {
                    min != null && max != null -> ThreadLocalRandom.current().nextDouble(min, max)
                    else -> 0.1
                }
            } else {
                offsetXString.toDoubleOrNull() ?: 0.1
            }
            // 获取纵向偏移量
            val offsetY: Double = if (offsetYString.contains("-")) {
                val index = offsetYString.indexOf("-")
                val min = offsetYString.substring(0, index).toDoubleOrNull()
                val max = offsetYString.substring(index + 1).toDoubleOrNull()
                when {
                    min != null && max != null -> ThreadLocalRandom.current().nextDouble(min, max)
                    else -> 0.1
                }
            } else {
                offsetYString.toDoubleOrNull() ?: 0.1
            }
            // 开始掉落
            dropItems.forEachIndexed { index, itemStack ->
                val vector = Vector(offsetX, offsetY, 0.0)
                if (angleType == "random") {
                    val angleCos = cos(Math.PI * 2 * ThreadLocalRandom.current().nextDouble())
                    val angleSin = sin(Math.PI * 2 * ThreadLocalRandom.current().nextDouble())
                    val x = angleCos * vector.x + angleSin * vector.z
                    val z = -angleSin * vector.x + angleCos * vector.z
                    vector.setX(x).z = z
                } else if (angleType == "round") {
                    val angleCos = cos(Math.PI * 2 * index / dropItems.size)
                    val angleSin = sin(Math.PI * 2 * index / dropItems.size)
                    val x = angleCos * vector.x + angleSin * vector.z
                    val z = -angleSin * vector.x + angleCos * vector.z
                    vector.setX(x).z = z
                }
                location.dropNiItem(itemStack, entity, itemStack.getNbt()).thenAccept { item ->
                    item ?: return@thenAccept
                    item.velocity = vector
                }
            }
        } else {
            // 普通掉落
            for (itemStack in dropItems) {
                location.dropNiItem(itemStack, entity, itemStack.getNbt())
            }
        }
    }

    /**
     * 计算钓鱼产物到钓鱼者之间的需要产生的向量
     *
     * @param caughtLocation 渔获坐标
     * @param playerLocation 玩家坐标
     * @return 渔获->玩家 的向量
     */
    @JvmStatic
    fun getCaughtVelocity(caughtLocation: Location, playerLocation: Location): Vector {
        val d0 = playerLocation.x - caughtLocation.x
        val d1 = playerLocation.y - caughtLocation.y
        val d2 = playerLocation.z - caughtLocation.z

        return Vector(d0 * 0.1, d1 * 0.1 + sqrt(sqrt(d0 * d0 + d1 * d1 + d2 * d2)) * 0.08, d2 * 0.1)
    }

    /**
     * 向NbtCompound中插入一个值(key默认以.作分隔, 默认以\转义, 如果key可以转换成Int, 就认为你当前层级是一个NbtList, 而非NbtCompound).
     * 我向前追溯500年, 向后预测500年, 也找不出比这还烂的代码了, 建议不要细看, 能用就行, 避免脏了眼睛.
     *
     * @param key NbtCompound键
     * @param value Nbt值
     * @param escape 转义符
     * @param separator 分隔符
     */
    @JvmStatic
    fun NbtCompound.putDeepWithList(
        key: String, value: Nbt<*>, escape: Char = '\\', separator: Char = '.'
    ) {
        // 父级NbtCompound
        var father: Nbt<*> = this
        // 当前Nbt的Id
        var tempId = ""
        // 当前Nbt
        var temp: Nbt<*> = this
        // 待获取NbtCompound键
        val args = key.split(separator, escape)

        // 逐层深入
        for (index in 0 until (args.size - 1)) {
            // 获取下一级Id
            val node = args[index]
            val nodeIndex = node.toIntOrNull()
            // 我姑且认为没炸
            var boom = false
            let {
                if (nodeIndex == null) return@let
                // 判断当前Nbt类型
                when (temp) {
                    // 是LIST
                    is NbtList -> {
                        val originFather = father
                        // 记录父级NbtList
                        father = temp
                        // 获取下一级
                        val content = (temp as NbtList).getOrNull(nodeIndex)
                        // 如果下一级有东西
                        if (content != null) {
                            // 皆大欢喜
                            temp = content
                            // 如果下一级没东西
                        } else {
                            // 如果刚好是比list的大小多一个
                            if (nodeIndex == (temp as NbtList).size) {
                                // 创建一个新的NbtCompound
                                val newItemTag = NbtCompound()
                                // 丢进去
                                (temp as NbtList).add(newItemTag)
                                // 记录一下
                                temp = newItemTag
                                // 如果现在这个index很离谱
                            } else {
                                // 你妈, 爬(变成NbtCompound)
                                boom = true
                                father = originFather
                                return@let
                            }
                        }
                        // 记录当前Nbt的Id
                        tempId = node
                    }
                    // 其他情况
                    else -> {
                        // 其他情况说明需要重新创建一个NbtList, 所以nodeIndex必须为0
                        if (nodeIndex == 0) {
                            // 新建一个NbtCompound
                            val fatherItemTagList = NbtList()
                            // 覆盖上一层
                            (father as NbtCompound)[tempId] = fatherItemTagList
                            // 新建当前Nbt
                            val tempItemTag = NbtCompound()
                            // 建立下一级Nbt
                            fatherItemTagList.add(tempItemTag)
                            // 记录父级NbtCompound
                            father = fatherItemTagList
                            // 记录当前Nbt
                            temp = tempItemTag
                            // 记录当前Nbt的Id
                            tempId = node
                        } else {
                            // 你给我爬(变成NbtCompound)
                            boom = true
                            return@let
                        }
                    }
                }
            }
            // 如果当前的键不是数字, 或者是数字但是不对劲炸了
            if (nodeIndex == null || boom) {
                // 判断当前Nbt类型
                when (temp) {
                    // 是COMPOUND
                    is NbtCompound -> {
                        // 记录父级NbtCompound
                        father = temp
                        // 获取下一级, 如果下一级是空的就创建一个新NbtCompound()丢进去
                        temp = (temp as NbtCompound).computeIfAbsent(node) {
                            NbtCompound()
                        }
                        // 记录当前Nbt的Id
                        tempId = node
                    }
                    // 其他情况
                    else -> {
                        // 新建一个NbtCompound
                        val fatherItemTag = NbtCompound()
                        // 覆盖上一层
                        (father as NbtCompound)[tempId] = fatherItemTag
                        // 新建当前Nbt
                        val tempItemTag = NbtCompound()
                        // 建立下一级Nbt
                        fatherItemTag[node] = tempItemTag
                        // 记录父级NbtCompound
                        father = fatherItemTag
                        // 记录当前Nbt
                        temp = tempItemTag
                        // 记录当前Nbt的Id
                        tempId = node
                    }
                }
            }
        }

        // 已达末级
        val node = args[args.lastIndex]
        val nodeIndex = node.toIntOrNull()
        var boom = false
        // 我姑且认为没炸
        let {
            if (nodeIndex == null) return@let
            // ByteArray插入
            if (temp is NbtByteArray && value is NbtByte) {
                val byteArray = (temp as NbtByteArray).asByteArray
                // 检测是否越界
                if (nodeIndex >= 0 && nodeIndex < byteArray.size) {
                    byteArray[nodeIndex] = value.asByte
                    // 刚好大一个
                } else if (nodeIndex == byteArray.size) {
                    // 复制扩容
                    val newArray = byteArray.copyOf(byteArray.size + 1)
                    newArray[nodeIndex] = value.asByte
                    // 覆盖上一层
                    when (father) {
                        is NbtList -> {
                            (father as NbtList)[tempId.toInt()] = NbtByteArray(
                                newArray
                            )
                        }

                        else -> {
                            (father as NbtCompound)[tempId] = NbtByteArray(
                                newArray
                            )
                        }
                    }
                    // 越界了, 爬
                } else {
                    // 你给我爬(变成ItemTag)
                    boom = true
                    return@let
                }
                // IntArray插入
            } else if (temp is NbtIntArray && value is NbtInt) {
                val intArray = (temp as NbtIntArray).asIntArray
                // 检测是否越界
                if (nodeIndex >= 0 && nodeIndex < intArray.size) {
                    intArray[nodeIndex] = value.asInt
                    // 刚好大一个
                } else if (nodeIndex == intArray.size) {
                    // 复制扩容
                    val newArray = intArray.copyOf(intArray.size + 1)
                    newArray[nodeIndex] = value.asInt
                    // 覆盖上一层
                    when (father) {
                        is NbtList -> {
                            (father as NbtList)[tempId.toInt()] = NbtIntArray(
                                newArray
                            )
                        }

                        else -> {
                            (father as NbtCompound)[tempId] = NbtIntArray(
                                newArray
                            )
                        }
                    }
                    // 越界了, 爬
                } else {
                    // 你给我爬(变成NbtCompound)
                    boom = true
                    return@let
                }
                // LongArray插入
            } else if (temp is NbtLongArray && value is NbtLong) {
                val longArray = (temp as NbtLongArray).asLongArray
                // 检测是否越界
                if (nodeIndex >= 0 && nodeIndex < longArray.size) {
                    longArray[nodeIndex] = value.asLong
                    // 刚好大一个
                } else if (nodeIndex == longArray.size) {
                    // 复制扩容
                    val newArray = longArray.copyOf(longArray.size + 1)
                    newArray[nodeIndex] = value.asLong
                    // 覆盖上一层
                    when (father) {
                        is NbtList -> {
                            (father as NbtList)[tempId.toInt()] = NbtLongArray(
                                newArray
                            )
                        }

                        else -> {
                            (father as NbtCompound)[tempId] = NbtLongArray(
                                newArray
                            )
                        }
                    }
                    // 越界了, 爬
                } else {
                    // 你给我爬(变成NbtCompound)
                    boom = true
                    return@let
                }
                // List插入
            } else if (temp is NbtList) {
                val list = temp as NbtList
                // 检测是否越界
                if (nodeIndex >= 0 && nodeIndex < list.size) {
                    list[nodeIndex] = value
                    // 刚好大一个, 直接add
                } else if (nodeIndex == list.size) {
                    list.add(value)
                    // 越界了, 爬
                } else {
                    // 你给我爬(变成NbtCompound)
                    boom = true
                    return@let
                }
            }
        }
        // 如果当前的键不是数字, 或者是数字但是不对劲炸了
        if (nodeIndex == null || boom) {
            if (temp is NbtCompound) {
                // 东西丢进去
                (temp as NbtCompound)[node] = value
                // 如果当前Nbt是其他类型
            } else {
                // 新建一个NbtCompound
                val newItemTag = NbtCompound()
                // 东西丢进去
                newItemTag[node] = value
                // 覆盖上一层
                (father as NbtCompound)[tempId] = newItemTag
            }
        }
    }

    /**
     * 如果物品不是CraftItemStack则将NBT应用到物品上.
     *
     * @param itemStack 待应用NBT的物品
     */
    @JvmStatic
    fun NbtCompound.saveToSafe(itemStack: ItemStack) {
        if (itemStack.type != Material.AIR && itemStack.amount != 0 && !NbtUtils.isCraftItemStack(itemStack)) {
            saveTo(itemStack)
        }
    }

    /**
     * 移除物品上用于记录NI物品拥有者的NBT.
     */
    @JvmStatic
    fun ItemStack?.removeOwnerNbt() {
        val nbt = this.getDirectTag()
        nbt?.getCompound("NeigeItems")?.remove("owner")
    }

    /**
     * 设置物品损伤值, 在1.13到1.20.4版本中拥有更佳的性能表现.
     *
     * @param damage 损伤值
     */
    @JvmStatic
    fun ItemStack?.setDamage(damage: Short) {
        if (this == null || this.type == Material.AIR) return
        if (GET_DAMAGE_FROM_ITEM_STACK) {
            this.durability = damage
        } else {
            val nbt = NbtItemStack(this).orCreateTag
            nbt.putInt(NbtUtils.getDamageNbtKeyOrThrow(), damage.toInt())
            nbt.saveToSafe(this)
        }
    }

    /**
     * 获取物品损伤值, 在1.13到1.20.4版本中拥有更佳的性能表现.
     *
     * @return 物品损伤值
     */
    @JvmStatic
    fun ItemStack?.getDamage(): Short {
        if (this == null || this.type == Material.AIR) return -1
        if (GET_DAMAGE_FROM_ITEM_STACK) {
            return this.durability
        } else {
            val nbt = NbtItemStack(this).tag ?: return -1
            return nbt.getShort(NbtUtils.getDamageNbtKeyOrThrow(), -1)
        }
    }
}