package pers.neige.neigeitems.action;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import javax.script.*;

public class ScriptWithSource extends CompiledScript {
    private final @NonNull String source;
    private final @NonNull CompiledScript script;

    private ScriptWithSource(@NonNull String source, @NonNull CompiledScript script) {
        this.script = script;
        this.source = source;
    }

    public static @Nullable ScriptWithSource compile(@NonNull Compilable compilable, @Nullable String source) throws ScriptException {
        return source == null ? null : new ScriptWithSource(source, compilable.compile(source));
    }

    public @NonNull CompiledScript getScript() {
        return script;
    }

    public @NonNull String getSource() {
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
