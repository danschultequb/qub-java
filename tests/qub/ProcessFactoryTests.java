package qub;

public interface ProcessFactoryTests
{
    static void test(TestRunner runner, Function1<Test,ProcessFactory> creator)
    {
        runner.testGroup(ProcessFactory.class, () ->
        {
            runner.test("getWorkingFolderPath()", (Test test) ->
            {
                final ProcessFactory factory = creator.run(test);
                test.assertNotNull(factory.getWorkingFolderPath());
            });

            runner.testGroup("getProcessBuilder(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final ProcessFactory factory = creator.run(test);
                    test.assertThrows(() -> factory.getProcessBuilder((String)null),
                        new PreConditionFailure("executablePath cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    final ProcessFactory factory = creator.run(test);
                    test.assertThrows(() -> factory.getProcessBuilder(""),
                        new PreConditionFailure("executablePath cannot be empty."));
                });

                runner.test("with \"javac\"", (Test test) ->
                {
                    final ProcessFactory factory = creator.run(test);
                    final ProcessBuilder processBuilder = factory.getProcessBuilder("javac").await();
                    test.assertNotNull(processBuilder);
                });
            });

            runner.testGroup("getProcessBuilder(Path)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final ProcessFactory factory = creator.run(test);
                    test.assertThrows(() -> factory.getProcessBuilder((Path)null),
                        new PreConditionFailure("executablePath cannot be null."));
                });
            });

            runner.testGroup("getProcessBuilder(File)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final ProcessFactory factory = creator.run(test);
                    test.assertThrows(() -> factory.getProcessBuilder((File)null),
                        new PreConditionFailure("executableFile cannot be null."));
                });
            });
        });
    }

    static void test(TestRunner runner)
    {
        runner.testGroup(ProcessFactory.class, () ->
        {
            runner.testGroup("getCommand()", () ->
            {
                runner.test("with null executablePath", (Test test) ->
                {
                    final Path executablePath = null;
                    final Iterable<String> arguments = Iterable.create("a", "b");
                    final Path workingFolderPath = Path.parse("/working/");
                    test.assertThrows(() -> ProcessFactory.getCommand(executablePath, arguments, workingFolderPath),
                        new PreConditionFailure("executablePath cannot be null."));
                });

                runner.test("with null arguments", (Test test) ->
                {
                    final Path executablePath = Path.parse("/executable/file");
                    final Iterable<String> arguments = null;
                    final Path workingFolderPath = Path.parse("/working/");
                    test.assertThrows(() -> ProcessFactory.getCommand(executablePath, arguments, workingFolderPath),
                        new PreConditionFailure("arguments cannot be null."));
                });

                runner.test("with null workingFolderPath", (Test test) ->
                {
                    final Path executablePath = Path.parse("/executable/file");
                    final Iterable<String> arguments = Iterable.create("a", "b", "c");
                    final Path workingFolderPath = null;
                    test.assertEqual("/executable/file a b c",
                        ProcessFactory.getCommand(executablePath, arguments, workingFolderPath));
                });

                runner.test("with empty arguments and non-null workingFolderPath", (Test test) ->
                {
                    final Path executablePath = Path.parse("/executable/file");
                    final Iterable<String> arguments = Iterable.create();
                    final Path workingFolderPath = Path.parse("/working/");
                    test.assertEqual("/working/: /executable/file",
                        ProcessFactory.getCommand(executablePath, arguments, workingFolderPath));
                });

                runner.test("with non-empty arguments and non-null workingFolderPath", (Test test) ->
                {
                    final Path executablePath = Path.parse("/executable/file");
                    final Iterable<String> arguments = Iterable.create("apples", "bananas");
                    final Path workingFolderPath = Path.parse("/working/");
                    test.assertEqual("/working/: /executable/file apples bananas",
                        ProcessFactory.getCommand(executablePath, arguments, workingFolderPath));
                });
            });

            runner.testGroup("escapeArgument()", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertThrows(() -> ProcessFactory.escapeArgument(null),
                        new PreConditionFailure("argument cannot be null."));
                });

                runner.test("with empty", (Test test) ->
                {
                    test.assertThrows(() -> ProcessFactory.escapeArgument(""),
                        new PreConditionFailure("argument cannot be empty."));
                });

                runner.test("with non-empty", (Test test) ->
                {
                    test.assertEqual("abc", ProcessFactory.escapeArgument("abc"));
                });

                runner.test("with non-empty that contains space", (Test test) ->
                {
                    test.assertEqual("\"a c\"", ProcessFactory.escapeArgument("a c"));
                });

                runner.test("with non-empty that contains multiple spaces", (Test test) ->
                {
                    test.assertEqual("\"a c e\"", ProcessFactory.escapeArgument("a c e"));
                });

                runner.test("with non-empty that contains tab", (Test test) ->
                {
                    test.assertEqual("\"a\tc\"", ProcessFactory.escapeArgument("a\tc"));
                });

                runner.test("with non-empty that contains newline", (Test test) ->
                {
                    test.assertEqual("\"a\nc\"", ProcessFactory.escapeArgument("a\nc"));
                });

                runner.test("with non-empty that contains carriage return", (Test test) ->
                {
                    test.assertEqual("\"a\rc\"", ProcessFactory.escapeArgument("a\rc"));
                });

                runner.test("with non-empty that contains single quote", (Test test) ->
                {
                    test.assertEqual("a\'c", ProcessFactory.escapeArgument("a\'c"));
                });

                runner.test("with non-empty that contains double quote", (Test test) ->
                {
                    test.assertEqual("\"a\\\"c\"", ProcessFactory.escapeArgument("a\"c"));
                });

                runner.test("with non-empty that contains multiple double quotes", (Test test) ->
                {
                    test.assertEqual("\"a\\\"\\\"c\"", ProcessFactory.escapeArgument("a\"\"c"));
                });

                runner.test("with non-empty that is missing closing double quote", (Test test) ->
                {
                    test.assertEqual("\"hello\\\"\"", ProcessFactory.escapeArgument("hello\""));
                });

                runner.test("with quoted empty", (Test test) ->
                {
                    test.assertEqual("\"\"", ProcessFactory.escapeArgument("\"\""));
                });

                runner.test("with quoted space", (Test test) ->
                {
                    test.assertEqual("\" \"", ProcessFactory.escapeArgument("\" \""));
                });

                runner.test("with quoted single quote", (Test test) ->
                {
                    test.assertEqual("\"\'\"", ProcessFactory.escapeArgument("\"\'\""));
                });

                runner.test("with quoted double quotes", (Test test) ->
                {
                    test.assertEqual("\"\\\"\\\"\"", ProcessFactory.escapeArgument("\"\"\"\""));
                });

                runner.test("with quoted tab", (Test test) ->
                {
                    test.assertEqual("\"\t\"", ProcessFactory.escapeArgument("\"\t\""));
                });

                runner.test("with quoted carriage return", (Test test) ->
                {
                    test.assertEqual("\"\r\"", ProcessFactory.escapeArgument("\"\r\""));
                });

                runner.test("with quoted newline", (Test test) ->
                {
                    test.assertEqual("\"\n\"", ProcessFactory.escapeArgument("\"\n\""));
                });
            });
        });
    }
}