package pers.neige.neigeitems.text.impl;

import lombok.NonNull;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.Condition;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.text.Text;

import java.util.List;
import java.util.function.Function;

public class ConditionText extends Text {
    private final @NonNull Condition condition;
    private final @NonNull Text text;
    private final @NonNull Text deny;

    public ConditionText(
            @NonNull BaseActionManager manager,
            @NonNull ConfigReader lore
    ) {
        super(manager);
        condition = new Condition(manager, lore.getString("condition"));
        this.text = Text.compile(manager, lore.get("text"));
        deny = Text.compile(manager, lore.get("deny"));
    }

    public @NonNull Condition getCondition() {
        return condition;
    }

    public @NonNull Text getLore() {
        return text;
    }

    public @NonNull Text getDeny() {
        return deny;
    }

    @Override
    public <T, R extends List<T>> @NonNull R getText(
            @NonNull R result,
            @NonNull BaseActionManager manager,
            @NonNull ActionContext context,
            Function<String, T> converter
    ) {
        if (condition.easyCheck(context)) {
            return text.getText(result, manager, context, converter);
        } else {
            return deny.getText(result, manager, context, converter);
        }
    }
}
