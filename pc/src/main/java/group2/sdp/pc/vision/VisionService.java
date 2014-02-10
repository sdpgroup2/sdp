package group2.sdp.pc.vision;

import group2.sdp.pc.Timer;
import group2.sdp.pc.geom.Rect;
import group2.sdp.pc.vision.clusters.BallCluster;
import group2.sdp.pc.vision.clusters.BlueRobotCluster;
import group2.sdp.pc.vision.clusters.DotCluster;
import group2.sdp.pc.vision.clusters.HSBCluster;
import group2.sdp.pc.vision.clusters.PitchLinesCluster;
import group2.sdp.pc.vision.clusters.PitchSectionCluster;
import group2.sdp.pc.vision.clusters.YellowRobotCluster;
import group2.sdp.util.Debug;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.List;

import au.edu.jcu.v4l4j.CaptureCallback;
import au.edu.jcu.v4l4j.DeviceInfo;
import au.edu.jcu.v4l4j.InputInfo;
import au.edu.jcu.v4l4j.JPEGFrameGrabber;
import au.edu.jcu.v4l4j.V4L4JConstants;
import au.edu.jcu.v4l4j.VideoDevice;
import au.edu.jcu.v4l4j.VideoFrame;
import au.edu.jcu.v4l4j.exceptions.V4L4JException;


public class VisionService implements CaptureCallback {

	private enum VisionState {
		Preparation, Processing
	}

	public static final int FRAME_WIDTH = 640;
	public static final int FRAME_HEIGHT = 480;
	public static final String DEFAULT_DEVICE = "/dev/video0";
	public static final String requiredInputName = "S-Video";
	public static final int requiredStandard = V4L4JConstants.STANDARD_PAL;

	private VideoDevice device;
	private JPEGFrameGrabber frameGrabber;

	private VisionServiceCallback callback;
	private VisionState state = VisionState.Preparation;
	private int currentFrame = 0;
	private int preparationFrames;

	private Timer timer = new Timer(10);

	// Stores colors for the current frame
	private BufferedImage currentImage;
	private int[] colorArray;
	private HSBColor[] hsbArray;

	private float meanSat = 0;
	private float meanBright = 0;

	// Clusters
	private BallCluster ballCluster = new BallCluster("Ball");
	private BlueRobotCluster blueRobotCluster = new BlueRobotCluster("Blue robots");
	private YellowRobotCluster yellowRobotCluster = new YellowRobotCluster("Yellow robots");
	private PitchSectionCluster pitchSectionCluster = new PitchSectionCluster("Pitch sections");
	private PitchLinesCluster pitchLinesCluster = new PitchLinesCluster("Pitch lines");
	private DotCluster dotCluster = new DotCluster("Dot");
	private HSBCluster[] clusters = new HSBCluster[] {
		ballCluster,
		blueRobotCluster,
		yellowRobotCluster,
	};

	public void start() {
		Debug.log("Vision started.");
		try {
			this.frameGrabber.startCapture();
		} catch (V4L4JException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Callback that gets the next frame of the video.
	 *
	 * @param frame - The frame that was captured.
	 */
	@Override
	public void nextFrame(VideoFrame frame) {
		timer.tick(25); // Prints the framerate every 25 frames
		currentImage = frame.getBufferedImage();
		callback.onFrameGrabbed(currentImage);

		// Read image into array colorArray
		currentImage.getRGB(0, 0, this.frameGrabber.getWidth(), this.frameGrabber.getHeight(), colorArray, 0, this.frameGrabber.getWidth());
		switch (state) {
			case Preparation: {
				this.prepareVision();
				this.currentFrame++;
				if (currentFrame >= preparationFrames) {
					state = VisionState.Processing;
					this.callback.onPreparationReady(pitchLinesCluster, pitchSectionCluster,
							ballCluster, yellowRobotCluster, blueRobotCluster);
				} else {
					this.callback.onPreparationFrame();
				}
				break;
			}
			case Processing: {
			    this.normaliseImage();
			    this.callback.onImageFiltered(hsbArray);
				this.processImage();
				this.callback.onImageProcessed(currentImage, hsbArray);
				break;
			}
		}
		frame.recycle();
	}

	/**
	 * This is run in the preparation stage of the Vision
	 * @param colorArray - RGB array representing the image
	 */
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
		this.currentFrame += 1;
		if (currentFrame >= preparationFrames) {
			meanSat /= preparationFrames;
			meanBright /= preparationFrames;
		}
	}

	private void normaliseImage() {
		for (int x = 0; x < this.getSize().width; x++) {
			for (int y = 0; y < this.getSize().height; y++) {
				int index = y * this.getSize().width + x;
				HSBColor color = hsbArray[index].set(colorArray[index]);
				color.offset(0, color.s - meanSat, color.b - meanBright);
			}
		}
	}

	public void processImage() {
		// Clear all clusters.
		for (HSBCluster cluster : clusters) {
			cluster.clear();
		}
		// Loop through pixels.
		for (int x = 0; x < getSize().width; x++) {
			for (int y = 0; y < getSize().height; y++) {
				int index = y * getSize().width + x;
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

	public void stopVision() {
		Debug.log("Vision stopped.");
		this.frameGrabber.stopCapture();
	}

	public void release() {
		Debug.log("Camera device released.");
		this.device.releaseFrameGrabber();
		this.device.release();
	}

	public Dimension getSize() {
		return new Dimension(frameGrabber.getWidth(), frameGrabber.getHeight());
	}

	public HSBCluster[] getClusters() {
	    return clusters;
    }

	public List<Rect> getYellowRobots() {
		return yellowRobotCluster.getImportantRects();
	}

	public List<Rect> getBlueRobots() {
		return blueRobotCluster.getImportantRects();
	}

	public HSBColor[] getHSBArray() {
		return hsbArray;
	}

	/**
	 * Called if there is an exception raised by the listener.
	 * @param e - The exception raised.
	 */
	@Override
	public void exceptionReceived(V4L4JException e) {
		this.stopVision();
		e.printStackTrace();
	}

	private void initColorArrays() {
		this.colorArray = new int[FRAME_WIDTH * FRAME_HEIGHT];
		this.hsbArray = new HSBColor[FRAME_WIDTH * FRAME_HEIGHT];
		for (int i = 0; i < this.hsbArray.length; i++) {
			this.hsbArray[i] = new HSBColor();
		}
	}

	public VisionService(String deviceName, int preparationFrames, VisionServiceCallback callback) {
		this.callback = callback;
		this.preparationFrames = preparationFrames;
		try {
			this.device = new VideoDevice(deviceName);
			DeviceInfo deviceInfo = device.getDeviceInfo();
			InputInfo inputInfo = null;
			for (InputInfo i: deviceInfo.getInputs()) {
				if (i.getName().equals(requiredInputName)) {
					inputInfo = i;
					break;
				}
			}
			if (inputInfo == null) {
				throw new RuntimeException("Video device has no " + requiredInputName + " input mode.");
			}

			this.frameGrabber = device.getJPEGFrameGrabber(FRAME_WIDTH, FRAME_HEIGHT, inputInfo.getIndex(), requiredStandard, V4L4JConstants.MAX_JPEG_QUALITY);
		} catch (V4L4JException e) {
			e.printStackTrace();
		}
		frameGrabber.setCaptureCallback(this);
		this.initColorArrays();
	}

	public VisionService(int preparationFrames, VisionServiceCallback callback) {
		this(DEFAULT_DEVICE, preparationFrames, callback);
	}

}
