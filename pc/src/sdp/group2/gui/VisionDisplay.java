package sdp.group2.gui;

import sdp.group2.geometry.Rect;
import sdp.group2.geometry.Vector;
import sdp.group2.util.Debug;
import sdp.group2.vision.HSBColor;
import sdp.group2.world.Ball;
import sdp.group2.world.Robot;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.image.BufferedImage;
import java.util.List;


public class VisionDisplay extends WindowAdapter {
    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 768;

    private static final Color RECT_COLOR = Color.RED;
    private static final Color DIRECTION_COLOR = Color.CYAN;
    private static final Color CONNECT_COLOR = Color.MAGENTA;

    private JFrame windowFrame;
    private Dimension frameSize;
    private JLabel imageLabel = new JLabel();


    /**
     * Initialise a window frame. PLEASE EXCUSE THIS AWFUL FUNCTION. I'll clean it up later.
     */
    public void initWindow() {
        JPanel contentPanel;

        windowFrame = new JFrame("Vision");
        windowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        windowFrame.addWindowListener(this);
        windowFrame.setVisible(true);
        windowFrame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        // Main container
        contentPanel = new JPanel();
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.LINE_AXIS));

        // Image
        imageLabel.setMinimumSize(frameSize);
        imageLabel.setPreferredSize(frameSize);
        imageLabel.setMaximumSize(frameSize);
        contentPanel.add(imageLabel);

        windowFrame.setContentPane(contentPanel);
    }

    public void update(BufferedImage image, Ball ball, List<Robot> robots) {
        for (Robot robot : robots) {
            // Bounding rect
            Debug.drawRect(image, robot.getBoundingRect(), RECT_COLOR);
            // Direction vector
            Debug.drawVector(image, robot.getPosition(), robot.getDirection(), DIRECTION_COLOR);
            // Vector to the ball
            Debug.drawVector(image, robot.getPosition(), robot.vectorTo(ball), CONNECT_COLOR);
        }

        // Draw the ball rect
        // Debug.drawRect(image, ball.getBoundingRect(), RECT_COLOR);

        showImage(image);
    }

    private void showImage(BufferedImage image) {
        imageLabel.setIcon(new ImageIcon(image));
    }

    public VisionDisplay(Dimension frameSize) {
        super();
        this.frameSize = frameSize;
        // Init GUI
        initWindow();
    }

}
