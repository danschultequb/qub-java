package qub;

public class JavaUIBase
{
    private final Display display;
    private final AsyncRunner asyncRunner;

    private JavaUIBase(Display display, AsyncRunner asyncRunner)
    {
        PreCondition.assertNotNull(display, "display");
        PreCondition.assertNotNull(asyncRunner, "asyncRunner");

        this.display = display;
        this.asyncRunner = asyncRunner;
    }

    public static JavaUIBase create(Display display, AsyncRunner asyncRunner)
    {
        return new JavaUIBase(display, asyncRunner);
    }

    public Result<Void> scheduleAsyncTask(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        return this.asyncRunner.schedule(action);
    }

    public PausedAsyncTask<Void> createPausedAsyncTask(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        return this.asyncRunner.create(action);
    }

    public PausedAsyncTask<Void> createPausedAsyncTask()
    {
        return this.createPausedAsyncTask(() -> {});
    }

    public Distance getWidth(java.awt.Component component)
    {
        PreCondition.assertNotNull(component, "component");

        final int widthInPixels = component.getWidth();
        final Distance result = this.display.convertHorizontalPixelsToDistance(widthInPixels);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertGreaterThanOrEqualTo(result, Distance.zero, "result");

        return result;
    }

    public Distance getHeight(java.awt.Component component)
    {
        PreCondition.assertNotNull(component, "component");

        final int heightInPixels = component.getHeight();
        final Distance result = this.display.convertVerticalPixelsToDistance(heightInPixels);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertGreaterThanOrEqualTo(result, Distance.zero, "result");

        return result;
    }

    public void setSize(java.awt.Component component, Distance width, Distance height)
    {
        PreCondition.assertNotNull(component, "component");
        PreCondition.assertNotNull(width, "width");
        PreCondition.assertGreaterThanOrEqualTo(width, Distance.zero, "width");
        PreCondition.assertNotNull(height, "height");
        PreCondition.assertGreaterThanOrEqualTo(height, Distance.zero, "height");

        final int widthInPixels = (int)this.display.convertHorizontalDistanceToPixels(width);
        final int heightInPixels = (int)this.display.convertVerticalDistanceToPixels(height);
        component.setSize(widthInPixels, heightInPixels);
    }

    /**
     * Register the provided callback to be invoked when the provided component's size changes.
     * @param component The component to watch.
     * @param callback The callback to register.
     * @return A Disposable that can be disposed to unregister the provided callback from the
     * provided component.
     */
    public Disposable onSizeChanged(java.awt.Component component, Action0 callback)
    {
        PreCondition.assertNotNull(component, "component");
        PreCondition.assertNotNull(callback, "callback");

        final java.awt.event.ComponentListener componentListener = new java.awt.event.ComponentListener()
        {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e)
            {
                JavaUIBase.this.scheduleAsyncTask(callback);
            }

            @Override
            public void componentMoved(java.awt.event.ComponentEvent e)
            {
            }

            @Override
            public void componentShown(java.awt.event.ComponentEvent e)
            {
            }

            @Override
            public void componentHidden(java.awt.event.ComponentEvent e)
            {
            }
        };
        component.addComponentListener(componentListener);
        return Disposable.create(() -> component.removeComponentListener(componentListener));
    }

    /**
     * Set the size of the font of the provided JComponent.
     * @param jComponent The JComponent to set the font size for.
     * @param fontSize The size of the font to set.
     */
    public void setFontSize(javax.swing.JComponent jComponent, Distance fontSize)
    {
        PreCondition.assertNotNull(jComponent, "jComponent");
        PreCondition.assertNotNull(fontSize, "fontSize");
        PreCondition.assertGreaterThanOrEqualTo(fontSize, Distance.zero, "fontSize");

        final java.awt.Font font = jComponent.getFont();
        final float fontPoints = (float)fontSize.toFontPoints().getValue();
        final java.awt.Font updatedFont = font.deriveFont(fontPoints);
        jComponent.setFont(updatedFont);
    }

    public Distance getFontSize(javax.swing.JComponent jComponent)
    {
        PreCondition.assertNotNull(jComponent, "jComponent");

        final float fontSize2D = jComponent.getFont().getSize2D();
        final Distance result = Distance.fontPoints(fontSize2D);

        PostCondition.assertNotNull(result, "result");
        PostCondition.assertGreaterThanOrEqualTo(result, Distance.zero, "result");

        return result;
    }
}
