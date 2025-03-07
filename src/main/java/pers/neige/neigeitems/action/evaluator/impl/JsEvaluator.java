package pers.neige.neigeitems.action.evaluator.impl;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ScriptWithSource;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.StringUtils;

import javax.script.Compilable;
import javax.script.ScriptException;
import java.util.logging.Level;

public class JsEvaluator<T> extends Evaluator<T> {
    protected final @Nullable ScriptWithSource script;

    public JsEvaluator(@NotNull BaseActionManager manager, @NotNull Class<T> type, @Nullable String script) {
        super(manager, type);
        try {
            this.script = ScriptWithSource.compile((Compilable) manager.getEngine(), script);
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    public @Nullable ScriptWithSource getScript() {
        return script;
    }

    public @Nullable T cast(@NotNull Object result) {
        return type.isInstance(result) ? type.cast(result) : null;
    }

    @Override
    @Contract("_, !null -> !null")
    public @Nullable T getOrDefault(@NotNull ActionContext context, @Nullable T def) {
        if (this.script == null) return def;
        Object evalResult;
        try {
            evalResult = this.script.eval(context.getBindings());
            if (evalResult == null) return def;
        } catch (Throwable error) {
            String[] lines = this.script.getSource().split("\n");
            for (int i = 0; i < lines.length; i++) {
                String conditionLine = lines[i];
                lines[i] = (i + 1) + ". " + conditionLine;
            }
            manager.getPlugin().getLogger().log(Level.WARNING, this.getClass().getSimpleName() + "解析异常, 脚本内容如下:\n" + StringUtils.joinToString(lines, "\n", 0), error);
            return def;
        }
        T result = cast(evalResult);
        return result == null ? def : result;
    }
}
