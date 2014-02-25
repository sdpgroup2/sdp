package sdp.group2.simulator;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import sdp.group2.world.IPitch;

/** TODO: draw pitch and zones
 * 	TODO: highlight active zone
 *  TODO: draw robot and its direction
 *  TODO: draw ball and its direction
 *  TODO: account for goals, add score table, and restart procedure
 *  TODO: add pause/resume
 */

public class Visualizator extends WindowAdapter {
	
    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 768;
    
    private IPitch pitch;
    
    private JFrame windowFrame;
    private Dimension frameSize;
    private JLabel imageLabel = new JLabel();

    private Image currentImage;
    private BufferedImage bufImage;

    public Visualizator(IPitch pitch) {
    	super();
    	this.pitch = pitch;
		initWindow();
    }
    
    public void initWindow() {
	    JPanel contentPanel;

        windowFrame = new JFrame("Match Simulator");
        windowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        windowFrame.addWindowListener(this);
        windowFrame.setVisible(true);
        windowFrame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        // Main container
        contentPanel = new JPanel();
        contentPanel.setBorder(new EmptyBorder(10,10,10,10));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.LINE_AXIS));

        // Image
        imageLabel.setMinimumSize(frameSize);
        imageLabel.setPreferredSize(frameSize);
        imageLabel.setMaximumSize(frameSize);
        contentPanel.add(imageLabel);

        // Sidebar
        JPanel controlPanel = new JPanel();
        controlPanel.setBorder(new EmptyBorder(10,10,10,10));
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.PAGE_AXIS));
        contentPanel.add(controlPanel);

        windowFrame.setContentPane(contentPanel);
    }

}
