package qub;

public interface SwingUIWindowTests
{
    static SwingUIBuilder createUIBuilder(Test test)
    {
        PreCondition.assertNotNull(test, "test");
        
        final Display display = new Display(1000, 1000, 100, 100);
        final AsyncRunner mainAsyncRunner = test.getMainAsyncRunner();
        final AsyncScheduler parallelAsyncRunner = test.getParallelAsyncRunner();
        final SwingUIBase uiBase = SwingUIBase.create(display, mainAsyncRunner, parallelAsyncRunner);
        final SwingUIBuilder result = SwingUIBuilder.create(uiBase);
        
        PostCondition.assertNotNull(result, "result");
        
        return result;
    }
    
    static SwingUIWindow createUIWindow(Test test)
    {
        PreCondition.assertNotNull(test, "test");
        
        final SwingUIBuilder uiBuilder = SwingUIWindowTests.createUIBuilder(test);
        final SwingUIWindow result = SwingUIWindowTests.createUIWindow(uiBuilder);
        
        PostCondition.assertNotNull(result, "result");
        
        return result;
    }

    static SwingUIWindow createUIWindow(SwingUIBuilder uiBuilder)
    {
        PreCondition.assertNotNull(uiBuilder, "uiBuilder");

        final SwingUIWindow result = uiBuilder.createSwingUIWindow().await();

        PostCondition.assertNotNull(result, "result");

        return result;
    }
    
    static void test(TestRunner runner)
    {
        runner.testGroup(SwingUIWindow.class, () ->
        {
            runner.testGroup("setTitle(String)", () ->
            {
                final Action2<String,Throwable> setTitleErrorTest = (String title, Throwable expected) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(title), (Test test) ->
                    {
                        try (final SwingUIWindow window = SwingUIWindowTests.createUIWindow(test))
                        {
                            test.assertThrows(() -> window.setTitle(title), expected);
                            test.assertEqual("", window.getTitle());
                        }
                    });
                };

                setTitleErrorTest.run(null, new PreConditionFailure("title cannot be null."));

                final Action1<String> setTitleTest = (String title) ->
                {
                    runner.test("with " + Strings.escapeAndQuote(title), (Test test) ->
                    {
                        try (final SwingUIWindow window = SwingUIWindowTests.createUIWindow(test))
                        {
                            final SwingUIWindow setTitleResult = window.setTitle(title);
                            test.assertSame(window, setTitleResult);
                            test.assertEqual(title, window.getTitle());
                        }
                    });
                };

                setTitleTest.run("");
                setTitleTest.run("a");
                setTitleTest.run("hello world");
            });

            runner.testGroup("setVisible(boolean)", () ->
            {
                runner.test("with false when not visible", (Test test) ->
                {
                    try (final SwingUIWindow window = SwingUIWindowTests.createUIWindow(test))
                    {
                        final SwingUIWindow setVisibleResult = window.setVisible(false);
                        test.assertSame(window, setVisibleResult);
                        test.assertFalse(window.isVisible());
                    }
                });

                runner.test("with true when not visible", (Test test) ->
                {
                    try (final SwingUIWindow window = SwingUIWindowTests.createUIWindow(test))
                    {
                        final SwingUIWindow setVisibleResult = window.setVisible(true);
                        test.assertSame(window, setVisibleResult);
                        test.assertTrue(window.isVisible());
                    }
                });

                runner.test("with false when visible", (Test test) ->
                {
                    try (final SwingUIWindow window = SwingUIWindowTests.createUIWindow(test))
                    {
                        final SwingUIWindow setVisibleResult = window.setVisible(false);
                        test.assertSame(window, setVisibleResult);
                        test.assertFalse(window.isVisible());
                    }
                });

                runner.test("with true when visible", (Test test) ->
                {
                    try (final SwingUIWindow window = SwingUIWindowTests.createUIWindow(test))
                    {
                        final SwingUIWindow setVisibleResult = window.setVisible(true);
                        test.assertSame(window, setVisibleResult);
                        test.assertTrue(window.isVisible());
                    }
                });

                runner.test("with false when disposed", (Test test) ->
                {
                    try (final SwingUIWindow window = SwingUIWindowTests.createUIWindow(test))
                    {
                        window.dispose().await();

                        test.assertThrows(() -> window.setVisible(false),
                                new PreConditionFailure("this.isDisposed() cannot be true."));
                    }
                });

                runner.test("with true when disposed", (Test test) ->
                {
                    try (final SwingUIWindow window = SwingUIWindowTests.createUIWindow(test))
                    {
                        window.dispose().await();

                        test.assertThrows(() -> window.setVisible(true),
                                new PreConditionFailure("this.isDisposed() cannot be true."));
                    }
                });
            });

            runner.test("dispose()", (Test test) ->
            {
                try (final SwingUIWindow window = SwingUIWindowTests.createUIWindow(test))
                {
                    test.assertFalse(window.isDisposed());

                    test.assertTrue(window.dispose().await());
                    test.assertTrue(window.isDisposed());

                    test.assertFalse(window.dispose().await());
                    test.assertTrue(window.isDisposed());
                }
            });

            runner.testGroup("await()", () ->
            {
                runner.test("when not visible", (Test test) ->
                {
                    try (final SwingUIWindow window = SwingUIWindowTests.createUIWindow(test))
                    {
                        test.assertThrows(() -> window.await(),
                                new PreConditionFailure("this.isVisible() cannot be false."));
                        test.assertFalse(window.isVisible());
                        test.assertFalse(window.isDisposed());
                    }
                });

                runner.test("when visible", runner.skip(), (Test test) ->
                {
                    final SwingUIBuilder uiBuilder = SwingUIBuilder.create(test.getProcess());
                    try (final SwingUIWindow window = uiBuilder.createSwingUIWindow().await())
                    {
                        window.setTitle(test.getFullName());
                        window.setSize(Distance.inches(4), Distance.inches(4));

                        final UITextBox textBox = uiBuilder.createUITextBox().await()
                            .setFontSize(Distance.inches(0.5))
                            .setSize(Distance.inches(2), Distance.inches(0.75));

                        final UIText text = uiBuilder.createUIText().await()
                            .setFontSize(Distance.inches(0.5));

                        textBox.onTextChanged(text::setText);

                        window.setContent(uiBuilder.createUIVerticalLayout().await()
                            .add(textBox)
                            .add(text));

                        window.setVisible(true);

                        window.await();

                        test.assertFalse(window.isVisible());
                        test.assertTrue(window.isDisposed());
                    }
                    test.fail();
                });
            });

            runner.testGroup("setContent(UIElement)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final SwingUIWindow window = SwingUIWindowTests.createUIWindow(test))
                    {
                        test.assertThrows(() -> window.setContent((UIElement)null),
                                new PreConditionFailure("content cannot be null."));
                        test.assertNull(window.getContent());
                    }
                });

