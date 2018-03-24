package qub;

public class BasicAsyncTaskTests
{
    public static void test(TestRunner runner, Function1<AsyncRunner,BasicAsyncTask> creator)
    {
        runner.testGroup(BasicAsyncTask.class, () ->
        {
            runner.testGroup("then(Action0)", () ->
            {
                runner.test("with null action", (Test test) ->
                {
                    final BasicAsyncTask basicAsyncTask = create(creator);
                    final AsyncAction thenAsyncAction = basicAsyncTask.then((Action0)null);
                    test.assertNull(thenAsyncAction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final BasicAsyncTask basicAsyncTask = create(creator);
                    final AsyncAction thenAsyncAction = basicAsyncTask.then(() -> {});
                    test.assertNotNull(thenAsyncAction);
                    test.assertEqual(1, basicAsyncTask.getPausedTaskCount());
                });

                runner.test("with non-null when completed", (Test test) ->
                {
                    final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = createScheduled(creator, asyncRunner);
                    asyncRunner.await();
                    test.assertTrue(basicAsyncTask.isCompleted());

                    final AsyncAction thenAsyncAction = basicAsyncTask.then(() -> {});

                    test.assertNotNull(thenAsyncAction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(1, asyncRunner.getScheduledTaskCount());

                    asyncRunner.await();
                    test.assertTrue(thenAsyncAction.isCompleted());
                });

                runner.test("with exception throwing action", (Test test) ->
                {
                    final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = createScheduled(creator, asyncRunner);
                    final AsyncAction thenAsyncAction = basicAsyncTask.then((Action0)() -> { throw new RuntimeException("This exception should be swallowed by the AsyncRunner."); });
                    test.assertNotNull(thenAsyncAction);
                    test.assertEqual(1, basicAsyncTask.getPausedTaskCount());

                    asyncRunner.await();

                    test.assertTrue(basicAsyncTask.isCompleted());
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertNull(basicAsyncTask.getIncomingError());
                    test.assertNull(basicAsyncTask.getOutgoingError());
                    test.assertTrue(thenAsyncAction.isCompleted());
                    test.assertNull(thenAsyncAction.getIncomingError());
                    test.assertNotNull(thenAsyncAction.getOutgoingError());
                    test.assertEqual(RuntimeException.class, thenAsyncAction.getOutgoingError().getClass());
                    test.assertEqual("This exception should be swallowed by the AsyncRunner.", thenAsyncAction.getOutgoingError().getMessage());
                });
            });

            runner.testGroup("then(Function0)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final BasicAsyncTask basicAsyncTask = create(creator);
                    final AsyncFunction<Integer> thenAsyncFunction = basicAsyncTask.then((Function0<Integer>)null);
                    test.assertNull(thenAsyncFunction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final BasicAsyncTask basicAsyncTask = create(creator);
                    final AsyncFunction<Integer> thenAsyncFunction = basicAsyncTask.then(() -> 0);
                    test.assertNotNull(thenAsyncFunction);
                    test.assertEqual(1, basicAsyncTask.getPausedTaskCount());
                });

                runner.test("with non-null when completed", (Test test) ->
                {
                    final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = createScheduled(creator, asyncRunner);
                    asyncRunner.await();
                    test.assertTrue(basicAsyncTask.isCompleted());

                    final AsyncFunction<Integer> thenAsyncFunction = basicAsyncTask.then(() -> 0);

                    test.assertNotNull(thenAsyncFunction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(1, asyncRunner.getScheduledTaskCount());

                    asyncRunner.await();

                    test.assertTrue(thenAsyncFunction.isCompleted());
                });
                
                runner.test("with exception throwing function", (Test test) ->
                {
                    final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = createScheduled(creator, asyncRunner);
                    final AsyncFunction<Integer> thenAsyncFunction = basicAsyncTask.then(() ->
                        {
                            if (1 + 1 == 2)
                            {
                                throw new RuntimeException("This exception should be swallowed by the AsyncRunner.");
                            }
                            return 0;
                        });
                    test.assertNotNull(thenAsyncFunction);
                    test.assertEqual(1, basicAsyncTask.getPausedTaskCount());

                    asyncRunner.await();

                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertNull(basicAsyncTask.getIncomingError());
                    test.assertNull(basicAsyncTask.getOutgoingError());
                    test.assertTrue(thenAsyncFunction.isCompleted());
                    test.assertNull(thenAsyncFunction.getIncomingError());
                    test.assertNotNull(thenAsyncFunction.getOutgoingError());
                    test.assertEqual(RuntimeException.class, thenAsyncFunction.getOutgoingError().getClass());
                    test.assertEqual("This exception should be swallowed by the AsyncRunner.", thenAsyncFunction.getOutgoingError().getMessage());
                    test.assertNull(thenAsyncFunction.awaitReturn());
                });
            });

            runner.testGroup("thenOn(AsyncRunner,Action0)", () ->
            {
                runner.test("with null AsyncRunner", (Test test) ->
                {
                    final AsyncRunner runner1 = createCurrentThreadAsyncRunner();
                    final AsyncRunner runner2 = null;
                    final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner1);

                    final AsyncAction thenOnAsyncAction = basicAsyncTask.thenOn(runner2, () -> {});
                    test.assertNull(thenOnAsyncAction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());

                    runner1.await();
                });

                runner.test("with null Action0", (Test test) ->
                {
                    final AsyncRunner runner1 = createCurrentThreadAsyncRunner();
                    final AsyncRunner runner2 = createCurrentThreadAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner1);

                    final AsyncAction thenOnAsyncAction = basicAsyncTask.thenOn(runner2, (Action0)null);
                    test.assertNull(thenOnAsyncAction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());

                    runner1.await();
                });

                runner.test("with non-null AsyncRunner and Action0", (Test test) ->
                {
                    final AsyncRunner runner1 = test.getMainAsyncRunner();
                    final AsyncRunner runner2 = createCurrentThreadAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner1);

                    final AsyncAction thenOnAsyncAction = basicAsyncTask.thenOn(runner2, () -> {});
                    test.assertNotNull(thenOnAsyncAction);
                    test.assertEqual(1, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(0, runner2.getScheduledTaskCount());

                    runner1.await();
                    test.assertEqual(1, runner2.getScheduledTaskCount());
                    test.assertTrue(basicAsyncTask.isCompleted());

                    runner2.await();
                    test.assertEqual(0, runner1.getScheduledTaskCount());
                    test.assertEqual(0, runner2.getScheduledTaskCount());
                    test.assertTrue(thenOnAsyncAction.isCompleted());
                });

                runner.test("with non-null AsyncRunner and Action0 when completed", (Test test) ->
                {
                    final AsyncRunner runner1 = createCurrentThreadAsyncRunner();
                    final AsyncRunner runner2 = createCurrentThreadAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner1);
                    runner1.await();

                    final AsyncAction thenOnAsyncAction = basicAsyncTask.thenOn(runner2, () -> {});
                    test.assertNotNull(thenOnAsyncAction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(1, runner2.getScheduledTaskCount());

                    runner2.await();
                    test.assertEqual(0, runner1.getScheduledTaskCount());
                    test.assertEqual(0, runner2.getScheduledTaskCount());
                    test.assertTrue(thenOnAsyncAction.isCompleted());
                });

                runner.test("with exception throwing action", (Test test) ->
                {
                    final AsyncRunner asyncRunner1 = test.getMainAsyncRunner();
                    final AsyncRunner asyncRunner2 = createCurrentThreadAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = createScheduled(creator, asyncRunner1);
                    final AsyncAction thenOnAsyncAction = basicAsyncTask.thenOn(asyncRunner2, (Action0)() -> { throw new RuntimeException("This exception should be swallowed by the AsyncRunner."); });
                    test.assertNotNull(thenOnAsyncAction);
                    test.assertEqual(1, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(1, asyncRunner1.getScheduledTaskCount());
                    test.assertEqual(0, asyncRunner2.getScheduledTaskCount());

                    asyncRunner1.await();

                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertNull(basicAsyncTask.getIncomingError());
                    test.assertNull(basicAsyncTask.getOutgoingError());
                    test.assertEqual(0, asyncRunner1.getScheduledTaskCount());
                    test.assertEqual(1, asyncRunner2.getScheduledTaskCount());

                    asyncRunner2.await();

                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(0, asyncRunner1.getScheduledTaskCount());
                    test.assertEqual(0, asyncRunner2.getScheduledTaskCount());
                    test.assertTrue(thenOnAsyncAction.isCompleted());
                    test.assertNull(thenOnAsyncAction.getIncomingError());
                    test.assertNotNull(thenOnAsyncAction.getOutgoingError());
                    test.assertEqual(RuntimeException.class, thenOnAsyncAction.getOutgoingError().getClass());
                    test.assertEqual("This exception should be swallowed by the AsyncRunner.", thenOnAsyncAction.getOutgoingError().getMessage());
                });
            });

            runner.testGroup("thenOn(AsyncRunner,Function0)", () ->
            {
                runner.test("with null AsyncRunner", (Test test) ->
                {
                    final AsyncRunner runner1 = createCurrentThreadAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner1);

                    final AsyncFunction<Integer> thenOnAsyncFunction = basicAsyncTask.thenOn(null, () -> 0);
                    test.assertNull(thenOnAsyncFunction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());

                    runner1.await();
                });

                runner.test("with null Function0", (Test test) ->
                {
                    final AsyncRunner runner1 = createCurrentThreadAsyncRunner();
                    final AsyncRunner runner2 = createCurrentThreadAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner1);

                    final AsyncFunction<Integer> thenOnAsyncFunction = basicAsyncTask.thenOn(runner2, (Function0<Integer>)null);
                    test.assertNull(thenOnAsyncFunction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(0, runner2.getScheduledTaskCount());

                    runner1.await();
                    test.assertEqual(0, runner2.getScheduledTaskCount());
                });

                runner.test("with non-null Function0", (Test test) ->
                {
                    final AsyncRunner runner1 = createCurrentThreadAsyncRunner();
                    final AsyncRunner runner2 = createCurrentThreadAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner1);

                    final AsyncFunction<Integer> thenOnAsyncFunction = basicAsyncTask.thenOn(runner2, () -> 0);
                    test.assertNotNull(thenOnAsyncFunction);
                    test.assertEqual(1, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(0, runner2.getScheduledTaskCount());

                    runner1.await();
                    test.assertEqual(1, runner2.getScheduledTaskCount());
                });

                runner.test("with non-null Function0", (Test test) ->
                {
                    final AsyncRunner runner1 = createCurrentThreadAsyncRunner();
                    final AsyncRunner runner2 = createCurrentThreadAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner1);

                    final AsyncFunction<Integer> thenOnAsyncFunction = basicAsyncTask.thenOn(runner2, () -> 0);
                    test.assertNotNull(thenOnAsyncFunction);
                    test.assertEqual(1, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(0, runner2.getScheduledTaskCount());

                    runner1.await();
                    test.assertEqual(1, runner2.getScheduledTaskCount());
                });

                runner.test("with non-null Function0 when completed", (Test test) ->
                {
                    final AsyncRunner runner1 = createCurrentThreadAsyncRunner();
                    final AsyncRunner runner2 = createCurrentThreadAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner1);
                    runner1.await();

                    final AsyncFunction<Integer> thenOnAsyncFunction = basicAsyncTask.thenOn(runner2, () -> 0);
                    test.assertNotNull(thenOnAsyncFunction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(1, runner2.getScheduledTaskCount());
                });

                runner.test("with exception throwing function", (Test test) ->
                {
                    final AsyncRunner asyncRunner1 = test.getMainAsyncRunner();
                    final AsyncRunner asyncRunner2 = createCurrentThreadAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = createScheduled(creator, asyncRunner1);
                    final AsyncFunction<Integer> thenAsyncFunction = basicAsyncTask.thenOn(asyncRunner2, () ->
                    {
                        if (1 + 1 == 2)
                        {
                            throw new RuntimeException("This exception should be swallowed by the AsyncRunner.");
                        }
                        return 0;
                    });
                    test.assertNotNull(thenAsyncFunction);
                    test.assertEqual(1, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(1, asyncRunner1.getScheduledTaskCount());
                    test.assertEqual(0, asyncRunner2.getScheduledTaskCount());

                    asyncRunner1.await();

                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertNull(basicAsyncTask.getIncomingError());
                    test.assertNull(basicAsyncTask.getOutgoingError());
                    test.assertEqual(0, asyncRunner1.getScheduledTaskCount());
                    test.assertEqual(1, asyncRunner2.getScheduledTaskCount());

                    asyncRunner2.await();

                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(0, asyncRunner1.getScheduledTaskCount());
                    test.assertEqual(0, asyncRunner2.getScheduledTaskCount());
                    test.assertNull(basicAsyncTask.getIncomingError());
                    test.assertNull(basicAsyncTask.getOutgoingError());
                    test.assertTrue(thenAsyncFunction.isCompleted());
                    test.assertNull(thenAsyncFunction.getIncomingError());
                    test.assertNotNull(thenAsyncFunction.getOutgoingError());
                    test.assertEqual(RuntimeException.class, thenAsyncFunction.getOutgoingError().getClass());
                    test.assertEqual("This exception should be swallowed by the AsyncRunner.", thenAsyncFunction.getOutgoingError().getMessage());
                    test.assertNull(thenAsyncFunction.awaitReturn());
                });
            });

            runner.testGroup("thenAsyncAction(Function0<AsyncAction>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final BasicAsyncTask basicAsyncTask = create(creator);
                    final AsyncAction thenAsyncAction = basicAsyncTask.thenAsyncAction(null);
                    test.assertNull(thenAsyncAction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final AsyncRunner runner12 = createCurrentThreadAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner12);

                    final Value<Integer> value = new Value<>();
                    final AsyncAction thenAsyncAction = basicAsyncTask.thenAsyncAction(() ->
                    {
                        return runner12.schedule(() -> value.set(5));
                    });
                    test.assertNotNull(thenAsyncAction);
                    test.assertEqual(1, basicAsyncTask.getPausedTaskCount());

                    runner12.await();
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertTrue(basicAsyncTask.isCompleted());
                    test.assertEqual(0, runner12.getScheduledTaskCount());
                    test.assertEqual(5, value.get());
                });

                runner.test("with non-null when completed", (Test test) ->
                {
                    final AsyncRunner runner12 = createCurrentThreadAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner12);
                    runner12.await();

                    final Value<Integer> value = new Value<>();
                    final AsyncAction thenAsyncAction = basicAsyncTask.thenAsyncAction(() ->
                    {
                        return runner12.schedule(() -> value.set(5));
                    });
                    test.assertNotNull(thenAsyncAction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(1, runner12.getScheduledTaskCount());

                    runner12.await();
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertTrue(basicAsyncTask.isCompleted());
                    test.assertEqual(0, runner12.getScheduledTaskCount());
                    test.assertEqual(5, value.get());
                });

                runner.test("with exception throwing function", (Test test) ->
                {
                    final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = createScheduled(creator, asyncRunner);

                    final Value<Integer> value = new Value<>();
                    final AsyncAction thenAsyncAction = basicAsyncTask.thenAsyncAction(() ->
                    {
                        if (1 + 1 == 2)
                        {
                            throw new RuntimeException("ABC");
                        }
                        return asyncRunner.schedule(() -> value.set(5));
                    });
                    test.assertNotNull(thenAsyncAction);
                    test.assertEqual(1, basicAsyncTask.getPausedTaskCount());

                    asyncRunner.await();

                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertTrue(basicAsyncTask.isCompleted());
                    test.assertEqual(0, asyncRunner.getScheduledTaskCount());
                    test.assertTrue(thenAsyncAction.isCompleted());
                    test.assertNull(thenAsyncAction.getIncomingError());
                    test.assertNotNull(thenAsyncAction.getOutgoingError());
                    test.assertEqual(RuntimeException.class, thenAsyncAction.getOutgoingError().getClass());
                    test.assertEqual("ABC", thenAsyncAction.getOutgoingError().getMessage());
                    test.assertFalse(value.hasValue());
                });

                runner.test("with exception throwing inner function", (Test test) ->
                {
                    final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = createScheduled(creator, asyncRunner);

                    final Value<Integer> value = new Value<>();
                    final AsyncAction thenAsyncAction = basicAsyncTask.thenAsyncAction(() ->
                    {
                        return asyncRunner.schedule(() ->
                        {
                            if (1 + 1 == 2)
                            {
                                throw new RuntimeException("ABC");
                            }
                            value.set(5);
                        });
                    });
                    test.assertNotNull(thenAsyncAction);
                    test.assertEqual(1, basicAsyncTask.getPausedTaskCount());

                    asyncRunner.await();

                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertTrue(basicAsyncTask.isCompleted());
                    test.assertEqual(0, asyncRunner.getScheduledTaskCount());
                    test.assertTrue(thenAsyncAction.isCompleted());
                    test.assertNull(thenAsyncAction.getIncomingError());
                    test.assertNotNull(thenAsyncAction.getOutgoingError());
                    test.assertEqual(RuntimeException.class, thenAsyncAction.getOutgoingError().getClass());
                    test.assertEqual("ABC", thenAsyncAction.getOutgoingError().getMessage());
                    test.assertFalse(value.hasValue());
                });
            });

