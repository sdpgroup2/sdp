package group2.sdp.pc;

import group2.sdp.pc.geom.GeomUtil;
import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.vision.BallCluster;
import group2.sdp.pc.vision.DebugBlacknessCluster;
import group2.sdp.pc.vision.SkyCam;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import au.edu.jcu.v4l4j.CaptureCallback;
import au.edu.jcu.v4l4j.VideoFrame;
import au.edu.jcu.v4l4j.exceptions.V4L4JException;

public class CamTest extends WindowAdapter implements CaptureCallback {

	public static void main(String[] args) {
		new CamTest();
	}
	
	private JFrame windowFrame;
	private SkyCam skyCam;
	private Dimension frameSize;
	
	private BallCluster ballCluster;
	private DebugBlacknessCluster blackCluster;
	
	
	public CamTest() {
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
		blackCluster = new DebugBlacknessCluster();
	}

	public void nextFrame(VideoFrame frame) {
		BufferedImage image = frame.getBufferedImage();
		// Draw image to frame.
		Graphics graphics = windowFrame.getGraphics();
		graphics.drawImage(image, 0, 0, frameSize.width, frameSize.height, null);
		// Clear all clusters.
		ballCluster.clear();
		// Loop through pixels.
		for (int x=0; x<frameSize.width; x++) {
			for (int y=0; y<frameSize.height; y++) {
				Color color = new Color(image.getRGB(x, y));
				ballCluster.testPixel(x, y, color);
				blackCluster.testPixel(x, y, color);
			}
		}
		findTheBall();
		findBlackRectangle();
		frame.recycle();
	}
	
	// Tries to print the location of the ball.
	public void findTheBall() {
		Rect ballBounds = GeomUtil.getBoundingBox(ballCluster.getPixels());
		if (ballBounds == null) {
			System.out.println("Ball not found.");
		} else {
			System.out.println("Ball at: "+ballBounds.getCentre());
		}
	}
	
	// Checks for a black rectangle (e.g. camera showing black)
	public void findBlackRectangle() {
		Rect blackBounds = GeomUtil.getBoundingBox(blackCluster.getPixels());
		if (blackBounds != null) {
			Debug.log("Black rect: "+blackBounds);
		}
	}
	
	public void exceptionReceived(V4L4JException e) {
		skyCam.stopVision();
		e.printStackTrace();
	}

}
