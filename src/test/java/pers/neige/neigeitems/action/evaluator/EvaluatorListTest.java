package pers.neige.neigeitems.action.evaluator;

import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.manager.BaseActionManager;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Evaluator List")
public class EvaluatorListTest {
    private static final EvaluatorConverter<Token> TOKEN_CONVERTER = new TokenConverter();

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static BaseActionManager mockManager() {
        val manager = mock(BaseActionManager.class);
        when(manager.getNullEvaluator(any())).thenAnswer((invocation) -> new Evaluator(manager, invocation.getArgument(0)));
        when(manager.parseNode(anyString(), any(ActionContext.class))).thenAnswer((invocation) -> invocation.getArgument(0));
        return manager;
    }

    private static ActionContext context() {
        return mock(ActionContext.class);
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
    }

    @Nested
    @DisplayName("null 元素语义")
    class NullElementTest {
        @Test
        public void explicitNullTypeReturnsNullElement() {
            val config = config("type", null);

            val result = Evaluator.createStringListEvaluator(mockManager(), config).get(context());

            assertNotNull(result);
            assertEquals(1, result.size());
            assertNull(result.get(0));
        }

        @Test
        public void explicitStringNullTypeReturnsNullElement() {
            val config = config("type", "null");

            val result = Evaluator.createStringListEvaluator(mockManager(), config).get(context());

            assertNotNull(result);
            assertEquals(1, result.size());
            assertNull(result.get(0));
        }

        @Test
        public void ordinaryNullElementIsSkipped() {
            val result = Evaluator.createStringListEvaluator(
                mockManager(),
                Arrays.asList("raw: a", null, "raw: b")
            ).get(context());

            assertEquals(Arrays.asList("a", "b"), result);
        }

        @Test
        public void explicitNullElementIsPreserved() {
            val nullConfig = config("type", null);

            val result = Evaluator.createStringListEvaluator(
                mockManager(),
                Arrays.asList("raw: a", nullConfig, "raw: b")
            ).get(context());

            assertEquals(Arrays.asList("a", null, "b"), result);
        }
    }

    @Nested
    @DisplayName("列表组合语义")
    class ListValueTest {
        @Test
        public void emptyYamlListReturnsEmptyList() {
            val result = Evaluator.createStringListEvaluator(mockManager(), Collections.emptyList()).get(context());

            assertNotNull(result);
            assertTrue(result.isEmpty());
            assertThrows(UnsupportedOperationException.class, () -> result.add("blocked"));
        }

        @Test
        public void nestedListsAreFlattenedAndOrdinaryNullsAreSkipped() {
            val nullConfig = config("type", null);

            val result = Evaluator.createStringListEvaluator(
                mockManager(),
                Arrays.asList("raw: a", nullConfig, Arrays.asList("raw: b", null, "raw: c"))
            ).get(context());

            assertEquals(Arrays.asList("a", null, "b", "c"), result);
        }

        @Test
        public void nonListInputIsWrappedAsSingleElementList() {
            val result = Evaluator.createIntegerListEvaluator(mockManager(), "raw: 10").get(context());

            assertEquals(Collections.singletonList(10), result);
        }

        @Test
        public void nonListInputReturnsDefaultWhenScalarResultIsNull() {
            val fallback = Collections.singletonList(9);
            val result = Evaluator.createIntegerListEvaluator(mockManager(), "bad").getOrDefault(context(), fallback);

            assertSame(fallback, result);
        }

        @Test
        public void evaluatorListInputIsReturnedDirectly() {
            val manager = mockManager();
            val existing = Evaluator.createStringListEvaluator(manager, "raw: a");

            assertSame(existing, Evaluator.createStringListEvaluator(manager, existing));
            assertEquals(Collections.singletonList("a"), existing.get(context()));
        }

        @Test
        public void scalarEvaluatorInputIsWrapped() {
            val manager = mockManager();
            val scalar = Evaluator.createStringEvaluator(manager, "raw: a");

            val result = Evaluator.createStringListEvaluator(manager, scalar).get(context());

            assertEquals(Collections.singletonList("a"), result);
        }

        @Test
        public void customListEvaluatorSkipsOrdinaryNullAndPreservesExplicitNull() {
            val manager = mockManager();
            val nullConfig = config("type", null);

            val result = Evaluator.createListEvaluator(
                manager,
                Token.class,
                Arrays.asList("id:first", null, nullConfig, "bad", Arrays.asList("id:second", null)),
                TOKEN_CONVERTER
            ).get(context());

            assertEquals(Arrays.asList(new Token("first"), null, new Token("second")), result);
        }
    }

    @Nested
    @DisplayName("标量列表 fallback 语义")
    class ScalarListFallbackTest {
        @Test
        public void scalarEvaluatorListReturnsFirstNonNullResult() {
            val result = Evaluator.createIntegerEvaluator(
                mockManager(),
                Arrays.asList("bad", "raw: 2", "raw: 3")
            ).get(context());

            assertEquals(2, result);
        }

        @Test
        public void scalarEvaluatorListReturnsDefaultWhenAllChildrenAreNull() {
            val result = Evaluator.createIntegerEvaluator(
                mockManager(),
                Arrays.asList("bad", null)
            ).getOrDefault(context(), 9);

            assertEquals(9, result);
        }

        @Test
        public void scalarEvaluatorListUsesListInputAsFallbackChainForListValues() {
            val converter = new EvaluatorConverter<List>(List.class) {
                @Override
                public @Nullable List convert(@NonNull String input) {
                    if (!input.startsWith("list:")) return null;
                    return Arrays.asList(input.substring(5).split(","));
                }
            };

            val result = Evaluator.createEvaluator(
                mockManager(),
                List.class,
                Arrays.asList("bad", "list:a,b"),
                converter
            ).get(context());

            assertEquals(Arrays.asList("bad", "list:a,b"), result);
        }
    }
}
