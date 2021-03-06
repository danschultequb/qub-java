package qub;

/**
 * An interface for a function that takes three arguments and doesn't return anything.
 * @param <T1> The first type of argument that this function takes.
 * @param <T2> The second type of argument that this function takes.
 * @param <T3> The third type of argument that this function takes.
 * @param <T4> The fourth type of argument that this function takes.
 * @param <T5> The fifth type of argument that this function takes.
 * @param <T6> The sixth type of argument that this function takes.
 * @param <T7> The seventh type of argument that this function takes.
 */
public interface Action7<T1,T2,T3,T4,T5,T6,T7>
{
    /**
     * The function to run.
     * @param arg1 The first argument for the function.
     * @param arg2 The second argument for the function.
     * @param arg3 The third argument for the function.
     * @param arg4 The fourth argument for the function.
     * @param arg5 The fifth argument for the function.
     * @param arg6 The sixth argument for the function.
     * @param arg7 The seventh argument for the function.
     */
    void run(T1 arg1, T2 arg2, T3 arg3, T4 arg4, T5 arg5, T6 arg6, T7 arg7);
}
