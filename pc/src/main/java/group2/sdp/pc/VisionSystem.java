package group2.sdp.pc;

import group2.sdp.pc.vision.HSBColor;
import group2.sdp.pc.vision.SkyCam;
import group2.sdp.pc.vision.VisionSystemCallback;
import group2.sdp.pc.vision.clusters.BallCluster;
import group2.sdp.pc.vision.clusters.BlueRobotCluster;
import group2.sdp.pc.vision.clusters.HSBCluster;
import group2.sdp.pc.vision.clusters.YellowRobotCluster;

import java.awt.Dimension;
import java.awt.image.BufferedImage;



/**
 * The main class for the vision system.
 * @author Paul Harris
 *
 */
public class VisionSystem implements VisionSystemCallback {

	private SkyCam skyCam;
	private Dimension frameSize;

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

	/**
	 * Initialises the camera and creates a colorArray which is going to contain
	 * RGB values of the pitch
	 */
	public VisionSystem() {
		this.skyCam = new SkyCam(5, this);
		frameSize = skyCam.getSize();
	}

	public void start() {
    	this.skyCam.start();
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
	}

	public HSBCluster[] getClusters() {
	    return clusters;
    }

	@Override
	public void onFrameGrabbed(BufferedImage image) {
	}

	@Override
	public void onImageFiltered(HSBColor[] hsbArray) {
	}

	@Override
	public void onPreparationFrame() {

	}

	@Override
	public void onImageProcessed() {
	}

}
