package qub;

public class ByteArrayIterator implements Iterator<Byte>
{
    private final byte[] bytes;
    private final int startIndex;
    private final int length;
    private boolean hasStarted;
    private int currentIndex;

    public ByteArrayIterator(byte[] bytes)
    {
        this(bytes, 0, bytes.length);
    }

    public ByteArrayIterator(byte[] bytes, int startIndex, int length)
    {
        PreCondition.assertNotNull(bytes, "bytes");
        PreCondition.assertStartIndex(startIndex, bytes.length);
        PreCondition.assertLength(length, startIndex, bytes.length);

        this.bytes = bytes;
        this.startIndex = startIndex;
        this.length = length;
    }

    @Override
    public boolean hasStarted()
    {
        return hasStarted;
    }

    @Override
    public boolean hasCurrent()
    {
        return hasStarted && currentIndex < length;
    }

    @Override
    public Byte getCurrent()
    {
        PreCondition.assertTrue(hasCurrent(), "hasCurrent()");

        return bytes[currentIndex];
    }

    @Override
    public boolean next()
    {
        if (!hasStarted)
        {
            hasStarted = true;
        }
        else if (currentIndex < length)
        {
            ++currentIndex;
        }
        return currentIndex < length;
    }

    /**
     * Create an iterator for the provided values.
     * @param values The values to iterate over.
     * @return The iterator that will iterate over the provided values.
     */
    static ByteArrayIterator create(byte... values)
    {
        return new ByteArrayIterator(values);
    }

    /**
     * Create an iterator for the provided values.
     * @param values The values to iterate over.
     * @return The iterator that will iterate over the provided values.
     */
    static ArrayIterator<Byte> create(int... values)
    {
        return new ByteArray(values).iterate();
    }

    /**
     * Create an Iterator for the provided values.
     * @param values The values to iterate.
     * @return The Iterator for the provided values.
     */
    static ByteArrayIterator create(byte[] values, int startIndex, int length)
    {
        PreCondition.assertNotNull(values, "values");
        PreCondition.assertStartIndex(startIndex, values.length);
        PreCondition.assertLength(length, startIndex, values.length);

        return new ByteArrayIterator(values, startIndex, length);
    }
}
