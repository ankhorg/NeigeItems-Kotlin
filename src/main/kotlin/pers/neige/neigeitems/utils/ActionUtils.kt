package pers.neige.neigeitems.utils

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.item.ItemInfo
import pers.neige.neigeitems.item.action.ActionTrigger
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtCompound
import pers.neige.neigeitems.manager.ConfigManager
import pers.neige.neigeitems.utils.ItemUtils.copy
import pers.neige.neigeitems.utils.ItemUtils.saveToSafe
import pers.neige.neigeitems.utils.PlayerUtils.checkCooldown
import pers.neige.neigeitems.utils.PlayerUtils.giveItem
import pers.neige.neigeitems.utils.PlayerUtils.sendActionBar
import pers.neige.neigeitems.utils.SchedulerUtils.syncLater
import pers.neige.neigeitems.utils.SectionUtils.parseItemSection
import pers.neige.neigeitems.utils.SectionUtils.parseSection

/**
 * 物品动作相关工具类
 */
object ActionUtils {
    /**
     * 通过动作信息判断玩家是否处于动作冷却(无消耗触发物品动作的冷却时间)
     *
     * @param player 消耗物品的玩家
     * @return 是否处于冷却时间
     */
    @JvmStatic
    fun ActionTrigger.isCoolDown(player: Player): Boolean {
        val cd = cooldown?.parseSection(player)?.toLongOrNull() ?: 1000
        return this.isCoolDown(player, cd)
    }

    /**
     * 通过动作信息判断玩家是否处于动作冷却(无消耗触发物品动作的冷却时间)
     *
     * @param player 消耗物品的玩家
     * @param itemStack 物品
     * @param itemInfo 物品信息
     * @return 是否处于冷却时间
     */
    @JvmStatic
    fun ActionTrigger.isCoolDown(
        player: Player,
        itemStack: ItemStack,
        itemInfo: ItemInfo
    ): Boolean {
        val cd = cooldown?.parseItemSection(itemStack, itemInfo, player)?.toLongOrNull() ?: 1000
        return this.isCoolDown(player, cd)
    }

    /**
     * 通过动作信息判断玩家是否处于动作冷却(无消耗触发物品动作的冷却时间)
     *
     * @param player 消耗物品的玩家
     * @param cd 冷却时间(ms)
     * @return 是否处于冷却时间
     */
    @JvmStatic
    fun ActionTrigger.isCoolDown(player: Player, cd: Long): Boolean {
        val leftTime = player.checkCooldown("ni:$group", cd)
        if (leftTime > 0) {
            ConfigManager.config.getString("Messages.itemCooldown")?.let {
                val message = it.replace("{time}", "%.1f".format(leftTime.toDouble() / 1000))
                player.sendActionBar(message)
            }
            return true
        }
        return false
    }

    /**
     * 消耗一定数量物品
     *
     * @param player 物品持有者, 用于接收拆分出的物品
     * @param amount 消耗数
     * @param itemTag 物品NBT
     * @param neigeItems NI特殊NBT
     * @param charge 已弃用的无意义参数
     * @return 是否消耗成功
     */
    @Deprecated(
        "已弃用",
        ReplaceWith(
            "this.consume(player, amount, itemTag, neigeItems, false)"
        )
    )
    @JvmStatic
    fun ItemStack.consume(
        player: Player,
        amount: Int,
        itemTag: NbtCompound,
        neigeItems: NbtCompound,
        charge: Int? = null
    ): Boolean {
        return this.consume(player, amount, itemTag, neigeItems, false)
    }

    /**
     * 消耗一定数量物品
     *
     * @param player 物品持有者, 用于接收拆分出的物品
     * @param amount 消耗数
     * @param itemTag 物品NBT
     * @param neigeItems NI特殊NBT
     * @return 是否消耗成功
     */
    @JvmStatic
    fun ItemStack.consume(
        player: Player,
        amount: Int,
        itemTag: NbtCompound,
        neigeItems: NbtCompound
    ): Boolean {
        return this.consume(player, amount, itemTag, neigeItems, false)
    }

    /**
     * 消耗一定数量物品
     *
     * @param player 物品持有者, 用于接收拆分出的物品
     * @param amount 消耗数
     * @param itemTag 物品NBT
     * @param neigeItems NI特殊NBT
     * @param giveLater 给予剩余物品是否需要延迟1tick
     * @return 是否消耗成功
     */
    @JvmStatic
    fun ItemStack.consume(
        player: Player,
        amount: Int,
        itemTag: NbtCompound,
        neigeItems: NbtCompound,
        giveLater: Boolean = false
    ): Boolean {
        val charge = neigeItems.getIntOrNull("charge")
        if (charge != null) {
            if (charge >= amount) {
                var itemClone: ItemStack? = null
                // 拆分物品
                if (this.amount != 1) {
                    itemClone = this.copy()
                    itemClone.amount -= 1
                    this.amount = 1
                }
                // 更新次数
                if (charge == amount) {
                    this.amount = 0
                } else {
                    neigeItems.putInt("charge", charge - amount)
                    itemTag.saveToSafe(this)
                }
                if (itemClone != null) {
                    if (giveLater) {
                        syncLater(1) {
                            player.giveItem(itemClone)
                        }
                    } else {
                        player.giveItem(itemClone)
                    }
                }
                return true
            }
        } else {
            if (this.amount >= amount) {
                this.amount -= amount
                return true
            }
        }
        return false
    }
}