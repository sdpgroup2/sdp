package sdp.group2.vision;

import static com.googlecode.javacv.cpp.opencv_core.cvCountNonZero;
import static com.googlecode.javacv.cpp.opencv_core.cvInRangeS;
import static com.googlecode.javacv.cpp.opencv_core.cvPoint;
import static com.googlecode.javacv.cpp.opencv_core.cvResetImageROI;
import static com.googlecode.javacv.cpp.opencv_core.cvScalar;
import static com.googlecode.javacv.cpp.opencv_core.cvSetImageROI;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvDilate;
import static sdp.group2.vision.ImageProcessor.newImage;

import java.util.ArrayList;
import java.util.List;

import sdp.group2.geometry.Point;
import sdp.group2.util.Tuple;

import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.IplImage;


public class RobotEntity extends Entity {
	
    private static DotEntity dotEntity = new DotEntity();
    private static List<Tuple<Point, Point>> yellowRobots = new ArrayList<Tuple<Point, Point>>();
    private static List<Tuple<Point, Point>> blueRobots = new ArrayList<Tuple<Point, Point>>();

    int[][] mins = new int[][] {
            new int[] {40, 100, 200}, // base plate min
//            new int[] {19, 107, 155}, // yellow min
//            new int[] {135, 20, 34}, // blue min
//            new int[] {20, 49, 120}, // dot min
    };

    int[][] maxs = new int[][] {
            new int[] {80, 150, 255}, // base plate max
//            new int[] {36, 155, 255}, // yellow max
//            new int[] {200, 50, 70}, // blue max
//            new int[] {57, 89, 160}, // dot max

    };
    
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
        cvInRangeS(hsvImage, cvScalar(mins[0][0], mins[0][1], mins[0][2], 0), cvScalar(maxs[0][0], maxs[0][1], maxs[0][2], 0), binaryImage);
        cvDilate(binaryImage, binaryImage, null, 9);
        return binaryImage;
    }

    public void detectRobots(IplImage hsvImage, IplImage binaryImage) {
    	IplImage binaryTemp = newImage(binaryImage, 1);
    	List<CvRect> rects = boundingBoxes(binaryImage);
    	yellowRobots.clear();
    	blueRobots.clear();
    	for (CvRect rect : rects) {
    		// Set the region of interest so we threshold only part of image
    		cvSetImageROI(hsvImage, rect);
    		cvSetImageROI(binaryTemp, rect);
    		dotEntity.threshold(hsvImage, binaryTemp);
    		
    		// beware the method below could return null
    		// we still add it though
    		Point dotCentroid = dotEntity.findCentroid(binaryTemp);
    		Point rectCentroid = new Point(rect.x() + (rect.width() / 2), rect.y() + (rect.height() / 2));
    		
    		// We check each robot if yellow or blue
    		if (isYellowRobot(hsvImage)) {
    			yellowRobots.add(new Tuple<Point, Point>(rectCentroid, dotCentroid));
    		} else {
    			blueRobots.add(new Tuple<Point, Point>(rectCentroid, dotCentroid));
    		}
    	}
		cvResetImageROI(hsvImage);
		cvResetImageROI(binaryTemp);
    }
    
    private boolean isYellowRobot(IplImage hsvImage) {
    	IplImage channel = newImage(hsvImage, 1);
    	cvInRangeS(hsvImage, cvScalar(20, 100, 100, 0), cvScalar(30, 255, 255, 0), channel);
    	// Yellow robots have non zero about 60
    	return cvCountNonZero(channel) > 10 ? true : false;
    }
    
}
