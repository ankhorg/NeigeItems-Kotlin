package pers.neige.neigeitems.text.impl;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.Condition;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.text.Text;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ConditionText implements Text {
    @NotNull
    private final Condition condition;
    @NotNull
    private final Text text;
    @NotNull
    private final Text deny;

    public ConditionText(
            @NotNull BaseActionManager manager,
            @NotNull ConfigurationSection lore
    ) {
        condition = new Condition(manager, lore.getString("condition"));
        this.text = Text.compile(manager, lore.get("text"));
        deny = Text.compile(manager, lore.get("deny"));
    }

    public ConditionText(
            @NotNull BaseActionManager manager,
            @NotNull Map<?, ?> lore
    ) {
        Object value = lore.get("condition");
        condition = new Condition(manager, value == null ? null : value.toString());
        this.text = Text.compile(manager, lore.get("text"));
        deny = Text.compile(manager, lore.get("deny"));
    }

    @NotNull
    public Condition getCondition() {
        return condition;
    }

    @NotNull
    public Text getLore() {
        return text;
    }

    @NotNull
    public Text getDeny() {
        return deny;
    }

    @NotNull
    @Override
    public <T, R extends List<T>> R getText(
            @NotNull R result,
            @NotNull BaseActionManager manager,
            @NotNull ActionContext context,
            Function<String, T> converter
    ) {
        if (condition.easyCheck(context)) {
            return text.getText(result, manager, context, converter);
        } else {
            return deny.getText(result, manager, context, converter);
        }
    }
}
