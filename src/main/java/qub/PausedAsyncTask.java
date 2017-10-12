package qub;

/**
 * A PausedAsyncTask that can be scheduled on the AsyncRunner that created it by calling this
 * PausedAsyncTasks's schedule() function.
 */
public interface PausedAsyncTask
{
    /**
     * Schedule this PausedAsyncTask to run on the AsyncRunner that created it.
     */
    void schedule();
}