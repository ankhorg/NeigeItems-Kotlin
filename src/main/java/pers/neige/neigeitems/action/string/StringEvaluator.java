package pers.neige.neigeitems.action.string;

import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.string.impl.JsStringEvaluator;
import pers.neige.neigeitems.action.string.impl.ParseStringEvaluator;
import pers.neige.neigeitems.action.string.impl.RawStringEvaluator;
import pers.neige.neigeitems.manager.BaseActionManager;

import java.util.Locale;

public abstract class StringEvaluator {
    protected final @NotNull BaseActionManager manager;

    public StringEvaluator(@NotNull BaseActionManager manager) {
        this.manager = manager;
    }

    public abstract @NotNull String get(@NotNull ActionContext context);

    public static StringEvaluator compile(@NotNull BaseActionManager manager, @NotNull String input) {
        String[] info = input.split(": ", 2);
        String key = info[0].toLowerCase(Locale.ROOT);
        String content = info.length > 1 ? info[1] : "";
        switch (key) {
            case "js":
                return new JsStringEvaluator(manager, content);
            case "raw":
                return new RawStringEvaluator(manager, content);
            default:
                return new ParseStringEvaluator(manager, input);
        }
    }
}
