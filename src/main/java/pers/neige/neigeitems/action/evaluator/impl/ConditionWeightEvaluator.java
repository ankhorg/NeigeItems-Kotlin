package pers.neige.neigeitems.action.evaluator.impl;

import kotlin.Pair;
import lombok.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.Condition;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.SamplingUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString(callSuper = true)
public abstract class ConditionWeightEvaluator<T> extends Evaluator<T> {
    private final @NonNull List<ConditionalWeightedEvaluator<T>> evaluators = new ArrayList<>();

    public ConditionWeightEvaluator(
        @NonNull BaseActionManager manager,
        @NonNull Class<T> type,
        @NonNull ConfigReader config
    ) {
        super(manager, type);
        val evaluators = config.get("evaluators");
        if (!(evaluators instanceof List<?>)) return;
        val list = (List<?>) evaluators;
        for (val rawEvaluator : list) {
            val evaluatorConfig = ConfigReader.parse(rawEvaluator);
            if (evaluatorConfig == null) continue;
            val evaluator = parseEvaluator(evaluatorConfig.get("evaluator"));
            val condition = new Condition(manager, config.getString("condition"));
            this.evaluators.add(
                new ConditionalWeightedEvaluator<>(
                    condition,
                    evaluator,
                    Evaluator.createDoubleEvaluator(manager, evaluatorConfig.get("weight"))
                )
            );
        }
    }

    protected abstract @NonNull Evaluator<T> parseEvaluator(@Nullable Object input);

    @Override
    @Contract("_, !null -> !null")
    public @Nullable T getOrDefault(@NonNull ActionContext context, @Nullable T def) {
        val evaluators = new ArrayList<Pair<Evaluator<T>, Double>>();
        for (val evaluator : this.evaluators) {
            if (!evaluator.condition.easyCheck(context)) continue;
            val weight = evaluator.weightEvaluator.getOrDefault(context, 1D);
            if (weight <= 0) continue;
            evaluators.add(new Pair<>(evaluator.evaluator, weight));
        }
        if (evaluators.isEmpty()) return def;
        if (evaluators.size() == 1) {
            return evaluators.get(0).getFirst().getOrDefault(context, def);
        }
        val evaluator = SamplingUtils.weight(evaluators);
        if (evaluator == null) return def;
        return evaluator.getOrDefault(context, def);
    }

    @AllArgsConstructor
    private static class ConditionalWeightedEvaluator<T> {
        private final @NonNull Condition condition;
        private final @NonNull Evaluator<T> evaluator;
        private final @NonNull Evaluator<Double> weightEvaluator;
    }
}
