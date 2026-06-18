package pers.neige.neigeitems.action.evaluator.impl.list;

import lombok.NonNull;
import lombok.ToString;
import pers.neige.neigeitems.action.evaluator.EvaluatorParser;
import pers.neige.neigeitems.action.evaluator.impl.ConditionEvaluator;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;

import java.util.List;

@ToString(callSuper = true)
public class ConditionListEvaluator<T> extends ConditionEvaluator<List<T>> {
    public ConditionListEvaluator(
        @NonNull BaseActionManager manager,
        @NonNull Class<List<T>> type,
        @NonNull ConfigReader config,
        @NonNull EvaluatorParser<List<T>> parser
    ) {
        super(manager, type, config, parser);
    }
}
