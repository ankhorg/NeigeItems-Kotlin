package pers.neige.neigeitems.action.node.impl;

import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ContextKeys;
import pers.neige.neigeitems.action.node.NodeParser;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;

import java.util.HashMap;

public class InheritParser extends NodeParser {
    public InheritParser(@NonNull BaseActionManager manager) {
        super(manager);
    }

    @Override
    public @NonNull String getId() {
        return "inherit";
    }

    @Override
    public @Nullable String parse(
        @NonNull ActionContext context,
        @NonNull ConfigReader params
    ) {
        return handle(
            context,
            getParsedValue(context, params, "template")
        );
    }

    @Override
    public @Nullable String parse(
        @NonNull ActionContext context,
        @NonNull String params
    ) {
        return handle(
            context,
            params
        );
    }

    private @Nullable String handle(
        @NonNull ActionContext context,
        @Nullable String template
    ) {
        if (template == null) {
            warning("未指定继承节点");
            return null;
        }
        val clone = context.clone();
        clone.set(ContextKeys.SECTION_CACHE, new HashMap<>());
        return this.manager.parseNodeSpec(template, clone);
    }
}
