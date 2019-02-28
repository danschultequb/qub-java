package qub;

public class BasicAsyncAction extends BasicAsyncTask implements AsyncAction
{
    private final Action0 action;

    BasicAsyncAction(AsyncRunner runner)
    {
        this(Value.create(runner), Action0.empty);
    }

    BasicAsyncAction(AsyncRunner runner, String label)
    {
        this(Value.create(runner), label, Action0.empty);
    }

    BasicAsyncAction(Getable<AsyncRunner> runner, Action0 action)
    {
        this(runner, null, action);
    }

    BasicAsyncAction(Getable<AsyncRunner> runner, String label, Action0 action)
    {
        super(runner, label);

        this.action = action;
    }

    @Override
    public AsyncAction catchError(final Action1<Throwable> action)
    {
        return action == null ? null : catchErrorOnInner(getAsyncRunnerGetable(), action);
    }

    @Override
    public AsyncAction catchErrorOn(AsyncRunner asyncRunner, Action1<Throwable> action)
    {
        return asyncRunner == null || action == null ? null : catchErrorOnInner(Value.create(asyncRunner), action);
    }

    private AsyncAction catchErrorOnInner(Getable<AsyncRunner> asyncRunner, Action1<Throwable> action)
    {
        final BasicAsyncActionErrorHandler asyncAction = new BasicAsyncActionErrorHandler(asyncRunner, action);
        asyncAction.addParentTask(this);
        return scheduleOrEnqueue(asyncAction);
    }

    @Override
    public AsyncAction catchErrorAsyncAction(Function1<Throwable, AsyncAction> function)
    {
        return function == null ? null : catchErrorAsyncActionOnInner(getAsyncRunnerGetable(), function);
    }

    @Override
    public AsyncAction catchErrorAsyncActionOn(AsyncRunner asyncRunner, Function1<Throwable, AsyncAction> function)
    {
        return asyncRunner == null || function == null ? null : catchErrorAsyncActionOnInner(Value.create(asyncRunner), function);
    }

    @Override
    protected void runTask()
    {
        final Throwable error = getIncomingError();
        if (error != null)
        {
            setOutgoingError(error);
        }
        else
        {
            if (action != null)
            {
                action.run();
            }
        }
    }

    @Override
    public AsyncAction thenOn(AsyncRunner runner)
    {
        return runner == getAsyncRunner() ? this : thenOn(runner, Action0.empty);
    }
}
