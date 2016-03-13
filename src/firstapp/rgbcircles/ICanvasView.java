package firstapp.rgbcircles;

/**
 * Created by 161 on 06.03.2016.
 */
public interface ICanvasView {
    void drawCircle(SimpleCircle circle);

    void redraw();

    void showMessage(String text);
}
