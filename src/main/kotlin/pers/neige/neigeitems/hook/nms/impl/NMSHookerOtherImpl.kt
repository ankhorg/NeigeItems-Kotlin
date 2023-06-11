package pers.neige.neigeitems.hook.nms.impl

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Item
import org.bukkit.inventory.ItemStack
import pers.neige.neigeitems.hook.nms.NMSHooker
import java.util.function.Consumer

class NMSHookerOtherImpl : NMSHooker() {
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