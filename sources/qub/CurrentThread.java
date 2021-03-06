package qub;

public final class CurrentThread
{
    /**
     * The mapping of thread IDs to registered ResultAsyncSchedulers.
     */
    private static MutableMap<Long,AsyncScheduler> asyncSchedulers = new ConcurrentHashMap<>();

    /**
     * Get the ID of the current thread.
     * @return The ID of the current thread.
     */
    static long getId()
    {
        return java.lang.Thread.currentThread().getId();
    }

    /**
     * Yield the current thread's execution so that the scheduler can execute a different thread.
     */
    static void yield()
    {
        java.lang.Thread.yield();
    }

    /**
     * Set the AsyncScheduler that will be registered with the current thread.
     * @param asyncRunner The AsyncScheduler that will be registered with the current thread.
     */
    static void setAsyncRunner(AsyncScheduler asyncRunner)
    {
        final long currentThreadId = getId();
        if (asyncRunner == null)
        {
            CurrentThread.asyncSchedulers.remove(currentThreadId)
                .catchError(NotFoundException.class)
                .await();
        }
        else
        {
            CurrentThread.asyncSchedulers.set(currentThreadId, asyncRunner);
        }
    }

    /**
     * Get the AsyncScheduler that has been registered with the current thread.
     * @return The AsyncScheduler that has been registered with the current thread.
     */
    static Result<AsyncScheduler> getAsyncRunner()
    {
        final long currentThreadId = getId();
        return CurrentThread.asyncSchedulers.get(currentThreadId)
            .convertError(NotFoundException.class, () -> new NotFoundException("No AsyncRunner has been registered with thread id " + currentThreadId + "."));
    }

    /**
     * Run the provided action using the provided ManualAsyncRunner as the current thread's
     * AsyncRunner. The previous AsyncRunner for the current thread will be returned to the current
     * thread's AsyncRunner when this function is finished.
     * @param asyncScheduler The AsyncScheduler to use for the current thread for the duration
     *                    of the provided action.
     * @param action The action to run.
     */
    static void withAsyncScheduler(AsyncScheduler asyncScheduler, Action0 action)
    {
        PreCondition.assertNotNull(asyncScheduler, "asyncScheduler");
        PreCondition.assertNotNull(action, "action");

        final AsyncScheduler backupAsyncScheduler = CurrentThread.getAsyncRunner().catchError(NotFoundException.class).await();
        CurrentThread.setAsyncRunner(asyncScheduler);
        try
        {
            action.run();
        }
        finally
        {
            CurrentThread.setAsyncRunner(backupAsyncScheduler);
        }
    }

    static void withManualAsyncScheduler(Action1<ManualAsyncRunner> action)
    {
        PreCondition.assertNotNull(action, "action");

        CurrentThread.withAsyncScheduler(ManualAsyncRunner::create, action::run);
    }

    static void withParallelAsyncScheduler(Action1<ParallelAsyncRunner> action)
    {
        PreCondition.assertNotNull(action, "action");

        CurrentThread.withAsyncScheduler(ParallelAsyncRunner::create, action::run);
    }

    static <T extends AsyncScheduler> void withAsyncScheduler(Function0<T> creator, Action1<T> action)
    {
        PreCondition.assertNotNull(creator, "creator");
        PreCondition.assertNotNull(action, "action");

        final T asyncScheduler = creator.run();
        CurrentThread.withAsyncScheduler(asyncScheduler, () -> action.run(asyncScheduler));
    }

    /**
     * Run the provided action with a temporary AsyncScheduler that will only exist for the
     * duration of the action.
     * @param action The action to run.
     */
    static void withAsyncScheduler(Action1<AsyncScheduler> action)
    {
        PreCondition.assertNotNull(action, "action");

        CurrentThread.withAsyncScheduler(ManualAsyncRunner::create, action);
    }
}
