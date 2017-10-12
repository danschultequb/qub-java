package qub;

public class MainAsyncFunction<T> extends MainAsyncTask implements AsyncFunction<T>
{
    private final Function0<T> function;
    private final Value<T> functionResult;

    public MainAsyncFunction(MainAsyncRunner runner, Function0<T> function)
    {
        super(runner);

        this.function = function;
        this.functionResult = new Value<>();
    }

    @Override
    public AsyncAction then(final Action1<T> action)
    {
        return action == null ? null : then(new Action0()
        {
            @Override
            public void run()
            {
                action.run(functionResult.get());
            }
        });
    }

    @Override
    public <U> AsyncFunction<U> then(final Function1<T, U> function)
    {
        return function == null ? null : then(new Function0<U>()
        {
            @Override
            public U run()
            {
                return function.run(functionResult.get());
            }
        });
    }

    @Override
    protected void runTask()
    {
        functionResult.set(function.run());
    }
}
