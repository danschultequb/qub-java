package qub;

public interface MutexTests
{
    static void test(TestRunner runner, Function1<Clock,Mutex> creator)
    {
        runner.testGroup(Mutex.class, () ->
        {
            runner.test("constructor()", (Test test) ->
            {
                final Mutex mutex = create(creator);
                test.assertFalse(mutex.isAcquired());
                test.assertFalse(mutex.isAcquiredByCurrentThread());
            });

            runner.testGroup("isAcquired()", () ->
            {
                runner.test("when not acquired", (Test test) ->
                {
                    final Mutex mutex = create(creator);
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("when acquired by current thread", (Test test) ->
                {
                    final Mutex mutex = create(creator);
                    mutex.acquire().await();
                    test.assertTrue(mutex.isAcquired());
                });

                runner.test("when acquired by non-current thread", (Test test) ->
                {
                    final Mutex mutex = create(creator);

                    final Gate mutexAcquired = new SpinGate(false);
                    final Gate isAcquiredChecked = new SpinGate(false);

                    final Result<Void> task = test.getParallelAsyncRunner().schedule(() ->
                    {
                        mutex.acquire().await();
                        mutexAcquired.open();
                        isAcquiredChecked.passThrough();
                    });

                    mutexAcquired.passThrough();
                    test.assertTrue(mutex.isAcquired());
                    isAcquiredChecked.open();
                    task.await();
                });
            });

            runner.testGroup("isAcquiredByCurrentThread()", () ->
            {
                runner.test("when not acquired", (Test test) ->
                {
                    final Mutex mutex = create(creator);
                    test.assertFalse(mutex.isAcquiredByCurrentThread());
                });

                runner.test("when acquired by current thread", (Test test) ->
                {
                    final Mutex mutex = create(creator);
                    mutex.acquire().await();
                    test.assertTrue(mutex.isAcquiredByCurrentThread());
                });

                runner.test("when acquired by non-current thread", (Test test) ->
                {
                    final Mutex mutex = create(creator);

                    final Gate mutexAcquired = new SpinGate(false);
                    final Gate isAcquiredChecked = new SpinGate(false);

                    final Result<Void> task = test.getParallelAsyncRunner().schedule(() ->
                    {
                        mutex.acquire().await();
                        mutexAcquired.open();
                        isAcquiredChecked.passThrough();
                    });

                    mutexAcquired.passThrough();
                    test.assertFalse(mutex.isAcquiredByCurrentThread());
                    isAcquiredChecked.open();
                    task.await();
                });
            });

            runner.testGroup("acquire()", () ->
            {
                runner.test("when not locked", (Test test) ->
                {
                    final Mutex mutex = create(creator);
                    mutex.acquire().await();
                    test.assertTrue(mutex.isAcquired());
                    test.assertTrue(mutex.isAcquiredByCurrentThread());
                });

                runner.test("when locked by this thread", (Test test) ->
                {
                    final Mutex mutex = create(creator);
                    mutex.acquire().await();
                    test.assertTrue(mutex.isAcquired());
                    test.assertTrue(mutex.isAcquiredByCurrentThread());

                    mutex.acquire().await();
                    test.assertTrue(mutex.isAcquired());
                    test.assertTrue(mutex.isAcquiredByCurrentThread());
                });

                runner.test("with multiple threads", (Test test) ->
                {
                    final Mutex mutex = create(creator);
                    test.assertFalse(mutex.isAcquired());

                    final IntegerValue value = IntegerValue.create(0);
                    final int taskCount = 100;
                    final List<Result<Void>> tasks = List.create();
                    for (int i = 0; i < taskCount; ++i)
                    {
                        tasks.add(test.getParallelAsyncRunner().schedule(() ->
                        {
                            mutex.acquire().await();
                            try
                            {
                                value.increment();
                            }
                            finally
                            {
                                mutex.release().await();
                            }
                        }));
                    }
                    Result.await(tasks);
                    test.assertEqual(taskCount, value.get());
                });
            });

            runner.testGroup("acquire(Duration)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    test.assertThrows(() -> mutex.acquire((Duration)null),
                        new PreConditionFailure("durationTimeout cannot be null."));
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("with zero", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    test.assertThrows(() -> mutex.acquire(Duration.zero),
                        new PreConditionFailure("durationTimeout (0.0 Seconds) must be greater than 0.0 Seconds."));
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("with null Clock", (Test test) ->
                {
                    final Mutex mutex = create(creator);
                    test.assertThrows(() -> mutex.acquire(Duration.minutes(5)),
                        new PreConditionFailure("clock cannot be null."));
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("with positive duration when Mutex is not acquired", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    test.assertNull(mutex.acquire(Duration.seconds(5)).await());
                    test.assertTrue(mutex.isAcquired());
                });

                runner.test("with positive duration when Mutex is already acquired by the current thread", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    test.assertNull(mutex.acquire().await());
                    test.assertNull(mutex.acquire(Duration.seconds(5)).await());
                    test.assertTrue(mutex.isAcquired());
                });

                runner.test("with positive duration when Mutex is already acquired by a different thread", (Test test) ->
                {
                    final Clock clock = test.getClock();
                    final Mutex mutex = create(creator, clock);
                    test.getParallelAsyncRunner().schedule(() -> mutex.acquire().await()).await();
                    test.assertTrue(mutex.isAcquired());
                    test.assertFalse(mutex.isAcquiredByCurrentThread());

                    final Duration timeout = Duration.seconds(1);

                    final DateTime startTime = clock.getCurrentDateTime();
                    test.assertThrows(() -> mutex.acquire(timeout).await(), new TimeoutException());
                    final DateTime endTime = clock.getCurrentDateTime();

                    test.assertTrue(mutex.isAcquired());
                    final Duration executionDuration = endTime.minus(startTime);
                    test.assertGreaterThanOrEqualTo(executionDuration, timeout);
                });
            });

            runner.testGroup("acquire(DateTime)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    test.assertThrows(() -> mutex.acquire((DateTime)null),
                        new PreConditionFailure("dateTimeTimeout cannot be null."));
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("with null Clock", (Test test) ->
                {
                    final Mutex mutex = create(creator);
                    test.assertThrows(() -> mutex.acquire(DateTime.date(2018, 1, 1)),
                        new PreConditionFailure("clock cannot be null."));
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("with timeout before current time", (Test test) ->
                {
                    final Clock clock = test.getClock();
                    final Mutex mutex = create(creator, clock);
                    final DateTime timeout = clock.getCurrentDateTime().minus(Duration.milliseconds(10));
                    test.assertThrows(() -> mutex.acquire(timeout).await(),
                        new TimeoutException());
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("with timeout at current time", (Test test) ->
                {
                    final Clock clock = test.getClock();
                    final Mutex mutex = create(creator, clock);
                    final DateTime timeout = clock.getCurrentDateTime();
                    test.assertThrows(() -> mutex.acquire(timeout).await(),
                        new TimeoutException());
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("with timeout after current time when mutex is not acquired", (Test test) ->
                {
                    final Clock clock = test.getClock();
                    final Mutex mutex = create(creator, clock);
                    final DateTime timeout = clock.getCurrentDateTime().plus(Duration.seconds(1));
                    test.assertNull(mutex.acquire(timeout).await());
                    test.assertTrue(mutex.isAcquired());
                });

                runner.test("with timeout after current time when Mutex is already acquired by a different thread", (Test test) ->
                {
                    final Clock clock = test.getClock();
                    final Mutex mutex = create(creator, test);
                    test.getParallelAsyncRunner().schedule(() -> mutex.acquire().await()).await();
                    test.assertTrue(mutex.isAcquired());
                    test.assertFalse(mutex.isAcquiredByCurrentThread());

                    final DateTime timeout = clock.getCurrentDateTime().plus(Duration.seconds(1));
                    test.assertThrows(() -> mutex.acquire(timeout).await(),
                        new TimeoutException());

                    test.assertTrue(mutex.isAcquired());
                    test.assertGreaterThanOrEqualTo(clock.getCurrentDateTime(), timeout);
                });
            });

            runner.testGroup("tryAcquire()", () ->
            {
                runner.test("when not locked", (Test test) ->
                {
                    final Mutex mutex = create(creator);
                    test.assertTrue(mutex.tryAcquire().await());
                    test.assertTrue(mutex.isAcquired());
                });

                runner.test("when locked by same thread", (Test test) ->
                {
                    final Mutex mutex = create(creator);
                    test.assertNull(mutex.acquire().await());
                    test.assertTrue(mutex.tryAcquire().await());
                    test.assertTrue(mutex.isAcquired());
                });

                runner.test("when locked by different thread", (Test test) ->
                {
                    final Mutex mutex = create(creator);
                    test.assertNull(mutex.acquire().await());

                    test.getParallelAsyncRunner().schedule(() ->
                    {
                        test.assertFalse(mutex.tryAcquire().await());
                        test.assertTrue(mutex.isAcquired());
                    }).await();

                    test.assertTrue(mutex.isAcquired());
                });
            });

            runner.testGroup("release()", () ->
            {
                runner.test("when not locked", (Test test) ->
                {
                    final Mutex mutex = create(creator);
                    mutex.release();
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("when locked", (Test test) ->
                {
                    final Mutex mutex = create(creator);
                    mutex.acquire();
                    test.assertTrue(mutex.isAcquired());
                    test.assertNull(mutex.release().await());
                    test.assertFalse(mutex.isAcquired());
                });
            });

            runner.testGroup("criticalSection(Action0)", () ->
            {
                runner.test("with null action", (Test test) ->
                {
                    final Mutex mutex = create(creator);
                    test.assertThrows(() -> mutex.criticalSection((Action0)null), new PreConditionFailure("action cannot be null."));
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("with non-null action", (Test test) ->
                {
                    final Mutex mutex = create(creator);
                    final Value<Integer> value = IntegerValue.create();
                    mutex.criticalSection(() ->
                    {
                        test.assertTrue(mutex.isAcquired());
                        value.set(20);
                    });
                    test.assertFalse(mutex.isAcquired());
                    test.assertEqual(20, value.get());
                });
            });

            runner.testGroup("criticalSection(Duration,Action0)", () ->
            {
                runner.test("with null Duration", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    final Value<Boolean> value = Value.create(false);
                    test.assertThrows(() -> mutex.criticalSection((Duration)null, () -> value.set(true)),
                        new PreConditionFailure("durationTimeout cannot be null."));
                    test.assertFalse(mutex.isAcquired());
                    test.assertFalse(value.get());
                });

                runner.test("with negative Duration", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    final Value<Boolean> value = Value.create(false);
                    test.assertThrows(() -> mutex.criticalSection(Duration.seconds(-1), () -> value.set(true)),
                        new PreConditionFailure("durationTimeout (-1.0 Seconds) must be greater than 0.0 Seconds."));
                    test.assertFalse(mutex.isAcquired());
                    test.assertFalse(value.get());
                });

                runner.test("with zero Duration", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    final Value<Boolean> value = Value.create(false);
                    test.assertThrows(() -> mutex.criticalSection(Duration.zero, () -> value.set(true)),
                        new PreConditionFailure("durationTimeout (0.0 Seconds) must be greater than 0.0 Seconds."));
                    test.assertFalse(mutex.isAcquired());
                    test.assertFalse(value.get());
                });

                runner.test("with positive duration when Mutex is not acquired", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    final Value<Boolean> value = Value.create(false);
                    test.assertNull(mutex.criticalSection(Duration.seconds(1), () -> value.set(true)).await());
                    test.assertFalse(mutex.isAcquired());
                    test.assertTrue(value.get());
                });

                runner.test("with positive duration when Mutex is already acquired by the current thread", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    test.assertNull(mutex.acquire().await());

                    final Value<Boolean> value = Value.create(false);
                    test.assertNull(mutex.criticalSection(Duration.seconds(1), () -> value.set(true)).await());
                    test.assertTrue(mutex.isAcquired());
                    test.assertTrue(value.get());
                });

                runner.test("with positive duration when Mutex is already acquired by a different thread", (Test test) ->
                {
                    final Clock clock = test.getClock();
                    final Mutex mutex = create(creator, clock);
                    test.getParallelAsyncRunner().schedule(() -> mutex.acquire().await()).await();
                    test.assertTrue(mutex.isAcquired());

                    final Duration timeout = Duration.seconds(1);

                    final Value<Boolean> value = Value.create(false);
                    final DateTime startTime = clock.getCurrentDateTime();
                    test.assertThrows(() -> mutex.criticalSection(timeout, () -> value.set(true)).await(), new TimeoutException());
                    final DateTime endTime = clock.getCurrentDateTime();

                    test.assertTrue(mutex.isAcquired());
                    test.assertGreaterThanOrEqualTo(endTime.minus(startTime), timeout);
                    test.assertFalse(value.get());
                });
            });

            runner.testGroup("criticalSection(DateTime,Action0)", () ->
            {
                runner.test("with null DateTime", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    final Value<Boolean> value = Value.create(false);
                    test.assertThrows(() -> mutex.criticalSection((DateTime)null, () -> value.set(true)),
                        new PreConditionFailure("dateTimeTimeout cannot be null."));
                    test.assertFalse(mutex.isAcquired());
                    test.assertFalse(value.get());
                });

                runner.test("with DateTime in the past", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    final Value<Boolean> value = Value.create(false);
                    final DateTime timeout = test.getClock().getCurrentDateTime().minus(Duration.seconds(1));
                    test.assertThrows(() -> mutex.criticalSection(timeout, () -> value.set(true)).await(), new TimeoutException());
                    test.assertFalse(mutex.isAcquired());
                    test.assertFalse(value.get());
                });

                runner.test("with current DateTime", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    final Value<Boolean> value = Value.create(false);
                    final DateTime timeout = test.getClock().getCurrentDateTime();
                    test.assertThrows(() -> mutex.criticalSection(timeout, () -> value.set(true)).await(),
                        new TimeoutException());
                    test.assertFalse(mutex.isAcquired());
                    test.assertFalse(value.get());
                });

                runner.test("with DateTime in the future when Mutex is not acquired", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    final Value<Boolean> value = Value.create(false);
                    final DateTime timeout = test.getClock().getCurrentDateTime().plus(Duration.seconds(1));
                    test.assertNull(mutex.criticalSection(timeout, () -> value.set(true)).await());
                    test.assertFalse(mutex.isAcquired());
                    test.assertTrue(value.get());
                });

                runner.test("with DateTime in the future when Mutex is already acquired by the current thread", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    mutex.acquire();

                    final Value<Boolean> value = Value.create(false);
                    final DateTime timeout = test.getClock().getCurrentDateTime().plus(Duration.seconds(1));
                    test.assertNull(mutex.criticalSection(timeout, () -> value.set(true)).await());
                    test.assertTrue(mutex.isAcquired());
                    test.assertTrue(value.get());
                });

                runner.test("with DateTime in the future when Mutex is already acquired by a different thread", (Test test) ->
                {
                    final Clock clock = test.getClock();
                    final Mutex mutex = create(creator, test);
                    test.getParallelAsyncRunner().schedule(() -> mutex.acquire().await()).await();
                    test.assertTrue(mutex.isAcquired());
                    test.assertFalse(mutex.isAcquiredByCurrentThread());

                    final Duration timeoutDuration = Duration.seconds(0.1);

                    final Value<Boolean> value = Value.create(false);
                    final DateTime startTime = clock.getCurrentDateTime();
                    final DateTime timeout = startTime.plus(timeoutDuration);
                    test.assertThrows(() -> mutex.criticalSection(timeout, () -> value.set(true)).await(),
                        new TimeoutException());
                    final DateTime endTime = clock.getCurrentDateTime();
                    test.assertTrue(mutex.isAcquired());
                    test.assertGreaterThanOrEqualTo(endTime, timeout);
                    test.assertFalse(value.get());
                });
            });

            runner.testGroup("criticalSection(Function0<T>)", () ->
            {
                runner.test("with null function", (Test test) ->
                {
                    final Mutex mutex = create(creator);
                    test.assertThrows(new PreConditionFailure("function cannot be null."),
                        () -> mutex.criticalSection((Function0<Integer>)null));
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("with non-null function", (Test test) ->
                {
                    final Mutex mutex = create(creator);
                    final Value<Integer> value = Value.create();
                    test.assertTrue(mutex.criticalSection(() ->
                    {
                        test.assertTrue(mutex.isAcquired());
                        value.set(20);
                        return true;
                    }).await());
                    test.assertFalse(mutex.isAcquired());
                    test.assertEqual(20, value.get());
                });
            });

            runner.testGroup("criticalSection(Duration,Function0<T>)", () ->
            {
                runner.test("with null function", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    test.assertThrows(() -> mutex.criticalSection(Duration.seconds(1), (Function0<Integer>)null), new PreConditionFailure("function cannot be null."));
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("with non-null function", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    final Value<Integer> value = Value.create();
                    test.assertTrue(mutex.criticalSection(Duration.seconds(1), () ->
                    {
                        test.assertTrue(mutex.isAcquired());
                        value.set(20);
                        return true;
                    }).await());
                    test.assertFalse(mutex.isAcquired());
                    test.assertEqual(20, value.get());
                });
            });

            runner.testGroup("criticalSection(DateTime,Function0<T>)", () ->
            {
                runner.test("with null function", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    test.assertThrows(() -> mutex.criticalSection(test.getClock().getCurrentDateTime().plus(Duration.seconds(1)), (Function0<Integer>)null), new PreConditionFailure("function cannot be null."));
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("with non-null function", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    final Value<Integer> value = Value.create();
                    test.assertTrue(mutex.criticalSection(test.getClock().getCurrentDateTime().plus(Duration.seconds(1)), () ->
                    {
                        test.assertTrue(mutex.isAcquired());
                        value.set(20);
                        return true;
                    }).await());
                    test.assertFalse(mutex.isAcquired());
                    test.assertEqual(20, value.get());
                });
            });

            runner.testGroup("criticalSectionResult(Duration,Function0<T>)", () ->
            {
                runner.test("with null function", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    test.assertThrows(() -> mutex.criticalSectionResult(Duration.seconds(1), (Function0<Result<Integer>>)null), new PreConditionFailure("function cannot be null."));
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("with non-null function", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    final Value<Integer> value = Value.create();
                    test.assertTrue(mutex.criticalSectionResult(Duration.seconds(1), () ->
                    {
                        test.assertTrue(mutex.isAcquired());
                        value.set(20);
                        return Result.successTrue();
                    }).await());
                    test.assertFalse(mutex.isAcquired());
                    test.assertEqual(20, value.get());
                });
            });

            runner.testGroup("criticalSectionResult(DateTime,Function0<Result<T>>)", () ->
            {
                runner.test("with null function", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    test.assertThrows(() -> mutex.criticalSectionResult(test.getClock().getCurrentDateTime().plus(Duration.seconds(1)), (Function0<Result<Integer>>)null), new PreConditionFailure("function cannot be null."));
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("with non-null function", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    final Value<Integer> value = Value.create();
                    test.assertTrue(mutex.criticalSectionResult(test.getClock().getCurrentDateTime().plus(Duration.seconds(1)), () ->
                    {
                        test.assertTrue(mutex.isAcquired());
                        value.set(20);
                        return Result.successTrue();
                    }).await());
                    test.assertFalse(mutex.isAcquired());
                    test.assertEqual(20, value.get());
                });

                runner.test("with null DateTime", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    final Value<Boolean> value = Value.create(false);
                    test.assertThrows(() -> mutex.criticalSectionResult((DateTime)null, () ->
                    {
                        value.set(true);
                        return Result.successTrue();
                    }), new PreConditionFailure("dateTimeTimeout cannot be null."));
                    test.assertFalse(mutex.isAcquired());
                    test.assertFalse(value.get());
                });

                runner.test("with DateTime in the past", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    final Value<Boolean> value = Value.create(false);
                    final DateTime timeout = test.getClock().getCurrentDateTime().minus(Duration.seconds(1));
                    test.assertThrows(() -> mutex.criticalSectionResult(timeout, () ->
                        {
                            value.set(true);
                            return Result.successTrue();
                        }).await(),
                        new TimeoutException());
                    test.assertFalse(mutex.isAcquired());
                    test.assertFalse(value.get());
                });

                runner.test("with current DateTime", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    final Value<Boolean> value = Value.create(false);
                    final DateTime timeout = test.getClock().getCurrentDateTime();
                    test.assertThrows(() -> mutex.criticalSectionResult(timeout, () ->
                        {
                            value.set(true);
                            return Result.successTrue();
                        }).await(),
                        new TimeoutException());
                    test.assertFalse(mutex.isAcquired());
                    test.assertFalse(value.get());
                });

                runner.test("with DateTime in the future when Mutex is not acquired", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    final Value<Boolean> value = Value.create(false);
                    test.assertTrue(mutex.criticalSectionResult(test.getClock().getCurrentDateTime().plus(Duration.seconds(1)), () ->
                    {
                        value.set(true);
                        return Result.successTrue();
                    }).await());
                    test.assertFalse(mutex.isAcquired());
                    test.assertTrue(value.get());
                });

                runner.test("with DateTime in the future when Mutex is not acquired and function throws an exception", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    test.assertThrows(() -> mutex.criticalSectionResult(test.getClock().getCurrentDateTime().plus(Duration.seconds(1)), () ->
                        {
                            throw new RuntimeException("Oops!");
                        }).await(),
                        new RuntimeException("Oops!"));
                    test.assertFalse(mutex.isAcquired());
                });

                runner.test("with DateTime in the future when Mutex is already acquired by the current thread", (Test test) ->
                {
                    final Mutex mutex = create(creator, test);
                    mutex.acquire();

                    final Value<Boolean> value = Value.create(false);
                    test.assertTrue(mutex.criticalSectionResult(test.getClock().getCurrentDateTime().plus(Duration.seconds(1)), () ->
                    {
                        value.set(true);
                        return Result.successTrue();
                    }).await());
                    test.assertTrue(mutex.isAcquired());
                    test.assertTrue(value.get());
                });

                runner.test("with DateTime in the future when Mutex is already acquired by a different thread", (Test test) ->
                {
                    final Clock clock = test.getClock();
                    final Mutex mutex = create(creator, clock);
                    test.getParallelAsyncRunner().schedule(() -> mutex.acquire().await()).await();
                    test.assertTrue(mutex.isAcquired());
                    test.assertFalse(mutex.isAcquiredByCurrentThread());

                    final Duration timeoutDuration = Duration.seconds(0.1);

                    final Value<Boolean> value = Value.create(false);
                    final DateTime startTime = clock.getCurrentDateTime();
                    final DateTime timeout = startTime.plus(timeoutDuration);
                    test.assertThrows(() -> mutex.criticalSectionResult(timeout, () ->
                        {
                            value.set(true);
                            return Result.successTrue();
                        }).await(),
                        new TimeoutException());
                    final DateTime endTime = clock.getCurrentDateTime();
                    test.assertTrue(mutex.isAcquired());
                    test.assertGreaterThanOrEqualTo(endTime, timeout);
                    test.assertFalse(value.get());
                });
            });

            runner.testGroup(MutexCondition.class, () ->
            {
                runner.testGroup("await()", () ->
                {
                    runner.test("when Mutex is not acquired", (Test test) ->
                    {
                        final Mutex mutex = create(creator, test);
                        final MutexCondition condition = mutex.createCondition();

                        test.assertThrows(new PreConditionFailure("mutex.isAcquiredByCurrentThread() cannot be false."),
                            () -> condition.await());
                    });

                    runner.test("when Mutex is not acquired by current thread", (Test test) ->
                    {
                        final Mutex mutex = create(creator, test);
                        final MutexCondition condition = mutex.createCondition();

                        test.getParallelAsyncRunner().schedule(() -> mutex.acquire().await()).await();
                        test.assertTrue(mutex.isAcquired());
                        test.assertFalse(mutex.isAcquiredByCurrentThread());

                        test.assertThrows(new PreConditionFailure("mutex.isAcquiredByCurrentThread() cannot be false."),
                            () -> condition.await());
                    });

                    runner.test("with one producer and one consumer", (Test test) ->
                    {
                        final Mutex mutex = create(creator);
                        final MutexCondition listHasValues = mutex.createCondition();
                        final List<Integer> values = List.create();
                        final IntegerValue sum = IntegerValue.create(0);
                        final int valueCount = 1000;

                        final List<AsyncTask<Void>> tasks = List.create();
                        tasks.add(test.getParallelAsyncRunner().schedule(() ->
                        {
                            for (int i = 1; i <= valueCount; ++i)
                            {
                                mutex.acquire().await();
                                try
                                {
                                    values.add(i);
                                    listHasValues.signalAll();
                                }
                                finally
                                {
                                    mutex.release().await();
                                }
                            }
                        }));

                        tasks.add(test.getParallelAsyncRunner().schedule(() ->
                        {
                            for (int i = 1; i <= valueCount; ++i)
                            {
                                mutex.acquire().await();
                                try
                                {
                                    while (!values.any())
                                    {
                                        listHasValues.await().await();
                                    }
                                    sum.plusAssign(values.removeFirst());
                                }
                                finally
                                {
                                    mutex.release().await();
                                }
                            }
                        }));

                        Result.await(tasks);
                        test.assertEqual(Math.summation(valueCount), sum.get());
                    });
                });

                runner.testGroup("await(Duration)", () ->
                {
                    runner.test("with null", (Test test) ->
                    {
                        final Mutex mutex = create(creator, test);
                        final MutexCondition condition = mutex.createCondition();

                        test.assertThrows(new PreConditionFailure("timeout cannot be null."),
                            () -> condition.await((Duration)null));
                    });

                    runner.test("with zero", (Test test) ->
                    {
                        final Mutex mutex = create(creator, test);
                        final MutexCondition condition = mutex.createCondition();

                        test.assertThrows(new PreConditionFailure("timeout (0.0 Seconds) must be greater than 0.0 Seconds."),
                            () -> condition.await(Duration.zero));
                    });

                    runner.test("when Mutex is not acquired", (Test test) ->
                    {
                        final Mutex mutex = create(creator, test);
                        final MutexCondition condition = mutex.createCondition();

                        test.assertThrows(new PreConditionFailure("mutex.isAcquiredByCurrentThread() cannot be false."),
                            () -> condition.await(Duration.seconds(1)));
                    });

                    runner.test("when Mutex is not acquired by current thread", (Test test) ->
                    {
                        final Mutex mutex = create(creator, test);
                        final MutexCondition condition = mutex.createCondition();

                        test.getParallelAsyncRunner().schedule(() -> mutex.acquire().await()).await();
                        test.assertTrue(mutex.isAcquired());
                        test.assertFalse(mutex.isAcquiredByCurrentThread());

                        test.assertThrows(new PreConditionFailure("mutex.isAcquiredByCurrentThread() cannot be false."),
                            () -> condition.await(Duration.seconds(1)));
                    });

                    runner.test("when Mutex doesn't have a Clock", (Test test) ->
                    {
                        final Mutex mutex = create(creator);
                        final MutexCondition condition = mutex.createCondition();

                        mutex.acquire().await();
                        test.assertThrows(new PreConditionFailure("clock cannot be null."),
                            () -> condition.await(Duration.seconds(1)));
                    });

                    runner.test("when timeout expires", (Test test) ->
                    {
                        final Mutex mutex = create(creator, test.getClock());
                        final MutexCondition condition = mutex.createCondition();

                        mutex.acquire().await();
                        test.assertThrows(new TimeoutException(),
                            () -> condition.await(Duration.seconds(0.01)).await());
                    });
                });

                runner.testGroup("await(DateTime)", () ->
                {
                    runner.test("with null", (Test test) ->
                    {
                        final Mutex mutex = create(creator, test);
                        final MutexCondition condition = mutex.createCondition();

                        test.assertThrows(new PreConditionFailure("timeout cannot be null."),
                            () -> condition.await((DateTime)null));
                    });

                    runner.test("when Mutex is not acquired", (Test test) ->
                    {
                        final Clock clock = test.getClock();
                        final Mutex mutex = create(creator, clock);
                        final MutexCondition condition = mutex.createCondition();

                        test.assertThrows(new PreConditionFailure("mutex.isAcquiredByCurrentThread() cannot be false."),
                            () -> condition.await(clock.getCurrentDateTime()));
                    });

                    runner.test("when Mutex is not acquired by current thread", (Test test) ->
                    {
                        final Clock clock = test.getClock();
                        final Mutex mutex = create(creator, clock);
                        final MutexCondition condition = mutex.createCondition();

                        test.getParallelAsyncRunner().schedule(() -> mutex.acquire().await()).await();
                        test.assertTrue(mutex.isAcquired());
                        test.assertFalse(mutex.isAcquiredByCurrentThread());

                        test.assertThrows(new PreConditionFailure("mutex.isAcquiredByCurrentThread() cannot be false."),
                            () -> condition.await(clock.getCurrentDateTime()));
                    });

                    runner.test("when Mutex doesn't have a Clock", (Test test) ->
                    {
                        final Clock clock = test.getClock();
                        final Mutex mutex = create(creator);
                        final MutexCondition condition = mutex.createCondition();

                        mutex.acquire().await();
                        test.assertThrows(new PreConditionFailure("clock cannot be null."),
                            () -> condition.await(clock.getCurrentDateTime()));
                    });
                });

                runner.testGroup("signalAll()", () ->
                {
                    runner.test("when Mutex is not acquired", (Test test) ->
                    {
                        final Clock clock = test.getClock();
                        final Mutex mutex = create(creator, clock);
                        final MutexCondition condition = mutex.createCondition();

                        test.assertThrows(new PreConditionFailure("mutex.isAcquiredByCurrentThread() cannot be false."),
                            () -> condition.signalAll());
                    });

                    runner.test("when Mutex is not acquired by current thread", (Test test) ->
                    {
                        final Clock clock = test.getClock();
                        final Mutex mutex = create(creator, clock);
                        final MutexCondition condition = mutex.createCondition();

                        test.getParallelAsyncRunner().schedule(() -> mutex.acquire().await()).await();
                        test.assertTrue(mutex.isAcquired());
                        test.assertFalse(mutex.isAcquiredByCurrentThread());

                        test.assertThrows(new PreConditionFailure("mutex.isAcquiredByCurrentThread() cannot be false."),
                            () -> condition.signalAll());
                    });
                });
            });
        });
    }

    static Mutex create(Function1<Clock,Mutex> creator)
    {
        PreCondition.assertNotNull(creator, "creator");

        return create(creator, (Clock)null);
    }

    static Mutex create(Function1<Clock,Mutex> creator, Test test)
    {
        PreCondition.assertNotNull(creator, "creator");
        PreCondition.assertNotNull(test, "test");

        return create(creator, test.getClock());
    }

    static Mutex create(Function1<Clock,Mutex> creator, Clock clock)
    {
        PreCondition.assertNotNull(creator, "creator");

        return creator.run(clock);
    }
}
