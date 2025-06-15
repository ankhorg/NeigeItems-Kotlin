package pers.neige.neigeitems.colonel.argument.command;

import kotlin.Unit;
import lombok.val;
import org.bukkit.command.CommandSender;
import pers.neige.neigeitems.utils.LangUtils;

import java.util.HashMap;

public class WorldArgument extends pers.neige.colonel.arguments.impl.WorldArgument<CommandSender, Unit> {
    public static final WorldArgument INSTANCE = new WorldArgument();

    private WorldArgument() {
        super();
        setNullFailExecutor((context) -> {
            val sender = context.getSource();
            if (sender == null) return;
            val params = new HashMap<String, String>();
            params.put("{world}", context.getInput().readString());
            LangUtils.sendLang(sender, "Messages.invalidWorld", params);
        });
    }
}
