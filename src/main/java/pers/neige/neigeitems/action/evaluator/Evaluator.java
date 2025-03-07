package pers.neige.neigeitems.action.evaluator;

import kotlin.text.StringsKt;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.evaluator.impl.dbl.JsDoubleEvaluator;
import pers.neige.neigeitems.action.evaluator.impl.dbl.ParseDoubleEvaluator;
import pers.neige.neigeitems.action.evaluator.impl.dbl.RawDoubleEvaluator;
import pers.neige.neigeitems.action.evaluator.impl.integer.JsIntegerEvaluator;
import pers.neige.neigeitems.action.evaluator.impl.integer.ParseIntegerEvaluator;
import pers.neige.neigeitems.action.evaluator.impl.integer.RawIntegerEvaluator;
import pers.neige.neigeitems.action.evaluator.impl.string.JsStringEvaluator;
import pers.neige.neigeitems.action.evaluator.impl.string.ParseStringEvaluator;
import pers.neige.neigeitems.action.evaluator.impl.string.RawStringEvaluator;
import pers.neige.neigeitems.manager.BaseActionManager;

import java.util.Locale;

public abstract class Evaluator<T> {
    protected final @NotNull BaseActionManager manager;
    protected final @NotNull Class<T> type;

    public Evaluator(@NotNull BaseActionManager manager, @NotNull Class<T> type) {
        this.manager = manager;
        this.type = type;
    }

    public static Evaluator<String> createStringEvaluator(@NotNull BaseActionManager manager, @NotNull String input) {
        String[] info = input.split(": ", 2);
        String key = info[0].toLowerCase(Locale.ROOT);
        String content = info.length > 1 ? info[1] : null;
        switch (key) {
            case "js":
                return new JsStringEvaluator(manager, content);
            case "raw":
                return new RawStringEvaluator(manager, content);
            default:
                return new ParseStringEvaluator(manager, input);
        }
    }

    public static Evaluator<Integer> createIntegerEvaluator(@NotNull BaseActionManager manager, @NotNull String input) {
        String[] info = input.split(": ", 2);
        String key = info[0].toLowerCase(Locale.ROOT);
        String content = info.length > 1 ? info[1] : null;
        switch (key) {
            case "js":
                return new JsIntegerEvaluator(manager, content);
            case "raw":
                return new RawIntegerEvaluator(manager, content);
            default:
                Integer maybe = StringsKt.toIntOrNull(input);
                if (maybe == null) {
                    return new ParseIntegerEvaluator(manager, input);
                } else {
                    return new RawIntegerEvaluator(manager, maybe);
                }
        }
    }

    public static Evaluator<Double> createDoubleEvaluator(@NotNull BaseActionManager manager, @NotNull String input) {
        String[] info = input.split(": ", 2);
        String key = info[0].toLowerCase(Locale.ROOT);
        String content = info.length > 1 ? info[1] : null;
        switch (key) {
            case "js":
                return new JsDoubleEvaluator(manager, content);
            case "raw":
                return new RawDoubleEvaluator(manager, content);
            default:
                Double maybe = StringsKt.toDoubleOrNull(input);
                if (maybe == null) {
                    return new ParseDoubleEvaluator(manager, input);
                } else {
                    return new RawDoubleEvaluator(manager, maybe);
                }
        }
    }

    @Contract("_, !null -> !null")
    public abstract @Nullable T getOrDefault(@NotNull ActionContext context, @Nullable T def);

    public @Nullable T get(@NotNull ActionContext context) {
        return getOrDefault(context, null);
    }

    public @NotNull BaseActionManager getManager() {
        return manager;
    }

    public @NotNull Class<T> getType() {
        return type;
    }
}