            runner.testGroup("thenAsyncFunction(Function1<T,AsyncAction>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final BasicAsyncTask basicAsyncTask = create(creator);
                    final AsyncFunction<Integer> thenAsyncFunction = basicAsyncTask.thenAsyncFunction(null);
                    test.assertNull(thenAsyncFunction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                });

                runner.test("with non-null", (Test test) ->
                {
                    final AsyncRunner runner12 = createCurrentThreadAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner12);

                    final Value<Integer> value = new Value<>();
                    final AsyncAction thenAsyncAction = basicAsyncTask
                        .thenAsyncFunction(() ->
                        {
                            return runner12.schedule(() ->
                            {
                                value.set(5);
                                return 6;
                            });
                        })
                        .then((Integer asyncFunctionReturnValue) ->
                        {
                            test.assertEqual(6, asyncFunctionReturnValue);
                        });
                    test.assertNotNull(thenAsyncAction);
                    test.assertEqual(1, basicAsyncTask.getPausedTaskCount());

                    runner12.await();
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertTrue(basicAsyncTask.isCompleted());
                    test.assertEqual(0, runner12.getScheduledTaskCount());
                    test.assertEqual(5, value.get());
                });

                runner.test("with non-null when completed", (Test test) ->
                {
                    final AsyncRunner runner12 = createCurrentThreadAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner12);
                    runner12.await();

                    final Value<Integer> value = new Value<>();
                    final AsyncAction thenAsyncAction = basicAsyncTask
                        .thenAsyncFunction(() ->
                        {
                            return runner12.schedule(() ->
                            {
                                value.set(5);
                                return 6;
                            });
                        })
                        .then((Integer asyncFunctionReturnValue) ->
                        {
                            test.assertEqual(6, asyncFunctionReturnValue);
                        });
                    test.assertNotNull(thenAsyncAction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(1, runner12.getScheduledTaskCount());

                    runner12.await();
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertTrue(basicAsyncTask.isCompleted());
                    test.assertEqual(0, runner12.getScheduledTaskCount());
                    test.assertEqual(5, value.get());
                });

                runner.test("with exception throwing function", (Test test) ->
                {
                    final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = createScheduled(creator, asyncRunner);

                    final Value<Integer> value = new Value<>();
                    final AsyncFunction<Integer> thenAsyncFunction = basicAsyncTask.thenAsyncFunction(() ->
                    {
                        if (1 + 1 == 2)
                        {
                            throw new RuntimeException("ABC");
                        }
                        return asyncRunner.schedule(() ->
                            {
                                value.set(5);
                                return 7;
                            });
                    });
                    test.assertNotNull(thenAsyncFunction);
                    test.assertEqual(1, basicAsyncTask.getPausedTaskCount());

                    asyncRunner.await();

                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertTrue(basicAsyncTask.isCompleted());
                    test.assertEqual(0, asyncRunner.getScheduledTaskCount());
                    test.assertTrue(thenAsyncFunction.isCompleted());
                    test.assertNull(thenAsyncFunction.getIncomingError());
                    test.assertNotNull(thenAsyncFunction.getOutgoingError());
                    test.assertEqual(RuntimeException.class, thenAsyncFunction.getOutgoingError().getClass());
                    test.assertEqual("ABC", thenAsyncFunction.getOutgoingError().getMessage());
                    test.assertFalse(value.hasValue());
                    test.assertNull(thenAsyncFunction.awaitReturn());
                });

                runner.test("with exception throwing inner function", (Test test) ->
                {
                    final AsyncRunner asyncRunner = test.getMainAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = createScheduled(creator, asyncRunner);

                    final Value<Integer> value = new Value<>();
                    final AsyncFunction<Integer> thenAsyncFunction = basicAsyncTask.thenAsyncFunction(() ->
                    {
                        return asyncRunner.schedule(() ->
                        {
                            if (1 + 1 == 2)
                            {
                                throw new RuntimeException("ABC");
                            }
                            value.set(5);
                            return 7;
                        });
                    });
                    test.assertNotNull(thenAsyncFunction);
                    test.assertEqual(1, basicAsyncTask.getPausedTaskCount());

                    asyncRunner.await();

                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertTrue(basicAsyncTask.isCompleted());
                    test.assertEqual(0, asyncRunner.getScheduledTaskCount());
                    test.assertTrue(thenAsyncFunction.isCompleted());
                    test.assertNull(thenAsyncFunction.getIncomingError());
                    test.assertNotNull(thenAsyncFunction.getOutgoingError());
                    test.assertEqual(RuntimeException.class, thenAsyncFunction.getOutgoingError().getClass());
                    test.assertEqual("ABC", thenAsyncFunction.getOutgoingError().getMessage());
                    test.assertFalse(value.hasValue());
                    test.assertNull(thenAsyncFunction.awaitReturn());
                });
            });

            runner.testGroup("thenAsyncActionOn(AsyncRunner,Function0<AsyncAction>)", () ->
            {
                runner.test("with null AsyncRunner", (Test test) ->
                {
                    final AsyncRunner runner12 = createCurrentThreadAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = create(creator);

                    final Value<Integer> value = new Value<>();
                    final AsyncAction thenOnAsyncAction = basicAsyncTask.thenAsyncActionOn(null, () ->
                    {
                        return runner12.schedule(() ->
                        {
                            value.set(5);
                        });
                    });
                    test.assertNull(thenOnAsyncAction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                });

                runner.test("with null Function", (Test test) ->
                {
                    final AsyncRunner runner12 = createCurrentThreadAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = create(creator);

                    final AsyncAction thenOnAsyncAction = basicAsyncTask.thenAsyncActionOn(runner12, null);
                    test.assertNull(thenOnAsyncAction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                });

                runner.test("with non-null Function", (Test test) ->
                {
                    final AsyncRunner runner1 = createCurrentThreadAsyncRunner();
                    final AsyncRunner runner2 = createCurrentThreadAsyncRunner();
                    final AsyncRunner runner3 = createCurrentThreadAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner1);

                    final Value<Integer> value = new Value<>();
                    final AsyncAction thenOnAsyncAction = basicAsyncTask
                        .thenAsyncActionOn(runner2, () ->
                        {
                            return runner3.schedule(() ->
                            {
                                value.set(4);
                            });
                        })
                        .then(() ->
                        {
                            test.assertEqual(4, value.get());
                        });
                    test.assertNotNull(thenOnAsyncAction);
                    test.assertEqual(1, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(1, runner1.getScheduledTaskCount());
                    test.assertEqual(0, runner2.getScheduledTaskCount());
                    test.assertEqual(0, runner3.getScheduledTaskCount());
                    test.assertFalse(value.hasValue());

                    runner1.await();
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(0, runner1.getScheduledTaskCount());
                    test.assertEqual(1, runner2.getScheduledTaskCount());
                    test.assertEqual(0, runner3.getScheduledTaskCount());
                    test.assertFalse(value.hasValue());

                    runner2.await();
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(0, runner1.getScheduledTaskCount());
                    test.assertEqual(0, runner2.getScheduledTaskCount());
                    test.assertEqual(1, runner3.getScheduledTaskCount());
                    test.assertFalse(value.hasValue());

                    runner3.await();
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(0, runner1.getScheduledTaskCount());
                    test.assertEqual(0, runner2.getScheduledTaskCount());
                    test.assertEqual(0, runner3.getScheduledTaskCount());
                    test.assertEqual(4, value.get());
                });

                runner.test("with non-null Function when completed", (Test test) ->
                {
                    final AsyncRunner runner1 = createCurrentThreadAsyncRunner();
                    final AsyncRunner runner2 = createCurrentThreadAsyncRunner();
                    final AsyncRunner runner3 = createCurrentThreadAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner1);
                    runner1.await();

                    final Value<Integer> value = new Value<>();
                    final AsyncAction thenOnAsyncAction = basicAsyncTask
                        .thenAsyncActionOn(runner2, () ->
                        {
                            return runner3.schedule(() ->
                            {
                                value.set(4);
                            });
                        })
                        .then(() ->
                        {
                            test.assertEqual(4, value.get());
                        });
                    test.assertNotNull(thenOnAsyncAction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(0, runner1.getScheduledTaskCount());
                    test.assertEqual(1, runner2.getScheduledTaskCount());
                    test.assertEqual(0, runner3.getScheduledTaskCount());
                    test.assertFalse(value.hasValue());

                    runner2.await();
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(0, runner1.getScheduledTaskCount());
                    test.assertEqual(0, runner2.getScheduledTaskCount());
                    test.assertEqual(1, runner3.getScheduledTaskCount());
                    test.assertFalse(value.hasValue());

                    runner3.await();
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(0, runner1.getScheduledTaskCount());
                    test.assertEqual(0, runner2.getScheduledTaskCount());
                    test.assertEqual(0, runner3.getScheduledTaskCount());
                    test.assertEqual(4, value.get());
                });
                
                runner.test("with multiple then tasks targeting different AsyncRunners", (Test test) ->
                {
                    final AsyncRunner runner1 = createCurrentThreadAsyncRunner();
                    final AsyncRunner runner2 = createCurrentThreadAsyncRunner();
                    final AsyncRunner runner3 = createCurrentThreadAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner1);

                    final Value<Integer> value1 = new Value<>();
                    final AsyncAction thenOnAsyncAction1 = basicAsyncTask
                        .thenAsyncActionOn(runner2, () ->
                        {
                            return runner3.schedule(() ->value1.set(4));
                        })
                        .thenOn(runner3, () ->
                        {
                            test.assertEqual(4, value1.get());
                        });

                    final Value<Integer> value2 = new Value<>();
                    final AsyncAction thenOnAsyncAction2 = basicAsyncTask
                        .thenAsyncActionOn(runner3, () ->
                        {
                            return runner2.schedule(() -> value2.set(5));
                        })
                        .thenOn(runner2, () ->
                        {
                            test.assertEqual(5, value2.get());
                        });

                    test.assertNotNull(thenOnAsyncAction1);
                    test.assertNotNull(thenOnAsyncAction2);
                    test.assertEqual(2, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(1, runner1.getScheduledTaskCount());
                    test.assertEqual(0, runner2.getScheduledTaskCount());
                    test.assertEqual(0, runner3.getScheduledTaskCount());
                    test.assertFalse(value1.hasValue());
                    test.assertFalse(value2.hasValue());

                    runner1.await();
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(0, runner1.getScheduledTaskCount());
                    test.assertEqual(1, runner2.getScheduledTaskCount());
                    test.assertEqual(1, runner3.getScheduledTaskCount());
                    test.assertFalse(value1.hasValue());
                    test.assertFalse(value2.hasValue());

                    runner2.await();
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(0, runner1.getScheduledTaskCount());
                    test.assertEqual(0, runner2.getScheduledTaskCount());
                    test.assertEqual(2, runner3.getScheduledTaskCount());
                    test.assertFalse(value1.hasValue());
                    test.assertFalse(value2.hasValue());

                    runner3.await();
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(0, runner1.getScheduledTaskCount());
                    test.assertEqual(1, runner2.getScheduledTaskCount());
                    test.assertEqual(0, runner3.getScheduledTaskCount());
                    test.assertEqual(4, value1.get());
                    test.assertFalse(value2.hasValue());

                    runner2.await();
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(0, runner1.getScheduledTaskCount());
                    test.assertEqual(0, runner2.getScheduledTaskCount());
                    test.assertEqual(0, runner3.getScheduledTaskCount());
                    test.assertEqual(4, value1.get());
                    test.assertEqual(5, value2.get());
                });
            });

            runner.testGroup("thenAsyncFunctionOn(AsyncRunner,Function0<AsyncFunction<T>>)", () ->
            {
                runner.test("with null AsyncRunner", (Test test) ->
                {
                    final AsyncRunner asyncRunner = createCurrentThreadAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = create(creator);

                    final AsyncFunction<Integer> thenOnAsyncFunction = basicAsyncTask.thenAsyncFunctionOn(null, () ->
                    {
                        return asyncRunner.schedule(() -> 10);
                    });
                    test.assertNull(thenOnAsyncFunction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                });

                runner.test("with null Function0", (Test test) ->
                {
                    final AsyncRunner runner12 = createCurrentThreadAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = create(creator);

                    final AsyncFunction<Integer> thenOnAsyncFunction = basicAsyncTask.thenAsyncFunctionOn(runner12, null);
                    test.assertNull(thenOnAsyncFunction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                });

                runner.test("with non-null Function0", (Test test) ->
                {
                    final AsyncRunner runner1 = createCurrentThreadAsyncRunner();
                    final AsyncRunner runner2 = createCurrentThreadAsyncRunner();
                    final AsyncRunner runner3 = createCurrentThreadAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner1);

                    final Value<Integer> value = new Value<>();
                    final AsyncAction thenOnAsyncAction = basicAsyncTask
                        .thenAsyncFunctionOn(runner2, () ->
                        {
                            return runner3.schedule(() ->
                            {
                                value.set(4);
                                return 5;
                            });
                        })
                        .then((Integer asyncFunctionReturnValue) ->
                        {
                            test.assertEqual(5, asyncFunctionReturnValue);
                        });
                    test.assertNotNull(thenOnAsyncAction);
                    test.assertEqual(1, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(1, runner1.getScheduledTaskCount());
                    test.assertEqual(0, runner2.getScheduledTaskCount());
                    test.assertEqual(0, runner3.getScheduledTaskCount());
                    test.assertFalse(value.hasValue());

                    runner1.await();
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(0, runner1.getScheduledTaskCount());
                    test.assertEqual(1, runner2.getScheduledTaskCount());
                    test.assertEqual(0, runner3.getScheduledTaskCount());
                    test.assertFalse(value.hasValue());

                    runner2.await();
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(0, runner1.getScheduledTaskCount());
                    test.assertEqual(0, runner2.getScheduledTaskCount());
                    test.assertEqual(1, runner3.getScheduledTaskCount());
                    test.assertFalse(value.hasValue());

                    runner3.await();
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(0, runner1.getScheduledTaskCount());
                    test.assertEqual(0, runner2.getScheduledTaskCount());
                    test.assertEqual(0, runner3.getScheduledTaskCount());
                    test.assertEqual(4, value.get());
                });

                runner.test("with non-null Function0 when completed", (Test test) ->
                {
                    final AsyncRunner runner1 = createCurrentThreadAsyncRunner();
                    final AsyncRunner runner2 = createCurrentThreadAsyncRunner();
                    final AsyncRunner runner3 = createCurrentThreadAsyncRunner();
                    final BasicAsyncTask basicAsyncTask = createScheduled(creator, runner1);
                    runner1.await();

                    final Value<Integer> value = new Value<>();
                    final AsyncAction thenOnAsyncAction = basicAsyncTask
                        .thenAsyncFunctionOn(runner2, () ->
                        {
                            return runner3.schedule(() ->
                            {
                                value.set(4);
                                return 5;
                            });
                        })
                        .then((Integer asyncFunctionReturnValue) ->
                        {
                            test.assertEqual(5, asyncFunctionReturnValue);
                        });
                    test.assertNotNull(thenOnAsyncAction);
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(0, runner1.getScheduledTaskCount());
                    test.assertEqual(1, runner2.getScheduledTaskCount());
                    test.assertEqual(0, runner3.getScheduledTaskCount());
                    test.assertFalse(value.hasValue());

                    runner2.await();
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(0, runner1.getScheduledTaskCount());
                    test.assertEqual(0, runner2.getScheduledTaskCount());
                    test.assertEqual(1, runner3.getScheduledTaskCount());
                    test.assertFalse(value.hasValue());

                    runner3.await();
                    test.assertEqual(0, basicAsyncTask.getPausedTaskCount());
                    test.assertEqual(0, runner1.getScheduledTaskCount());
                    test.assertEqual(0, runner2.getScheduledTaskCount());
                    test.assertEqual(0, runner3.getScheduledTaskCount());
                    test.assertEqual(4, value.get());
                });
            });
        });
    }

    private static AsyncRunner createCurrentThreadAsyncRunner()
    {
        final Synchronization synchronization = new Synchronization();
        return new ManualAsyncRunner(() -> synchronization);
    }

    private static BasicAsyncTask create(Function1<AsyncRunner,BasicAsyncTask> creator)
    {
        return creator.run(new ManualAsyncRunner());
    }

    private static BasicAsyncTask createScheduled(Function1<AsyncRunner,BasicAsyncTask> creator, AsyncRunner asyncRunner)
    {
        final BasicAsyncTask basicAsyncAction = creator.run(asyncRunner);
        basicAsyncAction.schedule();
        return basicAsyncAction;
    }
}
