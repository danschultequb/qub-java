package qub;

public interface CommandLineActionTests
{
    static void test(TestRunner runner)
    {
        PreCondition.assertNotNull(runner, "runner");

        runner.testGroup(CommandLineAction.class, () ->
        {
            runner.testGroup("create(String,Action1<TProcess>)", () ->
            {
                runner.test("with null name", (Test test) ->
                {
                    test.assertThrows(() -> CommandLineAction.create(null, (Process process) -> {}),
                        new PreConditionFailure("name cannot be null."));
                });

                runner.test("with empty name", (Test test) ->
                {
                    test.assertThrows(() -> CommandLineAction.create("", (Process process) -> {}),
                        new PreConditionFailure("name cannot be empty."));
                });

                runner.test("with non-empty name", (Test test) ->
                {
                    final CommandLineAction<Process> action = CommandLineAction.create("hello", (Process process) -> {});
                    test.assertEqual("hello", action.getName());
                    test.assertEqual(Iterable.create(), action.getAliases());
                    test.assertNull(action.getDescription());
                });

                runner.test("with null mainAction", (Test test) ->
                {
                    test.assertThrows(() -> CommandLineAction.create("hello", null),
                        new PreConditionFailure("mainAction cannot be null."));
                });
            });

            runner.testGroup("addAlias(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CommandLineAction<Process> action = CommandLineAction.create("hello", (Process process) -> {});
                    test.assertThrows(() -> action.addAlias(null),
                        new PreConditionFailure("alias cannot be null."));
                    test.assertEqual(Iterable.create(), action.getAliases());
                });

                runner.test("with empty", (Test test) ->
                {
                    final CommandLineAction<Process> action = CommandLineAction.create("hello", (Process process) -> {});
                    test.assertThrows(() -> action.addAlias(""),
                        new PreConditionFailure("alias cannot be empty."));
                    test.assertEqual(Iterable.create(), action.getAliases());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final CommandLineAction<Process> action = CommandLineAction.create("hello", (Process process) -> {});
                    test.assertSame(action, action.addAlias("h"));
                    test.assertEqual(Iterable.create("h"), action.getAliases());
                });

                runner.test("with alias equal to action's name", (Test test) ->
                {
                    final CommandLineAction<Process> action = CommandLineAction.create("hello", (Process process) -> {});
                    test.assertThrows(() -> action.addAlias("hello"),
                        new PreConditionFailure("this.aliasAlreadyExists(alias) cannot be true."));
                    test.assertEqual(Iterable.create(), action.getAliases());
                });

                runner.test("with alias already added to action", (Test test) ->
                {
                    final CommandLineAction<Process> action = CommandLineAction.create("hello", (Process process) -> {});
                    test.assertSame(action, action.addAlias("h"));
                    test.assertThrows(() -> action.addAlias("h"),
                        new PreConditionFailure("this.aliasAlreadyExists(alias) cannot be true."));
                    test.assertEqual(Iterable.create("h"), action.getAliases());
                });

                runner.test("with alias already in use as a different action's name", (Test test) ->
                {
                    final CommandLineActions<Process> actions = CommandLineActions.create();
                    final CommandLineAction<Process> action1 = actions.addAction("hello", (Process process) -> {});
                    final CommandLineAction<Process> action2 = actions.addAction("there", (Process process) -> {});
                    test.assertThrows(() -> action1.addAlias("there"),
                        new PreConditionFailure("this.aliasAlreadyExists(alias) cannot be true."));
                    test.assertEqual(Iterable.create(), action1.getAliases());
                    test.assertEqual(Iterable.create(), action2.getAliases());
                });

                runner.test("with alias already in use as a different action's alias", (Test test) ->
                {
                    final CommandLineActions<Process> actions = CommandLineActions.create();
                    final CommandLineAction<Process> action1 = actions.addAction("hello", (Process process) -> {});
                    final CommandLineAction<Process> action2 = actions.addAction("there", (Process process) -> {})
                        .addAlias("h");
                    test.assertThrows(() -> action1.addAlias("h"),
                        new PreConditionFailure("this.aliasAlreadyExists(alias) cannot be true."));
                    test.assertEqual(Iterable.create(), action1.getAliases());
                    test.assertEqual(Iterable.create("h"), action2.getAliases());
                });
            });

