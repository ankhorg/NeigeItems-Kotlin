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
import lombok.NonNull;
import lombok.val;
import org.bukkit.command.CommandSender;
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

    public static @NonNull String readUnquotedString(StringReader reader) {
        val start = reader.getCursor();
        while (reader.canRead() && reader.peek() != ' ') {
            reader.skip();
        }
        return reader.getString().substring(start, reader.getCursor());
    }

    public static @NonNull String readAllString(StringReader reader) {
        val text = reader.getRemaining();
        reader.setCursor(reader.getTotalLength());
        return text;
    }

    public static @NonNull Integer readInteger(StringReader reader) throws CommandSyntaxException {
        val start = reader.getCursor();
        val number = readUnquotedString(reader);
        val result = NumberParser.parseInteger(number);
        if (result == null) {
            reader.setCursor(start);
            throw READER_INVALID_INT.createWithContext(reader, number);
        }
        return result;
    }

    public static @NonNull Long readLong(StringReader reader) throws CommandSyntaxException {
        val start = reader.getCursor();
        val number = readUnquotedString(reader);
        val result = NumberParser.parseLong(number);
        if (result == null) {
            reader.setCursor(start);
            throw READER_INVALID_LONG.createWithContext(reader, number);
        }
        return result;
    }

    public static @NonNull Double readDouble(StringReader reader) throws CommandSyntaxException {
        val start = reader.getCursor();
        val number = readUnquotedString(reader);
        val result = NumberParser.parseDouble(number);
        if (result == null) {
            reader.setCursor(start);
            throw READER_INVALID_DOUBLE.createWithContext(reader, number);
        }
        return result;
    }

    public static @Nullable BigInteger readBigInteger(StringReader reader) {
        return NumberParser.parseBigInteger(readUnquotedString(reader));
    }

    public static @Nullable BigDecimal readBigDecimal(StringReader reader) {
        return NumberParser.parseBigDecimal(readUnquotedString(reader));
    }

    public static <S, T> RequiredArgumentBuilder<S, T> argument(String name, ArgumentType<T> type) {
        return argument(name, type, (context) -> {
            val sender = context.getSource();
            if (sender instanceof CommandSender) {
                Help.INSTANCE.help((CommandSender) sender, 1);
            }
        });
    }

    public static <S> LiteralArgumentBuilder<S> literal(String name) {
        return literal(name, (context) -> {
            val sender = context.getSource();
            if (sender instanceof CommandSender) {
                Help.INSTANCE.help((CommandSender) sender, 1);
            }
        });
    }

    public static <S, T> RequiredArgumentBuilder<S, T> argument(String name, ArgumentType<T> type, Consumer<CommandContext<S>> help) {
        val result = RequiredArgumentBuilder.<S, T>argument(name, type);
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
        val result = LiteralArgumentBuilder.<S>literal(name);
        result.executes((context) -> {
            help.accept(context);
            return 1;
        });
        return result;
    }
}
