package pers.neige.neigeitems.command.selector;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import lombok.NonNull;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.command.CommandUtils;

public abstract class UnquotedStringSelector<T> {
    protected final @NonNull String text;

    public UnquotedStringSelector(
        @NonNull StringReader reader
    ) {
        this.text = CommandUtils.readUnquotedString(reader);
    }

    public abstract @Nullable T select(CommandContext<CommandSender> context);

    public @NonNull String getText() {
        return text;
    }
}
