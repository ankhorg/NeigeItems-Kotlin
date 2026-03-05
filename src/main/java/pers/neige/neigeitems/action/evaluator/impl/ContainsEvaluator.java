package pers.neige.neigeitems.action.evaluator.impl;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.val;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;

import java.util.HashSet;
import java.util.Set;

@Getter
@ToString(callSuper = true)
public abstract class ContainsEvaluator<T> extends Evaluator<T> {
    private final @NonNull String globalId;
    private final @NonNull Evaluator<String> key;
    private final @NonNull Evaluator<T> defaultEvaluator;
    private final @NonNull Evaluator<T> containsEvaluator;
    private final @NonNull Set<String> elements;

    public ContainsEvaluator(
        @NonNull BaseActionManager manager,
        @NonNull Class<T> type,
        @NonNull ConfigReader config
    ) {
        super(manager, type);
        this.globalId = config.getString("global-id", "key");
        this.key = Evaluator.createStringEvaluator(manager, config.get("key"));
        this.defaultEvaluator = parseEvaluator(config.get("default-evaluator"));
        this.containsEvaluator = parseEvaluator(config.get("contains-evaluator"));
        this.elements = new HashSet<>(config.getStringList("elements"));
    }

    protected abstract @NonNull Evaluator<T> parseEvaluator(@Nullable Object input);

    @Override
    @Contract("_, !null -> !null")
    public @Nullable T getOrDefault(@NonNull ActionContext context, @Nullable T def) {
        val key = this.key.get(context);
        context.getGlobal().put(globalId, key);
        if (elements.contains(key)) {
            return containsEvaluator.getOrDefault(context, def);
        } else {
            return defaultEvaluator.getOrDefault(context, def);
        }
    }
}
