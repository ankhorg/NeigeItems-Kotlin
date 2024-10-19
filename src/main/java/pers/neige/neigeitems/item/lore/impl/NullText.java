package pers.neige.neigeitems.item.lore.impl;

import pers.neige.neigeitems.item.lore.Text;

public class NullText implements Text {
    public static Text INSTANCE = new NullText();

    private NullText() {
    }
}
