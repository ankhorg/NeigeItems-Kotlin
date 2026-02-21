package pers.neige.neigeitems.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import lombok.NonNull;
import lombok.val;
import org.bukkit.command.CommandSender;
import pers.neige.neigeitems.command.CommandUtils;

import java.util.Arrays;
import java.util.Collection;

/**
 * 整数参数类型
 */
public class LongArgumentType implements ArgumentType<Long> {
    private static final Collection<String> EXAMPLES = Arrays.asList("0", "123", "-123");
    private final long minimum;
    private final long maximum;

    private LongArgumentType(long minimum, long maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    /**
     * 正整数类型
     */
    public static @NonNull LongArgumentType positiveLong() {
        return longValue(1);
    }

    /**
     * 非负整数类型
     */
    public static @NonNull LongArgumentType nonNegativeLong() {
        return longValue(0);
    }

    /**
     * 负整数类型
     */
    public static @NonNull LongArgumentType negativeLong() {
        return longValue(Long.MIN_VALUE, -1);
    }

    /**
     * 非正整数类型
     */
    public static @NonNull LongArgumentType nonPositiveLong() {
        return longValue(Long.MIN_VALUE, 0);
    }

    /**
     * 无限制整数类型
     */
    public static @NonNull LongArgumentType longValue() {
        return longValue(Long.MIN_VALUE);
    }

    /**
     * 限制最小值整数类型
     */
    public static @NonNull LongArgumentType longValue(long min) {
        return longValue(min, Long.MAX_VALUE);
    }

    /**
     * 限制最大值整数类型
     */
    public static @NonNull LongArgumentType longValue(long min, long max) {
        return new LongArgumentType(min, max);
    }

    public static long getLong(
        @NonNull CommandContext<CommandSender> context,
        @NonNull String name
    ) {
        return context.getArgument(name, Long.TYPE);
    }

    /**
     * 获取下限
     */
    public long getMinimum() {
        return this.minimum;
    }

    /**
     * 获取上限
     */
    public long getMaximum() {
        return this.maximum;
    }

    @Override
    public @NonNull Long parse(
        @NonNull StringReader reader
    ) throws CommandSyntaxException {
        val result = CommandUtils.readLong(reader);
        return Math.min(Math.max(result, minimum), maximum);
    }

    @Override
    public @NonNull Collection<String> getExamples() {
        return EXAMPLES;
    }
}
