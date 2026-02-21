package pers.neige.neigeitems.hook.nms.impl;

import lombok.NonNull;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import pers.neige.neigeitems.hook.nms.NMSHooker;
import pers.neige.neigeitems.utils.ItemUtils;

/**
 * 1.16.2- 版本, HoverEvent 特殊兼容
 */
public class NMSHookerHoverEvent extends NMSHooker {
    @Override
    public @NonNull HoverEvent hoverText(
        @NonNull String text
    ) {
        return new HoverEvent(
            HoverEvent.Action.SHOW_TEXT,
            new TextComponent[]{new TextComponent(text)}
        );
    }

    @Override
    public @NonNull HoverEvent hoverItem(
        @NonNull ItemStack itemStack
    ) {
        String nbtString;
        if (itemStack.getType() == Material.AIR) {
            nbtString = "{}";
        } else {
            nbtString = ItemUtils.getNbt(itemStack).toString();
        }
        return new HoverEvent(
            HoverEvent.Action.SHOW_ITEM,
            new ComponentBuilder("{id:\"" + getNamespacedKey(itemStack.getType()).getKey() + "\",Count:" + itemStack.getAmount() + "b,tag:" + nbtString + "}").create()
        );
    }
}