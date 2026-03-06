package pers.neige.neigeitems.text.impl;

import lombok.NonNull;
import lombok.val;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.text.Text;

import java.util.List;
import java.util.function.Function;

public class OtherText extends Text {
    private final @NonNull Evaluator<String> lore;

    public OtherText(
        @NonNull BaseActionManager manager,
        @NonNull Object lore
    ) {
        super(manager);
        this.lore = Evaluator.createStringEvaluator(manager, lore);
    }

    @Override
    public <T, R extends List<T>> @NonNull R getText(
        @NonNull R result,
        @NonNull BaseActionManager manager,
        @NonNull ActionContext context,
        Function<String, T> converter
    ) {
        val text = this.lore.get(context);
        if (text == null) return result;
        val lore = converter.apply(text);
        result.add(lore);
        return result;
    }
}
