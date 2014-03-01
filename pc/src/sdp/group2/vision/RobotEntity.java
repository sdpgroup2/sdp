package sdp.group2.vision;

import static com.googlecode.javacv.cpp.opencv_core.cvInRangeS;
import static com.googlecode.javacv.cpp.opencv_core.cvOr;
import static com.googlecode.javacv.cpp.opencv_core.cvPoint;
import static com.googlecode.javacv.cpp.opencv_core.cvRect;
import static com.googlecode.javacv.cpp.opencv_core.cvRectangle;
import static com.googlecode.javacv.cpp.opencv_core.cvScalar;
import static com.googlecode.javacv.cpp.opencv_core.cvSetImageROI;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_CHAIN_APPROX_SIMPLE;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_RETR_LIST;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCanny;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvDilate;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvErode;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvFindContours;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvMoments;
import static sdp.group2.vision.ImageProcessor.newImage;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import sdp.group2.geometry.Point;
import sdp.group2.geometry.Vector;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;

import com.googlecode.javacpp.Loader;
import com.googlecode.javacv.cpp.opencv_core.CvContour;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_imgproc.CvMoments;


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
     * TODO: remove image from here
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

    /**
     * 
     * @param binaryImage
     * @return
     */
    public List<CvRect> boundingBoxes(IplImage binaryImage) {
    	List<CvRect> rects = new ArrayList<CvRect>();
    	CvSeq seq = findContours(binaryImage);
    	// At most 4 rects will be returned
    	int count = 0;
    	for (CvSeq c = seq; c != null && !c.isNull() && count < 4; c = c.h_next()) {
    		// Only take it if area is positive - because it works
    		if (cvContourArea(c, CV_WHOLE_SEQ, 1) < 0) {
    			continue;
    		}
    		rects.add(cvBoundingRect(c, 0));
    		count++;
    	}
    	for (int j = 0; j < rects.size(); j++) {
    		CvRect rect = rects.get(j);
    		cvDrawRect(binaryImage, cvPoint(rect.x(), rect.y()),
    				cvPoint(rect.x() + rect.width(), rect.y() + rect.height()),
    				cvScalar(255, 255, 255, 0), 1, 8, 0);
		}
    	return rects;
    }
    
    /**
     * 
     * @param The array of robots rects
     * @param An HSV image
     * @return The array of direction vectors for the robots
     */
    public Vector[] facingVectors(List<CvRect> rects, IplImage image, IplImage outImage) {
    	IplImage outputImage = newImage(image, 1);
    	
    	CvRect roi = cvGetImageROI(image);
    	
    	Vector[] vects = new Vector[4];
    	Point centroid = new Point(0, 0);
    	for (int i = 0; i < rects.size(); i++) {
    		CvRect rect = rects.get(i);
    		cvSetImageROI(image, rect);
    		cvSetImageROI(outputImage, rect);
    		dotEntity.threshold(image, outputImage);
    		cvDilate(outputImage, outputImage, null, 5);
    		CvSeq seq = findContours(outputImage);
        	for (CvSeq c = seq ; c != null && !c.isNull() ; c = c.h_next()) {
    			CvMoments moments = new CvMoments();
    			cvMoments(c, moments, 0);
    			if (moments.isNull()) {
    				continue;
    			}
    			centroid = new Point(moments.m10() / moments.m00(), moments.m01() / moments.m00());
        	}
        	Point rectCenter = new Point(rect.x() + (rect.width()/2.0), rect.y() + (rect.height()/2.0));
        	Vector vector = rectCenter.sub(centroid);
        	vects[i] = vector;
//    		iv.showImage(outputImage, BufferedImage.TYPE_BYTE_INDEXED);
        	cvDrawLine(outImage, cvPoint((int) (rect.x() + centroid.x), (int) (rect.y() + centroid.y)), cvPoint((int) rectCenter.x, (int) rectCenter.y), cvScalar(255, 255, 255, 0), 1, 4, 0);
    	}
    	
    	return vects;
    }
    

    @Override
    public IplImage detect(IplImage[] hsvImages) {
        return null;
    }
    
}
