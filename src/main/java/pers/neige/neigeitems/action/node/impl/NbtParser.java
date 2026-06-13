package pers.neige.neigeitems.action.node.impl;

import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ContextKeys;
import pers.neige.neigeitems.action.node.NodeParser;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.ItemUtils;
import pers.neige.neigeitems.utils.ListUtils;
import pers.neige.neigeitems.utils.StringUtils;

public class NbtParser extends NodeParser {
    public NbtParser(@NonNull BaseActionManager manager) {
        super(manager);
    }

    @Override
    public @NonNull String getId() {
        return "nbt";
    }

    @Override
    public @Nullable String parse(
        @NonNull ActionContext context,
        @NonNull String params
    ) {
        val args = StringUtils.split(params, '_', '\\', 2);
        if (context.has(ContextKeys.NBT)) {
            val nbt = context.get(ContextKeys.NBT);
            return nbt == null ? null : nbt.getDeepString(args.get(0), ListUtils.getOrNull(args, 1));
        }
        if (context.has(ContextKeys.ITEM_INFO)) {
            val itemInfo = context.get(ContextKeys.ITEM_INFO);
            val nbt = itemInfo == null ? null : itemInfo.getItemTag();
            context.set(ContextKeys.NBT, nbt);
            return nbt == null ? null : nbt.getDeepString(args.get(0), ListUtils.getOrNull(args, 1));
        }
        if (context.has(ContextKeys.ITEM_STACK)) {
            val itemStack = context.get(ContextKeys.ITEM_STACK);
            val nbt = ItemUtils.getNbtOrNull(itemStack);
            context.set(ContextKeys.NBT, nbt);
            return nbt == null ? null : nbt.getDeepString(args.get(0), ListUtils.getOrNull(args, 1));
        }
        return null;
    }
}
