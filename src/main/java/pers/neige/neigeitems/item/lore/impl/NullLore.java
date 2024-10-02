package pers.neige.neigeitems.item.lore.impl;

import pers.neige.neigeitems.item.lore.Lore;

public class NullLore implements Lore {
    public static Lore INSTANCE = new NullLore();

    private NullLore() {
    }
}
