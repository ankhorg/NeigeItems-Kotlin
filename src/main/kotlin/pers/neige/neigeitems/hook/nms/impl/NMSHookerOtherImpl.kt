package pers.neige.neigeitems.hook.nms.impl

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Item
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import pers.neige.neigeitems.hook.nms.NMSHooker
import java.util.function.Consumer

class NMSHookerOtherImpl : NMSHooker() {
    override fun setCustomModelData(itemMeta: ItemMeta?, data: Int) {
        itemMeta?.setCustomModelData(data)
    }

    override fun dropItem(
        world: World,
        location: Location,
        itemStack: ItemStack,
        function: Consumer<Item>
    ): Item {
        return world.dropItem(location, itemStack) {
            function.accept(it)
        }
    }
}