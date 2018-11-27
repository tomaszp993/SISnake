import sun.nio.ch.Net;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        Gameplay gameplay = new Gameplay();

        frame.setBounds(10, 10, 900, 700);
        frame.setBackground(Color.DARK_GRAY);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(gameplay);

        Network net = new Network(4, 8, 3, 1);
        double[] inputs = new double[]{45, 78, 85, 124};
        double[] targets = new double[]{1};

        for(int i = 0; i < 100; i++) {
            net.train(inputs, targets, 0.3);
        }
        double[] o = net.calculate(inputs);
        System.out.println(Arrays.toString(o));
    }
}
