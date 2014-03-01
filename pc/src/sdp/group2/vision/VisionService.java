package sdp.group2.vision;

import java.awt.Dimension;

import javax.swing.SwingWorker;

import sdp.group2.geometry.Rect;
import sdp.group2.pc.Timer;
import sdp.group2.util.Debug;
import au.edu.jcu.v4l4j.CaptureCallback;
import au.edu.jcu.v4l4j.DeviceInfo;
import au.edu.jcu.v4l4j.InputInfo;
import au.edu.jcu.v4l4j.JPEGFrameGrabber;
import au.edu.jcu.v4l4j.V4L4JConstants;
import au.edu.jcu.v4l4j.VideoDevice;
import au.edu.jcu.v4l4j.VideoFrame;
import au.edu.jcu.v4l4j.exceptions.V4L4JException;

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
	private ImageProcessor imageProcessor = new ImageProcessor();
	private int preparationFrames;

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
		this.preparationFrames = preparationFrames;
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
//        boolean ready = false;
//        PointSet pitchPoints = new PointSet();
//        pitchPoints.add(new Point(101, 94));
//        pitchPoints.add(new Point(66, 164));
//        pitchPoints.add(new Point(67, 311));
//        pitchPoints.add(new Point(100, 377));
//        pitchPoints.add(new Point(546, 382));
//        pitchPoints.add(new Point(584, 315));
//        pitchPoints.add(new Point(588, 170));
//        pitchPoints.add(new Point(554, 100));
//        PointSet[] zonePoints = new PointSet[4];
//        for (int i = 0; i < zonePoints.length; i++) {
//			zonePoints[i] = new PointSet();
//		}
//        zonePoints[0].add(new Point(157,89));
//        zonePoints[0].add(new Point(101,94));
//        zonePoints[0].add(new Point(66,164));
//        zonePoints[0].add(new Point(67,311));
//        zonePoints[0].add(new Point(100,377));
//        zonePoints[0].add(new Point(155,382));
//        
//        zonePoints[1].add(new Point(206,88));
//        zonePoints[1].add(new Point(306,88));
//        zonePoints[1].add(new Point(204,383));
//        zonePoints[1].add(new Point(301,385));
//        
//        zonePoints[2].add(new Point(356,90));
//        zonePoints[2].add(new Point(453,94));
//        zonePoints[2].add(new Point(350,385));
//        zonePoints[2].add(new Point(447,385));
//        
//        zonePoints[3].add(new Point(502,95));
//        zonePoints[3].add(new Point(554,100));
//        zonePoints[3].add(new Point(588,170));
//        zonePoints[3].add(new Point(584,315));
//        zonePoints[3].add(new Point(546,382));
//        zonePoints[3].add(new Point(495,384));
//        
//        Pitch pitch = new Pitch(pitchPoints, zonePoints);
//        imageProcessor.process(frame.getBufferedImage());
//        Ball ball = new Ball();
//        pitch.updateBallPosition(imageProcessor.getBallPoint());
//        pitch.updateRobotState(id, p, theta)
        imageProcessor.process(frame.getBufferedImage());
        frame.recycle();
        return;

//        // Read image into array colorArray and add it to the image
//        colorArray = frame.getBufferedImage().getRGB(0, 0, getSize().width, getSize().height,
//                null, 0, getSize().width);
//        currentImage.setRgbArray(colorArray);
//
//        callback.onFrameGrabbed(currentImage);
//
//        switch (state) {
//            case Preparation: {
//                // Prepare the vision: get parameters for normalisation.
//                this.prepareVision();
//                this.currentFrame++;
//                if (currentFrame >= preparationFrames) {
//                    endPrepareVision();
//                    state = VisionState.StaticDetection;
//                    currentImage.normaliseImage(meanBright, meanSat);
//                    processImage(); // Process once when ready so we have clusters
//                } else {
//                    callback.onPreparationFrame();
//                }
//                break;
//            }
//            case StaticDetection: {
//                // Find the objects that will not move and restrict processing region.
//                // Only change state from StaticDetection when we have pitch and sections.
//                Debug.log("Looking for pitch...");
//                currentImage.normaliseImage(meanBright, meanSat);
//                processImage();
//                Rect pitchRect = findPitch();
//                Rect[] sectionRects = findSections();
//                if (pitchRect != null && sectionRects != null) {
//                    boolean ready = callback.onPreparationReady(currentImage, ballCluster, baseRobotCluster, pitchRect, sectionRects);
//                    if (ready) {
//                        System.out.printf("Ready: %b\n", ready);
//                        processingRegion = new Rect(pitchRect.x - 15, pitchRect.y - 15,
//                                pitchRect.width + 30, pitchRect.height + 30);
//                        Debug.log("Found pitch");
//
//                        // Clear image to make new region obvious
//                        currentImage.clear();
//                        state = VisionState.Processing;
//                    }
//                }
//                break;
//            }
//            case Processing: {
//                // Process the images.
//                currentImage.normaliseImage(meanBright, meanSat);
//                callback.onImageFiltered(currentImage);
//                processImage();
//                callback.onImageProcessed(currentImage, ballCluster, baseRobotCluster);
//                break;
//            }
//        }
//        updateGUI();
        //frame.recycle();
    }

	/**
	 * Called if there is an exception raised by the listener.
	 * 
	 * @param e
	 *            - The exception raised.
	 */
	
	@Override
	public void exceptionReceived(V4L4JException e) {
		this.stopVision();
		this.callback.onExceptionThrown(e);
		e.printStackTrace();
	}

	private void updateGUI() {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
				// visionDisplay.redraw(currentImage, ball, robots);
				return null;
			}
		};
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
		return new Dimension(frameGrabber.getWidth(), frameGrabber.getHeight());
	}

	private enum VisionState {
		Preparation, StaticDetection, Processing
	}

}