                runner.test("with non-null when no content exists", (Test test) ->
                {
                    final SwingUIBuilder uiBuilder = SwingUIWindowTests.createUIBuilder(test);
                    try (final SwingUIWindow window = SwingUIWindowTests.createUIWindow(uiBuilder))
                    {
                        final SwingUIButton content = uiBuilder.create(SwingUIButton.class).await()
                            .setText("Hello World!");
                        final SwingUIWindow setContentResult = window.setContent((UIElement)content);
                        test.assertSame(window, setContentResult);
                        test.assertSame(content, window.getContent());
                    }
                });

                runner.test("with non-null when content exists", (Test test) ->
                {
                    final SwingUIBuilder uiBuilder = SwingUIWindowTests.createUIBuilder(test);
                    try (final SwingUIWindow window = SwingUIWindowTests.createUIWindow(uiBuilder))
                    {
                        final SwingUIButton content1 = uiBuilder.create(SwingUIButton.class).await()
                            .setText("First Content");
                        window.setContent((UIElement)content1);
                        test.assertSame(content1, window.getContent());

                        final SwingUIButton content2 = uiBuilder.create(SwingUIButton.class).await()
                            .setText("Hello World!");
                        final SwingUIWindow setContentResult = window.setContent((UIElement)content2);
                        test.assertSame(window, setContentResult);
                        test.assertSame(content2, window.getContent());
                    }
                });
            });

            runner.testGroup("setContent(AWTUIElement)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    try (final SwingUIWindow window = SwingUIWindowTests.createUIWindow(test))
                    {
                        test.assertThrows(() -> window.setContent((AWTUIElement)null),
                                new PreConditionFailure("content cannot be null."));
                        test.assertNull(window.getContent());
                    }
                });

                runner.test("with non-null when no content exists", (Test test) ->
                {
                    final SwingUIBuilder uiBuilder = SwingUIWindowTests.createUIBuilder(test);
                    try (final SwingUIWindow window = SwingUIWindowTests.createUIWindow(uiBuilder))
                    {
                        final SwingUIButton content = uiBuilder.create(SwingUIButton.class).await()
                            .setText("Hello World!");
                        final SwingUIWindow setContentResult = window.setContent((AWTUIElement)content);
                        test.assertSame(window, setContentResult);
                        test.assertSame(content, window.getContent());
                    }
                });

                runner.test("with non-null when content exists", (Test test) ->
                {
                    final SwingUIBuilder uiBuilder = SwingUIWindowTests.createUIBuilder(test);
                    try (final SwingUIWindow window = SwingUIWindowTests.createUIWindow(uiBuilder))
                    {
                        final SwingUIButton content1 = uiBuilder.create(SwingUIButton.class).await()
                            .setText("First Content");
                        window.setContent((AWTUIElement)content1);
                        test.assertSame(content1, window.getContent());

                        final SwingUIButton content2 = uiBuilder.create(SwingUIButton.class).await()
                            .setText("Hello World!");
                        final SwingUIWindow setContentResult = window.setContent((AWTUIElement)content2);
                        test.assertSame(window, setContentResult);
                        test.assertSame(content2, window.getContent());
                    }
                });
            });

            runner.testGroup("setWidth(Distance)", () ->
            {
                final Action2<Distance,Throwable> setWidthErrorTest = (Distance width, Throwable expected) ->
                {
                    runner.test("with " + width, (Test test) ->
                    {
                        try (final SwingUIWindow window = SwingUIWindowTests.createUIWindow(test))
                        {
                            test.assertThrows(() -> window.setWidth(width), expected);
                        }
                    });
                };

                setWidthErrorTest.run(null, new PreConditionFailure("width cannot be null."));
                setWidthErrorTest.run(Distance.inches(-1), new PreConditionFailure("width (-1.0 Inches) must be greater than or equal to 0.0 Inches."));

                final Action2<Distance,Iterable<Distance>> setWidthTest = (Distance width, Iterable<Distance> expectedWidths) ->
                {
                    runner.test("with " + width, (Test test) ->
                    {
                        try (final SwingUIWindow window = SwingUIWindowTests.createUIWindow(test))
                        {
                            final SwingUIWindow setWidthResult = window.setWidth(width);
                            test.assertSame(window, setWidthResult);
                            test.assertOneOf(expectedWidths, window.getWidth());
                        }
                    });
                };

                setWidthTest.run(
                    Distance.zero,
                    Iterable.create(Distance.inches(1.98), Distance.inches(2.58)));
                setWidthTest.run(
                    Distance.inches(1),
                    Iterable.create(Distance.inches(1.98), Distance.inches(2.58)));
                setWidthTest.run(
                    Distance.feet(1),
                    Iterable.create(Distance.feet(1)));
            });

            runner.testGroup("setHeight(Distance)", () ->
            {
                final Action2<Distance,Throwable> setHeightErrorTest = (Distance height, Throwable expected) ->
                {
                    runner.test("with " + height, (Test test) ->
                    {
                        try (final SwingUIWindow window = SwingUIWindowTests.createUIWindow(test))
                        {
                            test.assertThrows(() -> window.setHeight(height), expected);
                        }
                    });
                };

                setHeightErrorTest.run(null, new PreConditionFailure("height cannot be null."));
                setHeightErrorTest.run(Distance.inches(-1), new PreConditionFailure("height (-1.0 Inches) must be greater than or equal to 0.0 Inches."));

                final Action2<Distance,Iterable<Distance>> setHeightTest = (Distance height, Iterable<Distance> expectedHeights) ->
                {
                    runner.test("with " + height, (Test test) ->
                    {
                        try (final SwingUIWindow window = SwingUIWindowTests.createUIWindow(test))
                        {
                            final SwingUIWindow setHeightResult = window.setHeight(height);
                            test.assertSame(window, setHeightResult);
                            test.assertOneOf(expectedHeights, window.getHeight());
                        }
                    });
                };

                setHeightTest.run(
                    Distance.zero,
                    Iterable.create(Distance.inches(0.56), Distance.inches(0.71)));
                setHeightTest.run(
                    Distance.inches(1),
                    Iterable.create(Distance.inches(1)));
                setHeightTest.run(
                    Distance.feet(1),
                    Iterable.create(Distance.inches(7.35), Distance.inches(9.77)));
            });

            runner.testGroup("setSize(Size2D)", () ->
            {
                final Action2<Size2D,Throwable> setSizeErrorTest = (Size2D size, Throwable expected) ->
                {
                    runner.test("with " + size, (Test test) ->
                    {
                        try (final SwingUIWindow window = SwingUIWindowTests.createUIWindow(test))
                        {
                            test.assertThrows(() -> window.setSize(size), expected);
                        }
                    });
                };

                setSizeErrorTest.run(null, new PreConditionFailure("size cannot be null."));

                final Action2<Size2D,Iterable<Size2D>> setSizeTest = (Size2D size, Iterable<Size2D> expectedSizes) ->
                {
                    runner.test("with " + size, (Test test) ->
                    {
                        try (final SwingUIWindow window = SwingUIWindowTests.createUIWindow(test))
                        {
                            final SwingUIWindow setSizeResult = window.setSize(size);
                            test.assertSame(window, setSizeResult);
                            test.assertOneOf(expectedSizes, window.getSize());
                        }
                    });
                };

                setSizeTest.run(
                    Size2D.create(Distance.zero, Distance.zero),
                    Iterable.create(
                        Size2D.create(Distance.inches(1.98), Distance.inches(0.56)),
                        Size2D.create(Distance.inches(2.58), Distance.inches(0.71))));
                setSizeTest.run(
                    Size2D.create(Distance.zero, Distance.inches(1)),
                    Iterable.create(
                        Size2D.create(Distance.inches(1.98), Distance.inches(1)),
                        Size2D.create(Distance.inches(2.58), Distance.inches(1))));
                setSizeTest.run(Size2D.create(
                    Distance.inches(1), Distance.zero),
                    Iterable.create(
                        Size2D.create(Distance.inches(1.98), Distance.inches(0.56)),
                        Size2D.create(Distance.inches(2.58), Distance.inches(0.71))));
                setSizeTest.run(
                    Size2D.create(Distance.inches(2), Distance.inches(3)),
                    Iterable.create(
                        Size2D.create(Distance.inches(2), Distance.inches(3)),
                        Size2D.create(Distance.inches(2.58), Distance.inches(3))));
                setSizeTest.run(
                    Size2D.create(Distance.inches(3), Distance.inches(4)),
                    Iterable.create(
                        Size2D.create(Distance.inches(3), Distance.inches(4))));
            });

            runner.testGroup("setSize(Distance,Distance)", () ->
            {
                final Action3<Distance,Distance,Throwable> setSizeErrorTest = (Distance width, Distance height, Throwable expected) ->
                {
                    runner.test("with " + English.andList(width, height), (Test test) ->
                    {
                        try (final SwingUIWindow window = SwingUIWindowTests.createUIWindow(test))
                        {
                            test.assertThrows(() -> window.setSize(width, height), expected);
                        }
                    });
                };

                setSizeErrorTest.run(null, Distance.inches(1), new PreConditionFailure("width cannot be null."));
                setSizeErrorTest.run(Distance.inches(-1), Distance.inches(1), new PreConditionFailure("width (-1.0 Inches) must be greater than or equal to 0.0 Inches."));
                setSizeErrorTest.run(Distance.zero, null, new PreConditionFailure("height cannot be null."));
                setSizeErrorTest.run(Distance.zero, Distance.inches(-1), new PreConditionFailure("height (-1.0 Inches) must be greater than or equal to 0.0 Inches."));

                final Action4<Distance,Distance,Iterable<Distance>,Iterable<Distance>> setSizeTest = (Distance width, Distance height, Iterable<Distance> expectedWidths, Iterable<Distance> expectedHeights) ->
                {
                    runner.test("with " + English.andList(width, height), (Test test) ->
                    {
                        try (final SwingUIWindow window = SwingUIWindowTests.createUIWindow(test))
                        {
                            final SwingUIWindow setHeightResult = window.setSize(width, height);
                            test.assertSame(window, setHeightResult);
                            test.assertOneOf(expectedWidths, window.getWidth());
                            test.assertOneOf(expectedHeights, window.getHeight());
                        }
                    });
                };

                setSizeTest.run(
                    Distance.zero,
                    Distance.zero,
                    Iterable.create(Distance.inches(1.98), Distance.inches(2.58)),
                    Iterable.create(Distance.inches(0.56), Distance.inches(0.71)));
                setSizeTest.run(
                    Distance.zero,
                    Distance.inches(2),
                    Iterable.create(Distance.inches(1.98), Distance.inches(2.58)),
                    Iterable.create(Distance.inches(2)));
                setSizeTest.run(
                    Distance.inches(1.5),
                    Distance.zero,
                    Iterable.create(Distance.inches(1.98), Distance.inches(2.58)),
                    Iterable.create(Distance.inches(0.56), Distance.inches(0.71)));
                setSizeTest.run(
                    Distance.inches(5),
                    Distance.inches(4),
                    Iterable.create(Distance.inches(5)),
                    Iterable.create(Distance.inches(4)));
            });

            runner.testGroup("setContentWidth(Distance)", () ->
            {
                final Action2<Distance,Throwable> setContentWidthErrorTest = (Distance contentWidth, Throwable expected) ->
                {
                    runner.test("with " + contentWidth, (Test test) ->
                    {
                        try (final SwingUIWindow window = SwingUIWindowTests.createUIWindow(test))
                        {
                            test.assertThrows(() -> window.setContentWidth(contentWidth), expected);
                        }
                    });
                };

                setContentWidthErrorTest.run(null, new PreConditionFailure("contentWidth cannot be null."));
                setContentWidthErrorTest.run(Distance.inches(-1), new PreConditionFailure("contentWidth (-1.0 Inches) must be greater than or equal to 0.0 Inches."));

                final Action2<Distance,Iterable<Distance>> setContentWidthTest = (Distance contentWidth, Iterable<Distance> expectedWidths) ->
                {
                    runner.test("with " + contentWidth, (Test test) ->
                    {
                        try (final SwingUIWindow window = SwingUIWindowTests.createUIWindow(test))
                        {
                            final SwingUIWindow setWidthResult = window.setContentWidth(contentWidth);
                            test.assertSame(window, setWidthResult);
                            test.assertOneOf(expectedWidths, window.getWidth());
                        }
                    });
                };

                setContentWidthTest.run(
                    Distance.zero,
                    Iterable.create(Distance.inches(1.98), Distance.inches(2.58)));
                setContentWidthTest.run(
                    Distance.inches(1),
                    Iterable.create(Distance.inches(1.98), Distance.inches(2.58)));
                setContentWidthTest.run(
                    Distance.feet(1),
                    Iterable.create(Distance.inches(12.12), Distance.inches(12.14)));
            });

            runner.testGroup("setContentHeight(Distance)", () ->
            {
                final Action2<Distance,Throwable> setContentHeightErrorTest = (Distance contentHeight, Throwable expected) ->
                {
                    runner.test("with " + contentHeight, (Test test) ->
                    {
                        try (final SwingUIWindow window = SwingUIWindowTests.createUIWindow(test))
                        {
                            test.assertThrows(() -> window.setContentHeight(contentHeight), expected);
                        }
                    });
                };

                setContentHeightErrorTest.run(null, new PreConditionFailure("contentHeight cannot be null."));
                setContentHeightErrorTest.run(Distance.inches(-1), new PreConditionFailure("contentHeight (-1.0 Inches) must be greater than or equal to 0.0 Inches."));

                final Action2<Distance,Iterable<Distance>> setContentHeightTest = (Distance contentHeight, Iterable<Distance> expectedHeights) ->
                {
                    runner.test("with " + contentHeight, (Test test) ->
                    {
                        try (final SwingUIWindow window = SwingUIWindowTests.createUIWindow(test))
                        {
                            final SwingUIWindow setHeightResult = window.setContentHeight(contentHeight);
                            test.assertSame(window, setHeightResult);
                            test.assertOneOf(expectedHeights, window.getHeight());
                        }
                    });
                };

                setContentHeightTest.run(
                    Distance.zero,
                    Iterable.create(Distance.inches(0.56), Distance.inches(0.71)));
                setContentHeightTest.run(
                    Distance.inches(1),
                    Iterable.create(Distance.inches(1.35), Distance.inches(1.37)));
                setContentHeightTest.run(
                    Distance.feet(1),
                    Iterable.create(Distance.inches(7.35), Distance.inches(9.77)));
            });

            runner.testGroup("setContentSize(Size2D)", () ->
            {
                final Action2<Size2D,Throwable> setContentSizeErrorTest = (Size2D contentSize, Throwable expected) ->
                {
                    runner.test("with " + contentSize, (Test test) ->
                    {
                        try (final SwingUIWindow window = SwingUIWindowTests.createUIWindow(test))
                        {
                            test.assertThrows(() -> window.setContentSize(contentSize), expected);
                        }
                    });
                };

                setContentSizeErrorTest.run(null, new PreConditionFailure("contentSize cannot be null."));

                final Action2<Size2D,Iterable<Size2D>> setContentSizeTest = (Size2D contentSize, Iterable<Size2D> expectedSizes) ->
                {
                    runner.test("with " + contentSize, (Test test) ->
                    {
                        try (final SwingUIWindow window = SwingUIWindowTests.createUIWindow(test))
                        {
                            final SwingUIWindow setSizeResult = window.setContentSize(contentSize);
                            test.assertSame(window, setSizeResult);
                            test.assertOneOf(expectedSizes, window.getSize());
                        }
                    });
                };

                setContentSizeTest.run(
                    Size2D.create(Distance.zero, Distance.zero),
                    Iterable.create(
                        Size2D.create(Distance.inches(1.98), Distance.inches(0.56)),
                        Size2D.create(Distance.inches(2.58), Distance.inches(0.71))));
                setContentSizeTest.run(
                    Size2D.create(Distance.zero, Distance.inches(1)),
                    Iterable.create(
                        Size2D.create(Distance.inches(1.98), Distance.inches(1.37)),
                        Size2D.create(Distance.inches(2.58), Distance.inches(1.35))));
                setContentSizeTest.run(
                    Size2D.create(Distance.inches(1), Distance.zero),
                    Iterable.create(
                        Size2D.create(Distance.inches(1.98), Distance.inches(0.56)),
                        Size2D.create(Distance.inches(2.58), Distance.inches(0.71))));
                setContentSizeTest.run(
                    Size2D.create(Distance.inches(2), Distance.inches(3)),
                    Iterable.create(
                        Size2D.create(Distance.inches(2.14), Distance.inches(3.37)),
                        Size2D.create(Distance.inches(2.58), Distance.inches(3.35))));
            });

            runner.testGroup("setContentSize(Distance,Distance)", () ->
            {
                final Action3<Distance,Distance,Throwable> setContentSizeErrorTest = (Distance contentWidth, Distance contentHeight, Throwable expected) ->
                {
                    runner.test("with " + English.andList(contentWidth, contentHeight), (Test test) ->
                    {
                        try (final SwingUIWindow window = SwingUIWindowTests.createUIWindow(test))
                        {
                            test.assertThrows(() -> window.setContentSize(contentWidth, contentHeight), expected);
                        }
                    });
                };

                setContentSizeErrorTest.run(null, Distance.inches(1), new PreConditionFailure("contentWidth cannot be null."));
                setContentSizeErrorTest.run(Distance.inches(-1), Distance.inches(1), new PreConditionFailure("contentWidth (-1.0 Inches) must be greater than or equal to 0.0 Inches."));
                setContentSizeErrorTest.run(Distance.zero, null, new PreConditionFailure("contentHeight cannot be null."));
                setContentSizeErrorTest.run(Distance.zero, Distance.inches(-1), new PreConditionFailure("contentHeight (-1.0 Inches) must be greater than or equal to 0.0 Inches."));

                final Action4<Distance,Distance,Iterable<Distance>,Iterable<Distance>> setContentSizeTest = (Distance contentWidth, Distance contentHeight, Iterable<Distance> expectedWidths, Iterable<Distance> expectedHeights) ->
                {
                    runner.test("with " + English.andList(contentWidth, contentHeight), (Test test) ->
                    {
                        try (final SwingUIWindow window = SwingUIWindowTests.createUIWindow(test))
                        {
                            final SwingUIWindow setHeightResult = window.setContentSize(contentWidth, contentHeight);
                            test.assertSame(window, setHeightResult);
                            test.assertOneOf(expectedWidths, window.getWidth());
                            test.assertOneOf(expectedHeights, window.getHeight());
                        }
                    });
                };

                setContentSizeTest.run(
                    Distance.zero,
                    Distance.zero,
                    Iterable.create(Distance.inches(1.98), Distance.inches(2.58)),
                    Iterable.create(Distance.inches(0.56), Distance.inches(0.71)));
                setContentSizeTest.run(
                    Distance.zero,
                    Distance.inches(2),
                    Iterable.create(Distance.inches(1.98), Distance.inches(2.58)),
                    Iterable.create(Distance.inches(2.35), Distance.inches(2.37)));
                setContentSizeTest.run(
                    Distance.inches(1.5),
                    Distance.zero,
                    Iterable.create(Distance.inches(1.98), Distance.inches(2.58)),
                    Iterable.create(Distance.inches(0.56), Distance.inches(0.71)));
                setContentSizeTest.run(
                    Distance.inches(5),
                    Distance.inches(4),
                    Iterable.create(Distance.inches(5.12), Distance.inches(5.14)),
                    Iterable.create(Distance.inches(4.35), Distance.inches(4.37)));
            });
        });
    }
}