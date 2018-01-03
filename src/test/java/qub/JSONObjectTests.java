package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class JSONObjectTests
{
    @Test
    public void constructor()
    {
        constructorTest("{",
            JSONToken.leftCurlyBracket(0),
            new JSONProperty[0],
            null,
            0,
            1,
            1);

        constructorTest("{}",
            JSONToken.leftCurlyBracket(0),
            new JSONProperty[0],
            JSONToken.rightCurlyBracket(1),
            0,
            2,
            2);

        constructorTest("{\"a\":\"b\"}",
            JSONToken.leftCurlyBracket(0),
            new JSONProperty[]
            {
                JSON.parseProperty("\"a\":\"b\"", 1)
            },
            JSONToken.rightCurlyBracket(8),
            0,
            9,
            9);

        constructorTest("{\"1\":\"2\", \"3\":\"4\"}",
            JSONToken.leftCurlyBracket(0),
            new JSONProperty[]
                {
                    JSON.parseProperty("\"1\":\"2\"", 1),
                    JSON.parseProperty("\"3\":\"4\"", 10)
                },
            JSONToken.rightCurlyBracket(17),
            0,
            18,
            18);
    }

    private static void constructorTest(String text, JSONToken leftCurlyBracket, JSONProperty[] propertySegments, JSONToken rightCurlyBracket, int startIndex, int afterEndIndex, int length)
    {
        final JSONObject objectSegment = JSON.parseObject(text);
        assertEquals(leftCurlyBracket, objectSegment.getLeftCurlyBracket());
        assertEquals(rightCurlyBracket, objectSegment.getRightCurlyBracket());
        assertEquals(Array.fromValues(propertySegments), objectSegment.getProperties());
        assertEquals(startIndex, objectSegment.getStartIndex());
        assertEquals(afterEndIndex, objectSegment.getAfterEndIndex());
        assertEquals(length, objectSegment.getLength());
        assertEquals(text, objectSegment.toString());
    }

    @Test
    public void getPropertySegment()
    {
        final JSONObject objectSegment = JSON.parseObject("{ \"a\":1, \"b\": 2 }");
        assertNull(objectSegment.getProperty(null));
        assertNull(objectSegment.getProperty(""));
        assertNull(objectSegment.getProperty("c"));
        assertNull(objectSegment.getProperty("\"c\""));
        assertEquals(JSON.parseProperty("\"a\":1", 2), objectSegment.getProperty("\"a\""));
        assertEquals(JSON.parseProperty("\"a\":1", 2), objectSegment.getProperty("a"));
        assertEquals(JSON.parseProperty("\"b\": 2", 9), objectSegment.getProperty("\"b\""));
        assertEquals(JSON.parseProperty("\"b\": 2", 9), objectSegment.getProperty("b"));
    }

    @Test
    public void getPropertyValueSegment()
    {
        final JSONObject objectSegment = JSON.parseObject("{ \"a\":1, \"b\": 2 }");
        assertNull(objectSegment.getPropertyValue(null));
        assertNull(objectSegment.getPropertyValue(""));
        assertNull(objectSegment.getPropertyValue("c"));
        assertNull(objectSegment.getPropertyValue("\"c\""));
        assertEquals(JSONToken.number("1", 6), objectSegment.getPropertyValue("\"a\""));
        assertEquals(JSONToken.number("1", 6), objectSegment.getPropertyValue("a"));
        assertEquals(JSONToken.number("2", 14), objectSegment.getPropertyValue("\"b\""));
        assertEquals(JSONToken.number("2", 14), objectSegment.getPropertyValue("b"));
    }
}
