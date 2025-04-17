package pers.neige.neigeitems.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberParser {
    private static final int INTEGER_POSITIVE_LIMIT = -Integer.MAX_VALUE;
    private static final int INTEGER_NEGATIVE_LIMIT = Integer.MIN_VALUE;
    private static final int INTEGER_MULTMIN = INTEGER_POSITIVE_LIMIT / 10;

    private static final long LONG_POSITIVE_LIMIT = -Long.MAX_VALUE;
    private static final long LONG_NEGATIVE_LIMIT = Long.MIN_VALUE;
    private static final long LONG_MULTMIN = LONG_POSITIVE_LIMIT / 10;

    /**
     * 判断一段文本是否为朴素的十进制整数文本
     */
    public static boolean isIntegerString(@NotNull String string) {
        if (string.isEmpty()) return false;

        int offset = 0;

        int firstChar = string.charAt(offset);
        if (firstChar == '+' || firstChar == '-') {
            offset++;
        }

        int digitOffset = offset;
        while (offset < string.length()) {
            char current = string.charAt(offset);
            if (current >= '0' && current <= '9') {
                offset++;
            } else {
                break;
            }
        }

        return offset >= string.length() && digitOffset != offset;
    }

    /**
     * 判断一段文本是否为朴素的十进制小数文本
     */
    public static boolean isDecimalString(@NotNull String string) {
        if (string.isEmpty()) return false;

        int offset = 0;

        char firstChar = string.charAt(offset);
        if (firstChar == '+' || firstChar == '-') {
            offset++;
        }

        int digitsStart = offset;
        boolean hasDot = false;

        while (offset < string.length()) {
            char current = string.charAt(offset);
            if (current >= '0' && current <= '9') {
                offset++;
            } else {
                break;
            }
        }

        if (offset < string.length() && string.charAt(offset) == '.') {
            offset++;
            hasDot = true;

            while (offset < string.length()) {
                char current = string.charAt(offset);
                if (current >= '0' && current <= '9') {
                    offset++;
                } else {
                    break;
                }
            }
        }

        return digitsStart != offset && offset >= string.length() && (!hasDot || offset != digitsStart + 1);
    }

    /**
     * 非正常格式则返回 null, 超限则返回对应侧最大值
     */
    public static @Nullable Byte parseByte(@NotNull String string) {
        Integer integer = parseInteger(string);
        if (integer == null) return null;
        if (integer < Byte.MIN_VALUE) return Byte.MIN_VALUE;
        if (integer > Byte.MAX_VALUE) return Byte.MAX_VALUE;
        return integer.byteValue();
    }

    /**
     * 非正常格式则返回 def, 超限则返回对应侧最大值
     */
    public static byte parseByte(@NotNull String string, byte def) {
        int integer = parseInteger(string, def);
        if (integer < Byte.MIN_VALUE) return Byte.MIN_VALUE;
        if (integer > Byte.MAX_VALUE) return Byte.MAX_VALUE;
        return (byte) integer;
    }

    /**
     * 非正常格式则返回 null, 超限则返回对应侧最大值
     */
    public static @Nullable Short parseShort(@NotNull String string) {
        Integer integer = parseInteger(string);
        if (integer == null) return null;
        if (integer < Short.MIN_VALUE) return Short.MIN_VALUE;
        if (integer > Short.MAX_VALUE) return Short.MAX_VALUE;
        return integer.shortValue();
    }

    /**
     * 非正常格式则返回 def, 超限则返回对应侧最大值
     */
    public static short parseShort(@NotNull String string, short def) {
        int integer = parseInteger(string, def);
        if (integer < Short.MIN_VALUE) return Short.MIN_VALUE;
        if (integer > Short.MAX_VALUE) return Short.MAX_VALUE;
        return (short) integer;
    }

    /**
     * 非正常格式则返回 null, 超限则返回对应侧最大值
     */
    public static @Nullable Integer parseInteger(@NotNull String string) {
        if (string.isEmpty()) return null;

        int offset = 0;
        boolean negative = false;

        int limit = INTEGER_POSITIVE_LIMIT;

        char firstChar = string.charAt(offset);
        if (firstChar == '+') {
            offset++;
        } else if (firstChar == '-') {
            limit = INTEGER_NEGATIVE_LIMIT;
            negative = true;
            offset++;
        }

        int result = 0;
        boolean overflow = false;
        int digitOffset = offset;

        while (offset < string.length()) {
            char current = string.charAt(offset);
            if (current >= '0' && current <= '9') {
                if (result < INTEGER_MULTMIN) {
                    result = limit;
                    overflow = true;
                    offset++;
                    break;
                }
                result *= 10;
                int digit = current - 48;
                if (result < limit + digit) {
                    result = limit;
                    overflow = true;
                    offset++;
                    break;
                }
                result -= digit;
                offset++;
            } else {
                break;
            }
        }

        if (overflow) {
            while (offset < string.length()) {
                char current = string.charAt(offset);
                if (current >= '0' && current <= '9') {
                    offset++;
                } else {
                    break;
                }
            }
        }

        if (offset < string.length() || digitOffset == offset) {
            return null;
        }

        return negative ? result : -result;
    }

    /**
     * 非正常格式则返回 def, 超限则返回对应侧最大值
     */
    public static int parseInteger(@NotNull String string, int def) {
        if (string.isEmpty()) return def;

        int offset = 0;
        boolean negative = false;

        int limit = INTEGER_POSITIVE_LIMIT;

        char firstChar = string.charAt(offset);
        if (firstChar == '+') {
            offset++;
        } else if (firstChar == '-') {
            limit = INTEGER_NEGATIVE_LIMIT;
            negative = true;
            offset++;
        }

        int result = 0;
        boolean overflow = false;
        int digitOffset = offset;

        while (offset < string.length()) {
            char current = string.charAt(offset);
            if (current >= '0' && current <= '9') {
                if (result < INTEGER_MULTMIN) {
                    result = limit;
                    overflow = true;
                    offset++;
                    break;
                }
                result *= 10;
                int digit = current - 48;
                if (result < limit + digit) {
                    result = limit;
                    overflow = true;
                    offset++;
                    break;
                }
                result -= digit;
                offset++;
            } else {
                break;
            }
        }

        if (overflow) {
            while (offset < string.length()) {
                char current = string.charAt(offset);
                if (current >= '0' && current <= '9') {
                    offset++;
                } else {
                    break;
                }
            }
        }

        if (offset < string.length() || digitOffset == offset) {
            return def;
        }

        return negative ? result : -result;
    }

    /**
     * 非正常格式则返回 null, 超限则返回对应侧最大值
     */
    public static @Nullable Long parseLong(@NotNull String string) {
        if (string.isEmpty()) return null;

        int offset = 0;
        boolean negative = false;

        long limit = LONG_POSITIVE_LIMIT;

        char firstChar = string.charAt(offset);
        if (firstChar == '+') {
            offset++;
        } else if (firstChar == '-') {
            limit = LONG_NEGATIVE_LIMIT;
            negative = true;
            offset++;
        }

        long result = 0;
        boolean overflow = false;
        int digitOffset = offset;

        while (offset < string.length()) {
            char current = string.charAt(offset);
            if (current >= '0' && current <= '9') {
                if (result < LONG_MULTMIN) {
                    result = limit;
                    overflow = true;
                    offset++;
                    break;
                }
                result *= 10;
                int digit = current - 48;
                if (result < limit + digit) {
                    result = limit;
                    overflow = true;
                    offset++;
                    break;
                }
                result -= digit;
                offset++;
            } else {
                break;
            }
        }

        if (overflow) {
            while (offset < string.length()) {
                char current = string.charAt(offset);
                if (current >= '0' && current <= '9') {
                    offset++;
                } else {
                    break;
                }
            }
        }

        if (offset < string.length() || digitOffset == offset) {
            return null;
        }

        return negative ? result : -result;
    }

    /**
     * 非正常格式则返回 def, 超限则返回对应侧最大值
     */
    public static long parseLong(@NotNull String string, long def) {
        if (string.isEmpty()) return def;

        int offset = 0;
        boolean negative = false;

        long limit = LONG_POSITIVE_LIMIT;

        char firstChar = string.charAt(offset);
        if (firstChar == '+') {
            offset++;
        } else if (firstChar == '-') {
            limit = LONG_NEGATIVE_LIMIT;
            negative = true;
            offset++;
        }

        long result = 0;
        boolean overflow = false;
        int digitOffset = offset;

        while (offset < string.length()) {
            char current = string.charAt(offset);
            if (current >= '0' && current <= '9') {
                if (result < LONG_MULTMIN) {
                    result = limit;
                    overflow = true;
                    offset++;
                    break;
                }
                result *= 10;
                int digit = current - 48;
                if (result < limit + digit) {
                    result = limit;
                    overflow = true;
                    offset++;
                    break;
                }
                result -= digit;
                offset++;
            } else {
                break;
            }
        }

        if (overflow) {
            while (offset < string.length()) {
                char current = string.charAt(offset);
                if (current >= '0' && current <= '9') {
                    offset++;
                } else {
                    break;
                }
            }
        }

        if (offset < string.length() || digitOffset == offset) {
            return def;
        }

        return negative ? result : -result;
    }

    /**
     * 非正常格式则返回 null
     */
    public static @Nullable BigInteger parseBigInteger(@NotNull String string) {
        if (!isIntegerString(string)) return null;
        return new BigInteger(string);
    }

    /**
     * 非正常格式则返回 def
     */
    public static @NotNull BigInteger parseBigInteger(@NotNull String string, @NotNull BigInteger def) {
        if (!isIntegerString(string)) return def;
        return new BigInteger(string);
    }

    /**
     * 非正常格式则返回 null
     */
    public static @Nullable Float parseFloat(@NotNull String string) {
        if (!isDecimalString(string)) return null;
        return Float.parseFloat(string);
    }

    /**
     * 非正常格式则返回 def
     */
    public static float parseFloat(@NotNull String string, float def) {
        if (!isDecimalString(string)) return def;
        return Float.parseFloat(string);
    }

    /**
     * 非正常格式则返回 null
     */
    public static @Nullable Double parseDouble(@NotNull String string) {
        if (!isDecimalString(string)) return null;
        return Double.parseDouble(string);
    }

    /**
     * 非正常格式则返回 def
     */
    public static double parseDouble(@NotNull String string, double def) {
        if (!isDecimalString(string)) return def;
        return Double.parseDouble(string);
    }

    /**
     * 非正常格式则返回 null
     */
    public static @Nullable BigDecimal parseBigDecimal(@NotNull String string) {
        if (!isDecimalString(string)) return null;
        return new BigDecimal(string);
    }

    /**
     * 非正常格式则返回 def
     */
    public static @NotNull BigDecimal parseBigDecimal(@NotNull String string, @NotNull BigDecimal def) {
        if (!isDecimalString(string)) return def;
        return new BigDecimal(string);
    }
}
