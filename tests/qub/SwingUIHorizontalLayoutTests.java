package qub;

public interface SwingUIHorizontalLayoutTests
{
    static AWTUIBase createUIBase(FakeDesktopProcess process)
    {
        return AWTUIBase.create(process.getDisplays().first(), process.getMainAsyncRunner(), process.getParallelAsyncRunner());
    }
    
    static SwingUIHorizontalLayout createUIHorizontalLayout(FakeDesktopProcess process)
    {
        final SwingUIBuilder uiBuilder = SwingUIBuilder.create(AWTUIBase.create(process));
        return uiBuilder.create(SwingUIHorizontalLayout.class).await();
    }
    
    static void test(TestRunner runner)
    {
        runner.testGroup(SwingUIHorizontalLayout.class, () ->
        {
            UIHorizontalLayoutTests.test(runner, SwingUIHorizontalLayoutTests::createUIHorizontalLayout);
            SwingUIElementTests.test(runner, SwingUIHorizontalLayoutTests::createUIHorizontalLayout);

            runner.testGroup("create(SwingUIBase)", () ->
            {
                runner.test("with null uiBase", (Test test) ->
                {
                    test.assertThrows(() -> SwingUIHorizontalLayout.create(null),
                        new PreConditionFailure("uiBase cannot be null."));
                });

                runner.test("with valid arguments", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final AWTUIBase base = SwingUIHorizontalLayoutTests.createUIBase(process);
                        final SwingUIHorizontalLayout horizontalLayout = SwingUIHorizontalLayout.create(base);
                        test.assertNotNull(horizontalLayout);
                        test.assertEqual(Distance.zero, horizontalLayout.getWidth());
                        test.assertEqual(Distance.zero, horizontalLayout.getHeight());
                        test.assertEqual(UIPaddingInPixels.create(), horizontalLayout.getPaddingInPixels());

                        final javax.swing.JPanel jComponent = horizontalLayout.getComponent();
                        test.assertNotNull(jComponent);
                    }
                });
            });

            runner.testGroup("setWidth(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(UIHorizontalLayout.class), (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final SwingUIHorizontalLayout horizontalLayout = SwingUIHorizontalLayoutTests.createUIHorizontalLayout(process);
                        final SwingUIHorizontalLayout setWidthResult = horizontalLayout.setWidth(Distance.inches(1));
                        test.assertSame(horizontalLayout, setWidthResult);
                    }
                });
            });

            runner.testGroup("setWidthInPixels(int)", () ->
            {
                runner.test("should return " + Types.getTypeName(UIHorizontalLayout.class), (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final SwingUIHorizontalLayout horizontalLayout = SwingUIHorizontalLayoutTests.createUIHorizontalLayout(process);
                        final SwingUIHorizontalLayout setWidthInPixelsResult = horizontalLayout.setWidthInPixels(1);
                        test.assertSame(horizontalLayout, setWidthInPixelsResult);
                    }
                });
            });

            runner.testGroup("setHeight(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIHorizontalLayout.class), (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final SwingUIHorizontalLayout horizontalLayout = SwingUIHorizontalLayoutTests.createUIHorizontalLayout(process);
                        final SwingUIHorizontalLayout setHeightResult = horizontalLayout.setHeight(Distance.inches(1));
                        test.assertSame(horizontalLayout, setHeightResult);
                    }
                });
            });

            runner.testGroup("setHeightInPixels(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIHorizontalLayout.class), (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final SwingUIHorizontalLayout horizontalLayout = SwingUIHorizontalLayoutTests.createUIHorizontalLayout(process);
                        final SwingUIHorizontalLayout setHeightInPixelsResult = horizontalLayout.setHeightInPixels(1);
                        test.assertSame(horizontalLayout, setHeightInPixelsResult);
                    }
                });
            });

            runner.testGroup("setSize(Size2D)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIHorizontalLayout.class), (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final SwingUIHorizontalLayout horizontalLayout = SwingUIHorizontalLayoutTests.createUIHorizontalLayout(process);
                        final SwingUIHorizontalLayout setSizeResult = horizontalLayout.setSize(Size2D.create(Distance.inches(2), Distance.inches(3)));
                        test.assertSame(horizontalLayout, setSizeResult);
                    }
                });
            });

            runner.testGroup("setSize(Distance,Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIHorizontalLayout.class), (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final SwingUIHorizontalLayout horizontalLayout = SwingUIHorizontalLayoutTests.createUIHorizontalLayout(process);
                        final SwingUIHorizontalLayout setSizeResult = horizontalLayout.setSize(Distance.inches(2), Distance.inches(3));
                        test.assertSame(horizontalLayout, setSizeResult);
                    }
                });
            });

            runner.testGroup("setSizeInPixels(int,int)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIHorizontalLayout.class), (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final SwingUIHorizontalLayout horizontalLayout = SwingUIHorizontalLayoutTests.createUIHorizontalLayout(process);
                        final SwingUIHorizontalLayout setSizeInPixelsResult = horizontalLayout.setSizeInPixels(2, 3);
                        test.assertSame(horizontalLayout, setSizeInPixelsResult);
                    }
                });
            });

            runner.testGroup("add(UIElement)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIHorizontalLayout.class), (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final SwingUIHorizontalLayout horizontalLayout = SwingUIHorizontalLayoutTests.createUIHorizontalLayout(process);
                        final UIElement element = SwingUIHorizontalLayoutTests.createUIHorizontalLayout(process);

                        final SwingUIHorizontalLayout addResult = horizontalLayout.add(element);
                        test.assertSame(horizontalLayout, addResult);
                    }
                });
            });

            runner.testGroup("addAll(UIElement...)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIHorizontalLayout.class), (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final SwingUIHorizontalLayout horizontalLayout = SwingUIHorizontalLayoutTests.createUIHorizontalLayout(process);

                        final SwingUIHorizontalLayout addAllResult = horizontalLayout.addAll();
                        test.assertSame(horizontalLayout, addAllResult);
                    }
                });
            });

            runner.testGroup("addAll(Iterable<? extends UIElement>)", () ->
            {
                runner.test("should return " + Types.getTypeName(SwingUIHorizontalLayout.class), (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final SwingUIHorizontalLayout horizontalLayout = SwingUIHorizontalLayoutTests.createUIHorizontalLayout(process);

                        final SwingUIHorizontalLayout addResult = horizontalLayout.addAll(Iterable.create());
                        test.assertSame(horizontalLayout, addResult);
                    }
                });
            });

            runner.test("getComponent()", (Test test) ->
            {
                try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                {
                    final SwingUIHorizontalLayout uiElement = SwingUIHorizontalLayoutTests.createUIHorizontalLayout(process);
                    final javax.swing.JPanel component = uiElement.getComponent();
                    test.assertNotNull(component);
                    test.assertSame(component, uiElement.getComponent());
                }
            });

            runner.test("getJComponent()", (Test test) ->
            {
                try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                {
                    final SwingUIHorizontalLayout uiElement = SwingUIHorizontalLayoutTests.createUIHorizontalLayout(process);
                    final javax.swing.JPanel jComponent = uiElement.getJComponent();
                    test.assertNotNull(jComponent);
                    test.assertSame(jComponent, uiElement.getJComponent());
                    test.assertSame(jComponent, uiElement.getComponent());
                }
            });
        });
    }
}
