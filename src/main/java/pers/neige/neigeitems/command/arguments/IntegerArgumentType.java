package pers.neige.neigeitems.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
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
    @NotNull
    public static IntegerArgumentType positiveInteger() {
        return integer(1);
    }

    /**
     * 非负整数类型
     */
    @NotNull
    public static IntegerArgumentType nonNegativeInteger() {
        return integer(0);
    }

    /**
     * 负整数类型
     */
    @NotNull
    public static IntegerArgumentType negativeInteger() {
        return integer(Integer.MIN_VALUE, -1);
    }

    /**
     * 非正整数类型
     */
    @NotNull
    public static IntegerArgumentType nonPositiveInteger() {
        return integer(Integer.MIN_VALUE, 0);
    }

    /**
     * 无限制整数类型
     */
    @NotNull
    public static IntegerArgumentType integer() {
        return integer(Integer.MIN_VALUE);
    }

    /**
     * 限制最小值整数类型
     */
    @NotNull
    public static IntegerArgumentType integer(int min) {
        return integer(min, Integer.MAX_VALUE);
    }

    /**
     * 限制最大值整数类型
     */
    @NotNull
    public static IntegerArgumentType integer(int min, int max) {
        return new IntegerArgumentType(min, max);
    }

    public static int getInteger(
            @NotNull CommandContext<CommandSender> context,
            @NotNull String name
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

    @NotNull
    @Override
    public Integer parse(
            @NotNull StringReader reader
    ) throws CommandSyntaxException {
        Integer result = CommandUtils.readInteger(reader);
        return Math.min(Math.max(result, minimum), maximum);
    }

    @NotNull
    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
