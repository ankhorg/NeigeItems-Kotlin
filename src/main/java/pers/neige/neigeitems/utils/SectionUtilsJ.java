package pers.neige.neigeitems.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.function.Function;

public class SectionUtilsJ {
    @NotNull
    public static String parse(
            @NotNull String text,
            char head,
            char tail,
            char escape,
            Function<String, @Nullable String> transform
    ) {
        // 缓存待解析节点
        ArrayDeque<StringBuilder> stringBuilders = new ArrayDeque<>();
        // 储存解析结果
        StringBuilder result = new StringBuilder();
        char[] chars = text.toCharArray();
        // 表示前一个字符是不是转义符
        boolean lastIsEscape = false;
        // 遍历
        for (char c : chars) {
            // 当前字符是不是起始标识
            boolean isHead = c == head;
            // 当前字符是不是终止标识
            boolean isTail = c == tail;
            // 当前字符是不是转义符
            boolean isEscape = c == escape;
            // 当前为节点起始标识且前一字符不是转义符, 代表节点的起始
            if (isHead && !lastIsEscape) {
                // 添加一个新的StringBuilder, 用于缓存当前节点文本
                stringBuilders.addLast(new StringBuilder());
                // 当前为节点终止标识且前一字符不是转义符
            } else if (isTail && !lastIsEscape) {
                // 前面有节点起始标识
                if (!stringBuilders.isEmpty()) {
                    String string = stringBuilders.removeLast().toString();
                    String parseResult = transform.apply(string);
                    if (parseResult == null) parseResult = head + string + tail;
                    StringBuilder builder = stringBuilders.peekLast();
                    // 还不止一个
                    if (builder != null) {
                        // 证明这是一个嵌套节点, 解析当前内容, append进父节点的待解析文本里, 移除当前节点的缓存
                        builder.append(parseResult);
                        // 只有一个
                    } else {
                        // 证明当前节点是一个顶级节点, 解析当前内容, append进结果文本, 移除节点缓存
                        result.append(parseResult);
                    }
                } else {
                    // 因为已经检查过, 当前字符不被转义, 且不为反斜杠, 所以可以直接append
                    result.append(c);
                }
                // 字符不为节点的起始或终止
            } else {
                // 如果stringBuilders不为空, 说明当前仍在解析进行节点识别, 应将字符填入节点缓存
                // 如果stringBuilders为空, 说明当前处于顶层, 应直接填入result
                StringBuilder stringBuilder = stringBuilders.isEmpty() ? result : stringBuilders.peekLast();
                // 如果前一字符为转义符, 理应判断当前字符是否需要转义
                // 需要转义则吞掉转义符, 不需转义则将转义符视作普通反斜杠处理
                if (!isHead && !isTail && !isEscape && lastIsEscape) {
                    stringBuilder.append(escape);
                }
                // 如果当前字符不为转义符, 直接填入字符
                if (!isEscape || lastIsEscape) {
                    stringBuilder.append(c);
                }
            }
            // 进行转义符记录
            lastIsEscape = isEscape && !lastIsEscape;
        }
        // 遍历结束后stringBuilders不为空, 说明文本中存在冗余节点起始标识
        // 对于此种情况, 应将该标识符视作普通文本处理
        if (!stringBuilders.isEmpty()) {
            // 遍历stringBuilders
            for (StringBuilder stringBuilder : stringBuilders) {
                // 填入节点起始标识
                result.append(head);
                // 填入文本
                result.append(stringBuilder);
            }
        }
        return result.toString();
    }
}
