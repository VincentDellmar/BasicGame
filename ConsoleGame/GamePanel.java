import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

import javax.swing.JPanel;
import java.util.ArrayList;
public class GamePanel extends JPanel implements ActionListener{

    private static final long serialVersionUID = 1L;
    
    static final int WIDTH = 300;
    static final int HEIGHT = 1000;
    static final int UNIT_SIZE = 100;

    // hold x and y coordinates for body parts of the snake
    int x = (200);
    int y = (500);
    ArrayList<Integer> obstacleX = new ArrayList<Integer>();
    ArrayList<Integer> obstacleY = new ArrayList<Integer>();
    int waitCount = 0;
    int score  = 0;
    int speed = 150;
    boolean running = false;
    Random random;
    Timer timer;
    
    public GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.DARK_GRAY);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        play();
    }    
    
    public void play() {
        running = true;
        
        timer = new Timer(speed, this);
        timer.start();    
    }
    
    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        draw(graphics);
    }
    
    public void draw(Graphics graphics) {
        
        if (running) {
            graphics.setColor(Color.white);
            graphics.fillRect(x, y, UNIT_SIZE, UNIT_SIZE);
            for (int i = 0; i < obstacleX.size(); i++) {
                   int obsX = obstacleX.get(i);
                       obstacleY.set(i,obstacleY.get(i) + UNIT_SIZE);
                           
                           
                       if (obstacleY.get(i) > HEIGHT) {
                           obstacleX.remove(i);
                           obstacleY.remove(i);
                       } else {
                           graphics.setColor(Color.red);
                   graphics.fillRect(obstacleX.get(i), obstacleY.get(i), UNIT_SIZE, UNIT_SIZE);
                       }
                }
                
            graphics.setColor(Color.white);
            graphics.setFont(new Font("Sans serif", Font.ROMAN_BASELINE, 25));
            FontMetrics metrics = getFontMetrics(graphics.getFont());
            graphics.drawString("Score: " + score, (WIDTH - metrics.stringWidth("Score: " + score)) / 2, graphics.getFont().getSize());
        } else {
            gameOver(graphics);
        }
    }
    
    public void addObstacle() {
        obstacleX.add((random.nextInt(3)*UNIT_SIZE));
        obstacleY.add(0);
    }
    
    public void checkHit() {
        
        // check if head run into walls
        if (x < 0 || x > WIDTH || y < 0 || y > HEIGHT) {
            running = false;
        }
        for (int i = 0; i < obstacleX.size(); i++) {
            int obsX = obstacleX.get(i);
            int obsY = obstacleY.get(i);
            if (Math.abs(x - obsX) < UNIT_SIZE && Math.abs(y - obsY) < UNIT_SIZE) {
                running = false;
            }
        }
        if(!running) {
            timer.stop();
        }
    }
    
    public void gameOver(Graphics graphics) {
        graphics.setColor(Color.red);
        graphics.setFont(new Font("Sans serif", Font.ROMAN_BASELINE, 50));
        FontMetrics metrics = getFontMetrics(graphics.getFont());
        graphics.drawString("Game Over", (WIDTH - metrics.stringWidth("Game Over")) / 2, HEIGHT / 2);
        
        graphics.setColor(Color.white);
        graphics.setFont(new Font("Sans serif", Font.ROMAN_BASELINE, 25));
        metrics = getFontMetrics(graphics.getFont());
        graphics.drawString("Score: " + score, (WIDTH - metrics.stringWidth("Score: " + score)) / 2, graphics.getFont().getSize());

    }
    
    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (running) {
                    waitCount++;
                    if (waitCount >= 10) {
                        waitCount = 0;
                        addObstacle();
                        score += 100;
                        //speed -= 1;
                        //timer = new Timer(speed, this);
                        //timer.start();
                    };
            checkHit();
        }
        repaint();
    }
    
    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_A:
                            if (x - UNIT_SIZE >= 0) {x -= UNIT_SIZE;}
                            
                    break;
                case KeyEvent.VK_D:
                        if (x + UNIT_SIZE < WIDTH) {x += UNIT_SIZE;}
                    break;
            }
        }
    }
}
