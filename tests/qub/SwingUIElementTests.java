package qub;

public interface SwingUIElementTests
{
    static void test(TestRunner runner, Function1<FakeDesktopProcess,? extends SwingUIElement> creator)
    {
        PreCondition.assertNotNull(runner, "runner");
        PreCondition.assertNotNull(creator, "creator");

        runner.testGroup(SwingUIElement.class, () ->
        {
            UIElementTests.test(runner, creator);

            runner.testGroup("setWidth(Distance)", () ->
            {
                runner.test("returns SwingUIElement", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final SwingUIElement uiElement = creator.run(process);
                        final SwingUIElement setWidthResult = uiElement.setWidth(Distance.inches(2));
                        test.assertSame(uiElement, setWidthResult);
                    }
                });
            });

            runner.testGroup("setWidthInPixels(int)", () ->
            {
                runner.test("returns SwingUIElement", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final SwingUIElement uiElement = creator.run(process);
                        final SwingUIElement setWidthInPixelsResult = uiElement.setWidthInPixels(2);
                        test.assertSame(uiElement, setWidthInPixelsResult);
                    }
                });
            });

            runner.testGroup("setHeight(Distance)", () ->
            {
                runner.test("returns SwingUIElement", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final SwingUIElement uiElement = creator.run(process);
                        final SwingUIElement setHeightResult = uiElement.setHeight(Distance.inches(3));
                        test.assertSame(uiElement, setHeightResult);
                    }
                });
            });

            runner.testGroup("setHeightInPixels(int)", () ->
            {
                runner.test("returns SwingUIElement", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final SwingUIElement uiElement = creator.run(process);
                        final SwingUIElement setHeightInPixelsResult = uiElement.setHeightInPixels(2);
                        test.assertSame(uiElement, setHeightInPixelsResult);
                    }
                });
            });

            runner.testGroup("setSize(Size2D)", () ->
            {
                runner.test("returns SwingUIElement", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final SwingUIElement uiElement = creator.run(process);
                        final SwingUIElement setWidthResult = uiElement.setSize(Size2D.create(Distance.inches(2), Distance.inches(3)));
                        test.assertSame(uiElement, setWidthResult);
                    }
                });
            });

            runner.testGroup("setSize(Distance,Distance)", () ->
            {
                runner.test("returns SwingUIElement", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final SwingUIElement uiElement = creator.run(process);
                        final SwingUIElement setWidthResult = uiElement.setSize(Distance.inches(2), Distance.inches(3));
                        test.assertSame(uiElement, setWidthResult);
                    }
                });
            });

            runner.testGroup("setPadding(UIPadding)", () ->
            {
                runner.test("returns SwingUIElement", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final SwingUIElement uiElement = creator.run(process);
                        final SwingUIElement setPaddingResult = uiElement.setPadding(UIPadding.zero);
                        test.assertSame(uiElement, setPaddingResult);
                    }
                });
            });

            runner.testGroup("setBackgroundColor(Color)", () ->
            {
                runner.test("returns SwingUIElement", (Test test) ->
                {
                    try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                    {
                        final SwingUIElement uiElement = creator.run(process);
                        final SwingUIElement setBackgroundColorResult = uiElement.setBackgroundColor(Color.blue);
                        test.assertSame(uiElement, setBackgroundColorResult);
                    }
                });
            });

            runner.test("getComponent()", (Test test) ->
            {
                try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                {
                    final SwingUIElement uiElement = creator.run(process);
                    final javax.swing.JComponent component = uiElement.getComponent();
                    test.assertNotNull(component);
                    test.assertSame(component, uiElement.getComponent());
                }
            });

            runner.test("getJComponent()", (Test test) ->
            {
                try (final FakeDesktopProcess process = FakeDesktopProcess.create())
                {
                    final SwingUIElement uiElement = creator.run(process);
                    final javax.swing.JComponent jComponent = uiElement.getJComponent();
                    test.assertNotNull(jComponent);
                    test.assertSame(jComponent, uiElement.getJComponent());
                    test.assertSame(jComponent, uiElement.getComponent());
                }
            });
        });
    }
}
