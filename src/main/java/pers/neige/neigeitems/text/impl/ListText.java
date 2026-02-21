package pers.neige.neigeitems.text.impl;

import lombok.NonNull;
import lombok.val;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ListText extends Text {
    private final @NonNull List<Text> text;

    public ListText(
        @NonNull BaseActionManager manager,
        @NonNull List<?> lore
    ) {
        super(manager);
        this.text = new ArrayList<>();
        for (val it : lore) {
            this.text.add(Text.compile(manager, it));
        }
    }

    public @NonNull List<Text> getLore() {
        return text;
    }

    @Override
    public <T, R extends List<T>> @NonNull R getText(
        @NonNull R result,
        @NonNull BaseActionManager manager,
        @NonNull ActionContext context,
        Function<String, T> converter
    ) {
        for (val value : text) {
            value.getText(result, manager, context, converter);
        }
        return result;
    }
}
