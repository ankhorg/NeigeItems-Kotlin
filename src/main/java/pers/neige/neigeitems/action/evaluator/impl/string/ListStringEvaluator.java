package pers.neige.neigeitems.action.evaluator.impl.string;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.action.evaluator.impl.ListEvaluator;
import pers.neige.neigeitems.manager.BaseActionManager;

import java.util.List;

public class ListStringEvaluator extends ListEvaluator<String> {
    public ListStringEvaluator(@NonNull BaseActionManager manager, @NonNull List<?> list) {
        super(manager, String.class, list);
    }

    @Override
    protected @NonNull Evaluator<String> parseEvaluator(@Nullable Object input) {
        return Evaluator.createStringEvaluator(manager, input);
    }
}
