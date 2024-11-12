package pers.neige.neigeitems.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
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

    @NotNull
    public static PlayerArgumentType player() {
        return new PlayerArgumentType();
    }

    @Nullable
    public static Player getPlayer(
            @NotNull CommandContext<CommandSender> context,
            @NotNull String name
    ) {
        return getPlayerSelector(context, name).select(context);
    }

    @NotNull
    public static PlayerSelector getPlayerSelector(
            @NotNull CommandContext<CommandSender> context,
            @NotNull String name
    ) {
        return context.getArgument(name, PlayerSelector.class);
    }

    @NotNull
    @Override
    public PlayerSelector parse(
            @NotNull StringReader reader
    ) {
        return new PlayerSelector(reader);
    }

    @NotNull
    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(
            @NotNull CommandContext<S> context,
            @NotNull SuggestionsBuilder builder
    ) {
        String lowerCaseRemaining = builder.getRemaining().toLowerCase();
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

    @NotNull
    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
