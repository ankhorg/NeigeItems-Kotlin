package pers.neige.neigeitems.command.selector;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerSelector extends UnquotedStringSelector<Player> {
    private final boolean me;

    public PlayerSelector(@NotNull StringReader reader) {
        super(reader);
        this.me = text.equalsIgnoreCase("me");
    }

    @Nullable
    @Override
    public Player select(CommandContext<CommandSender> context) {
        CommandSender sender = context.getSource();
        if (me && sender instanceof Player) return (Player) sender;
        return Bukkit.getPlayerExact(text);
    }

    @Nullable
    public Player getPlayer(CommandContext<CommandSender> context) {
        return select(context);
    }

    @NotNull
    public String getName() {
        return text;
    }

    public boolean isMe() {
        return me;
    }
}
