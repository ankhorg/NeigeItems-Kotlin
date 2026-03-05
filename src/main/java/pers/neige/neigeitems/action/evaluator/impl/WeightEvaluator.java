package pers.neige.neigeitems.action.evaluator.impl;

import kotlin.Pair;
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
import pers.neige.neigeitems.utils.SamplingUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@ToString(callSuper = true)
public abstract class WeightEvaluator<T> extends Evaluator<T> {
    private final @NonNull List<Pair<Evaluator<T>, Double>> evaluators = new ArrayList<>();
    private double totalWeight = 0;
    private boolean equalWeight = false;

    public WeightEvaluator(
        @NonNull BaseActionManager manager,
        @NonNull Class<T> type,
        @NonNull ConfigReader config
    ) {
        super(manager, type);
        val evaluators = config.get("evaluators");
        if (!(evaluators instanceof List<?>)) return;
        val list = (List<?>) evaluators;
        double total = 0;
        for (val rawEvaluator : list) {
            val evaluatorConfig = ConfigReader.parse(rawEvaluator);
            if (evaluatorConfig == null) continue;
            val weight = evaluatorConfig.getDouble("weight", 1);
            if (weight <= 0) continue;
            val evaluator = parseEvaluator(evaluatorConfig.get("evaluator"));
            this.evaluators.add(new Pair<>(evaluator, weight));
            total += weight;
        }
        this.totalWeight = total;
        if (!this.evaluators.isEmpty()) {
            val firstWeight = this.evaluators.get(0).getSecond();
            this.equalWeight = this.evaluators.stream().allMatch((current) -> Objects.equals(current.getSecond(), firstWeight));
        } else {
            this.equalWeight = true;
        }
    }

    protected abstract @NonNull Evaluator<T> parseEvaluator(@Nullable Object input);

    @Override
    @Contract("_, !null -> !null")
    public @Nullable T getOrDefault(@NonNull ActionContext context, @Nullable T def) {
        if (totalWeight <= 0 || evaluators.isEmpty()) return def;
        if (evaluators.size() == 1) {
            return evaluators.get(0).getFirst().getOrDefault(context, def);
        }
        Evaluator<T> evaluator;
        if (equalWeight) {
            evaluator = evaluators.get(ThreadLocalRandom.current().nextInt(0, evaluators.size())).getFirst();
        } else {
            evaluator = SamplingUtils.weight(evaluators, totalWeight);
        }
        if (evaluator == null) return def;
        return evaluator.getOrDefault(context, def);
    }
}