            runner.testGroup("addAliases(String...)", () ->
            {
                runner.test("with no arguments", (Test test) ->
                {
                    final CommandLineAction<Process> action = CommandLineAction.create("hello", (Process process) -> {});
                    test.assertThrows(() -> action.addAliases(),
                        new PreConditionFailure("aliases cannot be empty."));
                    test.assertEqual(Iterable.create(), action.getAliases());
                });

                runner.test("with null array", (Test test) ->
                {
                    final CommandLineAction<Process> action = CommandLineAction.create("hello", (Process process) -> {});
                    test.assertThrows(() -> action.addAliases((String[])null),
                        new PreConditionFailure("aliases cannot be null."));
                    test.assertEqual(Iterable.create(), action.getAliases());
                });

                runner.test("with empty array", (Test test) ->
                {
                    final CommandLineAction<Process> action = CommandLineAction.create("hello", (Process process) -> {});
                    test.assertThrows(() -> action.addAliases(new String[0]),
                        new PreConditionFailure("aliases cannot be empty."));
                    test.assertEqual(Iterable.create(), action.getAliases());
                });

                runner.test("with null String", (Test test) ->
                {
                    final CommandLineAction<Process> action = CommandLineAction.create("hello", (Process process) -> {});
                    test.assertThrows(() -> action.addAliases((String)null),
                        new PreConditionFailure("alias cannot be null."));
                    test.assertEqual(Iterable.create(), action.getAliases());
                });

                runner.test("with empty String", (Test test) ->
                {
                    final CommandLineAction<Process> action = CommandLineAction.create("hello", (Process process) -> {});
                    test.assertThrows(() -> action.addAliases(""),
                        new PreConditionFailure("alias cannot be empty."));
                    test.assertEqual(Iterable.create(), action.getAliases());
                });

                runner.test("with one String", (Test test) ->
                {
                    final CommandLineAction<Process> action = CommandLineAction.create("hello", (Process process) -> {});
                    test.assertSame(action, action.addAliases("h"));
                    test.assertEqual(Iterable.create("h"), action.getAliases());
                });

                runner.test("with two Strings", (Test test) ->
                {
                    final CommandLineAction<Process> action = CommandLineAction.create("hello", (Process process) -> {});
                    test.assertSame(action, action.addAliases("h", "i"));
                    test.assertEqual(Iterable.create("h", "i"), action.getAliases());
                });

                runner.test("with alias equal to action's name", (Test test) ->
                {
                    final CommandLineAction<Process> action = CommandLineAction.create("hello", (Process process) -> {});
                    test.assertThrows(() -> action.addAliases("hello"),
                        new PreConditionFailure("this.aliasAlreadyExists(alias) cannot be true."));
                    test.assertEqual(Iterable.create(), action.getAliases());
                });

                runner.test("with alias already added to action", (Test test) ->
                {
                    final CommandLineAction<Process> action = CommandLineAction.create("hello", (Process process) -> {});
                    test.assertSame(action, action.addAliases("h"));
                    test.assertThrows(() -> action.addAliases("h"),
                        new PreConditionFailure("this.aliasAlreadyExists(alias) cannot be true."));
                    test.assertEqual(Iterable.create("h"), action.getAliases());
                });

                runner.test("with alias already in use as a different action's name", (Test test) ->
                {
                    final CommandLineActions<Process> actions = CommandLineActions.create();
                    final CommandLineAction<Process> action1 = actions.addAction("hello", (Process process) -> {});
                    final CommandLineAction<Process> action2 = actions.addAction("there", (Process process) -> {});
                    test.assertThrows(() -> action1.addAliases("there"),
                        new PreConditionFailure("this.aliasAlreadyExists(alias) cannot be true."));
                    test.assertEqual(Iterable.create(), action1.getAliases());
                    test.assertEqual(Iterable.create(), action2.getAliases());
                });

                runner.test("with alias already in use as a different action's alias", (Test test) ->
                {
                    final CommandLineActions<Process> actions = CommandLineActions.create();
                    final CommandLineAction<Process> action1 = actions.addAction("hello", (Process process) -> {});
                    final CommandLineAction<Process> action2 = actions.addAction("there", (Process process) -> {})
                        .addAliases("h");
                    test.assertThrows(() -> action1.addAliases("h"),
                        new PreConditionFailure("this.aliasAlreadyExists(alias) cannot be true."));
                    test.assertEqual(Iterable.create(), action1.getAliases());
                    test.assertEqual(Iterable.create("h"), action2.getAliases());
                });
            });

