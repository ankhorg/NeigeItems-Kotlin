package pers.neige.neigeitems.item.lore.impl;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ResultType;
import pers.neige.neigeitems.item.lore.Lore;
import pers.neige.neigeitems.manager.ActionManager;
import pers.neige.neigeitems.manager.BaseActionManager;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ConditionLore implements Lore {
    @Nullable
    private final String conditionString;
    @Nullable
    private final CompiledScript condition;
    @NotNull
    private final Lore lore;
    @NotNull
    private final Lore deny;

    public ConditionLore(
            @NotNull BaseActionManager manager,
            @NotNull ConfigurationSection lore
    ) {
        conditionString = lore.getString("condition");
        if (conditionString != null) {
            try {
                condition = ((Compilable) manager.getEngine()).compile(conditionString);
            } catch (ScriptException e) {
                throw new RuntimeException(e);
            }
        } else {
            condition = null;
        }
        this.lore = Lore.compile(manager, lore.get("lore"));
        deny = Lore.compile(manager, lore.get("deny"));
    }

    public ConditionLore(
            @NotNull BaseActionManager manager,
            @NotNull Map<?, ?> lore
    ) {
        Object value = lore.get("condition");
        if (value instanceof String) {
            conditionString = (String) value;
            try {
                condition = ((Compilable) manager.getEngine()).compile(conditionString);
            } catch (ScriptException e) {
                throw new RuntimeException(e);
            }
        } else {
            conditionString = null;
            condition = null;
        }
        this.lore = Lore.compile(manager, lore.get("lore"));
        deny = Lore.compile(manager, lore.get("deny"));
    }

    public @Nullable String getConditionString() {
        return conditionString;
    }

    public @Nullable CompiledScript getCondition() {
        return condition;
    }

    @NotNull
    public Lore getLore() {
        return lore;
    }

    @NotNull
    public Lore getDeny() {
        return deny;
    }

    @NotNull
    @Override
    public <T, R extends List<T>> R getLore(
            @NotNull R result,
            @NotNull BaseActionManager manager,
            @NotNull ActionContext context,
            Function<String, T> converter
    ) {
        if (manager.parseCondition(conditionString, condition, context).getType() == ResultType.SUCCESS) {
            return lore.getLore(result, manager, context, converter);
        } else {
            return deny.getLore(result, manager, context, converter);
        }
    }
}
