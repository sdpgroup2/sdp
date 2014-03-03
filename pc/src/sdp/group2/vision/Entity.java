package sdp.group2.vision;

import static com.googlecode.javacv.cpp.opencv_core.CV_WHOLE_SEQ;
import static com.googlecode.javacv.cpp.opencv_core.cvPoint;
import static com.googlecode.javacv.cpp.opencv_core.cvRectangle;
import static com.googlecode.javacv.cpp.opencv_core.cvScalar;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSeqElem;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_CHAIN_APPROX_SIMPLE;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_RETR_LIST;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvBoundingRect;
import static com.googlecode.javacv.cpp.opencv_core.cvGetImageROI;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCanny;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvContourArea;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvFindContours;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvMoments;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import sdp.group2.geometry.Point;

import com.googlecode.javacpp.Loader;
import com.googlecode.javacv.cpp.opencv_core.CvContour;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_imgproc.CvMoments;

public abstract class Entity {
	
	protected CvMemStorage storage = CvMemStorage.create();

	/**
	 * Finds contours in a binary image.
	 * @param binaryImage image to be searched
	 * @return list of contours wrapped in CvSeq
	 */
    public CvSeq findContours(IplImage binaryImage) {
    	CvSeq seq = new CvSeq();
    	cvCanny(binaryImage, binaryImage, 100, 300, 3);
    	cvFindContours(binaryImage, storage, seq, Loader.sizeof(CvContour.class), CV_RETR_LIST, CV_CHAIN_APPROX_SIMPLE);
    	if (seq.isNull() || seq.total() == 0) {
    		return null;
    	}
    	return seq;
    }
    
    /**
     * 
     * @param binaryImage
     * @param outputImage
     * @param rectSize
     */
    public void drawContours(IplImage binaryImage, IplImage outputImage, int rectSize) {
    	CvSeq seq = findContours(binaryImage);
    	for (CvSeq c = seq ; c != null && !c.isNull() ; c = c.h_next()) {
			CvMoments moments = new CvMoments();
			cvMoments(c, moments, 0);
			if (moments.isNull()) {
				continue;
			}
			Point pt = new Point(moments.m10() / moments.m00(), moments.m01() / moments.m00());
			cvRectangle(outputImage, cvPoint((int) pt.x - rectSize, (int) pt.y - rectSize), cvPoint( (int) pt.x + rectSize, (int) pt.y + rectSize), cvScalar(255, 0, 0, 0), 1, 1, 0);
    	}
    }
    
    public List<CvRect> boundingBoxes(IplImage binaryImage) {
    	return boundingBoxes(binaryImage, 0, Integer.MAX_VALUE);
    }
    
    public List<CvRect> boundingBoxes(IplImage binaryImage, int minArea, int maxArea) {
    	List<CvRect> rects = new ArrayList<CvRect>();
    	CvSeq seq = findContours(binaryImage);
    	
    	// At most 4 rects will be returned
    	int count = 0;
    	for (CvSeq c = seq; c != null && !c.isNull() && count < 4; c = c.h_next()) {
    		// Only take it if area is positive - because it works
    		double area = cvContourArea(c, CV_WHOLE_SEQ, 1);
//    		System.out.println(area);
    		if (area < minArea || area > maxArea) {
    			continue;
    		}
    		rects.add(cvBoundingRect(c, 0));
    		count++;
    	}	
    	return rects;
    }
    
    /**
     * Finds a centroid (only one) for a blob in a binary image.
     * Used for the ball and the robot dot.
     * @param binaryImage image to be searched
     * @return the centroid point
     */
    public Point findCentroid(IplImage binaryImage, int areaMin, int areaMax) {
    	List<Point> possibleCentroids = findPossibleCentroids(binaryImage, areaMin, areaMax, Integer.MAX_VALUE);
    	if (possibleCentroids.size() == 0) {
    		return null;
    	} else {
    		// Maybe do some filtering - for now just return first
    		return possibleCentroids.get(0);
    	}
    }
    
    public List<Point> findPossibleCentroids(IplImage binaryImage, int areaMin, int areaMax, int maxCount) {
    	List<Point> possibleCentroids = new ArrayList<Point>();
    	// Get the roi rect to offset the points
		CvMoments moments = new CvMoments();
    	CvRect roiRect = cvGetImageROI(binaryImage);
    	CvSeq seq = findContours(binaryImage);
//    	System.out.println("----------------------");
    	for (CvSeq c = seq; c != null && possibleCentroids.size() < maxCount; c = c.h_next()) {
    		// Only take it if area is positive - because it works
    		double area = cvContourArea(c, CV_WHOLE_SEQ, 1);
//    		System.out.println(area);
    		if (area < areaMin || area > areaMax) {
    			continue;
    		}
			cvMoments(c, moments, 0);
			if (moments.isNull()) {
				continue;
			}
			// This would result in a NaN bread
			if (moments.m10() == 0 || moments.m00() == 0 || moments.m01() == 0) {
				continue;
			}
			Point centroid = new Point(moments.m10() / moments.m00(), moments.m01() / moments.m00());
			// Offset the point so it's relative to the origin
			// and not the ROI
			centroid.offset(roiRect.x(), roiRect.y());
			possibleCentroids.add(centroid);
    	}
//    	System.out.println("----------------------");
    	return possibleCentroids; 
    }
    
    /**
     * Finds a centroid (only one) for a blob in a binary image.
     * Used for the ball and the robot dot.
     * @param binaryImage image to be searched
     * @return the centroid point
     */
    public Point findCentroid(IplImage binaryImage) {
    	return findCentroid(binaryImage, 0, Integer.MAX_VALUE);
    }
    
    public abstract IplImage threshold(IplImage image);

}
