package pers.neige.neigeitems.item

import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.Statistic
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.HumanEntity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.event.player.*
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.NeigeItems.bukkitScheduler
import pers.neige.neigeitems.NeigeItems.plugin
import pers.neige.neigeitems.manager.ItemManager.addCustomDurability
import pers.neige.neigeitems.utils.ItemUtils.getDeepOrNull
import pers.neige.neigeitems.utils.ItemUtils.putDeepFixed
import pers.neige.neigeitems.utils.LangUtils.getLang
import taboolib.common.platform.event.EventPriority
import taboolib.common.platform.event.SubscribeEvent
import taboolib.module.nms.ItemTagData
import taboolib.module.nms.getItemTag
import taboolib.platform.util.giveItem
import taboolib.platform.util.sendActionBar
import java.util.concurrent.ThreadLocalRandom

object ItemDurability {
    /**
     * 火焰弹
     */
    private val FIRE_CHARGE = (Material.matchMaterial("FIRE_CHARGE") ?: Material.matchMaterial("FIREBALL"))!!

    /**
     * 是否为1.12.2
     */
    private val LEGACY = FIRE_CHARGE.toString() == "FIREBALL"

    /**
     * 点燃TNT(点燃TNT不触发点燃方块事件, 故而另做考虑)
     */
    @SubscribeEvent(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun igniteTNT(event: PlayerInteractEvent) {
        // 交互物品
        val itemStack = event.item
        // 被交互方块
        val block = event.clickedBlock
//        // 无限耐久打火石点燃
//        if (
//            // 使用打火石
//            itemStack?.type == Material.FLINT_AND_STEEL
//            // 打火石无法破坏
//            && itemStack.itemMeta?.isUnbreakable == true
//            // 交互TNT
//            && block?.type == Material.TNT
//            // 右键交互
//            && event.action == Action.RIGHT_CLICK_BLOCK
//        ) {
//            // 尝试消耗耐久值, 对于已损坏物品取消事件
//            if (itemStack.damage(event.player) == DamageResult.BROKEN_ITEM) event.isCancelled = true
//        }
        // 火焰弹点燃
        if (
            // 非创造模式玩家
            event.player.gameMode != GameMode.CREATIVE
            // 使用火焰弹
            && itemStack?.type == FIRE_CHARGE
            // 交互TNT
            && block?.type == Material.TNT
            // 右键交互
            && event.action == Action.RIGHT_CLICK_BLOCK
        ) {
            // 尝试消耗耐久值并获取结果
            val result = itemStack.damage(
                player = event.player,
                // 可以交给服务端处理物品消耗
                breakItem = false
            )
            // 对于已损坏物品取消事件
            if (result == DamageResult.BROKEN_ITEM) {
                event.isCancelled = true
                // 物品损坏提示
                getLang("Messages.brokenItem")?.let {
                    if (it != "") event.player.sendActionBar(it)
                }
            // 对于存在自定义耐久值的物品
            } else if (result != DamageResult.VANILLA && result != DamageResult.BREAK) {
                // 物品数量+1, 让交互TNT事件消耗
                itemStack.amount += 1
                // 刷新玩家背包
                bukkitScheduler.runTaskLater(plugin, Runnable {event.player.updateInventory()}, 1)
            }
            return
        }
        // 对于已损坏物品取消事件
        if (itemStack?.getItemTag()?.getDeepOrNull("NeigeItems.durability")?.asInt() == 0) {
            event.isCancelled = true
            // 物品损坏提示
            getLang("Messages.brokenItem")?.let {
                if (it != "") event.player.sendActionBar(it)
            }
        }
    }

    /**
     * 交互实体
     */
    @SubscribeEvent(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun interact(event: PlayerInteractEntityEvent) {
        // 对于已损坏物品取消事件
        val itemStack = when(event.hand) {
            EquipmentSlot.HAND -> event.player.inventory.itemInMainHand
            else -> event.player.inventory.itemInOffHand
        }
        if (itemStack.type != Material.AIR) {
            if (itemStack.getItemTag().getDeepOrNull("NeigeItems.durability")?.asInt() == 0) {
                event.isCancelled = true
                // 物品损坏提示
                getLang("Messages.brokenItem")?.let {
                    if (it != "") event.player.sendActionBar(it)
                }
            }
        }
    }

    /**
     * 射箭
     */
    @SubscribeEvent(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun shootBow(event: EntityShootBowEvent) {
        // 对于已损坏物品取消事件
        if (event.bow?.getItemTag()?.getDeepOrNull("NeigeItems.durability")?.asInt() == 0) {
            event.isCancelled = true
            // 物品损坏提示
            getLang("Messages.brokenItem")?.let {
                if (it != "") (event.entity as? HumanEntity)?.sendActionBar(it)
            }
        }
    }

    /**
     * 吃/喝东西
     */
    @SubscribeEvent(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun consume(event: PlayerItemConsumeEvent) {
        // 对于已损坏物品取消事件
        if (event.item.getItemTag().getDeepOrNull("NeigeItems.durability")?.asInt() == 0) {
            event.isCancelled = true
            // 物品损坏提示
            getLang("Messages.brokenItem")?.let {
                if (it != "") event.player.sendActionBar(it)
            }
        }
    }

    /**
     * 伤害事件
     */
    @SubscribeEvent(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun entityDamageByEntity(event: EntityDamageByEntityEvent) {
        if (event.cause != EntityDamageEvent.DamageCause.ENTITY_ATTACK || event.damager !is Player) return
        val player = event.damager as Player
        val itemStack = player.inventory.itemInMainHand

        // 对于已损坏物品取消事件
        if (itemStack.getItemTag().getDeepOrNull("NeigeItems.durability")?.asInt() == 0) {
            event.isCancelled = true
            // 物品损坏提示
            getLang("Messages.brokenItem")?.let {
                if (it != "") player.sendActionBar(it)
            }
        }
    }

    /**
     * 含耐久物品损坏
     */
    @SubscribeEvent(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun itemDamage(event: PlayerItemDamageEvent) {
        // 消耗耐久值
        event.item.damage(event.player, event.damage, true, event)
    }

    /**
     * 经验修补
     */
    @SubscribeEvent(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun itemMend(event: PlayerItemMendEvent) {
        event.item.addCustomDurability(event.repairAmount)
    }

    /**
     * 扣除物品耐久值
     *
     * @param player 进行物品消耗的玩家
     * @param damage 伤害值
     * @param breakItem 是否损坏物品(对于火焰弹点燃TNT这类事件, 物品消耗大可交给服务端操作)
     * @param damageEvent PlayerItemDamageEvent, 用于比例修改物品耐久
     * @return 是否消耗成功(物品没有耐久值或耐久值不合法时同样返回true)
     */
    @JvmStatic
    fun ItemStack.damage(
        player: Player,
        damage: Int = 1,
        breakItem: Boolean = true,
        damageEvent: PlayerItemDamageEvent? = null
    ): DamageResult {
        // 检测伤害值合法性
        if (damage < 0) return DamageResult.INVALID_DAMAGE
        // 检测伤害值是否为0
        if (damage == 0) return DamageResult.ZERO_DAMAGE

        // 获取物品NBT
        val itemTag = getItemTag()
        // 获取物品耐久值(不存在则停止操作)
        val durability = itemTag.getDeepOrNull("NeigeItems.durability")?.asInt() ?: return DamageResult.VANILLA
        // 检测物品是否损坏
        if (durability == 0) {
            damageEvent?.isCancelled = true
            return DamageResult.BROKEN_ITEM
        }

        // 处理真实伤害值
        val realDamage = if (damageEvent == null) {
            // 在PlayerItemDamageEvent中, damage已经过处理, 不用我继续瞎寄吧操作
            damage
        } else {
            // 无限耐久物品处理
            var temp = damage
            // 获取耐久附魔等级
            val durabilityLevel = getEnchantmentLevel(Enchantment.DURABILITY)
            // 如果存在耐久附魔则进行原版耐久判断
            if (durabilityLevel > 0) {
                repeat(damage) {
                    // 1/(耐久等级+1)几率损耗耐久
                    if (ThreadLocalRandom.current().nextInt(durabilityLevel + 1) != 0) {
                        temp--
                    }
                }
            }
            temp
        }
        // 检测真实伤害值是否为0
        if (realDamage == 0) return DamageResult.ZERO_DAMAGE

        // 如果是堆叠物品
        if (amount != 1) {
            // 获取物品克隆
            val itemClone = clone()
            // 将克隆的数量-1
            itemClone.amount = itemClone.amount - 1
            // 将本体的数量设置为1
            amount = 1
            // 给予物品克隆, 即拆分出的物品
            player.giveItem(itemClone)
        }
        // 如果伤害值大于等于耐久值
        if (realDamage >= durability) {
            // 获取物品是否破坏(默认破坏)
            val itemBreak = itemTag.getDeepOrNull("NeigeItems.itemBreak")?.asByte() ?: 1.toByte()
            // 如果破坏物品
            return if (itemBreak == 1.toByte()) {
                // 扣除一个物品
                if (breakItem) {
                    if (damageEvent == null) {
                        amount -= 1
                    } else {
                        damageEvent.damage = type.maxDurability - this.durability + 1
                    }
                }
                // 为玩家添加一个破坏物品的统计数据
                if (type != FIRE_CHARGE) {
                    player.incrementStatistic(Statistic.BREAK_ITEM, type)
                }
                // 播放物品破碎声
                player.playSound(player.location, Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f)
                // 物品损坏提示
                getLang("Messages.brokenItem")?.let {
                    if (it != "") player.sendActionBar(it)
                }
                // 返回物品破坏结果
                DamageResult.BREAK
            // 如果不破坏物品
            } else {
                // 修改耐久值
                itemTag.putDeepFixed("NeigeItems.durability", ItemTagData(0))
                damageEvent?.let {damageEvent.damage = type.maxDurability - this.durability - 1}
                // 保存NBT
                itemTag.saveTo(this)
                // 播放物品破碎声
                player.playSound(player.location, Sound.ENTITY_ITEM_BREAK, 1.0f, 1.0f)
                // 物品损坏提示
                getLang("Messages.brokenItem")?.let {
                    if (it != "") player.sendActionBar(it)
                }
                // 返回耐久消耗成功结果
                DamageResult.SUCCESS
            }
        // 如果伤害值小于耐久值
        } else {
            // 修改耐久值
            itemTag.putDeepFixed("NeigeItems.durability", ItemTagData(durability-realDamage))
            if (damageEvent != null) {
                val maxDurability = itemTag.getDeepOrNull("NeigeItems.maxDurability")!!.asInt()
                damageEvent.damage = ((realDamage.toDouble() / maxDurability.toDouble()) * type.maxDurability).toInt()
            }
            // 保存NBT
            itemTag.saveTo(this)
            // 返回耐久消耗成功结果
            return DamageResult.SUCCESS
        }
    }

    enum class DamageResult {
        /**
         * 原版物品, 无自定义耐久
         */
        VANILLA,
        /**
         * 耐久耗尽, 物品破碎
         */
        BREAK,
        /**
         * 已损坏物品, 应取消本次事件
         */
        BROKEN_ITEM,
        /**
         * 不损失物品耐久
         */
        ZERO_DAMAGE,
        /**
         * 耐久正常消耗
         */
        SUCCESS,
        /**
         * 耐久消耗值小于0, 非法
         */
        INVALID_DAMAGE
    }
}