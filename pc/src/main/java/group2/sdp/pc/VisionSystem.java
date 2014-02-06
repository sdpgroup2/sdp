package group2.sdp.pc;

import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.gui.ColorChecker;
import group2.sdp.pc.gui.HSBPanel;
import group2.sdp.pc.vision.HSBColor;
import group2.sdp.pc.vision.SkyCam;
import group2.sdp.pc.vision.clusters.BallCluster;
import group2.sdp.pc.vision.clusters.BlueRobotCluster;
import group2.sdp.pc.vision.clusters.HSBCluster;
import group2.sdp.pc.vision.clusters.PitchLines;
import group2.sdp.pc.vision.clusters.PitchSection;
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

import au.edu.jcu.v4l4j.CaptureCallback;
import au.edu.jcu.v4l4j.VideoFrame;
import au.edu.jcu.v4l4j.exceptions.V4L4JException;


/**
 * The main class for the vision system. At the moment, it is runnable on its
 * own but in future, it may be created via a separate main class.
 * @author Paul Harris
 *
 */
public class VisionSystem extends WindowAdapter implements CaptureCallback {
	
	private static final int WINDOW_WIDTH = 1024;
	private static final int WINDOW_HEIGHT = 768;
	private static final int PREPARE_FRAMES = 5;
	
	private enum VisionState {
		Preparation,
		Processing
	}
	
	// Pre-processing
	private VisionState state = VisionState.Preparation;
	private int preparationFrames = 0;
	private float meanSat = 0;
	private float meanBright = 0;
	
	
	//GUI
	private JFrame windowFrame;
	private JPanel contentPanel;
	private JLabel imageLabel;
	private BufferedImage currentImage;
	private JList<HSBCluster> clusterList;
	private HSBPanel minHSBPanel;
	private HSBPanel maxHSBPanel;
	private ColorChecker colorChecker;
	
	private SkyCam skyCam;
	private Dimension frameSize;
	
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
			//pitchSectionCluster,
			//pitchLinesCluster
	};
	
	private Timer timer = new Timer(10);
	
	// Stores colors for the current frame
	private int[] colorArray;
	private HSBColor[] hsbArray;
	private int[] outputArray;
	
	
	public static void main(String[] args) {
		new VisionSystem();
	}
	
	
	public VisionSystem() {
		initCamera();
		initWindow();
		initColorArrays();
		
		skyCam.startVision(this);
	}
	
	/**
	 * Initialises the camera and creates a colorArray which is going to contain
	 * RGB values of the pitch
	 */
	public void initCamera() {
		skyCam = new SkyCam();
		frameSize = skyCam.getSize();
	}
	
	public void initColorArrays() {
		colorArray = new int[frameSize.width * frameSize.height];
		hsbArray = new HSBColor[frameSize.width * frameSize.height];
		for (int i=0; i<hsbArray.length; i++) {
			hsbArray[i] = new HSBColor();
		}
		outputArray = new int[frameSize.width * frameSize.height];
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
		final JCheckBox normBox = new JCheckBox("Normalize image");
		normBox.setSelected(Debug.VISION_NORMALIZE_IMAGE);
		normBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				Debug.VISION_NORMALIZE_IMAGE = ((JCheckBox) e.getSource()).isSelected();
			}
		});
		controlPanel.add(fillBox);
		controlPanel.add(rectBox);
		controlPanel.add(normBox);
		
		windowFrame.setContentPane(contentPanel);
		
	}
	
	public void updateButton() {
		int index = clusterList.getSelectedIndex();
		if (index >= 0) {
			HSBCluster cluster = clusters[index];
			cluster.setMinColor(minHSBPanel.getValue());
			cluster.setMaxColor(maxHSBPanel.getValue());
		}
	}

	/** 
	 * Callback that gets the next frame of the video.
	 * @param frame - The frame that was captured.
	 */
	public void nextFrame(VideoFrame frame) {
		timer.tick(25); // Prints the framerate every 25 frames
		currentImage = frame.getBufferedImage();
		// Read image into array
		currentImage.getRGB(0, 0, frameSize.width, frameSize.height, colorArray, 0, frameSize.width);
		switch (state) {
		case Preparation: 	prepareVision(); break;
		case Processing: 	processImage(); break;
		}
		// Draw image to frame.
		imageLabel.setIcon(new ImageIcon(currentImage));
		Point mouse = windowFrame.getMousePosition();
		if (mouse != null) {
			Point imgpos = imageLabel.getLocation();
			int x = mouse.x - imgpos.x - 4;
			int y = mouse.y - imgpos.y - 32;
			if (0 <= x && x < frameSize.width && 0 <= y && y < frameSize.height) {
				int color = currentImage.getRGB(x, y);
				currentImage.setRGB(x, y, Color.white.getRGB());
				colorChecker.updateColor(color);
			}
		}
		frame.recycle();
	}
	
	private void normalizeImage() {
		for (int x=0; x<frameSize.width; x++) {
			for (int y=0; y<frameSize.height; y++) {
				int index = y*frameSize.width + x;
				HSBColor color = hsbArray[index].set(colorArray[index]);
				color.offset(0, 0.5f-meanSat, 0.5f-meanBright);
				outputArray[index] = color.getRGB();
			}
		}
		currentImage.setRGB(0, 0, frameSize.width, frameSize.height, outputArray, 0, frameSize.width);
	}
		
	/**
	 * Initializes the vision system to adjust to the video feed.
	 */
	private void prepareVision() {
		double s = 0;
		double b = 0;
		for (int c=0; c<colorArray.length; c++) {
			HSBColor color = hsbArray[c].set(colorArray[c]);
			s += color.s;
			b += color.b;
		}
		s /= colorArray.length;
		b /= colorArray.length;
		Debug.logf("Mean saturation: %f, Mean brightness: %f", s, b);
		meanSat += s;
		meanBright += b;
		
		preparationFrames += 1;
		if (preparationFrames >= PREPARE_FRAMES) {
			state = VisionState.Processing;
			meanSat /= preparationFrames;
			meanBright /= preparationFrames;
		}
	}

	/**
	 * Processes an image to find positions of all the game objects.
	 * @param image - The current frame of video.
	 */
	private void processImage() {
		// Normalize image
		boolean norming = Debug.VISION_NORMALIZE_IMAGE;
		if (norming)
			normalizeImage();
		// Clear all clusters.
		for (HSBCluster cluster: clusters)
			cluster.clear();
		// Loop through pixels.
		for (int x=0; x < frameSize.width; x++) {
			for (int y=0; y < frameSize.height; y++) {
				int index = y*frameSize.width + x;
				HSBColor color = (norming) ? hsbArray[index] : hsbArray[index].set(colorArray[index]);
				// Test the pixel for each of the clusters
				for (HSBCluster cluster: clusters) {
					boolean matched = cluster.testPixel(x, y, color);
					if (matched)
						Debug.drawPixel(currentImage, x, y, cluster.debugColor);
				}
			}
		}
		for (HSBCluster cluster: clusters) {
			for (Rect rect: cluster.getImportantRects())
				Debug.drawRect(currentImage, rect, cluster.debugColor);
		}
		/*
		VecI corner = pitchLinesCluster.getCorner(Down, Left);
		if(corner != null)
			Debug.drawTestPixel(currentImage, corner.x, corner.y, Color.white);
		else
			System.out.println("Cannot find corner");
		*/
	}
	
	/**
	 * Called if there is an exception raised by the listener.
	 * @param e - The exception raised.
	 */
	public void exceptionReceived(V4L4JException e) {
		skyCam.stopVision();
		e.printStackTrace();
	}

}
