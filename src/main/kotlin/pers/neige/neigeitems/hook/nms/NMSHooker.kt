package pers.neige.neigeitems.hook.nms

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Item
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.util.function.Consumer

abstract class NMSHooker{
    open fun hasCustomModelData(itemMeta: ItemMeta?): Boolean {
        return itemMeta?.hasCustomModelData() ?: false
    }

    open fun getCustomModelData(itemMeta: ItemMeta?): Int? {
        return itemMeta?.customModelData
    }

    open fun setCustomModelData(itemMeta: ItemMeta?, data: Int) {
        itemMeta?.setCustomModelData(data)
    }

    abstract fun dropItem(
        world: World,
        location: Location,
        itemStack: ItemStack,
        function: Consumer<Item>
    ): Item
}