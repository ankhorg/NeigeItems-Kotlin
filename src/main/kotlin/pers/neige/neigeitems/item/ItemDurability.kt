package pers.neige.neigeitems.item

import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.Statistic
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemDamageEvent
import org.bukkit.event.player.PlayerItemMendEvent
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtCompound
import pers.neige.neigeitems.manager.HookerManager
import pers.neige.neigeitems.manager.ItemManager.refreshDurability
import pers.neige.neigeitems.utils.ItemUtils.copy
import pers.neige.neigeitems.utils.ItemUtils.getDamage
import pers.neige.neigeitems.utils.ItemUtils.getDirectTag
import pers.neige.neigeitems.utils.ItemUtils.isNiItem
import pers.neige.neigeitems.utils.ItemUtils.saveToSafe
import pers.neige.neigeitems.utils.LangUtils.getLang
import pers.neige.neigeitems.utils.PlayerUtils.giveItem
import pers.neige.neigeitems.utils.PlayerUtils.sendActionBar
import pers.neige.neigeitems.utils.SchedulerUtils.syncLater
import java.util.concurrent.ThreadLocalRandom

/**
 * 物品自定义耐久实现类
 */
object ItemDurability {
    private val FIRE_CHARGE_MATERIAL =
        (HookerManager.getMaterial("FIRE_CHARGE") ?: HookerManager.getMaterial("FIREBALL"))!!

    /**
     * 方块交互
     */
    fun interact(
        player: Player,
        neigeItems: NbtCompound,
        event: PlayerInteractEvent
    ): Boolean {
        // 对于已损坏物品取消事件
        if (neigeItems.getIntOrNull("durability") == 0) {
            event.isCancelled = true
            // 物品损坏提示
            getLang("Messages.brokenItem")?.let {
                if (it != "") player.sendActionBar(it)
            }
            return true
        }
        return false
    }

    /**
     * 点燃TNT(点燃TNT不触发点燃方块事件, 故而另做考虑)
     */
    fun igniteTNT(
        player: Player,
        itemStack: ItemStack,
        itemTag: NbtCompound,
        neigeItems: NbtCompound,
        event: PlayerInteractEvent
    ) {
        // 被交互方块
        val block = event.clickedBlock
        // 火焰弹点燃
        if (
        // 非创造模式玩家
            event.player.gameMode != GameMode.CREATIVE
            // 使用火焰弹
            && itemStack.type == FIRE_CHARGE_MATERIAL
            // 交互TNT
            && block?.type == Material.TNT
            // 右键交互
            && event.action == Action.RIGHT_CLICK_BLOCK
        ) {
            // 尝试消耗耐久值并获取结果
            val result = damage(
                player,
                itemStack,
                itemTag,
                neigeItems,
                1,
                false
            )
            // 对于已损坏物品取消事件
            if (result == DamageResult.BROKEN_ITEM) {
                event.isCancelled = true
                // 物品损坏提示
                getLang("Messages.brokenItem")?.let {
                    if (it != "") player.sendActionBar(it)
                }
                // 对于存在自定义耐久值的物品
            } else if (result != DamageResult.VANILLA && result != DamageResult.BREAK) {
                // 物品数量+1, 让交互TNT事件消耗
                itemStack.amount += 1
                // 刷新玩家背包
                syncLater(1) {
                    player.updateInventory()
                }
            }
        }
    }

