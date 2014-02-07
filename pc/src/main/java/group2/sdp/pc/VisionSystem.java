package group2.sdp.pc;

import group2.sdp.pc.vision.SkyCam;
import group2.sdp.pc.vision.VisionSystemCallback;
import group2.sdp.pc.vision.HSBColor;
import group2.sdp.pc.vision.clusters.*;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import au.edu.jcu.v4l4j.CaptureCallback;
import au.edu.jcu.v4l4j.VideoFrame;
import au.edu.jcu.v4l4j.exceptions.V4L4JException;



/**
 * The main class for the vision system.
 * @author Paul Harris
 *
 */
public class VisionSystem implements CaptureCallback {
	
	// Pre-processing
	private VisionState state = VisionState.Preparation;
	private int preparationFrames = 0;
	private int maxPreparationFrames = 0;
	private float meanSat = 0;
	private float meanBright = 0;
	
	private enum VisionState {
		Preparation, Processing
	}

	private VisionSystemCallback callback;

	private SkyCam skyCam;
	private Dimension frameSize;

	private BufferedImage currentImage;

	private Timer timer = new Timer(10);
	
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

	// Stores colors for the current frame
	private int[] colorArray;
	private HSBColor[] hsbArray;


	/**
	 * Initialises the camera and creates a colorArray which is going to contain
	 * RGB values of the pitch
	 */
	public VisionSystem(int maxPreparationFrames, VisionSystemCallback callback) {
		this.callback = callback;
		this.skyCam = new SkyCam();
		frameSize = skyCam.getSize();
		this.colorArray = new int[frameSize.width * frameSize.height];
        this.hsbArray = new HSBColor[frameSize.width * frameSize.height];
		for (int i = 0; i < hsbArray.length; i++) {
			this.hsbArray[i] = new HSBColor();
		}
		this.maxPreparationFrames = maxPreparationFrames;	
	}
	
	public void start() {
    	this.skyCam.startVision(this);
	}

	/**
	 * Callback that gets the next frame of the video.
	 *
	 * @param frame - The frame that was captured.
	 */
	public void nextFrame(VideoFrame frame) {
		timer.tick(25); // Prints the framerate every 25 frames
		currentImage = frame.getBufferedImage();
		callback.onFrameGrabbed(currentImage);
		// Read image into array
		currentImage.getRGB(0, 0, frameSize.width, frameSize.height, colorArray, 0, frameSize.width);
		switch (state) {
			case Preparation: {
			    prepareVision();
				preparationFrames += 1;
				if (preparationFrames >= maxPreparationFrames) {
					state = VisionState.Processing;
				}
				callback.onPreparationFrame();
				break;
			}
			case Processing: {
			    normaliseImage();
			    callback.onImageFiltered(hsbArray);
				processImage();
				callback.onImageProcessed();
				break;
			}
		}
		frame.recycle();
	}
	
	public void prepareVision() {
		double s = 0;
		double b = 0;
		for (int c = 0; c < colorArray.length; c++) {
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
		if (preparationFrames >= maxPreparationFrames) {
			meanSat /= preparationFrames;
			meanBright /= preparationFrames;
		}
	}
	
	private void normaliseImage() {
		for (int x = 0; x < frameSize.width; x++) {
			for (int y = 0; y < frameSize.height; y++) {
				int index = y * frameSize.width + x;
				HSBColor color = hsbArray[index].set(colorArray[index]);
				color.offset(0, 0.5f - meanSat, 0.5f - meanBright);
			}
		}
	}
	
	public void processImage() {
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
					cluster.testPixel(x,y,color);
				}
			}
		}
        for (HSBCluster cluster: clusters) {
            cluster.getImportantRects();
        }
	}
	
	public Dimension getFrameSize() {
		return this.frameSize;
	}
	
	public int[] getColorArray() {
		return this.colorArray;
	}
	
	public HSBCluster[] getClusters() {
	    return clusters;
    }
	
	/**
	 * Called if there is an exception raised by the listener.
	 *
	 * @param e - The exception raised.
	 */
	public void exceptionReceived(V4L4JException e) {
		skyCam.stopVision();
		e.printStackTrace();
	}

}
