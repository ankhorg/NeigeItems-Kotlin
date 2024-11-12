package pers.neige.neigeitems.command.selector;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.command.CommandUtils;

public abstract class UnquotedStringSelector<T> {
    @NotNull
    protected final String text;

    public UnquotedStringSelector(
            @NotNull StringReader reader
    ) {
        this.text = CommandUtils.readUnquotedString(reader);
    }

    @Nullable
    public abstract T select(CommandContext<CommandSender> context);

    @NotNull
    public String getText() {
        return text;
    }
}
