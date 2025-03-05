package pers.neige.neigeitems.action.string.impl;

import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.string.StringEvaluator;
import pers.neige.neigeitems.manager.BaseActionManager;

public class RawStringEvaluator extends StringEvaluator {
    protected final @NotNull String string;

    public RawStringEvaluator(@NotNull BaseActionManager manager, @NotNull String string) {
        super(manager);
        this.string = string;
    }

    public @NotNull String getString() {
        return string;
    }

    @Override
    public @NotNull String get(@NotNull ActionContext context) {
        return string;
    }
}
