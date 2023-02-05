package pers.neige.neigeitems.utils

import com.alibaba.fastjson2.parseObject
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Entity
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import pers.neige.neigeitems.NeigeItems.bukkitScheduler
import pers.neige.neigeitems.NeigeItems.plugin
import pers.neige.neigeitems.item.ItemInfo
import pers.neige.neigeitems.manager.HookerManager.mythicMobsHooker
import pers.neige.neigeitems.manager.ItemManager
import pers.neige.neigeitems.utils.PlayerUtils.setMetadataEZ
import pers.neige.neigeitems.utils.SectionUtils.getItemSection
import pers.neige.neigeitems.utils.SectionUtils.parseSection
import pers.neige.neigeitems.utils.StringUtils.split
import taboolib.module.nms.*
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin


/**
 * 物品相关工具类
 */
object ItemUtils {
    val invalidNBT by lazy { arrayListOf("Enchantments","VARIABLES_DATA","ench","Damage","HideFlags","Unbreakable", "CustomModelData") }

    /**
     * HashMap 转 ItemTag
     *
     * @return 转换结果
     */
    @JvmStatic
    fun HashMap<*, *>.toItemTag(): ItemTag {
        val itemTag = ItemTag()
        for ((key, value) in this) {
            itemTag[key as String] = value.toItemTagData()
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
        return when {
            this.startsWith("(Byte) ") -> this.substring(7, this.length).toByteOrNull() ?: this
            this.startsWith("(Short) ") -> this.substring(8, this.length).toShortOrNull() ?: this
            this.startsWith("(Int) ") -> this.substring(6, this.length).toIntOrNull() ?: this
            this.startsWith("(Long) ") -> this.substring(7, this.length).toLongOrNull() ?: this
            this.startsWith("(Float) ") -> this.substring(8, this.length).toFloatOrNull() ?: this
            this.startsWith("(Double) ") -> this.substring(9, this.length).toDoubleOrNull() ?: this
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
    fun String.castToItemTagData(): ItemTagData {
        return when {
            this.startsWith("(Byte) ") -> {
                this.substring(7, this.length).toByteOrNull()?.let { ItemTagData(it) } ?: ItemTagData(this)
            }
            this.startsWith("(Short) ") -> {
                this.substring(8, this.length).toShortOrNull()?.let { ItemTagData(it) } ?: ItemTagData(this)
            }
            this.startsWith("(Int) ") -> {
                this.substring(6, this.length).toIntOrNull()?.let { ItemTagData(it) } ?: ItemTagData(this)
            }
            this.startsWith("(Long) ") -> {
                this.substring(7, this.length).toLongOrNull()?.let { ItemTagData(it) } ?: ItemTagData(this)
            }
            this.startsWith("(Float) ") -> {
                this.substring(8, this.length).toFloatOrNull()?.let { ItemTagData(it) } ?: ItemTagData(this)
            }
            this.startsWith("(Double) ") -> {
                this.substring(9, this.length).toDoubleOrNull()?.let { ItemTagData(it) } ?: ItemTagData(this)
            }
            this.startsWith("[") && this.endsWith("]") -> {
                val list = this.substring(1, this.lastIndex).split(",").map { it.cast() }
                when {
                    list.all { it is Byte } -> ByteArray(list.size){ list[it] as Byte }.toItemTagData()
                    list.all { it is Int } -> IntArray(list.size){ list[it] as Int }.toItemTagData()
                    else -> ItemTagData(this)
                }
            }
            else -> ItemTagData(this)
        }
    }

    /**
     * 转 ItemTagData
     *
     * @return 转换结果
     */
    @JvmStatic
    fun Any.toItemTagData(): ItemTagData {
        return when (this) {
            is ItemTagData -> this
            is Byte -> ItemTagData(this)
            is Short -> ItemTagData(this)
            is Int -> ItemTagData(this)
            is Long -> ItemTagData(this)
            is Float -> ItemTagData(this)
            is Double -> ItemTagData(this)
            is ByteArray -> ItemTagData(this)
            is IntArray -> ItemTagData(this)
            is String -> this.castToItemTagData()
            is List<*> -> {
                val list = this.map { it?.cast() ?: it }
                when {
                    list.all { it is Byte } -> ByteArray(list.size){ list[it] as Byte }.toItemTagData()
                    list.all { it is Int } -> IntArray(list.size){ list[it] as Int }.toItemTagData()
                    else -> {
                        val itemTagList = ItemTagList()
                        for (obj in list) {
                            obj?.toItemTagData()?.let { itemTagList.add(it) }
                        }
                        itemTagList
                    }
                }
            }
            is HashMap<*, *> -> ItemTagData(this.toItemTag())
            else -> ItemTagData("nm的你塞了个什么东西进来，我给你一拖鞋")
        }
    }

    /**
     * ItemTag 转 HashMap
     *
     * @param invalidNBT 转换过程中忽视对应NBT
     * @return 转换结果
     */
    @JvmStatic
    fun ItemTag.toMap(invalidNBT: List<String>): HashMap<String, Any> {
        val hashMap = HashMap<String, Any>()
        for ((key, value) in this) {
            if (!invalidNBT.contains(key)) {
                hashMap[key] = value.parseValue()
            }
        }
        return hashMap
    }

    /**
     * ItemTag 转 HashMap
     *
     * @return 转换结果
     */
    @JvmStatic
    fun ItemTag.toMap(): HashMap<String, Any> {
        val hashMap = HashMap<String, Any>()
        for ((key, value) in this) {
            hashMap[key] = value.parseValue()
        }
        return hashMap
    }

    /**
     * ItemTagData 解析
     *
     * @return 解析结果
     */
    @JvmStatic
    fun ItemTagData.parseValue(): Any {
        return when (this.type) {
            ItemTagType.BYTE -> "(Byte) ${this.asString()}"
            ItemTagType.SHORT ->  "(Short) ${this.asString()}"
            ItemTagType.INT ->  "(Int) ${this.asString()}"
            ItemTagType.LONG ->  "(Long) ${this.asString()}"
            ItemTagType.FLOAT ->  "(Float) ${this.asString()}"
            ItemTagType.DOUBLE ->  "(Double) ${this.asString()}"
            ItemTagType.STRING ->  this.asString()
            ItemTagType.BYTE_ARRAY -> this.asByteArray().toList().map { "(Byte) $it" }
            ItemTagType.INT_ARRAY -> this.asIntArray().toList().map { "(Int) $it" }
            ItemTagType.COMPOUND -> this.asCompound().toMap()
            ItemTagType.LIST -> this.asList().map { it.parseValue() }
            else -> this.asString()
        }
    }

    /**
     * ItemTag 合并(后者覆盖前者)
     *
     * @param itemTag 用于合并覆盖
     * @return 合并结果
     */
    @JvmStatic
    fun ItemTag.coverWith(itemTag: ItemTag): ItemTag {
        // 遍历附加NBT
        for ((key, value) in itemTag) {
            // 如果二者包含相同键
            val overrideValue = this[key]
            if (overrideValue != null) {
                // 如果二者均为COMPOUND
                if (overrideValue.type == ItemTagType.COMPOUND
                    && value.type == ItemTagType.COMPOUND) {
                    // 合并
                    this[key] = overrideValue.asCompound().coverWith(value.asCompound())
                } else {
                    // 覆盖
                    this[key] = value
                }
                // 这个键原NBT里没有
            } else {
                // 添加
                this[key] = value
            }
        }
        return this
    }

    /**
     * 获取物品NBT, 这个方法的意义在于, 方便让js脚本调用
     *
     * @param itemStack 待操作物品
     * @return 物品NBT
     */
    @JvmStatic
    fun getItemTag(itemStack: ItemStack): ItemTag {
        return itemStack.getItemTag()
    }

    /**
     * 获取ItemTag中的值(key以.作分隔, 以\转义), 获取不到返回null
     *
     * @param key ItemTag键
     * @return ItemTag值(获取不到返回null)
     */
    @JvmStatic
    fun ItemTag.getDeepOrNull(key: String): ItemTagData? {
        // 当前层级ItemTagData
        var value: ItemTagData? = this
        // key以.作分隔
        val args = key.split('.', '\\')

        // 逐层深入
        args.forEach { currentKey ->
            // 检测当前ItemTagData类型
            when (value?.type) {
                ItemTagType.LIST -> {
                    // key无法转换为数字/数组越界将返回null, 其他情况下将返回下一级
                    value = currentKey.toIntOrNull()?.coerceAtLeast(0)?.let { index ->
                        val list = value!!.asList()
                        when {
                            list.size > index -> list[index]
                            else -> return null
                        }
                    }
                }
                ItemTagType.BYTE_ARRAY -> {
                    // key无法转换为数字/数组越界将返回null, 其他情况下将返回下一级
                    value = currentKey.toIntOrNull()?.coerceAtLeast(0)?.let { index ->
                        val array = value!!.asByteArray()
                        when {
                            array.size > index -> ItemTagData(array[index])
                            else -> return null
                        }
                    }
                }
                ItemTagType.INT_ARRAY -> {
                    // key无法转换为数字/数组越界将返回null, 其他情况下将返回下一级
                    value = currentKey.toIntOrNull()?.coerceAtLeast(0)?.let { index ->
                        val array = value!!.asIntArray()
                        when {
                            array.size > index -> ItemTagData(array[index])
                            else -> return null
                        }
                    }
                }
                ItemTagType.COMPOUND -> {
                    // 当前Compound不含对应的key将返回null, 其他情况下将返回下一级
                    value = value!!.asCompound().getOrElse(currentKey, null)
                }
                // 可能情况: 数组索引不是数字/数组越界/当前Compound不含对应的key/ItemTag层数不够
                else -> return null
            }
        }

        return value
    }

    /**
     * 向ItemTag中插入一个值(key以.作分隔, 以\转义). 我这代码写得依托答辩, 建议不要用.
     *
     * @param key ItemTag键
     * @param value ItemTag值
     */
    @JvmStatic
    fun ItemTag.putDeepFixed(key: String, value: ItemTagData) {
        // 父级ItemTag
        var father: ItemTagData = this
        // 当前ItemTagData的Id
        var tempId = ""
        // 当前ItemTagData
        var temp: ItemTagData = this
        // 待获取ItemTag键
        val args = key.split('.', '\\')

        // 逐层深入
        for (index in 0 until (args.size - 1)) {
            // 获取下一级Id
            val node = args[index]
            // 判断当前ItemTagData类型
            when (temp.type) {
                // 是COMPOUND
                ItemTagType.COMPOUND -> {
                    // 记录父级ItemTag
                    father = temp
                    // 获取下一级, 如果下一级是空的就创建一个新ItemTag()丢进去
                    temp = temp.asCompound().computeIfAbsent(node) {
                        ItemTag()
                    }
                    // 记录当前ItemTagData的Id
                    tempId = node
                }
                // 其他情况
                else -> {
                    // 新建一个ItemTag
                    val fatherItemTag = ItemTag()
                    // 覆盖上一层
                    father.asCompound()[tempId] = fatherItemTag
                    // 新建当前ItemTagData
                    val tempItemTag = ItemTag()
                    // 建立下一级ItemTagData
                    fatherItemTag[node] = tempItemTag
                    // 记录父级ItemTag
                    father = fatherItemTag
                    // 记录当前ItemTagData
                    temp = tempItemTag
                    // 记录当前ItemTagData的Id
                    tempId = node
                }
            }
        }

        // 已达末级
        val node = args[args.lastIndex]
        if (temp.type == ItemTagType.COMPOUND) {
            // 东西丢进去
            temp.asCompound()[node] = value
            // 如果当前ItemTagData是其他类型
        } else {
            // 新建一个ItemTag
            val newItemTag = ItemTag()
            // 东西丢进去
            newItemTag[node] = value
            // 覆盖上一层
            father.asCompound()[tempId] = newItemTag
        }
    }

    /**
     * 向ItemTag中插入一个值(key以.作分隔, 以\转义, 如果key可以转换成Int, 就认为你当前层级是一个ItemTagList, 而非ItemTag). 我这代码写得依托答辩, 建议不要用.
     *
     * @param key ItemTag键
     * @param value ItemTag值
     */
    @JvmStatic
    fun ItemTag.putDeepWithList(key: String, value: ItemTagData) {
        // 父级ItemTag
        var father: ItemTagData = this
        // 当前ItemTagData的Id
        var tempId = ""
        // 当前ItemTagData
        var temp: ItemTagData = this
        // 待获取ItemTag键
        val args = key.split('.', '\\')

        // 逐层深入
        for (index in 0 until (args.size - 1)) {
            // 获取下一级Id
            val node = args[index]
            val nodeIndex = node.toIntOrNull()
            // 我姑且认为没炸
            var boom = false
            let {
                if (nodeIndex != null) {
                    // 判断当前ItemTagData类型
                    when (temp.type) {
                        // 是LIST
                        ItemTagType.LIST -> {
                            val originFather = father
                            // 记录父级ItemTagList
                            father = temp
                            // 获取下一级
                            val content = temp.asList().getOrNull(nodeIndex)
                            // 如果下一级有东西
                            if (content != null) {
                                // 皆大欢喜
                                temp = content
                                // 如果下一级没东西
                            } else {
                                // 如果刚好是比list的大小多一个
                                if (nodeIndex == temp.asList().size) {
                                    // 创建一个新的ItemTag
                                    val newItemTag = ItemTag()
                                    // 丢进去
                                    temp.asList().add(newItemTag)
                                    // 记录一下
                                    temp = newItemTag
                                    // 如果现在这个index很离谱
                                } else {
                                    // 你妈, 爬(变成ItemTag)
                                    boom = true
                                    father = originFather
                                    return@let
                                }
                            }
                            // 记录当前ItemTagData的Id
                            tempId = node
                        }
                        // 其他情况
                        else -> {
                            // 其他情况说明需要重新创建一个ItemTagList, 所以nodeIndex必须为0
                            if (nodeIndex == 0) {
                                // 新建一个ItemTag
                                val fatherItemTagList = ItemTagList()
                                // 覆盖上一层
                                father.asCompound()[tempId] = fatherItemTagList
                                // 新建当前ItemTagData
                                val tempItemTag = ItemTag()
                                // 建立下一级ItemTagData
                                fatherItemTagList.add(tempItemTag)
                                // 记录父级ItemTag
                                father = fatherItemTagList
                                // 记录当前ItemTagData
                                temp = tempItemTag
                                // 记录当前ItemTagData的Id
                                tempId = node
                            } else {
                                // 你给我爬(变成ItemTag)
                                boom = true
                                return@let
                            }
                        }
                    }
                }
            }
            // 如果当前的键不是数字, 或者是数字但是不对劲炸了
            if (nodeIndex == null || boom) {
                // 判断当前ItemTagData类型
                when (temp.type) {
                    // 是COMPOUND
                    ItemTagType.COMPOUND -> {
                        // 记录父级ItemTag
                        father = temp
                        // 获取下一级, 如果下一级是空的就创建一个新ItemTag()丢进去
                        temp = temp.asCompound().computeIfAbsent(node) {
                            ItemTag()
                        }
                        // 记录当前ItemTagData的Id
                        tempId = node
                    }
                    // 其他情况
                    else -> {
                        // 新建一个ItemTag
                        val fatherItemTag = ItemTag()
                        // 覆盖上一层
                        father.asCompound()[tempId] = fatherItemTag
                        // 新建当前ItemTagData
                        val tempItemTag = ItemTag()
                        // 建立下一级ItemTagData
                        fatherItemTag[node] = tempItemTag
                        // 记录父级ItemTag
                        father = fatherItemTag
                        // 记录当前ItemTagData
                        temp = tempItemTag
                        // 记录当前ItemTagData的Id
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
            if (nodeIndex != null) {
                // ByteArray插入
                if (temp.type == ItemTagType.BYTE_ARRAY && value.type == ItemTagType.BYTE) {
                    val byteArray = temp.asByteArray()
                    // 检测是否越界
                    if (nodeIndex >= 0 && nodeIndex < byteArray.size) {
                        byteArray[nodeIndex] = value.asByte()
                        // 刚好大一个
                    } else if (nodeIndex == byteArray.size) {
                        // 复制扩容
                        val newArray = byteArray.copyOf(byteArray.size+1)
                        newArray[nodeIndex] = value.asByte()
                        // 覆盖上一层
                        when (father.type) {
                            ItemTagType.LIST -> {
                                father.asList()[tempId.toInt()] = ItemTagData(newArray)
                            }
                            else -> {
                                father.asCompound()[tempId] = ItemTagData(newArray)
                            }
                        }
                        // 越界了, 爬
                    } else {
                        // 你给我爬(变成ItemTag)
                        boom = true
                        return@let
                    }
                // IntArray插入
                } else if (temp.type == ItemTagType.INT_ARRAY && value.type == ItemTagType.INT) {
                    val intArray = temp.asIntArray()
                    // 检测是否越界
                    if (nodeIndex >= 0 && nodeIndex < intArray.size) {
                        intArray[nodeIndex] = value.asInt()
                        // 刚好大一个
                    } else if (nodeIndex == intArray.size) {
                        // 复制扩容
                        val newArray = intArray.copyOf(intArray.size+1)
                        newArray[nodeIndex] = value.asInt()
                        // 覆盖上一层
                        when (father.type) {
                            ItemTagType.LIST -> {
                                father.asList()[tempId.toInt()] = ItemTagData(newArray)
                            }
                            else -> {
                                father.asCompound()[tempId] = ItemTagData(newArray)
                            }
                        }
                        // 越界了, 爬
                    }  else {
                        // 你给我爬(变成ItemTag)
                        boom = true
                        return@let
                    }
                // List插入
                } else if (temp.type == ItemTagType.LIST) {
                    val list = temp.asList()
                    // 检测是否越界
                    if (nodeIndex >= 0 && nodeIndex < list.size) {
                        list[nodeIndex] = value
                        // 刚好大一个, 直接add
                    } else if (nodeIndex == list.size) {
                        list.add(value)
                        // 越界了, 爬
                    } else {
                        // 你给我爬(变成ItemTag)
                        boom = true
                        return@let
                    }
                }
            }
        }
        // 如果当前的键不是数字, 或者是数字但是不对劲炸了
        if (nodeIndex == null || boom) {
            if (temp.type == ItemTagType.COMPOUND) {
                // 东西丢进去
                temp.asCompound()[node] = value
            // 如果当前ItemTagData是其他类型
            } else {
                // 新建一个ItemTag
                val newItemTag = ItemTag()
                // 东西丢进去
                newItemTag[node] = value
                // 覆盖上一层
                father.asCompound()[tempId] = newItemTag
            }
        }
    }

    /**
     * 掉落指定数量NI物品并触发掉落技能及掉落归属
     *
     * @param itemStack 待掉落物品
     */
    @JvmStatic
    fun Location.dropNiItems(itemStack: ItemStack, entity: Entity? = null) {
        this.dropNiItems(itemStack, null, entity)
    }

    /**
     * 掉落指定数量NI物品并触发掉落技能及掉落归属
     *
     * @param itemStack 待掉落物品
     * @param amount 掉落数量
     */
    @JvmStatic
    fun Location.dropNiItems(itemStack: ItemStack, amount: Int?, entity: Entity? = null) {
        itemStack.getItems(amount).forEach { item -> this.dropNiItem(item, entity) }
    }

    /**
     * 掉落NI物品并触发掉落技能及掉落归属
     *
     * @param itemStack 待掉落物品
     * @param itemTag 物品NBT, 用于解析NI物品数据
     * @param neigeItems NI物品NBT
     * @return 掉落物品
     */
    @JvmStatic
    fun Location.dropNiItem(
        itemStack: ItemStack,
        entity: Entity? = null,
        itemTag: ItemTag = itemStack.getItemTag(),
        neigeItems: ItemTag? = itemTag["NeigeItems"]?.asCompound()
    ): Item? {
        // 记录掉落物拥有者
        val owner = neigeItems?.get("owner")?.asString()
        // 移除相关nbt, 防止物品无法堆叠
        owner?.let {
            neigeItems.remove("owner")
            itemTag.saveTo(itemStack)
        }
        // 返回结果物品
        return bukkitScheduler.callSyncMethod(plugin) {
            // 掉落物品
            val item = this.world?.dropItem(this, itemStack)
            // 设置拥有者相关Metadata
            owner?.let {
                item?.setMetadataEZ("NI-Owner", it)
            }
            // 返回结果物品
            item
        }.get()?.let { item ->
            // 掉落物技能
            neigeItems?.let {
                neigeItems["dropSkill"]?.asString()?.let { dropSkill ->
                    mythicMobsHooker?.castSkill(item, dropSkill, entity)
                }
            }
            item
        }
    }

    /**
     * 判断ItemStack是否为NI物品并返回NI物品信息
     *
     * @return NI物品信息?
     */
    @JvmStatic
    fun ItemStack.isNiItem(): ItemInfo? {
        return this.isNiItem(false)
    }

    /**
     * 判断ItemStack是否为NI物品并返回NI物品信息
     *
     * @param parseData 是否将data解析为HashMap
     * @return NI物品信息?
     */
    @JvmStatic
    fun ItemStack.isNiItem(parseData: Boolean): ItemInfo? {
        if (this.type != Material.AIR) {
            // 获取物品NBT
            val itemTag = this.getItemTag()
            // 如果为非NI物品则终止操作
            val neigeItems = itemTag["NeigeItems"]?.asCompound() ?: let { return null }
            // 获取物品id
            val id = neigeItems["id"]?.asString() ?: let { return null }
            val data = when {
                parseData -> neigeItems["data"]?.asString()?.parseObject<HashMap<String, String>>()
                else -> null
            }
            return ItemInfo(itemTag, neigeItems, id, data)
        }
        return null
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
        amount?.let {
            val item = this.clone()
            val maxStackSize = item.maxStackSize
            item.amount = maxStackSize
            val leftAmount = amount % maxStackSize
            val repeat = amount / maxStackSize
            repeat(repeat) {
                list.add(item)
            }
            if (leftAmount != 0) {
                val itemLeft = this.clone()
                itemLeft.amount = leftAmount
                list.add(itemLeft)
            }
        } ?: list.add(this)
        return list
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
        items: ArrayList<ItemStack>,
        itemInfos: List<String>,
        player: Player? = null
    ) {
        loadItems(items, itemInfos, player, null, null)
    }

    /**
     * 根据信息加载物品
     *
     * @param items 用于存储待生成物品
     * @param itemInfos 物品信息
     * @param player 用于解析物品的玩家
     * @param cache 随机节点缓存
     * @param sections 随机节点
     */
    @JvmStatic
    fun loadItems(
        items: ArrayList<ItemStack>,
        itemInfos: List<String>,
        player: Player? = null,
        cache: HashMap<String, String>? = null,
        sections: ConfigurationSection? = null
    ) {
        for (rawInfo in itemInfos) {
            // 先解析, 解析完根据换行符分割, 分割完遍历随机
            val infos = rawInfo.parseSection(cache, player, sections).split("\n")
            for (info in infos) {
                val args = info.split(" ")

                val data: String? = when {
                    args.size > 4 -> args.subList(4, args.size).joinToString(" ")
                    else -> null
                }

                // 获取概率并进行概率随机
                if (args.size > 2) {
                    val probability = args[2].toDoubleOrNull()
                    if (probability != null && Math.random() > probability) continue
                }
                // 如果NI和MM都不存在对应物品就跳过去
                if (!ItemManager.hasItem(args[0]) && mythicMobsHooker?.getItemStackSync(args[0]) == null) continue

                // 获取掉落数量
                var amount = 1
                if (args.size > 1) {
                    if (args[1].contains("-")) {
                        val index = args[1].indexOf("-")
                        val min = args[1].substring(0, index).toIntOrNull()
                        val max = args[1].substring(index+1, args[1].length).toIntOrNull()
                        if (min != null && max != null) {
                            amount = min + (Math.random() * (max - min)).roundToInt()
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
                    ItemManager.getItemStack(args[0], player, data)?.getItems(amount)?.forEach { items.add(it) } ?: let {
                        mythicMobsHooker?.getItemStackSync(args[0])?.let { itemStack ->
                            repeat(amount) {
                                items.add(itemStack)
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
                        // 对于MM物品, 这个配置项不代表是否随机生成, 代表物品是否合并
                        else -> {
                            mythicMobsHooker?.getItemStackSync(args[0])?.getItems(amount)?.forEach { items.add(it) }
                        }
                    }
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
                val max = offsetXString.substring(index+1).toDoubleOrNull()
                when {
                    min != null && max != null -> min + Math.random()*(max-min)
                    else -> 0.1
                }
            } else {
                offsetXString.toDoubleOrNull() ?: 0.1
            }
            // 获取纵向偏移量
            val offsetY: Double = if (offsetYString.contains("-")) {
                val index = offsetYString.indexOf("-")
                val min = offsetYString.substring(0, index).toDoubleOrNull()
                val max = offsetYString.substring(index+1).toDoubleOrNull()
                when {
                    min != null && max != null -> min + Math.random()*(max-min)
                    else -> 0.1
                }
            } else {
                offsetYString.toDoubleOrNull() ?: 0.1
            }
            // 开始掉落
            for ((index, itemStack) in dropItems.withIndex()) {
                val itemTag = itemStack.getItemTag()
                location.dropNiItem(itemStack, entity, itemTag)?.let { item ->
                    val vector = Vector(offsetX, offsetY, 0.0)
                    if (angleType == "random") {
                        val angleCos = cos(Math.PI * 2 * Math.random())
                        val angleSin = sin(Math.PI * 2 * Math.random())
                        val x = angleCos * vector.x + angleSin * vector.z
                        val z = -angleSin * vector.x + angleCos * vector.z
                        vector.setX(x).z = z
                    } else if (angleType == "round") {
                        val angleCos = cos(Math.PI * 2 * index/dropItems.size)
                        val angleSin = sin(Math.PI * 2 * index/dropItems.size)
                        val x = angleCos * vector.x + angleSin * vector.z
                        val z = -angleSin * vector.x + angleCos * vector.z
                        vector.setX(x).z = z
                    }
                    item.velocity = vector
                }
            }
        } else {
            // 普通掉落
            for (itemStack in dropItems) {
                val itemTag = itemStack.getItemTag()
                location.dropNiItem(itemStack, entity, itemTag)
            }
        }
    }
}