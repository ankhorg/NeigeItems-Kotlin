package pers.neige.neigeitems.colonel.argument.command;

import kotlin.Unit;
import lombok.NonNull;
import lombok.val;
import org.bukkit.command.CommandSender;
import pers.neige.colonel.arguments.impl.StringArgument;
import pers.neige.colonel.context.Context;
import pers.neige.neigeitems.utils.ConfigUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileNameArgument extends StringArgument<CommandSender, Unit> {
    private final @NonNull File directory;
    private final @NonNull String filePrefix;

    public FileNameArgument(@NonNull File directory) {
        this(directory, true);
    }

    public FileNameArgument(@NonNull File directory, boolean readAll) {
        super(1, Integer.MAX_VALUE, readAll);
        this.directory = directory;
        this.filePrefix = directory.getPath() + File.separator;
    }

    @Override
    public @NonNull List<String> tab(@NonNull Context<CommandSender, Unit> context, @NonNull String remaining) {
        val files = ConfigUtils.getAllFiles(directory);
        val lowerCaseRemaining = remaining.toLowerCase();
        val result = new ArrayList<String>();
        for (val file : files) {
            val fileName = file.getPath().substring(this.filePrefix.length());
            if (fileName.toLowerCase().startsWith(lowerCaseRemaining)) {
                result.add(fileName);
            }
        }
        return result;
    }
}
