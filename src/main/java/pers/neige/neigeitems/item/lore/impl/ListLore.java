package pers.neige.neigeitems.item.lore.impl;

import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.item.lore.Lore;
import pers.neige.neigeitems.manager.ActionManager;
import pers.neige.neigeitems.manager.BaseActionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ListLore implements Lore {
    @NotNull
    private final List<Lore> lore;

    public ListLore(
            @NotNull BaseActionManager manager,
            @NotNull List<?> lore
    ) {
        this.lore = new ArrayList<>();
        for (Object it : lore) {
            this.lore.add(Lore.compile(manager, it));
        }
    }

    @NotNull
    public List<Lore> getLore() {
        return lore;
    }

    @NotNull
    @Override
    public <T, R extends List<T>> R getLore(
            @NotNull R result,
            @NotNull BaseActionManager manager,
            @NotNull ActionContext context,
            Function<String, T> converter
    ) {
        for (Lore value : lore) {
            result.addAll(value.getLore(result, manager, context, converter));
        }
        return result;
    }
}
