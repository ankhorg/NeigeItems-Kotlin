package pers.neige.neigeitems.command.arguments;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.command.CommandUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * NI物品生成器参数类型
 */
public class MapArgumentType<T> implements ArgumentType<T> {
    private static final Collection<String> EXAMPLES = Collections.singletonList("key");
    @NotNull
    private final Supplier<Map<String, T>> mapGetter;
    @NotNull
    private final DynamicCommandExceptionType exceptionType;

    private MapArgumentType(@NotNull Supplier<Map<String, T>> mapGetter, @NotNull Function<String, String> nullMessageGetter) {
        this.mapGetter = mapGetter;
        this.exceptionType = new DynamicCommandExceptionType(value -> new LiteralMessage(nullMessageGetter.apply(String.valueOf(value))));
    }

    @NotNull
    public static <V> MapArgumentType<V> map(@NotNull Supplier<Map<String, V>> mapGetter, @NotNull Function<String, String> nullMessageGetter) {
        return new MapArgumentType<>(mapGetter, nullMessageGetter);
    }

    @NotNull
    public static <V> V getValue(
            @NotNull CommandContext<CommandSender> context,
            @NotNull String name
    ) {
        return (V) context.getArgument(name, Object.class);
    }

    @NotNull
    @Override
    public T parse(
            @NotNull StringReader reader
    ) throws CommandSyntaxException {
        String key = CommandUtils.readUnquotedString(reader);
        T result = mapGetter.get().get(key);
        if (result == null) {
            throw exceptionType.create(key);
        }
        return result;
    }

    @NotNull
    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(
            @NotNull CommandContext<S> context,
            @NotNull SuggestionsBuilder builder
    ) {
        String lowerCaseRemaining = builder.getRemaining().toLowerCase();
        mapGetter.get().keySet().forEach((id) -> {
            if (id.toLowerCase().startsWith(lowerCaseRemaining)) {
                builder.suggest(id);
            }
        });
        return builder.buildFuture();
    }

    @NotNull
    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
