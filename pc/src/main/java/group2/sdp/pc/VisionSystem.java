package group2.sdp.pc;

import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.geom.VecI;
import group2.sdp.pc.vision.BallCluster;
import group2.sdp.pc.vision.ColorConfig;
import group2.sdp.pc.vision.PitchLines;
import group2.sdp.pc.vision.RobotCluster;
import group2.sdp.pc.vision.SkyCam;
import group2.sdp.pc.vision.filter.ImageStatus;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.JFrame;

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
	
	private JFrame windowFrame;
	private SkyCam skyCam;
	private Dimension frameSize;
	
	// Clusters
	private BallCluster ballCluster;
	private RobotCluster yellowRobotCluster;
	private RobotCluster blueRobotCluster;
	private PitchLines linesCluster;
	
	private Timer timer = new Timer(10);
	
	// Stores colors for the current frame
	private int[] colorArray;
	
	
	public static void main(String[] args) {
		new VisionSystem();
	}
	
	
	public VisionSystem() {
		initCamera();
		initWindow();
		initClusters();
		
		skyCam.startVision(this);
	}
	
	/**
	 * Initialises the camera and creates a colorArray which is going to contain
	 * RGB values of the pitch
	 */
	public void initCamera() {
		skyCam = new SkyCam();
		frameSize = skyCam.getSize();
		colorArray = new int[frameSize.width * frameSize.height];
	}
	/**
	 * Initialise a window frame.
	 */
	public void initWindow() {
		windowFrame = new JFrame("Vision");
		windowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        windowFrame.addWindowListener(this);
        windowFrame.setVisible(true);
		windowFrame.setSize(skyCam.getSize());
	}
	
	/**
	 * Initialise clusters that identify the balls, robots and lines.
	 */
	public void initClusters() {
		ballCluster = new BallCluster();
		yellowRobotCluster = new RobotCluster(ColorConfig.ROBOT_YELLOW_MIN, ColorConfig.ROBOT_YELLOW_MAX);
		blueRobotCluster = new RobotCluster(ColorConfig.ROBOT_BLUE_MIN, ColorConfig.ROBOT_BLUE_MAX);
		linesCluster = new PitchLines(ColorConfig.LINES_MIN, ColorConfig.LINES_MAX);
	}

	/** 
	 * Callback that gets the next frame of the video.
	 * @param frame - The frame that was captured.
	 */
	public void nextFrame(VideoFrame frame) {
		timer.tick(25); // Prints the framerate every 25 frames
		BufferedImage image = frame.getBufferedImage();
		// Read image into array
		image.getRGB(0, 0, frameSize.width, frameSize.height, colorArray, 0, frameSize.width);
		//ImageStatus status = new ImageStatus(colorArray);
		//Debug.log(status.toString());
		processImage(image);
		// Draw image to frame.
		Graphics graphics = windowFrame.getGraphics();
		graphics.drawImage(image, 0, 0, frameSize.width, frameSize.height, null);
		frame.recycle();
	}	

	/**
	 * Processes an image to find positions of all the game objects.
	 * @param image - The current frame of video.
	 */
	private void processImage(BufferedImage image) {
		// Clear all clusters.
		ballCluster.clear();
		yellowRobotCluster.clear();
		blueRobotCluster.clear();
		linesCluster.clear();
		// Loop through pixels.
		for (int x=0; x < frameSize.width; x++) {
			for (int y=0; y < frameSize.height; y++) {
				Color color = new Color(colorArray[y * frameSize.width + x]);
				// Test the pixel for each of the clusters
				boolean isBallPixel = ballCluster.testPixel(x, y, color);
				boolean isYellowPixel = yellowRobotCluster.testPixel(x, y, color);
				boolean isBluePixel = blueRobotCluster.testPixel(x, y, color);    
				//boolean isLinePixel = linesCluster.testPixel(x, y, color);
				if (Debug.VISION_FILL_PIXELS) {
					// Color the pixels so we can see what got matched
					Debug.drawPixel(isBallPixel, image, x, y, Color.red);
					Debug.drawPixel(isYellowPixel, image, x, y, Color.yellow);
					Debug.drawPixel(isBluePixel, image, x, y, Color.blue);
					//Debug.drawPixel(isLinePixel, image, x, y, Color.white);
				}
			}
		}
		findTheBall(image);
		findRobots(image, blueRobotCluster);
		findRobots(image, yellowRobotCluster);
	}
	
	/**
	 * Finds the ball in the image
	 * @param image - The image we are looking for a ball in.
	 * @return The vector of the centre of the bounding box of the ball.
	 */
	public VecI findTheBall(BufferedImage image) {
		Rect ballRect = ballCluster.getBallRect();
		if (ballRect != null) {
			if (Debug.VISION_DRAW_BOUNDS) {
				image.getGraphics().drawRect(ballRect.minX, ballRect.minY, ballRect.width, ballRect.height);
			}
			return ballRect.getCentre();
		} else {
			return null;
		}
	}
	
	/**
	 * Draws bounding boxes around the robots
	 * @param image - The image we are looking for robots in
	 * @param cluster - The cluster that contains robot pixels
	 * @return The bounding boxes of the robots.
	 */
	public List<Rect> findRobots(BufferedImage image, RobotCluster cluster) {
		List<Rect> rects = cluster.getRobotRects();
		for (Rect rect: rects) {
			if (Debug.VISION_DRAW_BOUNDS) {
				image.getGraphics().drawRect(rect.minX, rect.minY, rect.width, rect.height);
			}
		}
		return rects;
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
