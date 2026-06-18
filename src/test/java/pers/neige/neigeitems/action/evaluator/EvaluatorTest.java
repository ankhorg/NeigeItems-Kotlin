package pers.neige.neigeitems.action.evaluator;

import lombok.NonNull;
import lombok.val;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ScriptWithSource;
import pers.neige.neigeitems.action.evaluator.impl.*;
import pers.neige.neigeitems.action.evaluator.impl.converter.*;
import pers.neige.neigeitems.action.result.Results;
import pers.neige.neigeitems.manager.BaseActionManager;

import javax.script.*;
import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DisplayName("Evaluator")
public class EvaluatorTest {
    private static final EvaluatorConverter<Token> TOKEN_CONVERTER = new TokenConverter();

    private static void assertIntTreeResult(String actionType, Token expected) {
        val manager = mockManager();
        val global = new HashMap<String, Object>();
        val context = context(global);
        val config = intTreeConfig(actionType, "raw: 10");

        assertEquals(expected, Evaluator.createEvaluator(manager, Token.class, config, TOKEN_CONVERTER).get(context));
        assertEquals(10, global.get("tree-key"));
    }

    private static BaseActionManager mockManager() {
        return mockManager(new FakeScriptEngine());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static BaseActionManager mockManager(FakeScriptEngine engine) {
        val manager = mock(BaseActionManager.class);
        val plugin = mock(Plugin.class);
        when(plugin.getLogger()).thenReturn(Logger.getLogger("EvaluatorTest"));
        when(manager.getPlugin()).thenReturn(plugin);
        when(manager.getEngine()).thenReturn(engine);
        when(manager.getNullEvaluator(any())).thenAnswer((invocation) -> new Evaluator(manager, invocation.getArgument(0)));
        when(manager.parseNode(anyString(), any(ActionContext.class))).thenAnswer((invocation) -> invocation.getArgument(0));
        when(manager.parseCondition(nullable(ScriptWithSource.class), any(ActionContext.class))).thenAnswer((invocation) -> {
            ScriptWithSource script = invocation.getArgument(0);
            if (script == null) return Results.SUCCESS;
            if ("true".equals(script.getSource())) return Results.SUCCESS;
            return Results.STOP;
        });
        return manager;
    }

    private static ActionContext context() {
        return context(new HashMap<>());
    }

    private static ActionContext context(Map<String, Object> global) {
        val context = mock(ActionContext.class);
        when(context.getGlobal()).thenReturn(global);
        when(context.getBindings()).thenReturn(new SimpleBindings());
        return context;
    }

    private static Map<String, Object> conditionConfig(String condition, Object thenValue, Object elseValue) {
        return config(
            "type", "condition",
            "condition", condition,
            "then", thenValue,
            "else", elseValue
        );
    }

    private static Map<String, Object> keyConfig(Object key) {
        val evaluators = config(
            "a", "id:a",
            "b", "id:b"
        );
        return config(
            "type", "key",
            "global-id", "selected",
            "key", key,
            "default-evaluator", "id:default",
            "evaluators", evaluators
        );
    }

    private static Map<String, Object> containsConfig(Object key, List<String> elements) {
        return config(
            "type", "contains",
            "global-id", "selected",
            "key", key,
            "default-evaluator", "id:default",
            "contains-evaluator", "id:contains",
            "elements", elements
        );
    }

    private static Map<String, Object> weightConfig(Object evaluator, Object weight) {
        return config(
            "type", "weight",
            "evaluators", Collections.singletonList(config(
                "weight", weight,
                "evaluator", evaluator
            ))
        );
    }

    private static Map<String, Object> conditionWeightConfig(String condition, Object evaluator, Object weight) {
        return config(
            "type", "condition-weight",
            "condition", condition,
            "evaluators", Collections.singletonList(config(
                "weight", weight,
                "evaluator", evaluator
            ))
        );
    }

    private static Map<String, Object> intTreeConfig(Object actionType, Object value) {
        val evaluators = config(
            "5", "id:five",
            "10", "id:ten",
            "20", "id:twenty"
        );
        return config(
            "type", "int-tree",
            "action-type", actionType,
            "global-id", "tree-key",
            "value", value,
            "default-evaluator", "id:default",
            "evaluators", evaluators
        );
    }

    private static Map<String, Object> doubleTreeConfig(Object actionType, Object value) {
        val evaluators = config(
            "1.0", "id:one",
            "2.0", "id:two"
        );
        return config(
            "type", "double-tree",
            "action-type", actionType,
            "global-id", "tree-key",
            "value", value,
            "default-evaluator", "id:default",
            "evaluators", evaluators
        );
    }

    private static Map<String, Object> config(Object... entries) {
        val result = new LinkedHashMap<String, Object>();
        for (int i = 0; i < entries.length; i += 2) {
            result.put((String) entries[i], entries[i + 1]);
        }
        return result;
    }

    private static class TokenConverter extends EvaluatorConverter<Token> {
        private TokenConverter() {
            super(Token.class);
        }

        @Override
        public @Nullable Token convert(@NonNull String input) {
            return input.startsWith("id:") ? new Token(input.substring(3)) : null;
        }

        @Override
        public @Nullable Token parseStaticValue(@NonNull String input) {
            return input.contains("<") && input.contains(">") ? null : super.parseStaticValue(input);
        }
    }

    private static class Token {
        private final String value;

        private Token(String value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Token)) return false;
            Token token = (Token) o;
            return Objects.equals(value, token.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public String toString() {
            return "Token{" + value + '}';
        }
    }

    private static class FakeScriptEngine extends AbstractScriptEngine implements Compilable {
        private final Map<String, Object> values = new HashMap<>();
        private final Set<String> throwingScripts = new HashSet<>();

        private static String read(Reader reader) throws ScriptException {
            try {
                val builder = new StringBuilder();
                int current;
                while ((current = reader.read()) != -1) {
                    builder.append((char) current);
                }
                return builder.toString();
            } catch (IOException exception) {
                throw new ScriptException(exception);
            }
        }

        private void set(String script, Object value) {
            values.put(script, value);
        }

        private void throwOn(String script) {
            throwingScripts.add(script);
        }

        @Override
        public Object eval(String script, ScriptContext context) throws ScriptException {
            return evaluate(script);
        }

        @Override
        public Object eval(Reader reader, ScriptContext context) throws ScriptException {
            return evaluate(read(reader));
        }

        @Override
        public Bindings createBindings() {
            return new SimpleBindings();
        }

        @Override
        public ScriptEngineFactory getFactory() {
            return null;
        }

        @Override
        public CompiledScript compile(String script) {
            return new FakeCompiledScript(this, script);
        }

        @Override
        public CompiledScript compile(Reader script) throws ScriptException {
            return compile(read(script));
        }

        private Object evaluate(String script) throws ScriptException {
            if (throwingScripts.contains(script)) {
                throw new ScriptException("throw: " + script);
            }
            return values.get(script);
        }
    }

    private static class FakeCompiledScript extends CompiledScript {
        private final FakeScriptEngine engine;
        private final String script;

        private FakeCompiledScript(FakeScriptEngine engine, String script) {
            this.engine = engine;
            this.script = script;
        }

        @Override
        public Object eval(ScriptContext context) throws ScriptException {
            return engine.evaluate(script);
        }

        @Override
        public ScriptEngine getEngine() {
            return engine;
        }
    }

    @Nested
    @DisplayName("转换器")
    class ConverterTest {
        @Test
        public void baseConverterOnlyAcceptsTargetType() {
            val converter = new EvaluatorConverter<>(String.class);

            assertEquals("test", converter.convert("test"));
            assertNull(converter.convert(1));
        }

        @Test
        public void numberConvertersAcceptStringAndNumberInputs() {
            assertEquals(10, IntegerConverter.INSTANCE.convert("10"));
            assertEquals(10, IntegerConverter.INSTANCE.convert("10.8"));
            assertEquals(3, IntegerConverter.INSTANCE.convert(3.5D));
            assertNull(IntegerConverter.INSTANCE.convert("bad"));

            assertEquals(10L, LongConverter.INSTANCE.convert("10"));
            assertEquals(10L, LongConverter.INSTANCE.convert("10.8"));
            assertEquals(3L, LongConverter.INSTANCE.convert(3.5D));
            assertNull(LongConverter.INSTANCE.convert("bad"));

            assertEquals(10D, DoubleConverter.INSTANCE.convert("10"));
            assertEquals(10.5D, DoubleConverter.INSTANCE.convert("10.5"));
            assertEquals(3.5D, DoubleConverter.INSTANCE.convert(3.5D));
            assertNull(DoubleConverter.INSTANCE.convert("bad"));
        }

        @Test
        public void booleanConverterAcceptsBooleanStringsAndBooleanObjects() {
            assertEquals(true, BooleanConverter.INSTANCE.convert("true"));
            assertEquals(true, BooleanConverter.INSTANCE.convert("TRUE"));
            assertEquals(false, BooleanConverter.INSTANCE.convert("false"));
            assertEquals(false, BooleanConverter.INSTANCE.convert("FALSE"));
            assertEquals(true, BooleanConverter.INSTANCE.convert(true));
            assertNull(BooleanConverter.INSTANCE.convert("bad"));
        }

        @Test
        public void stringConverterUsesToStringForObjectsButAvoidsStaticPlaceholderParsing() {
            assertEquals("test", StringConverter.INSTANCE.convert("test"));
            assertEquals("123", StringConverter.INSTANCE.convert(123));
            assertNull(StringConverter.INSTANCE.parseStaticValue("<name>"));
        }

        @Test
        public void customConverterCanControlStaticParsing() {
            assertEquals(new Token("test"), TOKEN_CONVERTER.convert("id:test"));
            assertNull(TOKEN_CONVERTER.convert("bad"));
            assertNull(TOKEN_CONVERTER.parseStaticValue("<token>"));
        }
    }

    @Nested
    @DisplayName("工厂分发")
    class FactoryTest {
        @Test
        public void nullInputCreatesNullEvaluatorBehavior() {
            val manager = mockManager();
            val context = context();

            assertNull(Evaluator.createEvaluator(manager, String.class, null).get(context));
            assertEquals("fallback", Evaluator.createEvaluator(manager, String.class, null).getOrDefault(context, "fallback"));
            assertEquals(1, Evaluator.createEvaluator(manager, Integer.class, null).getOrDefault(context, 1));
            assertEquals(1L, Evaluator.createEvaluator(manager, Long.class, null).getOrDefault(context, 1L));
            assertEquals(1D, Evaluator.createEvaluator(manager, Double.class, null).getOrDefault(context, 1D));
            assertEquals(true, Evaluator.createEvaluator(manager, Boolean.class, null).getOrDefault(context, true));
            assertEquals(new Token("fallback"), Evaluator.createEvaluator(manager, Token.class, null).getOrDefault(context, new Token("fallback")));
        }

        @Test
        public void evaluatorInputReturnsSameInstanceWhenTypeCompatible() {
            val manager = mockManager();
            Evaluator<String> existing = Evaluator.createStringEvaluator(manager, "raw: value");

            assertSame(existing, Evaluator.createEvaluator(manager, String.class, existing));
            assertSame(existing, Evaluator.createEvaluator(manager, Object.class, existing));
        }

        @Test
        public void evaluatorInputReturnsNullEvaluatorWhenTypeIncompatible() {
            val manager = mockManager();
            val context = context();
            Evaluator<String> existing = Evaluator.createStringEvaluator(manager, "raw: value");

            val result = Evaluator.createEvaluator(manager, Token.class, existing, TOKEN_CONVERTER);

            assertNull(result.get(context));
            assertEquals(new Token("fallback"), result.getOrDefault(context, new Token("fallback")));
        }

        @Test
        public void stringInputUsesRawJsStaticAndParseBranches() {
            val engine = new FakeScriptEngine();
            engine.set("tokenJs", new Token("js"));
            val manager = mockManager(engine);
            val context = context();
            when(manager.parseNode(eq("<token>"), same(context))).thenReturn("id:parsed");

            assertEquals(new Token("raw"), Evaluator.createEvaluator(manager, Token.class, "raw: id:raw", TOKEN_CONVERTER).get(context));
            assertEquals(new Token("js"), Evaluator.createEvaluator(manager, Token.class, "js: tokenJs", TOKEN_CONVERTER).get(context));
            assertEquals(new Token("static"), Evaluator.createEvaluator(manager, Token.class, "id:static", TOKEN_CONVERTER).get(context));
            assertEquals(new Token("parsed"), Evaluator.createEvaluator(manager, Token.class, "<token>", TOKEN_CONVERTER).get(context));
        }

        @Test
        public void stringPrefixRequiresColonSpaceAndIgnoresCase() {
            val engine = new FakeScriptEngine();
            engine.set("tokenJs", new Token("js"));
            val manager = mockManager(engine);
            val context = context();

            assertEquals(new Token("upper"), Evaluator.createEvaluator(manager, Token.class, "RAW: id:upper", TOKEN_CONVERTER).get(context));
            assertEquals(new Token("js"), Evaluator.createEvaluator(manager, Token.class, "Js: tokenJs", TOKEN_CONVERTER).get(context));
            assertEquals(new Token("raw:id:no-space"), Evaluator.createEvaluator(manager, Token.class, "id:raw:id:no-space", TOKEN_CONVERTER).get(context));
            assertNull(Evaluator.createEvaluator(manager, Token.class, "raw:id:no-space", TOKEN_CONVERTER).get(context));
        }

        @Test
        public void objectInputUsesConverterBeforeConfigParsing() {
            val manager = mockManager();
            val context = context();
            val token = new Token("object");

            assertEquals(token, Evaluator.createEvaluator(manager, Token.class, token, TOKEN_CONVERTER).get(context));
            assertEquals(3, Evaluator.createIntegerEvaluator(manager, 3.5D).get(context));
            assertEquals(3L, Evaluator.createLongEvaluator(manager, 3.5D).get(context));
            assertEquals(3.5D, Evaluator.createDoubleEvaluator(manager, 3.5D).get(context));
            assertEquals(true, Evaluator.createBooleanEvaluator(manager, true).get(context));
        }

        @Test
        public void objectInputFallsBackToStringParsingWhenItIsNotConfig() {
            val manager = mockManager();
            val context = context();
            val input = new Object() {
                @Override
                public String toString() {
                    return "id:from-to-string";
                }
            };

            assertEquals(new Token("from-to-string"), Evaluator.createEvaluator(manager, Token.class, input, TOKEN_CONVERTER).get(context));
        }

        @Test
        public void listInputKeepsScalarFallbackChain() {
            val manager = mockManager();
            val context = context();

            val result = Evaluator.createEvaluator(
                manager,
                Token.class,
                Arrays.asList("bad", "id:first", "id:second"),
                TOKEN_CONVERTER
            ).get(context);

            assertEquals(new Token("first"), result);
        }

        @Test
        public void configTypeDispatchesToCompositeEvaluators() {
            val manager = mockManager();

            assertInstanceOf(ConditionEvaluator.class, Evaluator.createEvaluator(manager, Token.class, conditionConfig("true", "id:then", "id:else"), TOKEN_CONVERTER));
            assertInstanceOf(ConditionWeightEvaluator.class, Evaluator.createEvaluator(manager, Token.class, conditionWeightConfig("true", "id:weighted", 1), TOKEN_CONVERTER));
            assertInstanceOf(KeyEvaluator.class, Evaluator.createEvaluator(manager, Token.class, keyConfig("raw: a"), TOKEN_CONVERTER));
            assertInstanceOf(ContainsEvaluator.class, Evaluator.createEvaluator(manager, Token.class, containsConfig("raw: a", Collections.singletonList("a")), TOKEN_CONVERTER));
            assertInstanceOf(WeightEvaluator.class, Evaluator.createEvaluator(manager, Token.class, weightConfig("id:weighted", 1), TOKEN_CONVERTER));
            assertInstanceOf(IntTreeEvaluator.class, Evaluator.createEvaluator(manager, Token.class, intTreeConfig("FLOOR", "raw: 1"), TOKEN_CONVERTER));
            assertInstanceOf(DoubleTreeEvaluator.class, Evaluator.createEvaluator(manager, Token.class, doubleTreeConfig("FLOOR", "raw: 1.0"), TOKEN_CONVERTER));
        }

        @Test
        public void implicitConditionAndSingleKeyMapsAreSupported() {
            val engine = new FakeScriptEngine();
            engine.set("tokenJs", new Token("js"));
            val manager = mockManager(engine);
            val context = context();

            val implicitCondition = config(
                "condition", "true",
                "then", "id:then",
                "else", "id:else"
            );
            assertInstanceOf(ConditionEvaluator.class, Evaluator.createEvaluator(manager, Token.class, implicitCondition, TOKEN_CONVERTER));

            assertEquals(new Token("raw"), Evaluator.createEvaluator(manager, Token.class, config("raw", "id:raw"), TOKEN_CONVERTER).get(context));
            assertEquals(new Token("js"), Evaluator.createEvaluator(manager, Token.class, config("js", "tokenJs"), TOKEN_CONVERTER).get(context));
            assertNull(Evaluator.createEvaluator(manager, Token.class, config("raw", null), TOKEN_CONVERTER).get(context));
            assertNull(Evaluator.createEvaluator(manager, Token.class, config("a", "id:a", "b", "id:b"), TOKEN_CONVERTER).get(context));
        }
    }

    @Nested
    @DisplayName("基础求值器")
    class BasicEvaluatorTest {
        @Test
        public void rawEvaluatorReturnsValueOrDefault() {
            val manager = mockManager();
            val context = context();
            val evaluator = new RawEvaluator<>(manager, Token.class, "id:raw", TOKEN_CONVERTER);
            val invalid = new RawEvaluator<>(manager, Token.class, "bad", TOKEN_CONVERTER);

            assertEquals(new Token("raw"), evaluator.get(context));
            assertEquals(new Token("raw"), evaluator.getValue());
            assertNull(invalid.get(context));
            assertEquals(new Token("fallback"), invalid.getOrDefault(context, new Token("fallback")));
        }

        @Test
        public void rawEvaluatorCoversBuiltInConverters() {
            val manager = mockManager();
            val context = context();

            assertEquals("text", new RawEvaluator<>(manager, String.class, "text", StringConverter.INSTANCE).get(context));
            assertEquals(1, new RawEvaluator<>(manager, Integer.class, "1", IntegerConverter.INSTANCE).get(context));
            assertEquals(1L, new RawEvaluator<>(manager, Long.class, "1", LongConverter.INSTANCE).get(context));
            assertEquals(1.5D, new RawEvaluator<>(manager, Double.class, "1.5", DoubleConverter.INSTANCE).get(context));
            assertEquals(false, new RawEvaluator<>(manager, Boolean.class, "false", BooleanConverter.INSTANCE).get(context));
            assertEquals(true, new RawEvaluator<>(manager, Boolean.class, "bad", BooleanConverter.INSTANCE).getOrDefault(context, true));
        }

        @Test
        public void parseEvaluatorUsesManagerParseNodeThenConverter() {
            val manager = mockManager();
            val context = context();
            when(manager.parseNode("<token>", context)).thenReturn("id:parsed");
            when(manager.parseNode("<bad>", context)).thenReturn("bad");

            assertEquals(new Token("parsed"), new ParseEvaluator<>(manager, Token.class, "<token>", TOKEN_CONVERTER).get(context));
            assertEquals(new Token("fallback"), new ParseEvaluator<>(manager, Token.class, "<bad>", TOKEN_CONVERTER).getOrDefault(context, new Token("fallback")));
            verify(manager).parseNode("<token>", context);
        }

        @Test
        public void parseEvaluatorWithNullFormulaReturnsDefaultWithoutParsing() {
            val manager = mockManager();
            val context = context();

            assertEquals("fallback", new ParseEvaluator<>(manager, String.class, null).getOrDefault(context, "fallback"));
            verify(manager, never()).parseNode(anyString(), any(ActionContext.class));
        }

        @Test
        public void jsEvaluatorUsesCompiledScriptAndConverter() {
            val engine = new FakeScriptEngine();
            engine.set("string", 123);
            engine.set("int", 1.8D);
            engine.set("long", 2.8D);
            engine.set("double", 3);
            engine.set("boolean", true);
            engine.set("token", new Token("js"));
            val manager = mockManager(engine);
            val context = context();

            assertEquals("123", new JsEvaluator<>(manager, String.class, "string", StringConverter.INSTANCE).get(context));
            assertEquals(1, new JsEvaluator<>(manager, Integer.class, "int", IntegerConverter.INSTANCE).get(context));
            assertEquals(2L, new JsEvaluator<>(manager, Long.class, "long", LongConverter.INSTANCE).get(context));
            assertEquals(3D, new JsEvaluator<>(manager, Double.class, "double", DoubleConverter.INSTANCE).get(context));
            assertEquals(true, new JsEvaluator<>(manager, Boolean.class, "boolean", BooleanConverter.INSTANCE).get(context));
            assertEquals(new Token("js"), new JsEvaluator<>(manager, Token.class, "token", TOKEN_CONVERTER).get(context));
        }

        @Test
        public void jsEvaluatorReturnsDefaultForNullWrongTypeNullScriptAndException() {
            val engine = new FakeScriptEngine();
            engine.set("null", null);
            engine.set("wrong", "bad");
            engine.throwOn("throw");
            val manager = mockManager(engine);
            val context = context();

            assertEquals(9, new JsEvaluator<>(manager, Integer.class, null, IntegerConverter.INSTANCE).getOrDefault(context, 9));
            assertEquals(9, new JsEvaluator<>(manager, Integer.class, "null", IntegerConverter.INSTANCE).getOrDefault(context, 9));
            assertEquals(9, new JsEvaluator<>(manager, Integer.class, "wrong", IntegerConverter.INSTANCE).getOrDefault(context, 9));
            assertEquals(9, new JsEvaluator<>(manager, Integer.class, "throw", IntegerConverter.INSTANCE).getOrDefault(context, 9));
        }
    }

    @Nested
    @DisplayName("组合求值器")
    class CompositeEvaluatorTest {
        @Test
        public void conditionEvaluatorSelectsThenOrElseBranch() {
            val manager = mockManager();
            val context = context();

            assertEquals(new Token("then"), Evaluator.createEvaluator(manager, Token.class, conditionConfig("true", "id:then", "id:else"), TOKEN_CONVERTER).get(context));
            assertEquals(new Token("else"), Evaluator.createEvaluator(manager, Token.class, conditionConfig("false", "id:then", "id:else"), TOKEN_CONVERTER).get(context));
            assertEquals(new Token("fallback"), Evaluator.createEvaluator(manager, Token.class, conditionConfig("true", null, "id:else"), TOKEN_CONVERTER).getOrDefault(context, new Token("fallback")));
        }

        @Test
        public void keyEvaluatorSelectsMatchOrDefaultAndWritesGlobal() {
            val manager = mockManager();
            val global = new HashMap<String, Object>();
            val context = context(global);

            assertEquals(new Token("a"), Evaluator.createEvaluator(manager, Token.class, keyConfig("raw: a"), TOKEN_CONVERTER).get(context));
            assertEquals("a", global.get("selected"));

            assertEquals(new Token("default"), Evaluator.createEvaluator(manager, Token.class, keyConfig("raw: missing"), TOKEN_CONVERTER).get(context));
            assertEquals("missing", global.get("selected"));

            assertEquals(new Token("default"), Evaluator.createEvaluator(manager, Token.class, keyConfig(null), TOKEN_CONVERTER).get(context));
            assertTrue(global.containsKey("selected"));
            assertNull(global.get("selected"));
        }

        @Test
        public void containsEvaluatorSelectsContainsOrDefaultAndWritesGlobal() {
            val manager = mockManager();
            val global = new HashMap<String, Object>();
            val context = context(global);

            assertEquals(new Token("contains"), Evaluator.createEvaluator(manager, Token.class, containsConfig("raw: a", Arrays.asList("a", "b")), TOKEN_CONVERTER).get(context));
            assertEquals("a", global.get("selected"));

            assertEquals(new Token("default"), Evaluator.createEvaluator(manager, Token.class, containsConfig("raw: c", Arrays.asList("a", "b")), TOKEN_CONVERTER).get(context));
            assertEquals("c", global.get("selected"));

            assertEquals(new Token("default"), Evaluator.createEvaluator(manager, Token.class, containsConfig(null, Collections.emptyList()), TOKEN_CONVERTER).get(context));
            assertTrue(global.containsKey("selected"));
            assertNull(global.get("selected"));
        }

        @Test
        public void intTreeEvaluatorSupportsAllActionTypesAndWritesGlobal() {
            assertIntTreeResult("LOWER", new Token("five"));
            assertIntTreeResult("FLOOR", new Token("ten"));
            assertIntTreeResult("HIGHER", new Token("twenty"));
            assertIntTreeResult("CEILING", new Token("ten"));
        }

        @Test
        public void treeEvaluatorFallsBackForNoMatchNullValueAndInvalidKeys() {
            val manager = mockManager();
            val global = new HashMap<String, Object>();
            val context = context(global);

            val noMatch = intTreeConfig("LOWER", "raw: 1");
            assertEquals(new Token("default"), Evaluator.createEvaluator(manager, Token.class, noMatch, TOKEN_CONVERTER).get(context));
            assertEquals(1, global.get("tree-key"));

            val nullValue = intTreeConfig("FLOOR", "bad");
            assertEquals(new Token("default"), Evaluator.createEvaluator(manager, Token.class, nullValue, TOKEN_CONVERTER).get(context));
            assertNull(global.get("tree-key"));

            val invalid = intTreeConfig("bad", "raw: 10");
            ((Map<String, Object>) invalid.get("evaluators")).put("bad-key", "id:bad");
            assertEquals(new Token("five"), Evaluator.createEvaluator(manager, Token.class, invalid, TOKEN_CONVERTER).get(context));
        }

        @Test
        public void doubleTreeEvaluatorSupportsDoubleKeyParsing() {
            val manager = mockManager();
            val config = doubleTreeConfig("FLOOR", "raw: 1.5");
            val evaluator = Evaluator.createEvaluator(manager, Token.class, config, TOKEN_CONVERTER);

            assertInstanceOf(DoubleTreeEvaluator.class, evaluator);
            assertEquals(1.5D, ((DoubleTreeEvaluator<Token>) evaluator).cast("1.5"));
        }

        @Test
        public void weightEvaluatorUsesDeterministicBranches() {
            val manager = mockManager();
            val context = context();

            assertEquals(new Token("fallback"), Evaluator.createEvaluator(manager, Token.class, config("type", "weight"), TOKEN_CONVERTER).getOrDefault(context, new Token("fallback")));
            assertEquals(new Token("weighted"), Evaluator.createEvaluator(manager, Token.class, weightConfig("id:weighted", 1), TOKEN_CONVERTER).get(context));

            val weighted = config(
                "type", "weight",
                "evaluators", Arrays.asList(
                    config("weight", 0, "evaluator", "id:zero"),
                    config("weight", -1, "evaluator", "id:negative"),
                    config("weight", 2, "evaluator", "id:positive")
                )
            );
            assertEquals(new Token("positive"), Evaluator.createEvaluator(manager, Token.class, weighted, TOKEN_CONVERTER).get(context));
        }

        @Test
        public void conditionWeightEvaluatorUsesDeterministicBranches() {
            val manager = mockManager();
            val context = context();

            assertEquals(new Token("fallback"), Evaluator.createEvaluator(manager, Token.class, conditionWeightConfig("false", "id:weighted", 1), TOKEN_CONVERTER).getOrDefault(context, new Token("fallback")));
            assertEquals(new Token("weighted"), Evaluator.createEvaluator(manager, Token.class, conditionWeightConfig("true", "id:weighted", 1), TOKEN_CONVERTER).get(context));
            assertEquals(new Token("fallback"), Evaluator.createEvaluator(manager, Token.class, conditionWeightConfig("true", "id:weighted", 0), TOKEN_CONVERTER).getOrDefault(context, new Token("fallback")));
            assertEquals(new Token("weighted"), Evaluator.createEvaluator(manager, Token.class, conditionWeightConfig("true", "id:weighted", "bad"), TOKEN_CONVERTER).get(context));
        }

        @Test
        public void conditionWeightEvaluatorUsesTopLevelConditionForEntries() {
            val manager = mockManager();
            val context = context();
            val entry = config(
                "condition", "false",
                "weight", 1,
                "evaluator", "id:entry-condition-is-ignored"
            );
            val config = config(
                "type", "condition-weight",
                "condition", "true",
                "evaluators", Collections.singletonList(entry)
            );

            assertEquals(new Token("entry-condition-is-ignored"), Evaluator.createEvaluator(manager, Token.class, config, TOKEN_CONVERTER).get(context));
        }
    }
}
