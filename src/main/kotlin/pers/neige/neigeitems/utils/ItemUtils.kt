package pers.neige.neigeitems.utils

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import pers.neige.neigeitems.NeigeItems
import pers.neige.neigeitems.NeigeItems.bukkitScheduler
import pers.neige.neigeitems.NeigeItems.plugin
import pers.neige.neigeitems.NeigeItems.pluginManager
import pers.neige.neigeitems.item.ItemInfo
import pers.neige.neigeitems.manager.HookerManager.mythicMobsHooker
import pers.neige.neigeitems.manager.ItemManager
import pers.neige.neigeitems.utils.ItemUtils.getItems
import pers.neige.neigeitems.utils.PlayerUtils.setMetadataEZ
import pers.neige.neigeitems.utils.SectionUtils.parseSection
import taboolib.module.nms.*
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin


object ItemUtils {
    private val invalidNBT by lazy { arrayListOf("Enchantments","VARIABLES_DATA","ench","Damage","HideFlags","Unbreakable", "CustomModelData") }

    // HashMap 转 ItemTag
    @JvmStatic
    fun HashMap<*, *>.toItemTag(): ItemTag {
        val itemTag = ItemTag()
        for ((key, value) in this) {
            itemTag[key as String] = value.toItemTagData()
        }
        return itemTag
    }

    // 类型强制转换
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

    // 类型强制转换
    @JvmStatic
    fun Any.cast(): Any {
        return when (this) {
            is String -> this.cast()
            else -> this
        }
    }

