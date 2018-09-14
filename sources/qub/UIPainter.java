package qub;

public interface UIPainter
{
    void drawText(String text);

    void drawLine(double startXInPixels, double startYInPixels, double endXInPixels, double endYInPixels);
    void drawLine(Distance startX, Distance startY, Distance endX, Distance endY);
}