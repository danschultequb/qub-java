package qub;

public interface BasicTestRunnerTests
{
    static void test(TestRunner runner)
    {
        runner.testGroup(BasicTestRunner.class, () ->
        {
            runner.testGroup("create(DesktopProcess,PathPattern)", () ->
            {
                runner.test("with null process", (Test test) ->
                {
                    test.assertThrows(() -> BasicTestRunner.create(null, null),
                        new PreConditionFailure("process cannot be null."));
                });
            });

            runner.test("skip()", (Test test) ->
            {
                try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                {
                    final BasicTestRunner btr = BasicTestRunner.create(process, null);
                    final Skip skip = btr.skip();
                    test.assertNotNull(skip);
                    test.assertSame(skip, btr.skip());
                }
            });

            runner.testGroup("skip(boolean)", () ->
            {
                runner.test("with false", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final BasicTestRunner btr = BasicTestRunner.create(process, null);
                        test.assertNull(btr.skip(false));
                    }
                });

                runner.test("with true", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final BasicTestRunner btr = BasicTestRunner.create(process, null);
                        final Skip skip = btr.skip(true);
                        test.assertNotNull(skip);
                        test.assertSame(skip, btr.skip());
                        test.assertSame(skip, btr.skip(true));
                    }
                });
            });

            runner.testGroup("skip(boolean,String)", () ->
            {
                runner.test("with false and null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final BasicTestRunner btr = BasicTestRunner.create(process, null);
                        test.assertThrows(() -> btr.skip(false, null),
                            new PreConditionFailure("message cannot be null."));
                    }
                });

                runner.test("with false and empty", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final BasicTestRunner btr = BasicTestRunner.create(process, null);
                        test.assertThrows(() -> btr.skip(false, ""),
                            new PreConditionFailure("message cannot be empty."));
                    }
                });

                runner.test("with false and non-empty", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final BasicTestRunner btr = BasicTestRunner.create(process, null);
                        test.assertNull(btr.skip(false, "abc"));
                    }
                });

                runner.test("with true and null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final BasicTestRunner btr = BasicTestRunner.create(process, null);
                        test.assertThrows(() -> btr.skip(true, null),
                            new PreConditionFailure("message cannot be null."));
                    }
                });

                runner.test("with true and empty", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final BasicTestRunner btr = BasicTestRunner.create(process, null);
                        test.assertThrows(() -> btr.skip(true, ""),
                            new PreConditionFailure("message cannot be empty."));
                    }
                });

                runner.test("with true and non-empty", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final BasicTestRunner btr = BasicTestRunner.create(process, null);
                        final Skip skip = btr.skip(true, "abc");
                        test.assertNotNull(skip);
                        test.assertEqual("abc", skip.getMessage());
                    }
                });
            });

            runner.testGroup("skip(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final BasicTestRunner btr = BasicTestRunner.create(process, null);
                        test.assertThrows(() -> btr.skip(null),
                            new PreConditionFailure("message cannot be null."));
                    }
                });

                runner.test("with empty", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final BasicTestRunner btr = BasicTestRunner.create(process, null);
                        test.assertThrows(() -> btr.skip(""),
                            new PreConditionFailure("message cannot be empty."));
                    }
                });

                runner.test("with non-empty", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final BasicTestRunner btr = BasicTestRunner.create(process, null);
                        final Skip skip = btr.skip("abc");
                        test.assertNotNull(skip);
                        test.assertEqual("abc", skip.getMessage());
                    }
                });
            });

            runner.testGroup("testGroup(Class<?>,Action0)", () ->
            {
                runner.test("with null class and null action", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final BasicTestRunner btr = BasicTestRunner.create(process, null);
                        test.assertThrows(() -> btr.testGroup((Class<?>)null, null),
                            new PreConditionFailure("testClass cannot be null."));
                    }
                });

                runner.test("with null class and non-null action", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final BasicTestRunner btr = BasicTestRunner.create(process, null);
                        test.assertThrows(() -> btr.testGroup((Class<?>)null, Action0.empty),
                            new PreConditionFailure("testClass cannot be null."));
                    }
                });

                runner.test("with non-null class and null action", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final BasicTestRunner btr = BasicTestRunner.create(process, null);
                        test.assertThrows(() -> btr.testGroup(BasicTestRunnerTests.class, null),
                            new PreConditionFailure("testGroupAction cannot be null."));
                    }
                });

                runner.test("with no event actions", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final Value<Integer> value = Value.create(0);
                        final BasicTestRunner btr = BasicTestRunner.create(process, null);
                        btr.testGroup(BasicTestRunnerTests.class, () -> value.set(value.get() + 1));
                        test.assertEqual(1, value.get());
                    }
                });

                runner.test("with event actions", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final BasicTestRunner btr = BasicTestRunner.create(process, null);
                        final IntegerValue counter = new IntegerValue(0);
                        final Value<Integer> value = Value.create(0);
                        final MutableMap<Integer, TestGroup> beforeTestGroup = Map.create();
                        final MutableMap<Integer, TestGroup> afterTestGroupSkipped = Map.create();
                        final MutableMap<Integer, TestError> afterTestGroupError = Map.create();
                        final MutableMap<Integer, TestGroup> afterTestGroup = Map.create();
                        btr.beforeTestGroup((TestGroup tg) ->
                        {
                            beforeTestGroup.set(counter.incrementAndGetAsInt(), tg);
                        });
                        btr.afterTestGroupSkipped((TestGroup tg) ->
                        {
                            afterTestGroupSkipped.set(counter.incrementAndGetAsInt(), tg);
                        });
                        btr.afterTestGroupFailure((TestGroup tg, TestError error) ->
                        {
                            afterTestGroupError.set(counter.incrementAndGetAsInt(), new TestError(tg.getFullName(), "blah", error));
                        });
                        btr.afterTestGroup((TestGroup tg) ->
                        {
                            afterTestGroup.set(counter.incrementAndGetAsInt(), tg);
                        });

                        btr.testGroup(BasicTestRunnerTests.class, () -> value.set(counter.incrementAndGet()));

                        test.assertEqual(
                            Iterable.create(MapEntry.create(1, TestGroup.create("BasicTestRunnerTests", null, null))),
                            beforeTestGroup);
                        test.assertEqual(2, value.get());
                        test.assertEqual(
                            Iterable.create(),
                            afterTestGroupSkipped);
                        test.assertEqual(
                            Iterable.create(),
                            afterTestGroupError);
                        test.assertEqual(
                            Iterable.create(MapEntry.create(3, TestGroup.create("BasicTestRunnerTests", null, null))),
                            afterTestGroup);
                    }
                });

                runner.test("with error during beforeTestGroup actions", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final BasicTestRunner btr = BasicTestRunner.create(process, null);
                        final IntegerValue counter = new IntegerValue(0);
                        final IntegerValue value = IntegerValue.create(0);
                        final MutableMap<Integer, TestGroup> beforeTestGroup = Map.create();
                        final MutableMap<Integer, TestGroup> afterTestGroupSkipped = Map.create();
                        final MutableMap<Integer, TestError> afterTestGroupError = Map.create();
                        final MutableMap<Integer, TestGroup> afterTestGroup = Map.create();
                        btr.beforeTestGroup((TestGroup tg) ->
                        {
                            throw new RuntimeException("abc");
                        });
                        btr.afterTestGroupSkipped((TestGroup tg) ->
                        {
                            afterTestGroupSkipped.set(counter.incrementAndGetAsInt(), tg);
                        });
                        btr.afterTestGroupFailure((TestGroup tg, TestError error) ->
                        {
                            afterTestGroupError.set(counter.incrementAndGetAsInt(), error);
                        });
                        btr.afterTestGroup((TestGroup tg) ->
                        {
                            afterTestGroup.set(counter.incrementAndGetAsInt(), tg);
                        });

                        btr.testGroup(BasicTestRunnerTests.class, () -> value.set(counter.incrementAndGet()));

                        test.assertEqual(Iterable.create(), beforeTestGroup);
                        test.assertEqual(0, value.get());
                        test.assertEqual(Iterable.create(), afterTestGroupSkipped);
                        test.assertEqual(
                            Map.<Integer, TestError>create().set(1, new TestError("BasicTestRunnerTests", "An unexpected error occurred during \"BasicTestRunnerTests\".", new RuntimeException("abc"))),
                            afterTestGroupError);
                        test.assertEqual(
                            Map.<Integer, TestGroup>create().set(2, TestGroup.create("BasicTestRunnerTests", null, null)),
                            afterTestGroup);
                    }
                });
            });

            runner.testGroup("testGroup(String,Action0)", () ->
            {
                runner.test("with null and null action", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final BasicTestRunner btr = BasicTestRunner.create(process, null);
                        test.assertThrows(() -> btr.testGroup((String)null, null),
                            new PreConditionFailure("testGroupName cannot be null."));
                    }
                });

                runner.test("with empty and non-null action", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final BasicTestRunner btr = BasicTestRunner.create(process, null);
                        test.assertThrows(() -> btr.testGroup("", Action0.empty),
                            new PreConditionFailure("testGroupName cannot be empty."));
                    }
                });

                runner.test("with non-empty and null action", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final BasicTestRunner btr = BasicTestRunner.create(process, null);
                        test.assertThrows(() -> btr.testGroup("abc", null),
                            new PreConditionFailure("testGroupAction cannot be null."));
                    }
                });

                runner.test("with no event actions", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final BasicTestRunner btr = BasicTestRunner.create(process, null);
                        final Value<Integer> value = Value.create(0);
                        btr.testGroup("abc", () -> value.set(value.get() + 1));
                        test.assertEqual(1, value.get());
                    }
                });

                runner.test("with event actions", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final BasicTestRunner btr = BasicTestRunner.create(process, null);
                        final IntegerValue counter = new IntegerValue(0);
                        final Value<Integer> value = Value.create(0);
                        final MutableMap<Integer, TestGroup> beforeTestGroup = Map.create();
                        btr.beforeTestGroup((TestGroup tg) ->
                        {
                            beforeTestGroup.set(counter.incrementAndGetAsInt(), tg);
                        });
                        final MutableMap<Integer, TestGroup> afterTestGroup = Map.create();
                        btr.afterTestGroup((TestGroup tg) ->
                        {
                            afterTestGroup.set(counter.incrementAndGetAsInt(), tg);
                        });

                        btr.testGroup("abc", () -> value.set(counter.incrementAndGetAsInt()));

                        test.assertEqual(
                            Iterable.create(MapEntry.create(1, TestGroup.create("abc", null, null))),
                            beforeTestGroup);
                        test.assertEqual(2, value.get());
                        test.assertEqual(
                            Iterable.create(MapEntry.create(3, TestGroup.create("abc", null, null))),
                            afterTestGroup);
                    }
                });
            });
        });
    }
}
