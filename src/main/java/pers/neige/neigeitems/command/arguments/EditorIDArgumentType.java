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
import pers.neige.neigeitems.manager.ItemEditorManager;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * 编辑器ID参数类型
 */
public class EditorIDArgumentType implements ArgumentType<String> {
    private static final Collection<String> EXAMPLES = Arrays.asList("addLore", "setName");

    private EditorIDArgumentType() {
    }

    public static @NonNull EditorIDArgumentType editorID() {
        return new EditorIDArgumentType();
    }

    public static @NonNull String getEditorID(
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
        val lowerCaseRemaining = builder.getRemaining().toLowerCase();
        ItemEditorManager.INSTANCE.getItemEditors().keySet().forEach((key) -> {
            if (key.startsWith(lowerCaseRemaining)) {
                builder.suggest(key);
            }
        });
        return builder.buildFuture();
    }

    @Override
    public @NonNull Collection<String> getExamples() {
        return EXAMPLES;
    }
}
