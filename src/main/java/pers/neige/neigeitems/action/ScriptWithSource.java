package pers.neige.neigeitems.action;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.script.*;

public class ScriptWithSource extends CompiledScript {
    private final @NotNull String source;
    private final @NotNull CompiledScript script;

    private ScriptWithSource(@NotNull String source, @NotNull CompiledScript script) {
        this.script = script;
        this.source = source;
    }

    public static @Nullable ScriptWithSource compile(@NotNull Compilable compilable, @Nullable String source) throws ScriptException {
        return source == null ? null : new ScriptWithSource(source, compilable.compile(source));
    }

    public @NotNull CompiledScript getScript() {
        return script;
    }

    public @NotNull String getSource() {
        return source;
    }

    @Override
    public Object eval() throws ScriptException {
        return script.eval();
    }

    @Override
    public Object eval(Bindings bindings) throws ScriptException {
        return script.eval(bindings);
    }

    @Override
    public Object eval(ScriptContext context) throws ScriptException {
        return script.eval(context);
    }

    @Override
    public ScriptEngine getEngine() {
        return script.getEngine();
    }
}
