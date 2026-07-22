package pers.neige.neigeitems.action.node.impl;

import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ContextKeys;
import pers.neige.neigeitems.action.node.NodeParser;
import pers.neige.neigeitems.libs.bot.inker.bukkit.nbt.NbtUtils;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.ListUtils;
import pers.neige.neigeitems.utils.NumberParser;
import pers.neige.neigeitems.utils.StringUtils;

public class WholeLoreParser extends NodeParser {
    public WholeLoreParser(@NonNull BaseActionManager manager) {
        super(manager);
    }

    @Override
    public @NonNull String getId() {
        return "whole_lore";
    }

    @Override
    public @Nullable String parse(
        @NonNull ActionContext context,
        @NonNull String params
    ) {
        val args = StringUtils.split(params, '_', '\\', 2);
        val index = parse(args.get(0), 0, NumberParser::parseInteger, "{0} 并非数字, 无法用作lore行数索引");
        if (index == null) return null;
        val itemStack = context.get(ContextKeys.ITEM_STACK);
        val lore = NbtUtils.getLore(itemStack);
        val def = ListUtils.getOrDefault(args, 1, "");
        return lore == null ? def : StringUtils.joinToString(lore, "\n", index);
    }
}
