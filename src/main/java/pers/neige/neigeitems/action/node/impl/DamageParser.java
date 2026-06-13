package pers.neige.neigeitems.action.node.impl;

import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ContextKeys;
import pers.neige.neigeitems.action.node.NodeParser;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.ItemUtils;

public class DamageParser extends NodeParser {
    public DamageParser(@NonNull BaseActionManager manager) {
        super(manager);
    }

    @Override
    public @NonNull String getId() {
        return "damage";
    }

    @Override
    public @Nullable String parse(
        @NonNull ActionContext context
    ) {
        val itemStack = context.get(ContextKeys.ITEM_STACK);
        return itemStack == null ? null : Integer.toString(ItemUtils.getDamage(itemStack));
    }
}
