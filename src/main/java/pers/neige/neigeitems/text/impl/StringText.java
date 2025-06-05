package pers.neige.neigeitems.text.impl;

import lombok.NonNull;
import lombok.val;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.text.Text;
import pers.neige.neigeitems.utils.SectionUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class StringText extends Text {
    private final @NonNull String lore;

    public StringText(
            @NonNull BaseActionManager manager,
            @NonNull String lore
    ) {
        super(manager);
        this.lore = lore;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T, R extends List<T>> @NonNull R getText(
            @NonNull R result,
            @NonNull BaseActionManager manager,
            @NonNull ActionContext context,
            Function<String, T> converter
    ) {
        val text = SectionUtils.parseSection(this.lore, (Map<String, String>) (Object) context.getGlobal(), context.getPlayer(), null);
        val lore = converter.apply(text);
        result.add(lore);
        return result;
    }
}
