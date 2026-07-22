package pers.neige.neigeitems.action.node.impl;

import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ContextKeys;
import pers.neige.neigeitems.action.node.NodeParser;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtUtils;
import pers.neige.neigeitems.manager.BaseActionManager;

public class LoreSizeParser extends NodeParser {
    public LoreSizeParser(@NonNull BaseActionManager manager) {
        super(manager);
    }

    @Override
    public @NonNull String getId() {
        return "lore_size";
    }

    @Override
    public @Nullable String parse(
        @NonNull ActionContext context
    ) {
        val itemStack = context.get(ContextKeys.ITEM_STACK);
        if (itemStack == null) return null;
        return Integer.toString(NbtUtils.getLoreSize(itemStack));
    }
}
