package group2.sdp.pc;

import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.geom.VecI;
import group2.sdp.pc.vision.BallCluster;
import group2.sdp.pc.vision.ColorConfig;
import group2.sdp.pc.vision.RobotCluster;
import group2.sdp.pc.vision.SkyCam;

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

	public static void main(String[] args) {
		new VisionSystem();
	}
	
	private JFrame windowFrame;
	private SkyCam skyCam;
	private Dimension frameSize;
	
	private BallCluster ballCluster;
	private RobotCluster yellowRobotCluster;
	private RobotCluster blueRobotCluster;
	
	private Timer timer = new Timer(10);
	
	
	public VisionSystem() {
		initCamera();
		initWindow();
		initClusters();
		
		skyCam.startVision(this);
	}
	
	public void initCamera() {
		skyCam = new SkyCam();
		frameSize = skyCam.getSize();
	}
	
	public void initWindow() {
		windowFrame = new JFrame("Vision");
		windowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        windowFrame.addWindowListener(this);
        windowFrame.setVisible(true);
		windowFrame.setSize(skyCam.getSize());
	}
	
	public void initClusters() {
		ballCluster = new BallCluster();
		yellowRobotCluster = new RobotCluster(ColorConfig.ROBOT_YELLOW_MIN, ColorConfig.ROBOT_YELLOW_MAX);
		blueRobotCluster = new RobotCluster(ColorConfig.ROBOT_BLUE_MIN, ColorConfig.ROBOT_BLUE_MAX);
	}

	public void nextFrame(VideoFrame frame) {
		timer.tick(100); // Prints the framerate every 100 frames
		BufferedImage image = frame.getBufferedImage();
		processImage(image);
		// Draw image to frame.
		Graphics graphics = windowFrame.getGraphics();
		graphics.drawImage(image, 0, 0, frameSize.width, frameSize.height, null);
		frame.recycle();
	}

	/**
	 * Processes an image to find positions of all the game objects.
	 * @param image The current frame of video.
	 */
	private void processImage(BufferedImage image) {
		// Clear all clusters.
		ballCluster.clear();
		yellowRobotCluster.clear();
		blueRobotCluster.clear();
		// Loop through pixels.
		for (int x=0; x<frameSize.width; x++) {
			for (int y=0; y<frameSize.height; y++) {
				Color color = new Color(image.getRGB(x, y));
				// Test the pixel for each of the clusters
				boolean isBallPixel = ballCluster.testPixel(x, y, color);
				boolean isYellowPixel = yellowRobotCluster.testPixel(x, y, color);
				boolean isBluePixel = blueRobotCluster.testPixel(x, y, color);
				if (Debug.VISION_FILL_PIXELS) {
					// Color the pixels so we can see what got matched
					Debug.drawPixel(isBallPixel, image, x, y, Color.red);
					Debug.drawPixel(isYellowPixel, image, x, y, Color.yellow);
					Debug.drawPixel(isBluePixel, image, x, y, Color.blue);
				}
			}
		}
		VecI ballPosition = findTheBall(image);
		List<Rect> blueRobots = findRobots(image, blueRobotCluster);
		List<Rect> yellowRobots = findRobots(image, yellowRobotCluster);
		//Debug.log("Ball at "+ballPosition);
		//Debug.logf("Found %d yellow robots and %d blue robots.", yellowRobots.size(), blueRobots.size());
	}
	
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
	
	public List<Rect> findRobots(BufferedImage image, RobotCluster cluster) {
		List<Rect> rects = cluster.getRobotRects();
		for (Rect rect: rects) {
			if (Debug.VISION_DRAW_BOUNDS) {
				image.getGraphics().drawRect(rect.minX, rect.minY, rect.width, rect.height);
			}
		}
		return rects;
	}
	
	public void exceptionReceived(V4L4JException e) {
		skyCam.stopVision();
		e.printStackTrace();
	}

}
