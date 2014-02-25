package sdp.group2.vision;

import com.googlecode.javacpp.Loader;
import com.googlecode.javacv.cpp.opencv_core.CvContour;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_imgproc.CvMoments;

import sdp.group2.geometry.Point;
import sdp.group2.util.Constants;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static sdp.group2.vision.ImageProcessor.newImage;


public class RobotEntity implements Detectable {
    private Constants.PitchType pitchType;
    private CvMemStorage storage = CvMemStorage.create();

    int[][] mins = new int[][] {
            new int[] {47, 60, 130}, // base plate min
//            new int[] {19, 107, 155}, // yellow min
//            new int[] {135, 20, 34}, // blue min
            new int[] {24, 70, 61}, // dot min
    };

    int[][] maxs = new int[][] {
            new int[] {60, 115, 255}, // base plate max
//            new int[] {36, 155, 255}, // yellow max
//            new int[] {200, 50, 70}, // blue max
            new int[] {53, 125, 111}, // dot max

    };

    public RobotEntity(Constants.PitchType pitchType) {
        this.pitchType = pitchType;
    }

    /**
     * TODO: remove image from here
     * Thresholds the image so that only the wanted colors are white
     * and everything else is black in the resulting image.
     * @param images 3 source 1-channel images
     * @param image just for size
     * @return the resulting image
     */
    public IplImage threshold(IplImage[] images, IplImage image) {
        IplImage result = newImage(image, 1);
        IplImage temp = newImage(image, 1);

        cvInRangeS(image, cvScalar(mins[0][0], mins[0][1], mins[0][2], 0), cvScalar(maxs[0][0], maxs[0][1], maxs[0][2], 0), result);
        cvInRangeS(image, cvScalar(mins[1][0], mins[1][1], mins[1][2], 0), cvScalar(maxs[1][0], maxs[1][1], maxs[1][2], 0), temp);
        cvOr(result, temp, result, null);
//        cvInRangeS(image, cvScalar(mins[2][0], mins[2][1], mins[2][2], 0), cvScalar(maxs[2][0], maxs[2][1], maxs[2][2], 0), temp);
//        cvOr(result, temp, result, null);
//        cvInRangeS(image, cvScalar(mins[3][0], mins[3][1], mins[3][2], 0), cvScalar(maxs[3][0], maxs[3][1], maxs[3][2], 0), temp);
//        cvOr(result, temp, result, null);
        cvErode(result, result, null, 1);
        cvDilate(result, result, null, 11);
        return result;
    }

    @Override
    public IplImage detect(IplImage[] hsvImages) {
        return null;
    }

    @Override
    public Point findBlobs(IplImage binaryImage, int numOfBlobs) {
    	CvSeq seq = new CvSeq();
    	cvCanny(binaryImage, binaryImage, 100, 300, 3);
    	cvFindContours(binaryImage, storage, seq, Loader.sizeof(CvContour.class), CV_RETR_LIST, CV_CHAIN_APPROX_SIMPLE);
    	if (seq.isNull() || seq.total() == 0) {
    		return null;
    	}
    	CvMoments moments = new CvMoments();
    	cvMoments(seq, moments, 0);
    	if (moments.isNull()) {
    		return null;
    	}
    	Point pt = new Point(moments.m10() / moments.m00(), moments.m01() / moments.m00());
    	return pt;
    }
    
    @Override
    public void drawBlobs(IplImage binaryImage, IplImage outputImage, int numOfBlobs) {
    	CvSeq seq = new CvSeq();
    	cvCanny(binaryImage, binaryImage, 300, 300, 3);
    	cvFindContours(binaryImage, storage, seq, Loader.sizeof(CvContour.class), CV_RETR_LIST, CV_CHAIN_APPROX_SIMPLE);
    	if (seq.isNull() || seq.total() == 0) {
    		return;
    	}
    	int count = 0;
    	for (CvSeq c = seq ; c != null && !c.isNull() ; c = c.h_next()) {
    		count++;
			CvMoments moments = new CvMoments();
			cvMoments(c, moments, 0);
			if (moments.isNull()) {
				continue;
			}
			Point pt = new Point(moments.m10() / moments.m00(), moments.m01() / moments.m00());
			cvRectangle(outputImage, cvPoint((int) pt.x - 10, (int) pt.y - 10), cvPoint( (int) pt.x + 10, (int) pt.y + 10), cvScalar(255, 0, 0, 0), 1, 1, 0);
    	}
    	System.out.println(count);
    }
    
}
