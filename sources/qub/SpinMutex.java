package qub;

import java.util.concurrent.atomic.AtomicLong;

/**
 * A mutex/lock that can be used to synchronize access to shared resources between multiple threads.
 * This lock uses an atomic variable to synchronize access to shared resources, and as such doesn't
 * require OS interaction.
 */
public class SpinMutex extends MutexBase
{
    private final AtomicLong acquiredByThreadId;

    /**
     * Create a new SpinMutex that is ready to be acquired.
     */
    public SpinMutex()
    {
        acquiredByThreadId = new AtomicLong(-1);
    }

    /**
     * Get whether or not this SpinMutex is currently acquired.
     * @return Whether or not this SpinMutex is currently acquired.
     */
    @Override
    public boolean isAcquired()
    {
        return acquiredByThreadId.get() != -1;
    }

    /**
     * Acquire this mutex. If the mutex is already acquired, this thread will block until the owning
     * thread releases this mutex and this thread acquires the mutex.
     */
    @Override
    public void acquire()
    {
        final long threadId = Thread.currentThread().getId();
        while (!acquiredByThreadId.compareAndSet(-1, threadId))
        {
            while (isAcquired())
            {
            }
        }
    }

    /**
     * Attempt to acquire this SpinMutex and return whether or not it was acquired.
     * @return Whether or not the SpinMutex was acquired.
     */
    @Override
    public boolean tryAcquire()
    {
        final long threadId = Thread.currentThread().getId();
        return acquiredByThreadId.get() == threadId || acquiredByThreadId.compareAndSet(-1, threadId);
    }

    /**
     * Release this SpinMutex so that other threads can acquire it.
     * @return Whether or not this SpinMutex was released.
     */
    @Override
    public boolean release()
    {
        final long threadId = Thread.currentThread().getId();
        return acquiredByThreadId.compareAndSet(threadId, -1);
    }
}
