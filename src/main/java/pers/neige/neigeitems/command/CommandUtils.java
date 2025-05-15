package pers.neige.neigeitems.command;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.command.subcommand.Help;
import pers.neige.neigeitems.utils.NumberParser;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Consumer;

public class CommandUtils {
    private static final DynamicCommandExceptionType READER_INVALID_INT = new DynamicCommandExceptionType(value -> new LiteralMessage("'" + value + "' 不是一个有效的整数"));
    private static final DynamicCommandExceptionType READER_INVALID_LONG = new DynamicCommandExceptionType(value -> new LiteralMessage("'" + value + "' 不是一个有效的长整数"));
    private static final DynamicCommandExceptionType READER_INVALID_DOUBLE = new DynamicCommandExceptionType(value -> new LiteralMessage("'" + value + "' 不是一个有效的双精度浮点数"));

    @NotNull
    public static String readUnquotedString(StringReader reader) {
        int start = reader.getCursor();
        while (reader.canRead() && reader.peek() != ' ') {
            reader.skip();
        }
        return reader.getString().substring(start, reader.getCursor());
    }

    @NotNull
    public static String readAllString(StringReader reader) {
        String text = reader.getRemaining();
        reader.setCursor(reader.getTotalLength());
        return text;
    }

    @NotNull
    public static Integer readInteger(StringReader reader) throws CommandSyntaxException {
        final int start = reader.getCursor();
        String number = readUnquotedString(reader);
        Integer result = NumberParser.parseInteger(number);
        if (result == null) {
            reader.setCursor(start);
            throw READER_INVALID_INT.createWithContext(reader, number);
        }
        return result;
    }

    @NotNull
    public static Long readLong(StringReader reader) throws CommandSyntaxException {
        final int start = reader.getCursor();
        String number = readUnquotedString(reader);
        Long result = NumberParser.parseLong(number);
        if (result == null) {
            reader.setCursor(start);
            throw READER_INVALID_LONG.createWithContext(reader, number);
        }
        return result;
    }

    @NotNull
    public static Double readDouble(StringReader reader) throws CommandSyntaxException {
        final int start = reader.getCursor();
        String number = readUnquotedString(reader);
        Double result = NumberParser.parseDouble(number);
        if (result == null) {
            reader.setCursor(start);
            throw READER_INVALID_DOUBLE.createWithContext(reader, number);
        }
        return result;
    }

    @Nullable
    public static BigInteger readBigInteger(StringReader reader) {
        return NumberParser.parseBigInteger(readUnquotedString(reader));
    }

    @Nullable
    public static BigDecimal readBigDecimal(StringReader reader) {
        return NumberParser.parseBigDecimal(readUnquotedString(reader));
    }

    public static <S, T> RequiredArgumentBuilder<S, T> argument(String name, ArgumentType<T> type) {
        return argument(name, type, (context) -> {
            Object sender = context.getSource();
            if (sender instanceof CommandSender) {
                Help.INSTANCE.help((CommandSender) sender, 1);
            }
        });
    }

    public static <S> LiteralArgumentBuilder<S> literal(String name) {
        return literal(name, (context) -> {
            Object sender = context.getSource();
            if (sender instanceof CommandSender) {
                Help.INSTANCE.help((CommandSender) sender, 1);
            }
        });
    }

    public static <S, T> RequiredArgumentBuilder<S, T> argument(String name, ArgumentType<T> type, Consumer<CommandContext<S>> help) {
        RequiredArgumentBuilder<S, T> result = RequiredArgumentBuilder.argument(name, type);
        try {
            type.getClass().getDeclaredMethod("listSuggestions", CommandContext.class, SuggestionsBuilder.class);
        } catch (NoSuchMethodException e) {
            result.suggests((context, builder) -> {
                if (builder.getRemaining().isEmpty()) {
                    builder.suggest(name);
                }
                return builder.buildFuture();
            });
        }
        result.executes((context) -> {
            help.accept(context);
            return 1;
        });
        return result;
    }

    public static <S> LiteralArgumentBuilder<S> literal(String name, Consumer<CommandContext<S>> help) {
        LiteralArgumentBuilder<S> result = LiteralArgumentBuilder.literal(name);
        result.executes((context) -> {
            help.accept(context);
            return 1;
        });
        return result;
    }
}
