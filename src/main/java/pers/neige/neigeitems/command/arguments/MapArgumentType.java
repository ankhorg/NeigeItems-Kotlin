package pers.neige.neigeitems.command.arguments;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import lombok.NonNull;
import lombok.val;
import org.bukkit.command.CommandSender;
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
    private final @NonNull Supplier<Map<String, T>> mapGetter;
    private final @NonNull DynamicCommandExceptionType exceptionType;

    private MapArgumentType(@NonNull Supplier<Map<String, T>> mapGetter, @NonNull Function<String, String> nullMessageGetter) {
        this.mapGetter = mapGetter;
        this.exceptionType = new DynamicCommandExceptionType(value -> new LiteralMessage(nullMessageGetter.apply(String.valueOf(value))));
    }

    public static @NonNull <V> MapArgumentType<V> map(@NonNull Supplier<Map<String, V>> mapGetter, @NonNull Function<String, String> nullMessageGetter) {
        return new MapArgumentType<>(mapGetter, nullMessageGetter);
    }

    public static @NonNull <V> V getValue(
        @NonNull CommandContext<CommandSender> context,
        @NonNull String name
    ) {
        return (V) context.getArgument(name, Object.class);
    }

    @Override
    public @NonNull T parse(
        @NonNull StringReader reader
    ) throws CommandSyntaxException {
        val key = CommandUtils.readUnquotedString(reader);
        T result = mapGetter.get().get(key);
        if (result == null) {
            throw exceptionType.create(key);
        }
        return result;
    }

    @Override
    public <S> @NonNull CompletableFuture<Suggestions> listSuggestions(
        @NonNull CommandContext<S> context,
        @NonNull SuggestionsBuilder builder
    ) {
        val lowerCaseRemaining = builder.getRemaining().toLowerCase();
        mapGetter.get().keySet().forEach((id) -> {
            if (id.toLowerCase().startsWith(lowerCaseRemaining)) {
                builder.suggest(id);
            }
        });
        return builder.buildFuture();
    }

    @Override
    public @NonNull Collection<String> getExamples() {
        return EXAMPLES;
    }
}
