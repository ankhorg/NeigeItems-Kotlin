package pers.neige.neigeitems.action.node.impl;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.node.NodeParser;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class StringsParser extends NodeParser {
    public StringsParser(@NonNull BaseActionManager manager) {
        super(manager);
    }

    @Override
    public @NonNull String getId() {
        return "strings";
    }

    @Override
    public @Nullable String parse(
        @NonNull ActionContext context,
        @NonNull ConfigReader params
    ) {
        return handle(
            params.getStringList("values").stream().map(it -> this.manager.parseNode(it, context)).collect(Collectors.toList())
        );
    }

    @Override
    public @Nullable String parse(
        @NonNull ActionContext context,
        @NonNull List<String> params
    ) {
        return handle(
            params
        );
    }

    private @Nullable String handle(
        @Nullable List<String> values
    ) {
        if (values == null || values.isEmpty()) return null;
        return values.get(ThreadLocalRandom.current().nextInt(values.size()));
    }
}
