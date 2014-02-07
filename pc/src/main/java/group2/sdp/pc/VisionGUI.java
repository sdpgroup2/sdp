package group2.sdp.pc;

import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.geom.VecI;
import group2.sdp.pc.gui.ColorChecker;
import group2.sdp.pc.gui.HSBPanel;
import group2.sdp.pc.vision.HSBColor;
import group2.sdp.pc.vision.SkyCam;
import group2.sdp.pc.vision.VisionSystemCallback;
import group2.sdp.pc.vision.clusters.BallCluster;
import group2.sdp.pc.vision.clusters.BlueRobotCluster;
import group2.sdp.pc.vision.clusters.HSBCluster;
import group2.sdp.pc.vision.clusters.YellowRobotCluster;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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


public class VisionGUI extends WindowAdapter implements VisionSystemCallback {
    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 768;

    private SkyCam skyCam;

    private JFrame windowFrame;
    private Dimension frameSize;
    private JLabel imageLabel = new JLabel();
    private ColorChecker colorChecker;

    private BufferedImage currentImage;
    private int[] postColorArray;

	// Clusters
	private BallCluster ballCluster = new BallCluster("Ball");
	private BlueRobotCluster blueRobotCluster = new BlueRobotCluster("Blue robots");
	private YellowRobotCluster yellowRobotCluster = new YellowRobotCluster("Yellow robots");
//	private PitchSection pitchSectionCluster = new PitchSection("Pitch sections");
//	private PitchLines pitchLinesCluster = new PitchLines("Pitch lines");
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
	    JPanel contentPanel;
	    final JList<HSBCluster> clusterList = new JList<HSBCluster>(clusters);
        final HSBPanel minHSBPanel = new HSBPanel("Min color");
        final HSBPanel maxHSBPanel = new HSBPanel("Max color");
	    colorChecker = new ColorChecker();;

        windowFrame = new JFrame("Vision");
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

        // Cluster list
        JPanel listPanel = new JPanel();
        clusterList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        clusterList.setLayoutOrientation(JList.VERTICAL);
        clusterList.setVisibleRowCount(-1);
        clusterList.setPreferredSize(new Dimension(200, 100));
        clusterList.setBorder(new LineBorder(Color.black));
        clusterList.addListSelectionListener(new ListSelectionListener() {
            @Override
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
        controlPanel.add(minHSBPanel);
        controlPanel.add(maxHSBPanel);

        // Update button
        JButton button = new JButton("Update");
        button.addActionListener(new ActionListener() {
            @Override
			public void actionPerformed(ActionEvent e) {
                int index = clusterList.getSelectedIndex();
                if (index >= 0) {
                    HSBCluster cluster = clusters[index];
                    cluster.setMinColor(minHSBPanel.getValue());
                    cluster.setMaxColor(maxHSBPanel.getValue());
                }
            }
        });
        controlPanel.add(button);
        controlPanel.add(colorChecker);

        // Debug checkboxes
        final JCheckBox fillBox = new JCheckBox("Fill clusters");
        fillBox.setSelected(Debug.VISION_FILL_PIXELS);
        fillBox.addItemListener(new ItemListener() {
            @Override
			public void itemStateChanged(ItemEvent e) {
                Debug.VISION_FILL_PIXELS = ((JCheckBox) e.getSource()).isSelected();
            }
        });
        final JCheckBox rectBox = new JCheckBox("Draw rectangles");
        rectBox.setSelected(Debug.VISION_DRAW_BOUNDS);
        rectBox.addItemListener(new ItemListener() {
            @Override
			public void itemStateChanged(ItemEvent e) {
                Debug.VISION_DRAW_BOUNDS = ((JCheckBox) e.getSource()).isSelected();
            }
        });
        final JCheckBox normBox = new JCheckBox("Normalize image");
		normBox.setSelected(Debug.VISION_NORMALIZE_IMAGE);
		normBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				Debug.VISION_NORMALIZE_IMAGE = ((JCheckBox) e.getSource()).isSelected();
			}
		});
        controlPanel.add(fillBox);
        controlPanel.add(rectBox);
        controlPanel.add(normBox);

        windowFrame.setContentPane(contentPanel);
    }

    @Override
    public void onFrameGrabbed(BufferedImage image) {
        currentImage = image;
    }

    @Override
    public void onPreparationFrame() {
        showImage(currentImage);
    }

    @Override
    public void onImageFiltered(HSBColor[] hsbArray) {
        if (Debug.VISION_NORMALIZE_IMAGE) {
            for (int i=0; i<hsbArray.length; i++) {
                postColorArray[i] = hsbArray[i].getRGB();
            }
            currentImage.setRGB(0, 0, frameSize.width, frameSize.height,
                postColorArray, 0, frameSize.width);
        }
        Point mouse = windowFrame.getMousePosition();
        if (mouse != null) {
            int imgX = mouse.x - 14;
            int imgY = mouse.y - 160;
            if (0 <= imgX && imgX < frameSize.width &&
                0 <= imgY && imgY < frameSize.height) {
                    colorChecker.updateColor(currentImage.getRGB(imgX, imgY));
                    currentImage.setRGB(imgX, imgY, Color.white.getRGB());
            }
        }
    }
    
    @Override
    public void onImageProcessed() {
    	
    }

	@Override
	public void processImage(HSBColor[] hsbArray) {
		// Clear all clusters.
		for (HSBCluster cluster : clusters) {
			cluster.clear();
		}
		// Loop through pixels.
		for (int x = 0; x < frameSize.width; x++) {
			for (int y = 0; y < frameSize.height; y++) {
				int index = y * frameSize.width + x;
				HSBColor color = hsbArray[index];
				// Test the pixel for each of the clusters
				for (HSBCluster cluster : clusters) {
					cluster.testPixel(x, y, color);
				}
			}
		}
        for (HSBCluster cluster: clusters) {
            cluster.getImportantRects();
        }
        for (HSBCluster cluster: clusters) {
            for (VecI pixel: cluster.getPixels()) {
                Debug.drawPixel(currentImage, pixel.x, pixel.y, cluster.debugColor);
            }
            for (Rect rect: cluster.getImportantRects()) {
                Debug.drawRect(currentImage, rect, cluster.debugColor);
            }
        }
        showImage(currentImage);
	}

    private void showImage(BufferedImage image) {
        imageLabel.setIcon(new ImageIcon(image));
    }

	public VisionGUI() {
		super();
		// Start the vision system
		this.skyCam = new SkyCam(5, this);
		this.frameSize = skyCam.getSize();
		this.postColorArray = new int[frameSize.width * frameSize.height];

		// Init GUI
		initWindow();
		skyCam.start();
	}
}
