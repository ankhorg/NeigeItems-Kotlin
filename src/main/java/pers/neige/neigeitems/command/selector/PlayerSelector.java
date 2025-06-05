package pers.neige.neigeitems.command.selector;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import lombok.NonNull;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class PlayerSelector extends UnquotedStringSelector<Player> {
    private final boolean me;

    public PlayerSelector(@NonNull StringReader reader) {
        super(reader);
        this.me = text.equalsIgnoreCase("me");
    }

    @Override
    public @Nullable Player select(CommandContext<CommandSender> context) {
        val sender = context.getSource();
        if (me && sender instanceof Player) return (Player) sender;
        return Bukkit.getPlayerExact(text);
    }

    public @Nullable Player getPlayer(CommandContext<CommandSender> context) {
        return select(context);
    }

    public @NonNull String getName() {
        return text;
    }

    public boolean isMe() {
        return me;
    }
}
