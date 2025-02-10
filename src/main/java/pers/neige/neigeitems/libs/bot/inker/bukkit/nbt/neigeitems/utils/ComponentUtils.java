package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils;

import pers.neige.neigeitems.ref.RefMinecraftKey;
import pers.neige.neigeitems.ref.core.component.RefDataComponentType;
import pers.neige.neigeitems.ref.registry.RefBuiltInRegistries;

public class ComponentUtils {
    public static Object getDataComponentType(String id) {
        return RefBuiltInRegistries.DATA_COMPONENT_TYPE.getValue(RefMinecraftKey.parse(id));
    }

    public static Object getKeyByType(Object value) {
        return RefBuiltInRegistries.DATA_COMPONENT_TYPE.getKey((RefDataComponentType<?>) value);
    }
}
