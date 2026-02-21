package pers.neige.neigeitems.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import lombok.NonNull;
import lombok.val;
import org.bukkit.command.CommandSender;
import pers.neige.neigeitems.command.CommandUtils;
import pers.neige.neigeitems.manager.HookerManager;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

/**
 * MM物品ID生成器参数类型
 */
public class MMItemIDArgumentType implements ArgumentType<String> {
    private static final Collection<String> EXAMPLES = Collections.singletonList("SkeletonKingSword");

    private MMItemIDArgumentType() {
    }

    public static @NonNull MMItemIDArgumentType mmItemID() {
        return new MMItemIDArgumentType();
    }

    public static @NonNull String getMMItemID(
        @NonNull CommandContext<CommandSender> context,
        @NonNull String name
    ) {
        return context.getArgument(name, String.class);
    }

    @Override
    public @NonNull String parse(
        @NonNull StringReader reader
    ) {
        return CommandUtils.readUnquotedString(reader);
    }

    @Override
    public <S> @NonNull CompletableFuture<Suggestions> listSuggestions(
        @NonNull CommandContext<S> context,
        @NonNull SuggestionsBuilder builder
    ) {
        val hooker = HookerManager.INSTANCE.getMythicMobsHooker();
        if (hooker != null) {
            val lowerCaseRemaining = builder.getRemaining().toLowerCase();
            hooker.getItemIds().forEach((id) -> {
                if (id.toLowerCase().startsWith(lowerCaseRemaining)) {
                    builder.suggest(id);
                }
            });
        }
        return builder.buildFuture();
    }

    @Override
    public @NonNull Collection<String> getExamples() {
        return EXAMPLES;
    }
}
