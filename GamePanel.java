package Snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class GamePanel extends JPanel implements ActionListener {

    // width of the screen
    static final int SCREEN_WIDTH = 600;
    // height of the screen
    static final int SCREEN_HEIGHT = 600;
    // size of units in the game
    static final int UNIT_SIZE = 25;
    // how many available units we have
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    // how fast the game moves
    static final int DELAY = 75;
    // coordinates for body parts of snake
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    // snake initial body parts
    int bodyParts = 6;
    // amounts of apples eaten
    int applesEaten;
    // coordinate x for apple
    int appleX;
    // coordinate y for apple
    int appleY;
    // initial direction in which the snake is moving
    char direction = 'R';
    // check if game is running or not
    boolean running = false;
    // timer
    Timer timer;
    // random generator
    Random random;
    String userName = "";

    GamePanel() {
        // create the random generator
        random = new Random();
        // dimensions of the window
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        // color of the background
        this.setBackground(Color.BLACK);
        // focusable state
        this.setFocusable(true);
        // key events to control the game
        this.addKeyListener(new MyKeyAdapter());
        this.addKeyListener(new MyKeyChar());
        // game start
        startGame();
    }

    public void startGame() {
        // create a new apple
        newApple();
        // set running to true in order to start the game
        running = true;
        // set the timer
        timer = new Timer(DELAY, this);
        // start the timer
        timer.start();
    }

    // put the components into the frame
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    // draw the components
    public void draw(Graphics g) {
        if (running) {
            // color of the apple
            g.setColor(new Color(153, 0, 0));
            // shape of the apple
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            // snake draw
            for (int i = 0; i < bodyParts; i++) {
                // head of the snake
                if (i == 0) {
                    g.setColor(new Color(224, 224, 224));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                    // body of the snake
                } else {
                    g.setColor(new Color(255, 255, 255));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            // Score view
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 20));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("SCORE: " + applesEaten,
                    (SCREEN_WIDTH - metrics.stringWidth("SCORE: " + applesEaten)) / 2,
                    g.getFont().getSize());
        } else {
            gameOver(g);
        }
    }

    public void newApple() {
        // setting the apple coordinates to be random always
        appleX = random.nextInt((SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;

    }

    public void move() {
        // making the body move after the head
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        // setting movement
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
        // if head of snake hits the apple , the snake increase size, score goes up, and we spawn a new apple
        if (((x[0]) == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
        }

    }

    public void checkCollisions() {
        // checks if head collides with body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0]) == y[i]) {
                running = false;
            }
        }
        // check if head collides left border
        if (x[0] < 0) {
            running = false;
        }
        // check if head collides right border
        if (x[0] > SCREEN_WIDTH) {
            running = false;
        }
        // check if head collides top border
        if (y[0] < 0) {
            running = false;
        }
        // check if head collides bottom border
        if (y[0] > SCREEN_HEIGHT) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        // GAME OVER text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("GAME OVER", (SCREEN_WIDTH - metrics1.stringWidth("GAME OVER")) / 2, SCREEN_HEIGHT / 2);

        // SCORE
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("SCORE: " + applesEaten,
                (SCREEN_WIDTH - metrics2.stringWidth("SCORE: " + applesEaten)) / 2,
                g.getFont().getSize());

        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 50));
        FontMetrics metrics3 = getFontMetrics(g.getFont());
        g.drawString(userName, SCREEN_WIDTH - metrics3.stringWidth(userName) / 2, SCREEN_HEIGHT);

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

        // based on key pressed, we only allow 90 degree moves
        public void keyPressed(KeyEvent e) {
            userName += e.getKeyChar();
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                        break;
                    }
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                        break;
                    }
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                        break;
                    }
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                        break;
                    }
            }
        }
    }

    public class MyKeyChar extends KeyAdapter{
        public void userNameSet(KeyEvent e){


            repaint();
        }
    }

}
