package pers.neige.neigeitems.hook.mythicmobs

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.NeigeItems.plugin
import pers.neige.neigeitems.event.MobInfoReloadedEvent
import pers.neige.neigeitems.event.MythicDropEvent
import pers.neige.neigeitems.event.MythicEquipEvent
import pers.neige.neigeitems.manager.ConfigManager
import pers.neige.neigeitems.manager.HookerManager
import pers.neige.neigeitems.manager.ItemManager
import pers.neige.neigeitems.manager.ItemPackManager
import pers.neige.neigeitems.utils.ConfigUtils
import pers.neige.neigeitems.utils.ItemUtils
import pers.neige.neigeitems.utils.ItemUtils.getCaughtVelocity
import pers.neige.neigeitems.utils.ItemUtils.getDoubleOrNull
import pers.neige.neigeitems.utils.ItemUtils.getExactStringOrNull
import pers.neige.neigeitems.utils.ItemUtils.getNbt
import pers.neige.neigeitems.utils.ItemUtils.getNbtOrNull
import pers.neige.neigeitems.utils.ItemUtils.loadItems
import pers.neige.neigeitems.utils.ItemUtils.saveToSafe
import pers.neige.neigeitems.utils.SectionUtils.parseSection
import taboolib.common.platform.event.ProxyListener
import taboolib.common.platform.function.submit
import java.io.File
import java.text.DecimalFormat
import java.util.*
import java.util.concurrent.ThreadLocalRandom

/**
 * MM挂钩
 */
abstract class MythicMobsHooker {
    /**
     * MM挂钩版本
     */
    abstract val version: String

    /**
     * MM怪物信息
     */
    abstract val mobInfos: HashMap<String, ConfigurationSection>

    /**
     * MM怪物生成事件
     */
    abstract val spawnEventClass: Class<*>

    /**
     * MM怪物死亡事件
     */
    abstract val deathEventClass: Class<*>

    /**
     * MM重载事件
     */
    abstract val reloadEventClass: Class<*>

    /**
     * MM怪物生成事件监听器, 监听器优先级HIGH, 得以覆盖MM自身的装备操作
     */
    abstract val spawnListener: ProxyListener

    /**
     * MM怪物死亡事件监听器, 监听器优先级NORMAL
     */
    abstract val deathListener: ProxyListener

    /**
     * MM重载事件监听器, 监听器优先级NORMAL
     */
    abstract val reloadListener: ProxyListener

    /**
     * 获取MM物品, 不存在对应ID的MM物品则返回null
     *
     * @param id MM物品ID
     * @return MM物品(不存在则返回null)
     */
    abstract fun getItemStack(id: String): ItemStack?

    /**
     * 同步获取MM物品, 不存在对应ID的MM物品则返回null(在5.1.0左右的版本中, MM物品的获取强制同步)
     * 不一定真的同步获取, 只在必要时同步(指高版本)
     *
     * @param id MM物品ID
     * @return MM物品(不存在则为空)
     */
    abstract fun getItemStackSync(id: String): ItemStack?

    /**
     * 释放MM技能
     *
     * @param entity 技能释放者
     * @param skill 技能ID
     */
    abstract fun castSkill(entity: Entity, skill: String, trigger: Entity? = null)

    /**
     * 获取所有MM物品ID
     *
     * @return 所有MM物品ID
     */
    abstract fun getItemIds(): List<String>

    /**
     * 判断实体是否为MM生物
     *
     * @param entity 待判断实体
     * @return 该实体是否为MM生物
     */
    abstract fun isMythicMob(entity: Entity): Boolean

    /**
     * 获取MM实体的ID(非MM实体返回null)
     *
     * @param entity MM实体
     * @return MM实体ID(非MM实体返回null)
     */
    abstract fun getMythicId(entity: Entity): String?

    private val df2 = DecimalFormat("#0.00")

