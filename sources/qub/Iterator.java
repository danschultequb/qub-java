package qub;

/**
 * The Iterator interface defines a type that can synchronously iterate over a collection of values.
 * @param <T> The type of value that the Iterator returns.
 */
public interface Iterator<T> extends java.lang.Iterable<T>
{
    /**
     * Ensure that this Iterator has started. If it hasn't started, then it will be moved to the
     * next value.
     */
    default void ensureHasStarted()
    {
        if (!hasStarted())
        {
            next();
        }
    }

    /**
     * Whether or not this Iterator has begun iterating over its values.
     * @return Whether or not this Iterator has begun iterating over its values.
     */
    boolean hasStarted();

    /**
     * Whether or not this Iterator has a current value.
     * @return Whether or not this Iterator has a current value.
     */
    boolean hasCurrent();

    /**
     * Get the current value that this Iterator is pointing at.
     * @return The current value that this Iterator is pointing at.
     */
    T getCurrent();

    /**
     * Get the next value for this Iterator. Returns whether or not a new value was found.
     * @return Whether or not a new value was found.
     */
    boolean next();

    /**
     * Return the current value for this Iterator and advance this Iterator to the next value.
     * @return The current value for this Iterator.
     */
    default T takeCurrent()
    {
        final T current = getCurrent();
        next();
        return current;
    }

    /**
     * Get whether or not this Iterator contains any values. This function may move this Iterator
     * forward one position, but it can be called multiple times without consuming any of the
     * values in this Iterator.
     * @return Whether or not this Iterator contains any values.
     */
    default boolean any()
    {
        return hasCurrent() || next();
    }

    /**
     * Get the number of values that are in this Iterator. This will iterate through all of the
     * values in this Iterator. Use this method only if you care how many values are in the
     * Iterator, not what the values actually are.
     * @return The number of values that are in this Iterator.
     */
    default int getCount()
    {
        int result = hasCurrent() ? 1 : 0;
        while (next()) {
            ++result;
        }
        return result;
    }

    /**
     * Get the first value in this Iterator. This may advance the Iterator once.
     * @return The first value of this Iterator, or null if this Iterator has no (more) values.
     */
    default T first()
    {
        if (!hasStarted())
        {
            next();
        }
        return getCurrent();
    }

    /**
     * Get the first value in this Iterator that matches the provided condition.
     * @param condition The condition to run against each of the values in this Iterator.
     * @return The first value of this Iterator that matches the provided condition, or null if this
     * Iterator has no values that match the condition.
     */
    default T first(Function1<T,Boolean> condition)
    {
        T result = null;

        if (condition != null)
        {
            if (hasCurrent() && condition.run(getCurrent()))
            {
                result = getCurrent();
            }
            else
            {
                while (next())
                {
                    if (condition.run(getCurrent()))
                    {
                        result = getCurrent();
                        break;
                    }
                }
            }
        }

        return result;
    }

    /**
     * Get the last value in this Iterator. This will iterate through all of the values in this
     * Iterator.
     * @return The last value of this Iterator, or null if this Iterator has no (more) values.
     */
    default T last()
    {
        T result = null;

        if (hasCurrent())
        {
            result = getCurrent();
        }

        while (next())
        {
            result = getCurrent();
        }

        return result;
    }

    /**
     * Get the last value in this Iterator that matches the provided condition.
     * @param condition The condition to run against each of the values in this Iterator.
     * @return The last value of this Iterator that matches the provided condition, or null if this
     * Iterator has no values that match the condition.
     */
    default T last(Function1<T,Boolean> condition)
    {
        T result = null;

        if (condition != null)
        {
            if (hasCurrent() && condition.run(getCurrent()))
            {
                result = getCurrent();
            }

            while (next())
            {
                if (condition.run(getCurrent()))
                {
                    result = getCurrent();
                }
            }
        }

        return result;
    }

    /**
     * Get whether or not this Iterator contains the provided value using the standard equals()
     * method to compare values.
     * @param value The value to look for in this Iterator.
     * @return Whether or not this Iterator contains the provided value.
     */
    default boolean contains(T value)
    {
        return contains((T iteratorValue) -> Comparer.equal(iteratorValue, value));
    }

    /**
     * Get whether or not this Iterator contains a value that matches the provided condition.
     * @param condition The condition to check against the values in this Iterator.
     * @return Whether or not this Iterator contains a value that matches the provided condition.
     */
    default boolean contains(Function1<T,Boolean> condition)
    {
        boolean result = false;

        if (condition != null)
        {
            if (hasCurrent())
            {
                result = condition.run(getCurrent());
            }

            while (!result && next())
            {
                if (condition.run(getCurrent()))
                {
                    result = condition.run(getCurrent());
                }
            }
        }

        return result;
    }

    /**
     * Create a new Iterator that will iterate over no more than the provided number of values from
     * this Iterator.
     * @param toTake The number of values to take from this Iterator.
     * @return A new Iterator that will iterate over no more than the provided number of values from
     * this Iterator.
     */
    default Iterator<T> take(int toTake)
    {
        return new TakeIterator<>(this, toTake);
    }

    /**
     * Create a new Iterator that will skip over the first toSkip number of elements in this
     * Iterator and then iterate over the remaining elements.
     * @param toSkip The number of elements to skip.
     * @return A new Iterator that will skip over the first toSkip number of elements in this
     * Iterator and then iterate over the remaining elements.
     */
    default Iterator<T> skip(int toSkip)
    {
        return new SkipIterator<>(this, toSkip);
    }

    /**
     * Create a new Iterator that will skip over the elements in this Iterator until it finds an
     * element that makes the provided condition true. The returned Iterator will start at the
     * element after the element that made the condition true.
     * @param condition The condition.
     * @return a new Iterator that will skip over the elements in this Iterator until it finds an
     * element that makes the provided condition true.
     */
    default Iterator<T> skipUntil(Function1<T,Boolean> condition)
    {
        return new SkipUntilIterator<>(this, condition);
    }

    /**
     * Create a new Iterator that only returns the values from this Iterator that satisfy the given
     * condition.
     * @param condition The condition values must satisfy to be returned from the created Iterator.
     * @return An Iterator that only returns the values from this Iterator that satisfy the given
     * condition.
     */
    default Iterator<T> where(Function1<T,Boolean> condition)
    {
        return condition == null ? this : new WhereIterator<>(this, condition);
    }

    /**
     * Convert this Iterator into an Iterator that returns values of type U instead of type T.
     * @param conversion The function to use to convert values of type T to type U.
     * @param <U> The type to convert values of type T to.
     * @return An Iterator that returns values of type U instead of type T.
     */
    default <U> Iterator<U> map(Function1<T,U> conversion)
    {
        return new MapIterator<>(this, conversion);
    }

    /**
     * Convert this Iterator into an Iterator that only returns the values in this Iterator that are
     * of type or sub-classes of type U.
     * @param type The type to filter the results to.
     * @param <U> The type to return.
     * @return An Iterator that only returns the values in this Iterator that are of type of
     * sub-classes of type U.
     */
    default <U> Iterator<U> instanceOf(Class<U> type)
    {
        return new InstanceOfIterator<>(this, type);
    }

    /**
     * Create a java.util.Iterator that will iterate over this Iterator.
     * @return A java.util.Iterator that will iterate over this Iterator.
     */
    default java.util.Iterator<T> iterator()
    {
        return new IteratorToJavaIteratorAdapter<>(this);
    }
}