package pers.neige.neigeitems.colonel.argument.command;

import kotlin.Unit;
import lombok.NonNull;
import lombok.val;
import org.bukkit.command.CommandSender;
import pers.neige.colonel.arguments.impl.StringArgument;
import pers.neige.colonel.context.Context;
import pers.neige.neigeitems.utils.ConfigUtils;
import pers.neige.neigeitems.utils.lazy.ThreadSafeLazy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class FileNameArgument extends StringArgument<CommandSender, Unit> {
    private final @NonNull ThreadSafeLazy<File> directory;
    private final @NonNull ThreadSafeLazy<String> filePrefix;

    public FileNameArgument(@NonNull Supplier<File> directory) {
        this(directory, true);
    }

    public FileNameArgument(@NonNull Supplier<File> directory, boolean readAll) {
        super(1, Integer.MAX_VALUE, readAll);
        this.directory = new ThreadSafeLazy<>(directory);
        this.filePrefix = new ThreadSafeLazy<>(() -> {
            return directory.get().getPath() + File.separator;
        });
    }

    @Override
    public @NonNull List<String> tab(@NonNull Context<CommandSender, Unit> context, @NonNull String remaining) {
        val files = ConfigUtils.getAllFiles(directory.get());
        val lowerCaseRemaining = remaining.toLowerCase();
        val result = new ArrayList<String>();
        for (val file : files) {
            val fileName = file.getPath().substring(this.filePrefix.get().length());
            if (fileName.toLowerCase().startsWith(lowerCaseRemaining)) {
                result.add(fileName);
            }
        }
        return result;
    }
}
