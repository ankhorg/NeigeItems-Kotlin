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
public class IntegerArgumentType implements ArgumentType<Integer> {
    private static final Collection<String> EXAMPLES = Arrays.asList("0", "123", "-123");
    private final int minimum;
    private final int maximum;

    private IntegerArgumentType(int minimum, int maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    /**
     * 正整数类型
     */
    public static @NonNull IntegerArgumentType positiveInteger() {
        return integer(1);
    }

    /**
     * 非负整数类型
     */
    public static @NonNull IntegerArgumentType nonNegativeInteger() {
        return integer(0);
    }

    /**
     * 负整数类型
     */
    public static @NonNull IntegerArgumentType negativeInteger() {
        return integer(Integer.MIN_VALUE, -1);
    }

    /**
     * 非正整数类型
     */
    public static @NonNull IntegerArgumentType nonPositiveInteger() {
        return integer(Integer.MIN_VALUE, 0);
    }

    /**
     * 无限制整数类型
     */
    public static @NonNull IntegerArgumentType integer() {
        return integer(Integer.MIN_VALUE);
    }

    /**
     * 限制最小值整数类型
     */
    public static @NonNull IntegerArgumentType integer(int min) {
        return integer(min, Integer.MAX_VALUE);
    }

    /**
     * 限制最大值整数类型
     */
    public static @NonNull IntegerArgumentType integer(int min, int max) {
        return new IntegerArgumentType(min, max);
    }

    public static int getInteger(
        @NonNull CommandContext<CommandSender> context,
        @NonNull String name
    ) {
        return context.getArgument(name, Integer.TYPE);
    }

    /**
     * 获取下限
     */
    public int getMinimum() {
        return this.minimum;
    }

    /**
     * 获取上限
     */
    public int getMaximum() {
        return this.maximum;
    }

    @Override
    public @NonNull Integer parse(
        @NonNull StringReader reader
    ) throws CommandSyntaxException {
        val result = CommandUtils.readInteger(reader);
        return Math.min(Math.max(result, minimum), maximum);
    }

    @Override
    public @NonNull Collection<String> getExamples() {
        return EXAMPLES;
    }
}
