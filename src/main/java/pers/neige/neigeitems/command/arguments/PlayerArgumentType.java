package pers.neige.neigeitems.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import lombok.NonNull;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.command.selector.PlayerSelector;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * 玩家选择器参数类型
 */
public class PlayerArgumentType implements ArgumentType<PlayerSelector> {
    private static final Collection<String> EXAMPLES = Arrays.asList("me", "Neige", "NeigeExact");

    private PlayerArgumentType() {
    }

    public static @NonNull PlayerArgumentType player() {
        return new PlayerArgumentType();
    }

    public static @Nullable Player getPlayer(
        @NonNull CommandContext<CommandSender> context,
        @NonNull String name
    ) {
        return getPlayerSelector(context, name).select(context);
    }

    public static @NonNull PlayerSelector getPlayerSelector(
        @NonNull CommandContext<CommandSender> context,
        @NonNull String name
    ) {
        return context.getArgument(name, PlayerSelector.class);
    }

    @Override
    public @NonNull PlayerSelector parse(
        @NonNull StringReader reader
    ) {
        return new PlayerSelector(reader);
    }

    @Override
    public <S> @NonNull CompletableFuture<Suggestions> listSuggestions(
        @NonNull CommandContext<S> context,
        @NonNull SuggestionsBuilder builder
    ) {
        val lowerCaseRemaining = builder.getRemaining().toLowerCase();
        if ("me".startsWith(lowerCaseRemaining)) {
            builder.suggest("me");
        }
        Bukkit.getOnlinePlayers().forEach((player) -> {
            String name = player.getName();
            if (name.toLowerCase().startsWith(lowerCaseRemaining)) {
                builder.suggest(name);
            }
        });
        return builder.buildFuture();
    }

    @Override
    public @NonNull Collection<String> getExamples() {
        return EXAMPLES;
    }
}
