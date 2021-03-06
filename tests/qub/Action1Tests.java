package qub;

public class Action1Tests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Action1.class, () ->
        {
            runner.testGroup("sequence(Action1<T>...)", () ->
            {
                runner.test("with no arguments", (Test test) ->
                {
                    test.assertThrows(() -> Action1.sequence(),
                        new PreConditionFailure("actions cannot be empty."));
                });

                runner.test("with null argument array", (Test test) ->
                {
                    test.assertThrows(() -> Action1.sequence((Action1<String>[])null),
                        new PreConditionFailure("actions cannot be null."));
                });

                runner.test("with one null argument", (Test test) ->
                {
                    final Action1<String> action = Action1.sequence((Action1<String>)null);
                    test.assertNotNull(action);
                    action.run("hello");
                });

                runner.test("with one non-null argument", (Test test) ->
                {
                    final Value<String> value = Value.create();
                    final Action1<String> setValue = value::set;
                    final Action1<String> action = Action1.sequence(setValue);
                    test.assertSame(action, setValue);
                });

                runner.test("with null and null arguments", (Test test) ->
                {
                    final Action1<String> action = Action1.sequence(null, null);
                    test.assertNotNull(action);
                    action.run("hello");
                });

                runner.test("with null and non-null arguments", (Test test) ->
                {
                    final Value<String> value = Value.create();
                    final Action1<String> setValue = value::set;
                    final Action1<String> action = Action1.sequence(null, setValue);
                    test.assertSame(action, setValue);
                });

                runner.test("with non-null and null arguments", (Test test) ->
                {
                    final Value<String> value = Value.create();
                    final Action1<String> setValue = value::set;
                    final Action1<String> action = Action1.sequence(setValue, null);
                    test.assertSame(action, setValue);
                });

                runner.test("with non-null and non-null arguments", (Test test) ->
                {
                    final Value<String> value1 = Value.create();
                    final Value<String> value2 = Value.create();
                    final Action1<String> action = Action1.sequence(value1::set, value2::set);
                    test.assertNotNull(action);
                    action.run("oops");
                    test.assertEqual("oops", value1.get());
                    test.assertEqual("oops", value2.get());
                });
            });
        });
    }
}
