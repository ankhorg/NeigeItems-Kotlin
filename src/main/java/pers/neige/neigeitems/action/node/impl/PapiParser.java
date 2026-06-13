package pers.neige.neigeitems.action.node.impl;

import lombok.NonNull;
import lombok.val;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.node.NodeParser;
import pers.neige.neigeitems.manager.BaseActionManager;

import static pers.neige.neigeitems.manager.HookerManager.requestPapi;

public class PapiParser extends NodeParser {
    public PapiParser(@NonNull BaseActionManager manager) {
        super(manager);
    }

    @Override
    public @NonNull String getId() {
        return "papi";
    }

    @Override
    public @Nullable String parse(
        @NonNull ActionContext context,
        @NonNull String params
    ) {
        val player = context.getCaster();
        if (!(player instanceof OfflinePlayer)) return null;
        val args = params.split("_", 2);
        return requestPapi((OfflinePlayer) player, args[0], args[1]);
    }
}
