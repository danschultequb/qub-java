package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class BasicAsyncActionTests extends BasicAsyncTaskTests
{
    @Override
    protected BasicAsyncAction create(AsyncRunner runner)
    {
        return new BasicAsyncAction(runner, TestUtils.emptyAction0);
    }

    private CurrentThreadAsyncRunner createCurrentThreadAsyncRunner()
    {
        final Synchronization synchronization = new Synchronization();
        return new CurrentThreadAsyncRunner(new Function0<Synchronization>()
        {
            @Override
            public Synchronization run()
            {
                return synchronization;
            }
        });
    }

    private BasicAsyncAction create()
    {
        final CurrentThreadAsyncRunner runner = createCurrentThreadAsyncRunner();
        return create(runner);
    }

    private BasicAsyncAction createScheduled(AsyncRunner runner)
    {
        final BasicAsyncAction basicAsyncAction = create(runner);
        basicAsyncAction.schedule();
        return basicAsyncAction;
    }

    @Test
    public void constructor()
    {
        final CurrentThreadAsyncRunner runner = createCurrentThreadAsyncRunner();
        final BasicAsyncAction basicAsyncAction = new BasicAsyncAction(runner, TestUtils.emptyAction0);
        assertEquals(0, runner.getScheduledTaskCount());
        assertEquals(0, basicAsyncAction.getPausedTaskCount());
        assertFalse(basicAsyncAction.isCompleted());
    }

    @Test
    public void thenOnAsyncRunnerWithNullRunner()
    {
        final CurrentThreadAsyncRunner runner = createCurrentThreadAsyncRunner();
        final BasicAsyncAction basicAsyncAction = create(runner);
        assertNull(basicAsyncAction.thenOn(null));
    }

    @Test
    public void thenOnAsyncRunnerWithSameRunner()
    {
        final CurrentThreadAsyncRunner runner = createCurrentThreadAsyncRunner();
        final BasicAsyncAction basicAsyncAction = createScheduled(runner);
        assertSame(basicAsyncAction, basicAsyncAction.thenOn(runner));
        assertEquals(0, basicAsyncAction.getPausedTaskCount());
        assertEquals(1, runner.getScheduledTaskCount());
    }

    @Test
    public void thenOnAsyncRunnerWithDifferentRunner()
    {
        final CurrentThreadAsyncRunner runner = createCurrentThreadAsyncRunner();
        final BasicAsyncAction basicAsyncAction = createScheduled(runner);

        final CurrentThreadAsyncRunner runner2 = createCurrentThreadAsyncRunner();
        final AsyncAction thenAsyncAction = basicAsyncAction.thenOn(runner2);
        assertNotNull(thenAsyncAction);
        assertNotSame(basicAsyncAction, thenAsyncAction);
        assertEquals(1, basicAsyncAction.getPausedTaskCount());

        runner.await();
        assertEquals(1, runner2.getScheduledTaskCount());

        runner2.await();
        assertEquals(0, runner2.getScheduledTaskCount());
    }
}
