package group2.sdp.pc;

import group2.sdp.pc.vision.HSBColor;
import group2.sdp.pc.vision.SkyCam;
import group2.sdp.pc.vision.VisionSystemCallback;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import au.edu.jcu.v4l4j.CaptureCallback;
import au.edu.jcu.v4l4j.VideoFrame;
import au.edu.jcu.v4l4j.exceptions.V4L4JException;

/**
 * The main class for the vision system. At the moment, it is runnable on its
 * own but in future, it may be created via a separate main class.
 *
 * @author Paul Harris
 *
 */
public class VisionSystem implements CaptureCallback {

	private static final int PREPARE_FRAMES = 5;

	private enum VisionState {
		Preparation, Processing
	}

	// Pre-processing
	private VisionState state = VisionState.Preparation;
	private int preparationFrames = 0;
	private float meanSat = 0;
	private float meanBright = 0;

	private VisionSystemCallback callback;

	private SkyCam skyCam;
	private Dimension frameSize;
	private BufferedImage currentImage;

	private Timer timer = new Timer(10);

	// Stores colors for the current frame
	private int[] colorArray;
	private HSBColor[] hsbArray;
	private int[] outputArray;


	/**
	 * Initialises the camera and creates a colorArray which is going to contain
	 * RGB values of the pitch
	 */
	public VisionSystem(VisionSystemCallback callback) {
		this.callback = callback;
		this.skyCam = new SkyCam();
		this.frameSize = skyCam.getSize();

		this.colorArray = new int[frameSize.width * frameSize.height];
		this.hsbArray = new HSBColor[frameSize.width * frameSize.height];
		for (int i = 0; i < hsbArray.length; i++) {
			this.hsbArray[i] = new HSBColor();
		}
		this.outputArray = new int[frameSize.width * frameSize.height];

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
		// Read image into array
		currentImage.getRGB(0, 0, frameSize.width, frameSize.height, colorArray, 0, frameSize.width);
		switch (state) {
		case Preparation: {
			prepareVision();
			break;
		}
		case Processing: {
			this.callback.processImage(currentImage);
			break;
		}
		}
		frame.recycle();
	}

	/**
	 * Not tested yet.
	 */
	private void normaliseImage() {
		for (int x = 0; x < frameSize.width; x++) {
			for (int y = 0; y < frameSize.height; y++) {
				int index = y * frameSize.width + x;
				HSBColor color = hsbArray[index].set(colorArray[index]);
				color.offset(0, 0.5f - meanSat, 0.5f - meanBright);
				outputArray[index] = color.getRGB();
			}
		}
		currentImage.setRGB(0, 0, frameSize.width, frameSize.height,
				outputArray, 0, frameSize.width);
	}

	/**
	 * Initializes the vision system to adjust to the video feed.
	 */
	private void prepareVision() {
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
		if (preparationFrames >= PREPARE_FRAMES) {
			state = VisionState.Processing;
			meanSat /= preparationFrames;
			meanBright /= preparationFrames;
		}
	}

	/**
	 * Called if there is an exception raised by the listener.
	 *
	 * @param e
	 *            - The exception raised.
	 */
	public void exceptionReceived(V4L4JException e) {
		skyCam.stopVision();
		e.printStackTrace();
	}

}
