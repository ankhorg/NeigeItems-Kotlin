package pers.neige.neigeitems.action.evaluator.impl.list;

import lombok.NonNull;
import lombok.ToString;
import pers.neige.neigeitems.action.evaluator.EvaluatorParser;
import pers.neige.neigeitems.action.evaluator.impl.ConditionWeightEvaluator;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;

import java.util.List;

@ToString(callSuper = true)
public class ConditionWeightListEvaluator<T> extends ConditionWeightEvaluator<List<T>> {
    public ConditionWeightListEvaluator(
        @NonNull BaseActionManager manager,
        @NonNull Class<List<T>> type,
        @NonNull ConfigReader config,
        @NonNull EvaluatorParser<List<T>> parser
    ) {
        super(manager, type, config, parser);
    }
}
