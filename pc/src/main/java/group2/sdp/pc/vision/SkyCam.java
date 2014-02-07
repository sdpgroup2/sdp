package group2.sdp.pc.vision;

import group2.sdp.pc.Debug;
import group2.sdp.pc.Timer;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import au.edu.jcu.v4l4j.CaptureCallback;
import au.edu.jcu.v4l4j.DeviceInfo;
import au.edu.jcu.v4l4j.InputInfo;
import au.edu.jcu.v4l4j.JPEGFrameGrabber;
import au.edu.jcu.v4l4j.V4L4JConstants;
import au.edu.jcu.v4l4j.VideoDevice;
import au.edu.jcu.v4l4j.VideoFrame;
import au.edu.jcu.v4l4j.exceptions.V4L4JException;


public class SkyCam implements CaptureCallback {

	public static final String DEFAULT_DEVICE = "/dev/video0";
	public static final String requiredInputName = "S-Video";
	public static final int requiredStandard = V4L4JConstants.STANDARD_PAL;

	private InputInfo inputInfo = null;
	private JPEGFrameGrabber frameGrabber;

	private VisionSystemCallback callback;

	private VisionState state = VisionState.Preparation;
	private int currentFrame = 0;
	private int preparationFrames;

	private enum VisionState {
		Preparation, Processing
	}

	private Timer timer = new Timer(10);

	// Stores colors for the current frame
	private BufferedImage currentImage;
	private int[] colorArray;
	private HSBColor[] hsbArray;

	private float meanSat = 0;
	private float meanBright = 0;

	private VideoDevice device;

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
			    this.prepareVision(colorArray);
				currentFrame += 1;
				if (currentFrame >= preparationFrames) {
					state = VisionState.Processing;
				}
				this.callback.onPreparationFrame();
				break;
			}
			case Processing: {
			    this.normaliseImage(colorArray);
			    this.callback.onImageFiltered(hsbArray);
				this.callback.processImage(hsbArray);
				break;
			}
		}
		frame.recycle();
	}

	/**
	 * This is run in the preparation stage of the Vision
	 * @param colorArray - RGB array representing the image
	 */
	public void prepareVision(int[] colorArray) {
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
		if (preparationFrames >= preparationFrames) {
			meanSat /= preparationFrames;
			meanBright /= preparationFrames;
		}
	}

	private void normaliseImage(int[] colorArray) {
		for (int x = 0; x < this.getSize().width; x++) {
			for (int y = 0; y < this.getSize().height; y++) {
				int index = y * this.getSize().width + x;
				HSBColor color = hsbArray[index].set(colorArray[index]);
				color.offset(0, 0.5f - meanSat, 0.5f - meanBright);
			}
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

	/**
	 * Called if there is an exception raised by the listener.
	 * @param e - The exception raised.
	 */
	@Override
	public void exceptionReceived(V4L4JException arg0) {
		// TODO Auto-generated method stub

	}

	public SkyCam(String deviceName, int preparationFrames, VisionSystemCallback callback) {
		this.callback = callback;
		this.preparationFrames = preparationFrames;
		try {
			this.device = new VideoDevice(deviceName);
			DeviceInfo deviceInfo = device.getDeviceInfo();
			for (InputInfo i: deviceInfo.getInputs()) {
				if (i.getName().equals(requiredInputName)) {
					inputInfo = i;
					break;
				}
			}
			if (inputInfo == null) {
				throw new RuntimeException("Video device has no " + requiredInputName + " input mode.");
			}
			this.frameGrabber = device.getJPEGFrameGrabber(640, 480, inputInfo.getIndex(), requiredStandard, V4L4JConstants.MAX_JPEG_QUALITY);
		} catch (V4L4JException e) {
			e.printStackTrace();
		}
		frameGrabber.setCaptureCallback(this);
	}

	public SkyCam(int preparationFrames, VisionSystemCallback callback) {
		this(DEFAULT_DEVICE, preparationFrames, callback);
	}

}
