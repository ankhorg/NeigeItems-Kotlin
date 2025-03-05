package pers.neige.neigeitems.text.impl;

import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.text.Text;
import pers.neige.neigeitems.utils.SectionUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class StringText extends Text {
    @NotNull
    private final String lore;

    public StringText(
            @NotNull BaseActionManager manager,
            @NotNull String lore
    ) {
        super(manager);
        this.lore = lore;
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public <T, R extends List<T>> R getText(
            @NotNull R result,
            @NotNull BaseActionManager manager,
            @NotNull ActionContext context,
            Function<String, T> converter
    ) {
        String text = SectionUtils.parseSection(this.lore, (Map<String, String>) (Object) context.getGlobal(), context.getPlayer(), null);
        T lore = converter.apply(text);
        result.add(lore);
        return result;
    }
}
