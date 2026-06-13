package pers.neige.neigeitems.action.node.impl;

import lombok.NonNull;
import lombok.val;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.node.NodeParser;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.manager.ScriptManager;
import pers.neige.neigeitems.utils.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import static pers.neige.neigeitems.manager.HookerManager.papi;

public class JavascriptParser extends NodeParser {
    public JavascriptParser(@NonNull BaseActionManager manager) {
        super(manager);
    }

    @Override
    public @NonNull String getId() {
        return "js";
    }

    @Override
    public @Nullable String parse(
        @NonNull ActionContext context,
        @NonNull ConfigReader params
    ) {
        return handle(
            context,
            getParsedValue(context, params, "path"),
            params.getStringList("args")
        );
    }

    @Override
    public @Nullable String parse(
        @NonNull ActionContext context,
        @NonNull String params
    ) {
        val paramsList = StringUtils.split(params, '_', '\\');
        val path = paramsList.get(0);
        paramsList.remove(0);
        return handle(
            context,
            path,
            paramsList
        );
    }

    private @Nullable String handle(
        @NonNull ActionContext context,
        @Nullable String info,
        @NonNull List<?> args
    ) {
        if (info == null) {
            warning("未指定脚本路径");
            return null;
        }
        val array = info.split("::", 2);
        val path = array[0];
        val script = ScriptManager.INSTANCE.getCompiledScripts().get(path);
        if (script == null) {
            warning("不存在脚本 " + path);
            return null;
        }
        val func = array[1];
        val map = new HashMap<String, Object>();
        if (context.getCaster() instanceof OfflinePlayer) {
            map.put("player", context.getCaster());
            map.put("papi", (Function<String, String>) (string) -> papi((OfflinePlayer) context.getCaster(), string));
        }
        map.put("vars", (Function<String, String>) (string) -> this.manager.parseNode(string, context));
        return this.manager.parseNode(String.valueOf(script.invoke(func, map, args.toArray())), context);
    }
}
