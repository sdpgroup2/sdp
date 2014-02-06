package group2.sdp.pc;

import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.gui.ColorChecker;
import group2.sdp.pc.gui.HSBPanel;
import group2.sdp.pc.vision.HSBColor;
import group2.sdp.pc.vision.VisionSystemCallback;
import group2.sdp.pc.vision.clusters.BallCluster;
import group2.sdp.pc.vision.clusters.BlueRobotCluster;
import group2.sdp.pc.vision.clusters.HSBCluster;
import group2.sdp.pc.vision.clusters.PitchLines;
import group2.sdp.pc.vision.clusters.PitchSection;
import group2.sdp.pc.vision.clusters.YellowRobotCluster;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * The main class for the vision system. At the moment, it is runnable on its
 * own but in future, it may be created via a separate main class.
 * @author Paul Harris
 *
 */
public class VisionGUI extends WindowAdapter implements VisionSystemCallback {
	    private static final int WINDOW_WIDTH = 1024;
	    private static final int WINDOW_HEIGHT = 768;

	    private Dimension frameSize;

	    private BufferedImage currentImage;

	    //GUI
	    private JFrame windowFrame;
	    private JPanel contentPanel;
	    private JLabel imageLabel;
	    private JList<HSBCluster> clusterList;
	    private HSBPanel minHSBPanel;
	    private HSBPanel maxHSBPanel;
	    private ColorChecker colorChecker;

	    // Stores colors for the current frame
	    private int[] colorArray;
	    private HSBColor[] hsbArray;

		// Clusters
		private BallCluster ballCluster = new BallCluster("Ball");
		private BlueRobotCluster blueRobotCluster = new BlueRobotCluster("Blue robots");
		private YellowRobotCluster yellowRobotCluster = new YellowRobotCluster("Yellow robots");
		private PitchSection pitchSectionCluster = new PitchSection("Pitch sections");
		private PitchLines pitchLinesCluster = new PitchLines("Pitch lines");
		private HSBCluster[] clusters = new HSBCluster[] {
			ballCluster,
			blueRobotCluster,
			yellowRobotCluster,
//			pitchSectionCluster,
//			pitchLinesCluster
		};

    public static void main(String[] args) {
    	new VisionGUI();
    }

    /**
     * Initialise a window frame. PLEASE EXCUSE THIS AWFUL FUNCTION. I'll clean it up later.
     */
    public void initWindow() {
        windowFrame = new JFrame("Vision");
        windowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        windowFrame.addWindowListener(this);
        windowFrame.setVisible(true);
        windowFrame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        // Main container
        contentPanel = new JPanel();
        contentPanel.setBorder(new EmptyBorder(10,10,10,10));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.LINE_AXIS));

        // Color checker
        colorChecker = new ColorChecker();

        // Image
        imageLabel = new JLabel();
        imageLabel.setMinimumSize(frameSize);
        imageLabel.setPreferredSize(frameSize);
        imageLabel.setMaximumSize(frameSize);
        imageLabel.addMouseMotionListener(new MouseMotionListener() {
            public void mouseDragged(MouseEvent e) {
            }

            public void mouseMoved(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                try {
                    colorChecker.updateColor(currentImage.getRGB(x, y));
                } catch (ArrayIndexOutOfBoundsException ex) {

                }
            }
        });
        contentPanel.add(imageLabel);

        // Sidebar
        JPanel controlPanel = new JPanel();
        controlPanel.setBorder(new EmptyBorder(10,10,10,10));
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.PAGE_AXIS));
        contentPanel.add(controlPanel);

        // Cluster list
        JPanel listPanel = new JPanel();
        clusterList = new JList<HSBCluster>(clusters);
        clusterList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        clusterList.setLayoutOrientation(JList.VERTICAL);
        clusterList.setVisibleRowCount(-1);
        clusterList.setPreferredSize(new Dimension(200, 100));
        clusterList.setBorder(new LineBorder(Color.black));
        clusterList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() == false) {
                    int index = clusterList.getSelectedIndex();
                    if (index >= 0) {
                        HSBCluster cluster = clusters[index];
                        minHSBPanel.setValue(cluster.getMinColor());
                        maxHSBPanel.setValue(cluster.getMaxColor());
                    }
                }
            }
        });
        listPanel.add(clusterList);
        controlPanel.add(listPanel);

        // Sliders
        minHSBPanel = new HSBPanel("Min color");
        maxHSBPanel = new HSBPanel("Max color");
        controlPanel.add(minHSBPanel);
        controlPanel.add(maxHSBPanel);

        // Update button
        JButton button = new JButton("Update");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateButton();
            }
        });
        controlPanel.add(button);
        controlPanel.add(colorChecker);

        // Debug checkboxes
        final JCheckBox fillBox = new JCheckBox("Fill clusters");
        fillBox.setSelected(Debug.VISION_FILL_PIXELS);
        fillBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                Debug.VISION_FILL_PIXELS = ((JCheckBox) e.getSource()).isSelected();
            }
        });
        final JCheckBox rectBox = new JCheckBox("Draw rectangles");
        rectBox.setSelected(Debug.VISION_DRAW_BOUNDS);
        rectBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                Debug.VISION_DRAW_BOUNDS = ((JCheckBox) e.getSource()).isSelected();
            }
        });
        controlPanel.add(fillBox);
        controlPanel.add(rectBox);

        windowFrame.setContentPane(contentPanel);
    }


		/**
		 * Processes an image to find positions of all the game objects.
		 *
		 * @param image
		 *            - The current frame of video.
		 */
	public void processImage(BufferedImage currentImage) {
		this.currentImage = currentImage;
		// Clear all clusters.
		for (HSBCluster cluster : clusters) {
			cluster.clear();
		}
		// Loop through pixels.
		for (int x = 0; x < frameSize.width; x++) {
			for (int y = 0; y < frameSize.height; y++) {
				int index = y * frameSize.width + x;
				HSBColor color = hsbArray[index].set(colorArray[index]);
				// Test the pixel for each of the clusters
				for (HSBCluster cluster : clusters) {
					boolean matched = cluster.testPixel(x, y, color);
					if (matched) {
						Debug.drawPixel(currentImage, x, y, cluster.debugColor);
					}
				}
			}
		}
		for (HSBCluster cluster : clusters) {
			for (Rect rect : cluster.getImportantRects()) {
				Debug.drawRect(currentImage, rect, cluster.debugColor);
			}
		}
		this.imageLabel.setIcon(new ImageIcon(currentImage));
		/*
		 * VecI corner = pitchLinesCluster.getCorner(Down, Left); if(corner !=
		 * null) Debug.drawTestPixel(currentImage, corner.x, corner.y,
		 * Color.white); else System.out.println("Cannot find corner");
		 */
	}

	public void updateButton() {
        int index = clusterList.getSelectedIndex();
        if (index >= 0) {
            HSBCluster cluster = clusters[index];
            cluster.setMinColor(minHSBPanel.getValue());
            cluster.setMaxColor(maxHSBPanel.getValue());
        }
	}

	public VisionGUI() {
		super();
		new VisionSystem(this);
	}

}
