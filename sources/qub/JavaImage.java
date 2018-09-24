package qub;

public class JavaImage implements Image
{
    private final java.awt.Image awtImage;

    public JavaImage(java.awt.Image awtImage)
    {
        PreCondition.assertNotNull(awtImage, "awtImage");

        this.awtImage = awtImage;
    }

    public java.awt.Image getAWTImage()
    {
        return awtImage;
    }
}
