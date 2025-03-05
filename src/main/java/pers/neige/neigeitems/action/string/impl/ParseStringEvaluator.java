package pers.neige.neigeitems.action.string.impl;

import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.SectionUtils;

import java.util.Map;

public class ParseStringEvaluator extends RawStringEvaluator {
    public ParseStringEvaluator(@NotNull BaseActionManager manager, @NotNull String string) {
        super(manager, string);
    }

    @Override
    public @NotNull String get(@NotNull ActionContext context) {
        return SectionUtils.parseSection(
                string,
                (Map<String, String>) (Object) context.getGlobal(),
                context.getPlayer(),
                manager.getSectionConfig(context)
        );
    }
}
