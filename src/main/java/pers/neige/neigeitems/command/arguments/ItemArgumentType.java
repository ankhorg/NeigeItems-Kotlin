package pers.neige.neigeitems.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import lombok.NonNull;
import lombok.val;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.command.selector.ItemSelector;
import pers.neige.neigeitems.item.ItemGenerator;
import pers.neige.neigeitems.manager.ItemManager;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

/**
 * NI物品生成器参数类型
 */
public class ItemArgumentType implements ArgumentType<ItemSelector> {
    private static final Collection<String> EXAMPLES = Collections.singletonList("ExampleItem");

    private ItemArgumentType() {
    }

    public static @NonNull ItemArgumentType item() {
        return new ItemArgumentType();
    }

    public static @Nullable ItemGenerator getItem(
        @NonNull CommandContext<CommandSender> context,
        @NonNull String name
    ) {
        return getItemSelector(context, name).select(context);
    }

    public static @NonNull ItemSelector getItemSelector(
        @NonNull CommandContext<CommandSender> context,
        @NonNull String name
    ) {
        return context.getArgument(name, ItemSelector.class);
    }

    @Override
    public @NonNull ItemSelector parse(
        @NonNull StringReader reader
    ) {
        return new ItemSelector(reader);
    }

    @Override
    public <S> @NonNull CompletableFuture<Suggestions> listSuggestions(
        @NonNull CommandContext<S> context,
        @NonNull SuggestionsBuilder builder
    ) {
        val lowerCaseRemaining = builder.getRemaining().toLowerCase();
        ItemManager.INSTANCE.getItems().keySet().forEach((id) -> {
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
