package sdp.group2.vision;

import au.edu.jcu.v4l4j.*;
import au.edu.jcu.v4l4j.exceptions.V4L4JException;
import sdp.group2.geometry.Rect;
import sdp.group2.gui.VisionDisplay;
import sdp.group2.pc.Timer;
import sdp.group2.util.Debug;
import sdp.group2.vision.clusters.*;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;


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
    private int currentFrame = 0;
    private int preparationFrames;
    private VisionState state = VisionState.Preparation;
    private Timer timer = new Timer(10);
    private int[] colorArray;
    private Image currentImage = new Image();
    private float meanSat = 0;
    private float meanBright = 0;
    private VisionDisplay visionDisplay;
    // Clusters
    private BallCluster ballCluster = new BallCluster("Ball");
    //	private YellowRobotCluster yellowRobotCluster = new YellowRobotCluster("Yellow robots");
//	private CompoundRobotCluster blueCompoundRobot = new CompoundRobotCluster();
    private PitchSectionCluster pitchSectionCluster = new PitchSectionCluster("Pitch sections");
    private PitchLinesCluster pitchLinesCluster = new PitchLinesCluster("Pitch lines");
    private RobotBaseCluster baseRobotCluster = new RobotBaseCluster("Bases");
    private YellowRobotCluster yellowRobotCluster = new YellowRobotCluster("Yellow");
    private DotCluster dotCluster = new DotCluster("Dots");
    private HSBCluster[] clusters = new HSBCluster[]{
            ballCluster,
            //blueRobotCluster,
            baseRobotCluster,
            yellowRobotCluster,
            //pitchLinesCluster,
            //pitchSectionCluster,
            dotCluster,
    };
    private Rect processingRegion;

    /**
     * Initialises a new vision service with a certain device name,
     * number of preparation frames and a callback.
     *
     * @param deviceName        name of the video device
     * @param preparationFrames number number of frames to use for preparation stage
     * @param callback          callback to be called after certain stages
     */
    public VisionService(String deviceName, int preparationFrames, VisionServiceCallback callback) {
        this.callback = callback;
        this.preparationFrames = preparationFrames;
        if (ENABLE_GUI) {
            this.visionDisplay = new VisionDisplay(FRAME_WIDTH, FRAME_HEIGHT);
        }
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
                throw new RuntimeException("Video device has no " + requiredInputName + " input mode.");
            }
            this.frameGrabber = device.getJPEGFrameGrabber(FRAME_WIDTH, FRAME_HEIGHT, inputInfo.getIndex(), requiredStandard, V4L4JConstants.MAX_JPEG_QUALITY);
        } catch (V4L4JException e) {
            e.printStackTrace();
        }
        frameGrabber.setCaptureCallback(this);
        currentImage.setSize(getSize());
        this.processingRegion = new Rect(0, 0, getSize().width, getSize().height);
    }

    /**
     * Initialises the VisionService with the default device
     *
     * @param preparationFrames number of frames to use for preparation stage
     * @param callback          callback to be called after certain stages
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

    public HSBCluster[] getClusters() {
        return clusters;
    }

    /**
     * Callback that gets the next frame of the video.
     *
     * @param frame - The frame that was captured.
     */
    @Override
    public void nextFrame(VideoFrame frame) {
        timer.tick(25); // Prints the framerate every 25 frames

        ImageProcessor processor = new ImageProcessor(frame.getBufferedImage());
        processor.undistort(frame.getSequenceNumber());
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
     * @param e - The exception raised.
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
                //visionDisplay.redraw(currentImage, ball, robots);
                return null;
            }
        };
    }

    /**
     * Find the bounding rectangle of the pitch.
     *
     * @return bounding rectangle
     */
    private Rect findPitch() {
        for (int x = 0; x < getSize().width; x++) {
            for (int y = 0; y < getSize().height; y++) {
                HSBColor color = currentImage.getHSBColor(x, y);
                pitchLinesCluster.testPixel(x, y, color);
            }
        }
        List<Rect> rects = pitchLinesCluster.getImportantRects();
        return (!rects.isEmpty()) ? rects.get(0) : null;
    }

    /**
     * Find the bounding rectangles of the pitch sections.
     *
     * @return bounding rectangles
     */
    private Rect[] findSections() {
        for (int x = 0; x < getSize().width; x++) {
            for (int y = 0; y < getSize().height; y++) {
                HSBColor color = currentImage.getHSBColor(x, y);
                pitchSectionCluster.testPixel(x, y, color);
            }
        }
        List<Rect> rects = pitchSectionCluster.getImportantRects();
        if (rects.size() > 4) {
            rects = rects.subList(0, 4);
        }
        Collections.sort(rects); // sort them so leftmost is byte 0 and so on
        return (!rects.isEmpty()) ? rects.toArray(new Rect[rects.size()]) : null;
    }

    /**
     * This is ran in the preparation stage of the vision service. It measures
     * the average brightness and saturation for normalisation.
     */
    public void prepareVision() {
        if (currentFrame == 0) {
            // First frame sometimes has scanlines etc. Ignore it.
            return;
        }
        double s = 0;
        double b = 0;
        for (HSBColor color : currentImage.getHSBColorPixelIterator()) {
            s += color.s;
            b += color.b;
        }
        s /= colorArray.length;
        b /= colorArray.length;
        Debug.logf("Mean saturation: %f, Mean brightness: %f", s, b);
        meanSat += s;
        meanBright += b;
    }

    /**
     * Runs after the preparation stage is over.
     */
    public void endPrepareVision() {
        // We ignored the first frame.
        int frames = preparationFrames - 1;
        meanSat /= frames;
        meanBright /= frames;
        Debug.logf("Final mean saturation: %f, mean brightness: %f", meanSat, meanBright);
    }

    /**
     * Runs every frame from the video feed.
     * TODO: This function is overflowing in the iteration phase
     */
    public void processImage() {
        // Clear all clusters.
        for (HSBCluster cluster : clusters) {
            cluster.clear();
        }
        // Loop through pixels.
        for (int x = (int) processingRegion.getMinX(); x < (int) processingRegion.getMaxX(); x++) {
            for (int y = (int) processingRegion.getMinY(); y < (int) processingRegion.getMaxY(); y++) {
                int index = y * getSize().width + x;
                index = Math.max(0, index);
                index = Math.min(index, getSize().width * getSize().height - 1);
                HSBColor color = currentImage.getHSBColor(index);
                // Test the pixel for each of the clusters
                for (HSBCluster cluster : clusters) {
                    cluster.testPixel(x, y, color);
                }
            }
        }

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

    // these should be changed to get[Yellow/Blue]RobotRects() as they
    // return rects and not robot objects
//	public List<Rect> getYellowRobots() {
//		return yellowRobotCluster.getImportantRects();
//	}
//
//	public List<Rect> getBlueRobots() {
//		return blueRobotCluster.getImportantRects();
//	}
//	
//	public List<Rect> getRobotBases() {
//		return baseCluster.getImportantRects();
//	}

//	//Should be changed to getYellowRobots()
//	public List<Robot> getYellowRobotObjects() {
//		ArrayList<Robot> robots = new ArrayList<Robot>();
//		List<Rect> colorRects = getYellowRobots();
//		for(Rect colorRect : colorRects) {
//			for(Rect base : getRobotBases()) {
//				if (base.contains(colorRect)) {
//					Robot robot = new Robot(colorRect, base);
//					robots.add(robot);
//				}
//			}
//		}
//		return robots;
//	}


}