    /**
     * 为MM怪物穿戴装备
     *
     * @param entity 怪物实体
     * @param internalName 怪物ID
     */
    internal fun spawnEvent(
        internalName: String,
        entity: LivingEntity,
        mobLevel: Int
    ) {
        // 获取MM怪物的ConfigurationSection
        mobInfos[internalName]?.let { config ->
            // 装备信息
            val equipment = config.getStringList("NeigeItems.Equipment")
            // 掉落装备概率
            val dropEquipment = config.getStringList("NeigeItems.DropEquipment")

            val entityEquipment = entity.equipment
            val dropChance = HashMap<String, Double>()

            // 构建怪物参数
            val params = mutableMapOf<String, String>().also { map ->
                map["mobMaxHealth"] = df2.format(entity.maxHealth)
                map["mobId"] = internalName
                map["mobLevel"] = mobLevel.toString()
                val location = entity.location
                map["mobLocationX"] = df2.format(location.x)
                map["mobLocationY"] = df2.format(location.y)
                map["mobLocationZ"] = df2.format(location.z)
                map["mobLocationYaw"] = df2.format(location.yaw)
                map["mobLocationPitch"] = df2.format(location.pitch)
                map["mobWorld"] = entity.world.name
                map["mobName"] = entity.name
                entity.customName?.let {
                    map["mobCustomName"] = it
                }
            }

            // 获取死亡后相应NI物品掉落几率
            for (value in dropEquipment) {
                val string = value.parseSection(params)
                var id = string.lowercase(Locale.getDefault())
                var chance = 1.toDouble()
                if (string.contains(" ")) {
                    val index = string.indexOf(" ")
                    id = string.substring(0, index).lowercase(Locale.getDefault())
                    chance = string.substring(index+1).toDoubleOrNull() ?: 1.toDouble()
                }
                dropChance[id] = chance
            }

            // 获取出生附带装备信息
            for (value in equipment) {
                val string = value.parseSection(params)
                if (string.contains(": ")) {
                    val index = string.indexOf(": ")
                    val slot = string.substring(0, index).lowercase(Locale.getDefault())
                    val info = string.substring(index+2)
                    // [物品ID] (生成概率) (指向数据)
                    val args = info.split(" ", limit = 3)

                    val data: String? = args.getOrNull(2)

                    if (args.size > 1) {
                        val probability = args[1].toDoubleOrNull()
                        if (probability != null && ThreadLocalRandom.current().nextDouble() > probability) continue
                    }

                    try {
                        (ItemManager.getItemStack(args[0], null, data)
                            ?: HookerManager.easyItemHooker?.getItemStack(args[0])
                            ?: getItemStackSync(args[0]))?.let { itemStack ->
                            dropChance[slot]?.let { chance ->
                                val itemTag = itemStack.getNbt()
                                itemTag.putDeepDouble("NeigeItems.dropChance", chance)
                                itemTag.saveToSafe(itemStack)
                            }
                            val event = MythicEquipEvent(entity, internalName, slot, itemStack)

                            when (slot) {
                                "helmet" -> {
                                    event.call()
                                    when {
                                        !event.isCancelled -> entityEquipment?.helmet = event.itemStack
                                        else -> {}
                                    }
                                }
                                "chestplate" -> {
                                    event.call()
                                    when {
                                        !event.isCancelled -> entityEquipment?.chestplate = event.itemStack
                                        else -> {}
                                    }
                                }
                                "leggings" -> {
                                    event.call()
                                    when {
                                        !event.isCancelled -> entityEquipment?.leggings = event.itemStack
                                        else -> {}
                                    }
                                }
                                "boots" -> {
                                    event.call()
                                    when {
                                        !event.isCancelled -> entityEquipment?.boots = event.itemStack
                                        else -> {}
                                    }
                                }
                                "mainhand" -> {
                                    event.call()
                                    when {
                                        !event.isCancelled -> entityEquipment?.setItemInMainHand(event.itemStack)
                                        else -> {}
                                    }
                                }
                                "offhand" -> {
                                    event.call()
                                    when {
                                        !event.isCancelled -> entityEquipment?.setItemInOffHand(event.itemStack)
                                        else -> {}
                                    }
                                }
                                else -> {}
                            }
                        }
                    } catch (error: Throwable) {
                        ConfigManager.config.getString("Messages.equipFailed")?.let { message ->
                            println(message
                                .replace("{mobID}", internalName)
                                .replace("{itemID}", args[0]))
                        }
                        error.printStackTrace()
                    }
                }
            }
        }
    }

