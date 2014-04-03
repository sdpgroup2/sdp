package sdp.group2.vision;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.SwingWorker;

import sdp.group2.geometry.Point;
import sdp.group2.gui.VisionGUI;
import sdp.group2.pc.MasterController;
import sdp.group2.pc.Timer;
import sdp.group2.util.Constants;
import sdp.group2.util.Constants.TeamColour;
import sdp.group2.util.Debug;
import sdp.group2.util.Tuple;
import au.edu.jcu.v4l4j.CaptureCallback;
import au.edu.jcu.v4l4j.DeviceInfo;
import au.edu.jcu.v4l4j.InputInfo;
import au.edu.jcu.v4l4j.JPEGFrameGrabber;
import au.edu.jcu.v4l4j.V4L4JConstants;
import au.edu.jcu.v4l4j.VideoDevice;
import au.edu.jcu.v4l4j.VideoFrame;
import au.edu.jcu.v4l4j.exceptions.V4L4JException;

import com.googlecode.javacv.cpp.opencv_core.CvRect;

public class VisionService implements CaptureCallback {

	public static final int FRAME_WIDTH = 640;
	public static final int FRAME_HEIGHT = 480;
	public static final String DEFAULT_DEVICE = "/dev/video0";
	public static final String requiredInputName = "S-Video";
	public static final int requiredStandard = V4L4JConstants.STANDARD_PAL;
	public static boolean ENABLE_GUI = true;
	private VideoDevice device;
	private JPEGFrameGrabber frameGrabber;
	private VisionServiceCallback callback;
	private Timer timer = new Timer(10);
	private VisionState state = VisionState.Preparation;
	private CvRect cropRect = Constants.PITCH0_CROPRECT;

	/**
	 * Initialises a new vision service with a certain device name, number of
	 * preparation frames and a callback.
	 * 
	 * @param deviceName
	 *            name of the video device
	 * @param preparationFrames
	 *            number number of frames to use for preparation stage
	 * @param callback
	 *            callback to be called after certain stages
	 */
	public VisionService(String deviceName, int preparationFrames,
			VisionServiceCallback callback) {
		this.callback = callback;
		try {
			this.device = new VideoDevice(deviceName);
			DeviceInfo deviceInfo = device.getDeviceInfo();
			InputInfo inputInfo = null;
			for (InputInfo i : deviceInfo.getInputs()) {
				if (i.getName().equals(requiredInputName)) {
					inputInfo = i;
					break;
				}
			}
			if (inputInfo == null) {
				throw new RuntimeException("Video device has no "
						+ requiredInputName + " input mode.");
			}
			this.frameGrabber = device.getJPEGFrameGrabber(FRAME_WIDTH,
					FRAME_HEIGHT, inputInfo.getIndex(), requiredStandard,
					V4L4JConstants.MAX_JPEG_QUALITY);
		} catch (V4L4JException e) {
			e.printStackTrace();
		}
		frameGrabber.setCaptureCallback(this);		
	}

	/**
	 * Initialises the VisionService with the default device
	 * 
	 * @param preparationFrames
	 *            number of frames to use for preparation stage
	 * @param callback
	 *            callback to be called after certain stages
	 */
	public VisionService(int preparationFrames, VisionServiceCallback callback) {
		this(DEFAULT_DEVICE, preparationFrames, callback);
	}

	/**
	 * Starts the vision service.
	 */
	public void start() {
		Debug.log("Vision started.");
		try {
			this.frameGrabber.startCapture();
		} catch (V4L4JException e) {
			e.printStackTrace();
		}
		VisionGUI.start();
		System.out.println("In Preparation state.");
	}

	/**
	 * Callback that gets the next frame of the video.
	 * 
	 * @param frame
	 *            - The frame that was captured.
	 */
	@Override
    public void nextFrame(VideoFrame frame) {
        timer.tick(25); // Prints the framerate every 25 frames

        ImageProcessor.process(frame.getBufferedImage());
//        gui.setImage(crop(ImageProcessor.getImage(gui.getSelectedImage())));
		Point ballCentroid = ImageProcessor.ballCentroid();
		List<Tuple<Point, Point>> yellowRobots = ImageProcessor.yellowRobots();
		List<Tuple<Point, Point>> blueRobots = ImageProcessor.blueRobots();
        switch (state) {
		case Preparation:
//			System.out.println("Not prepared.");
			boolean prepared = true;

    		// If we don't have the ball then we're not ready
    		if (ballCentroid == null) {
//    		    System.out.println("Can't find ball.");
    			prepared = false;
    			break;
    		}
    		
    		// If we don't have all 4 robots, we're not ready
    		if (yellowRobots.size() != 2 || blueRobots.size() != 2) {
    		    Debug.logf(
    		            "Finding %d yellow and %d blue robots. Need 2 of each.",
    		            yellowRobots.size(), blueRobots.size());
    			prepared = false;
    			break;
    		}

    		// If one of the direction vectors is null we're not ready
    		// Position (first of tuple) is never null
//    		if (MasterController.ourTeam == TeamColour.BLUE) {
				for (Tuple<Point, Point> tuple : blueRobots) {
					if (tuple.getSecond() == null) {
		//    			    System.out.println("Can't find dot for blue robot.");
						prepared = false;
						break;
					}
				}
//    		}
    		
    		// If one of the direction vectors is null we're not ready
    		// Position (first of tuple) is never null
//    		if (MasterController.ourTeam == TeamColour.YELLOW) {
	    		for (Tuple<Point, Point> tuple : yellowRobots) {
	    			if (tuple.getSecond() == null) {
	//    			    System.out.println("Can't find dot for yellow robot.");
	    				prepared = false;
	    				break;
	    			}
	    		}
//    		}
    		if (prepared) {
    			// We're ready switch to processing
    			state = VisionState.Processing;
    			callback.prepared(ballCentroid, yellowRobots, blueRobots);
    			System.out.println("Prepared! Switched state to Processing.");
    		}
			break;

		case Processing:
	        // Now the objects should be set
	        callback.update(ballCentroid, yellowRobots, blueRobots);
	        break;
	       
	    default:
	    	// Shouldnt occur
	    	break;
		}
        frame.recycle();
        return;
    }

	/**
	 * Called if there is an exception raised by the listener.
	 * 
	 * @param e - The exception raised.
	 */
	
	@Override
	public void exceptionReceived(V4L4JException e) {
		this.stopVision();
		this.callback.onExceptionThrown(e);
		e.printStackTrace();
	}

	/**
	 * Stops the vision service.
	 */
	public void stopVision() {
		Debug.log("Vision stopped.");
		this.frameGrabber.stopCapture();
		release();
	}

	/**
	 * Releases the frame grabber.
	 */
	public void release() {
		Debug.log("Camera device released.");
		this.device.releaseFrameGrabber();
		this.device.release();
	}

	/**
	 * Returns the dimension of the frame grabber frames.
	 * 
	 * @return dimension of the frame grabber
	 */
	public Dimension getSize() {
		return new Dimension(cropRect.width(), cropRect.height());
	}

	private enum VisionState {
		Preparation, StaticDetection, Processing
	}

}
