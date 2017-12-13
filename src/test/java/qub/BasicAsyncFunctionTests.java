package qub;

import org.junit.Test;

import static org.junit.Assert.*;

public class BasicAsyncFunctionTests extends BasicAsyncTaskTests
{
    @Override
    protected BasicAsyncFunction<Integer> create(AsyncRunner runner)
    {
        return new BasicAsyncFunction<>(runner, new Synchronization(), TestUtils.emptyFunction0);
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

    private BasicAsyncFunction<Integer> create()
    {
        final CurrentThreadAsyncRunner runner = createCurrentThreadAsyncRunner();
        return create(runner);
    }

    private BasicAsyncFunction<Integer> createScheduled(AsyncRunner runner)
    {
        final BasicAsyncFunction<Integer> basicAsyncFunction = create(runner);
        basicAsyncFunction.schedule();
        return basicAsyncFunction;
    }

    @Test
    public void constructor()
    {
        final CurrentThreadAsyncRunner runner = createCurrentThreadAsyncRunner();
        final BasicAsyncFunction<Integer> basicAsyncFunction = new BasicAsyncFunction<>(runner, new Synchronization(), TestUtils.emptyFunction0);
        assertEquals(0, runner.getScheduledTaskCount());
        assertEquals(0, basicAsyncFunction.getPausedTaskCount());
        assertFalse(basicAsyncFunction.isCompleted());
    }

    @Test
    public void thenAction1WithNull()
    {
        final BasicAsyncFunction<Integer> basicAsyncFunction = create();
        final AsyncAction thenAsyncAction = basicAsyncFunction.then(TestUtils.nullAction1);
        assertNull(thenAsyncAction);
        assertEquals(0, basicAsyncFunction.getPausedTaskCount());
    }

    @Test
    public void thenAction1WithNonNull()
    {
        final BasicAsyncFunction<Integer> basicAsyncFunction = create();
        final AsyncAction thenAsyncAction = basicAsyncFunction.then(TestUtils.emptyAction1);
        assertNotNull(thenAsyncAction);
        assertEquals(1, basicAsyncFunction.getPausedTaskCount());
    }

    @Test
    public void thenAction1WithNonNullWhenCompleted()
    {
        final CurrentThreadAsyncRunner runner = createCurrentThreadAsyncRunner();
        final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(runner);
        runner.await();

        final AsyncAction thenAsyncAction = basicAsyncFunction.then(TestUtils.emptyAction1);
        assertNotNull(thenAsyncAction);
        assertEquals(0, basicAsyncFunction.getPausedTaskCount());
        assertEquals(1, runner.getScheduledTaskCount());
    }

    @Test
    public void thenFunction1WithNull()
    {
        final BasicAsyncFunction<Integer> basicAsyncFunction = create();
        final AsyncFunction<Integer> thenAsyncFunction = basicAsyncFunction.then(TestUtils.nullFunction1);
        assertNull(thenAsyncFunction);
        assertEquals(0, basicAsyncFunction.getPausedTaskCount());
    }

    @Test
    public void thenFunction1WithNonNull()
    {
        final BasicAsyncFunction<Integer> basicAsyncFunction = create();
        final AsyncFunction<Integer> thenAsyncFunction = basicAsyncFunction.then(TestUtils.emptyFunction1);
        assertNotNull(thenAsyncFunction);
        assertEquals(1, basicAsyncFunction.getPausedTaskCount());
    }

    @Test
    public void thenFunction1WithNonNullWhenCompleted()
    {
        final CurrentThreadAsyncRunner runner = createCurrentThreadAsyncRunner();
        final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(runner);
        runner.await();

        final AsyncFunction<Integer> thenAsyncFunction = basicAsyncFunction.then(TestUtils.emptyFunction1);
        assertNotNull(thenAsyncFunction);
        assertEquals(0, basicAsyncFunction.getPausedTaskCount());
        assertEquals(1, runner.getScheduledTaskCount());
    }

    @Test
    public void thenOnAction1()
    {
        final CurrentThreadAsyncRunner runner1 = createCurrentThreadAsyncRunner();
        final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(runner1);

        final CurrentThreadAsyncRunner runner2 = createCurrentThreadAsyncRunner();
        final AsyncAction thenOnAsyncAction = basicAsyncFunction.thenOn(runner2, TestUtils.emptyAction1);
        assertNotNull(thenOnAsyncAction);
        assertEquals(1, basicAsyncFunction.getPausedTaskCount());
        assertEquals(0, runner2.getScheduledTaskCount());

        runner1.await();
        assertEquals(1, runner2.getScheduledTaskCount());

        runner2.await();
        assertEquals(0, runner2.getScheduledTaskCount());
    }

    @Test
    public void thenOnAction1WhenCompleted()
    {
        final CurrentThreadAsyncRunner runner1 = createCurrentThreadAsyncRunner();
        final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(runner1);
        runner1.await();
        assertTrue(basicAsyncFunction.isCompleted());

        final CurrentThreadAsyncRunner runner2 = createCurrentThreadAsyncRunner();
        final AsyncAction thenOnAsyncAction = basicAsyncFunction.thenOn(runner2, TestUtils.emptyAction1);
        assertNotNull(thenOnAsyncAction);
        assertEquals(0, basicAsyncFunction.getPausedTaskCount());
        assertEquals(1, runner2.getScheduledTaskCount());

        runner2.await();
        assertEquals(0, runner2.getScheduledTaskCount());
    }

    @Test
    public void thenOnFunction1()
    {
        final CurrentThreadAsyncRunner runner1 = createCurrentThreadAsyncRunner();
        final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(runner1);

        final CurrentThreadAsyncRunner runner2 = createCurrentThreadAsyncRunner();
        final AsyncFunction<Integer> thenOnAsyncFunction = basicAsyncFunction.thenOn(runner2, TestUtils.emptyFunction1);
        assertNotNull(thenOnAsyncFunction);
        assertEquals(1, basicAsyncFunction.getPausedTaskCount());
        assertEquals(0, runner2.getScheduledTaskCount());

        runner1.await();
        assertEquals(1, runner2.getScheduledTaskCount());

        runner2.await();
        assertEquals(0, runner2.getScheduledTaskCount());
    }

    @Test
    public void thenOnFunction1WhenCompleted()
    {
        final CurrentThreadAsyncRunner runner1 = createCurrentThreadAsyncRunner();
        final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(runner1);
        runner1.await();

        final CurrentThreadAsyncRunner runner2 = createCurrentThreadAsyncRunner();
        final AsyncFunction<Integer> thenOnAsyncFunction = basicAsyncFunction.thenOn(runner2, TestUtils.emptyFunction1);
        assertNotNull(thenOnAsyncFunction);
        assertEquals(0, basicAsyncFunction.getPausedTaskCount());
        assertEquals(1, runner2.getScheduledTaskCount());

        runner2.await();
        assertEquals(0, runner2.getScheduledTaskCount());
    }

    @Test
    public void thenOnAsyncRunnerWithNullRunner()
    {
        final CurrentThreadAsyncRunner runner = createCurrentThreadAsyncRunner();
        final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(runner);
        assertNull(basicAsyncFunction.thenOn(null));
    }

    @Test
    public void thenOnAsyncRunnerWithSameRunner()
    {
        final CurrentThreadAsyncRunner runner = createCurrentThreadAsyncRunner();
        final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(runner);
        assertSame(basicAsyncFunction, basicAsyncFunction.thenOn(runner));
        assertEquals(0, basicAsyncFunction.getPausedTaskCount());
        assertEquals(1, runner.getScheduledTaskCount());
    }

    @Test
    public void thenOnAsyncRunnerWithDifferentRunner()
    {
        final CurrentThreadAsyncRunner runner = createCurrentThreadAsyncRunner();
        final BasicAsyncFunction<Integer> basicAsyncFunction = createScheduled(runner);

        final CurrentThreadAsyncRunner runner2 = createCurrentThreadAsyncRunner();
        final AsyncFunction<Integer> thenAsyncAction = basicAsyncFunction.thenOn(runner2);
        assertNotNull(thenAsyncAction);
        assertNotSame(basicAsyncFunction, thenAsyncAction);
        assertEquals(1, basicAsyncFunction.getPausedTaskCount());

        runner.await();
        assertEquals(1, runner2.getScheduledTaskCount());

        runner2.await();
        assertEquals(0, runner2.getScheduledTaskCount());
    }

    @Test
    public void awaitReturn()
    {
        final ParallelAsyncRunner runner = new ParallelAsyncRunner(new Synchronization());
        final AsyncFunction<Integer> asyncFunction = runner.schedule(new Function0<Integer>()
        {
            @Override
            public Integer run()
            {
                return 20;
            }
        });
        assertEquals(20, asyncFunction.awaitReturn().intValue());
        assertTrue(asyncFunction.isCompleted());

        assertEquals(20, asyncFunction.awaitReturn().intValue());
    }
}
