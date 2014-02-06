package group2.sdp.pc;

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
 * @author Paul Harris
 *
 */
public class VisionSystem implements CaptureCallback {
	
	// Pre-processing
	private VisionState state = VisionState.Preparation;
	private int preparationFrames = 0;
	private int maxPreparationFrames = 0;
	
	private enum VisionState {
		Preparation, Processing
	}

	private VisionSystemCallback callback;

	private SkyCam skyCam;
	private Dimension frameSize;

	private BufferedImage currentImage;

	private Timer timer = new Timer(10);

	// Stores colors for the current frame
	private int[] colorArray;

	/**
	 * Initialises the camera and creates a colorArray which is going to contain
	 * RGB values of the pitch
	 */
	public VisionSystem(int maxPreparationFrames, VisionSystemCallback callback) {
		this.callback = callback;
		this.skyCam = new SkyCam();
		frameSize = skyCam.getSize();
		this.colorArray = new int[frameSize.width * frameSize.height];
		this.maxPreparationFrames = maxPreparationFrames;
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
				preparationFrames += 1;
				if (preparationFrames >= maxPreparationFrames) {
					state = VisionState.Processing;
				}
				this.callback.prepareVision(currentImage);
				break;
			}
			case Processing: {
				this.callback.processImage(currentImage);
				break;
			}
		}
		frame.recycle();
	}
	
	public Dimension getFrameSize() {
		return this.frameSize;
	}
	
	public int[] getColorArray() {
		return this.colorArray;
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
