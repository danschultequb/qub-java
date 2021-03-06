package qub;

public interface ProjectSignatureTests
{
    static void test(TestRunner runner)
    {
        PreCondition.assertNotNull(runner, "runner");

        runner.testGroup(ProjectSignature.class, () ->
        {
            runner.testGroup("create(String,String,String)", () ->
            {
                runner.test("with null publisher", (Test test) ->
                {
                    test.assertThrows(() -> ProjectSignature.create(null, "b", "c"),
                        new PreConditionFailure("publisher cannot be null."));
                });

                runner.test("with empty publisher", (Test test) ->
                {
                    test.assertThrows(() -> ProjectSignature.create("", "b", "c"),
                        new PreConditionFailure("publisher cannot be empty."));
                });

                runner.test("with null project", (Test test) ->
                {
                    test.assertThrows(() -> ProjectSignature.create("a", null, "c"),
                        new PreConditionFailure("project cannot be null."));
                });

                runner.test("with empty project", (Test test) ->
                {
                    test.assertThrows(() -> ProjectSignature.create("a", "", "c"),
                        new PreConditionFailure("project cannot be empty."));
                });

                runner.test("with null version", (Test test) ->
                {
                    test.assertThrows(() -> ProjectSignature.create("a", "b", (String)null),
                        new PreConditionFailure("version cannot be null."));
                });

                runner.test("with empty version", (Test test) ->
                {
                    test.assertThrows(() -> ProjectSignature.create("a", "b", ""),
                        new PreConditionFailure("version cannot be empty."));
                });

                runner.test("with all non-empty values", (Test test) ->
                {
                    final ProjectSignature signature = ProjectSignature.create("a", "b", "c");
                    test.assertEqual("a", signature.getPublisher());
                    test.assertEqual("b", signature.getProject());
                    test.assertEqual(VersionNumber.create().setSuffix("c"), signature.getVersion());
                    test.assertEqual("a/b@c", signature.toString());
                });
            });

            runner.testGroup("equals(Object)", () ->
            {
                final Action3<ProjectSignature,Object,Boolean> equalsTest = (ProjectSignature signature, Object rhs, Boolean expected) ->
                {
                    runner.test("with " + signature + " and " + rhs, (Test test) ->
                    {
                        test.assertEqual(expected, signature.equals(rhs));
                    });
                };

                equalsTest.run(ProjectSignature.create("a", "b", "c"), null, false);
                equalsTest.run(ProjectSignature.create("a", "b", "c"), "hello", false);
                equalsTest.run(ProjectSignature.create("a", "b", "c"), ProjectSignature.create("z", "b", "c"), false);
                equalsTest.run(ProjectSignature.create("a", "b", "c"), ProjectSignature.create("a", "y", "c"), false);
                equalsTest.run(ProjectSignature.create("a", "b", "c"), ProjectSignature.create("a", "b", "x"), false);
                equalsTest.run(ProjectSignature.create("a", "b", "c"), ProjectSignature.create("A", "B", "C"), false);
                equalsTest.run(ProjectSignature.create("a", "b", "c"), ProjectSignature.create("a", "b", "c"), true);
            });

            runner.testGroup("equals(QubProjectSignature)", () ->
            {
                final Action3<ProjectSignature,ProjectSignature,Boolean> equalsTest = (ProjectSignature signature, ProjectSignature rhs, Boolean expected) ->
                {
                    runner.test("with " + signature + " and " + rhs, (Test test) ->
                    {
                        test.assertEqual(expected, signature.equals(rhs));
                    });
                };

                equalsTest.run(ProjectSignature.create("a", "b", "c"), null, false);
                equalsTest.run(ProjectSignature.create("a", "b", "c"), ProjectSignature.create("z", "b", "c"), false);
                equalsTest.run(ProjectSignature.create("a", "b", "c"), ProjectSignature.create("a", "y", "c"), false);
                equalsTest.run(ProjectSignature.create("a", "b", "c"), ProjectSignature.create("a", "b", "x"), false);
                equalsTest.run(ProjectSignature.create("a", "b", "c"), ProjectSignature.create("A", "B", "C"), false);
                equalsTest.run(ProjectSignature.create("a", "b", "c"), ProjectSignature.create("a", "b", "c"), true);
            });

            runner.testGroup("equalsIgnoreVersion(QubProjectSignature)", () ->
            {
                final Action3<ProjectSignature,ProjectSignature,Boolean> equalsTest = (ProjectSignature signature, ProjectSignature rhs, Boolean expected) ->
                {
                    runner.test("with " + signature + " and " + rhs, (Test test) ->
                    {
                        test.assertEqual(expected, signature.equalsIgnoreVersion(rhs));
                    });
                };

                equalsTest.run(ProjectSignature.create("a", "b", "c"), null, false);
                equalsTest.run(ProjectSignature.create("a", "b", "c"), ProjectSignature.create("z", "b", "c"), false);
                equalsTest.run(ProjectSignature.create("a", "b", "c"), ProjectSignature.create("a", "y", "c"), false);
                equalsTest.run(ProjectSignature.create("a", "b", "c"), ProjectSignature.create("a", "b", "x"), true);
                equalsTest.run(ProjectSignature.create("a", "b", "c"), ProjectSignature.create("A", "B", "C"), false);
                equalsTest.run(ProjectSignature.create("a", "b", "c"), ProjectSignature.create("a", "b", "c"), true);
            });

            runner.testGroup("parse(String)", () ->
            {
                final Action2<String,Throwable> parseErrorTest = (String text, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        test.assertThrows(() -> ProjectSignature.parse(text).await(),
                            expected);
                    });
                };

                parseErrorTest.run(null, new PreConditionFailure("text cannot be null."));
                parseErrorTest.run("", new PreConditionFailure("text cannot be empty."));
                parseErrorTest.run("a", new ParseException("No project found in \"a\"."));
                parseErrorTest.run("/", new ParseException("No publisher found in \"/\"."));
                parseErrorTest.run("a/", new ParseException("No project found in \"a/\"."));
                parseErrorTest.run("a/b", new ParseException("No version found in \"a/b\"."));
                parseErrorTest.run("a/@", new ParseException("No project found in \"a/@\"."));
                parseErrorTest.run("a/b@", new ParseException("No version found in \"a/b@\"."));

                final Action2<String,ProjectSignature> parseTest = (String text, ProjectSignature expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(text), (Test test) ->
                    {
                        test.assertEqual(expected, ProjectSignature.parse(text).await());
                    });
                };

                parseTest.run("a/b@c", ProjectSignature.create("a", "b", "c"));
                parseTest.run("qub/qub-java@123", ProjectSignature.create("qub", "qub-java", "123"));
            });
        });
    }
}
