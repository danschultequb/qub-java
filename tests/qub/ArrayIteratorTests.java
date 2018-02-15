package qub;

public class ArrayIteratorTests
{
    public static void test(final TestRunner runner)
    {
        runner.testGroup("ArrayIterator<T>", () ->
        {
            IteratorTests.test(runner, ArrayIteratorTests::createIterator);
        });
    }

    public static ArrayIterator<Integer> createIterator(int count, boolean started)
    {
        final Array<Integer> a = new Array<>(count);
        for (int i = 0; i < count; ++i) {
            a.set(i, i);
        }

        final ArrayIterator<Integer> iterator = new ArrayIterator<>(a);
        if (started)
        {
            iterator.next();
        }

        return iterator;
    }
}
