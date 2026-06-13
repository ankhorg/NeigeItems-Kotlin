package pers.neige.neigeitems.action.node.impl;

import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ContextKeys;
import pers.neige.neigeitems.action.node.NodeParser;
import pers.neige.neigeitems.manager.BaseActionManager;

public class AmountParser extends NodeParser {
    public AmountParser(@NonNull BaseActionManager manager) {
        super(manager);
    }

    @Override
    public @NonNull String getId() {
        return "amount";
    }

    @Override
    public @Nullable String parse(
        @NonNull ActionContext context
    ) {
        val itemStack = context.get(ContextKeys.ITEM_STACK);
        return itemStack == null ? null : Integer.toString(itemStack.getAmount());
    }
}
