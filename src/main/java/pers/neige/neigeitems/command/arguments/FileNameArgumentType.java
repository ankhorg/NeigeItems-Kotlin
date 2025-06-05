package pers.neige.neigeitems.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import lombok.NonNull;
import lombok.val;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import pers.neige.neigeitems.command.CommandUtils;
import pers.neige.neigeitems.utils.ConfigUtils;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

/**
 * 文件名参数类型
 */
public class FileNameArgumentType implements ArgumentType<String> {
    private static final Collection<String> EXAMPLES = Collections.singletonList("test.yml");

    private final @NonNull File directory;
    private final @NonNull String filePrefix;
    private final boolean greedy;

    private FileNameArgumentType(@NonNull File directory, boolean greedy) {
        this.directory = directory;
        this.filePrefix = directory.getPath() + File.separator;
        this.greedy = greedy;
    }

    public static @NonNull FileNameArgumentType fileName(@NonNull String directoryName) {
        return new FileNameArgumentType(ConfigUtils.getFile(directoryName), true);
    }

    public static @NonNull FileNameArgumentType fileName(@NonNull String directoryName, boolean greedy) {
        return new FileNameArgumentType(ConfigUtils.getFile(directoryName), greedy);
    }

    public static @NonNull FileNameArgumentType fileName(@NonNull Plugin plugin, @NonNull String directoryName) {
        return new FileNameArgumentType(ConfigUtils.getFile(plugin, directoryName), true);
    }

    public static @NonNull FileNameArgumentType fileName(@NonNull Plugin plugin, @NonNull String directoryName, boolean greedy) {
        return new FileNameArgumentType(ConfigUtils.getFile(plugin, directoryName), greedy);
    }

    public static @NonNull String getFileName(
            @NonNull CommandContext<CommandSender> context,
            @NonNull String name
    ) {
        return context.getArgument(name, String.class);
    }

    @Override
    public @NonNull String parse(
            @NonNull StringReader reader
    ) {
        if (greedy) {
            return CommandUtils.readAllString(reader);
        } else {
            return CommandUtils.readUnquotedString(reader);
        }
    }

    @Override
    public <S> @NonNull CompletableFuture<Suggestions> listSuggestions(
            @NonNull CommandContext<S> context,
            @NonNull SuggestionsBuilder builder
    ) {
        val files = ConfigUtils.getAllFiles(directory);
        val lowerCaseRemaining = builder.getRemaining().toLowerCase();
        for (val file : files) {
            val fileName = file.getPath().replace(this.filePrefix, "");
            if (fileName.toLowerCase().startsWith(lowerCaseRemaining)) {
                builder.suggest(fileName);
            }
        }
        return builder.buildFuture();
    }

    @Override
    public @NonNull Collection<String> getExamples() {
        return EXAMPLES;
    }
}
