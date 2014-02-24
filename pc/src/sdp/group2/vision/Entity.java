package sdp.group2.vision;

import static com.googlecode.javacv.cpp.opencv_core.cvAnd;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_CHAIN_APPROX_SIMPLE;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_RETR_LIST;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_THRESH_BINARY;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_THRESH_BINARY_INV;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvErode;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvFindContours;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvMoments;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvThreshold;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_core.*;

import java.awt.Color;
import java.awt.image.BufferedImage;

import sdp.group2.geometry.Point;
import sdp.group2.util.Debug;

import com.googlecode.javacpp.Loader;
import com.googlecode.javacv.cpp.opencv_core.CvContour;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_imgproc.CvMoments;

public class Entity {

    private int[] maxs;
    private int[] mins;
    private CvMemStorage storage = CvMemStorage.create();

    public Entity(int[] mins, int[] maxs) {
    	this.mins = mins;
    	this.maxs = maxs;
    }
    
    public Point threshold(IplImage[] images, IplImage image, CvRect cropRect) {
        // For each pixel property (hue, saturation or value), find the pixels that lie
        // between the entity limits for that property.
        IplImage channel = ImageProcessor.newImage(image, 1);
        IplImage temp1 = ImageProcessor.newImage(image, 1);
        
        for (int i = 0; i < 3 ; ++i) {
//            cvSetImageROI(images[i], cropRect);
//            cvSetImageROI(channel, cropRect);
//            cvSetImageROI(temp1, cropRect);
                
			IplImage hsvi = images[i];
			
			int min = mins[i];
			int max = maxs[i];
			
			// TODO: maybe use inRange here
			if (i == 0) {
			    cvThreshold(hsvi, channel, min, 255.0, CV_THRESH_BINARY);
			} else {
				cvThreshold(hsvi, temp1, min, 255.0, CV_THRESH_BINARY);
			    cvAnd(channel, temp1, channel, null);
			}
			
			cvThreshold(hsvi, temp1, max, 255.0, CV_THRESH_BINARY_INV);
			cvAnd(channel, temp1, channel, null);
        }
        
        // Erode here to remove all the small white pixel chunks
        cvErode(channel, channel, null, 1);
        
        CvSeq contours = new CvSeq(null);
//        cvCanny(channel, channel, 100, 100, 3);
        cvFindContours(channel, storage, contours, Loader.sizeof(CvContour.class), CV_RETR_LIST, CV_CHAIN_APPROX_SIMPLE);
        if (contours.isNull() || contours.total() == 0) {
        	return null;
        }
        CvSeq firstContour = contours.h_next();
        CvMoments moments = new CvMoments();
        cvMoments(firstContour, moments, 0);
        Point point = new Point(moments.m10() / moments.m00(), moments.m01() / moments.m00());
        System.out.println(point);
        return point;
    }

}
