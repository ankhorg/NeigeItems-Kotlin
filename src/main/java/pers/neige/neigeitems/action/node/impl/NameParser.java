package pers.neige.neigeitems.action.node.impl;

import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ContextKeys;
import pers.neige.neigeitems.action.node.NodeParser;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.neigeitems.utils.TranslationUtils;
import pers.neige.neigeitems.manager.BaseActionManager;

public class NameParser extends NodeParser {
    public NameParser(@NonNull BaseActionManager manager) {
        super(manager);
    }

    @Override
    public @NonNull String getId() {
        return "name";
    }

    @Override
    public @Nullable String parse(
        @NonNull ActionContext context
    ) {
        val itemStack = context.get(ContextKeys.ITEM_STACK);
        return itemStack == null ? null : TranslationUtils.getDisplayOrTranslationName(itemStack);
    }
}
