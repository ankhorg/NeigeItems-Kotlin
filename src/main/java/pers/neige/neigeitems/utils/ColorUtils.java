package pers.neige.neigeitems.utils;

public class ColorUtils {
    public static String toHexColorPrefix(int rgb) {
        rgb = Math.max(0, Math.min(0xFFFFFF, rgb));
        StringBuilder result = new StringBuilder("§x");
        String hexString = Integer.toHexString(rgb);
        int zeroLength = 6 - hexString.length();
        if (zeroLength > 0) {
            for (int i = 0; i < zeroLength; i++) {
                result.append("§0");
            }
        }
        for (char c : hexString.toCharArray()) {
            result.append("§");
            result.append(c);
        }
        return result.toString();
    }

    public static String toHexColorPrefix(int r, int g, int b) {
        r = Math.max(0, Math.min(0xFF, r));
        g = Math.max(0, Math.min(0xFF, g));
        b = Math.max(0, Math.min(0xFF, b));
        StringBuilder result = new StringBuilder("§x");
        String rString = Integer.toHexString(r);
        if (rString.length() == 1) {
            result.append("§0");
        }
        for (char c : rString.toCharArray()) {
            result.append("§");
            result.append(c);
        }
        String gString = Integer.toHexString(g);
        if (gString.length() == 1) {
            result.append("§0");
        }
        for (char c : gString.toCharArray()) {
            result.append("§");
            result.append(c);
        }
        String bString = Integer.toHexString(b);
        if (bString.length() == 1) {
            result.append("§0");
        }
        for (char c : bString.toCharArray()) {
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
