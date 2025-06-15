package pers.neige.neigeitems.colonel.argument.command;

import kotlin.Unit;
import lombok.val;
import org.bukkit.command.CommandSender;
import pers.neige.neigeitems.utils.LangUtils;

import java.util.HashMap;

public class PlayerArgument extends pers.neige.colonel.arguments.impl.PlayerArgument<CommandSender, Unit> {
    public static final PlayerArgument NONNULL = new PlayerArgument(true);
    public static final PlayerArgument NULLABLE = new PlayerArgument(false);

    private PlayerArgument(boolean nonnull) {
        super(nonnull);
        setNullFailExecutor((context) -> {
            val sender = context.getSource();
            if (sender == null) return;
            val params = new HashMap<String, String>();
            params.put("{player}", context.getInput().readString());
            LangUtils.sendLang(sender, "Messages.invalidPlayer", params);
        });
    }
}
