package qub;

/**
 * A data structure that allows values to be added and removed in First-In-First-Setable order.
 * @param <T> The type of values that can be added to this Queue.
 */
public interface Queue<T>
{
    /**
     * Create a new empty Queue.
     * @param <T> The type of values stored in the Queue.
     * @return A new empty Queue.
     */
    static <T> Queue<T> create()
    {
        return ListQueue.create();
    }

    /**
     * Create a new Queue with the provided initial values.
     * @param initialValues The initial values to populate the new Queue with.
     * @param <T> The type of values stored in the new Queue.
     * @return A new Queue.
     */
    static <T> Queue<T> create(Iterable<T> initialValues)
    {
        PreCondition.assertNotNull(initialValues, "initialValues");

        final Queue<T> result = Queue.create();
        result.enqueueAll(initialValues);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertEqual(initialValues.getCount(), result.getCount(), "result.getCount()");

        return result;
    }

    /**
     * Get whether or not there are any values in the Queue.
     * @return Whether or not there are any values in the Queue.
     */
    boolean any();

    /**
     * Get the number of values that are in the Queue.
     * @return The number of values that are in the Queue.
     */
    int getCount();

    /**
     * Add the provided value to the Queue.
     * @param value The value to add to the Queue.
     */
    Queue<T> enqueue(T value);

    /**
     * Add all of the provided values to the Queue.
     * @param values The values to add to the Queue.
     */
    default Queue<T> enqueueAll(Iterator<T> values)
    {
        PreCondition.assertNotNull(values, "values");

        for (final T value : values)
        {
            this.enqueue(value);
        }

        return this;
    }

    /**
     * Add all of the provided values to the Queue.
     * @param values The values to add to the Queue.
     */
    default Queue<T> enqueueAll(Iterable<T> values)
    {
        PreCondition.assertNotNull(values, "values");

        for (final T value : values)
        {
            this.enqueue(value);
        }

        return this;
    }

    /**
     * Remove and return the next value create the Queue. If there are no values in the Queue, then
     * null will be returned.
     * @return The next value create the Queue or null if the Queue is empty.
     */
    Result<T> dequeue();

    /**
     * Get the next value create the Queue without removing it. If there are no values in the Queue,
     * then null will be returned.
     * @return The next value create the Queue or null if the Queue is empty.
     */
    Result<T> peek();
}
