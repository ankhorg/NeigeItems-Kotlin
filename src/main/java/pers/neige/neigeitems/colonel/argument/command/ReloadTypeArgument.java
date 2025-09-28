package pers.neige.neigeitems.colonel.argument.command;

import kotlin.Unit;
import lombok.NonNull;
import lombok.val;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;
import pers.neige.colonel.arguments.Argument;
import pers.neige.colonel.arguments.ParseResult;
import pers.neige.colonel.context.Context;
import pers.neige.colonel.context.NodeChain;
import pers.neige.colonel.reader.StringReader;
import pers.neige.neigeitems.event.PluginReloadEvent;

import java.util.Collection;

public class ReloadTypeArgument extends Argument<CommandSender, PluginReloadEvent.Type, Unit> {
    public static final ReloadTypeArgument INSTANCE = new ReloadTypeArgument();

    private ReloadTypeArgument() {
        setDefaultValue(PluginReloadEvent.Type.ALL);
    }

    @Override
    @NonNull
    public ParseResult<PluginReloadEvent.Type> parse(@NonNull NodeChain<CommandSender, Unit> nodeChain, @NonNull StringReader input, @Nullable CommandSender source) {
        val name = input.readString();
        val type = PluginReloadEvent.Type.lowercaseNameToType.getOrDefault(name.toLowerCase(), PluginReloadEvent.Type.ALL);
        return new ParseResult<>(type, true);
    }

    @Override
    protected @NonNull Collection<String> rawTab(@NonNull Context<CommandSender, Unit> context, @NonNull String remaining) {
        return PluginReloadEvent.Type.lowercaseNameToType.keySet();
    }
}
