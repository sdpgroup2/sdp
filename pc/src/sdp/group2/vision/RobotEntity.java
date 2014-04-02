package sdp.group2.vision;

import static com.googlecode.javacv.cpp.opencv_core.cvInRangeS;
import static com.googlecode.javacv.cpp.opencv_core.cvPoint;
import static com.googlecode.javacv.cpp.opencv_core.cvRect;
import static com.googlecode.javacv.cpp.opencv_core.cvCircle;
import static com.googlecode.javacv.cpp.opencv_core.cvCopy;
import static com.googlecode.javacv.cpp.opencv_core.cvResetImageROI;
import static com.googlecode.javacv.cpp.opencv_core.cvScalar;
import static com.googlecode.javacv.cpp.opencv_core.cvSetImageROI;
import static com.googlecode.javacv.cpp.opencv_core.cvSize;
import static com.googlecode.javacv.cpp.opencv_core.cvSet;
import static com.googlecode.javacv.cpp.opencv_core.cvAnd;
import static com.googlecode.javacv.cpp.opencv_imgproc.BORDER_DEFAULT;
import static com.googlecode.javacv.cpp.opencv_imgproc.blur;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvDilate;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvErode;
import static sdp.group2.vision.ImageProcessor.newImage;
import sdp.group2.pc.MasterController;

import java.util.ArrayList;
import java.util.List;

import sdp.group2.geometry.Point;
import sdp.group2.gui.VisionGUI;
import sdp.group2.pc.MasterController;
import sdp.group2.util.Tuple;
import sdp.group2.util.Constants;

import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.IplImage;


public class RobotEntity extends Entity {
	
    private static DotEntity dotEntity = new DotEntity();
    private static List<Tuple<Point, Point>> yellowRobots = new ArrayList<Tuple<Point, Point>>();
    private static List<Tuple<Point, Point>> blueRobots = new ArrayList<Tuple<Point, Point>>();
//    private static IplImage lowerLightMask;
//    private static IplImage upperLightMask;
    
//    static {
//    	CvRect cropRect = Thresholds.activeThresholds.cropRect;
//    	lowerLightMask = IplImage.create(cvSize(cropRect.width(), cropRect.height()), 8, 1);
//    	upperLightMask = IplImage.create(cvSize(cropRect.width(), cropRect.height()), 8, 1);
//    	
//    	// Set lower value for all regions
//    	cvSet(lowerLightMask, cvScalar(100, 0, 0, 0));
//    	// Set the rightmost region to an even lower value (light balance)
//    	cvSetImageROI(lowerLightMask, cvRect(420, 0, 123, 300));
//    	cvSet(lowerLightMask, cvScalar(90, 0, 0, 0));
//    	cvResetImageROI(lowerLightMask);
//
//    	// Set upper value for all regions
//    	cvSet(upperLightMask, cvScalar(130, 0, 0, 0));
//    	// Set the rightmost region to an even lower value (light balance)
//    	cvSetImageROI(upperLightMask, cvRect(420, 0, 123, 300));
//    	cvSet(upperLightMask, cvScalar(110, 0, 0, 0));
//    	cvResetImageROI(upperLightMask);
//    }
    
    public static List<Tuple<Point, Point>> yellowRobots() {
		return yellowRobots;
	}

	public static List<Tuple<Point, Point>> blueRobots() {
		return blueRobots;
	}

