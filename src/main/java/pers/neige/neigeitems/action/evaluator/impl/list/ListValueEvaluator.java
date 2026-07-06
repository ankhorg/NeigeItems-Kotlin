package pers.neige.neigeitems.action.evaluator.impl.list;

import lombok.NonNull;
import lombok.ToString;
import lombok.val;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.action.evaluator.EvaluatorParser;
import pers.neige.neigeitems.manager.BaseActionManager;

import java.util.ArrayList;
import java.util.List;

@ToString(callSuper = true)
public class ListValueEvaluator<T> extends Evaluator<List<T>> {
    private final @NonNull List<Evaluator<List<T>>> evaluators = new ArrayList<>();

    public ListValueEvaluator(
        @NonNull BaseActionManager manager,
        @NonNull Class<List<T>> type,
        @NonNull List<?> list,
        @NonNull EvaluatorParser<List<T>> parser
    ) {
        super(manager, type);
        for (val element : list) {
            evaluators.add(parser.parse(element));
        }
    }

    @Override
    @Contract("_, !null -> !null")
    public @Nullable List<T> getOrDefault(@NonNull ActionContext context, @Nullable List<T> def) {
        val result = new ArrayList<T>();
        for (val evaluator : evaluators) {
            val value = evaluator.get(context);
            if (value == null) continue;
            result.addAll(value);
        }
        return result;
    }
}
