package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils;

import pers.neige.neigeitems.ref.RefMinecraftKey;
import pers.neige.neigeitems.ref.registry.RefBuiltInRegistries;

public class ComponentUtils {
    public static Object getDataComponentType(String id) {
        return RefBuiltInRegistries.DATA_COMPONENT_TYPE.getValue(RefMinecraftKey.parse(id));
    }
}
