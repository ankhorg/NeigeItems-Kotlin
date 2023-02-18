package pers.neige.neigeitems.hook.mythicmobs.impl

import io.lumine.xikage.mythicmobs.MythicMobs
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobSpawnEvent
import io.lumine.xikage.mythicmobs.io.MythicConfig
import io.lumine.xikage.mythicmobs.items.ItemManager
import io.lumine.xikage.mythicmobs.utils.config.file.FileConfiguration
import io.lumine.xikage.mythicmobs.utils.config.file.YamlConfiguration
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.event.MythicDropEvent
import pers.neige.neigeitems.hook.mythicmobs.MythicMobsHooker
import pers.neige.neigeitems.item.ItemPack
import pers.neige.neigeitems.manager.ItemPackManager.itemPacks
import pers.neige.neigeitems.utils.ItemUtils.dropItems
import pers.neige.neigeitems.utils.ItemUtils.loadItems
import pers.neige.neigeitems.utils.SectionUtils.parseSection
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.function.registerBukkitListener
import taboolib.common.platform.function.submit

/**
 * 4.9.0版本MM挂钩
 *
 * @constructor 启用4.9.0版本MM挂钩
 */
class MythicMobsHookerImpl490 : MythicMobsHooker() {
    private val test = YamlConfiguration()

    private val itemManager: ItemManager = MythicMobs.inst().itemManager

    private val apiHelper = MythicMobs.inst().apiHelper

    override val spawnListener = registerBukkitListener(MythicMobSpawnEvent::class.java, EventPriority.HIGH, false) {
        submit(async = true) {
            val entity = it.entity as LivingEntity
            val config = it.mobType.config.getNestedConfig("NeigeItems")
            val equipment = config.getStringList("Equipment")
            val dropEquipment = config.getStringList("DropEquipment")

            loadEquipment(equipment, dropEquipment, entity, it.mobType.internalName)
        }
    }

    override val deathListener = registerBukkitListener(MythicMobDeathEvent::class.java, EventPriority.NORMAL, false) {
        submit(async = true) {
            // 获取击杀者
            val player = it.killer
            // 获取MM怪物配置
            val mythicMob = it.mobType
            // 获取MM怪物ID
            val mythicId = mythicMob.internalName
            // 获取对应MythicConfig
            val mythicConfig: MythicConfig = mythicMob.config
            // 不要问我为什么这么干, 谁问谁死
            val fc = mythicConfig::class.java.getDeclaredField("fc")
            fc.isAccessible = true
            // 获取MM怪物的ConfigurationSection
            val fileConfiguration: FileConfiguration = fc.get(mythicConfig) as FileConfiguration
            val configSection = fileConfiguration.getConfigurationSection(mythicId)

            // 如果怪物配置了NeigeItems相关信息
            if (configSection.contains("NeigeItems")) {
                // 获取被击杀的MM怪物
                val entity = it.entity as LivingEntity
                // 获取NeigeItems相关配置项
                val neigeItems = configSection.getConfigurationSection("NeigeItems")

                // 获取物品配置
                val drops: List<String>? =
                    // 如果配置了掉落相关信息
                    if (neigeItems.contains("Drops")) {
                        // 获取掉落相关的配置项
                        neigeItems.getStringList("Drops")
                    } else null

                // 获取物品包配置(未经过节点解析的物品包ID)
                val dropPackRawIds: List<String>? =
                    // 如果配置了掉落物品包
                    if (neigeItems.contains("DropPacks")) {
                        // 获取物品包信息
                        neigeItems.getStringList("DropPacks")
                    } else null

                // 获取多彩掉落信息
                var offsetXString: String? = null
                var offsetYString: String? = null
                var angleType: String? = null

                // 如果配置了多彩掉落信息
                if (neigeItems.contains("FancyDrop")) {
                    // 获取多彩掉落相关信息
                    val fancyDrop = neigeItems.getConfigurationSection("FancyDrop")
                    // 获取掉落偏移信息
                    val offset = fancyDrop.getConfigurationSection("offset")
                    offsetXString = offset.getString("x")
                    offsetYString = offset.getString("y")
                    angleType = fancyDrop.getString("angle.type")
                }

                // 东西都加载好了, 触发一下事件
                val configLoadedEvent = MythicDropEvent.ConfigLoaded(mythicId, entity, player, drops, dropPackRawIds, offsetXString, offsetYString, angleType)
                configLoadedEvent.call()
                if (configLoadedEvent.isCancelled) return@submit

                // 判断是否是玩家击杀, 别问我为什么现在才判断, 这是为了兼容另外一个插件, 所以MythicDropEvent.ConfigLoaded必须先触发一下
                if (player !is Player) return@submit

                offsetXString = configLoadedEvent.offsetXString
                offsetYString = configLoadedEvent.offsetYString
                angleType = configLoadedEvent.angleType

                // 待掉落物品包
                val dropPacks = ArrayList<ItemPack>()
                configLoadedEvent.dropPacks?.forEach { id ->
                    itemPacks[id.parseSection(player)]?.let { itemPack ->
                        dropPacks.add(itemPack)
                        // 尝试加载多彩掉落
                        if (itemPack.fancyDrop) {
                            offsetXString = itemPack.offsetXString
                            offsetYString = itemPack.offsetYString
                            angleType = itemPack.angleType
                        }
                    }
                }

                // 预定掉落物列表
                val dropItems = ArrayList<ItemStack>()
                // 掉落应该掉落的装备
                loadEquipmentDrop(entity, dropItems, player)
                // 加载掉落物品包信息
                dropPacks.forEach { itemPack ->
                    // 加载物品掉落信息
                    loadItems(dropItems, itemPack.items, player, HashMap(), itemPack.sections)
                }
                // 加载掉落信息
                configLoadedEvent.drops?.let { loadItems(dropItems, it, player) }

                // 物品都加载好了, 触发一下事件
                val dropEvent = MythicDropEvent.Drop(mythicId, entity, player, dropItems, offsetXString, offsetYString, angleType)
                dropEvent.call()
                if (dropEvent.isCancelled) return@submit

                // 掉落物品
                dropItems(dropEvent.dropItems, entity.location, player, dropEvent.offsetXString, dropEvent.offsetYString, dropEvent.angleType)
            }
        }
    }

    override fun getItemStack(id: String): ItemStack? {
        return itemManager.getItemStack(id)
    }

    // 这个版本并不需要同步获取物品
    override fun getItemStackSync(id: String): ItemStack? {
        return itemManager.getItemStack(id)
    }

    override fun castSkill(entity: Entity, skill: String, trigger: Entity?) {
        apiHelper.castSkill(entity, skill, trigger, entity.location, null, null, 1.0F)
    }

    override fun getItemIds(): List<String> {
        return itemManager.itemNames.toList()
    }
}