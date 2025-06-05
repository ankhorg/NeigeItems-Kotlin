package pers.neige.neigeitems.utils;

import lombok.NonNull;
import lombok.val;
import lombok.var;
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
    public static boolean isIntegerString(@NonNull String string) {
        if (string.isEmpty()) return false;

        var offset = 0;

        val firstChar = string.charAt(offset);
        if (firstChar == '+' || firstChar == '-') {
            offset++;
        }

        val digitOffset = offset;
        while (offset < string.length()) {
            val current = string.charAt(offset);
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
    public static boolean isDecimalString(@NonNull String string) {
        if (string.isEmpty()) return false;

        var offset = 0;

        val firstChar = string.charAt(offset);
        if (firstChar == '+' || firstChar == '-') {
            offset++;
        }

        val digitsStart = offset;
        var hasDot = false;

        while (offset < string.length()) {
            val current = string.charAt(offset);
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
                val current = string.charAt(offset);
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
    public static @Nullable Byte parseByte(@NonNull String string) {
        val integer = parseInteger(string);
        if (integer == null) return null;
        if (integer < Byte.MIN_VALUE) return Byte.MIN_VALUE;
        if (integer > Byte.MAX_VALUE) return Byte.MAX_VALUE;
        return integer.byteValue();
    }

    /**
     * 非正常格式则返回 def, 超限则返回对应侧最大值
     */
    public static byte parseByte(@NonNull String string, byte def) {
        val integer = parseInteger(string, def);
        if (integer < Byte.MIN_VALUE) return Byte.MIN_VALUE;
        if (integer > Byte.MAX_VALUE) return Byte.MAX_VALUE;
        return (byte) integer;
    }

    /**
     * 非正常格式则返回 null, 超限则返回对应侧最大值
     */
    public static @Nullable Short parseShort(@NonNull String string) {
        val integer = parseInteger(string);
        if (integer == null) return null;
        if (integer < Short.MIN_VALUE) return Short.MIN_VALUE;
        if (integer > Short.MAX_VALUE) return Short.MAX_VALUE;
        return integer.shortValue();
    }

    /**
     * 非正常格式则返回 def, 超限则返回对应侧最大值
     */
    public static short parseShort(@NonNull String string, short def) {
        val integer = parseInteger(string, def);
        if (integer < Short.MIN_VALUE) return Short.MIN_VALUE;
        if (integer > Short.MAX_VALUE) return Short.MAX_VALUE;
        return (short) integer;
    }

    /**
     * 非正常格式则返回 null, 超限则返回对应侧最大值
     */
    public static @Nullable Integer parseInteger(@NonNull String string) {
        if (string.isEmpty()) return null;

        var offset = 0;
        var negative = false;

        var limit = INTEGER_POSITIVE_LIMIT;

        val firstChar = string.charAt(offset);
        if (firstChar == '+') {
            offset++;
        } else if (firstChar == '-') {
            limit = INTEGER_NEGATIVE_LIMIT;
            negative = true;
            offset++;
        }

        var result = 0;
        var overflow = false;
        val digitOffset = offset;

        while (offset < string.length()) {
            val current = string.charAt(offset);
            if (current >= '0' && current <= '9') {
                if (result < INTEGER_MULTMIN) {
                    result = limit;
                    overflow = true;
                    offset++;
                    break;
                }
                result *= 10;
                val digit = current - 48;
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
                val current = string.charAt(offset);
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
    public static int parseInteger(@NonNull String string, int def) {
        if (string.isEmpty()) return def;

        var offset = 0;
        var negative = false;

        var limit = INTEGER_POSITIVE_LIMIT;

        val firstChar = string.charAt(offset);
        if (firstChar == '+') {
            offset++;
        } else if (firstChar == '-') {
            limit = INTEGER_NEGATIVE_LIMIT;
            negative = true;
            offset++;
        }

        var result = 0;
        var overflow = false;
        val digitOffset = offset;

        while (offset < string.length()) {
            val current = string.charAt(offset);
            if (current >= '0' && current <= '9') {
                if (result < INTEGER_MULTMIN) {
                    result = limit;
                    overflow = true;
                    offset++;
                    break;
                }
                result *= 10;
                val digit = current - 48;
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
                val current = string.charAt(offset);
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
    public static @Nullable Long parseLong(@NonNull String string) {
        if (string.isEmpty()) return null;

        var offset = 0;
        var negative = false;

        var limit = LONG_POSITIVE_LIMIT;

        val firstChar = string.charAt(offset);
        if (firstChar == '+') {
            offset++;
        } else if (firstChar == '-') {
            limit = LONG_NEGATIVE_LIMIT;
            negative = true;
            offset++;
        }

        var result = 0L;
        var overflow = false;
        val digitOffset = offset;

        while (offset < string.length()) {
            val current = string.charAt(offset);
            if (current >= '0' && current <= '9') {
                if (result < LONG_MULTMIN) {
                    result = limit;
                    overflow = true;
                    offset++;
                    break;
                }
                result *= 10;
                val digit = current - 48;
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
                val current = string.charAt(offset);
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
    public static long parseLong(@NonNull String string, long def) {
        if (string.isEmpty()) return def;

        var offset = 0;
        var negative = false;

        var limit = LONG_POSITIVE_LIMIT;

        val firstChar = string.charAt(offset);
        if (firstChar == '+') {
            offset++;
        } else if (firstChar == '-') {
            limit = LONG_NEGATIVE_LIMIT;
            negative = true;
            offset++;
        }

        var result = 0L;
        var overflow = false;
        val digitOffset = offset;

        while (offset < string.length()) {
            val current = string.charAt(offset);
            if (current >= '0' && current <= '9') {
                if (result < LONG_MULTMIN) {
                    result = limit;
                    overflow = true;
                    offset++;
                    break;
                }
                result *= 10;
                val digit = current - 48;
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
                val current = string.charAt(offset);
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
    public static @Nullable BigInteger parseBigInteger(@NonNull String string) {
        if (!isIntegerString(string)) return null;
        return new BigInteger(string);
    }

    /**
     * 非正常格式则返回 def
     */
    public static @NonNull BigInteger parseBigInteger(@NonNull String string, @NonNull BigInteger def) {
        if (!isIntegerString(string)) return def;
        return new BigInteger(string);
    }

    /**
     * 非正常格式则返回 null
     */
    public static @Nullable Float parseFloat(@NonNull String string) {
        if (!isDecimalString(string)) return null;
        return Float.parseFloat(string);
    }

    /**
     * 非正常格式则返回 def
     */
    public static float parseFloat(@NonNull String string, float def) {
        if (!isDecimalString(string)) return def;
        return Float.parseFloat(string);
    }

    /**
     * 非正常格式则返回 null
     */
    public static @Nullable Double parseDouble(@NonNull String string) {
        if (!isDecimalString(string)) return null;
        return Double.parseDouble(string);
    }

    /**
     * 非正常格式则返回 def
     */
    public static double parseDouble(@NonNull String string, double def) {
        if (!isDecimalString(string)) return def;
        return Double.parseDouble(string);
    }

    /**
     * 非正常格式则返回 null
     */
    public static @Nullable BigDecimal parseBigDecimal(@NonNull String string) {
        if (!isDecimalString(string)) return null;
        return new BigDecimal(string);
    }

    /**
     * 非正常格式则返回 def
     */
    public static @NonNull BigDecimal parseBigDecimal(@NonNull String string, @NonNull BigDecimal def) {
        if (!isDecimalString(string)) return def;
        return new BigDecimal(string);
    }
}
