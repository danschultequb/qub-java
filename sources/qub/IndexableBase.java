package qub;

public abstract class IndexableBase<T> extends IterableBase<T> implements Indexable<T>
{
    @Override
    public final int indexOf(Function1<T, Boolean> condition)
    {
        return IndexableBase.indexOf(this, condition);
    }

    @Override
    public final int indexOf(T value)
    {
        return IndexableBase.indexOf(this, value);
    }

    @Override
    public final <U> Indexable<U> map(Function1<T,U> conversion)
    {
        return IndexableBase.map(this, conversion);
    }

    /**
     * Get the index of the first element in this Indexable that satisfies the provided condition,
     * or -1 if no element matches the condition.
     * @param condition The condition to compare against the elements in this Indexable.
     * @return The index of the first element that satisfies the provided condition or -1 if no
     * element matches the condition.
     */
    public static <T> int indexOf(Indexable<T> indexable, Function1<T,Boolean> condition)
    {
        int result = -1;
        if (condition != null)
        {
            int index = 0;
            for (final T element : indexable)
            {
                if (condition.run(element))
                {
                    result = index;
                    break;
                }
                else
                {
                    ++index;
                }
            }
        }
        return result;
    }

    /**
     * Get the index of the first element in this Indexable that equals the provided value or -1 if
     * no element equals the value.
     * @param value The value to look for in this Indexable.
     * @return The index of the first element that equals the provided value or -1 if no element
     * equals the provided value.
     */
    public static <T> int indexOf(Indexable<T> indexable, final T value)
    {
        return indexable.indexOf(new Function1<T, Boolean>()
        {
            @Override
            public Boolean run(T element)
            {
                return Comparer.equal(element, value);
            }
        });
    }

    /**
     * Convert this Indexable into an Indexable that returns values of type U instead of type T.
     * @param conversion The function to use to convert values of type T to type U.
     * @param <U> The type to convert values of type T to.
     * @return An Indexable that returns values of type U instead of type T.
     */
    public static <T,U> Indexable<U> map(Indexable<T> indexable, Function1<T,U> conversion)
    {
        return new MapIndexable<>(indexable, conversion);
    }
}