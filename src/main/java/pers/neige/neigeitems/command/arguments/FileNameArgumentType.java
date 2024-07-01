package pers.neige.neigeitems.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.command.CommandUtils;
import pers.neige.neigeitems.utils.ConfigUtils;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 文件名参数类型
 */
public class FileNameArgumentType implements ArgumentType<String> {
    private static final Collection<String> EXAMPLES = Collections.singletonList("test.yml");

    @NotNull
    private final File directory;
    @NotNull
    private final String filePrefix;
    private final boolean greedy;

    private FileNameArgumentType(@NotNull File directory, boolean greedy) {
        this.directory = directory;
        this.filePrefix = directory.getPath() + File.separator;
        this.greedy = greedy;
    }

    @NotNull
    public static FileNameArgumentType fileName(@NotNull String directoryName) {
        return new FileNameArgumentType(ConfigUtils.getFile(directoryName), true);
    }

    @NotNull
    public static FileNameArgumentType fileName(@NotNull String directoryName, boolean greedy) {
        return new FileNameArgumentType(ConfigUtils.getFile(directoryName), greedy);
    }

    @NotNull
    public static FileNameArgumentType fileName(@NotNull Plugin plugin, @NotNull String directoryName) {
        return new FileNameArgumentType(ConfigUtils.getFile(plugin, directoryName), true);
    }

    @NotNull
    public static FileNameArgumentType fileName(@NotNull Plugin plugin, @NotNull String directoryName, boolean greedy) {
        return new FileNameArgumentType(ConfigUtils.getFile(plugin, directoryName), greedy);
    }

    @NotNull
    public static String getFileName(
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
        if (greedy) {
            return CommandUtils.readAllString(reader);
        } else {
            return CommandUtils.readUnquotedString(reader);
        }
    }

    @NotNull
    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(
            @NotNull CommandContext<S> context,
            @NotNull SuggestionsBuilder builder
    ) {
        List<File> files = ConfigUtils.getAllFiles(directory);
        for (File file : files) {
            String fileName = file.getPath().replace(this.filePrefix, "");
            if (fileName.toLowerCase().startsWith(builder.getRemaining().toLowerCase())) {
                builder.suggest(fileName);
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
