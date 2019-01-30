package qub;

public class TestTests
{
    public static void test(TestRunner runner)
    {
        runner.testGroup(Test.class, () ->
        {
            runner.testGroup("constructor", () ->
            {
                runner.test("with null name", (Test test) ->
                {
                    test.assertThrows(() -> new Test(null, null, null, test.getProcess()));
                });

                runner.test("with empty name", (Test test) ->
                {
                    test.assertThrows(() -> new Test("", null, null, test.getProcess()));
                });

                runner.test("with null process", (Test test) ->
                {
                    test.assertThrows(() -> new Test("my fake test", null, null, null));
                });

                runner.test("with non-empty name and non-null process", (Test test) ->
                {
                    final Test t = new Test("my fake test", null, null, test.getProcess());
                    test.assertEqual("my fake test", t.getName());
                    test.assertEqual("my fake test", t.getFullName());
                    test.assertNull(t.getParentTestGroup());
                    test.assertFalse(t.shouldSkip());
                    test.assertNull(t.getSkipMessage());
                    test.assertNotNull(t.getMainAsyncRunner());
                    test.assertNotNull(t.getParallelAsyncRunner());
                    test.assertNotNull(t.getNetwork());
                    test.assertNotNull(t.getFileSystem());
                    test.assertNotNull(t.getClock());
                    test.assertNotNull(t.getDisplays());
                });

                runner.test("with non-null test group parent", (Test test) ->
                {
                    final TestGroup tg = new TestGroup("my fake test group", null, null);
                    final Test t = new Test("my fake test", tg, null, test.getProcess());
                    test.assertEqual("my fake test", t.getName());
                    test.assertEqual("my fake test group my fake test", t.getFullName());
                    test.assertSame(tg, t.getParentTestGroup());
                    test.assertFalse(t.shouldSkip());
                    test.assertNull(t.getSkipMessage());
                    test.assertNotNull(t.getMainAsyncRunner());
                    test.assertNotNull(t.getParallelAsyncRunner());
                });

                runner.test("with non-null test group grandparent", (Test test) ->
                {
                    final TestGroup tg1 = new TestGroup("apples", null, null);
                    final TestGroup tg2 = new TestGroup("my fake test group", tg1, null);
                    final Test t = new Test("my fake test", tg2, null, test.getProcess());
                    test.assertEqual("my fake test", t.getName());
                    test.assertEqual("apples my fake test group my fake test", t.getFullName());
                    test.assertSame(tg2, t.getParentTestGroup());
                    test.assertFalse(t.shouldSkip());
                    test.assertNull(t.getSkipMessage());
                    test.assertNotNull(t.getMainAsyncRunner());
                    test.assertNotNull(t.getParallelAsyncRunner());
                });
            });

            runner.testGroup("isMatch()", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertTrue(test.matches(null));
                });

                runner.test("with pattern that doesn't match test name or full name", (Test test) ->
                {
                    final Test t = new Test("apples", null, null, test.getProcess());
                    test.assertFalse(t.matches(PathPattern.parse("bananas")));
                });

                runner.test("with pattern that isMatch test name", (Test test) ->
                {
                    final Test t = new Test("apples", null, null, test.getProcess());
                    test.assertTrue(t.matches(PathPattern.parse("apples")));
                });

                runner.test("with pattern that isMatch test full name", (Test test) ->
                {
                    final TestGroup tg = new TestGroup("apples and", null, null);
                    final Test t = new Test("bananas", tg, null, test.getProcess());
                    test.assertTrue(t.matches(PathPattern.parse("apples*bananas")));
                });
            });

