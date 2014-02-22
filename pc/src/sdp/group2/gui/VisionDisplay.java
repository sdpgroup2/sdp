package sdp.group2.gui;

import sdp.group2.util.Debug;
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

    private JLabel imageLabel = new JLabel();


    /**
     * Initialise a window frame. PLEASE EXCUSE THIS AWFUL FUNCTION. I'll clean it up later.
     *
     * @param frameSize size of the video feed frames
     */
    public void initWindow(Dimension frameSize) {
        JPanel contentPanel = new JPanel();
        JFrame windowFrame = new JFrame("Vision");

        // Window container
        windowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        windowFrame.addWindowListener(this);
        windowFrame.setVisible(true);
        windowFrame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        // Main content container
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.LINE_AXIS));

        // Image display
        imageLabel.setMinimumSize(frameSize);
        imageLabel.setPreferredSize(frameSize);
        imageLabel.setMaximumSize(frameSize);
        contentPanel.add(imageLabel);

        windowFrame.setContentPane(contentPanel);
    }

    /**
     * Updates the image and redraws it.
     *
     * @param image  image to be drawn
     * @param ball   ball on the pitch
     * @param robots robots on the pitch
     */
    public synchronized void redraw(BufferedImage image, Ball ball, List<Robot> robots) {
        for (Robot robot : robots) {
            // Bounding rect
            Debug.drawRect(image, robot.getBoundingRect(), RECT_COLOR);
            // Facing vector
            Debug.drawVector(image, robot.getPosition(), robot.getFacingVector(), DIRECTION_COLOR);
            // Vector to the ball
            Debug.drawVector(image, robot.getPosition(), robot.vectorTo(ball), CONNECT_COLOR);
        }

        // Draw the ball rect
        // Debug.drawRect(image, ball.getBoundingRect(), RECT_COLOR);

        // Update the label with the new image
        imageLabel.setIcon(new ImageIcon(image));
    }

    /**
     * Initialises new VisionDisplay
     *
     * @param frameSize size of the video feed frames.
     */
    public VisionDisplay(Dimension frameSize) {
        super();
        initWindow(frameSize);
    }

    /**
     * Initialises new VisionDisplay
     *
     * @param width width of the frame
     * @param height height of the frame
     */
    public VisionDisplay(int width, int height) {
        this(new Dimension(width, height));
    }

}
