package qub;

public class UIBase
{
    private final Display display;
    private final AsyncRunner mainAsyncRunner;

    protected UIBase(Display display, AsyncRunner mainAsyncRunner)
    {
        PreCondition.assertNotNull(display, "display");
        PreCondition.assertNotNull(mainAsyncRunner, "mainAsyncRunner");

        this.display = display;
        this.mainAsyncRunner = mainAsyncRunner;
    }

    public static UIBase create(Display display, AsyncRunner mainAsyncRunner)
    {
        return new UIBase(display, mainAsyncRunner);
    }

    public static UIBase create(Process process)
    {
        return UIBase.create(process.getDisplays().first(), process.getMainAsyncRunner());
    }

    public Result<Void> scheduleAsyncTask(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        return this.mainAsyncRunner.schedule(action);
    }

    public PausedAsyncTask<Void> createPausedAsyncTask(Action0 action)
    {
        PreCondition.assertNotNull(action, "action");

        return this.mainAsyncRunner.create(action);
    }

    public PausedAsyncTask<Void> createPausedAsyncTask()
    {
        return this.createPausedAsyncTask(() -> {});
    }

    public double convertHorizontalDistanceToPixels(Distance horizontalDistance)
    {
        return this.display.convertHorizontalDistanceToPixels(horizontalDistance);
    }

    public Distance convertHorizontalPixelsToDistance(double horizontalPixels)
    {
        return this.display.convertHorizontalPixelsToDistance(horizontalPixels);
    }

    public double convertVerticalDistanceToPixels(Distance verticalDistance)
    {
        return this.display.convertVerticalDistanceToPixels(verticalDistance);
    }

    public Distance convertVerticalPixelsToDistance(double verticalPixels)
    {
        return this.display.convertVerticalPixelsToDistance(verticalPixels);
    }

    public Size2D convertPixelsToSize2D(double horizontalPixels, double verticalPixels)
    {
        return this.display.convertPixelsToSize2D(horizontalPixels, verticalPixels);
    }

    public Point2D convertPixelsToPoint2D(double horizontalPixels, double verticalPixels)
    {
        return this.display.convertPixelsToPoint2D(horizontalPixels, verticalPixels);
    }

    /**
     * Convert the provided UIPaddingInPixels to a UIPadding.
     * @param padding The UIPaddingInPixels to convert.
     * @return The converted UIPadding.
     */
    public UIPadding convertUIPaddingInPixelsToUIPadding(UIPaddingInPixels padding)
    {
        PreCondition.assertNotNull(padding, "padding");

        return UIPadding.create(
            this.convertHorizontalPixelsToDistance(padding.getLeft()),
            this.convertVerticalPixelsToDistance(padding.getTop()),
            this.convertHorizontalPixelsToDistance(padding.getRight()),
            this.convertVerticalPixelsToDistance(padding.getBottom()));
    }

    /**
     * Convert the provided UIPadding to a UIPaddingInPixels.
     * @param padding The UIPadding to convert.
     * @return The converted UIPaddingInPixels.
     */
    public UIPaddingInPixels convertUIPaddingToUIPaddingInPixels(UIPadding padding)
    {
        PreCondition.assertNotNull(padding, "padding");

        return UIPaddingInPixels.create(
            (int)this.convertHorizontalDistanceToPixels(padding.getLeft()),
            (int)this.convertVerticalDistanceToPixels(padding.getTop()),
            (int)this.convertHorizontalDistanceToPixels(padding.getRight()),
            (int)this.convertVerticalDistanceToPixels(padding.getBottom()));
    }
}
