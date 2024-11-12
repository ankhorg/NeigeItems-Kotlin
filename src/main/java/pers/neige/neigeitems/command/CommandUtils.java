package pers.neige.neigeitems.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import kotlin.text.StringsKt;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.command.subcommand.Help;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Consumer;

public class CommandUtils {
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

    @Nullable
    public static Integer readInteger(StringReader reader) {
        return StringsKt.toIntOrNull(readUnquotedString(reader));
    }

    @Nullable
    public static Long readLong(StringReader reader) {
        return StringsKt.toLongOrNull(readUnquotedString(reader));
    }

    @Nullable
    public static Double readDouble(StringReader reader) {
        return StringsKt.toDoubleOrNull(readUnquotedString(reader));
    }

    @Nullable
    public static BigInteger readBigInteger(StringReader reader) {
        return StringsKt.toBigIntegerOrNull(readUnquotedString(reader));
    }

    @Nullable
    public static BigDecimal readBigDecimal(StringReader reader) {
        return StringsKt.toBigDecimalOrNull(readUnquotedString(reader));
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
