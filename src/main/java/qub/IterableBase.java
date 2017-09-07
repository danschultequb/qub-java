package qub;

/**
 * An abstract base implementation of Iterable that contains the common implementation of many of
 * Iterable's methods.
 * @param <T> The type of value that this IterableBase contains.
 */
public abstract class IterableBase<T> implements Iterable<T>
{
    @Override
    public boolean any(Function1<T,Boolean> condition)
    {
        return iterate().any(condition);
    }

    @Override
    public Iterable<T> take(int toTake)
    {
        return new TakeIterable<>(this, toTake);
    }

    @Override
    public Iterable<T> skip(int toSkip)
    {
        return toSkip <= 0 ? this : new SkipIterable<>(this, toSkip);
    }

    @Override
    public Iterable<T> where(Function1<T,Boolean> condition)
    {
        return condition == null ? this : new WhereIterable<>(this, condition);
    }

    @Override
    public java.util.Iterator<T> iterator()
    {
        return iterate().iterator();
    }
}