            runner.testGroup("shouldSkip()", () ->
            {
                runner.test("with null skip and null parentTestGroup", (Test test) ->
                {
                    final Test t = new Test("abc", null, null, test.getProcess());
                    test.assertFalse(t.shouldSkip());
                });

                runner.test("with non-null skip with no arguments and null parentTestGroup", (Test test) ->
                {
                    final Test t = new Test("abc", null, runner.skip(), test.getProcess());
                    test.assertTrue(t.shouldSkip());
                });

                runner.test("with non-null skip with false and null parentTestGroup", (Test test) ->
                {
                    final Test t = new Test("abc", null, runner.skip(false), test.getProcess());
                    test.assertFalse(t.shouldSkip());
                });

                runner.test("with non-null skip with false and message and null parentTestGroup", (Test test) ->
                {
                    final Test t = new Test("abc", null, runner.skip(false, "xyz"), test.getProcess());
                    test.assertFalse(t.shouldSkip());
                });

                runner.test("with non-null skip with true and null parentTestGroup", (Test test) ->
                {
                    final Test t = new Test("abc", null, runner.skip(true), test.getProcess());
                    test.assertTrue(t.shouldSkip());
                });

                runner.test("with non-null skip with false and message and null parentTestGroup", (Test test) ->
                {
                    final Test t = new Test("abc", null, runner.skip(true, "xyz"), test.getProcess());
                    test.assertTrue(t.shouldSkip());
                });

                runner.test("with null skip and non-null parentTestGroup with null skip", (Test test) ->
                {
                    final TestGroup tg = new TestGroup("my", null, null);
                    final Test t = new Test("abc", tg, null, test.getProcess());
                    test.assertFalse(t.shouldSkip());
                });

                runner.test("with non-null skip with no arguments and non-null parentTestGroup with null skip", (Test test) ->
                {
                    final TestGroup tg = new TestGroup("my", null, null);
                    final Test t = new Test("abc", tg, runner.skip(), test.getProcess());
                    test.assertTrue(t.shouldSkip());
                });

                runner.test("with non-null skip with false and non-null parentTestGroup with null skip", (Test test) ->
                {
                    final TestGroup tg = new TestGroup("my", null, null);
                    final Test t = new Test("abc", tg, runner.skip(false), test.getProcess());
                    test.assertFalse(t.shouldSkip());
                });

                runner.test("with non-null skip with false and message and non-null parentTestGroup with null skip", (Test test) ->
                {
                    final TestGroup tg = new TestGroup("my", null, null);
                    final Test t = new Test("abc", tg, runner.skip(false, "xyz"), test.getProcess());
                    test.assertFalse(t.shouldSkip());
                });

                runner.test("with non-null skip with true and non-null parentTestGroup with null skip", (Test test) ->
                {
                    final TestGroup tg = new TestGroup("my", null, null);
                    final Test t = new Test("abc", tg, runner.skip(true), test.getProcess());
                    test.assertTrue(t.shouldSkip());
                });

                runner.test("with non-null skip with false and message and non-null parentTestGroup with null skip", (Test test) ->
                {
                    final TestGroup tg = new TestGroup("my", null, null);
                    final Test t = new Test("abc", tg, runner.skip(true, "xyz"), test.getProcess());
                    test.assertTrue(t.shouldSkip());
                });

                runner.test("with non-null skip with false and non-null parentTestGroup with non-null skip with true", (Test test) ->
                {
                    final TestGroup tg = new TestGroup("my", null, runner.skip(true));
                    final Test t = new Test("abc", tg, runner.skip(false), test.getProcess());
                    test.assertTrue(t.shouldSkip());
                });
            });

            runner.testGroup("getSkipMessage()", () ->
            {
                runner.test("with null skip and null parentTestGroup", (Test test) ->
                {
                    final Test t = new Test("abc", null, null, test.getProcess());
                    test.assertEqual(null, t.getSkipMessage());
                });

                runner.test("with non-null skip with no arguments and null parentTestGroup", (Test test) ->
                {
                    final Test t = new Test("abc", null, runner.skip(), test.getProcess());
                    test.assertEqual(null, t.getSkipMessage());
                });

                runner.test("with non-null skip with false and null parentTestGroup", (Test test) ->
                {
                    final Test t = new Test("abc", null, runner.skip(false), test.getProcess());
                    test.assertEqual(null, t.getSkipMessage());
                });

                runner.test("with non-null skip with false and message and null parentTestGroup", (Test test) ->
                {
                    final Test t = new Test("abc", null, runner.skip(false, "xyz"), test.getProcess());
                    test.assertEqual(null, t.getSkipMessage());
                });

                runner.test("with non-null skip with true and null parentTestGroup", (Test test) ->
                {
                    final Test t = new Test("abc", null, runner.skip(true), test.getProcess());
                    test.assertEqual(null, t.getSkipMessage());
                });

                runner.test("with non-null skip with true and message and null parentTestGroup", (Test test) ->
                {
                    final Test t = new Test("abc", null, runner.skip(true, "xyz"), test.getProcess());
                    test.assertEqual("xyz", t.getSkipMessage());
                });
            });

