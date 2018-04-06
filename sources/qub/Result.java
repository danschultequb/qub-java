package qub;

final public class Result<T>
{
    private final T value;
    private final Throwable error;

    private Result(T value, Throwable error)
    {
        this.value = value;
        this.error = error;
    }

    final public T getValue()
    {
        return value;
    }

    final public boolean hasError()
    {
        return error != null;
    }

    final public Throwable getError()
    {
        return error;
    }

    final public Class<? extends Throwable> getErrorType()
    {
        return error == null ? null : error.getClass();
    }

    final public String getErrorMessage()
    {
        return error == null ? null : error.getMessage();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object rhs)
    {
        return rhs instanceof Result && equals((Result<T>)rhs);
    }

    public boolean equals(Result<T> rhs)
    {
        return rhs != null && Comparer.equal(value, rhs.value) && Comparer.equal(error, rhs.error);
    }

    public static <T> Result<T> success(T value)
    {
        return Result.done(value, null);
    }

    public static <T> Result<T> error(Throwable error)
    {
        return Result.done(null, error);
    }

    public static <T> Result<T> done(T value, Throwable error)
    {
        return new Result<T>(value, error);
    }
}