    /**
     * 已损坏物品检测
     */
    fun durabilityChecker(
        player: Player,
        neigeItems: NbtCompound,
        event: Cancellable
    ) {
        // 对于已损坏物品取消事件
        if (neigeItems.getIntOrNull("durability") == 0) {
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
    fun itemDamage(
        player: Player,
        itemStack: ItemStack,
        itemTag: NbtCompound,
        neigeItems: NbtCompound,
        event: PlayerItemDamageEvent
    ) {
        // 消耗耐久值
        damage(player, itemStack, itemTag, neigeItems, event.damage, true, event)
    }

    /**
     * 经验修补
     */
    fun itemMend(event: PlayerItemMendEvent) {
        val itemStack = event.item
        // 直掏NBT
        val directTag = itemStack.getDirectTag() ?: return
        // 不是NI物品还加个屁的自定义耐久
        val neigeItems = directTag.getCompound("NeigeItems") ?: return
        // 获取物品耐久值
        val durability = neigeItems.getIntOrNull("durability") ?: return
        event.isCancelled = true
        event.experienceOrb.experience -= event.repairAmount / 2
        // 获取物品最大耐久值
        val maxDurability = neigeItems.getInt("maxDurability")
        // 计算新耐久值
        val newDurability = (durability + event.repairAmount).coerceAtMost(maxDurability).coerceAtLeast(0)
        // 获取物品是否破坏(默认破坏)
        val itemBreak = neigeItems.getBoolean("itemBreak", true)
        // 破坏
        if (newDurability == 0 && itemBreak) {
            itemStack.amount = 0
            // 不破坏
        } else {
            // 修改耐久值
            neigeItems.putInt("durability", newDurability)
            itemStack.refreshDurability(newDurability, maxDurability)
        }
    }

    /**
     * 扣除物品耐久值
     *
     * @param player 进行物品消耗的玩家
     * @param itemStack 待操作物品
     * @param damage 伤害值
     * @param breakItem 是否损坏物品(对于火焰弹点燃TNT这类事件, 物品消耗大可交给服务端操作)
     * @param damageEvent PlayerItemDamageEvent, 用于比例修改物品耐久
     * @return 是否消耗成功(物品没有耐久值或耐久值不合法时同样返回true)
     */
    fun damage(
        player: Player,
        itemStack: ItemStack,
        damage: Int = 1,
        breakItem: Boolean = true,
        damageEvent: PlayerItemDamageEvent? = null
    ): DamageResult {
        // 获取NI物品信息(不是NI物品就停止操作)
        val itemInfo = itemStack.isNiItem(false) ?: return DamageResult.VANILLA
        // 物品NBT
        val itemTag: NbtCompound = itemInfo.itemTag
        // NI物品数据
        val neigeItems: NbtCompound = itemInfo.neigeItems
        // NI物品id
        val id: String = itemInfo.id
        return damage(player, itemStack, itemTag, neigeItems, damage, breakItem, damageEvent)
    }

    /**
     * 扣除物品耐久值
     *
     * @param player 进行物品消耗的玩家
     * @param itemStack 待操作物品
     * @param itemTag 物品nbt
     * @param neigeItems nbt下的NeigeItems部分
     * @param damage 伤害值
     * @param breakItem 是否损坏物品(对于火焰弹点燃TNT这类事件, 物品消耗大可交给服务端操作)
     * @param damageEvent PlayerItemDamageEvent, 用于比例修改物品耐久
     * @return 是否消耗成功(物品没有耐久值或耐久值不合法时同样返回true)
     */
    @JvmStatic
    fun damage(
        player: Player,
        itemStack: ItemStack,
        itemTag: NbtCompound,
        neigeItems: NbtCompound,
        damage: Int = 1,
        breakItem: Boolean = true,
        damageEvent: PlayerItemDamageEvent? = null
    ): DamageResult {
        // 检测伤害值合法性
        if (damage < 0) return DamageResult.INVALID_DAMAGE
        // 检测伤害值是否为0
        if (damage == 0) return DamageResult.ZERO_DAMAGE

        // 获取物品耐久值(不存在则停止操作)
        val durability = neigeItems.getIntOrNull("durability") ?: return DamageResult.VANILLA
        // 检测物品是否损坏
        if (durability == 0) {
            damageEvent?.isCancelled = true
            return DamageResult.BROKEN_ITEM
        }

        // 处理真实伤害值
        val realDamage = if (damageEvent != null) {
            // 在PlayerItemDamageEvent中, damage已经过处理, 不用我继续瞎寄吧操作
            damage
        } else {
            // 无限耐久物品处理
            var temp = damage
            // 获取耐久附魔等级
            val durabilityLevel = itemStack.getEnchantmentLevel(Enchantment.DURABILITY)
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
        if (itemStack.amount != 1) {
            // 获取物品克隆
            val itemClone = itemStack.copy()
            // 将克隆的数量-1
            itemClone.amount -= 1
            // 将本体的数量设置为1
            itemStack.amount = 1
            // 给予物品克隆, 即拆分出的物品
            player.giveItem(itemClone)
        }
        // 如果伤害值大于等于耐久值
        if (realDamage >= durability) {
            // 获取物品是否破坏(默认破坏)
            val itemBreak = neigeItems.getBoolean("itemBreak", true)
            // 如果破坏物品
            return if (itemBreak) {
                // 扣除一个物品
                if (breakItem) {
                    if (damageEvent == null) {
                        itemStack.amount -= 1
                    } else {
                        damageEvent.damage = itemStack.type.maxDurability - itemStack.getDamage() + 1
                    }
                }
                // 为玩家添加一个破坏物品的统计数据
                if (itemStack.type != FIRE_CHARGE_MATERIAL) {
                    player.incrementStatistic(Statistic.BREAK_ITEM, itemStack.type)
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
                neigeItems.putInt("durability", 0)
                damageEvent?.let { damageEvent.damage = itemStack.type.maxDurability - itemStack.getDamage() - 1 }
                // 保存NBT
                itemTag.saveToSafe(itemStack)
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
            val finalDurability = durability - realDamage
            neigeItems.putInt("durability", finalDurability)
            if (damageEvent != null) {
                val maxDurability = neigeItems.getInt("maxDurability")
                damageEvent.damage =
                    (itemStack.type.maxDurability * (1 - (finalDurability.toDouble() / maxDurability))).toInt() - itemStack.getDamage()
            }
            // 保存NBT
            itemTag.saveToSafe(itemStack)
            // 返回耐久消耗成功结果
            return DamageResult.SUCCESS
        }
    }

    /**
     * 耐久消耗事件的各种结果
     */
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