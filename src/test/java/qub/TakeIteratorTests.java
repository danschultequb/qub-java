package qub;

public class TakeIteratorTests extends IteratorTests
{
    @Override
    protected Iterator<Integer> createIterator(int count, boolean started)
    {
        final int additionalValues = 5;

        final Array<Integer> array = new Array<>(count + additionalValues);
        for (int i = 0; i < count + additionalValues; ++i)
        {
            array.set(i, i);
        }

        final Iterator<Integer> iterator = array.iterate().take(count);

        if (started)
        {
            iterator.next();
        }

        return iterator;
    }
}