import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 75;
    final int x[] = new int[GAME_UNITS]; // Holds all the X coordinates of the snake
    final int y[] = new int[GAME_UNITS]; // Holds all the Y coordinates of the snake
    int bodyParts = 6;
    int applesEaten;
    int appleX; // The X coordinates of the random apple
    int appleY; // The Y coordinates of the random apple
    char direction = 'R'; // The snake starts the game going 'RIGHT'
    boolean running = false;
    Timer timer;
    Random random;
    boolean gameOverScreen = false;
    int highScore;
    boolean startScreen = true;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startMenu(Graphics g) {

        if (startScreen) {
            if (!running) {
                // Start menu text
                /*JLabel label1 = new JLabel("WELCOME TO THE SNAKE GAME");
                label1.setForeground(Color.RED);
                label1.setFont(new Font("Arial", Font.BOLD, 25));
                this.add(label1);

                JLabel label2 = new JLabel("PRESS [S] TO START");
                label2.setForeground(Color.YELLOW);
                label2.setFont(new Font("Arial", Font.BOLD, 50));
                this.add(label2);*/

                //startGame();
            }
            else {
                startScreen = false;
                startGame();
            }
        }
    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        if (running) {
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {

                // The head of the snake
                if (i == 0) {
                    g.setColor(Color.GREEN);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                // The body of the snake
                else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont()); // Lineup text in top of the screen
            g.drawString("SCORE: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("SCORE: " + applesEaten)) / 2, g.getFont().getSize());

        } else {
            gameOver(g);
        }
    }

    public void newApple() {
        appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE; // Generate a random apple inside the screen
        appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    public void checkApple() {

        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions() {
        // Checks if the head collided with the body of snake
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
        // Checks if the head touches left border
        if (x[0] < 0) {
            running = false;
        }
        // Checks if the head touches right border
        if (x[0] > SCREEN_WIDTH - UNIT_SIZE) {
            running = false;
        }
        // Checks if the head touches top border
        if (y[0] < 0) {
            running = false;
        }
        // Checks if the head touches bottom border
        if (y[0] > SCREEN_HEIGHT - UNIT_SIZE) {
            running = false;
        }

        if (!running) {
            gameOverScreen = true;
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {

        if (gameOverScreen) {
            if (!running) {
                // Display the score
                g.setColor(Color.YELLOW);
                g.setFont(new Font("Arial", Font.BOLD, 40));
                FontMetrics metrics = getFontMetrics(g.getFont()); //Lineup text in top of the screen
                g.drawString("YOUR SCORE: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("YOUR SCORE: " + applesEaten)) / 2, g.getFont().getSize());

                // Display the high score
                g.setColor(Color.GREEN);
                g.setFont(new Font("Arial", Font.BOLD, 50));
                FontMetrics metrics1 = getFontMetrics(g.getFont()); //Lineup text in top of the screen
                if (applesEaten > highScore) {
                    highScore = applesEaten;
                }
                g.drawString("HIGH SCORE: " + highScore, (SCREEN_WIDTH - metrics1.stringWidth("HIGH SCORE: " + highScore)) / 2, g.getFont().getSize() + UNIT_SIZE * 4);

                // Game Over text
                g.setColor(Color.RED);
                g.setFont(new Font("Arial", Font.BOLD, 75));
                FontMetrics metrics2 = getFontMetrics(g.getFont()); //Lineup text in the center of the screen
                g.drawString("GAME OVER", (SCREEN_WIDTH - metrics2.stringWidth("GAME OVER")) / 2, SCREEN_HEIGHT / 2);

                // Restart text
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 40));
                FontMetrics metrics3 = getFontMetrics(g.getFont()); //Lineup text in the center of the screen
                g.drawString("PRESS [R] TO RESTART", (SCREEN_WIDTH - metrics3.stringWidth("PRESS [R] TO RESTART")) / 2, SCREEN_HEIGHT - UNIT_SIZE * 6);

                // Exit text
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 40));
                FontMetrics metrics4 = getFontMetrics(g.getFont()); //Lineup text in the center of the screen
                g.drawString("PRESS [ESC] TO EXIT", (SCREEN_WIDTH - metrics4.stringWidth("PRESS [ESC] TO EXIT")) / 2, SCREEN_HEIGHT - UNIT_SIZE);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {

            switch (e.getKeyCode()) {
                // Initiate the game with letter [S]
                case KeyEvent.VK_S:
                    if (!running) {
                        startGame();
                    }
                    break;
                // Snake can only do 90 degrees move
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
                // Restart the game with letter [R]
                case KeyEvent.VK_R:
                    if (!running) {
                        // Set everything to the original engine
                        running = true;
                        bodyParts = 6;
                        newApple();
                        applesEaten = 0;
                        x[0] = 0;
                        y[0] = 0;
                        direction = 'R';
                        timer.restart();
                        new GamePanel();
                        repaint();
                    }
                    break;
                // Exit the game with letter [ESC]
                case KeyEvent.VK_ESCAPE:
                    if (!running) {
                        System.exit(0);
                    }
                    break;
            }
        }
    }
}