            runner.testGroup("writeLine()", () ->
            {
                runner.test("with null process.getOutputByteWriteStream()", (Test test) ->
                {
                    try (final Process p = new Process())
                    {
                        p.setOutput((ByteWriteStream)null);
                        final Test t = new Test("abc", null, null, p);
                        test.assertThrows(() -> t.writeLine("Hello"));
                    }
                });

                runner.test("with null formattedText", (Test test) ->
                {
                    try (final Process p = new Process())
                    {
                        final InMemoryLineStream stdout = new InMemoryLineStream();
                        p.setOutput(stdout);
                        final Test t = new Test("abc", null, null, p);
                        test.assertThrows(() -> t.writeLine(null));
                    }
                });

                runner.test("with empty formattedText", (Test test) ->
                {
                    try (final Process p = new Process())
                    {
                        final InMemoryLineStream stdout = new InMemoryLineStream();
                        p.setOutput(stdout);
                        final Test t = new Test("abc", null, null, p);
                        test.assertThrows(() -> t.writeLine(""));
                    }
                });

                runner.test("with non-empty formattedText", (Test test) ->
                {
                    try (final Process p = new Process())
                    {
                        final InMemoryLineStream stdout = new InMemoryLineStream();
                        p.setOutput(stdout);
                        final Test t = new Test("abc", null, null, p);
                        t.writeLine("hello");
                        test.assertSuccess("hello\r\n", stdout.getText());
                    }
                });
            });

            runner.testGroup("assertTrue(boolean)", () ->
            {
                runner.test("with false", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertTrue(false),
                        new TestAssertionFailure("abc", new String[]
                            {
                                "Expected: true",
                                "Actual:   false"
                            }));
                });

