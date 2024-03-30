package pers.neige.neigeitems.hook.nms.impl

import org.bukkit.Material
import pers.neige.neigeitems.hook.nms.NamespacedKey
import pers.neige.neigeitems.manager.HookerManager

/**
 * 1.12.2 版本, NamespacedKey 特殊兼容
 */
class NMSHookerNamespacedKey : NMSHookerCustomModelData() {
    override fun loadNamespacedKeys(): Map<Material, NamespacedKey> {
        return HookerManager.nashornHooker.getNashornEngine().eval(
            """
                const Item = Packages.net.minecraft.server.v1_12_R1.Item
                const Material = Packages.org.bukkit.Material
                const CraftMagicNumbers = Packages.org.bukkit.craftbukkit.v1_12_R1.util.CraftMagicNumbers
                const NamespacedKey = Packages.pers.neige.neigeitems.hook.nms.NamespacedKey
                const HashMap = Packages.java.util.HashMap
            
                const REGISTRY = Item.REGISTRY;
                const materials = Material.values()
                const result = new HashMap()
                for (let index = 0; index < materials.length; index++) {
                    const material = materials[index]
                    try {
                        const item = CraftMagicNumbers.getItem(material)
                        if (item != null) {
                            const minecraftKey = REGISTRY.b(item)
                            result.put(material, new NamespacedKey(minecraftKey.b(), minecraftKey.getKey()))
                        }
                    } catch (error) {
                        error.printStackTrace()
                    }
                }
                result
            """
        ) as Map<Material, NamespacedKey>
    }
}