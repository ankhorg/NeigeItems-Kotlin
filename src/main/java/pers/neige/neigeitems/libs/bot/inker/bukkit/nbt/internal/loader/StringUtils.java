package pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.internal.loader;

import org.jetbrains.annotations.NotNull;

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

    @NotNull
    public static ArrayList<String> split(@NotNull String text, char separator, char escape) {
        // 参数文本
        final char[] chars = text.toCharArray();
        // 所有参数
        final ArrayList<String> args = new ArrayList<>();
        // 缓存当前参数
        final StringBuilder arg = new StringBuilder();

        // 表示前一个字符是不是转义符(即反斜杠)
        boolean lastIsEscape = false;

        // 遍历
        for (char c : chars) {
            // 当前字符是不是转义符
            boolean isEscape = c == escape;
            // 当前字符是不是分隔符
            boolean isSeparator = c == separator;
            // 如果当前字符是分隔符且上一个字符不是转义符
            if (isSeparator && !lastIsEscape) {
                // 参数怼回去
                args.add(arg.toString());
                arg.setLength(0);
            } else {
                // 如果前一字符为转义符, 理应判断当前字符是否需要转义
                // 需要转义则吞掉转义符, 不需转义则将转义符视作普通反斜杠处理
                if (!isEscape && !isSeparator && lastIsEscape) {
                    arg.append(escape);
                }
                // 如果当前字符不为转义符, 直接填入字符
                if (!isEscape || lastIsEscape) {
                    arg.append(c);
                }
            }
            // 进行转义符记录
            lastIsEscape = isEscape && !lastIsEscape;
        }
        // 处理最后一个参数
        // 如果最后一个字符是转义符, 应将其视作普通反斜杠处理
        if (lastIsEscape) {
            arg.append(escape);
            args.add(arg.toString());
        } else {
            args.add(arg.toString());
        }
        return args;
    }
}
