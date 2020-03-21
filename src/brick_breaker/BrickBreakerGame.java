package brick_breaker;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class BrickBreakerGame implements KeyListener {

    private JFrame gameFrame;
    private GamePanel gamePanel;
    private JLabel welcomeLabel = new JLabel("Welcome to Brick Breaker game", SwingConstants.CENTER);
    
    private String level;
    private int rows, cols;
    private JPanel mainPanel, levelPanel, scorePanel, instructionsPanel;
    private JButton startButton, scoresButton, instructionsButton, exitButton, backButton, easy, middle, hard, scoresReset;
    
    private String playerName;

    public BrickBreakerGame() {

        gameFrame = new JFrame();
        setGameFrame(gameFrame);

        gameFrame.setFocusable(true);
        gameFrame.addKeyListener(this);

        startButton = new JButton("Start Game");
        scoresButton = new JButton("Best Scores");
        instructionsButton = new JButton("Instructions");
        exitButton = new JButton("Exit");

        easy = new JButton("Easy");
        middle = new JButton("Middle");
        hard = new JButton("Hard");
        backButton = new JButton("Back");

        scoresReset = new JButton("Scores Reset");

        // Setting Buttons
        setButton(startButton);
        setButton(scoresButton);
        setButton(instructionsButton);
        setButton(exitButton);
        setButton(easy);
        setButton(middle);
        setButton(hard);
        setButton(backButton);
        setButton(scoresReset);

        setButtonsActions();

        loadMainPanel();

    }

    public void setGameFrame(JFrame jframe) {

        jframe.setTitle("Brick Breaker Game");
        jframe.setVisible(true);
        jframe.setResizable(false);
        jframe.setBounds(0, 0, 700, 600);
        jframe.setLocationRelativeTo(null);

        jframe.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                if (gamePanel != null) {
                    gamePanel.setGameOver(true);
                }

                int confirm = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to exit?", "Exit !!!", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    System.exit(0);
                } else {
                    if (gamePanel != null) {
                        gamePanel.setGameOver(false);
                    }
                    jframe.setDefaultCloseOperation(jframe.DO_NOTHING_ON_CLOSE);
                }

            }
        });

    }

    public void setButton(JButton jbutton) {
        jbutton.setBackground(Color.black);
        jbutton.setBorderPainted(false);
        jbutton.setForeground(Color.red);
        jbutton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        Font font = new Font("serif", Font.BOLD, 30);
        jbutton.setFont(font);

        jbutton.addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent evt) {
                jbutton.setFont(new Font("serif", Font.BOLD, 40));
            }

            public void mouseExited(MouseEvent evt) {
                jbutton.setFont(font);
            }
        });

    }

    public void setButtonsActions() {
        // Set Exit Button
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                int confirm = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to exit?", "Comform !!!", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        // Set Start Button
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                loadLevelPanel();
            }
        });

        // Set Back Button
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                loadMainPanel();
            }
        });

        // Set Scores Reset Button
        scoresReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to Reset Scores?", 
                        "Scores Reset...", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);

                if (confirm == JOptionPane.YES_OPTION) {
                    
                    // Delete Scores Directory
                    File file = new File("scores");
                    if (file.exists())
                        deleteDirectory(file);
                    
                    loadScorePanel();
                }
            }
        });

        // Set Score Button
        scoresButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                loadScorePanel();
            }
        });
        
        // Set Instructions Button
        instructionsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadInstructionsPanel();
            }
        });

        easy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                rows = 3; cols = 7; level = "easy";
                playerName = getPlayerName();
                if (playerName != null)
                    loadGamePanel();
            }
        });

        middle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                rows = 4; cols = 9; level = "middle";
                playerName = getPlayerName();
                if (playerName != null)
                    loadGamePanel();
            }
        });

        hard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                rows = 5; cols = 11; level = "hard";
                playerName = getPlayerName();
                if (playerName != null)
                    loadGamePanel();
            }
        });
    }

    public static void deleteDirectory(File file) {
        if (file.isDirectory())
            for (File subFile : file.listFiles())
                deleteDirectory(subFile);
        file.delete();
    }
    
    public void setMainPanel() {
        mainPanel = new JPanel();

        mainPanel.setBounds(0, 0, 700, 600);
        mainPanel.setBackground(Color.black);
        mainPanel.setLayout(null);

        welcomeLabel.setBounds(0, 0, 700, 100);
        welcomeLabel.setFont(new Font("serif", Font.BOLD, 35));
        welcomeLabel.setForeground(Color.white);

        startButton.setBounds(200, 150, 300, 50);
        scoresButton.setBounds(200, 200, 300, 50);
        instructionsButton.setBounds(200, 250, 300, 50);
        exitButton.setBounds(200, 300, 300, 50);

        mainPanel.add(welcomeLabel);

        mainPanel.add(startButton);
        mainPanel.add(scoresButton);
        mainPanel.add(instructionsButton);
        mainPanel.add(exitButton);

    }

    public void setLevelPanel() {

        levelPanel = new JPanel();

        levelPanel.setBounds(0, 0, 700, 600);
        levelPanel.setBackground(Color.black);
        levelPanel.setLayout(null);

        easy.setBounds(200, 150, 300, 35);
        middle.setBounds(200, 200, 300, 35);
        hard.setBounds(200, 250, 300, 35);
        backButton.setBounds(-50, 400, 300, 35);

        levelPanel.add(easy);
        levelPanel.add(middle);
        levelPanel.add(hard);
        levelPanel.add(backButton);
    }

    public void setScorePanel() {

        scorePanel = new JPanel();

        scorePanel.setBounds(0, 0, 700, 600);
        scorePanel.setBackground(Color.black);
        scorePanel.setLayout(null);

        welcomeLabel.setBounds(0, 0, 700, 100);
        welcomeLabel.setFont(new Font("serif", Font.BOLD, 35));
        welcomeLabel.setForeground(Color.white);

        backButton.setBounds(-50, 450, 300, 35);
        scoresReset.setBounds(400, 450, 300, 35);

        // Scores Label
        JLabel easyScores = new JLabel("EASY", SwingConstants.CENTER);
        easyScores.setFont(new Font("serif", Font.BOLD, 30));
        easyScores.setForeground(Color.red);

        JLabel midScores = new JLabel("MIDDLE", SwingConstants.CENTER);
        midScores.setFont(new Font("serif", Font.BOLD, 30));
        midScores.setForeground(Color.red);

        JLabel hardScores = new JLabel("HARD", SwingConstants.CENTER);
        hardScores.setFont(new Font("serif", Font.BOLD, 30));
        hardScores.setForeground(Color.red);

        easyScores.setBounds(50, 180, 200, 30);
        midScores.setBounds(250, 180, 200, 30);
        hardScores.setBounds(450, 180, 200, 30);

        scorePanel.add(welcomeLabel);
        scorePanel.add(backButton);
        scorePanel.add(scoresReset);
        scorePanel.add(easyScores);
        scorePanel.add(midScores);
        scorePanel.add(hardScores);

        loadScores(50, "easyScores");
        loadScores(250, "middleScores");
        loadScores(450, "hardScores");
    }
    
    public void setInstructionsPanel() {
        instructionsPanel = new JPanel();

        instructionsPanel.setBounds(0, 0, 700, 600);
        instructionsPanel.setBackground(Color.black);
        instructionsPanel.setLayout(null);
        
        String instructions;
        instructions = "<html> <div style='text-align: center;'>"
                + "use <b style='color: red;'>LEFT CLICK</b> to start the Game<br/><br/>"
                + "use <b style='color: red;'>ENTER</b> to restart the Game<br/><br/>"
                + "use <b style='color: red;'>ESC</b> key to Pause and go Back<br/><br/>"
                + "</div></html>";
        
        JLabel instructionsLabel = new JLabel(instructions, SwingConstants.CENTER);
        
        instructionsLabel.setBounds(0, 0, 700, 450);
        instructionsLabel.setForeground(Color.white);
        instructionsLabel.setFont(new Font("serif", 0, 25));
        
        backButton.setBounds(-50, 450, 300, 35);
        
        instructionsPanel.add(instructionsLabel);
        instructionsPanel.add(backButton);
    }
    
    public void loadInstructionsPanel() {
        setInstructionsPanel();
        gameFrame.getContentPane().removeAll();

        gameFrame.add(instructionsPanel);
        gameFrame.repaint();
    }
    
    public void loadScores(int tempShift, String str) {

        File file = new File("scores/" + str + ".txt");
        JLabel tempLabel;

        if (file.exists()) {

            String[] scoreAndName = new String[2];
            int tempHeight = 240;
            
            try {
                FileReader fileReader = new FileReader(file);
                BufferedReader reader = new BufferedReader(fileReader);

                String line = reader.readLine();
                scoreAndName = line.split(" ", 2);

                for (int i = 1; i >= 0; i--) {
                    
                    tempLabel = new JLabel(scoreAndName[i], SwingConstants.CENTER);
                    tempLabel.setFont(new Font("serif", Font.BOLD, 30));
                    tempLabel.setForeground(Color.white);
                    tempLabel.setBounds(tempShift, tempHeight, 200, 30);
                    tempHeight += 50;
                    scorePanel.add(tempLabel);
                }
                
                fileReader.close();
                reader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            tempLabel = new JLabel("No Scores!", SwingConstants.CENTER);
            tempLabel.setFont(new Font("serif", Font.BOLD, 30));
            tempLabel.setForeground(Color.white);
            tempLabel.setBounds(tempShift, 240, 200, 30);

            scorePanel.add(tempLabel);
        }
    }

    public void loadMainPanel() {
        setMainPanel();
        gameFrame.getContentPane().removeAll();

        gameFrame.add(mainPanel);
        gameFrame.repaint();
    }

    public void loadLevelPanel() {
        setLevelPanel();
        gameFrame.getContentPane().removeAll();

        gameFrame.add(levelPanel);
        gameFrame.repaint();
    }

    public void loadScorePanel() {
        setScorePanel();
        gameFrame.getContentPane().removeAll();

        gameFrame.add(scorePanel);
        gameFrame.repaint();
    }

    public void loadGamePanel() {
        gamePanel = new GamePanel(rows, cols, level, playerName);
        gameFrame.getContentPane().removeAll();

        gameFrame.add(gamePanel);
        SwingUtilities.updateComponentTreeUI(gameFrame);
        gameFrame.repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_ENTER && gamePanel.getGameOver()) {
            loadGamePanel();
        }

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE && gamePanel != null) {
            gamePanel.setGameOver(true);
            int confirm = JOptionPane.showConfirmDialog(null,
                    "Back to Main List?", "Exit !!!", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                gamePanel = null;
                loadMainPanel();
            } else {
                gamePanel.setGameOver(false);
                gameFrame.setDefaultCloseOperation(gameFrame.DO_NOTHING_ON_CLOSE);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
    
    public String getPlayerName() {
        
        String name = JOptionPane.showInputDialog(null, "Enter your Name", 
                "Player Name...", JOptionPane.QUESTION_MESSAGE);
        
        if (name != null && name.equals(""))
            name = "No Name";
        
        return name;
    }
}
