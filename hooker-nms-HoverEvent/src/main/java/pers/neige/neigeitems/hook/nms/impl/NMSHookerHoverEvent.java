package pers.neige.neigeitems.hook.nms.impl;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.hook.nms.NMSHooker;
import pers.neige.neigeitems.utils.ItemUtils;

/**
 * 1.16.2- 版本, HoverEvent 特殊兼容
 */
public class NMSHookerHoverEvent extends NMSHooker {
    @Override
    @NotNull
    public HoverEvent hoverText(
            @NotNull String text
    ) {
        return new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                new TextComponent[] {new TextComponent(text)}
        );
    }

    @Override
    @NotNull
    public HoverEvent hoverItem(
            @NotNull ItemStack itemStack
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