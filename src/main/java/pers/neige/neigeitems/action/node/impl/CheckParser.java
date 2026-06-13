package pers.neige.neigeitems.action.node.impl;

import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ContextKeys;
import pers.neige.neigeitems.action.node.NodeParser;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.ActionManager;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.SchedulerUtils;

import java.util.HashMap;

public class CheckParser extends NodeParser {
    public CheckParser(@NonNull BaseActionManager manager) {
        super(manager);
    }

    @Override
    public @NonNull String getId() {
        return "check";
    }

    @Override
    public @Nullable String parse(
        @NonNull ActionContext context,
        @NonNull ConfigReader params
    ) {
        return handle(
            context,
            getParsedValue(context, params, "value"),
            params.get("actions")
        );
    }

    private @Nullable String handle(
        @NonNull ActionContext context,
        @Nullable String value,
        @Nullable Object actions
    ) {
        val player = context.getPlayer();
        if (player == null) return value;
        SchedulerUtils.async(() -> {
            val params = new HashMap<String, Object>();
            params.put("value", value);
            params.put("cache", context.getSectionCache());
            params.put("sections", context.get(ContextKeys.SECTIONS));
            ActionManager.INSTANCE.compile(actions).eval(new ActionContext(player, params, params));
        });
        return value;
    }
}
