package com.example.snakegame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    // set screen width & height
    final static int SCREEN_WIDTH = 600;
    final static int SCREEN_HEIGHT = 600;
    // objects size, you can increase it and the game will be bigger or shorter
    final static int UNIT_SIZE = 25;
    // calculate the amount of objects you can fit in the screen
    final static int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    // delay for timer,
    // note: higher the timer, slower the game will be
    final static int DELAY = 75;
    // these arrays will hold the coordinates of the body parts of the snake
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    //initial body parts of the snake
    int bodyParts = 6;
    // apples eaten
    int applesEaten = 0;
    // X and Y coordinates of apples (Random)
    int appleX;
    int appleY;
    // X and Y coordinates of enemy (Random)
    int enemyX;
    int enemyY;
    /**
     * directions:
     * R = Right
     * L = Left
     * U = Up
     * D = Down
     * */
    char direction = 'R';
    // if the snake is running
    boolean running = false;
    // a timer
    Timer timer;
    // instance of random
    Random random;


    // constructor
    GamePanel(){
        random = new Random();
        // set size and background
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        // focusability
        this.setFocusable(true);
        //key listener
        this.addKeyListener(new MyKeyAdapter());
        // start the game
        startGame();
    }
    // this method will start the game
    public void startGame(){
        // create a new apple
        newApple();
        newEnemy();
        running = true;
        // how fast the game is going
        // you use "this" because you are using the actionListener
        timer = new Timer(DELAY, this);
        // this start the timer
        timer.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        if(running){
            // draw lines so it can be more visible and easy to play
            for (int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, i * SCREEN_HEIGHT);
                g.drawLine(0,  i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }
            // draw apple
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
            // draw enemy
            g.setColor(Color.white);
            g.fillOval(enemyX, enemyY, UNIT_SIZE, UNIT_SIZE);

            // draw snake
            // go to all the parts of the snake
            for(int i = 0; i < bodyParts; i++){
                if(i == 0){
                    g.setColor(Color.white);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else{
                    //g.setColor(Color.gray);
                    
                    // you can make the snake in random colors using this
                    g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            // draw the current score
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            // to center the text
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
        }
        else{
            gameOver(g);
        }
    }
    public void newApple(){
        // random apple
        appleX = random.nextInt((int)(SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;

    }
    // this will show a new enemy
    public void newEnemy(){
        enemyX = random.nextInt((int)(SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        enemyY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;

        // for enemy to not spawn in your body
        while(enemyX != x[0] && enemyY != y[0]){
            newEnemy();
        }
    }
    public void move(){
        // go to all the body parts of the snake
        for(int i = bodyParts; i > 0; i--){
            // change the coordinate over 1 spot
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        // change snake direction
        // starts with the coordinates of the head of the snake
        // - is down and left. + is up and right
        switch (direction){
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
    // apple is eaten
    public void checkApple(){
        if((x[0] == appleX) && (y[0] == appleY)){
            bodyParts++;
            applesEaten++;
            newApple();

            // spawn an enemy after every eaten apple after 3 points
            if(applesEaten > 3){
                newEnemy();
            }
        }
    }
    public void checkEnemy(){
        if((x[0] == enemyX) && (y[0] == enemyY)){
            running = false;
        }
    }
    public void checkCollisions(){
        // check if head collies with body
        for (int i = bodyParts; i > 0 ; i--) {
            if((x[0] == x[i]) && (y[0] == y[i])){
                running = false;
            }
        }
        // check if head touches left, right, top, or bottom border
        if(x[0] < 0 || x[0] > SCREEN_WIDTH || y[0] < 0 || y[0] > SCREEN_HEIGHT){
            running = false;
        }
        if(!running){
            timer.stop();
        }

    }
    // game over method
    public void gameOver(Graphics g){
        //display score in game over
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        // to center the text
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());

        //Game Over text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        // to center the text
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);


    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){
            move();
            checkApple();
            checkEnemy();
            checkCollisions();
        }
        repaint();
    }

    // inner class
    // get key input
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()){
                // limit the way you move
                // left
                case KeyEvent.VK_LEFT:
                    if(direction != 'R'){
                        direction = 'L';
                    }
                    break;
                // right
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L'){
                        direction = 'R';
                    }
                    break;
                // up
                case KeyEvent.VK_UP:
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    break;
                // down
                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
