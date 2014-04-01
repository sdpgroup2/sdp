package sdp.group2.vision;

import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_32F;
import static com.googlecode.javacv.cpp.opencv_core.cvCircle;
import static com.googlecode.javacv.cpp.opencv_core.cvCopy;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSize;
import static com.googlecode.javacv.cpp.opencv_core.cvLine;
import static com.googlecode.javacv.cpp.opencv_core.cvLoad;
import static com.googlecode.javacv.cpp.opencv_core.cvResetImageROI;
import static com.googlecode.javacv.cpp.opencv_core.cvScalar;
import static com.googlecode.javacv.cpp.opencv_core.cvScalarAll;
import static com.googlecode.javacv.cpp.opencv_core.cvSetImageROI;
import static com.googlecode.javacv.cpp.opencv_core.cvSize;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_BGR2HSV;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_INTER_LINEAR;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_WARP_FILL_OUTLIERS;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvInitUndistortMap;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvRemap;
import static com.googlecode.javacv.cpp.opencv_imgproc.medianBlur;

import java.awt.image.BufferedImage;
import java.util.List;

import sdp.group2.geometry.Point;
import sdp.group2.gui.VisionGUI;
import sdp.group2.pc.MasterController;
import sdp.group2.util.Constants;
import sdp.group2.util.Constants.PitchType;
import sdp.group2.util.Tuple;

import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.IplImage;


public class ImageProcessor {
	
    private static final int MEDIAN_FILTER_SIZE = 3; // must be odd and > 1
    private static final String ASSETS_FOLDER = "./assets";
    
    // Matrices used for remapping
    private static final CvMat cameraMatrix = new CvMat(cvLoad(ASSETS_FOLDER + "/CameraMatrix.yml"));
    private static final CvMat distCoeffs = new CvMat(cvLoad(ASSETS_FOLDER + "/DistCoeffs.yml"));

    private static CvRect cropRect = Thresholds.activeThresholds.cropRect; 

	private static IplImage uncropped; // The image we get from the camera 
    private static IplImage uncroppedTemp; // Temp image we need in undistort
    private static IplImage temp; // Temporary image used for processing
    private static IplImage image; // Processing image (immutable)
    private static IplImage binaryImage; // Temporary binary image for processing
    private static BallEntity ballEntity = new BallEntity(); // Ball thresholding
    private static RobotEntity robotEntity = new RobotEntity();; // Robot thresholding
    
    private static Point ballCentroid;
        
    static {
    	uncroppedTemp = IplImage.create(cvSize(640, 480), 8, 3);
    	temp = IplImage.create(cvSize(cropRect.width(), cropRect.height()), 8, 3);
    	image = IplImage.create(cvSize(cropRect.width(), cropRect.height()), 8, 3);
    }

    /**
     * Un-distorts the image. Cannot be done in-place.
     *
     * @param image        image to be un-distorted
     * @param temp         temporary image
     * @param cameraMatrix camera matrix input from calibration
     * @param distCoeffs   distortion coefficients input from calibration
     */
    public static void undistort(IplImage image, IplImage temp, CvMat cameraMatrix, CvMat distCoeffs) {
        if (cameraMatrix == null || distCoeffs == null) {
            System.err.println("Can't open distortion info.");
        } else {
            IplImage remapX = IplImage.create(cvGetSize(image), IPL_DEPTH_32F, 1);
            IplImage remapY = IplImage.create(cvGetSize(image), IPL_DEPTH_32F, 1);

            // Initialise the undistortion map
            cvInitUndistortMap(cameraMatrix, distCoeffs, remapX, remapY);

            if (remapX != null && remapY != null) {
                cvCopy(image, temp);
                // Apply the undistortion map
                cvRemap(temp, image, remapX, remapY, CV_INTER_LINEAR | CV_WARP_FILL_OUTLIERS, cvScalarAll(0));
            }
        }
    }
    
    /**
     * Sets the region of interest (ROI) of the image.
     *
     * @param image    image to be set
     * @param cropRect region of interest
     */
    private static void crop(IplImage uncropped, CvRect cropRect) {
    	cvSetImageROI(uncropped, cropRect);
    	//Copy original image (only ROI) to the cropped image
    	cvCopy(uncropped, image);
    	cvCopy(uncropped, temp);
    	cvResetImageROI(uncropped);
    }

    /**
     * Filters the image with the median blur. This should remove noise
     *
     * @param image image to be filtered
     */
    private static void filter(IplImage image) {
        medianBlur(image, image, MEDIAN_FILTER_SIZE);
    }

