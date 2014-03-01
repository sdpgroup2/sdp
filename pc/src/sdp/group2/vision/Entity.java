package sdp.group2.vision;

import static com.googlecode.javacv.cpp.opencv_core.cvPoint;
import static com.googlecode.javacv.cpp.opencv_core.cvRectangle;
import static com.googlecode.javacv.cpp.opencv_core.cvScalar;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_CHAIN_APPROX_SIMPLE;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_RETR_LIST;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCanny;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvFindContours;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvMoments;
import sdp.group2.geometry.Point;

import com.googlecode.javacpp.Loader;
import com.googlecode.javacv.cpp.opencv_core.CvContour;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_imgproc.CvMoments;

public abstract class Entity implements Detectable {
	
	protected CvMemStorage storage = CvMemStorage.create();

    @Override
    public CvSeq findContours(IplImage binaryImage) {
    	CvSeq seq = new CvSeq();
    	cvCanny(binaryImage, binaryImage, 100, 300, 3);
    	cvFindContours(binaryImage, storage, seq, Loader.sizeof(CvContour.class), CV_RETR_LIST, CV_CHAIN_APPROX_SIMPLE);
    	if (seq.isNull() || seq.total() == 0) {
    		return null;
    	}
    	return seq;
    }
    
    @Override
    public void drawContours(IplImage binaryImage, IplImage outputImage, int rectSize) {
    	CvSeq seq = findContours(binaryImage);
    	for (CvSeq c = seq ; c != null && !c.isNull() ; c = c.h_next()) {
			CvMoments moments = new CvMoments();
			cvMoments(c, moments, 0);
			if (moments.isNull()) {
				continue;
			}
			Point pt = new Point(moments.m10() / moments.m00(), moments.m01() / moments.m00());
			// System.out.println("Object point: " + pt);
			cvRectangle(outputImage, cvPoint((int) pt.x - rectSize, (int) pt.y - rectSize), cvPoint( (int) pt.x + rectSize, (int) pt.y + rectSize), cvScalar(255, 0, 0, 0), 1, 1, 0);
    	}
    }

	@Override
	public abstract IplImage detect(IplImage[] hsvImages);

}
