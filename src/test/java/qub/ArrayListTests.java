package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class ArrayListTests extends IterableTests
{
    @Override
    protected Iterable<Integer> createIterable(int count)
    {
        final ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < count; ++i)
        {
            result.add(i);
        }
        return result;
    }

    @Test
    public void add()
    {
        final ArrayList<Integer> arrayList = new ArrayList<>();
        assertEquals(0, arrayList.getCount());
        assertFalse(arrayList.any());
        assertEquals(null, arrayList.get(0));

        for (int i = 0; i < 100; ++i)
        {
            arrayList.add(100 - i);
            assertEquals(i + 1, arrayList.getCount());
            assertTrue(arrayList.any());
            assertEquals(100 - i, arrayList.get(i).intValue());
        }
    }

    @Test
    public void addAll()
    {
        final ArrayList<Integer> arrayList = new ArrayList<>();
        assertEquals(0, arrayList.getCount());
        assertFalse(arrayList.any());
        assertEquals(null, arrayList.get(0));

        arrayList.addAll();
        assertEquals(0, arrayList.getCount());
        assertFalse(arrayList.any());
        assertEquals(null, arrayList.get(0));

        arrayList.addAll(0);
        assertEquals(1, arrayList.getCount());
        assertTrue(arrayList.any());
        assertEquals(0, arrayList.get(0).intValue());

        arrayList.addAll(1, 2, 3, 4, 5);
        assertEquals(6, arrayList.getCount());
        assertTrue(arrayList.any());
        for (int i = 0; i < arrayList.getCount(); ++i)
        {
            assertEquals(i, arrayList.get(i).intValue());
        }
    }

    @Test
    public void set()
    {
        final ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = -1; i <= 1; ++i)
        {
            arrayList.set(i, i);
            assertNull(arrayList.get(i));
        }

        for (int i = 0; i < 10; ++i)
        {
            arrayList.add(i);
        }

        for (int i = 0; i < arrayList.getCount(); ++i)
        {
            arrayList.set(i, -arrayList.get(i));
        }

        for (int i = 0; i < arrayList.getCount(); ++i)
        {
            assertEquals(-i, arrayList.get(i).intValue());
        }
    }

    @Test
    public void removeAt()
    {
        final ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = -1; i <= 1; ++i)
        {
            assertNull(arrayList.removeAt(i));
        }
        assertEquals(0, arrayList.getCount());

        for (int i = 0; i < 10; ++i)
        {
            arrayList.add(i);
        }
        assertEquals(10, arrayList.getCount());

        assertEquals(0, arrayList.removeAt(0).intValue());
        assertEquals(9, arrayList.getCount());

        assertEquals(9, arrayList.removeAt(8).intValue());
        assertEquals(8, arrayList.getCount());

        assertEquals(5, arrayList.removeAt(4).intValue());
        assertEquals(7, arrayList.getCount());
    }
}
