package pers.neige.neigeitems.action.string.impl;

import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.string.StringEvaluator;
import pers.neige.neigeitems.manager.BaseActionManager;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptException;

public class JsStringEvaluator extends StringEvaluator {
    private final @NotNull String scriptString;
    private final @NotNull CompiledScript script;

    public JsStringEvaluator(@NotNull BaseActionManager manager, @NotNull String script) {
        super(manager);
        this.scriptString = script;
        try {
            this.script = ((Compilable) manager.getEngine()).compile(script);
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    public @NotNull String getScriptString() {
        return scriptString;
    }

    public @NotNull CompiledScript getScript() {
        return script;
    }

    @Override
    public @NotNull String get(@NotNull ActionContext context) {
        Object result;
        try {
            result = this.script.eval(context.getBindings());
            if (result == null) {
                return "";
            }
        } catch (Throwable error) {
            manager.getPlugin().getLogger().warning("条件解析异常, 条件内容如下:");
            String[] lines = scriptString.split("\n");
            for (int i = 0; i < lines.length; i++) {
                String conditionLine = lines[i];
                manager.getPlugin().getLogger().warning((i + 1) + ". " + conditionLine);
            }
            error.printStackTrace();
            return "";
        }
        return result.toString();
    }
}