    internal fun deathEvent(
        killer: LivingEntity?,
        entity: LivingEntity,
        internalName: String,
        mobLevel: Int
    ) {
        // 获取MM怪物的ConfigurationSection
        mobInfos[internalName]?.let { configSection ->
            // 如果怪物配置了NeigeItems相关信息
            if (configSection.contains("NeigeItems")) {
                val drops = configSection.getStringList("NeigeItems.Drops")
                val fishDrops = configSection.getStringList("NeigeItems.FishDrops")
                val dropPackRawIds = configSection.getStringList("NeigeItems.DropPacks")
                var offsetXString = configSection.getString("NeigeItems.FancyDrop.offset.x")
                var offsetYString = configSection.getString("NeigeItems.FancyDrop.offset.y")
                var angleType = configSection.getString("NeigeItems.FancyDrop.angle.type")

                // 东西都加载好了, 触发一下事件
                val configLoadedEvent = MythicDropEvent.ConfigLoaded(internalName, entity, killer, drops, fishDrops, dropPackRawIds, offsetXString, offsetYString, angleType)
                configLoadedEvent.call()
                if (configLoadedEvent.isCancelled) return

                // 判断玩家击杀
                if (killer !is Player && configSection.getBoolean("NeigeItems.PlayerOnly", true)) return

                val player = killer as? Player

                offsetXString = configLoadedEvent.offsetXString
                offsetYString = configLoadedEvent.offsetYString
                angleType = configLoadedEvent.angleType

                // 构建怪物参数
                val params = mutableMapOf<String, String>().also { map ->
                    map["mobMaxHealth"] = df2.format(entity.maxHealth)
                    map["mobId"] = internalName
                    map["mobLevel"] = mobLevel.toString()
                    val location = entity.location
                    map["mobLocationX"] = df2.format(location.x)
                    map["mobLocationY"] = df2.format(location.y)
                    map["mobLocationZ"] = df2.format(location.z)
                    map["mobLocationYaw"] = df2.format(location.yaw)
                    map["mobLocationPitch"] = df2.format(location.pitch)
                    map["mobWorld"] = entity.world.name
                    map["mobName"] = entity.name
                    entity.customName?.let {
                        map["mobCustomName"] = it
                    }
                }

                // 预定掉落物列表
                val dropItems = ArrayList<ItemStack>()
                // 加载物品包掉落
                configLoadedEvent.dropPacks?.forEach { info ->
                    // 物品包ID 数量 概率 指向数据
                    val args = info.parseSection(params, player).split(" ", limit = 4)
                    // 物品包ID
                    val id = args[0]
                    // 物品包数量
                    val amount = args.getOrNull(1)?.let {
                        when {
                            it.contains("-") -> {
                                val index = it.indexOf("-")
                                val min = it.substring(0, index).toIntOrNull()
                                val max = it.substring(index+1, it.length).toIntOrNull()
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
                    // 生成概率
                    val probability = args.getOrNull(2)?.toDoubleOrNull() ?: 1.0
                    // 指向数据
                    val data: String? = args.getOrNull(3)

                    // 进行概率随机
                    if (ThreadLocalRandom.current().nextDouble() <= probability) {
                        // 获取对应物品包
                        ItemPackManager.getItemPack(id)?.let { itemPack ->
                            // 尝试加载多彩掉落
                            if (itemPack.fancyDrop) {
                                offsetXString = itemPack.offsetXString
                                offsetYString = itemPack.offsetYString
                                angleType = itemPack.angleType
                            }
                            // 重复amount次
                            repeat(amount) {
                                // 加载物品掉落信息
                                dropItems.addAll(itemPack.getItemStacks(player, data))
                            }
                        }
                    }
                }
                // 掉落应该掉落的装备
                loadEquipmentDrop(entity, dropItems, player)
                // 加载掉落信息
                configLoadedEvent.drops?.let { loadItems(dropItems, it, player as? OfflinePlayer, params, null, true) }

                // 预定拟渔获掉落物列表
                val fishDropItems: ArrayList<ItemStack>? =
                    // 存在击杀者, 载入fishDropItems
                    if (killer != null) {
                        ArrayList<ItemStack>().also {
                            configLoadedEvent.fishDrops?.let { fishDrops -> loadItems(it, fishDrops, player as? OfflinePlayer, params, null, true) }
                        }
                    // 不存在击杀者, 载入dropItems
                    } else {
                        configLoadedEvent.fishDrops?.let { loadItems(dropItems, it, player as? OfflinePlayer, params, null, true) }
                        null
                    }

                // 物品都加载好了, 触发一下事件
                val dropEvent = MythicDropEvent.Drop(internalName, entity, player, dropItems, fishDropItems, offsetXString, offsetYString, angleType)
                dropEvent.call()
                if (dropEvent.isCancelled) return

                // 掉落物品
                ItemUtils.dropItems(
                    dropEvent.dropItems,
                    entity.location,
                    killer,
                    dropEvent.offsetXString,
                    dropEvent.offsetYString,
                    dropEvent.angleType
                )
                if (killer != null) {
                    // 拟渔获向量计算
                    val caughtVelocity = getCaughtVelocity(entity.location, killer.location)
                    // 拟渔获掉落
                    Bukkit.getScheduler().callSyncMethod(plugin) {
                        dropEvent.fishDropItems?.forEach { itemStack ->
                            // 掉落
                            HookerManager.nmsHooker.dropItem(
                                entity.world,
                                entity.location,
                                itemStack
                            ) { item ->
                                item.velocity = caughtVelocity
                            }
                        }
                    }
                }
            }
        }
    }

    init {
        loadMobInfos()
    }

    internal fun loadMobInfos() {
        submit(async = true) {
            mobInfos.clear()
            for (file: File in ConfigUtils.getAllFiles("MythicMobs", "Mobs")) {
                val config = YamlConfiguration.loadConfiguration(file)
                config.getKeys(false).forEach { id ->
                    config.getConfigurationSection(id)?.let {
                        mobInfos[id] = it
                    }
                }
            }
            MobInfoReloadedEvent().call()
        }
    }

    /**
     * 根据掉落信息加载掉落物品
     *
     * @param entity 待掉落物品的MM怪物
     * @param dropItems 用于存储待掉落物品
     * @param player 用于解析物品的玩家
     */
    private fun loadEquipmentDrop(
        entity: LivingEntity,
        dropItems: ArrayList<ItemStack>,
        player: LivingEntity?
    ) {
        // 获取MM怪物身上的装备
        val entityEquipment = entity.equipment
        // 一个个的全掏出来, 等会儿挨个康康
        val equipments = ArrayList<ItemStack>()
        entityEquipment?.helmet?.clone()?.let { equipments.add(it) }
        entityEquipment?.chestplate?.clone()?.let { equipments.add(it) }
        entityEquipment?.leggings?.clone()?.let { equipments.add(it) }
        entityEquipment?.boots?.clone()?.let { equipments.add(it) }
        entityEquipment?.itemInMainHand?.clone()?.let { equipments.add(it) }
        entityEquipment?.itemInOffHand?.clone()?.let { equipments.add(it) }

        loadEquipmentDrop(equipments, dropItems, player)
    }

    /**
     * 根据掉落信息加载掉落物品
     *
     * @param equipments 怪物身上的装备
     * @param dropItems 用于存储待掉落物品
     * @param player 用于解析物品的玩家
     */
    private fun loadEquipmentDrop(
        equipments: ArrayList<ItemStack>,
        dropItems: ArrayList<ItemStack>,
        player: LivingEntity?
    ) {
        // 遍历怪物身上的装备, 看看哪个是生成时自带的需要掉落的NI装备
        for (itemStack in equipments) {
            // 空检测
            if (itemStack.type == Material.AIR) continue
            // 获取NBT
            val itemTag = itemStack.getNbtOrNull() ?: continue
            // 掏掉落概率
            val neigeItems = itemTag.getCompound("NeigeItems") ?: continue
            val dropChance = neigeItems.getDoubleOrNull("dropChance") ?: continue
            if (ThreadLocalRandom.current().nextDouble() <= dropChance) {
                val id = neigeItems.getExactStringOrNull("id")
                // 处理NI物品(根据玩家信息重新生成)
                if (id != null) {
                    val target = when (player) {
                        is OfflinePlayer -> player
                        else -> null
                    }
                    ItemManager.getItemStack(id, target, neigeItems.getExactStringOrNull("data"))?.let {
                        // 丢进待掉落列表里
                        dropItems.add(it)
                    }
                    // 处理MM/EI物品(单纯移除NBT)
                } else {
                    neigeItems.remove("dropChance")
                    if (neigeItems.isEmpty()) {
                        itemTag.remove("NeigeItems")
                    }
                    itemTag.saveToSafe(itemStack)
                    dropItems.add(itemStack)
                }
            }
        }
    }
}