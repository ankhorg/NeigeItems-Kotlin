package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.loader;

import java.util.ArrayList;
import java.util.List;

public final class StringUtils {
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    private StringUtils() {
        throw new UnsupportedOperationException();
    }

    public static String[] split(String str, char separatorChar) {
        // Performance tuned for 2.0 (JDK1.4)

        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len == 0) {
            return EMPTY_STRING_ARRAY;
        }
        List<String> list = new ArrayList<>();
        int i = 0, start = 0;
        boolean match = false;
        while (i < len) {
            if (str.charAt(i) == separatorChar) {
                if (match) {
                    list.add(str.substring(start, i));
                    match = false;
                }
                start = ++i;
                continue;
            }
            match = true;
            i++;
        }
        if (match) {
            list.add(str.substring(start, i));
        }
        return list.toArray(EMPTY_STRING_ARRAY);
    }
}
