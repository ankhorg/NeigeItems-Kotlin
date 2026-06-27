package pers.neige.neigeitems.action.node;

import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.StringUtils;

import java.text.MessageFormat;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Level;

public abstract class NodeParser {
    protected final @NonNull BaseActionManager manager;

    public NodeParser(@NonNull BaseActionManager manager) {
        this.manager = manager;
    }

    public abstract @NonNull String getId();

    public @Nullable String parse(
        @NonNull ActionContext context,
        @NonNull ConfigReader params
    ) {
        return parse(context);
    }

    public @Nullable String parse(
        @NonNull ActionContext context,
        @NonNull List<String> params
    ) {
        return parse(context);
    }

    public @Nullable String parse(
        @NonNull ActionContext context,
        @NonNull String params
    ) {
        return parse(context, StringUtils.split(params, '_', '\\'));
    }

    public @Nullable String parse(
        @NonNull ActionContext context
    ) {
        return null;
    }

    protected @Nullable String getParsedValue(
        @NonNull ActionContext context,
        @NonNull ConfigReader params,
        @NonNull String key
    ) {
        return this.manager.parseNullableNode(params.getString(key), context);
    }

    protected <T> T parse(@Nullable String input, @NonNull T def, @NonNull Function<String, T> parser, @NonNull String errorMsg) {
        if (input == null || input.isEmpty()) return def;
        val maybeResult = parser.apply(input);
        if (maybeResult != null) {
            return maybeResult;
        }
        warning(MessageFormat.format(errorMsg, input));
        return null;
    }

    protected void warning(@NonNull String message) {
        manager.getPlugin().getLogger().warning(getId() + " 节点: " + message);
    }

    protected void warning(@NonNull Throwable thrown, @NonNull String message) {
        manager.getPlugin().getLogger().log(Level.WARNING, thrown, () -> getId() + " 节点: " + message);
    }
}