	/**
     * Thresholds the image so that only the wanted colours are white
     * and everything else is black in the resulting image.
     * 
     * @param images 3 source 1-channel images
     * @param hsvImage just for size
     * @return the resulting image
     */
    public IplImage threshold(IplImage hsvImage) {
        IplImage binaryImage = newImage(hsvImage, 1);
        // Range the bases
        int[] mins = Thresholds.activeThresholds.basePlateMins;
        int[] maxs = Thresholds.activeThresholds.basePlateMaxs;
        cvInRangeS(hsvImage, cvScalar(mins[0], mins[1], mins[2], 0), cvScalar(maxs[0], maxs[1], maxs[2], 0), binaryImage);
        cvErode(binaryImage, binaryImage, null, 3);
        cvDilate(binaryImage, binaryImage, null, 10);
        return binaryImage;
    }
    
//    public IplImage threshold(IplImage rgbImage) {
//    	// when using this, set the areas to 1500 and 2500
//        IplImage greyImage = newImage(rgbImage, 1);
//        IplImage binaryTemp = newImage(rgbImage, 1);
//        cvSplit(rgbImage, null, greyImage, null, null);
//        cvInRange(greyImage, lowerLightMask, upperLightMask, binaryTemp);
//        
//        cvErode(binaryTemp, binaryTemp, null, 1);
//        cvDilate(binaryTemp, binaryTemp, null, 4);
//    	
//        return binaryTemp;
//    }
    
    public void detectRobots(IplImage hsvImage, IplImage binaryImage) {
    	IplImage binaryTemp = newImage(binaryImage, 1);
    	IplImage binaryCircles = newImage(binaryImage, 1);
    	IplImage binaryDisplay;
    	cvSet(binaryCircles, cvScalar(0, 0, 0, 0));
    	List<Point> centroids = findPossibleCentroids(binaryImage, 1000, 3000, 4);
//    	System.out.printf("Found %d robots.\n", centroids.size());
    	yellowRobots.clear();
    	blueRobots.clear();
    	for (Point rectCentroid : centroids) {
    		cvCircle(binaryCircles, rectCentroid.asCV(), 13, cvScalar(255,255,255,0), -1, 8, 0);
    	}
    	dotEntity.threshold(hsvImage, binaryTemp);
    	cvAnd(binaryTemp, binaryCircles, binaryTemp, null);
    	binaryDisplay = newImage(binaryTemp, 1);
    	cvCopy(binaryTemp, binaryDisplay);
    	List<Point> possibleCentroids = findPossibleCentroids(binaryTemp, 10, 150, Integer.MAX_VALUE);
    	for (Point rectCentroid : centroids) {
    		Point dotCentroid = dotEntity.findClosestCentroid(possibleCentroids, rectCentroid);
    		// Probably should put all this in a different method.
    		Point mmRobot = new Point(rectCentroid).toMillis();
    		int zone = MasterController.getZoneOfPoint(mmRobot);
    		if (dotCentroid != null) {
	    		Point mmDot = new Point(dotCentroid).toMillis();
	    		int dotZone = MasterController.getZoneOfPoint(mmDot);
	    		if (zone != dotZone) {
	    			dotCentroid = null;
	    		}
    		}
    		if (isYellowRobot(zone)) {
    			yellowRobots.add(new Tuple<Point, Point>(rectCentroid, dotCentroid));
    		} else {
    			blueRobots.add(new Tuple<Point, Point>(rectCentroid, dotCentroid));
    		}
    	}
    	if (VisionGUI.selectedImage == VisionGUI.DOT_INDEX) {
    		VisionGUI.updateImage(binaryDisplay);
    	}
    }
    
    public CvRect rectFromPoint(Point pt, int width, int height) {
    	return cvRect((int) pt.x - (width / 2), (int) pt.y - (height / 2), width, height);
    }
    
    private boolean isYellowRobot(int zone) {
    	boolean leftIsYellow = (
    			(MasterController.ourSide == Constants.TeamSide.LEFT &&
    			 MasterController.ourTeam == Constants.TeamColour.YELLOW) ||
    			(MasterController.ourSide == Constants.TeamSide.RIGHT &&
    			 MasterController.ourTeam == Constants.TeamColour.BLUE));
    	if (zone == 0 || zone == 2) {
    		return leftIsYellow;
    	} else {
    		return !leftIsYellow;
    	}
    }
    
}
