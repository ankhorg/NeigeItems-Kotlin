package pers.neige.neigeitems.colonel.argument.command;

import kotlin.Unit;
import lombok.val;
import org.bukkit.command.CommandSender;
import pers.neige.neigeitems.utils.LangUtils;

public class IntegerArgument extends pers.neige.colonel.arguments.impl.IntegerArgument<CommandSender, Unit> {
    public static final IntegerArgument POSITIVE_DEFAULT_ONE = new IntegerArgument(1, Integer.MAX_VALUE, 1);
    public static final IntegerArgument SLOT = new IntegerArgument(0, 40);

    private IntegerArgument(final int minimum, final int maximum) {
        super(minimum, maximum);
        setNullFailExecutor((context) -> {
            val sender = context.getSource();
            if (sender == null) return;
            LangUtils.sendLang(sender, "Messages.invalidAmount");
        });
    }

    private IntegerArgument(final int minimum, final int maximum, final int defaultValue) {
        super(minimum, maximum);
        setDefaultValue(defaultValue);
        setNullFailExecutor((context) -> {
            val sender = context.getSource();
            if (sender == null) return;
            LangUtils.sendLang(sender, "Messages.invalidAmount");
        });
    }
}
