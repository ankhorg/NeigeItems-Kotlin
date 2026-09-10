package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils;

import pers.neige.neigeitems.ref.chat.RefCraftChatMessage;

final class LegacyTextSerializer {
    private LegacyTextSerializer() {
    }

    static String fromStringToJSON(String message) {
        return RefCraftChatMessage.fromStringToJSON(message);
    }

    static String fromStringToJSON(String message, boolean keepNewlines) {
        return RefCraftChatMessage.fromStringToJSON(message, keepNewlines);
    }
}
