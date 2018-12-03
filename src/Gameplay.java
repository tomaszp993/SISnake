import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class Gameplay extends JPanel implements KeyListener, ActionListener {

    Network network = new Network(4, 12, 18, 4);
    // logo
    private ImageIcon titleImage;
    // glowa w lewo
    private ImageIcon leftMouth;
    //glowa w prawo
    private ImageIcon rightMouth;
    //glowa w gore
    private ImageIcon upMouth;
    // glowa w dol
    private ImageIcon downMouth;
    //snake
    private ImageIcon snakeBody;
    //apple
    private ImageIcon appleImage;

    Robot robot;

    private int lenghtOfSnake = 3;
    private int iteration = 100;

    private int scores;

    private int[] snakeXLenght = new int[750];
    private int[] snakeYLenght = new int[750];

    private int[] appleXPositions = new int[35];
    private int[] appleYPositions = new int[24];

    private int moves = 0;

    private Random random = new Random();

    private int appleX = random.nextInt(34);
    private int appleY = random.nextInt(23);
    // Kierunki ruchu
    private boolean left = false;
    private boolean right = false;
    private boolean up = false;
    private boolean down = false;

    //target
    private double[] target = new double[4];
    private double[] output;

    //Czas

    private Timer timer;
    private int delay = 100;

    public Gameplay()
    {
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
        initAppleXPositions();
        initAppleYPositions();
        scores = 0;
        try {
            robot = new Robot();
        } catch (AWTException e1) {
            e1.printStackTrace();
        }

    }

    public void paint(Graphics g)
    {

        // Defaultowa pozycja snake'a
        if(moves == 0)
        {
            snakeXLenght[2] = 50;
            snakeXLenght[1] = 75;
            snakeXLenght[0] = 100;

            snakeYLenght[2] = 100;
            snakeYLenght[1] = 100;
            snakeYLenght[0] = 100;
        }

        // Rysuje obramowanie dla logo gry
        g.setColor(Color.green);
        g.drawRect(24, 10, 851, 55);

        //Rysuj logo
        titleImage = new ImageIcon("images/snaketitle.jpg");
        titleImage.paintIcon(this, g, 25, 11);

        //Rysuj ramke dla okna gry
        g.setColor(Color.green);
        g.drawRect(24, 74, 851, 577);

        // Tlo dla okna gry
        g.setColor(Color.GRAY);
        g.fillRect(25, 75, 850, 575);

        // Wyswietlanie punktow
        g.setColor(Color.green);
        g.setFont(new Font("arial", Font.BOLD, 14));
        g.drawString("Punkty:  " + scores, 780, 30);

        // Wyswietlanie dlugosci
        g.setColor(Color.green);
        g.setFont(new Font("arial", Font.BOLD, 14));
        g.drawString("Długość:  " + lenghtOfSnake, 780, 50);

        // Wyswietlanie iteracje
        g.setColor(Color.green);
        g.setFont(new Font("arial", Font.BOLD, 14));
        g.drawString("Iteracja:  " + iteration, 50, 50);


        rightMouth = new ImageIcon("images/rightmouth.png");
        rightMouth.paintIcon(this, g, snakeXLenght[0], snakeYLenght[0]);

        // Rysowanie glowy weza jako pierwszej + kierunki
        for (int i = 0; i < lenghtOfSnake; i++)
        {
            if(i == 0 && right)
            {
                rightMouth = new ImageIcon("images/rightmouth.png");
                rightMouth.paintIcon(this, g, snakeXLenght[i], snakeYLenght[i]);
            }

            if(i == 0 && left)
            {
                leftMouth = new ImageIcon("images/leftmouth.png");
                leftMouth.paintIcon(this, g, snakeXLenght[i], snakeYLenght[i]);
            }

            if(i == 0 && down)
            {
                downMouth = new ImageIcon("images/downmouth.png");
                downMouth.paintIcon(this, g, snakeXLenght[i], snakeYLenght[i]);
            }

            if(i == 0 && up)
            {
                upMouth = new ImageIcon("images/upmouth.png");
                upMouth.paintIcon(this, g, snakeXLenght[i], snakeYLenght[i]);
            }

            if(i != 0)
            {
                snakeBody = new ImageIcon("images/snakeimage.png");
                snakeBody.paintIcon(this, g, snakeXLenght[i], snakeYLenght[i]);
            }
        }

        appleImage = new ImageIcon("images/enemy.png");

        if((appleXPositions[appleX] == snakeXLenght[0]) && appleYPositions[appleY] == snakeYLenght[0]) {
            lenghtOfSnake++;
            scores++;
            appleX = random.nextInt(34);
            appleY = random.nextInt(23);
        }

        appleImage.paintIcon(this, g, appleXPositions[appleX], appleYPositions[appleY]);

        for(int i = 1; i < lenghtOfSnake; i++)
        {
            if(snakeXLenght[i] == snakeXLenght[0] && snakeYLenght[i] == snakeYLenght[0])
            {
                moves = 0;
                robot.keyPress(KeyEvent.VK_SPACE);
                iteration++;
//                right = left = up = down = false;
//
//                g.setColor(Color.GREEN);
//                g.setFont(new Font("arial", Font.BOLD, 50));
//                g.drawString("Game Over", 320, 300);
//
//                g.setFont(new Font("arial", Font.BOLD, 20));
//                g.drawString("Naciśnij SPACE, aby zrestartować", 290, 340);
            }
        }
        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if(right)
        {
            for(int i = lenghtOfSnake -1; i >= 0; i--)
            {
                snakeYLenght[i+1] = snakeYLenght[i];
            }
            for(int i = lenghtOfSnake; i >= 0; i--)
            {
                if(i == 0)
                {
                    snakeXLenght[i] = snakeXLenght[i] + 25;
                }
                else
                {
                    snakeXLenght[i] = snakeXLenght[i-1];
                }
                if(snakeXLenght[i] > 850)
                {
                    snakeXLenght[i] = 25;
                }
            }
            repaint();
        }
        if(left)
        {
            for(int i = lenghtOfSnake -1; i >= 0; i--)
            {
                snakeYLenght[i+1] = snakeYLenght[i];
            }
            for(int i = lenghtOfSnake; i >= 0; i--)
            {
                if(i == 0)
                {
                    snakeXLenght[i] = snakeXLenght[i] - 25;
                }
                else
                {
                    snakeXLenght[i] = snakeXLenght[i-1];
                }
                if(snakeXLenght[i] < 25)
                {
                    snakeXLenght[i] = 850;
                }
            }
            repaint();
        }
        if(up)
        {
            for(int i = lenghtOfSnake -1; i >= 0; i--)
            {
                snakeXLenght[i+1] = snakeXLenght[i];
            }
            for(int i = lenghtOfSnake; i >= 0; i--)
            {
                if(i == 0)
                {
                    snakeYLenght[i] = snakeYLenght[i] - 25;
                }
                else
                {
                    snakeYLenght[i] = snakeYLenght[i-1];
                }
                if(snakeYLenght[i] < 75)
                {
                    snakeYLenght[i] = 625;
                }
            }
            repaint();
        }
        if(down)
        {
            for(int i = lenghtOfSnake -1; i >= 0; i--)
            {
                snakeXLenght[i+1] = snakeXLenght[i];
            }
            for(int i = lenghtOfSnake; i >= 0; i--)
            {
                if(i == 0)
                {
                    snakeYLenght[i] = snakeYLenght[i] + 25;
                }
                else
                {
                    snakeYLenght[i] = snakeYLenght[i-1];
                }
                if(snakeYLenght[i] > 625)
                {
                    snakeYLenght[i] = 75;
                }
            }
            repaint();
        }
        getTarget(appleXPositions[appleX],appleYPositions[appleY], snakeXLenght[0], snakeYLenght[0]);
        double[] inputs = new double[]{appleXPositions[appleX],appleYPositions[appleY],  snakeXLenght[0], snakeYLenght[0]};
        for(int i = 0; i < iteration; i++) {
            network.train(inputs, target, 0.2);
        }
        output = network.calculate(inputs);
        System.out.println(Arrays.toString(output));
        System.out.println(Arrays.toString(target));

        changeDirect(output);


    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE)
        {
            moves = 0;
            scores = 0;
            lenghtOfSnake = 3;
            repaint();
        }
        if(e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            moves++;
            right = true;
            if(!left)
            {
                right = true;
            }
            else
            {
                right = false;
                left = true;
            }
            up = down = false;
        }

        if(e.getKeyCode() == KeyEvent.VK_LEFT)
        {
            moves++;
            left = true;
            if(!right)
            {
                left = true;
            }
            else
            {
                left = false;
                right = true;
            }
            up = down = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_UP)
        {
            moves++;
            up = true;
            if(!down)
            {
                up = true;
            }
            else
            {
                up = false;
                down = true;
            }
            left = right = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_DOWN)
        {
            moves++;
            down = true;
            if(!up)
            {
                down = true;
            }
            else
            {
                down = false;
                up = true;
            }
            left = right = false;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private void initAppleXPositions() {
        appleXPositions[0] = 25;
        for (int i = 1; i < appleXPositions.length - 1; i++)
        {
            appleXPositions[i] = appleXPositions[i-1] + 25;
        }
    }

    private void initAppleYPositions() {
        appleYPositions[0] = 75;
        for (int i = 1; i < appleYPositions.length - 1; i++)
        {
            appleYPositions[i] = appleYPositions[i-1] + 25;
        }
    }

    public int getAppleX() {
        return appleX;
    }

    public int getAppleY() {
        return appleY;
    }

    private void getTarget(int appleX, int appleY, int snakeX, int snakeY) {

        if(up) {
            if(appleX > snakeX) {
                initTarget("right");
            } else if(appleX < snakeX) {
                initTarget("left");
            } else if(appleX == snakeX) {
                initTarget("up");
            }
        }
        if(down) {
            if(appleX > snakeX) {
                initTarget("right");
            } else if(appleX < snakeX) {
                initTarget("left");
            } else if(appleX == snakeX) {
                initTarget("down");
            }
        }
        if(left) {
            if(appleY > snakeY) {
                initTarget("down");
            } else if(appleY < snakeY) {
                initTarget("up");
            } else if(appleY == snakeY) {
                initTarget("left");
            }
        }
        if(right) {
            if(appleY > snakeY) {
                initTarget("down");
            } else if(appleY < snakeY) {
                initTarget("up");
            } else if(appleY == snakeY) {
                initTarget("right");
            }
        }

    }

    private void initTarget(String direction) {
        switch(direction){
            case "up": {
                target[0] = 1;
                target[1] = 0;
                target[2] = 0;
                target[3] = 0;
                break;
            }
            case "down": {
                target[0] = 0;
                target[1] = 1;
                target[2] = 0;
                target[3] = 0;
                break;
            }
            case "right": {
                target[0] = 0;
                target[1] = 0;
                target[2] = 1;
                target[3] = 0;
                break;
            }
            case "left": {
                target[0] = 0;
                target[1] = 0;
                target[2] = 0;
                target[3] = 1;
                break;
            }
            default: {
                System.out.println("Switch sie wywalil");
            }

        }

    }

    private void changeDirect(double[] target) {
        if(target[0] > 0.7) {
            robot.keyPress(KeyEvent.VK_UP);
        }
        else if(target[1] > 0.7) {
            robot.keyPress(KeyEvent.VK_DOWN);
        }
        else if(target[2] > 0.7) {
            robot.keyPress(KeyEvent.VK_RIGHT);
        }
        else if(target[3] > 0.7) {
            robot.keyPress(KeyEvent.VK_LEFT);
        }
    }

}
