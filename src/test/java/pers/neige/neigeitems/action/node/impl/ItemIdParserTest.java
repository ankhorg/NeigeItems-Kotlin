package pers.neige.neigeitems.action.node.impl;

import lombok.val;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ContextKeys;
import pers.neige.neigeitems.manager.BaseActionManager;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("ItemIdParser")
public class ItemIdParserTest {
    private ActionContext context;
    private ItemIdParser parser;

    @BeforeEach
    void setUp() {
        val manager = mock(BaseActionManager.class);
        context = mock(ActionContext.class);
        val plugin = mock(Plugin.class);
        when(plugin.getLogger()).thenReturn(Logger.getLogger("ItemIdParserTest"));
        when(manager.getPlugin()).thenReturn(plugin);
        parser = new ItemIdParser(manager);
    }

    @Test
    void shouldExposeId() {
        assertEquals("item_id", parser.getId());
    }

    @Test
    void shouldReturnNullWithoutItemStack() {
        when(context.get(ContextKeys.ITEM_STACK)).thenReturn(null);
        assertNull(parser.parse(context));
    }
}