    /**
     * Detects the objects on the image
     *
     * @param image image to be inspected
     * @param temp  temporary image
     */
    private static void detect(IplImage image, IplImage temp) {
        // Convert from BGR to HSV
        cvCvtColor(image, temp, CV_BGR2HSV);
        
        binaryImage = ballEntity.threshold(temp);
        if (VisionGUI.selectedImage == VisionGUI.BALL_INDEX) {
        	// show ball
        	VisionGUI.updateImage(binaryImage);
        }
        ballCentroid = ballEntity.findCentroid(binaryImage);
        
        binaryImage = robotEntity.threshold(temp);
        if (VisionGUI.selectedImage == VisionGUI.ROBOT_INDEX) {
        	// show robots
        	VisionGUI.updateImage(binaryImage);
        }
        robotEntity.detectRobots(temp, binaryImage);
    }

    public static Point ballCentroid() {
		return ballCentroid;
	}

	public static List<Tuple<Point, Point>> yellowRobots() {
		return RobotEntity.yellowRobots();
	}

	public static List<Tuple<Point, Point>> blueRobots() {
		return RobotEntity.blueRobots();
	}

	/**
     * Creates a new IplImage same size as the source image.
     *
     * @param img      source image
     * @param channels number of channels
     * @return newly created image
     */
    public static IplImage newImage(IplImage img, int channels) {
        return IplImage.create(cvGetSize(img), img.depth(), channels);
    }
    
    /**
     * Draws world objects on the shitty pitch.
     * 
     * @param image
     */
    public static void drawShit(IplImage image) {
    	if (ballCentroid != null) {
    		cvCircle(image, ballCentroid.asCV(), 6, cvScalar(0, 0, 255, 0), -1, 8, 0);
    	}
    	
    	CvPoint centroid;
    	Point dotPoint;
    	
    	for (Tuple<Point, Point> robot : blueRobots()) {
    		centroid = robot.getFirst().asCV();
    		cvCircle(image, centroid, 8, cvScalar(255, 0, 0, 0), -1, 8, 0);
    		dotPoint = robot.getSecond();
    		if (dotPoint != null) {
    			cvLine(image, centroid, dotPoint.asCV(), cvScalar(0, 0, 255, 0), 1, 8, 0);
    		}
		}
    	
    	for (Tuple<Point, Point> robot : yellowRobots()) {
    		centroid = robot.getFirst().asCV();
    		cvCircle(image, centroid, 8, cvScalar(0, 255, 255, 0), -1, 8, 0);
    		dotPoint = robot.getSecond();
    		if (dotPoint != null) {
    			cvLine(image, centroid, dotPoint.asCV(), cvScalar(0, 0, 255, 0), 1, 8, 0);
    		}
		}
    }
    
    public static void heightFilter(List<Tuple<Point, Point>> robots) {
    	double cameraHeight;
    	Point pitchCenter;
    	if (MasterController.pitchPlayed == PitchType.MAIN) {
			cameraHeight = Constants.PITCH0_CAMERA_HEIGHT;
			pitchCenter = Constants.PITCH0_CENTER;
		} else {
			cameraHeight = Constants.PITCH1_CAMERA_HEIGHT;
			pitchCenter = Constants.PITCH1_CENTER;
		}
    	for (Tuple<Point, Point> robot : robots) {
	    	adjustByHeight(robot.getFirst(), pitchCenter, cameraHeight);
	    	Point second = robot.getSecond();
	    	if (second != null) {
	    		adjustByHeight(second, pitchCenter, cameraHeight);
	    	}
		}
    }
    
    public static Point adjustByHeight(Point originalPoint, Point pitchCenter, double cameraHeight) {
    	originalPoint.offset(-pitchCenter.x, -pitchCenter.y);
		double ratio = (cameraHeight - Constants.ROBOT_HEIGHT) / cameraHeight;
		originalPoint.mult(ratio);
		originalPoint.offset(pitchCenter.x, pitchCenter.y);
		return originalPoint;
    }

    /**
     * Processes the image.
     *
     * @param inputImage image to be processed
     */
    public static void process(BufferedImage inputImage) {
        uncropped = IplImage.createFrom(inputImage);
//        undistort(uncropped, uncroppedTemp, cameraMatrix, distCoeffs);
        crop(uncropped, cropRect);
        filter(image);
        detect(image, temp);
        if (VisionGUI.selectedImage == VisionGUI.MAIN_INDEX) { 
        	// show main
            if (VisionGUI.drawObjects) {
            	drawShit(image);
            }
        	VisionGUI.updateImage(image);
        }

    }

}
