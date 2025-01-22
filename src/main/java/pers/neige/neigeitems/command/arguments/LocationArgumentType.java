package pers.neige.neigeitems.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.command.coordinates.Coordinates;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * 坐标参数类型
 */
public class LocationArgumentType implements ArgumentType<Coordinates> {
    private static final Collection<String> EXAMPLES = Arrays.asList("0 0 0", "~ ~ ~", "^ ^ ^", "^1 ^ ^-5", "~0.5 ~1 ~-5");

    private LocationArgumentType() {
    }

    @NotNull
    public static LocationArgumentType location() {
        return new LocationArgumentType();
    }

    @NotNull
    public static Coordinates getCoordinates(CommandContext<CommandSender> context, String name) {
        return context.getArgument(name, Coordinates.class);
    }

    @Nullable
    public static Location getLocation(
            @NotNull World world,
            @NotNull CommandContext<CommandSender> context,
            @NotNull String name
    ) {
        return getLocation(world, context, name, null);
    }

    @Nullable
    public static Location getLocation(
            @NotNull World world,
            @NotNull CommandContext<CommandSender> context,
            @NotNull String name,
            @Nullable Player target
    ) {
        return getCoordinates(context, name).getLocation(world, context.getSource(), target);
    }

    @NotNull
    @Override
    public Coordinates parse(
            @NotNull StringReader reader
    ) throws CommandSyntaxException {
        return Coordinates.parse(reader);
    }

    @NotNull
    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(
            @NotNull CommandContext<S> context,
            @NotNull SuggestionsBuilder builder
    ) {
        String remaining = builder.getRemaining();
        if (remaining.isEmpty()) {
            builder.suggest("~ ~ ~");
            builder.suggest("^ ^ ^");
        } else {
            String[] args = remaining.split(" ");
            if (args.length == 1) {
                if (remaining.endsWith(" ")) {
                    if (remaining.startsWith("^")) {
                        builder.suggest("^ ^");
                    } else {
                        builder.suggest("~ ~");
                    }
                } else {
                    if (remaining.startsWith("^")) {
                        builder.suggest(remaining + " ^ ^");
                    } else {
                        builder.suggest(remaining + " ~ ~");
                    }
                }
            } else if (args.length == 2) {
                if (remaining.endsWith(" ")) {
                    if (remaining.startsWith("^")) {
                        builder.suggest("^");
                    } else {
                        builder.suggest("~");
                    }
                } else {
                    String arg1 = args[1];
                    if (remaining.startsWith("^")) {
                        builder.suggest(arg1 + " ^");
                    } else {
                        builder.suggest(arg1 + " ~");
                    }
                }
            } else if (args.length == 3) {
                builder.suggest(args[2]);
            }
        }
        return builder.buildFuture();
    }

    @NotNull
    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
