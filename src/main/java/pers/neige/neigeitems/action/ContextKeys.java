package pers.neige.neigeitems.action;

import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.item.ItemInfo;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtCompound;

import java.util.Map;

public final class ContextKeys {
    public static final ContextKey<ItemStack> ITEM_STACK = new ContextKey<>("itemStack");
    public static final ContextKey<NbtCompound> NBT = new ContextKey<>("nbt", "itemTag");
    public static final ContextKey<ItemInfo> ITEM_INFO = new ContextKey<>("itemInfo");
    public static final ContextKey<Map<String, String>> DATA = new ContextKey<>("data");
    public static final ContextKey<Event> EVENT = new ContextKey<>("event");
    public static final ContextKey<Map<String, Object>> SECTION_CACHE = new ContextKey<>("cache");
    public static final ContextKey<ConfigReader> SECTIONS = new ContextKey<>("sections");
    public static final ContextKey<Void> PAPI_ENVIRONMENT = new ContextKey<>("papi-environment");

    private ContextKeys() {
    }
}
