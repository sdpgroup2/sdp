package sdp.group2.vision;

import static com.googlecode.javacv.cpp.opencv_core.cvCountNonZero;
import static com.googlecode.javacv.cpp.opencv_core.cvInRangeS;
import static com.googlecode.javacv.cpp.opencv_core.cvLine;
import static com.googlecode.javacv.cpp.opencv_core.cvOr;
import static com.googlecode.javacv.cpp.opencv_core.cvPoint;
import static com.googlecode.javacv.cpp.opencv_core.cvResetImageROI;
import static com.googlecode.javacv.cpp.opencv_core.cvScalar;
import static com.googlecode.javacv.cpp.opencv_core.cvSetImageROI;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvDilate;
import static com.googlecode.javacv.cpp.opencv_core.cvRect;
import static sdp.group2.vision.ImageProcessor.newImage;

import java.util.ArrayList;
import java.util.List;

import sdp.group2.geometry.Point;
import sdp.group2.util.Tuple;

import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.IplImage;


public class RobotEntity extends Entity {
	
    private static DotEntity dotEntity = new DotEntity();
    private ImageViewer iv = new ImageViewer();
    private static List<Tuple<Point, Point>> yellowRobots = new ArrayList<Tuple<Point, Point>>();
    private static List<Tuple<Point, Point>> blueRobots = new ArrayList<Tuple<Point, Point>>();

    // MAIN PITCH
    int[][] mins = new int[][] {
            new int[] {65, 135, 175}, // base plate min
          	new int[] {20, 120, 200}, // yellow min
//          	new int[] {75, 65, 150}, // blue min
    };
    
    int[][] maxs = new int[][] {
            new int[] {80, 204, 250}, // base plate max
      		new int[] {40, 180, 256}, // yellow max
//            new int[] {120, 130, 240}, // blue max
    };
    
    
//    // SIDE PITCH
//    int[][] mins = new int[][] {
//            new int[] {40, 100, 200}, // base plate min
////            new int[] {19, 107, 155}, // yellow min
////            new int[] {135, 20, 34}, // blue min
////            new int[] {20, 49, 120}, // dot min
//    };
//
//    int[][] maxs = new int[][] {
//            new int[] {80, 150, 255}, // base plate max
////            new int[] {36, 155, 255}, // yellow max
////            new int[] {200, 50, 70}, // blue max
////            new int[] {57, 89, 160}, // dot max
//
//    };
    
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
        IplImage tempImage = newImage(hsvImage, 1);
        // Range the bases
        cvInRangeS(hsvImage, cvScalar(mins[0][0], mins[0][1], mins[0][2], 0), cvScalar(maxs[0][0], maxs[0][1], maxs[0][2], 0), binaryImage);
        // Range the yellow or blue
//        cvInRangeS(hsvImage, cvScalar(mins[1][0], mins[1][1], mins[1][2], 0), cvScalar(maxs[1][0], maxs[1][1], maxs[1][2], 0), tempImage);
//        cvOr(binaryImage, tempImage, binaryImage, null);
//        // Range the other one
//        cvInRangeS(hsvImage, cvScalar(mins[2][0], mins[2][1], mins[2][2], 0), cvScalar(maxs[2][0], maxs[2][1], maxs[2][2], 0), tempImage);
//        cvOr(binaryImage, tempImage, binaryImage, null);
        // Measure so we connect to one component
        cvDilate(binaryImage, binaryImage, null, 9);
        return binaryImage;
    }

    public void detectRobots(IplImage hsvImage, IplImage binaryImage) {
    	IplImage binaryTemp = newImage(binaryImage, 1);
    	List<Point> centroids = findPossibleCentroids(binaryImage, 1500, 3000, 4);
//    	System.out.printf("Found %d robots.\n", centroids.size());
    	yellowRobots.clear();
    	blueRobots.clear();
    	for (Point rectCentroid : centroids) {
    		// Set the region of interest so we threshold only part of image
    		CvRect rect = rectFromPoint(rectCentroid, 30, 30);
    		cvSetImageROI(hsvImage, rect);
    		cvSetImageROI(binaryTemp, rect);
    		dotEntity.threshold(hsvImage, binaryTemp);
    		iv.showImage(binaryTemp);
    		
    		// beware the method below could return null
    		// we still add it though
    		Point dotCentroid = dotEntity.findClosestCentroid(binaryTemp, 40, 120, rectCentroid);
    		if (dotCentroid != null) {
    			cvLine(binaryImage, cvPoint((int) dotCentroid.x, (int) dotCentroid.y), cvPoint((int) rectCentroid.x, (int) rectCentroid.y), cvScalar(255, 255, 255, 0), 1, 8, 0);
    		}
    		// We check each robot if yellow or blue
    		if (isYellowRobot(hsvImage)) {
    			yellowRobots.add(new Tuple<Point, Point>(rectCentroid, dotCentroid));
    		} else {
    			blueRobots.add(new Tuple<Point, Point>(rectCentroid, dotCentroid));
    		}
    	}
		cvResetImageROI(hsvImage);
    }
    
    public CvRect rectFromPoint(Point pt, int width, int height) {
    	return cvRect((int) pt.x - (width / 2), (int) pt.y - (height / 2), width, height);
    }
    
    private boolean isYellowRobot(IplImage hsvImage) {
    	IplImage channel = newImage(hsvImage, 1);
    	cvInRangeS(hsvImage, cvScalar(20, 100, 100, 0), cvScalar(30, 255, 255, 0), channel);
    	// Yellow robots have non zero about 60
    	int nonZero = cvCountNonZero(channel);
    	System.out.println(nonZero);
    	return nonZero > 50 ? true : false;
    }
    
}
