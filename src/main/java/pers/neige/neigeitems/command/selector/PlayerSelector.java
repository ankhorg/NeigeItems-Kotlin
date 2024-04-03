package pers.neige.neigeitems.command.selector;

import com.mojang.brigadier.context.CommandContext;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerSelector {
    @NotNull
    private final String name;
    private final boolean me;

    public PlayerSelector(
            @NotNull String name
    ) {
        this.name = name;
        this.me = name.equalsIgnoreCase("me");
    }

    public PlayerSelector(
            @NotNull String name,
            boolean me
    ) {
        this.name = name;
        this.me = me;
    }

    @Nullable
    public Player getPlayer(CommandContext<CommandSender> context) {
        CommandSender sender = context.getSource();
        if (me && sender instanceof Player) return (Player) sender;
        return Bukkit.getPlayerExact(name);
    }

    @NotNull
    public String getName() {
        return name;
    }

    public boolean isMe() {
        return me;
    }
}
