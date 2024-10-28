package pers.neige.neigeitems.text.impl;

import pers.neige.neigeitems.text.Text;

public class NullText implements Text {
    public static Text INSTANCE = new NullText();

    private NullText() {
    }
}
