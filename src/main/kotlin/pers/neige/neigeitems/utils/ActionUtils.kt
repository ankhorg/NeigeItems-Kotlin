package pers.neige.neigeitems.utils

import bot.inker.bukkit.nbt.NbtCompound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.item.action.ActionTrigger
import pers.neige.neigeitems.manager.ConfigManager
import pers.neige.neigeitems.utils.ItemUtils.saveToSafe
import pers.neige.neigeitems.utils.PlayerUtils.getMetadataEZ
import pers.neige.neigeitems.utils.PlayerUtils.setMetadataEZ
import pers.neige.neigeitems.utils.SectionUtils.parseItemSection
import pers.neige.neigeitems.utils.SectionUtils.parseSection
import taboolib.platform.util.actionBar
import taboolib.platform.util.giveItem

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
     * @param itemTag 物品NBT
     * @return 是否处于冷却时间
     */
    @JvmStatic
    fun ActionTrigger.isCoolDown(
        player: Player,
        itemStack: ItemStack,
        itemTag: NbtCompound,
        data: MutableMap<String, String>?
    ): Boolean {
        val cd = cooldown?.parseItemSection(itemStack, itemTag, data, player)?.toLongOrNull() ?: 1000
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
        // 如果冷却存在且大于0
        if (cd > 0) {
            // 获取当前时间
            val time = System.currentTimeMillis()
            // 获取上次使用时间
            val lastTime = player.getMetadataEZ("NI-CD-$group", "Long", 0.toLong()) as Long
            // 如果仍处于冷却时间
            if (lastTime > time) {
                ConfigManager.config.getString("Messages.itemCooldown")?.let {
                    val message = it.replace("{time}", "%.1f".format((lastTime - time).toDouble()/1000))
                    player.actionBar(message)
                }
                // 冷却中
                return true
            }
            player.setMetadataEZ("NI-CD-$group", time + cd)
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
     * @param charge 物品剩余使用次数, 不填则主动获取
     * @return 是否消耗成功
     */
    @JvmStatic
    fun ItemStack.consume(
        player: Player,
        amount: Int,
        itemTag: NbtCompound,
        neigeItems: NbtCompound,
        charge: Int? =
            if (neigeItems.containsKey("charge"))
                neigeItems.getInt("charge")
            else
                null
    ): Boolean {
        if (charge != null) {
            if (charge >= amount) {
                var itemClone: ItemStack? = null
                // 拆分物品
                if (this.amount != 1) {
                    itemClone = this.clone()
                    itemClone.amount = itemClone.amount - 1
                    this.amount = 1
                }
                // 更新次数
                if (charge == amount) {
                    this.amount = 0
                } else {
                    neigeItems.putInt("charge", charge - amount)
                    itemTag.saveToSafe(this)
                }
                if (itemClone != null) player.giveItem(itemClone)
                return true
            }
        } else {
            if (this.amount >= amount) {
                this.amount = this.amount - amount
                return true
            }
        }
        return false
    }

    /**
     * 消耗一定数量物品, 返回操作后的物品数组
     *
     * @param amount 消耗数
     * @param itemTag 物品NBT
     * @param neigeItems NI特殊NBT
     * @return 操作后的物品数组, 消耗失败返回空值
     */
    @JvmStatic
    fun ItemStack.consumeAndReturn(
        amount: Int,
        itemTag: NbtCompound,
        neigeItems: NbtCompound
    ): Array<ItemStack>? {
        if (neigeItems.containsKey("charge")) {
            val charge = neigeItems.getInt("charge")
            if (charge >= amount) {
                var itemClone: ItemStack? = null
                // 拆分物品
                if (this.amount != 1) {
                    itemClone = this.clone()
                    itemClone.amount = itemClone.amount - 1
                    this.amount = 1
                }
                // 更新次数
                if (charge == amount) {
                    this.amount = 0
                } else {
                    neigeItems.putInt("charge", charge - amount)
                    itemTag.saveToSafe(this)
                }
                if (itemClone != null) return arrayOf(this, itemClone)
                return arrayOf(this)
            }
        } else {
            if (this.amount >= amount) {
                this.amount = this.amount - amount
                return arrayOf(this)
            }
        }
        return null
    }
}