package pers.neige.neigeitems.text.impl;

import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ListText extends Text {
    @NotNull
    private final List<Text> text;

    public ListText(
            @NotNull BaseActionManager manager,
            @NotNull List<?> lore
    ) {
        super(manager);
        this.text = new ArrayList<>();
        for (Object it : lore) {
            this.text.add(Text.compile(manager, it));
        }
    }

    @NotNull
    public List<Text> getLore() {
        return text;
    }

    @NotNull
    @Override
    public <T, R extends List<T>> R getText(
            @NotNull R result,
            @NotNull BaseActionManager manager,
            @NotNull ActionContext context,
            Function<String, T> converter
    ) {
        for (Text value : text) {
            value.getText(result, manager, context, converter);
        }
        return result;
    }
}
