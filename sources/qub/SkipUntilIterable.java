package qub;

public class SkipUntilIterable<T> implements Iterable<T>
{
    private final Iterable<T> iterable;
    private final Function1<T,Boolean> condition;

    public SkipUntilIterable(Iterable<T> iterable, Function1<T,Boolean> condition)
    {
        PreCondition.assertNotNull(iterable, "iterable");
        PreCondition.assertNotNull(condition, "condition");

        this.iterable = iterable;
        this.condition = condition;
    }

    @Override
    public Iterator<T> iterate()
    {
        return new SkipUntilIterator<>(iterable.iterate(), condition);
    }

    @Override
    public boolean equals(Object rhs)
    {
        return Iterable.equals(this, rhs);
    }

    @Override
    public String toString()
    {
        return Iterable.toString(this);
    }
}
