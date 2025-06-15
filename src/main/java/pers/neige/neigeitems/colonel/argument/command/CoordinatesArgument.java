package pers.neige.neigeitems.colonel.argument.command;

import kotlin.Unit;
import lombok.val;
import org.bukkit.command.CommandSender;
import pers.neige.colonel.coordinates.CoordinatesContainer;
import pers.neige.neigeitems.utils.LangUtils;

import java.util.HashMap;

public class CoordinatesArgument extends pers.neige.colonel.arguments.impl.CoordinatesArgument<CommandSender, Unit> {
    public static final CoordinatesArgument INSTANCE = new CoordinatesArgument();

    private CoordinatesArgument() {
        super();
        setNullFailExecutor((context) -> {
            val sender = context.getSource();
            if (sender == null) return;
            val coordinatesContainer = context.<CoordinatesContainer>getLastArgument();
            val params = new HashMap<String, String>();
            params.put("{location}", coordinatesContainer.getText());
            LangUtils.sendLang(sender, "Messages.invalidLocation", params);
        });
    }
}
