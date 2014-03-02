package sdp.group2.vision;

import static com.googlecode.javacv.cpp.opencv_core.CV_WHOLE_SEQ;
import static com.googlecode.javacv.cpp.opencv_core.cvInRangeS;
import static com.googlecode.javacv.cpp.opencv_core.cvScalar;
import static com.googlecode.javacv.cpp.opencv_core.cvSetImageROI;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvBoundingRect;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvContourArea;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvDilate;
import static sdp.group2.vision.ImageProcessor.newImage;

import java.util.ArrayList;
import java.util.List;

import sdp.group2.util.Tuple;

import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;


public class RobotEntity extends Entity {
	
    private static DotEntity dotEntity = new DotEntity();
    private ImageViewer iv = new ImageViewer();

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

    /**
     * Thresholds the image so that only the wanted colors are white
     * and everything else is black in the resulting image.
     * @param images 3 source 1-channel images
     * @param image just for size
     * @return the resulting image
     */
    public IplImage threshold(IplImage image) {
        IplImage result = newImage(image, 1);
        cvInRangeS(image, cvScalar(mins[0][0], mins[0][1], mins[0][2], 0), cvScalar(maxs[0][0], maxs[0][1], maxs[0][2], 0), result);
        cvDilate(result, result, null, 9);
        return result;
    }

    public List<Tuple<CvRect, CvPoint>> detectRobots(IplImage binaryImage) {
    	List<CvRect> rects = boundingBoxes(binaryImage);
    	List<Tuple<CvRect, CvPoint>> rectPointTuples = new ArrayList<Tuple<CvRect, CvPoint>>();
    	for (CvRect rect : rects) {    		
    		// Set the region of interest so we threshold only part of image
    		cvSetImageROI(binaryImage, rect);
    		cvSetImageROI(binaryImage, rect);
    		dotEntity.threshold(binaryImage, binaryImage);
    		
    		CvPoint dotCentroid = dotEntity.findCentroid(binaryImage);
    		// beware the method above could return null
        	rectPointTuples.add(new Tuple<CvRect, CvPoint>(rect, dotCentroid));
    	}
    	return rectPointTuples;
    }
    
    private boolean isYellowRobot(IplImage binaryImage, CvRect rect) {
    	return true;
    }
    
    public Tuple<List<Tuple<CvRect, CvPoint>>, List<Tuple<CvRect, CvPoint>>> categoriseRobots(List<Tuple<CvRect, CvPoint>> robotTuples) {
    	List<Tuple<CvRect, CvPoint>> yellowTuples = new ArrayList<Tuple<CvRect, CvPoint>>();
    	
//    	for (Tuple<CvRect,CvPoint> tuple : robotTuples) {
//    		if (tuple.getFirst())
//		}
		return null;
    	
    }
    
}
