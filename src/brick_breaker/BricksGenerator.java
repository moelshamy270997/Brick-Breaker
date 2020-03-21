package brick_breaker;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

public class BricksGenerator {

    public int bricks[][];
    private Color bricksColor[][];
    public int width, height;

    public BricksGenerator(int row, int col) {

        bricks = new int[row][col];
        bricksColor = new Color[row][col];

        Random random = new Random();

        for (int i = 0; i < bricks.length; i++) {
            for (int j = 0; j < bricks[0].length; j++) {
                bricks[i][j] = 1;

                float r = random.nextFloat();
                bricksColor[i][j] = new Color(r, 0f, 0.3f);
            }
        }

        width = 540 / col;
        height = 150 / row;
    }

    public void draw(Graphics2D g) {

        for (int i = 0; i < bricks.length; i++) {
            for (int j = 0; j < bricks[0].length; j++) {
                if (bricks[i][j] > 0) {
                    g.setColor(bricksColor[i][j]);
                    g.fillRect(j * width + 80, i * height + 50, width, height);

                    g.setStroke(new BasicStroke(3));
                    g.setColor(Color.black);
                    g.drawRect(j * width + 80, i * height + 50, width, height);
                }
            }
        }
    }

    public void setBrickValue(int value, int row, int col) {
        bricks[row][col] = value;
    }
}
