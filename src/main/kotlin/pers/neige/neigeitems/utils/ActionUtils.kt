package pers.neige.neigeitems.utils

import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.item.ItemAction
import pers.neige.neigeitems.manager.ConfigManager
import pers.neige.neigeitems.utils.PlayerUtils.getMetadataEZ
import pers.neige.neigeitems.utils.PlayerUtils.setMetadataEZ
import taboolib.module.nms.ItemTag
import taboolib.module.nms.ItemTagData
import taboolib.platform.util.actionBar
import taboolib.platform.util.giveItem
import java.text.DecimalFormat
import java.util.*

object ActionUtils {
    // 通过配置信息判断玩家是否处于消耗冷却(消耗物品触发物品动作的冷却时间)
    @JvmStatic
    fun ConfigurationSection.isCoolDown(player: Player, id: String): Boolean {
        // 获取冷却
        val cooldown = this.getLong("cooldown", 0)
        // 如果冷却存在且大于0
        if (cooldown > 0) {
            // 获取当前时间
            val time = Date().time
            // 获取上次使用时间
            val lastTime = player.getMetadataEZ("NI-Consume-CD-$id", "Long", 0.toLong()) as Long
            // 如果仍处于冷却时间
            if ((lastTime + cooldown) > time) {
                ConfigManager.config.getString("Messages.itemCooldown")?.let {
                    val message = it.replace("{time}", DecimalFormat("0.#").format((lastTime + cooldown - time)/1000))
                    player.actionBar(message)
                }
                // 冷却中
                return true
            }
            player.setMetadataEZ("NI-Consume-CD-$id", time)
        }
        return false
    }

    // 通过动作信息判断玩家是否处于动作冷却(无消耗触发物品动作的冷却时间)
    @JvmStatic
    fun ItemAction.isCoolDown(player: Player): Boolean {
        // 获取冷却
        val cooldown = this.cooldown
        // 如果冷却存在且大于0
        if (cooldown > 0) {
            // 获取当前时间
            val time = Date().time
            // 获取上次使用时间
            val lastTime = player.getMetadataEZ("NI-CD-$id", "Long", 0.toLong()) as Long
            // 如果仍处于冷却时间
            if ((lastTime + cooldown) > time) {
                ConfigManager.config.getString("Messages.itemCooldown")?.let {
                    val message = it.replace("{time}", DecimalFormat("0.#").format((lastTime + cooldown - time)/1000))
                    player.actionBar(message)
                }
                // 冷却中
                return true
            }
            player.setMetadataEZ("NI-CD-$id", time)
        }
        return false
    }

    /**
     * 消耗一定数量物品
     * @param player 物品持有者, 用于接收拆分出的物品
     * @param amount 消耗数
     * @param itemTag 物品NBT
     * @param neigeItems NI特殊NBT
     * @param charge 物品使用次数NBT, 不填则主动生成
     * @return 是否消耗成功
     */
    @JvmStatic
    fun ItemStack.consume(
        player: Player,
        amount: Int,
        itemTag: ItemTag,
        neigeItems: ItemTag,
        charge: ItemTagData? = neigeItems["charge"]
    ): Boolean {
        if (charge != null) {
            // 获取剩余使用次数
            val chargeInt = charge.asInt()
            if (chargeInt >= amount) {
                var itemClone: ItemStack? = null
                // 拆分物品
                if (this.amount != 1) {
                    itemClone = this.clone()
                    itemClone.amount = itemClone.amount - 1
                    this.amount = 1
                }
                // 更新次数
                if (chargeInt == amount) {
                    this.amount = 0
                } else {
                    neigeItems["charge"] = ItemTagData(chargeInt - amount)
                    itemTag.saveTo(this)
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
     * @param amount 消耗数
     * @param itemTag 物品NBT
     * @param neigeItems NI特殊NBT
     * @param charge 物品使用次数NBT, 不填则主动生成
     * @return 操作后的物品数组, 消耗失败返回空值
     */
    @JvmStatic
    fun ItemStack.consumeAndReturn(amount: Int,
        itemTag: ItemTag,
        neigeItems: ItemTag,
        charge: ItemTagData? = neigeItems["charge"]
    ): Array<ItemStack>? {
        if (charge != null) {
            // 获取剩余使用次数
            val chargeInt = charge.asInt()
            if (chargeInt >= amount) {
                var itemClone: ItemStack? = null
                // 拆分物品
                if (this.amount != 1) {
                    itemClone = this.clone()
                    itemClone.amount = itemClone.amount - 1
                    this.amount = 1
                }
                // 更新次数
                if (chargeInt == amount) {
                    this.amount = 0
                } else {
                    neigeItems["charge"] = ItemTagData(chargeInt - amount)
                    itemTag.saveTo(this)
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