                runner.test("with true", (Test test) ->
                {
                    test.assertTrue(true);
                });
            });

            runner.testGroup("assertTrue(boolean, String)", () ->
            {
                runner.test("with false and null", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertTrue(false, null),
                        new TestAssertionFailure("abc", new String[]
                                                            {
                                                                "Expected: true",
                                                                "Actual:   false"
                                                            }));
                });

                runner.test("with false and empty", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertTrue(false, ""),
                        new TestAssertionFailure("abc", new String[]
                                                            {
                                                                "Expected: true",
                                                                "Actual:   false"
                                                            }));
                });

                runner.test("with false and non-empty", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertTrue(false, "blah"),
                        new TestAssertionFailure("abc", new String[]
                                                            {
                                                                "Message:  blah",
                                                                "Expected: true",
                                                                "Actual:   false"
                                                            }));
                });

                runner.test("with true and null", (Test test) ->
                {
                    test.assertTrue(true, null);
                });

                runner.test("with true and empty", (Test test) ->
                {
                    test.assertTrue(true, "");
                });

                runner.test("with true and non-empty", (Test test) ->
                {
                    test.assertTrue(true, "blah");
                });
            });

            runner.testGroup("assertFalse(boolean)", () ->
            {
                runner.test("with true", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertFalse(true),
                        new TestAssertionFailure("abc", new String[]
                                                            {
                                                                "Expected: false",
                                                                "Actual:   true"
                                                            }));
                });

                runner.test("with false", (Test test) ->
                {
                    test.assertFalse(false);
                });
            });

            runner.testGroup("assertFalse(boolean, String)", () ->
            {
                runner.test("with true and null", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertFalse(true, (String)null),
                        new TestAssertionFailure("abc", new String[]
                                                            {
                                                                "Expected: false",
                                                                "Actual:   true"
                                                            }));
                });

                runner.test("with true and empty", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertFalse(true, ""),
                        new TestAssertionFailure("abc", new String[]
                                                            {
                                                                "Expected: false",
                                                                "Actual:   true"
                                                            }));
                });

                runner.test("with true and non-empty", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertFalse(true, "blah"),
                        new TestAssertionFailure("abc", new String[]
                                                            {
                                                                "Message:  blah",
                                                                "Expected: false",
                                                                "Actual:   true"
                                                            }));
                });

                runner.test("with false and null", (Test test) ->
                {
                    test.assertFalse(false, (String)null);
                });

                runner.test("with false and empty", (Test test) ->
                {
                    test.assertFalse(false, "");
                });

                runner.test("with false and non-empty", (Test test) ->
                {
                    test.assertFalse(false, "blah");
                });
            });

            runner.testGroup("assertFalse(boolean, Function0<String>)", () ->
            {
                runner.test("with true and null", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertFalse(true, (Function0<String>)null),
                        new TestAssertionFailure("abc", new String[]
                                                            {
                                                                "Expected: false",
                                                                "Actual:   true"
                                                            }));
                });

                runner.test("with true and function that returns null", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertFalse(true, () -> null),
                        new TestAssertionFailure("abc", new String[]
                                                            {
                                                                "Expected: false",
                                                                "Actual:   true"
                                                            }));
                });

                runner.test("with true and function that returns empty", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertFalse(true, () -> ""),
                        new TestAssertionFailure("abc", new String[]
                                                            {
                                                                "Expected: false",
                                                                "Actual:   true"
                                                            }));
                });

                runner.test("with true and function that returns non-empty", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertFalse(true, () -> "blah"),
                        new TestAssertionFailure("abc", new String[]
                                                            {
                                                                "Message:  blah",
                                                                "Expected: false",
                                                                "Actual:   true"
                                                            }));
                });

                runner.test("with false and null", (Test test) ->
                {
                    test.assertFalse(false, (Function0<String>)null);
                });

                runner.test("with false and function that returns null", (Test test) ->
                {
                    test.assertFalse(false, () -> null);
                });

                runner.test("with false and empty", (Test test) ->
                {
                    test.assertFalse(false, "");
                });

                runner.test("with false and non-empty", (Test test) ->
                {
                    test.assertFalse(false, "blah");
                });
            });

            runner.testGroup("assertNull(Object)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    test.assertNull(null);
                });

                runner.test("with non-null", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertNull("Hello"),
                        new TestAssertionFailure("abc", new String[]
                                                            {
                                                                "Expected: null",
                                                                "Actual:   \"Hello\""
                                                            }));
                });
            });

            runner.testGroup("assertNull(Object,String)", () ->
            {
                runner.test("with null and null message", (Test test) ->
                {
                    test.assertNull(null, null);
                });

                runner.test("with null and empty message", (Test test) ->
                {
                    test.assertNull(null, "");
                });

                runner.test("with null and non-empty message", (Test test) ->
                {
                    test.assertNull(null, "blah");
                });

                runner.test("with non-null and null message", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertNull("Hello", null),
                        new TestAssertionFailure("abc", new String[]
                                                            {
                                                                "Expected: null",
                                                                "Actual:   \"Hello\""
                                                            }));
                });

                runner.test("with non-null and empty message", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertNull("Hello", ""),
                        new TestAssertionFailure("abc", new String[]
                                                            {
                                                                "Expected: null",
                                                                "Actual:   \"Hello\""
                                                            }));
                });

                runner.test("with non-null and non-empty message", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertNull("Hello", "blah"),
                        new TestAssertionFailure("abc", new String[]
                                                            {
                                                                "Message:  blah",
                                                                "Expected: null",
                                                                "Actual:   \"Hello\""
                                                            }));
                });
            });

            runner.testGroup("assertNotNull(Object)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertNotNull(null),
                        new TestAssertionFailure("abc", new String[]
                                                            {
                                                                "Expected: \"not null\"",
                                                                "Actual:   null"
                                                            }));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    t.assertNotNull("Hello");
                });
            });

            runner.testGroup("assertNotNull(Object,String)", () ->
            {
                runner.test("with null and null message", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertNotNull(null),
                        new TestAssertionFailure("abc", new String[]
                                                            {
                                                                "Expected: \"not null\"",
                                                                "Actual:   null"
                                                            }));
                });

                runner.test("with null and empty message", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertNotNull(null, ""),
                        new TestAssertionFailure("abc", new String[]
                                                            {
                                                                "Expected: \"not null\"",
                                                                "Actual:   null"
                                                            }));
                });

                runner.test("with null and non-empty message", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertNotNull(null, "blah"),
                        new TestAssertionFailure("abc", new String[]
                                                            {
                                                                "Message:  blah",
                                                                "Expected: \"not null\"",
                                                                "Actual:   null"
                                                            }));
                });

                runner.test("with non-null and null message", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    t.assertNotNull("Hello", null);
                });

                runner.test("with non-null and empty message", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    t.assertNotNull("Hello", "");
                });

                runner.test("with non-null and non-empty message", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    t.assertNotNull("Hello", "blah");
                });
            });

            runner.testGroup("assertNotNullAndNotEmpty(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertNotNullAndNotEmpty((String)null),
                        new TestAssertionFailure("abc", new String[]
                                                            {
                                                                "Expected: \"not null and not empty\"",
                                                                "Actual:   null"
                                                            }));
                });

                runner.test("with empty", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertNotNullAndNotEmpty(""),
                        new TestAssertionFailure("abc", Iterable.create("Expected: \"not null and not empty\"", "Actual:   \"\"")));
                });

                runner.test("with non-empty", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    t.assertNotNullAndNotEmpty("Hello");
                });
            });

            runner.testGroup("assertSuccess(Result<T>)", () ->
            {
                runner.test("with null Result", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertSuccess(null),
                        new TestAssertionFailure("abc", new String[]
                        {
                            "Expected: \"not null\"",
                            "Actual:   null"
                        }));
                });

                runner.test("with error Result", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertSuccess(Result.error(new RuntimeException("xyz"))),
                        new TestAssertionFailure("abc", new String[]
                                                            {
                                                                "Message:  java.lang.RuntimeException: xyz",
                                                                "Expected: false",
                                                                "Actual:   true"
                                                            }));
                });

                runner.test("with null action", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertSuccess(Result.success("hello"), (Action1<String>)null));
                });
            });

            runner.testGroup("assertSuccess(Result<T>,Action1<T>)", () ->
            {
                runner.test("with null Result", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    final Value<Integer> value = Value.create();
                    test.assertThrows(() -> t.assertSuccess(null, value::set),
                        new TestAssertionFailure("abc", Iterable.create("Expected: \"not null\"", "Actual:   null")));
                    test.assertFalse(value.hasValue());
                });

                runner.test("with error Result", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    final Value<Integer> value = Value.create();
                    test.assertThrows(() -> t.assertSuccess(Result.error(new Exception("blah")), value::set),
                        new TestAssertionFailure("abc", Iterable.create("Message:  java.lang.Exception: blah", "Expected: false", "Actual:   true")));
                    test.assertFalse(value.hasValue());
                });

                runner.test("with success Result with null value", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    final Value<Integer> value = Value.create();
                    t.assertSuccess(Result.success(null), value::set);
                    test.assertTrue(value.hasValue());
                    test.assertEqual(null, value.get());
                });

                runner.test("with success Result with non-null value", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    final Value<Integer> value = Value.create();
                    t.assertSuccess(Result.success(5), value::set);
                    test.assertTrue(value.hasValue());
                    test.assertEqual(5, value.get());
                });

                runner.test("with success Result with non-null value and Exception in resultAction", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    final Value<Integer> value = Value.create();
                    test.assertThrows(() -> t.assertSuccess(Result.success(5),
                        (Integer resultValue) ->
                        {
                            value.set(resultValue);
                            throw new RuntimeException("blah");
                        }),
                        new TestAssertionFailure("abc", Iterable.create("Message:  java.lang.RuntimeException: blah", "Expected: false", "Actual:   true")));
                    test.assertTrue(value.hasValue());
                    test.assertEqual(5, value.get());
                });

                runner.test("with success Result with non-null value and assertion faliure in resultAction", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    final Value<Integer> value = Value.create();
                    test.assertThrows(() -> t.assertSuccess(Result.success(5),
                        (Integer resultValue) ->
                        {
                            value.set(resultValue);
                            t.fail("whoops!");
                        }),
                        new TestAssertionFailure("abc", Iterable.create("whoops!")));
                    test.assertTrue(value.hasValue());
                    test.assertEqual(5, value.get());
                });
            });

            runner.testGroup("assertSuccess(T,Result<T>)", () ->
            {
                runner.test("with null Result", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    final String expectedValue = "hello";
                    final Result<String> result = null;
                    test.assertThrows(() -> t.assertSuccess(expectedValue, result),
                        new TestAssertionFailure("abc", Iterable.create("Message:  A successful Result should not be null", "Expected: \"not null\"", "Actual:   null")));
                });

                runner.test("with error Result", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    final String expectedValue = "hello";
                    final Result<String> result = Result.error(new RuntimeException("oops"));
                    test.assertThrows(() -> t.assertSuccess(expectedValue, result),
                        new TestAssertionFailure("abc", Iterable.create("Message:  A successful Result should not have an error", "Expected: null", "Actual:   java.lang.RuntimeException: oops")));
                });

                runner.test("with success Result with no value", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    final String expectedValue = "hello";
                    final Result<String> result = Result.success();
                    test.assertThrows(() -> t.assertSuccess(expectedValue, result),
                        new TestAssertionFailure("abc", Iterable.create("Message:  Unexpected successful Result value", "Expected: \"hello\"", "Actual:   null")));
                });

                runner.test("with success Result with different value", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    final String expectedValue = "hello";
                    final Result<String> result = Result.success("abc");
                    test.assertThrows(() -> t.assertSuccess(expectedValue, result),
                        new TestAssertionFailure("abc", Iterable.create("Message:  Unexpected successful Result value", "Expected: \"hello\"", "Actual:   \"abc\"")));
                });

                runner.test("with success Result with equal value", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    final String expectedValue = "hello";
                    final Result<String> result = Result.success("hello");
                    t.assertSuccess(expectedValue, result);
                });
            });

            runner.testGroup("assertEqual(Throwable,Throwable)", () ->
            {
                runner.test("with null and null", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    t.assertEqual((Throwable)null, (Throwable)null);
                });

                runner.test("with null and NullPointerException", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertEqual(null, new NullPointerException("abc")),
                        new TestAssertionFailure("abc", new String[]
                        {
                            "Expected: null",
                            "Actual:   java.lang.NullPointerException: abc"
                        }));
                });

                runner.test("with NullPointerException and null", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertEqual(new NullPointerException("abc"), null),
                        new TestAssertionFailure("abc", new String[]
                        {
                            "Expected: java.lang.NullPointerException: abc",
                            "Actual:   null"
                        }));
                });

                runner.test("with NullPointerException(a) and NullPointerException(b)", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    test.assertThrows(() -> t.assertEqual(new NullPointerException("a"), new NullPointerException("b")),
                        new TestAssertionFailure("abc", new String[]
                        {
                            "Expected: java.lang.NullPointerException: a",
                            "Actual:   java.lang.NullPointerException: b"
                        }));
                });

                runner.test("with NullPointerException(a) and NullPointerException(a)", (Test test) ->
                {
                    final Test t = createTest("abc", test);
                    t.assertEqual(new NullPointerException("a"), new NullPointerException("a"));
                });
            });
        });
    }

    private static Test createTest(String testName, Test test)
    {
        return new Test(testName, null, null, test.getProcess());
    }
}