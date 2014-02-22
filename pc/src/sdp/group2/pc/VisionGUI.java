package sdp.group2.pc;

import sdp.group2.geometry.Rect;
import sdp.group2.geometry.VecI;
import sdp.group2.geometry.Vector;
import sdp.group2.gui.ColorChecker;
import sdp.group2.gui.HSBPanel;
import sdp.group2.util.Debug;
import sdp.group2.vision.HSBColor;
import sdp.group2.vision.Image;
import sdp.group2.vision.VisionService;
import sdp.group2.vision.VisionServiceCallback;
import sdp.group2.vision.clusters.BallCluster;
import sdp.group2.vision.clusters.HSBCluster;
import sdp.group2.vision.clusters.RobotBaseCluster;
import sdp.group2.world.Ball;
import sdp.group2.world.Pitch;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.image.BufferedImage;
import java.util.List;

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


public class VisionGUI extends WindowAdapter implements VisionServiceCallback {
    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 768;

    private VisionService visionService;

    private JFrame windowFrame;
    private Dimension frameSize;
    private JLabel imageLabel = new JLabel();
    private ColorChecker colorChecker;

    private Image currentImage;
    private BufferedImage bufImage;
    private int[] preColorArray;
    private int[] postColorArray;
    private Pitch pitch;

    public static void main(String[] args) {
    	new VisionGUI();
    }

    /**
     * Initialise a window frame. PLEASE EXCUSE THIS AWFUL FUNCTION. I'll clean it up later.
     */
    public void initWindow() {
    	final HSBCluster[] clusters = visionService.getClusters();
	    JPanel contentPanel;
	    final JList<HSBCluster> clusterList = new JList<HSBCluster>(clusters);
        final HSBPanel minHSBPanel = new HSBPanel("Min color");
        final HSBPanel maxHSBPanel = new HSBPanel("Max color");
	    colorChecker = new ColorChecker();

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
    public void onFrameGrabbed(Image currentImage) {
        bufImage = currentImage.getBufferedImage();
    }

    @Override
    public void onPreparationFrame() {
        showImage(bufImage);
    }

    @Override
    public void onImageFiltered(Image currentImage) {
        if (Debug.VISION_NORMALIZE_IMAGE) {
            for (int i=0; i < preColorArray.length; i++) {
                postColorArray[i] = currentImage.getRGB(i);
            }
            currentImage.setRgbArray(postColorArray);
        }
        Point mouse = windowFrame.getMousePosition();
        if (mouse != null) {
            int imgX = mouse.x - 14;
            int imgY = mouse.y - 160;
            if (0 <= imgX && imgX < frameSize.width &&
                0 <= imgY && imgY < frameSize.height) {
                    colorChecker.updateColor(currentImage.getRGB(imgX, imgY));
                    currentImage.setColor(imgX, imgY, Color.WHITE);
            }
        }
    }

    @Override
    public void onImageProcessed(Image currentImage, BallCluster ballCluster,
                                 RobotBaseCluster robotBaseCluster) {
        bufImage = currentImage.getBufferedImage();

        for (HSBCluster cluster: visionService.getClusters()) {
            for (VecI pixel: cluster.getPixels()) {
                Debug.drawPixel(bufImage, pixel.x, pixel.y, cluster.debugColor);
            }
            for (Rect rect: cluster.getImportantRects()) {
                Debug.drawRect(bufImage, rect, cluster.debugColor);
            }
        }

    	showImage(bufImage);
    }

    private void showImage(BufferedImage image) {
        imageLabel.setIcon(new ImageIcon(image));
    }

	public VisionGUI() {
		super();
		// Start the vision system
		this.visionService = new VisionService(5, this);
		this.frameSize = visionService.getSize();
		this.postColorArray = new int[frameSize.width * frameSize.height];

		// Init GUI
		initWindow();
		visionService.start();
	}

	@Override
	public boolean onPreparationReady(Image image,
			BallCluster ballCluster, RobotBaseCluster robotBaseCluster,
			Rect pitchRect, Rect[] sectionRects) {
		return true;
	}

	@Override
	public void onExceptionThrown(Exception e) {
	}

}
