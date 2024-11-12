package pers.neige.neigeitems.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
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

    @NotNull
    public static EditorIDArgumentType editorID() {
        return new EditorIDArgumentType();
    }

    @NotNull
    public static String getEditorID(
            @NotNull CommandContext<CommandSender> context,
            @NotNull String name
    ) {
        return context.getArgument(name, String.class);
    }

    @NotNull
    @Override
    public String parse(
            @NotNull StringReader reader
    ) {
        return CommandUtils.readUnquotedString(reader);
    }

    @NotNull
    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(
            @NotNull CommandContext<S> context,
            @NotNull SuggestionsBuilder builder
    ) {
        String lowerCaseRemaining = builder.getRemaining().toLowerCase();
        ItemEditorManager.INSTANCE.getItemEditors().keySet().forEach((key) -> {
            if (key.startsWith(lowerCaseRemaining)) {
                builder.suggest(key);
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
