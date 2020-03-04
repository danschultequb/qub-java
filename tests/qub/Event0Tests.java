package qub;

public interface Event0Tests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(Event0.class, () ->
        {
            runner.test("create()", (Test test) ->
            {
                final RunnableEvent0 event = Event0.create();
                test.assertNotNull(event);
            });
        });
    }

    static void test(TestRunner runner, Function0<Event0> creator)
    {
        runner.testGroup(Event0.class, () ->
        {
            runner.testGroup("add(Action0)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Event0 event = creator.run();
                    test.assertThrows(() -> event.add(null),
                        new PreConditionFailure("callback cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final Event0 event = creator.run();
                    final Disposable disposable = event.add(() -> {});
                    test.assertNotNull(disposable);
                    test.assertFalse(disposable.isDisposed());
                });
            });
        });
    }
}
