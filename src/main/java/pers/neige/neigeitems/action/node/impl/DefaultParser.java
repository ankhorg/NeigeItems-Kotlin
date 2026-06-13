package pers.neige.neigeitems.action.node.impl;

import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.node.NodeParser;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.ListUtils;
import pers.neige.neigeitems.utils.StringUtils;

public class DefaultParser extends NodeParser {
    public DefaultParser(@NonNull BaseActionManager manager) {
        super(manager);
    }

    @Override
    public @NonNull String getId() {
        return "default";
    }

    @Override
    public @Nullable String parse(
        @NonNull ActionContext context,
        @NonNull ConfigReader params
    ) {
        return handle(
            context,
            getParsedValue(context, params, "key"),
            getParsedValue(context, params, "default")
        );
    }

    @Override
    public @Nullable String parse(
        @NonNull ActionContext context,
        @NonNull String params
    ) {
        val paramsList = StringUtils.split(params, '_', '\\', 2);
        return handle(
            context,
            paramsList.get(0),
            ListUtils.getOrNull(paramsList, 1)
        );
    }

    private @Nullable String handle(
        @NonNull ActionContext context,
        @Nullable String key,
        @Nullable String def
    ) {
        if (key == null) return def;
        this.manager.parseNodeSpec(key, context);
        return String.valueOf(context.getSectionCache().getOrDefault(key, def));
    }
}
