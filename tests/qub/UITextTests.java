package qub;

public interface UITextTests
{
    static void test(TestRunner runner, Function1<FakeDesktopProcess,? extends UIText> creator)
    {
        PreCondition.assertNotNull(runner, "runner");
        PreCondition.assertNotNull(creator, "creator");

        runner.testGroup(UIText.class, () ->
        {
            runner.testGroup("setWidth(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(UIText.class), (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIText text = creator.run(process);
                        final UIText setWidthResult = text.setWidth(Distance.inches(1));
                        test.assertSame(text, setWidthResult);
                    }
                });
            });

            runner.testGroup("setHeight(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(UIText.class), (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIText text = creator.run(process);
                        final UIText setHeightResult = text.setHeight(Distance.inches(1));
                        test.assertSame(text, setHeightResult);
                    }
                });
            });

            runner.testGroup("setSize(Size2D)", () ->
            {
                runner.test("should return " + Types.getTypeName(UIText.class), (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIText text = creator.run(process);
                        final UIText setHeightResult = text.setSize(Size2D.create(Distance.inches(2), Distance.inches(3)));
                        test.assertSame(text, setHeightResult);
                    }
                });
            });

            runner.testGroup("setSize(Distance,Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(UIText.class), (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIText text = creator.run(process);
                        final UIText setHeightResult = text.setSize(Distance.inches(2), Distance.inches(3));
                        test.assertSame(text, setHeightResult);
                    }
                });
            });

            runner.testGroup("setText(String)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIText text = creator.run(process);

                        test.assertThrows(() -> text.setText(null),
                            new PreConditionFailure("text cannot be null."));

                        test.assertEqual("", text.getText());
                    }
                });

                runner.test("with empty", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIText text = creator.run(process);

                        final UIText setTextResult = text.setText("");
                        test.assertSame(text, setTextResult);

                        test.assertEqual("", text.getText());
                    }
                });

                runner.test("with non-empty", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final UIText text = creator.run(process);

                        final UIText setTextResult = text.setText("hello");
                        test.assertSame(text, setTextResult);

                        test.assertEqual("hello", text.getText());
                    }
                });
            });

            runner.testGroup("setFontSize(Distance)", () ->
            {
                final Action2<Distance,Throwable> setFontSizeErrorTest = (Distance fontSize, Throwable expected) ->
                {
                    runner.test("with " + fontSize, (Test test) ->
                    {
                        try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                        {
                            final UIText text = creator.run(process);
                            test.assertThrows(() -> text.setFontSize(fontSize), expected);
                        }
                    });
                };

                setFontSizeErrorTest.run(null, new PreConditionFailure("fontSize cannot be null."));
                setFontSizeErrorTest.run(Distance.inches(-1), new PreConditionFailure("fontSize (-1.0 Inches) must be greater than or equal to 0.0 Inches."));

                final Action1<Distance> setFontSizeTest = (Distance fontSize) ->
                {
                    runner.test("with " + fontSize, (Test test) ->
                    {
                        try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                        {
                            final UIText text = creator.run(process);
                            final UIText setFontSizeResult = text.setFontSize(fontSize);
                            test.assertSame(text, setFontSizeResult);
                            test.assertEqual(fontSize, text.getFontSize());
                        }
                    });
                };

                setFontSizeTest.run(Distance.zero);
                setFontSizeTest.run(Distance.fontPoints(12));
                setFontSizeTest.run(Distance.inches(1));
            });
        });
    }
}
