package pers.neige.neigeitems.utils;

import lombok.val;

public class ColorUtils {
    public static String toHexColorPrefix(int rgb) {
        rgb = Math.max(0, Math.min(0xFFFFFF, rgb));
        val result = new StringBuilder("§x");
        val hexString = Integer.toHexString(rgb);
        val zeroLength = 6 - hexString.length();
        if (zeroLength > 0) {
            for (int i = 0; i < zeroLength; i++) {
                result.append("§0");
            }
        }
        for (val c : hexString.toCharArray()) {
            result.append("§");
            result.append(c);
        }
        return result.toString();
    }

    public static String toHexColorPrefix(int r, int g, int b) {
        r = Math.max(0, Math.min(0xFF, r));
        g = Math.max(0, Math.min(0xFF, g));
        b = Math.max(0, Math.min(0xFF, b));
        val result = new StringBuilder("§x");
        val rString = Integer.toHexString(r);
        if (rString.length() == 1) {
            result.append("§0");
        }
        for (val c : rString.toCharArray()) {
            result.append("§");
            result.append(c);
        }
        val gString = Integer.toHexString(g);
        if (gString.length() == 1) {
            result.append("§0");
        }
        for (val c : gString.toCharArray()) {
            result.append("§");
            result.append(c);
        }
        val bString = Integer.toHexString(b);
        if (bString.length() == 1) {
            result.append("§0");
        }
        for (val c : bString.toCharArray()) {
            result.append("§");
            result.append(c);
        }
        return result.toString();
    }

    public static int getRed(int rgb) {
        return (rgb >> 16) & 0xFF;
    }

    public static int getGreen(int rgb) {
        return (rgb >> 8) & 0xFF;
    }

    public static int getBlue(int rgb) {
        return rgb & 0xFF;
    }
}
