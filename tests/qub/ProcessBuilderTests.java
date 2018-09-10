package qub;

public class ProcessBuilderTests
{
    private static ProcessBuilder createBuilder(Test test)
    {
        final FileSystem fileSystem = test.getFileSystem();
        final File fakeFile = fileSystem.getFile("C:/idontexist.exe").getValue();
        return new ProcessBuilder(test.getParallelAsyncRunner(), fakeFile);
    }

    public static void test(TestRunner runner)
    {
        runner.testGroup(ProcessBuilder.class, () ->
        {
            runner.test("constructor()", (Test test) ->
            {
                final ProcessBuilder builder = createBuilder(test);
                test.assertEqual("C:/idontexist.exe", builder.getExecutableFile().toString());
                test.assertEqual(0, builder.getArgumentCount());
                test.assertEqual("C:/idontexist.exe", builder.getCommand());
            });

            runner.testGroup("addArgument()", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final ProcessBuilder builder = createBuilder(test);
                    builder.addArgument(null);
                    test.assertEqual(0, builder.getArgumentCount());
                    test.assertEqual("C:/idontexist.exe", builder.getCommand());
                });

                runner.test("with empty", (Test test) ->
                {
                    final ProcessBuilder builder = createBuilder(test);
                    builder.addArgument("");
                    test.assertEqual(0, builder.getArgumentCount());
                    test.assertEqual("C:/idontexist.exe", builder.getCommand());
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final ProcessBuilder builder = createBuilder(test);
                    builder.addArgument("test");
                    test.assertEqual(1, builder.getArgumentCount());
                    test.assertEqual("test", builder.getArgument(0));
                    test.assertEqual("C:/idontexist.exe test", builder.getCommand());
                });
            });

            runner.testGroup("addArguments()", () ->
            {
                runner.test("with no arguments", (Test test) ->
                {
                    final ProcessBuilder builder = createBuilder(test);
                    builder.addArguments();
                    test.assertEqual(0, builder.getArgumentCount());
                    test.assertEqual("C:/idontexist.exe", builder.getCommand());
                });

                runner.test("with one null value", (Test test) ->
                {
                    final ProcessBuilder builder = createBuilder(test);
                    builder.addArguments((String)null);
                    test.assertEqual(0, builder.getArgumentCount());
                    test.assertEqual("C:/idontexist.exe", builder.getCommand());
                });
            });

            runner.testGroup("setArgument()", () ->
            {
                runner.test("with negative index", (Test test) ->
                {
                    final ProcessBuilder builder = createBuilder(test);
                    builder.setArgument(-1, "test");
                    test.assertEqual(0, builder.getArgumentCount());
                    test.assertEqual("C:/idontexist.exe", builder.getCommand());
                });

                runner.test("with null value", (Test test) ->
                {
                    final ProcessBuilder builder = createBuilder(test);
                    builder.addArguments("a", "b", "c");
                    builder.setArgument(0, null);
                    test.assertEqual(Array.fromValues(new String[] { "b", "c" }), builder.getArguments());
                    test.assertEqual("C:/idontexist.exe b c", builder.getCommand());
                });

                runner.test("with emtpy value", (Test test) ->
                {
                    final ProcessBuilder builder = createBuilder(test);
                    builder.addArguments("a", "b", "c");
                    builder.setArgument(2, "");
                    test.assertEqual(Array.fromValues(new String[] { "a", "b", "" }), builder.getArguments());
                    test.assertEqual("C:/idontexist.exe a b \"\"", builder.getCommand());
                });

                runner.test("with non-empty value", (Test test) ->
                {
                    final ProcessBuilder builder = createBuilder(test);
                    builder.addArguments("a", "b", "c");
                    builder.setArgument(1, "\"d\"");
                    test.assertEqual(Array.fromValues(new String[] { "a", "\"d\"", "c" }), builder.getArguments());
                    test.assertEqual("C:/idontexist.exe a \"d\" c", builder.getCommand());
                });
            });

            runner.test("removeArgumentAt()", (Test test) ->
            {
                final ProcessBuilder builder = createBuilder(test);
                builder.addArguments("a", "b", "c");
                builder.removeArgument(1);
                test.assertEqual(Array.fromValues(new String[] { "a", "c" }), builder.getArguments());
                test.assertEqual("C:/idontexist.exe a c", builder.getCommand());
            });

            runner.test("run() with not found executable file", (Test test) ->
            {
                final ProcessBuilder builder = createBuilder(test);
                builder.addArgument("won't matter");
                test.assertEqual(null, builder.run());
                test.assertEqual("C:/idontexist.exe \"won't matter\"", builder.getCommand());
            });

            runner.test("runAsync() with not found executable file", (Test test) ->
            {
                final ProcessBuilder builder = createBuilder(test);
                builder.addArgument("won't matter");
                test.assertEqual(null, builder.runAsync().awaitReturn());
                test.assertEqual("C:/idontexist.exe \"won't matter\"", builder.getCommand());
            });

            runner.test("escapeArgument()", (Test test) ->
            {
                test.assertEqual("\"Then he said, \\\"Hey there!\\\"\"", ProcessBuilder.escapeArgument("Then he said, \"Hey there!\""));
                test.assertEqual("-argument=\"value\"", ProcessBuilder.escapeArgument("-argument=\"value\""));
                test.assertEqual("\"\"", ProcessBuilder.escapeArgument(""));
            });
        });
    }
}