    // 类型强制转换
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
            else -> ItemTagData(this)
        }
    }

    // 转 ItemTagData
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

    // ItemTag 转 HashMap
    @JvmStatic
    fun ItemTag.toMap(): HashMap<String, Any> {
        val hashMap = HashMap<String, Any>()
        for ((key, value) in this) {
            if (!invalidNBT.contains(key)) {
                hashMap[key] = value.parseValue()
            }
        }
        return hashMap
    }

    // ItemTagData 解析
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

    // ItemTag 合并(后者覆盖前者)
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
            } else {
                // 添加
                this[key] = value
            }
        }
        return this
    }


    // 掉落指定数量NI物品并触发掉落技能及掉落归属
    @JvmStatic
    fun Location.dropNiItems(itemStack: ItemStack, amount: Int? = null) {
        amount?.let {
            val maxStackSize = itemStack.maxStackSize
            itemStack.amount = maxStackSize
            val leftAmount = amount % maxStackSize
            val repeat = floor((amount / maxStackSize).toDouble()).toInt()
            repeat(repeat) {
                this.dropNiItem(itemStack)
            }
            if (leftAmount != 0) {
                itemStack.amount = leftAmount
                this.dropNiItem(itemStack)
            }
        } ?: this.dropNiItem(itemStack)
    }

    // 掉落NI物品并触发掉落技能及掉落归属
    @JvmStatic
    fun Location.dropNiItem(itemStack: ItemStack) {
        val itemTag = itemStack.getItemTag()
        val neigeItems = itemTag["NeigeItems"]?.asCompound()
        bukkitScheduler.callSyncMethod(plugin) {
            val item = this.world?.dropItem(this, itemStack)
            neigeItems?.let {
                neigeItems["owner"]?.asString()?.let { owner ->
                    item?.setMetadataEZ("NI-Owner", owner)
                }
            }
            item
        }.get()?.let { item ->
            neigeItems?.let {
                neigeItems["dropSkill"]?.asString()?.let { dropSkill ->
                    mythicMobsHooker?.castSkill(item, dropSkill)
                }
            }
        }
    }

    // 判断ItemStack是否为NI物品并返回NI物品信息
    @JvmStatic
    fun ItemStack.isNiItem(): ItemInfo? {
        if (this.type != Material.AIR) {
            // 获取物品NBT
            val itemTag = this.getItemTag()
            // 如果为非NI物品则终止操作
            val neigeItems = itemTag["NeigeItems"]?.asCompound() ?: let { return null }
            // 获取物品id
            val id = neigeItems["id"]?.asString() ?: let { return null }
            return ItemInfo(itemTag, neigeItems, id)
        }
        return null
    }

    // 根据数量将物品超级加倍, 返回一个列表
    @JvmStatic
    fun ItemStack.getItems(amount: Int? = null): ArrayList<ItemStack> {
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
     * 根据掉落信息加载掉落物品
     * @param dropItems 用于存储待掉落物品
     * @param drops 掉落信息
     * @param player 用于解析物品的玩家
     */
    @JvmStatic
    fun loadDrops(
        dropItems: ArrayList<ItemStack>,
        drops: List<String>,
        player: Player? = null
    ) {
        for (drop in drops) {
            val args = drop.parseSection(player).split(" ")

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
            if (!ItemManager.hasItem(args[0]) && mythicMobsHooker?.getItemStackSync(args[0]) != null) continue

            // 获取掉落数量
            var amount = 1
            if (args.size > 1) {
                if (args[1].contains("-")) {
                    val index = args[1].indexOf("-")
                    val min = args[1].substring(0, index).toIntOrNull()
                    val max = args[1].substring(index+1, args[1].length).toIntOrNull()
                    if (min != null && max != null) {
                        amount = (min + Math.round(Math.random()*(max-min))).toInt()
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
                ItemManager.getItemStack(args[0], player, data)?.let { itemStack ->
                    val maxStackSize = itemStack.maxStackSize
                    itemStack.amount = maxStackSize
                    var givenAmt = 0
                    while ((givenAmt + maxStackSize) <= amount) {
                        dropItems.add(itemStack.clone())
                        givenAmt += maxStackSize
                    }
                    if (givenAmt < amount) {
                        itemStack.amount = amount - givenAmt
                        dropItems.add(itemStack.clone())
                    }
                }
            } else {
                // 随机生成, 那疯狂造就完事儿了
                when {
                    ItemManager.hasItem(args[0]) -> {
                        repeat(amount) {
                            ItemManager.getItemStack(args[0], player, data)?.let { itemStack ->
                                dropItems.add(itemStack)
                            }
                        }
                    }
                    else -> {
                        mythicMobsHooker?.getItemStackSync(args[0])?.getItems(amount)?.forEach { dropItems.add(it) }
                    }
                }
            }
        }
    }

    /**
     * 根据信息进行物品掉落
     * @param dropItems 掉落物列表
     * @param location 掉落位置
     * @param offsetXString 发射横向偏移量
     * @param offsetYString 发射纵向偏移量
     * @param angleType 发射角度类型
     */
    @JvmStatic
    fun dropItems(
        dropItems: ArrayList<ItemStack>,
        location: Location,
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

                NeigeItems.bukkitScheduler.callSyncMethod(NeigeItems.plugin) {
                    location.world?.dropItem(location, itemStack) { item ->
                        itemTag["NeigeItems"]?.asCompound()?.let { neigeItems ->
                            neigeItems["owner"]?.asString()?.let { owner ->
                                item.setMetadataEZ("NI-Owner", owner)
                            }
                        }
                    }
                }.get()?.let { item ->
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

                    itemTag["NeigeItems"]?.asCompound()?.let { neigeItems ->
                        neigeItems["dropSkill"]?.asString()?.let { dropSkill ->
                            if (pluginManager.isPluginEnabled("MythicMobs")) {
                                mythicMobsHooker?.castSkill(item, dropSkill)
                            }
                        }
                    }
                }
            }
        } else {
            // 普通掉落
            for (itemStack in dropItems) {
                val itemTag = itemStack.getItemTag()

                NeigeItems.bukkitScheduler.callSyncMethod(NeigeItems.plugin) {
                    location.world?.dropItem(location, itemStack) { item ->
                        itemTag["NeigeItems"]?.asCompound()?.let { neigeItems ->
                            neigeItems["owner"]?.asString()?.let { owner ->
                                item.setMetadataEZ("NI-Owner", owner)
                            }
                        }
                    }
                }.get()?.let { item ->
                    itemTag["NeigeItems"]?.asCompound()?.let { neigeItems ->
                        neigeItems["dropSkill"]?.asString()?.let { dropSkill ->
                            if (pluginManager.isPluginEnabled("MythicMobs")) {
                                mythicMobsHooker?.castSkill(item, dropSkill)
                            }
                        }
                    }
                }
            }
        }
    }
}