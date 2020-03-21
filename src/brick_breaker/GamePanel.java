package brick_breaker;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener, MouseMotionListener, MouseListener {

    private BricksGenerator bg;
    private boolean gameOver = true;
    private int score;
    private int bricksNumber;
    private int rows, cols;

    private int totalBricks;
    private Timer time;
    private int delay;

    private int playerPositionX;

    private int ballPositionX;
    private int ballPositionY;

    private int ballDirectionX;
    private int ballDirectionY;
    
    private String level, playerName;

    public GamePanel(int rows, int cols, String level, String playerName) {

        this.rows = rows;
        this.cols = cols;
        this.level = level;
        this.playerName = playerName;
                
        bricksNumber = rows * cols;
        totalBricks = bricksNumber;

        addMouseMotionListener(this);
        addMouseListener(this);

        startGame();
    }

    public void startGame() {

        bg = new BricksGenerator(rows, cols);
        setDelay();

        score = 0;

        playerPositionX = 300;

        ballPositionX = (int) (Math.random() * ((640 - 50) + 1)) + 50;
        ballPositionY = 300;

        ballDirectionX = -1;
        ballDirectionY = -2;
        
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        time = new Timer(delay, this);
        time.start();

        repaint();
    }

    public void setDelay() {
        if (level.equals("easy")) {
            delay = 8;
        } else if (level.equals("middle")) {
            delay = 6;
        } else if (level.equals("hard")) {
            delay = 4;
        }
    }

    public boolean getGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean b) {
        gameOver = b;
    }

    @Override
    public void paint(Graphics g) {
        // Background
        g.setColor(Color.black);
        g.fillRect(5, 5, 685, 570);

        // Paddle
        g.setColor(Color.green);
        g.fillRect(playerPositionX, 550, 100, 8);

        // print Score
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("Score: " + score, 520, 30);

        // Borders
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 5, 600);
        g.fillRect(0, 0, 700, 5);
        g.fillRect(690, 0, 5, 600);

        // Ball
        g.setColor(Color.yellow);
        g.fillOval(ballPositionX, ballPositionY, 20, 20);

        // Draw Bricks
        bg.draw((Graphics2D) g);

        // Won
        if (totalBricks <= 0) {
            time.stop();
            gameOver = true;

            g.setColor(Color.green);
            g.setFont(new Font("serif", Font.BOLD, 30));
            centerString(g, "YOU WON", 0, 250);

            g.setColor(Color.white);
            g.setFont(new Font("serif", Font.BOLD, 25));
            centerString(g, "press ENTER to restart", 0, 300);
            
            g.setFont(new Font("serif", Font.BOLD, 25));
            centerString(g, "press ESC to go back", 0, 340);
        }

        // Game Over
        if (ballPositionY > 570) {
            time.stop();
            gameOver = true;
            
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            centerString(g, "YOU LOST", 0, 250);

            g.setColor(Color.white);
            g.setFont(new Font("serif", Font.BOLD, 25));
            centerString(g, "press ENTER to restart", 0, 300);
            
            g.setFont(new Font("serif", Font.BOLD, 25));
            centerString(g, "press ESC to go back", 0, 340);
        }
        
        delayIncrease();
        
        g.dispose();
        
        if (!time.isRunning())
            saveScores();
        
    }
    
    public void delayIncrease() {
        int procent = (int) ( ( (float) totalBricks / bricksNumber ) * 100 );
        
        if (procent > 50 && procent < 80)
            time.setDelay(delay - 1);
        else if (procent > 25 && procent < 50)
            time.setDelay(delay - 2);
        else if (procent > 0 && procent < 25)
            time.setDelay(delay - 3);
    }
    
    public void centerString(Graphics g, String str, int x, int y) {
        
        int stringLen = (int) g.getFontMetrics().getStringBounds(str, g).getWidth();
        int start = 350 - stringLen / 2;
        g.drawString(str, start + x, y);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            //Intersection from left
            boolean boardIntersectLeft = new Rectangle(ballPositionX, ballPositionY, 20, 20)
                    .intersects(new Rectangle(playerPositionX, 550, 50, 8));

            //Intersection from right
            boolean boardIntersectRight = new Rectangle(ballPositionX, ballPositionY, 20, 20)
                    .intersects(new Rectangle(playerPositionX + 50, 550, 50, 8));

            if (boardIntersectLeft) {
                if (ballDirectionX < 0) {
                    ballDirectionY = -ballDirectionY;
                } else {
                    ballDirectionX = -ballDirectionX;
                    ballDirectionY = -ballDirectionY;
                }

            } else if (boardIntersectRight) {
                if (ballDirectionX < 0) {
                    ballDirectionX = -ballDirectionX;
                    ballDirectionY = -ballDirectionY;
                } else {
                    ballDirectionY = -ballDirectionY;
                }
            }

            // Intersection with Bricks
            Level:
            for (int i = 0; i < bg.bricks.length; i++) {
                for (int j = 0; j < bg.bricks[0].length; j++) {
                    if (bg.bricks[i][j] > 0) {
                        int brickX = j * bg.width + 80;
                        int brickY = i * bg.height + 50;

                        Rectangle tempBallRect = new Rectangle(ballPositionX, ballPositionY, 20, 20);
                        Rectangle tempBrickRect = new Rectangle(brickX, brickY, bg.width, bg.height);

                        boolean bricksIntersect = tempBallRect.intersects(tempBrickRect);

                        if (bricksIntersect) {
                            bg.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;

                            // Change Dall Direction
                            if (ballPositionX + 19 <= tempBrickRect.x
                                    || ballPositionX + 1 >= tempBrickRect.x + tempBrickRect.width) {

                                ballDirectionX = -ballDirectionX;
                            } else {
                                ballDirectionY = -ballDirectionY;
                            }
                            break Level;
                        }

                    }
                }
            }

            ballPositionX += ballDirectionX;
            ballPositionY += ballDirectionY;

            // Left
            if (ballPositionX < 5) {
                ballDirectionX = -ballDirectionX;
            }

            // Top
            if (ballPositionY < 5) {
                ballDirectionY = -ballDirectionY;
            }

            // Right
            if (ballPositionX >= 670) {
                ballDirectionX = -ballDirectionX;
            }

            repaint();
        }
        
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int x = e.getX();
        if (x > 5 && x < 590 && !gameOver) {
            playerPositionX = e.getX();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        gameOver = false;
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    public void hideCursor() {
        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorImg, new Point(0, 0), "blank cursor");
        setCursor(blankCursor);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        hideCursor();
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    
    public void saveScores() {
        try {
            File dir = new File("scores");
            if (!dir.exists())
                dir.mkdir();
            
            File file = new File("scores/" + level + "Scores.txt");

            if (file.exists()) {
                compareScoresAndSave(file);
            } else if (score > 0) {
                file.createNewFile();
                FileWriter fileWriter = new FileWriter(file);

                fileWriter.write(Integer.toString(score) + " " + playerName);
                fileWriter.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void compareScoresAndSave(File file) {

        int bestScore = 0;
        String[] scoreAndName = new String[2];

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);

            String line = reader.readLine();
            scoreAndName = line.split(" ", 2);
            bestScore = Integer.parseInt(scoreAndName[0]);

            fileReader.close();
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (bestScore < score) {
            bestScore = score;
            
            try {
                
                FileWriter fileWriter = new FileWriter(file);
                BufferedWriter writer = new BufferedWriter(fileWriter);

                writer.write(Integer.toString(bestScore) + " " + playerName);
                writer.newLine();

                writer.close();
                fileWriter.close();
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
}