            runner.testGroup("addAliases(Iterable<String>)", () ->
            {
                runner.test("with null Iterable", (Test test) ->
                {
                    final CommandLineAction<Process> action = CommandLineAction.create("hello", (Process process) -> {});
                    test.assertThrows(() -> action.addAliases((Iterable<String>)null),
                        new PreConditionFailure("aliases cannot be null."));
                    test.assertEqual(Iterable.create(), action.getAliases());
                });

                runner.test("with empty Iterable", (Test test) ->
                {
                    final CommandLineAction<Process> action = CommandLineAction.create("hello", (Process process) -> {});
                    test.assertThrows(() -> action.addAliases(Iterable.create()),
                        new PreConditionFailure("aliases cannot be empty."));
                    test.assertEqual(Iterable.create(), action.getAliases());
                });

                runner.test("with null String", (Test test) ->
                {
                    final CommandLineAction<Process> action = CommandLineAction.create("hello", (Process process) -> {});
                    test.assertThrows(() -> action.addAliases(Iterable.create((String)null)),
                        new PreConditionFailure("alias cannot be null."));
                    test.assertEqual(Iterable.create(), action.getAliases());
                });

                runner.test("with empty String", (Test test) ->
                {
                    final CommandLineAction<Process> action = CommandLineAction.create("hello", (Process process) -> {});
                    test.assertThrows(() -> action.addAliases(Iterable.create("")),
                        new PreConditionFailure("alias cannot be empty."));
                    test.assertEqual(Iterable.create(), action.getAliases());
                });

                runner.test("with one String", (Test test) ->
                {
                    final CommandLineAction<Process> action = CommandLineAction.create("hello", (Process process) -> {});
                    test.assertSame(action, action.addAliases(Iterable.create("h")));
                    test.assertEqual(Iterable.create("h"), action.getAliases());
                });

                runner.test("with two Strings", (Test test) ->
                {
                    final CommandLineAction<Process> action = CommandLineAction.create("hello", (Process process) -> {});
                    test.assertSame(action, action.addAliases(Iterable.create("h", "i")));
                    test.assertEqual(Iterable.create("h", "i"), action.getAliases());
                });

                runner.test("with alias equal to action's name", (Test test) ->
                {
                    final CommandLineAction<Process> action = CommandLineAction.create("hello", (Process process) -> {});
                    test.assertThrows(() -> action.addAliases(Iterable.create("hello")),
                        new PreConditionFailure("this.aliasAlreadyExists(alias) cannot be true."));
                    test.assertEqual(Iterable.create(), action.getAliases());
                });

                runner.test("with alias already added to action", (Test test) ->
                {
                    final CommandLineAction<Process> action = CommandLineAction.create("hello", (Process process) -> {});
                    test.assertSame(action, action.addAliases(Iterable.create("h")));
                    test.assertThrows(() -> action.addAliases(Iterable.create("h")),
                        new PreConditionFailure("this.aliasAlreadyExists(alias) cannot be true."));
                    test.assertEqual(Iterable.create("h"), action.getAliases());
                });

                runner.test("with alias already in use as a different action's name", (Test test) ->
                {
                    final CommandLineActions<Process> actions = CommandLineActions.create();
                    final CommandLineAction<Process> action1 = actions.addAction("hello", (Process process) -> {});
                    final CommandLineAction<Process> action2 = actions.addAction("there", (Process process) -> {});
                    test.assertThrows(() -> action1.addAliases(Iterable.create("there")),
                        new PreConditionFailure("this.aliasAlreadyExists(alias) cannot be true."));
                    test.assertEqual(Iterable.create(), action1.getAliases());
                    test.assertEqual(Iterable.create(), action2.getAliases());
                });

                runner.test("with alias already in use as a different action's alias", (Test test) ->
                {
                    final CommandLineActions<Process> actions = CommandLineActions.create();
                    final CommandLineAction<Process> action1 = actions.addAction("hello", (Process process) -> {});
                    final CommandLineAction<Process> action2 = actions.addAction("there", (Process process) -> {})
                        .addAliases(Iterable.create("h"));
                    test.assertThrows(() -> action1.addAliases(Iterable.create("h")),
                        new PreConditionFailure("this.aliasAlreadyExists(alias) cannot be true."));
                    test.assertEqual(Iterable.create(), action1.getAliases());
                    test.assertEqual(Iterable.create("h"), action2.getAliases());
                });
            });

            runner.testGroup("setDescription(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CommandLineAction<Process> action = CommandLineAction.create("hello", (Process process) -> {});
                    test.assertSame(action, action.setDescription(null));
                    test.assertNull(action.getDescription());
                });

                runner.test("with empty", (Test test) ->
                {
                    final CommandLineAction<Process> action = CommandLineAction.create("hello", (Process process) -> {});
                    test.assertSame(action, action.setDescription(""));
                    test.assertEqual("", action.getDescription());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final CommandLineAction<Process> action = CommandLineAction.create("hello", (Process process) -> {});
                    test.assertSame(action, action.setDescription("there"));
                    test.assertEqual("there", action.getDescription());
                });
            });

            runner.testGroup("setParentActions(CommandLineActions)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CommandLineAction<Process> action = CommandLineAction.create("hello", (Process process) -> {});
                    test.assertSame(action, action.setParentActions(null));
                });
            });

            runner.testGroup("run(Process)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final CommandLineAction<Process> action = CommandLineAction.create("hello", (Process process) -> {});
                    test.assertThrows(() -> action.run(null),
                        new PreConditionFailure("process cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final IntegerValue value = IntegerValue.create(0);
                    final CommandLineAction<Process> action = CommandLineAction.create("hello", (Process process) -> { value.increment(); });
                    try (final Process process = Process.create())
                    {
                        action.run(process);
                        test.assertEqual(1, value.get());
                    }
                });
            });
        });
    }
}