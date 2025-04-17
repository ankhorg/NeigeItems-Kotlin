package pers.neige.neigeitems;

import org.junit.jupiter.api.Test;
import pers.neige.neigeitems.utils.NumberParser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class NumberParserTest {
    @Test
    public void parseInteger() {
        String string = "123";
        assertEquals(123, NumberParser.parseInteger(string));

        string = "+123";
        assertEquals(123, NumberParser.parseInteger(string));

        string = "-123";
        assertEquals(-123, NumberParser.parseInteger(string));

        string = "123";
        assertEquals(123, NumberParser.parseInteger(string));

        string = "+123";
        assertEquals(123, NumberParser.parseInteger(string));

        string = "-123";
        assertEquals(-123, NumberParser.parseInteger(string));

        string = String.valueOf(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, NumberParser.parseInteger(string));

        string = String.valueOf(Integer.MAX_VALUE - 1);
        assertEquals(Integer.MAX_VALUE - 1, NumberParser.parseInteger(string));

        string = Integer.MAX_VALUE + "1";
        assertEquals(Integer.MAX_VALUE, NumberParser.parseInteger(string));

        string = Integer.MAX_VALUE + "1a";
        assertNull(NumberParser.parseInteger(string));
        assertEquals(114514, NumberParser.parseInteger(string, 114514));

        string = String.valueOf(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, NumberParser.parseInteger(string));

        string = String.valueOf(Integer.MIN_VALUE + 1);
        assertEquals(Integer.MIN_VALUE + 1, NumberParser.parseInteger(string));

        string = Integer.MIN_VALUE + "1";
        assertEquals(Integer.MIN_VALUE, NumberParser.parseInteger(string));

        string = Integer.MIN_VALUE + "1a";
        assertNull(NumberParser.parseInteger(string));
        assertEquals(114514, NumberParser.parseInteger(string, 114514));

        string = "hello";
        assertNull(NumberParser.parseInteger(string));
        assertEquals(114514, NumberParser.parseInteger(string, 114514));

        string = "123.3";
        assertNull(NumberParser.parseInteger(string));
        assertEquals(114514, NumberParser.parseInteger(string, 114514));

        string = "123hello";
        assertNull(NumberParser.parseInteger(string));
        assertEquals(114514, NumberParser.parseInteger(string, 114514));

        string = "+";
        assertNull(NumberParser.parseInteger(string));
        assertEquals(114514, NumberParser.parseInteger(string, 114514));

        string = "-";
        assertNull(NumberParser.parseInteger(string));
        assertEquals(114514, NumberParser.parseInteger(string, 114514));

        string = "";
        assertNull(NumberParser.parseInteger(string));
        assertEquals(114514, NumberParser.parseInteger(string, 114514));
    }

    @Test
    public void parseDouble() {
        String string = "123.456";
        assertEquals(123.456, NumberParser.parseDouble(string));

        string = "+123.456";
        assertEquals(123.456, NumberParser.parseDouble(string));

        string = "-123.456";
        assertEquals(-123.456, NumberParser.parseDouble(string));

        string = ".456";
        assertEquals(0.456, NumberParser.parseDouble(string));

        string = "+.456";
        assertEquals(0.456, NumberParser.parseDouble(string));

        string = "-.456";
        assertEquals(-0.456, NumberParser.parseDouble(string));

        string = "123.";
        assertEquals(123.0, NumberParser.parseDouble(string));

        string = "+123.";
        assertEquals(123.0, NumberParser.parseDouble(string));

        string = "-123.";
        assertEquals(-123.0, NumberParser.parseDouble(string));

        string = "123";
        assertEquals(123.0, NumberParser.parseDouble(string));

        string = "+123";
        assertEquals(123.0, NumberParser.parseDouble(string));

        string = "-123";
        assertEquals(-123.0, NumberParser.parseDouble(string));

        string = "123.456";
        assertEquals(123.456, NumberParser.parseDouble(string));

        string = "+123.456";
        assertEquals(123.456, NumberParser.parseDouble(string));

        string = "-123.456";
        assertEquals(-123.456, NumberParser.parseDouble(string));

        string = ".456";
        assertEquals(0.456, NumberParser.parseDouble(string));

        string = "+.456";
        assertEquals(0.456, NumberParser.parseDouble(string));

        string = "-.456";
        assertEquals(-0.456, NumberParser.parseDouble(string));

        string = "123.";
        assertEquals(123.0, NumberParser.parseDouble(string));

        string = "+123.";
        assertEquals(123.0, NumberParser.parseDouble(string));

        string = "-123.";
        assertEquals(-123.0, NumberParser.parseDouble(string));

        string = "123";
        assertEquals(123.0, NumberParser.parseDouble(string));

        string = "+123";
        assertEquals(123.0, NumberParser.parseDouble(string));

        string = "-123";
        assertEquals(-123.0, NumberParser.parseDouble(string));

        string = "hello";
        assertNull(NumberParser.parseDouble(string));
        assertEquals(114514.0, NumberParser.parseDouble(string, 114514.0));

        string = "123.456hello";
        assertNull(NumberParser.parseDouble(string));
        assertEquals(114514.0, NumberParser.parseDouble(string, 114514.0));

        string = "123..456";
        assertNull(NumberParser.parseDouble(string));
        assertEquals(114514.0, NumberParser.parseDouble(string, 114514.0));

        string = "+";
        assertNull(NumberParser.parseDouble(string));
        assertEquals(114514.0, NumberParser.parseDouble(string, 114514.0));

        string = "-";
        assertNull(NumberParser.parseDouble(string));
        assertEquals(114514.0, NumberParser.parseDouble(string, 114514.0));

        string = ".";
        assertNull(NumberParser.parseDouble(string));
        assertEquals(114514.0, NumberParser.parseDouble(string, 114514.0));

        string = "+.";
        assertNull(NumberParser.parseDouble(string));
        assertEquals(114514.0, NumberParser.parseDouble(string, 114514.0));

        string = "-.";
        assertNull(NumberParser.parseDouble(string));
        assertEquals(114514.0, NumberParser.parseDouble(string, 114514.0));

        string = "";
        assertNull(NumberParser.parseDouble(string));
        assertEquals(114514.0, NumberParser.parseDouble(string, 114514.0));
    }
}
