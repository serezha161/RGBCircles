package firstapp.rgbcircles;

import java.util.ArrayList;

/**
 * Created by 161 on 06.03.2016.
 */
public class GameManager {
    public static final int MAX_CIRCLES = 10;
    private MainCircle mainCircle;
    private ArrayList<EnemyCircle> circles;
    private CanvasView canvasView;
    private static int width;
    private static int height;
    private int wins = 0;
    private int loses = 0;
    public  int scope = 0;

    public GameManager(CanvasView canvasView, int w, int h) {
        this.canvasView = canvasView;
        width = w;
        height = h;
        initMainCircle();
        initEnemyCircles();
    }

    private void initEnemyCircles() {
        SimpleCircle mainCircleArea = mainCircle.getCircleArea();
        circles = new ArrayList<EnemyCircle>();
        for (int i = 0; i < MAX_CIRCLES; i++) {
            EnemyCircle circle;
            do {
                circle = EnemyCircle.GetRandomCircle();
            } while (circle.isIntersect(mainCircleArea));
            circles.add(circle);
        }
        calculateAndSetCirclesColor();
    }

    private void calculateAndSetCirclesColor() {
        for (EnemyCircle circle : circles) {
            circle.setEnemyOrFoodColorDependsOn(mainCircle);
        }
    }

    public static int getWidth() { return width; }
    public static int getHeight() { return height; }

    private void initMainCircle() { mainCircle = new MainCircle(width / 2, height / 2); }

    public void onDraw() {
        canvasView.drawCircle(mainCircle);
        for (EnemyCircle circle : circles) { canvasView.drawCircle(circle);}
    }

    public void onTouchManager(int x, int y) {
        mainCircle.moveMainCircleWhenTouchAt(x, y);
        checkCollisions();
        moveCircles();
    }

    private void checkCollisions() {
        SimpleCircle circleForDell = null;
        for (EnemyCircle circle : circles) {
            if (mainCircle.isIntersect(circle)) {
                if (circle.isSmallerThan(mainCircle)) {
                    mainCircle.growRadius(circle);
                    circleForDell = circle;
                    calculateAndSetCirclesColor();
                    scope += circle.radius;
                    canvasView.showSkope("Skope: " + scope);
                    break;
                } else {
                    ++loses;
                    scope = 0;
                    gameEnd(" LOSE!\n Losses: " + loses + "\n Wins: " + wins);
                    return;
                }
            }
        }
        if (circleForDell != null) { circles.remove(circleForDell); }
        if (circles.isEmpty()) {
            ++wins;
            gameEnd(" WIN!\n Wins: " + wins + "\n Losses: " + loses);
        }
    }

    private void gameEnd(String text) {
        canvasView.showMessage(text);
        mainCircle.initRadius();
        initEnemyCircles();
        canvasView.redraw();
    }

    private void moveCircles() {
        for (EnemyCircle circle : circles) { circle.moveOneStep(); }
    }
}
