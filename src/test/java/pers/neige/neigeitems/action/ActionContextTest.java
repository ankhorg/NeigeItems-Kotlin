package pers.neige.neigeitems.action;

import org.bukkit.Bukkit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ActionContextTest {
    private MockedStatic<Bukkit> mockedBukkit;

    @BeforeEach
    void setUp() {
        mockedBukkit = Mockito.mockStatic(Bukkit.class);
        mockedBukkit.when(Bukkit::isPrimaryThread).thenReturn(true);
    }

    @AfterEach
    void tearDown() {
        mockedBukkit.close();
    }

    @Test
    void cloneCopiesBindingsAndValuesButRebindsContext() {
        ActionContext original = new ActionContext((Object) "caster", new HashMap<>(), null);
        ContextKey<String> local = new ContextKey<>("local", "local-alias");
        original.set(local, "original");
        original.getBindings().put("custom", "custom-value");

        ActionContext copy = original.clone();

        assertNotSame(original, copy);
        assertSame(original, original.getBindings().get("context"));
        assertSame(copy, copy.getBindings().get("context"));
        assertEquals("custom-value", copy.getBindings().get("custom"));
        assertEquals("original", copy.get(local));

        copy.set(local, "copy");
        assertEquals("original", original.get(local));
        assertEquals("original", original.getBindings().get("local"));
        assertEquals("copy", copy.getBindings().get("local"));
        assertEquals("copy", copy.getBindings().get("local-alias"));

        copy.remove(local);
        assertFalse(copy.getBindings().containsKey("local"));
        assertTrue(original.getBindings().containsKey("local"));
    }

    @Test
    void clonePreservesSharedGlobalAndResetsSyncForCurrentThread() {
        Map<String, Object> global = new HashMap<>();
        ActionContext original = new ActionContext((Object) null, global, null);
        ContextKey<String> globalKey = new ContextKey<>(true, "shared");
        original.set(globalKey, "original");
        original.setSync(true);

        mockedBukkit.when(Bukkit::isPrimaryThread).thenReturn(true);
        ActionContext copy = original.clone();

        assertSame(global, copy.getGlobal());
        assertTrue(copy.isSync());
        assertTrue(original.isSync());

        copy.set(globalKey, "copy");
        assertEquals("copy", global.get("shared"));
        assertEquals("original", original.getBindings().get("shared"));
        assertEquals("copy", copy.getBindings().get("shared"));

        copy.setSync(false);
        assertTrue(original.isSync());
        assertFalse(copy.isSync());

        mockedBukkit.when(Bukkit::isPrimaryThread).thenReturn(false);
        assertFalse(original.clone().isSync());
    }

    @Test
    void clonePreservesSubclassTypeAndFields() {
        DerivedContext original = new DerivedContext("marker");

        ActionContext cloned = original.clone();

        assertInstanceOf(DerivedContext.class, cloned);
        assertEquals("marker", ((DerivedContext) cloned).getMarker());
    }

    private static final class DerivedContext extends ActionContext {
        private final String marker;

        private DerivedContext(String marker) {
            super((Object) null);
            this.marker = marker;
        }

        private String getMarker() {
            return marker;
        }
    }
}
