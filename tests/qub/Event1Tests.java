package qub;

public interface Event1Tests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Event1.class, () ->
        {
            runner.test("create()", (Test test) ->
            {
                final RunnableEvent1<Integer> event = Event1.create();
                test.assertNotNull(event);
            });
        });
    }

    static void test(TestRunner runner, Function0<? extends Event1<Integer>> creator)
    {
        runner.testGroup(Event1.class, () ->
        {
            Event0Tests.test(runner, creator);

            runner.testGroup("subscribe(Action1<T>)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Event1<Integer> event = creator.run();
                    test.assertThrows(() -> event.subscribe((Action1<Integer>)null),
                        new PreConditionFailure("callback cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final Event1<Integer> event = creator.run();
                    final Disposable disposable = event.subscribe((Integer value) -> {});
                    test.assertNotNull(disposable);
                    test.assertFalse(disposable.isDisposed());
                });
            });
        });
    }
}
