package qub;

/**
 * A UIPainter that records the actions that are performed while drawing UIElements.
 */
public class FakePainter implements UIPainter
{
    private final List<PainterAction> actions;
    private final List<Vector2D> translations;
    private final List<Font> fonts;
    private final List<Color> colors;

    /**
     * Create a new FakePainter.
     */
    public FakePainter()
    {
        this.actions = new ArrayList<>();
        this.translations = ArrayList.fromValues(new Vector2D[] { Vector2D.zero });
        this.fonts = ArrayList.fromValues(new Font[] { new Font(Distance.fontPoints(14)) });
        this.colors = ArrayList.fromValues(new Color[] { Color.black });
    }

    public void clearActions()
    {
        actions.clear();
    }

    public Indexable<PainterAction> getActions()
    {
        return actions;
    }

    public Vector2D getTranslation()
    {
        return translations.last();
    }

    public Font getFont()
    {
        return fonts.last();
    }

    public Distance getFontSize()
    {
        return getFont().getSize();
    }

    public Color getColor()
    {
        return colors.last();
    }

    private Point2D transform(Point2D point)
    {
        PreCondition.assertNotNull(point, "point");

        return getTranslation().plus(point);
    }

    @Override
    public void drawText(String text, Point2D topLeft)
    {
        actions.add(new DrawTextAction(text, transform(topLeft), getFontSize(), getColor()));
    }

    @Override
    public void drawText(String text)
    {
        PreCondition.assertNotNullAndNotEmpty(text, "text");

        drawText(text, Point2D.zero);
    }

    @Override
    public void drawText(String text, Distance topLeftX, Distance topLeftY)
    {
        drawText(text, new Point2D(topLeftX, topLeftY));
    }

    @Override
    public void drawLine(Point2D start, Point2D end)
    {
        actions.add(new DrawLineAction(transform(start), transform(end)));
    }

    @Override
    public void drawLine(Distance startX, Distance startY, Distance endX, Distance endY)
    {
        drawLine(new Point2D(startX, startY), new Point2D(endX, endY));
    }

    @Override
    public void drawRectangle(Distance width, Distance height)
    {
        drawRectangle(Distance.zero, Distance.zero, width, height);
    }

    @Override
    public void drawRectangle(Distance topLeftX, Distance topLeftY, Distance width, Distance height)
    {
        PreCondition.assertNotNull(topLeftX, "topLeftX");
        PreCondition.assertNotNull(topLeftY, "topLeftY");
        PreCondition.assertGreaterThan(width, Distance.zero, "width");
        PreCondition.assertGreaterThan(height, Distance.zero, "height");

        actions.add(new DrawRectangleAction(new Point2D(topLeftX, topLeftY), width, height, getColor()));
    }

    @Override
    public void fillRectangle(Distance width, Distance height)
    {
        fillRectangle(Distance.zero, Distance.zero, width, height);
    }

    @Override
    public void fillRectangle(Distance topLeftX, Distance topLeftY, Distance width, Distance height)
    {
        PreCondition.assertNotNull(topLeftX, "topLeftX");
        PreCondition.assertNotNull(topLeftY, "topLeftY");
        PreCondition.assertGreaterThan(width, Distance.zero, "width");
        PreCondition.assertGreaterThan(height, Distance.zero, "height");

        actions.add(new FillRectangleAction(new Point2D(topLeftX, topLeftY), width, height, getColor()));
    }

    @Override
    public void translate(Distance x, Distance y)
    {
        final Vector2D currentTranslation = getTranslation();
        translations.setLast(currentTranslation.plusVector(x, y));
    }

    @Override
    public void translateY(Distance y)
    {
        translate(Distance.zero, y);
    }

    @Override
    public Disposable saveTransform()
    {
        translations.add(getTranslation());
        return new BasicDisposable()
        {
            @Override
            protected void onDispose()
            {
                restoreTransform();
            }
        };
    }

    @Override
    public void restoreTransform()
    {
        PreCondition.assertGreaterThanOrEqualTo(translations.getCount(), 2, "translations.getCount()");

        translations.removeLast();
    }

    @Override
    public void setFont(Font font)
    {
        PreCondition.assertNotNull(font, "font");

        fonts.setLast(font);
    }

    @Override
    public void setFontSize(Distance fontSize)
    {
        PreCondition.assertGreaterThanOrEqualTo(fontSize, Distance.zero, "fontSize");

        setFont(getFont().changeSize(fontSize));
    }

    @Override
    public Disposable saveFont()
    {
        fonts.add(getFont());
        return new BasicDisposable()
        {
            @Override
            protected void onDispose()
            {
                restoreFont();
            }
        };
    }

    @Override
    public void restoreFont()
    {
        PreCondition.assertGreaterThanOrEqualTo(fonts.getCount(), 2, "fontSizes.getCount()");

        fonts.removeLast();
    }

    @Override
    public void setColor(Color color)
    {
        PreCondition.assertNotNull(color, "color");

        colors.setLast(color);
    }

    @Override
    public Disposable saveColor()
    {
        colors.add(getColor());
        return new BasicDisposable()
        {
            @Override
            protected void onDispose()
            {
                restoreColor();
            }
        };
    }

    @Override
    public void restoreColor()
    {
        PreCondition.assertNotNullAndNotEmpty(colors, "colors");

        colors.removeLast();
    }
}