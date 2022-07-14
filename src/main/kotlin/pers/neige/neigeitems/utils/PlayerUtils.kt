package pers.neige.neigeitems.utils

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.platform.util.giveItem
import kotlin.math.floor

object PlayerUtils {
    @JvmStatic
    fun Player.giveItems(itemStack: ItemStack, amount: Int?) {
        amount?.let {
            val maxStackSize = itemStack.maxStackSize
            itemStack.amount = maxStackSize
            val leftAmount = amount % maxStackSize
            val repeat = floor((amount / maxStackSize).toDouble()).toInt()
            giveItem(itemStack, repeat)
            when {
                leftAmount != 0 -> {
                    itemStack.amount = leftAmount
                    giveItem(itemStack)
                }
            }
        } ?: giveItem(itemStack)
    }
}