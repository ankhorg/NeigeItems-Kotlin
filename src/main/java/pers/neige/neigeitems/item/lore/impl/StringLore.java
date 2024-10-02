package pers.neige.neigeitems.item.lore.impl;

import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.item.lore.Lore;
import pers.neige.neigeitems.manager.ActionManager;
import pers.neige.neigeitems.utils.SectionUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class StringLore implements Lore {
    @NotNull
    private final String lore;

    public StringLore(
            @NotNull String lore
    ) {
        this.lore = lore;
    }

    @NotNull
    @Override
    public <T, R extends List<T>> R getLore(
            @NotNull R result,
            @NotNull ActionManager manager,
            @NotNull ActionContext context,
            Function<String, T> converter
    ) {
        String text = SectionUtils.parseSection(this.lore, (Map<String, String>) (Object) context.getGlobal(), context.getPlayer(), null);
        T lore = converter.apply(text);
        result.add(lore);
        return result;
    }
}
