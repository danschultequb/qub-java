package qub;

public interface UIHorizontalLayoutTests
{
    static void test(TestRunner runner, Function1<Test,? extends UIHorizontalLayout> creator)
    {
        PreCondition.assertNotNull(runner, "runner");
        PreCondition.assertNotNull(creator, "creator");

        runner.testGroup(UIHorizontalLayout.class, () ->
        {
            runner.testGroup("setWidth(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(UIHorizontalLayout.class), (Test test) ->
                {
                    final UIHorizontalLayout verticalLayout = creator.run(test);
                    final UIHorizontalLayout setWidthResult = verticalLayout.setWidth(Distance.inches(1));
                    test.assertSame(verticalLayout, setWidthResult);
                });
            });

            runner.testGroup("setHeight(Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(UIHorizontalLayout.class), (Test test) ->
                {
                    final UIHorizontalLayout verticalLayout = creator.run(test);
                    final UIHorizontalLayout setHeightResult = verticalLayout.setHeight(Distance.inches(1));
                    test.assertSame(verticalLayout, setHeightResult);
                });
            });

            runner.testGroup("setSize(Size2D)", () ->
            {
                runner.test("should return " + Types.getTypeName(UIHorizontalLayout.class), (Test test) ->
                {
                    final UIHorizontalLayout verticalLayout = creator.run(test);
                    final UIHorizontalLayout setHeightResult = verticalLayout.setSize(Size2D.create(Distance.inches(2), Distance.inches(3)));
                    test.assertSame(verticalLayout, setHeightResult);
                });
            });

            runner.testGroup("setSize(Distance,Distance)", () ->
            {
                runner.test("should return " + Types.getTypeName(UIHorizontalLayout.class), (Test test) ->
                {
                    final UIHorizontalLayout verticalLayout = creator.run(test);
                    final UIHorizontalLayout setHeightResult = verticalLayout.setSize(Distance.inches(2), Distance.inches(3));
                    test.assertSame(verticalLayout, setHeightResult);
                });
            });

            runner.testGroup("add(UIElement)", () ->
            {
                runner.test("with null", (Test test) ->
                {
                    final UIHorizontalLayout verticalLayout = creator.run(test);
                    test.assertThrows(() -> verticalLayout.add((UIElement)null),
                        new PreConditionFailure("uiElement cannot be null."));
                });

                runner.test("with non-null", (Test test) ->
                {
                    final UIHorizontalLayout verticalLayout = creator.run(test);
                    final UIElement element = creator.run(test);

                    final UIHorizontalLayout addResult = verticalLayout.add(element);
                    test.assertSame(verticalLayout, addResult);
                });
            });

            runner.testGroup("addAll(UIElement...)", () ->
            {
                runner.test("with null element", (Test test) ->
                {
                    final UIHorizontalLayout verticalLayout = creator.run(test);

                    test.assertThrows(() -> verticalLayout.addAll((UIElement)null),
                        new PreConditionFailure("uiElement cannot be null."));
                });

                runner.test("with no arguments", (Test test) ->
                {
                    final UIHorizontalLayout verticalLayout = creator.run(test);

                    final UIHorizontalLayout addResult = verticalLayout.addAll();
                    test.assertSame(verticalLayout, addResult);
                });

                runner.test("with one argument", (Test test) ->
                {
                    final UIHorizontalLayout verticalLayout = creator.run(test);
                    final UIElement element = creator.run(test);

                    final UIHorizontalLayout addResult = verticalLayout.addAll(element);
                    test.assertSame(verticalLayout, addResult);
                });

                runner.test("with multiple arguments", (Test test) ->
                {
                    final UIHorizontalLayout verticalLayout = creator.run(test);
                    final UIElement element1 = creator.run(test);
                    final UIElement element2 = creator.run(test);

                    final UIHorizontalLayout addResult = verticalLayout.addAll(element1, element2);
                    test.assertSame(verticalLayout, addResult);
                });

                runner.test("with null array", (Test test) ->
                {
                    final UIHorizontalLayout verticalLayout = creator.run(test);

                    test.assertThrows(() -> verticalLayout.addAll((UIElement[])null),
                        new PreConditionFailure("uiElements cannot be null."));
                });

                runner.test("with empty array", (Test test) ->
                {
                    final UIHorizontalLayout verticalLayout = creator.run(test);

                    final UIHorizontalLayout addResult = verticalLayout.addAll(new UIElement[0]);
                    test.assertSame(verticalLayout, addResult);
                });

                runner.test("with one element array", (Test test) ->
                {
                    final UIHorizontalLayout verticalLayout = creator.run(test);
                    final UIElement element = creator.run(test);

                    final UIHorizontalLayout addResult = verticalLayout.addAll(new UIElement[] { element });
                    test.assertSame(verticalLayout, addResult);
                });

                runner.test("with multiple element array", (Test test) ->
                {
                    final UIHorizontalLayout verticalLayout = creator.run(test);
                    final UIElement element1 = creator.run(test);
                    final UIElement element2 = creator.run(test);

                    final UIHorizontalLayout addResult = verticalLayout.addAll(new UIElement[] { element1, element2 });
                    test.assertSame(verticalLayout, addResult);
                });
            });

            runner.testGroup("addAll(Iterable<UIElement>)", () ->
            {
                runner.test("with null Iterable", (Test test) ->
                {
                    final UIHorizontalLayout verticalLayout = creator.run(test);

                    test.assertThrows(() -> verticalLayout.addAll((Iterable<UIElement>)null),
                        new PreConditionFailure("uiElements cannot be null."));
                });

                runner.test("with empty Iterable", (Test test) ->
                {
                    final UIHorizontalLayout verticalLayout = creator.run(test);

                    final UIHorizontalLayout addResult = verticalLayout.addAll(Iterable.create());
                    test.assertSame(verticalLayout, addResult);
                });

                runner.test("with one element Iterable", (Test test) ->
                {
                    final UIHorizontalLayout verticalLayout = creator.run(test);
                    final UIElement element = creator.run(test);

                    final UIHorizontalLayout addResult = verticalLayout.addAll(Iterable.create(element));
                    test.assertSame(verticalLayout, addResult);
                });

                runner.test("with multiple element Iterable", (Test test) ->
                {
                    final UIHorizontalLayout verticalLayout = creator.run(test);
                    final UIElement element1 = creator.run(test);
                    final UIElement element2 = creator.run(test);

                    final UIHorizontalLayout addResult = verticalLayout.addAll(Iterable.create(element1, element2));
                    test.assertSame(verticalLayout, addResult);
                });
            });
        });
    }
}